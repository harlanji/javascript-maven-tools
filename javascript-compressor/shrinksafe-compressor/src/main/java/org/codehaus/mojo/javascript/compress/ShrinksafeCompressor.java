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
import java.io.PrintStream;

import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.StringOutputStream;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextAction;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.ToolErrorReporter;
import org.mozilla.javascript.tools.shell.Global;
import org.mozilla.javascript.tools.shell.Main;
import org.mozilla.javascript.tools.shell.ShellContextFactory;

/**
 * A JS compressor that uses Dojo modified Rhino engine to compress the script.
 * The resulting compressed-js is garanteed to be functionaly equivalent as this
 * is the internal view of the rhino context.
 * 
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 */
public class ShrinksafeCompressor
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
        PrintStream o = System.out;
        System.setOut( new PrintStream( new StringOutputStream() ) );
        PrintStream out = null;
        try
        {
            String[] args = new String[3];
            args[0] = "-c"; // Set Main.outputCompressed = true
            args[1] = "-o"; // Set Main.outputFileName
            args[2] = compressed.getAbsolutePath();

            Main.processOptions( args );

            final ToolErrorReporter errorReporter = new ToolErrorReporter( false, System.err );
            errorReporter.setIsReportingWarnings( false );

            final ShellContextFactory shellContextFactory = new ShellContextFactory();
            shellContextFactory.setLanguageVersion( language );
            shellContextFactory.setOptimizationLevel( level );
            shellContextFactory.setErrorReporter( errorReporter );
            shellContextFactory.setStrictMode( true );

            final Global scope = new Global();
            scope.init( shellContextFactory );

            shellContextFactory.call( new ContextAction()
            {
                public Object run( Context context )
                {
                    Object[] args = new Object[1];
                    scope.defineProperty( "arguments", args, ScriptableObject.DONTENUM );
                    Main.processFile( context, scope, input.getAbsolutePath() );
                    return null;
                }
            } );
        }
        catch ( Exception e )
        {
            throw new CompressionException( "Failed to create compressed file", e, input );
        }
        finally
        {
            System.setOut( o );
            IOUtil.close( out );
        }
    }
}
