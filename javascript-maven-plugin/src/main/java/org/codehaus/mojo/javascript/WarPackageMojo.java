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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.mojo.javascript.archive.JavascriptArtifactManager;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.FileUtils;

/**
 * Compile scripts in a WAR the same way as for a stand-alone javascript project,
 * except output the scripts to the webapp's target directory instead of
 * a dedicated scripts output directory.
 * 
 * @goal war-package
 * @requiresDependencyResolution runtime
 * @phase prepare-package
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class WarPackageMojo
    extends AbstractJavascriptMojo
{

    /**
     * The directory where the webapp is built.
     * 
     * @parameter expression="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    protected File webappDirectory;

    /**
     * The directory where the webapp is built.
     *
     * @parameter expression="${project.build.directory}/scripts"
     * @required
     */
    protected File outputDirectory;

    /**
     * The folder in webapp for javascripts
     * 
     * @parameter default-value="scripts"
     */
    protected String scriptsDirectory;

    /**
     * The folder for javascripts dependencies
     * 
     * @parameter default-value="lib"
     */
    protected String libsDirectory;

    /**
     * Use the artifactId as folder
     * 
     * @parameter default-value="true"
     */
    protected boolean useArtifactId;

    /**
     * @component 
     */
    protected JavascriptArtifactManager javascriptArtifactManager;

    /**
	 * Set the output directory to webappDirectory/scriptsDirectory and then
	 * copy all dependencies to webappDirectory/scriptsDirectory/libDirectory.
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {
        File scriptsDirectory = new File(webappDirectory, this.scriptsDirectory);
		File libsDirectory = new File(scriptsDirectory, this.libsDirectory);

		scriptsDirectory.mkdirs();
		libsDirectory.mkdirs();

		

        try{
			FileUtils.copyDirectoryStructureIfModified(outputDirectory, scriptsDirectory);

            javascriptArtifactManager.unpack( project, DefaultArtifact.SCOPE_RUNTIME,
                libsDirectory, useArtifactId );
        }
		catch ( IOException e )
        {
            throw new MojoExecutionException( "Failed to unpack javascript dependencies", e );
        }
        catch ( ArchiverException e )
        {
            throw new MojoExecutionException( "Failed to unpack javascript dependencies", e );
        }
    }

}
