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
import java.io.FilenameFilter;

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.util.FileUtils;

public class CompressMojoTest
    extends AbstractMojoTestCase
{
    public void testAttachCompressedScriptaculous()
        throws Exception
    {
        File testPom = new File( getBasedir(), "src/test/resources/attach-compressed.pom" );
        Mojo mojo = (Mojo) lookupMojo( "attach-compressed", testPom );
        assertNotNull( "Failed to configure the plugin", mojo );

        mojo.execute();

        File[] js = getScripts( "./src/test/resources/scripts" );
        long size = 0;
        for ( int i = 0; i < js.length; i++ )
        {
            size += js[i].length();
        }

        File expected =
            new File( "./target/test-target/attach-compressed/scriptaculous-1.7-compressed.jar" );
        assertTrue( "expected file not found " + expected, expected.exists() );
        assertTrue( "no compression occured", expected.length() < size );
    }

    public void testCompressScriptaculous()
        throws Exception
    {
        File target = new File( "target/test-target/compress" );
        target.mkdirs();
        FileUtils.cleanDirectory( target );
        FileUtils.copyDirectory( new File( "src/test/resources/scripts" ), target );

        File testPom = new File( getBasedir(), "/target/test-classes/compress.pom" );
        Mojo mojo = (Mojo) lookupMojo( "compress", testPom );
        assertNotNull( "Failed to configure the plugin", mojo );

        mojo.execute();

        File[] js = getScripts( "./src/test/resources/scripts" );
        for ( int i = 0; i < js.length; i++ )
        {
            File expected = new File( target, js[i].getName().replace( ".js", "-compressed.js" ) );
            assertTrue( "expected file not found " + expected.getName(), expected.exists() );
            assertTrue( "no compression occured on " + expected.getName(),
                expected.length() < js[i].length() );
        }
    }

    public void testZeroLengthInput()
        throws Exception
    {
        File target = new File( "target/test-target/compress-zero-length" );
        target.mkdirs();
        FileUtils.cleanDirectory( target );
        FileUtils.copyDirectory( new File( "src/test/resources/zero-length" ), target );

        File testPom = new File( getBasedir(), "/target/test-classes/compress-zero-length.pom" );
        Mojo mojo = (Mojo) lookupMojo( "compress", testPom );
        assertNotNull( "Failed to configure the plugin", mojo );

        mojo.execute();

        File[] js = getScripts( "./src/test/resources/zero-length" );
        for ( int i = 0; i < js.length; i++ )
        {
            File expected = new File( target, js[i].getName().replace( ".js", "-min.js" ) );
            assertTrue( "expected file not found " + expected.getName(), expected.exists() );
        }
    }

    private File[] getScripts( String path )
    {
        File[] js = new File( path ).listFiles( new FilenameFilter()
        {
            public boolean accept( File dir, String name )
            {
                return name.endsWith( ".js" );
            }
        } );
        return js;
    }
}
