package org.codehaus.mojo.javascript.compress;

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
import java.net.URL;

import junit.framework.TestCase;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.codehaus.plexus.PlexusTestCase;

/**
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class IsolatedClassLoaderTest
    extends PlexusTestCase
{

    private ArtifactFactory artifactFactory;

    protected void setUp()
        throws Exception
    {
        super.setUp();
        artifactFactory = (ArtifactFactory) lookup( ArtifactFactory.class.getName() );
    }

    /**
     * Ensure a class can be loaded in isolation based on an artifact
     */
    public void testLoadClassInIsolation()
        throws Exception
    {
        // Create an artifact for junit.jar
        Artifact artifact =
            artifactFactory.createArtifact( "junit", "junit", "3.8.2", DefaultArtifact.SCOPE_TEST,
                "jar" );
        // locate junit.jar based on current loader class
        URL url = getClass().getClassLoader().getResource( "junit/framework/TestCase.class" );
        String path = url.toString();
        assertTrue( "junit TestCase class is not loaded from a jar", path.startsWith( "jar:" ) );
        int i = path.indexOf( '!' );
        url = new URL( path.substring( 4, i ) );

        // setup the isolated classloader
        artifact.setFile( new File( url.getFile() ) );
        ClassLoader cl = new IsolatedClassLoader( artifact );

        Class isolated = cl.loadClass( "junit.framework.TestCase" );
        assertNotNull( "failed to load TestCase class in isolation", isolated );
        assertTrue( "TestCase loaded, but not in isolation", isolated != TestCase.class );
    }
}
