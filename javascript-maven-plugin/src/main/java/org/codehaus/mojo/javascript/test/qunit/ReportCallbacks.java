package org.codehaus.mojo.javascript.test.qunit;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.ReporterManager;

public class ReportCallbacks {

	protected static final String DEFAULT_MODULE = "default";
	protected static final String DEFAULT_TEST = "no test";


	protected String currentModule = DEFAULT_MODULE;
	protected String currentTest = DEFAULT_TEST;

	protected final ReporterManager reportManager;

	public ReportCallbacks(ReporterManager reportManager) {
		this.reportManager = reportManager;
	}

	public void log(Boolean result, String message) {
		if(!result.booleanValue()) {
			ReportEntry entry = new ReportEntry(this, getTestName(), message);
			reportManager.testFailed(entry);
		} else {
			reportManager.writeMessage("log: " + message);
		}
	}

	public void moduleStart(String name, Object testEnvironment) {
		currentModule = name;
	}

	public void moduleDone(String name, Long failures, Long total) {
		currentModule = DEFAULT_MODULE;
	}

	public void testStart(String name, Object testEnvironment) {
		currentTest = name;

		ReportEntry entry = new ReportEntry(this, getTestName(), "Test Started");
		reportManager.testStarting(entry);

	}

	public void testDone(String name, Long failures, Long total) {
		ReportEntry entry = new ReportEntry(this, getTestName(), "Test Done");


		if(failures > 0) {
			reportManager.testFailed(entry);
		} else {
			reportManager.testSucceeded(entry);
		}

		currentTest = DEFAULT_TEST;
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
