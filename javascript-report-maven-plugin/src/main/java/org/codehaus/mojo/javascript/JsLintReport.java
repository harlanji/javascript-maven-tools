package org.codehaus.mojo.javascript;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.sink.Sink;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;

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

/**
 * Generate JsLint code analysis report
 * 
 * @goal jslint
 * @phase site
 * @author <a href="mailto:nicolas.deloof@gmail.com">nicolas De Loof</a>
 */
public class JsLintReport
    extends AbstractJavascriptReport
{

    /**
     * Location of the source files.
     * 
     * @parameter default-value="${basedir}/src/main/javascript"
     */
    private File sourceDirectory;

    /**
     * @parameter
     */
    private boolean stopOnFirstError;

    /**
     * @parameter
     */
    private boolean strictWhitespace;

    /**
     * @parameter
     */
    private boolean assumeBrowser;

    /**
     * @parameter
     */
    private boolean assumeYahooWidget;

    /**
     * @parameter
     */
    private boolean assumeRhino;

    /**
     * @parameter
     */
    private boolean tolerateDebuggerStatements;

    /**
     * @parameter
     */
    private boolean tolerateEval;

    /**
     * @parameter
     */
    private boolean tolerateHtmlCase;

    /**
     * @parameter
     */
    private boolean tolerateHtmlEventHandlers;

    /**
     * @parameter
     */
    private boolean tolerateHtmlFragments;

    /**
     * @parameter
     */
    private boolean tolerateSloppyLineBreaking;

    /**
     * @parameter
     */
    private boolean tolerateUnfilteredForIn;

    /**
     * @parameter
     */
    private boolean disallowUndefinedVariables;

    /**
     * @parameter
     */
    private boolean disallowLeadingUnderscoreInIdentifiers;

    /**
     * @parameter
     */
    private boolean disallowEqualEqualandNotEqual;

    /**
     * @parameter
     */
    private boolean disallowPlusPlusandMinusMinus;

    /**
     * @parameter
     */
    private boolean disallowBitwiseOperators;

    /**
     * @parameter
     */
    private boolean aDsafe;

    /**
     * Source code encoding
     * 
     * @parameter
     */
    private String encoding = "UTF-8";

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.maven.reporting.AbstractMavenReport#executeReport(java.util.Locale)
     * @see http://code.google.com/p/jsdoc-toolkit/wiki/CmdlineOptions
     */
    protected void executeReport( Locale locale )
        throws MavenReportException
    {
        File workDirectory = getWorkDirectory();
        unpackJavascriptDependency( "com.jslint:jslint", workDirectory );

        File jslint = new File( workDirectory, "fulljslint.js" );
        List args = new ArrayList();
        Map context = new HashMap();
        StringBuffer options = new StringBuffer( "{" );
        options.append( "\n  adsafe : " ).append( aDsafe ? "true" : "false" );
        options.append( ",\n bitwise : " ).append( disallowBitwiseOperators ? "true" : "false" );
        options.append( ",\n browser : " ).append( assumeBrowser ? "true" : "false" );
        options.append( ",\n cap : " ).append( tolerateHtmlCase ? "true" : "false" );
        options.append( ",\n debug : " ).append( tolerateDebuggerStatements ? "true" : "false" );
        options.append( ",\n eqeqeq : " ).append( disallowEqualEqualandNotEqual ? "true" : "false" );
        options.append( ",\n evil : " ).append( tolerateEval ? "true" : "false" );
        options.append( ",\n forin : " ).append( tolerateUnfilteredForIn ? "true" : "false" );
        options.append( ",\n fragment : " ).append( tolerateHtmlFragments ? "true" : "false" );
        options.append( ",\n laxbreak : " ).append( tolerateSloppyLineBreaking ? "true" : "false" );
        options.append( ",\n nomen : " ).append(
            disallowLeadingUnderscoreInIdentifiers ? "true" : "false" );
        options.append( ",\n on : " ).append( tolerateHtmlEventHandlers ? "true" : "false" );
        options.append( ",\n passfail : " ).append( stopOnFirstError ? "true" : "false" );
        options.append( ",\n plusplus : " ).append(
            disallowPlusPlusandMinusMinus ? "true" : "false" );
        options.append( ",\n rhino : true " );
        options.append( ",\n undef : " ).append( disallowUndefinedVariables ? "true" : "false" );
        options.append( ",\n white : " ).append( strictWhitespace ? "true" : "false" );
        options.append( ",\n widget : " ).append( assumeYahooWidget ? "true" : "false" );
        options.append( "\n}" );

        getLog().debug( "options : " + options );

        String[] scripts = getScripts( sourceDirectory );
        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text( "JsLint Javascript Verifier report" );
        sink.title_();
        sink.head_();
        sink.body();
        for ( int i = 0; i < scripts.length; i++ )
        {
            sink.section3();
            sink.sectionTitle3();
            sink.text( scripts[i] );
            sink.sectionTitle3_();
            sink.paragraph();
            try
            {
                String file = new File( sourceDirectory, scripts[i] ).toURL().toString();
                getLog().debug( "Generate jslint report for " + file );
                String script =
                    "JSLINT( readUrl( '" + file + "', '" + encoding + "' ), " + options
                        + " ); JSLINT.errors;";
                NativeArray errors =
                    (NativeArray) evalScript( jslint, script, (String[]) args
                        .toArray( new String[0] ), context );
                if ( errors == null || errors.getLength() == 0 )
                {
                    sink.text( getBundle( locale ).getString( getName() + ".no-error" ) );
                }
                else
                {
                    sink.table();
                    sink.tableRow();
                    sink.tableHeaderCell();
                    sink.text( "position" );
                    sink.tableHeaderCell_();
                    sink.tableHeaderCell();
                    sink.text( "error" );
                    sink.tableHeaderCell_();
                    sink.tableHeaderCell();
                    sink.text( "code" );
                    sink.tableHeaderCell_();
                    sink.tableRow_();

                    long length = errors.getLength();
                    for ( int j = 0; j < length; j++ )
                    {
                        NativeObject object = (NativeObject) errors.get( j, null );
                        sink.tableRow();
                        sink.tableCell();
                        Double line = (Double) object.get( "line", null );
                        Double character = (Double) object.get( "character", null );
                        sink.text( line.longValue() + ":" + character.longValue() );
                        sink.tableCell_();
                        sink.tableCell();
                        String reason = String.valueOf( object.get( "reason", null ) );
                        sink.text( reason );
                        sink.tableCell_();
                        sink.tableCell();
                        String evidence = String.valueOf( object.get( "evidence", null ) );
                        sink.rawText( evidence );
                        sink.tableCell_();
                        sink.tableRow_();
                    }
                    sink.table_();
                }
                // sink.rawText( report.toString() );
            }
            catch ( Exception e )
            {
                getLog().warn( "Error generating report for " + scripts[i], e );
                sink.text( "Error generating report for " + scripts[i] );
            }
            sink.paragraph_();
            sink.section3_();
        }
        sink.body_();
        sink.flush();
        sink.close();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.maven.reporting.AbstractMavenReport#canGenerateReport()
     */
    public boolean canGenerateReport()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractJavascriptReport#getName()
     */
    protected String getName()
    {
        return "jslint";
    }
}
