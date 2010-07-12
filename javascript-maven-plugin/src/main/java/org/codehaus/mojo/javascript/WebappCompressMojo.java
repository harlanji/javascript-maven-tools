package org.codehaus.mojo.javascript;

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
 * Goal to be used from a war project, to compress the scripts present in the
 * webapp packaging folder. Configured to run in the test phase as there is no
 * way (in maven 2.0) to run between exploded webapp assembly and .war
 * packaging.
 * 
 * @goal compress
 * @phase test
 * @author <a href="mailto:nicolas@apache.org">nicolas De Loof</a>
 */
public class WebappCompressMojo
    extends AbstractCompressMojo
{

    /**
     * The directory where the webapp is built.
     * 
     * @parameter expression="${project.build.directory}/${project.build.finalName}"
     * @required
     */
    private File webappDirectory;

    /**
     * Folder in webapp containing javascripts
     * 
     * @parameter default-value="scripts"
     */
    private String scripts;

    /**
     * classifier for the compressed artifact. If not set, compressed script
     * will replace uncompressed ones, and will apply without any change in
     * HTML/JSP.
     * 
     * @parameter
     */
    private String classifier;

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractCompressMojo#getExtension()
     */
    public String getExtension()
    {
        return classifier;
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractCompressMojo#getOutputDirectory()
     */
    protected File getOutputDirectory()
    {
        return getSourceDirectory();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.codehaus.mojo.javascript.AbstractCompressMojo#getSourceDirectory()
     */
    protected File getSourceDirectory()
    {
        return new File( webappDirectory, scripts );
    }

}
