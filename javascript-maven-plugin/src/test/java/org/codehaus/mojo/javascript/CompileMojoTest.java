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

/**
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 */
public class CompileMojoTest
    extends AbstractMojoTestCase
{
    public void testCompileAndAssemble()
        throws Exception
    {
        File testPom = new File( getBasedir(), "src/test/resources/compile.pom" );
        Mojo mojo = (Mojo) lookupMojo( "compile", testPom );
        assertNotNull( "Failed to configure the plugin", mojo );

        mojo.execute();

        File expected = new File( "./target/test-target/compile/scriptaculous.js" );
        assertTrue( "expected file not found " + expected.getName(), expected.exists() );

        expected = new File( "./target/test-target/compile/prototype.js" );
        assertTrue( "expected file not found " + expected.getName(), expected.exists() );

        String merged = FileUtils.fileRead( expected );
        assertTrue( "builder not merged", merged.contains( "var Builder = {" ) );
        assertTrue( "slider not merged", merged.contains( "Slider.prototype = {" ) );

        File unexpected = new File( "./target/test-target/compile/builder.js" );
        assertTrue( "unexpected file found " + unexpected.getName(), !unexpected.exists() );

        unexpected = new File( "./target/test-target/compile/slider.js" );
        assertTrue( "unexpected file found " + unexpected.getName(), !unexpected.exists() );
    }
}