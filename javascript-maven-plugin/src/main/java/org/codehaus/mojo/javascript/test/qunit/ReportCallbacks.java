package org.codehaus.mojo.javascript.test.qunit;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.report.StackTraceWriter;

public class ReportCallbacks {

	protected static final String DEFAULT_MODULE = "default";
	protected static final String DEFAULT_TEST = "no test";


	protected String currentModule = DEFAULT_MODULE;
	protected String currentTest = DEFAULT_TEST;

	protected final ReporterManager reportManager;
	protected final Log log;

	public ReportCallbacks(ReporterManager reportManager, Log log) {
		this.reportManager = reportManager;
		this.log = log;
	}

	public void log(Boolean result, final String message) {
		if(!result.booleanValue()) {
			ReportEntry entry = new ReportEntry(this, getTestName(), message, new StackTraceWriter() {
				public Throwable getThrowable() {
					return new Exception(message);
				}

				public String writeTraceToString() {
					return message;
				}

				public String writeTrimmedTraceToString() {
					return message;
				}
			});
			reportManager.testFailed(entry);
		} else {
			reportManager.writeMessage("log: " + message);
		}
	}

	public void moduleStart(String name, Object testEnvironment) {
		currentModule = name;

		log.debug("QUnit - Starting Module: " + name);
	}

	public void moduleDone(String name, Long failures, Long total) {
		currentModule = DEFAULT_MODULE;

		log.debug("QUnit - Finished Module: " + name);
	}

	public void testStart(String name, Object testEnvironment) {
		currentTest = name;

		ReportEntry entry = new ReportEntry(this, getTestName(), "Test Started");
		reportManager.testStarting(entry);

		log.debug("QUnit - Starting Test: " + name);

	}

	public void testDone(String name, Long failures, Long total) {
		ReportEntry entry = new ReportEntry(this, getTestName(), "Test Done");


		if(failures > 0) {
			// This is taken care of in log failure.
			//reportManager.testFailed(entry);
		} else {
			reportManager.testSucceeded(entry);
		}

		currentTest = DEFAULT_TEST;

		log.debug("QUnit - Finished Test: " + name);
	}

	// before any tests start.
	public void begin() throws Exception {
		//reportManager.runStarting(0);

		ReportEntry entry = new ReportEntry(this, "qunit", "QUnit Starting");
		reportManager.testSetStarting(entry);
	}

	public void done(Long failures, Long total) throws Exception {
		//reportManager.runCompleted();

		ReportEntry entry = new ReportEntry(this, "qunit", "QUnit Done");
		reportManager.testSetCompleted(entry);

		log.debug("QUnit - All finished. ");
	}

	private String getTestName() {
		String name = "";

		if(!currentModule.equals(DEFAULT_MODULE)) {
			name += currentModule + " : ";
		}
		name += currentTest;

		return  name;
	}
}
