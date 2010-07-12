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

import java.util.Iterator;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import net.jsunit.JsUnitServer;
import net.jsunit.StandaloneTest;
import net.jsunit.TestCaseResult;
import net.jsunit.TestSuiteResult;

/**
 * Simple wrapper arround StandaloneTest to setup the jetty http server from
 * Maven.
 * 
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class JsUnitTestCase
    extends StandaloneTest
{
    /**
     * @param name
     */
    public JsUnitTestCase( String name )
    {
        super( name );
    }

    private static JsUnitServer sharedServer;

    /**
     * @param server the server to set
     */
    public static void setSharedServer( JsUnitServer server )
    {
        JsUnitTestCase.sharedServer = server;
    }

    /**
     * {@inheritDoc}
     * 
     * @see net.jsunit.StandaloneTest#setUp()
     */
    public void setUp()
        throws Exception
    {
        setServer( sharedServer );
        super.setUp();
    }

    /**
     * Override the junit run method to gather browser test result an run a fake
     * junit test suite that replay the browser results.
     * 
     * @author Arnaud Bailly.
     * @see junit.framework.TestCase#run(junit.framework.TestResult)
     */
    public void run( TestResult result )
    {
        super.run( new TestResult() );
        TestSuiteResult suiteResult = JsUnitTestCase.sharedServer.lastResult();
        if (suiteResult == null )
        {
            return;
        }
        for ( Iterator it = suiteResult.getTestCaseResults().iterator(); it.hasNext(); )
        {
            final TestCaseResult testCaseResult = (TestCaseResult) it.next();
            Test fake = new Test()
            {

                public void run( TestResult result )
                {
                }

                public int countTestCases()
                {
                    return 1;
                }

                public String toString()
                {
                    return testCaseResult.getName();
                }
            };
            result.startTest( fake );
            if ( testCaseResult.hadError() )
            {
                result.addError( fake, new Exception( testCaseResult.getError() ) );
            }
            if ( testCaseResult.hadFailure() )
            {
                result.addFailure( fake, new AssertionFailedError( testCaseResult.getFailure() ) );
            }
            result.endTest( fake );
        }
    }

    private static TestSuite suite;

    public static Test suite()
    {
        return suite;
    }

    /**
     * @param suite the suite to set
     */
    public static void setSuite( TestSuite suite )
    {
        JsUnitTestCase.suite = suite;
    }

}
