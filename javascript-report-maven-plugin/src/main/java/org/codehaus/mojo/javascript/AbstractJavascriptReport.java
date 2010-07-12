package org.codehaus.mojo.javascript;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.doxia.site.renderer.SiteRenderer;
import org.codehaus.mojo.javascript.archive.JavascriptArtifactManager;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;

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
 * Abstract report for running Javascript-based report tools
 *
 * @author <a href="mailto:nicolas.deloof@gmail.com">nicolas De Loof</a>
 * @phase site
 */
public abstract class AbstractJavascriptReport
    extends AbstractMavenReport
{

    /**
     * template for running a script with rhino
     */
    private RhinoTemplate rhino = new RhinoTemplate();

    /**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     * @required
     */
    private SiteRenderer siteRenderer;

    /**
     * The output directory of the jsdoc report.
     *
     * @parameter expression="${project.reporting.outputDirectory}"
     * @required
     * @readonly
     */
    protected File outputDirectory;

    /**
     * {@inheritDoc}
     *
     * @see org.apache.maven.reporting.AbstractMavenReport#getOutputDirectory()
     */
    protected String getOutputDirectory()
    {
        return outputDirectory.getAbsolutePath() + "/" + getName();
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.maven.reporting.AbstractMavenReport#getSiteRenderer()
     */
    protected SiteRenderer getSiteRenderer()
    {
        return siteRenderer;
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.maven.reporting.AbstractMavenReport#getProject()
     */
    protected MavenProject getProject()
    {
        return project;
    }

    /**
     * Map of of plugin artifacts.
     *
     * @parameter expression="${plugin.artifactMap}"
     * @required
     * @readonly
     */
    private Map pluginArtifactMap;

    /**
     * @component
     */
    private JavascriptArtifactManager javascriptArtifactManager;

    /**
     * The jsdoc working directory
     *
     * @parameter default-value="${project.build.directory}"
     */
    private File workDirectory;

    /**
     * Unpack a javascript dependency
     */
    protected void unpackJavascriptDependency( String artifact, File dest )
        throws MavenReportException
    {
        if ( !pluginArtifactMap.containsKey( artifact ) )
        {
            throw new MavenReportException( "Failed to resolve dependency " + artifact
                + " required by the plugin" );
        }
        Artifact javascript = (Artifact) pluginArtifactMap.get( artifact );
        try
        {
            javascriptArtifactManager.unpack( javascript, dest );
        }
        catch ( ArchiverException e )
        {
            throw new MavenReportException( "Failed to unpack javascript dependency", e );
        }
    }

    protected String[] getScripts( File sourceDirectory )
    {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( sourceDirectory );
        scanner.addDefaultExcludes();
        scanner.setIncludes( new String[] { "**/*.js" } );
        scanner.scan();
        return scanner.getIncludedFiles();
    }

    /**
     * @param script script to execute
     * @param args script arguments
     * @param context Map<String,Object> of arguments to pass to the script
     * @throws IOException failure...
     */
    protected Object evalScript( String script, String[] args, Map context )
        throws MavenReportException
    {
        return evalScript( new File[0], null, args, context );
    }

    protected Object evalScript( File script, String cmd, String[] args, Map context )
        throws MavenReportException
    {
        return evalScript( new File[] { script }, cmd, args, context );
    }

    protected Object evalScript( File script, String[] args, Map context )
        throws MavenReportException
    {
        return evalScript( new File[] { script }, null, args, context );
    }

    protected Object evalScript( File[] scripts, String[] args, Map context )
        throws MavenReportException
    {
        return evalScript( scripts, null, args, context );
    }

    /**
     * @param script script to execute
     * @param args script arguments
     * @param context Map<String,Object> of arguments to pass to the script
     * @throws IOException failure...
     */
    protected Object evalScript( final File[] scripts, final String cmd, String[] args, Map context )
        throws MavenReportException
    {
        try
        {
            return rhino.evalScript( context, args, new RhinoCallBack()
            {
                public Object doWithContext( Context ctx, Scriptable scope )
                    throws IOException
                {
                    Object result = null;
                    for ( int i = 0; i < scripts.length; i++ )
                    {
                        FileReader reader = new FileReader( scripts[i] );
                        result = ctx.evaluateReader( scope, reader, scripts[i].getName(), 1, null );
                    }
                    if ( cmd != null )
                    {
                        return ctx.evaluateString( scope, cmd, getName(), 1, null );
                    }
                    return result;
                }
            } );
        }
        catch ( Exception e )
        {
            throw new MavenReportException( "Failed to execute script", e );
        }
    }

    protected void copyScripts( File source, File dest )
        throws IOException
    {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( source );
        scanner.addDefaultExcludes();
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        for ( int i = 0; i < files.length; i++ )
        {
            FileUtils.copyFileToDirectory( new File( source, files[i] ), dest );
        }
    }

    protected abstract String getName();

    public ResourceBundle getBundle( Locale locale )
    {
        return ResourceBundle.getBundle( "javascript-report-maven-plugin", locale );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.maven.reporting.MavenReport#getDescription(java.util.Locale)
     */
    public String getDescription( Locale locale )
    {
        return getBundle( locale ).getString( getName() + ".description" );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.maven.reporting.MavenReport#getName(java.util.Locale)
     */
    public String getName( Locale locale )
    {
        return getBundle( locale ).getString( getName() + ".name" );
    }

    /**
     * {@inheritDoc}
     *
     * @see org.apache.maven.reporting.MavenReport#getOutputName()
     */
    public String getOutputName()
    {
        return getName();
    }

    /**
     * @return the workDirectory
     */
    public File getWorkDirectory()
    {
        return new File( workDirectory, getName() );
    }

    /**
     * Execute Rhino
     *
     * @see http://lxr.mozilla.org/mozilla/source/js/rhino/examples/RunScript.java
     */
    private class RhinoTemplate
    {
        public Object evalScript( Map context, String[] args, RhinoCallBack callback )
            throws Exception
        {
            getLog().info( "Running javascript for " + getName() );

            // Creates and enters a Context. The Context stores information
            // about the execution environment of a script.
            Context ctx = Context.enter();
            ctx.setLanguageVersion( getLanguageVersion() );
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

    private interface RhinoCallBack
    {
        Object doWithContext( Context ctx, Scriptable scope )
            throws IOException;
    }

    /**
     * @return
     */
    protected int getLanguageVersion()
    {
        return Context.VERSION_1_6;
    }
}
