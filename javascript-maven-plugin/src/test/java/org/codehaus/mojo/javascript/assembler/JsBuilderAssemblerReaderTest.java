package org.codehaus.mojo.javascript.assembler;

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

import junit.framework.TestCase;

import org.apache.maven.plugin.testing.SilentLog;

public class JsBuilderAssemblerReaderTest
    extends TestCase
{
    public void testReadJsBuilderAssembler()
        throws Exception
    {
        JsBuilderAssemblerReader reader = new JsBuilderAssemblerReader();
        reader.enableLogging( new SilentLog() );
        Assembler assembler = reader.getAssembler( new File( "src/test/resources/assembler.jsb" ) );
        assertEquals( 2, assembler.getScripts().size() );
        Script script = (Script) assembler.getScripts().get( 0 );
        assertEquals( "test.js", script.getFileName() );
        assertEquals( 3, script.getIncludes().size() );
        assertEquals( "builder.js", script.getIncludes().get( 0 ).toString() );
    }
}
