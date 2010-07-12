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
import java.io.FileReader;
import java.io.FileWriter;

import org.codehaus.plexus.util.IOUtil;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.EvaluatorException;

import com.yahoo.platform.yui.compressor.JavaScriptCompressor;

/**
 * A JS compressor that uses Dojo modified Rhino engine to compress the script.
 * The resulting compressed-js is garanteed to be functionaly equivalent as this
 * is the internal view of the rhino context.
 * 
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 */
public class YahooUICompressor
    implements JSCompressor
{
    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.compress.JSCompressor#compress(java.io.File,
     * java.io.File, int, int)
     */
    public void compress( final File input, File compressed, int level, int language )
        throws CompressionException
    {
        FileWriter out = null;
        try
        {
            JavaScriptCompressor compressor =
                new JavaScriptCompressor( new FileReader( input ), new ErrorReporter()
                {

                    public void warning( String message, String sourceName, int line,
                                         String lineSource, int lineOffset )
                    {
                        if ( line < 0 )
                        {
                            System.err.println( "\n[WARNING] " + message );
                        }
                        else
                        {
                            System.err.println( "\n" + line + ':' + lineOffset + ':' + message );
                        }
                    }

                    public void error( String message, String sourceName, int line,
                                       String lineSource, int lineOffset )
                    {
                        if ( line < 0 )
                        {
                            System.err.println( "\n[ERROR] " + message );
                        }
                        else
                        {
                            System.err.println( "\n" + line + ':' + lineOffset + ':' + message );
                        }
                    }

                    public EvaluatorException runtimeError( String message, String sourceName,
                                                            int line, String lineSource,
                                                            int lineOffset )
                    {
                        error( message, sourceName, line, lineSource, lineOffset );
                        return new EvaluatorException( message );
                    }
                } );

            int linebreakpos = level < 4 ? -1 : 80;
            boolean munge = level < 3;
            boolean preserveAllSemiColons = level < 2;
            boolean preserveStringLiterals = level < 1;

            out = new FileWriter( compressed );
            compressor.compress( out, linebreakpos, munge, true, preserveAllSemiColons,
                preserveStringLiterals );
        }
        catch ( Exception e )
        {
            throw new CompressionException( "Failed to create compressed file", e, input );
        }
        finally
        {
            IOUtil.close( out );
        }
    }
}
