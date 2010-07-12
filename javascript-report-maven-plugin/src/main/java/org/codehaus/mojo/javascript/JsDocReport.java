package org.codehaus.mojo.javascript;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.maven.reporting.MavenReportException;

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
 * Generate JsDoc report
 *
 * @goal jsdoc
 * @phase site
 * @author <a href="mailto:nicolas.deloof@gmail.com">nicolas De Loof</a>
 */
public class JsDocReport
    extends AbstractJavascriptReport
{

    /**
     * Location of the source files.
     *
     * @parameter default-value="${basedir}/src/main/javascript"
     */
    protected File sourceDirectory;

    /**
     * By default this will collect all source files to a depth of 10 folders
     * deep, but you can specify your preferred depth
     *
     * @parameter default-value="10"
     */
    private int recurse;

    /**
     * Source code encoding
     *
     * @parameter
     */
    private String encoding = "UTF-8";

    /**
     * Include symbols tagged as private ?
     *
     * @parameter
     */
    private boolean includePrivate;

    /**
     * Include all functions, even undocumented ones ?
     *
     * @parameter
     */
    private boolean includeUndocumented;

    /**
     * Include all functions, even undocumented, underscored ones ?
     *
     * @parameter
     */
    private boolean includeUnderscore;

    /**
     * JsDoc template to be used
     *
     * @parameter default-value="sunny"
     */
    private String template;

    /**
     * JsDoc custom template to be used (overrides template)
     *
     * @parameter
     */
    private File customTemplate;

    public boolean isExternalReport()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.maven.reporting.MavenReport#getOutputName()
     */
    public String getOutputName()
    {
        return "jsdoc/index";
    }

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
        unpackJavascriptDependency( "org.jsdoctoolkit:jsdoc", workDirectory );

        File script = new File( workDirectory.getAbsolutePath(), "/app/run.js" );
        List args = new ArrayList();
        if ( customTemplate == null )
        {
            args.add( "-t=" + workDirectory.getAbsolutePath() + "/templates/" + template );
        }
        else
        {
            args.add( "-t=" + customTemplate.getAbsolutePath() );
        }
        args.add( "-d=" + getOutputDirectory() );
        if ( includeUndocumented )
        {
            // --allfunctions : Include all functions, even undocumented ones.
            args.add( "-a" );
        }
        if ( includeUnderscore )
        {
            // --Allfunctions : Include all functions, even undocumented,
            // underscored ones.
            args.add( "-A" );
        }
        if ( includePrivate )
        {
            // --private : Include symbols tagged as private and inner symbols.
            args.add( "-p" );
        }
        args.add( "-r=" + recurse );

        // --directory=<PATH> : Output to this directory (defaults to "out").
        args.add( "-d=" + getOutputDirectory() );

        // --encoding=<ENCODING> : Use this encoding to read and write files.
        args.add( "-e=" + encoding );

        args.add( "-o=" + workDirectory.getAbsolutePath() + "/jsdoc.log" );
        args.add( sourceDirectory.getAbsolutePath() );

        // Required to resolve relative path bug in jsdoc 2.0-beta :
		// absolute working directory is not well detected
		// http://code.google.com/p/jsdoc-toolkit/issues/detail?id=116
        String baseDir = getProject().getBasedir().getAbsolutePath();
        if ( baseDir.startsWith( "/" ) )
        {
            args.add( "-j=" + script.getAbsolutePath() );
        }
        else
        {
            String path = script.getAbsolutePath().substring( baseDir.length() + 1 );
            args.add( "-j=" + path );
        }

        System.setProperty( "jsdoc.dir", workDirectory.getAbsolutePath() );
        evalScript( script, (String[]) args.toArray( new String[0] ), null );
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
        return "jsdoc";
    }
}
