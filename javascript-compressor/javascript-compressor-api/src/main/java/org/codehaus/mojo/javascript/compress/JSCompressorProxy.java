/**
 *
 */
package org.codehaus.mojo.javascript.compress;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.codehaus.plexus.personality.plexus.lifecycle.phase.InitializationException;

/**
 * @author ndeloof
 */
public class JSCompressorProxy
    implements JSCompressor
{

    private Object compressor;

    private Method compress;

    public JSCompressorProxy( Object compressor )
        throws InitializationException
    {
        super();
        this.compressor = compressor;
        try
        {
            this.compress =
                compressor.getClass().getMethod( "compress",
                    new Class[] { File.class, File.class, int.class, int.class } );
        }
        catch ( Exception e )
        {
            throw new InitializationException(
                "proxied object has no method compress(File,File,int,int)" );
        }
    }

    public void compress( File input, File output, int level, int language )
        throws CompressionException
    {
        try
        {
            compress.invoke( compressor, new Object[] { input, output, Integer.valueOf( level ),
                Integer.valueOf( language ) } );
        }
        catch ( InvocationTargetException e )
        {
            if ( e.getTargetException() instanceof CompressionException )
            {
                throw (CompressionException) e.getTargetException();
            }
            throw new CompressionException( "Failed to compress JS file", e, input );
        }
        catch ( Exception e )
        {
            throw new CompressionException( "Failed to compress JS file", e, input );
        }
    }

}
