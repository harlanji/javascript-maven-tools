/*
 *  Copyright 2010 harlan.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.codehaus.mojo.javascript;

import java.io.File;
import org.apache.maven.project.MavenProject;

/**
 *
 * @goal compile
 * @phase compile
 * @author harlan
 */
public class CompileSourceMojo extends CompileMojo {

    /** default includes pattern */
    protected static final String[] DEFAULT_INCLUDES = { "**/*.js" };


    /**
     * Location of the source files.
     *
     * @parameter default-value="${basedir}/src/main/javascript"
     */
    protected File sourceDirectory;

    /**
     * The output directory of the assembled js file.
     *
     * @parameter default-value="${basedir}/target/scripts"
     */
    protected File outputDirectory;

    /**
     * Descriptor for the strategy to assemble individual scripts sources into
     * destination.
     *
     * @parameter default-value="src/assembler/${project.artifactId}.xml"
     */
    protected File descriptor;

    /**
     * Descriptor file format (default or jsbuilder)
     *
     * @parameter
     */
    protected String descriptorFormat;


   /**
     * Exclusion pattern.
     *
     * @parameter
     */
    protected String[] excludes;

    /**
     * Inclusion pattern.
     *
     * @parameter
     */
    protected String[] includes;


	public String[] getDefaultIncludes() {
		return DEFAULT_INCLUDES;
	}

	public File getDescriptor() {
		return descriptor;
	}

	public String getDescriptorFormat() {
		return descriptorFormat;
	}

	public File getOutputDirectory() {
		return outputDirectory;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public String[] getExcludes() {
		return excludes;
	}

	public String[] getIncludes() {
		return includes;
	}



}
