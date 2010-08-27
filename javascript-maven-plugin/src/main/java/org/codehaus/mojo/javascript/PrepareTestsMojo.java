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
import java.io.IOException;

import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.codehaus.mojo.javascript.archive.JavascriptArtifactManager;
import org.codehaus.plexus.archiver.ArchiverException;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.FileUtils;

/**
 * Goal which copies scripts to the test-script directory.
 * 
 * @goal prepare-tests
 * @phase test-compile
 * @requiresDependencyResolution test
 * @author <a href="mailto:nicolas@apache.org">Nicolas De Loof</a>
 */
public class PrepareTestsMojo
    extends CompileMojo
{


    /** default includes pattern */
    private static final String[] DEFAULT_INCLUDES = { "**/*.js", "**/*.html" };


    /**
     * Location of the source files.
     *
     * @parameter default-value="${basedir}/src/test/javascript"
     */
    protected File sourceDirectory;

    /**
     * The output directory of the assembled js file.
     *
     * @parameter default-value="${project.build.testOutputDirectory}"
     */
    protected File outputDirectory;

    /**
     * Descriptor for the strategy to assemble individual scripts sources into
     * destination.
     *
     * @parameter 
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
