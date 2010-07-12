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
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.inconspicuous.jsmin.JSMin;

/**
 * Use the Java version of the JSMin algorithm to compress a set of JS files.
 * For simplicity, the JSMin code (one class) is included in the plugin.
 * 
 * @see http://www.crockford.com/javascript/jsmin.html
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 */
public class JSMinCompressor
    implements JSCompressor
{

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.compress.JSCompressor#compress(java.io.File,
     * java.io.File, int, int)
     */
    public void compress( File input, File output, int level, int language )
        throws CompressionException
    {
        try
        {
            new JSMin( new FileInputStream( input ), new FileOutputStream( output ) ).jsmin();
        }
        catch ( Exception e )
        {
            throw new CompressionException( "Failed to create compressed file", e, input );
        }
    }

}
