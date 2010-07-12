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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.metadata.ArtifactMetadataSource;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.resolver.ArtifactResolutionResult;
import org.apache.maven.artifact.resolver.ArtifactResolver;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.compress.CompressionException;
import org.codehaus.mojo.javascript.compress.IsolatedClassLoader;
import org.codehaus.mojo.javascript.compress.JSCompressor;
import org.codehaus.mojo.javascript.compress.JSCompressorProxy;
import org.codehaus.mojo.javascript.compress.JSMinCompressor;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * Abstact mojo for compressing JavaScripts.
 * 
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 */
public abstract class AbstractCompressMojo
    extends AbstractMojo
{

    /**
     * Resolves the artifacts and dependencies.
     * 
     * @component
     */
    private ArtifactResolver artifactResolver;

    /**
     * Create Artifact references
     * 
     * @component
     */
    private ArtifactFactory artifactFactory;

    /**
     * The local repository
     * 
     * @parameter expression="${localRepository}"
     * @required
     */
    private ArtifactRepository localRepository;

    /**
     * For retrieval of artifact's metadata.
     * 
     * @component
     */
    private ArtifactMetadataSource metadataSource;

    /**
     * The remote repositories declared in the pom.
     * 
     * @parameter expression="${project.pluginArtifactRepositories}"
     */
    private List remoteRepositories;

    /**
     *
     */
    private static final NumberFormat INTEGER = NumberFormat.getIntegerInstance();

    private static final String HR = StringUtils.rightPad( "", 78, "-" );

    /**
     * The maven project we are working on.
     * 
     * @parameter default-value="${project}"
     * @required
     * @readonly
     */
    MavenProject project;

    /**
     * The available compressors
     */
    private Map compressors = new HashMap();
    {
        compressors.put( "jsmin", new JSMinCompressor() );
    }

    /**
     * Optimization level, from 0 to 9
     * 
     * @parameter default-value="9"
     */
    private int optimizationLevel;

    /**
     * JS Language version (130 for JS 1.3)
     * 
     * @parameter default-value="130"
     */
    private int languageVersion;

    /**
     * The compressor to used. Either "shrinksafe", "yahooui" or "jsmin" for default compressor, 
	 * or a custom one provided as an artifact in repo org.codehaus.mojo.javascript:<xxx>-compressor.
     * 
     * @parameter default-value="jsmin"
     */
    private String compressor;

    /**
     * Don't display compression stats
     * 
     * @parameter
     */
    private boolean skipStats;

    private static final String[] DEFAULT_INCLUDES = new String[] { "**/*.js" };

    /**
     * Inclusion patterns
     * 
     * @parameter
     */
    private String[] includes;

    /**
     * Exclusion patterns
     * 
     * @parameter
     */
    private String[] excludes;

    /**
     * A special token to recognize lines to be removed from scripts (debugging
     * code).
     * 
     * @parameter
     */
    private String strip;

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.maven.plugin.AbstractMojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( getSourceDirectory() );
        if ( includes == null )
        {
            includes = DEFAULT_INCLUDES;
        }
        scanner.setIncludes( includes );
        scanner.addDefaultExcludes();
        if ( excludes != null )
        {
            scanner.setExcludes( excludes );
        }
        scanner.scan();
        String[] files = scanner.getIncludedFiles();

        // if ( !Context.isValidOptimizationLevel( optimizationLevel ) )
        // {
        // throw new MojoExecutionException( "optimizationLevel is invalid" );
        // }
        // if ( !Context.isValidLanguageVersion( languageVersion ) )
        // {
        // throw new MojoExecutionException( "languageVersion is invalid" );
        // }

        JSCompressor jscompressor = getCompressor();

        logStats( HR );
        getOutputDirectory().mkdirs();
        long saved = 0;
        for ( int i = 0; i < files.length; i++ )
        {
            String file = files[i];
            saved = compress( jscompressor, file );
        }
        logStats( HR );
        logStats( "compression saved " + INTEGER.format( saved ) + " bytes" );
    }

    private File stripDebugs( File file )
        throws MojoExecutionException
    {
        if ( strip == null )
        {
            return file;
        }
        try
        {

            File stripped = File.createTempFile( "stripped", ".js" );

            BufferedReader reader = new BufferedReader( new FileReader( file ) );
            PrintWriter writer = new PrintWriter( stripped );
            String line;
            while ( ( line = reader.readLine() ) != null )
            {
                if ( !line.trim().startsWith( strip ) )
                {
                    writer.println( line );
                }
            }
            IOUtil.close( reader );
            IOUtil.close( writer );

            FileUtils.copyFile( stripped, file );
            stripped.delete();
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Failed to strip debug code in " + file, e );
        }
        return file;
    }

    private JSCompressor getCompressor()
        throws MojoExecutionException
    {
        if ( compressors.containsKey( compressor ) )
        {
            return (JSCompressor) compressors.get( compressor );
        }

        // Inspired by the surefire plugin
        // allows to use multiple compressor that rely on modifier Rhino engines
        // without dependencies/classpath conflicts

        String id = compressor.toLowerCase() + "-compressor";

        // TODO don't have version hardcoded
        Artifact compressorArtifact =
            artifactFactory.createDependencyArtifact( "org.codehaus.mojo.javascript", id,
                VersionRange.createFromVersion( "1.0-alpha-1-SNAPSHOT" ), "jar", null,
                Artifact.SCOPE_RUNTIME );

        Artifact originatingArtifact =
            artifactFactory.createBuildArtifact( "dummy", "dummy", "1.0", "jar" );

        ArtifactResolutionResult dependencies;
        try
        {
            dependencies =
                artifactResolver.resolveTransitively( Collections.singleton( compressorArtifact ),
                    originatingArtifact, localRepository, remoteRepositories, metadataSource, null );
        }
        catch ( Exception e )
        {
            getLog().info( "Failed to load compressor artifact " + compressorArtifact.toString() );
            throw new MojoExecutionException( "Failed to load compressor artifact"
                + compressorArtifact.toString(), e );
        }

        IsolatedClassLoader classLoader = new IsolatedClassLoader( dependencies.getArtifacts() );

        compressor = StringUtils.capitalize( compressor );
        String compressorClassName =
            "org.codehaus.mojo.javascript.compress." + compressor + "Compressor";
        Class compressorClass;
        try
        {
            compressorClass = classLoader.loadClass( compressorClassName );
        }
        catch ( ClassNotFoundException e )
        {
            getLog().info( "Failed to load compressor class " + compressorClassName );
            throw new MojoExecutionException( "Failed to load compressor class"
                + compressorClassName, e );
        }

        JSCompressor jscompressor;
        try
        {
            jscompressor = new JSCompressorProxy( compressorClass.newInstance() );
        }
        catch ( Exception e )
        {
            getLog().info(
                "Failed to create a isolated-classloader proxy for " + compressorClassName );
            throw new MojoExecutionException( "Failed to create a isolated-classloader proxy for "
                + compressorClassName, e );
        }
        getLog().info( "Compressing javascript using " + compressor );

        compressors.put( compressor, jscompressor );
        return jscompressor;
    }

    private long compress( JSCompressor jscompressor, String file )
        throws MojoExecutionException
    {
        String name = file;
        if ( getExtension() != null )
        {
            int ext = file.lastIndexOf( '.' );
            name = file.substring( 0, ext ) + "-" + getExtension() + file.substring( ext );
        }
        File compressed = new File( getOutputDirectory(), name );
        compressed.getParentFile().mkdirs();
        File in = new File( getSourceDirectory(), file );
        if ( in.equals( compressed ) )
        {
            try
            {
                File temp = File.createTempFile( "compress", ".js" );
                long size = compress( in, temp, jscompressor );
                FileUtils.copyFile( temp, compressed );
                temp.delete();
                return size;
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Error creating temp file for compression", e );
            }
        }
        else
        {
            return compress( in, compressed, jscompressor );
        }
    }

    private long compress( File in, File compressed, JSCompressor jscompressor )
        throws MojoExecutionException
    {
        if ( in.length() > 0 )
        {
            File stripped = stripDebugs( in );
            try
            {
                jscompressor.compress( stripped, compressed, optimizationLevel, languageVersion );
            }
            catch ( CompressionException e )
            {
                throw new MojoExecutionException( "Failed to compress Javascript file "
                    + e.getScript(), e );
            }
            String describe = in.getName() + " (" + INTEGER.format( in.length() ) + " bytes) ";
            String title = StringUtils.rightPad( describe, 60, "." );
            logStats( title + " compressed at " + ratio( compressed, in ) + "%" );
            return in.length() - compressed.length();
        }
        else
        {
            try
            {
                compressed.createNewFile();
                getLog().info( in.getName() + " was zero length; not compressed." );
                return 0;
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Error handling zero length file.", e );
            }
        }
    }

    private long ratio( File compressed, File in )
    {
        long length = in.length();
        if ( length == 0 )
        {
            return 0;
        }
        return ( ( ( length - compressed.length() ) * 100 ) / length );
    }

    private void logStats( String line )
    {
        if ( skipStats )
        {
            return;
        }
        getLog().info( line );
    }

    /**
     * @return the extension to append to compressed scripts.
     */
    public abstract String getExtension();

    /**
     * @return the outputDirectory
     */
    protected abstract File getOutputDirectory();

    /**
     * @return the sourceDirectory
     */
    protected abstract File getSourceDirectory();

    protected void setLocalRepository( ArtifactRepository localRepository )
    {
        this.localRepository = localRepository;
    }
}
