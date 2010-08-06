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
import java.util.Collections;


import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.surefire.report.FileReporter;
import org.apache.maven.surefire.report.ReportEntry;
import org.apache.maven.surefire.report.Reporter;
import org.apache.maven.surefire.report.ReporterManager;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.IOUtil;

/**
 * Goal which runs javascript tests using jsunit framework. Tests can be writter
 * either inside an html page, as documented by jsunit, or simply as javascript.
 * 
 * @goal qunit
 * @phase test
 * @author Harlan Iverson
 */
public class QUnitEnvMojo
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

    private static final String[] DEFAULT_INCLUDES = { "**/*.js" };
    

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

        //unpackJavascriptDependency( "net.jsunit:jsunit-testRunner", workDirectory );

        try
        {
            runQUnitTests( tests );
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Failed to run QUnit tests", e );
        }
        finally
        {
  
        }
    }

    private void runQUnitTests( String[] tests )
        throws Exception
    {
       

        Reporter reporter = new FileReporter( reportsDirectory, Boolean.FALSE );
        ReporterManager reportManager = new ReporterManager( Collections.singletonList( reporter ) );
        ReportEntry report = new ReportEntry( this, "jsunit", "test Starting" );
        reportManager.testSetStarting( report );

        for ( int i = 0; i < tests.length; i++ )
        {
            String test = tests[i];
            //String path = workDirectory.toURI().getPath();
            //String name = test.substring( 0, test.lastIndexOf( '.' ) );
            //if ( test.toLowerCase().endsWith( ".js" ) )
            //{
                //test = buildMinimalHtml( test );
            //}
            /*server.setTestURL( new URL( "file://" + path
                + "testRunner.html?autoRun=true&submitresults=true" + "&resultid=TEST-" + name
                + "&testPage=" + path.substring( 1 ) + test ) );*/

            //JsUnitTestCase.setSuite( new TestSuite( JsUnitTestCase.class, name ) );
            //AbstractTestSet testSet = new JUnitTestSet( JsUnitTestCase.class );

            //testSet.execute( reportManager, getClass().getClassLoader() );

			ReportEntry testReport = new ReportEntry(this, test, "Started");


			reportManager.testStarting(testReport);
			reportManager.testSucceeded(testReport);



			//reportManager.testStarting(new ReportEntry(this, test, "Started"));
			//reportManager.testSucceeded(new ReportEntry(this, test, "Succeeded"));
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
}
