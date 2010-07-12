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

/**
 * Abstraction of a JS compression tool.
 */
public interface JSCompressor
{

    /**
     * Constants for optimization level
     */
    int NONE = 0;

    int MAX = 9;

    /**
     * Javascript language versions
     */
    int JAVASCRIPT_1_1 = 110;

    int JAVASCRIPT_1_2 = 120;

    int JAVASCRIPT_1_3 = 130;

    /**
     * Compress the input script file into the output file (may be same).
     * 
     * @param input source to get compressed
     * @param output compressed script
     * @param level optimization level from 0 to 9. May have various
     * signification dependending on the compressor, from beeing ignored to some
     * fine tweaking the output.
     * @param language version of javascript to be used ("130" for JS 1.3), as
     * defined by Mozilla Rhino engine
     * @throws CompressionException any error during compression
     */
    void compress( File input, File output, int level, int language )
        throws CompressionException;
}
