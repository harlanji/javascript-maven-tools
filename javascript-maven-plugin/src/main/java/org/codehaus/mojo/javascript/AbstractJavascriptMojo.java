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
import java.util.Map;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.archive.JavascriptArtifactManager;
import org.codehaus.plexus.archiver.ArchiverException;

/**
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public abstract class AbstractJavascriptMojo
    extends AbstractMojo
{

	/**
     * The maven project.
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;


    /**
     * Map of of plugin artifacts.
     * 
     * @parameter expression="${plugin.artifactMap}"
     * @required
     * @readonly
     */
    protected Map pluginArtifactMap;

    /**
     * @component 
     */
    protected JavascriptArtifactManager javascriptArtifactManager;

	protected void unpackJavascriptDependency( String artifact, File dest ) throws MojoExecutionException {
		unpackJavascriptDependency(artifact, dest, false);
	}

    /**
     * Unpack a javascript dependency
     */
    protected void unpackJavascriptDependency( String artifact, File dest, boolean useArtifactId )
        throws MojoExecutionException
    {
        if ( !pluginArtifactMap.containsKey( artifact ) )
        {
            throw new MojoExecutionException( "Failed to resolve dependency " + artifact
                + " required by the plugin" );
        }
        Artifact javascript = (Artifact) pluginArtifactMap.get( artifact );

        try
        {
            javascriptArtifactManager.unpack( javascript, dest, useArtifactId );
        }
        catch ( ArchiverException e )
        {
            throw new MojoExecutionException( "Failed to unpack javascript dependency " + artifact,
                e );
        }
    }

}
