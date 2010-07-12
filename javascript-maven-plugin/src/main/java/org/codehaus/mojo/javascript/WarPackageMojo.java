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

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.mojo.javascript.archive.JavascriptArtifactManager;
import org.codehaus.plexus.archiver.ArchiverException;

/**
 * Goal that prepares scripts for packaging as a web application.
 * 
 * @goal war-package
 * @requiresDependencyResolution runtime
 * @phase compile
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class WarPackageMojo
    extends CompileMojo
{

    /**
     * The directory where the webapp is built.
     * 
     * @parameter expression="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    private File webappDirectory;

    /**
     * The folder in webapp for javascripts
     * 
     * @parameter expression="${scripts}" default-value="scripts"
     */
    private String scriptsDirectory;

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
        super.outputDirectory = new File( webappDirectory, scriptsDirectory );
        super.execute();

        try
        {
            javascriptArtifactManager.unpack( getProject(), DefaultArtifact.SCOPE_RUNTIME,
                new File( webappDirectory, scriptsDirectory + "/" + libsDirectory ), useArtifactId );
        }
        catch ( ArchiverException e )
        {
            throw new MojoExecutionException( "Failed to unpack javascript dependencies", e );
        }
    }

}
