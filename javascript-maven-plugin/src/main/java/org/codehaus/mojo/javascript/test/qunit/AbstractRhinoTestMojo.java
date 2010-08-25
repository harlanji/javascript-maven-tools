package org.codehaus.mojo.javascript.test.qunit;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.AbstractMojo;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.*;
import java.util.*;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.surefire.report.FileReporter;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.report.XMLReporter;
import org.codehaus.mojo.javascript.AbstractJavascriptMojo;
import org.codehaus.mojo.javascript.archive.Types;
import org.codehaus.plexus.util.FileUtils;


/**
 *
 * @author harlan
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
	 * @parameter expression="${project.build.directory}/surefire-reports"
	 */
	File reportsDirectory;
	/**
	 * Base directory where jsunit will run.
	 *
	 * @parameter expression="${project.build.directory}/test-scripts"
	 */
	File suiteDirectory;

    /**
     * The output directory of the assembled js file.
     *
     * @parameter default-value="${project.build.outputDirectory}"
     */
    protected File outputDirectory;

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

		File outputDirectory = new File(project.getBuild().getOutputDirectory());

		if(workDirectory == null) {
			workDirectory = FileUtils.createTempFile("test", "work", outputDirectory);
			workDirectory.deleteOnExit();
		} else if(workDirectory.exists()) {
			throw new MojoFailureException("workDirectory [" + workDirectory + "] already exists, and it should not.");
		} else {
			workDirectory.mkdirs();
		}

		// copy scripts to work directory
		try {
			FileUtils.copyDirectoryStructure(outputDirectory, workDirectory);
		} catch (IOException ex) {
			throw new MojoFailureException("Could not create workDirectory from suiteDirectory [" + suiteDirectory + "]");
		}
		
		// copy test suite to work directory
		try {
			FileUtils.copyDirectoryStructure(suiteDirectory, workDirectory);
		} catch (IOException ex) {
			throw new MojoFailureException("Could not create workDirectory from suiteDirectory [" + suiteDirectory + "]");
		}

		File libDirectory = new File(workDirectory, "lib");
		libDirectory.mkdirs();


		// unpack test suite dependencies
		unpackJavascriptDependency("com.devspan.vendor.envjs:envjs-rhino", libDirectory, true);
		unpackJavascriptDependency("com.devspan.vendor.jquery:qunit", libDirectory, true);
		unpackJavascriptDependency("com.devspan.vendor.jquery:jquery", libDirectory, true);

		try {
			// unpack project dependencies
			javascriptArtifactManager.unpack(project, DefaultArtifact.SCOPE_TEST, libDirectory, true);
		} catch (ArchiverException ex) {
			throw new MojoFailureException("Could not unpack dependencies");
		}


		for (String suiteName : suites) {
			getLog().info("Running suite: " + suiteName);
			File suite = new File(workDirectory, suiteName);


			ReporterManager reporterManager = new ReporterManager(
					Arrays.asList(new Reporter[]{
						new FileReporter(reportsDirectory, Boolean.FALSE),
						new XMLReporter(reportsDirectory, Boolean.FALSE)
					}));

			ReportCallbacks reportCb = new ReportCallbacks(reporterManager);

			final RhinoRuntime rt;
			try {
				rt = createRhinoRuntime(reportCb);
			} catch(Exception e) {
				throw new MojoExecutionException("Could not create the Rhino runtime.", e);
			}

			try {
				runSuite(rt, suite);
			} catch(Exception e) {
				throw new MojoExecutionException("Error running test suite '"+suite.getAbsolutePath()+"'.", e);
			}

			checkFailure(reporterManager);
		}

	}

	/**
	 * Creates a RhinoRuntime with env.js and inserts a $report object that
	 * collects test results as they are run. 
	 * 
	 * @return
	 * @throws Exception
	 */
	protected RhinoRuntime createRhinoRuntime(ReportCallbacks reportCb) throws Exception {
		RhinoRuntime rt = new RhinoRuntime();

		// Put our reporting callbacks in there
		rt.putGlobal("$report", reportCb);


		// Establish window scope with dom and all imported and inline scripts executed
		//rt.execClasspathScript("env.rhino.js");
		rt.execScriptFile(new File(workDirectory, "lib/envjs-rhino/env.rhino.js"));

		return rt;
	}

	protected abstract void runSuite(RhinoRuntime rt, File suite) throws Exception;

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

	private void checkFailure(ReporterManager reportManager)
			throws MojoFailureException {
		if (reportManager.getNumErrors() + reportManager.getNumFailures() > 0) {
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
}
