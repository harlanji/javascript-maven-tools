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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.mojo.javascript.archive.JavascriptArchiver;
import org.codehaus.mojo.javascript.archive.Types;

/**
 * Goal which packages scripts and resources as a javascript archive to be
 * installed / deployed in maven repositories.
 * 
 * @goal package
 * @phase package
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class PackageMojo
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
     * @component
     */
    MavenProjectHelper projectHelper;

    /**
     * The output directory of the js file.
     * 
     * @parameter default-value="${project.build.directory}"
     */
    private File outputDirectory;

    /**
     * The filename of the js file.
     * 
     * @parameter default-value="${project.build.finalName}"
     */
    private String finalName;

    /**
     * Plexus archiver.
     * 
     * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="javascript"
     * @required
     */
    private JavascriptArchiver archiver;

    /**
     * Optional classifier
     * 
     * @parameter
     */
    private String classifier;

    /**
     * @parameter
     */
    private File manifest;

    /**
     * Location of the scripts files.
     * 
     * @parameter default-value="${project.build.outputDirectory}"
     */
    private File scriptsDirectory;

    public void execute()
        throws MojoExecutionException
    {
        File jsarchive = new File( outputDirectory, finalName + "." + Types.JAVASCRIPT_EXTENSION );
        try
        {		
            if ( manifest != null )
            {
                archiver.setManifest( manifest );
            }
            else
            {
                archiver.createDefaultManifest( project );
            }
            archiver.addDirectory( scriptsDirectory );
            String groupId = project.getGroupId();
            String artifactId = project.getArtifactId();
            archiver.addFile( project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId
                + "/pom.xml" );
            archiver.setDestFile( jsarchive );
            archiver.createArchive();
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Failed to create the javascript archive", e );
        }

        if ( classifier != null )
        {
            projectHelper.attachArtifact( project, Types.JAVASCRIPT_TYPE, classifier, jsarchive );
        }
        else
        {
            project.getArtifact().setFile( jsarchive );
        }
    }
}
