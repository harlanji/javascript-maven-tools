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

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProjectHelper;
import org.codehaus.mojo.javascript.archive.JavascriptArchiver;
import org.codehaus.mojo.javascript.archive.Types;

/**
 * Goal used to build javascript libraries with maven. Compress the JavaScript
 * files from the packaging directory, pakage them as a javascript archive and
 * attach this new artifact to the project for beeing installed / deployed with
 * the regular uncompressed js-archive.
 * 
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 * @goal attach-compressed
 * @phase package
 */
public class AttachCompressedMojo
    extends AbstractCompressMojo
{

    /**
     * @component
     */
    MavenProjectHelper projectHelper;

    /**
     * Plexus archiver.
     * 
     * @component role="org.codehaus.plexus.archiver.Archiver" role-hint="javascript"
     * @required
     */
    private JavascriptArchiver archiver;

    /**
     * The output directory of the compressed javascript files.
     * 
     * @parameter default-value="${project.build.directory}/compressed"
     */
    private File compressedDirectory;

    /**
     * The output directory of the compressed javascript archive.
     * 
     * @parameter default-value="${project.build.directory}"
     */
    private File buildDirectory;

    /**
     * The filename of the compressed js file.
     * 
     * @parameter default-value="${project.build.finalName}"
     */
    private String finalName;

    /**
     * classifier for the compressed artifact
     * 
     * @parameter default-value="compressed"
     */
    private String classifier;

    /**
     * optional extension for the compressed artifact. Example "compressed"
     * 
     * @parameter
     */
    private String scriptClassifier;

    /**
     * The intput directory for the source javascript files.
     * 
     * @parameter default-value="${project.build.outputDirectory}"
     */
    private File scriptsDirectory;

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractCompressMojo#getExtension()
     */
    public String getExtension()
    {
        return scriptClassifier;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractCompressMojo#getOutputDirectory()
     */
    protected File getOutputDirectory()
    {
        return compressedDirectory;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractCompressMojo#getSourceDirectory()
     */
    protected File getSourceDirectory()
    {
        return scriptsDirectory;
    }

    /**
     * @parameter
     */
    private File manifest;

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractCompressMojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {
        super.execute();

        File compressed =
            new File( buildDirectory, finalName + "-" + classifier + "."
                + Types.JAVASCRIPT_EXTENSION );
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

            archiver.addDirectory( compressedDirectory );
            String groupId = project.getGroupId();
            String artifactId = project.getArtifactId();
            archiver.addFile( project.getFile(), "META-INF/maven/" + groupId + "/" + artifactId
                + "/pom.xml" );
            archiver.setDestFile( compressed );
            archiver.createArchive();
        }
        catch ( Exception e )
        {
            throw new MojoExecutionException( "Failed to create the javascript archive", e );
        }

        projectHelper.attachArtifact( project, Types.JAVASCRIPT_EXTENSION, classifier, compressed );
    }
}
