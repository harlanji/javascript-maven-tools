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
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.jxpath.JXPathContext;
import org.codehaus.plexus.logging.LogEnabled;
import org.codehaus.plexus.logging.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class JsBuilderAssemblerReader
    implements AssemblerReader, LogEnabled
{
    private Logger logger;

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.plexus.logging.LogEnabled#enableLogging(org.codehaus.plexus.logging.Logger)
     */
    public void enableLogging( Logger logger )
    {
        this.logger = logger;
    }

    private static final String OUTPUT = "$output/";

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.assembler.AssemblerReader#getAssembler(java.io.File)
     */
    public Assembler getAssembler( File file )
        throws Exception
    {
        logger.info( "Reading assembler descriptor " + file.getAbsolutePath() );
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document dom = builder.parse( file );

        Assembler assembler = new Assembler();
        JXPathContext xpath = JXPathContext.newContext( dom );
        xpath.setLenient( true );
        String src = (String) xpath.getValue( "//directory/@name" );
        List nodes = xpath.selectNodes( "//target" );
        for ( Iterator iterator = nodes.iterator(); iterator.hasNext(); )
        {
            Node node = (Node) iterator.next();
            Script script = new Script();
            assembler.addScript( script );
            JXPathContext nodeContext = JXPathContext.newContext( node );
            String fileName = (String) nodeContext.getValue( "@file" );
            fileName = fileName.replace( '\\', '/' );
            if ( fileName.startsWith( OUTPUT ) )
            {
                fileName = fileName.substring( OUTPUT.length() );
            }
            script.setFileName( fileName );

            for ( Iterator iter = nodeContext.iterate( "//include/@name" ); iter.hasNext(); )
            {
                String include = ( (String) iter.next() ).replace( '\\', '/' );
                if ( src != null && src.length() > 0 )
                {
                    include = include.substring( src.length() + 1 );
                }
                script.addInclude( include );
            }
        }
        return assembler;
    }
}
