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
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.assembler.Assembler;
import org.codehaus.mojo.javascript.assembler.AssemblerReader;
import org.codehaus.mojo.javascript.assembler.AssemblerReaderManager;
import org.codehaus.mojo.javascript.assembler.Script;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.IOUtil;

/**
 * Goal which assemble javascript sources into the packaging directory. An
 * optional assembler descriptor can be set to configure scripts to be merged.
 * Other scripts are simply copied to the output directory.
 * 

 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public abstract class CompileMojo
    extends AbstractJavascriptMojo
{


	public abstract File getDescriptor();
	public abstract String getDescriptorFormat();
	public abstract File getOutputDirectory();
	public abstract File getSourceDirectory();

	public abstract String[] getDefaultIncludes();
	public abstract String[] getIncludes();
	public abstract String[] getExcludes();





    /**
     * @component
     */
    protected AssemblerReaderManager assemblerReaderManager;



    public void execute()
        throws MojoExecutionException, MojoFailureException
    {

		if( !getSourceDirectory().exists() ) {
			getLog().warn("No source directory exists at: " + getSourceDirectory() + "... continuing anyway.");
			return;
		}

        getOutputDirectory().mkdirs();

        Set merged = assemble();

		String[] includes = getIncludes();

        if ( includes == null )
        {
            includes = getDefaultIncludes();
        }
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( getSourceDirectory() );
        scanner.setExcludes( getExcludes() );
        scanner.addDefaultExcludes();

        scanner.setIncludes( includes );
        scanner.scan();

        try
        {
            String[] files = scanner.getIncludedFiles();
            for ( int i = 0; i < files.length; i++ )
            {
                String file = files[i];
                if ( merged.contains( file ) )
                {
                    continue;
                }
                File source = new File( getSourceDirectory(), file );
                File dest = new File( getOutputDirectory(), file );
                dest.getParentFile().mkdir();
                FileUtils.copyFile( source, dest );
            }
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Failed to copy source files to " + getOutputDirectory(),
                e );
        }
    }

    /**
     * Honor the assembly rules to build merged scripts from individual ones.
     * 
     * @return a set of all script merged, to be skiped from the target
     * directory.
     * @throws MojoExecutionException
     */
    protected Set assemble()
        throws MojoExecutionException
    {
		File descriptor = getDescriptor();
		String descriptorFormat = getDescriptorFormat();

        if ( descriptor == null )
        {
            return Collections.EMPTY_SET;
        }

        if ( !descriptor.exists() )
        {
            if ( descriptor.getName().equals( project.getArtifactId() + ".xml" ) )
            {
                getLog().info( "No default assembler descriptor - just copy scripts" );
                return Collections.EMPTY_SET;
            }
            throw new MojoExecutionException( "The assembler descriptor does not exists : "
                + descriptor );
        }

        if ( descriptorFormat == null )
        {
            descriptorFormat = "default";
            if ( descriptor.getName().toLowerCase().endsWith( ".jsb" ) )
            {
                descriptorFormat = "jsbuilder";
            }
        }

        Assembler assembler;
        try
        {
            AssemblerReader reader = assemblerReaderManager.getAssemblerReader( descriptorFormat );
            assembler = reader.getAssembler( descriptor );
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Failed to read the assembler descriptor "
                + descriptor.getAbsolutePath(), e );
        }
        return assemble( assembler );
    }

    private Set assemble( Assembler assembler )
        throws MojoExecutionException
    {
        Set merged = new HashSet();

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir( getSourceDirectory() );
        scanner.setExcludes( getExcludes() );
        scanner.addDefaultExcludes();

        for ( Iterator iterator = assembler.getScripts().iterator(); iterator.hasNext(); )
        {
            Script script = (Script) iterator.next();
            String fileName = script.getFileName();
            PrintWriter writer = null;
            try
            {
                File target = new File( getOutputDirectory(), fileName );
                target.getParentFile().mkdirs();
                writer = new PrintWriter( target );

                List scriptOrderedIncludes = script.getIncludes();
                for ( Iterator iter = scriptOrderedIncludes.iterator(); iter.hasNext(); )
                {
                    String scriptInclude = (String) iter.next();
                    scanner.setIncludes( new String[] { scriptInclude } );
                    scanner.scan();

                    String[] files = scanner.getIncludedFiles();
                    for ( int i = 0; i < files.length; i++ )
                    {
                        String file = files[i];
                        File source = new File( getSourceDirectory().getAbsolutePath() + "/" + file );
                        IOUtil.copy( new FileReader( source ), writer );
                        writer.println();
                        merged.add( file );
                    }
                }
            }
            catch ( IOException e )
            {
                throw new MojoExecutionException( "Failed to write merged file " + fileName, e );
            }
            finally
            {
                IOUtil.close( writer );
            }
        }
        return merged;
    }
}
