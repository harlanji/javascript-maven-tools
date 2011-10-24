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

package org.codehaus.mojo.javascript.test.qunit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;


/**
 * @component
 * @goal qunit-test
 * @phase test
 * 
 */
public class QUnitTestMojo extends AbstractRhinoTestMojo {

	@Override
	protected void runSuite(RhinoRuntime rt, File suite) throws Exception {

		// FIXME do we want to provide qunit automatically? maybe if no others
		// have been loaded when the first test starts.
		//rt.execScriptFile(new File(workDirectory, "lib/qunit/qunit.js"));

    rt.execClasspathScript("qunit.js");
		rt.execClasspathScript("qunit-runner.js");

    getLog().info("Qunit - Running suite: "+suite.getName());

   
		// HACK
		String code = "window.location = \"" + suite.getAbsolutePath() + "\";";
		rt.exec( code, suite.getAbsolutePath());

        //rt.exec( "Envjs.wait();", "start" );
	}
}
