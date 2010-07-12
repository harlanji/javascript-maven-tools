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

import junit.framework.TestCase;

import org.codehaus.plexus.util.FileUtils;

/**
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 */
public class ShrinksafeCompressorTest
    extends TestCase
{
    private JSCompressor compressor = new ShrinksafeCompressor();

    /**
     * Check the compressor is invoked as expected.
     * 
     * @throws Exception
     */
    public void testCompress()
        throws Exception
    {
        File input = new File( "src/test/resources/test1.js" );
        File output = new File( "target/test-out.js" );

        compressor.compress( input, output, JSCompressor.MAX, JSCompressor.JAVASCRIPT_1_3 );

        assertTrue( "exepected file not found", output.exists() );
        assertTrue( "no compression occured", output.length() < input.length() );
    }

    /**
     * @throws Exception
     */
    public void testMultipleCompress()
        throws Exception
    {
        // Fix issue with Shrinksafe : the rhino context is not cleared when
        // used multiple times.

        File output = new File( "target/test-out.js" );
        File input = null;
        for ( int i = 1; i <= 3; i++ )
        {
            input = new File( "src/test/resources/test" + i + ".js" );
            compressor.compress( input, output, JSCompressor.MAX, JSCompressor.JAVASCRIPT_1_3 );
        }

        assertTrue( "exepected file not found", output.exists() );
        assertTrue( "no compression occured", output.length() < input.length() );
        String out = FileUtils.fileRead( output );
        assertTrue( "test1.js code present in test3 compressed", out.indexOf( "test1" ) < 0 );
    }
}
