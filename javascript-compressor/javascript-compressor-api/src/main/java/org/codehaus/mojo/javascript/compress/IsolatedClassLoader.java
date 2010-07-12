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

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.Iterator;

import org.apache.maven.artifact.Artifact;

/**
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class IsolatedClassLoader
    extends URLClassLoader
{

    public IsolatedClassLoader( Artifact artifact )
    {
        super( getArtifactURLs( artifact ) );
    }

    public IsolatedClassLoader( Collection artifacts )
    {
        super( getArtifactURLs( artifacts ) );
    }

    private static URL[] getArtifactURLs( Collection artifacts )
    {
        URL[] urls = new URL[artifacts.size()];
        try
        {
            int i = 0;
            for ( Iterator iterator = artifacts.iterator(); iterator.hasNext(); )
            {
                Artifact artifact = (Artifact) iterator.next();
                urls[i++] = artifact.getFile().toURL();
            }
        }
        catch ( MalformedURLException e )
        {
            return null;
        }
        return urls;
    }

    private static URL[] getArtifactURLs( Artifact artifact )
    {
        URL url;
        try
        {
            url = artifact.getFile().toURL();
            return new URL[] { url };
        }
        catch ( MalformedURLException e )
        {
            return null;
        }
    }

    public Class loadClass( String name )
        throws ClassNotFoundException
    {

        Class c = findLoadedClass( name );
        if ( c == null )
        {
            try
            {
                return findClass( name );
            }
            catch ( ClassNotFoundException e )
            {
                return super.loadClass( name );
            }
        }
        return super.loadClass( name );
    }

}
