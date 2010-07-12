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

import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.component.repository.exception.ComponentLookupException;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.codehaus.plexus.util.FileUtils;

/**
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class DefaultAssemblerReaderManager
    implements AssemblerReaderManager, Contextualizable
{
    private PlexusContainer container;

    public void contextualize( Context context )
        throws ContextException
    {
        container = (PlexusContainer) context.get( PlexusConstants.PLEXUS_KEY );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.assembler.AssemblerReaderManager#getAssemblerReader(java.io.File)
     */
    public AssemblerReader getAssemblerReader( File file )
        throws NoSuchAssemblerReaderException
    {
        String ext = FileUtils.getExtension( file.getName() ).toLowerCase();
        if ( "xml".equals( ext ) )
        {
            return getAssemblerReader( "default" );
        }
        return getAssemblerReader( ext );
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.assembler.AssemblerReaderManager#getAssemblerReader(java.lang.String)
     */
    public AssemblerReader getAssemblerReader( String name )
        throws NoSuchAssemblerReaderException
    {
        try
        {
            return (AssemblerReader) container.lookup( AssemblerReader.ROLE, name );
        }
        catch ( ComponentLookupException e )
        {
            throw new NoSuchAssemblerReaderException( name );
        }
    }
}
