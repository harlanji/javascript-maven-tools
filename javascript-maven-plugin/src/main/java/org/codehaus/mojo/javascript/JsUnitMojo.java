package org.codehaus.mojo.javascript;

/*
 * Copyright 2001-2005 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;

import junit.framework.TestSuite;
import net.jsunit.Configuration;
import net.jsunit.ConfigurationException;
import net.jsunit.JsUnitServer;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.surefire.junit.JUnitTestSet;
import org.apache.maven.surefire.report.FileReporter;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.ReporterManager;
import org.apache.maven.surefire.testset.AbstractTestSet;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;
import org.mortbay.util.MultiException;

/**
 * Goal which runs javascript tests using jsunit framework. Tests can be writter
 * either inside an html page, as documented by jsunit, or simply as javascript.
 * 
 * @goal jsunit
 * @phase test
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class JsUnitMojo
    extends AbstractJavascriptMojo
{
    /**
     * Set this to 'true' to bypass unit tests entirely. Its use is NOT
     * RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.test.skip}"
     */
    private boolean skip;

    /**
     * Set this to true to ignore a failure during testing. Its use is NOT
     * RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.test.failure.ignore}"
     */
    private boolean testFailureIgnore;

    /**
     * Base directory where all reports are written to.
     * 
     * @parameter expression="${project.build.directory}/surefire-reports"
     */
    private File reportsDirectory;

    /**
     * Base directory where jsunit will run.
     * 
     * @parameter expression="${project.build.directory}/test-scripts"
     */
    private File workDirectory;

    /**
     * Base directory for jsunit test.
     * 
     * @parameter expression="${basedir}/src/test/javascript"
     */
    private File testSourceDirectory;

    /**
     * Browsers to run the jsunit tests
     * 
     * @parameter
     */
    private String[] browsers;

    private String[] DEFAULT_BROWSERS = { "firefox" };

    /**
     * The local port to use for the jsunit HTTP server
     * 
     * @parameter default-value="8080"
     */
    private int port;

    /**
     * Exclusion pattern.
     * 
     * @parameter
     */
    private String[] excludes;

    /**
     * Inclusion pattern.
     * 
     * @parameter
     */
    private String[] includes;

    private static final String[] DEFAULT_INCLUDES = { "**/*.html", "**/*.htm", "**/*.js" };
    
    
    /**
     * @parameter expression="${javascript.jsunit.firefox.path}" default-value="c:/program files/Mozilla Firefox/firefox.exe"
     */
    private String firefoxPath;
    
    /**
     * @parameter expression="${javascript.jsunit.ie.path}" default-value="c:/program files/internet explorer/iexplore.exe"
     */    
    private String iePath;

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if ( skip )
        {
            getLog().warn( "tests are skipped." );
            return;
        }

        String[] tests = getTestsToRun();

		if ( tests == null || tests.length == 0 )
		{
			getLog().info( "no jsunit tests to run." );
			return;
		}

        unpackJavascriptDependency( "net.jsunit:jsunit-testRunner", workDirectory );

        if ( browsers == null )
        {
            browsers = DEFAULT_BROWSERS;
        }

        for ( int i = 0; i < browsers.length; i++ )
        {
            if ( new File( browsers[i] ).exists() )
            {
                continue;
            }
            if ( "firefox".equalsIgnoreCase( browsers[i] ) )
            {
                browsers[i] = firefoxPath;
            }
            if ( "iexplorer".equalsIgnoreCase( browsers[i] ) )
            {
                browsers[i] = iePath;
            }
        }

        JsUnitServer server = new JsUnitServer();
        try
        {
            runJsUnitTests( server, tests );
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Failed to run JsUnit tests", e );
        }
        finally
        {
            try
            {
                server.stop();
            }
            catch ( InterruptedException e )
            {
                // Ignore;
            }
        }
    }

    private void runJsUnitTests( JsUnitServer server, String[] tests )
        throws Exception
    {
        setupServer( server );

        Reporter reporter = new FileReporter( reportsDirectory, Boolean.FALSE );
        ReporterManager reportManager = new ReporterManager( Collections.singletonList( reporter ) );
        ReportEntry report = new ReportEntry( this, "jsunit", "test Starting" );
        reportManager.testSetStarting( report );

        for ( int i = 0; i < tests.length; i++ )
        {
            String test = tests[i];
            String path = workDirectory.toURI().getPath();
            String name = test.substring( 0, test.lastIndexOf( '.' ) );
            if ( test.toLowerCase().endsWith( ".js" ) )
            {
                test = buildMinimalHtml( test );
            }
            server.setTestURL( new URL( "file://" + path
                + "testRunner.html?autoRun=true&submitresults=true" + "&resultid=TEST-" + name
                + "&testPage=" + path.substring( 1 ) + test ) );

            JsUnitTestCase.setSuite( new TestSuite( JsUnitTestCase.class, name ) );
            AbstractTestSet testSet = new JUnitTestSet( JsUnitTestCase.class );

            testSet.execute( reportManager, getClass().getClassLoader() );
        }

        report = new ReportEntry( this, "jsunit", "test Completed" );
        reportManager.testSetCompleted( report );
        checkFailure( reportManager );
    }

    /**
     * @param test
     * @return
     * @throws IOException
     */
    private String buildMinimalHtml( String test )
        throws IOException
    {
        String name = test.substring( 0, test.lastIndexOf( '.' ) );
        String html = name + ".html";
        File file = new File( workDirectory, html );
        file.getParentFile().mkdirs();

        test = test.replace( '\\', '/' );
        String basedir = "";
        int i = 0;
        while ( ( i = test.indexOf( "/" ) ) > 0 )
        {
            test = test.substring( i );
            basedir += "../";
        }
        if ( basedir.trim().length() == 0 )
        {
            basedir = ".";
        }

        Writer w = new FileWriter( file );
        w.write( "<html>\n" );
        w.write( "<head>\n" );
        w.write( "<script type='text/javascript' src='" + basedir
            + "/app/jsUnitCore.js'></script>\n" );
        w.write( "<script type='text/javascript' src='" + test + "'></script>\n" );
        w.write( "</head>\n" );
        w.write( "<body>\n" );
        w.write( "</body>\n" );
        w.write( "</html>\n" );

        IOUtil.close( w );
        return html;
    }

    private String[] getTestsToRun()
    {
		if ( !testSourceDirectory.exists() )
		{
			return null;
		}
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( testSourceDirectory );
        scanner.setExcludes( excludes );
        scanner.addDefaultExcludes();
        if ( includes == null )
        {
            includes = DEFAULT_INCLUDES;
        }
        scanner.setIncludes( includes );
        scanner.scan();
        String[] tests = scanner.getIncludedFiles();
        return tests;
    }

    private void checkFailure( ReporterManager reportManager )
        throws MojoFailureException
    {
        if ( reportManager.getNumErrors() + reportManager.getNumFailures() > 0 )
        {
            String msg =
                "There are test failures.\n\nPlease refer to " + reportsDirectory
                    + " for the individual test results.";
            if ( testFailureIgnore )
            {
                getLog().error( msg );
            }
            else
            {
                throw new MojoFailureException( msg );
            }
        }
    }

    private void setupServer( JsUnitServer server )
        throws ConfigurationException, MultiException
    {
        System.setProperty( Configuration.URL, "http://localhost:" + port + "/jsunit/" );
        System.setProperty( Configuration.PORT, String.valueOf( port ) );
        System.setProperty( Configuration.LOGS_DIRECTORY, reportsDirectory.getAbsolutePath() );
        server.initialize();
        server.setResourceBase( workDirectory );
        server.setLocalBrowserFileNames( Arrays.asList( browsers ) );
        server.start();
        JsUnitTestCase.setSharedServer( server );
    }
}
