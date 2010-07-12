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

import org.apache.maven.plugin.Mojo;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;
import org.codehaus.plexus.util.FileUtils;

public class StripDebugsMojoTest
    extends AbstractMojoTestCase
{
    public void testStripDebugs()
        throws Exception
    {
        File target = new File( "target/test-target/strip-debugs" );
        target.mkdirs();
        FileUtils.cleanDirectory( target );
        FileUtils.copyFileToDirectory( new File( "src/test/resources/with-debug/debugs.js" ),
            target );

        File testPom = new File( getBasedir(), "src/test/resources/strip-debugs.pom" );
        Mojo mojo = (Mojo) lookupMojo( "compress", testPom );
        assertNotNull( "Failed to configure the plugin", mojo );

        mojo.execute();

        File expected = new File( "./target/test-target/strip-debugs/debugs.js" );
        
        assertTrue( "Expected the stripped file to have non-zero length.", expected.length() > 0 );
        
        String source = FileUtils.fileRead( expected );
        assertTrue( source.indexOf( "logger" ) < 0 );
    }
}
