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

/**
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class NoSuchAssemblerReaderException
    extends Exception
{
    private String assemblerName;

    public NoSuchAssemblerReaderException( String name )
    {
        super( "No such assembler reader: '" + name + "'." );
        this.assemblerName = name;
    }

    /**
     * @return the assemblerName
     */
    public String getAssemblerName()
    {
        return assemblerName;
    }

}