package org.codehaus.mojo.javascript.test.qunit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.*;
import java.util.*;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.surefire.report.FileReporter;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.report.RunStatistics;
import org.apache.maven.surefire.report.XMLReporter;
import org.codehaus.mojo.javascript.AbstractJavascriptMojo;
import org.codehaus.plexus.util.FileUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;


/**
 *
 * @author harlan
 * @requiresDependencyResolution test
 */
public abstract class AbstractRhinoTestMojo extends AbstractJavascriptMojo {

	/**
	 * Set this to 'true' to bypass unit tests entirely. Its use is NOT
	 * RECOMMENDED, but quite convenient on occasion.
	 *
	 * @parameter expression="${maven.test.skip}"
	 */
	boolean skip;
	/**
	 * Set this to true to ignore a failure during testing. Its use is NOT
	 * RECOMMENDED, but quite convenient on occasion.
	 *
	 * @parameter expression="${maven.test.failure.ignore}"
	 */
	boolean testFailureIgnore;
	/**
	 * Base directory where all reports are written to.
	 *
	 * @parameter expression="${project.build.directory}/surefire"
	 */
	File reportsDirectory;


    /**
     * The output directory of the assembled js file.
     *
     * @parameter default-value="${project.build.directory}"
     */
    protected File targetDirectory;

	/**
	 * Base directory where jsunit will run.
	 *
	 * @parameter expression="${project.build.directory}/test-scripts"
	 */
	File suiteDirectory;

    /**
     * The output directory of the assembled js file.
     *
     * @parameter default-value="${project.build.directory}/scripts"
     */
    protected File outputDirectory;


    /**
     * The output directory of the assembled js file.
     *
     * @parameter default-value="scripts"
     */
    protected String scriptsDirectory;

    /**
     * The output directory of the assembled js file.
     *
     * @parameter default-value="lib"
     */
    protected String libsDirectory;

	/**
	 * Base directory where jsunit will run.
	 *
	 * @parameter 
	 */
	File workDirectory;

	/**
	 * Exclusion pattern.
	 *
	 * @parameter
	 */
	String[] excludes;
	/**
	 * Inclusion pattern.
	 *
	 * @parameter
	 */
	String[] includes;

	private static final String[] DEFAULT_INCLUDES = {"**/suite-*.html"};

	public void execute() throws MojoExecutionException, MojoFailureException {
		if (skip) {
			getLog().warn("tests are skipped.");
			return;
		}


		String[] suites = getTestsToRun();

		if (suites.length == 0) {
			getLog().info("No tests to run.");
			return;
		}

		reportsDirectory.mkdirs();

		if(workDirectory == null) {
			getLog().debug("Creating temp working directory");
			workDirectory = FileUtils.createTempFile("test", "work", targetDirectory);
			workDirectory.deleteOnExit();
		} else if(workDirectory.exists()) {
			throw new MojoFailureException("workDirectory [" + workDirectory + "] already exists, and it should not.");
		} else {
			getLog().debug("Creating specified working directory");
			workDirectory.mkdirs();
		}

		getLog().debug("Copying source to working directory");

		// TODO this code should be shared with the war package code
		//		that copies scripts and libs.
		File scriptsDirectory = new File(workDirectory, this.scriptsDirectory);


		// copy scripts to work directory
		try {
			scriptsDirectory.mkdirs();

			FileUtils.copyDirectoryStructure(outputDirectory, scriptsDirectory);
		} catch (IOException ex) {
			throw new MojoFailureException("Could not create workDirectory from suiteDirectory [" + suiteDirectory + "]");
		}

		getLog().debug("Copying test source to working directory");
		
		// copy test suite to work directory
		try {
			FileUtils.copyDirectoryStructure(suiteDirectory, workDirectory);
		} catch (IOException ex) {
			throw new MojoFailureException("Could not create workDirectory from suiteDirectory [" + suiteDirectory + "]");
		}

		File libsDirectory = new File(scriptsDirectory, this.libsDirectory);
		libsDirectory.mkdirs();

		getLog().debug("Unpacking dependencies");

		try {
			// unpack project dependencies
			javascriptArtifactManager.unpack(project, DefaultArtifact.SCOPE_TEST, libsDirectory, true);
		} catch (ArchiverException ex) {
			throw new MojoFailureException("Could not unpack dependencies");
		}


		for (String suiteName : suites) {
			getLog().info("Running suite: " + suiteName);
			File suite = new File(workDirectory, suiteName);
      
      RunStatistics runStats = new RunStatistics();

			ReporterManager reporterManager = new ReporterManager(
					Arrays.asList(new Reporter[]{
						new FileReporter(reportsDirectory, Boolean.FALSE),
						new XMLReporter(reportsDirectory, Boolean.FALSE)
					}), runStats);

			ReportCallbacks reportCb = new ReportCallbacks(reporterManager, getLog());



			try {
				runSuite(suite, reportCb);
			} catch(Exception e) {
				throw new MojoExecutionException("Error running test suite '"+suite.getAbsolutePath()+"'.", e);
			}

			getLog().debug("Finished suite: " + suiteName);

			checkFailure(runStats);
		}

		getLog().debug("Finished running all tests.");

	}


	protected abstract void runSuite(File suite, ReportCallbacks reportCb) throws Exception;

	private String[] getTestsToRun() {
		if (!suiteDirectory.exists()) {
			getLog().warn("No suite directory exists");
			return new String[0];
		}
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(suiteDirectory);
		scanner.setExcludes(excludes);
		scanner.addDefaultExcludes();
		if (includes == null) {
			includes = DEFAULT_INCLUDES;
		}
		scanner.setIncludes(includes);
		scanner.scan();
		String[] tests = scanner.getIncludedFiles();
		return tests;
	}

	private void checkFailure(RunStatistics runStats)
			throws MojoFailureException {
		if (runStats.hadErrors() || runStats.hadFailures()) {
			String msg =
					"There are test failures.\n\nPlease refer to " + reportsDirectory
					+ " for the individual test results.";
			if (testFailureIgnore) {
				getLog().error(msg);
			} else {
				throw new MojoFailureException(msg);
			}
		}
	}
  
  
    protected class RhinoTemplate
    {
        public Object evalScript( Map context, String[] args, RhinoCallBack callback )
            throws Exception
        {

            // Creates and enters a Context. The Context stores information
            // about the execution environment of a script.
            Context ctx = Context.enter();
            ctx.setLanguageVersion( getLanguageVersion() );
            ctx.setOptimizationLevel(-1);
            ctx.initStandardObjects();
            try
            {
                // Initialize the standard objects (Object, Function, etc.)
                // This must be done before scripts can be executed. Returns
                // a scope object that we use in later calls.

                // Use a "Golbal" scope to allow use of importClass in scripts
                Global scope = new Global();
                scope.init( ctx );

                if ( context != null )
                {
                    for ( Iterator iterator = context.entrySet().iterator(); iterator.hasNext(); )
                    {
                        Map.Entry entry = (Map.Entry) iterator.next();
                        Scriptable jsObject = Context.toObject( entry.getValue(), scope );
                        String key = (String) entry.getKey();
                        getLog().debug(
                            "set object available to javascript " + key + "=" + jsObject );
                        scope.put( key, scope, jsObject );
                    }
                }
                if ( args != null )
                {
                    scope.defineProperty( "arguments", args, ScriptableObject.DONTENUM );
                }
                return callback.doWithContext( ctx, scope );
            }
            finally
            {
                Context.exit();
            }
        }

    }

    protected interface RhinoCallBack
    {
        Object doWithContext( Context ctx, Scriptable scope )
            throws IOException;
    }
    
    
    protected int getLanguageVersion()
    {
        return Context.VERSION_1_6;
    }
  
}
