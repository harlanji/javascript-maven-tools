package org.codehaus.mojo.javascript.test.qunit;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.AbstractMojo;
import org.codehaus.plexus.util.DirectoryScanner;

import java.io.*;
import java.util.*;
import org.apache.maven.surefire.report.FileReporter;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.report.XMLReporter;
import org.codehaus.mojo.javascript.AbstractJavascriptMojo;


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
	File workDirectory;
	/**
	 * Base directory for jsunit test.
	 *
	 * @parameter expression="${basedir}/src/test/javascript"
	 */
	File testSourceDirectory;
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
	private static final String[] DEFAULT_INCLUDES = {"**/*.html", "**/*.htm", "**/*.js"};

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

		workDirectory.mkdirs();
		reportsDirectory.mkdirs();


		unpackJavascriptDependency( "net.jsunit:jsunit-testRunner", workDirectory );

		for (String suiteName : suites) {
			getLog().info("Running suite: " + suiteName);
			File suite = new File(testSourceDirectory, suiteName);


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
		rt.execClasspathScript("env.rhino.js");

		return rt;
	}

	protected abstract void runSuite(RhinoRuntime rt, File suite) throws Exception;

	private String[] getTestsToRun() {
		if (!testSourceDirectory.exists()) {
			return new String[0];
		}
		DirectoryScanner scanner = new DirectoryScanner();
		scanner.setBasedir(testSourceDirectory);
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