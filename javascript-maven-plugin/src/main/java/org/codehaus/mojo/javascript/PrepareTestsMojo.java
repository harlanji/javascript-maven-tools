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
import java.io.IOException;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.archive.JavascriptArtifactManager;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * Goal which copies scripts to the test-script directory.
 * 
 * @goal prepare-tests
 * @phase test-compile
 * @requiresDependencyResolution test
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class PrepareTestsMojo
    extends AbstractMojo
{

    /**
     * The maven project.
     * 
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * Set this to 'true' to bypass unit tests entirely. Its use is NOT
     * RECOMMENDED, but quite convenient on occasion.
     * 
     * @parameter expression="${maven.test.skip}"
     */
    private boolean skip;

    /**
     * Location of the source files.
     * 
     * @parameter default-value="${basedir}/src/main/javascript"
     */
    protected File sourceDirectory;

    /**
     * Location of the source files.
     * 
     * @parameter default-value="${basedir}/src/test/javascript"
     */
    protected File testSourceDirectory;

    /**
     * Location of the source files.
     * 
     * @parameter default-value="${project.build.directory}/test-scripts"
     */
    protected File outputDirectory;

    /**
     * The folder for javascripts dependencies
     * 
     * @parameter expression="${scripts}" default-value="lib"
     */
    private String libsDirectory;

    /**
     * Use the artifactId as folder
     * 
     * @parameter
     */
    private boolean useArtifactId;

    /**
     * @component
     */
    private JavascriptArtifactManager javascriptArtifactManager;

    /**
     * {@inheritDoc}
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        if ( skip || !testSourceDirectory.exists() )
        {
            return;
        }

        DirectoryScanner scanner = new DirectoryScanner();
        scanner.addDefaultExcludes();
        try
        {
            scanner.setBasedir( sourceDirectory );
            scanner.scan();
            String[] files = scanner.getIncludedFiles();
            for ( int i = 0; i < files.length; i++ )
            {
                File destFile = new File( outputDirectory, files[i] );
                destFile.getParentFile().mkdirs();
                FileUtils.copyFile( new File( sourceDirectory, files[i] ), destFile );
            }

            scanner.setBasedir( testSourceDirectory );
            scanner.scan();
            files = scanner.getIncludedFiles();
            for ( int i = 0; i < files.length; i++ )
            {
                File destFile = new File( outputDirectory, files[i] );
                destFile.getParentFile().mkdirs();
                FileUtils.copyFile( new File( testSourceDirectory, files[i] ), destFile );
            }
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "Failed to copy scripts in " + outputDirectory );
        }

        try
        {
            javascriptArtifactManager.unpack( project, DefaultArtifact.SCOPE_TEST, new File(
                outputDirectory, libsDirectory ), useArtifactId );
        }
        catch ( ArchiverException e )
        {
            throw new MojoExecutionException( "Failed to unpack javascript dependencies", e );
        }
    }
}
