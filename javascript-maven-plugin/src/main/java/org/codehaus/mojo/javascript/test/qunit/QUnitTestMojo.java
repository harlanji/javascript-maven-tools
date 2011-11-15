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

import com.sun.imageio.plugins.common.ReaderUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.util.FileUtils;
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
	protected void runSuite(final File suite, final ReportCallbacks reportCb) throws Exception {
    
    
		RhinoTemplate rt = new RhinoTemplate();
    
    Map<String, Object> context = new HashMap<String, Object>();

		// Put our reporting callbacks in there
		context.put("$report", reportCb);


		// Establish window scope with dom and all imported and inline scripts executed
		//rt.execClasspathScript("env.rhino.js");
		// FIXME this is extremely ghetto and should be integrated more with the copying above
		
    //rt.execScriptFile(new File(workDirectory, scriptsDirectory + "/" + libsDirectory + "/envjs-rhino/env.rhino.js"));
    rt.evalScript(context, new String[]{}, new RhinoCallBack() {

      public Object doWithContext(Context ctx, Scriptable scope) throws IOException {
        getLog().info("Qunit - Running suite: "+suite.getName());
        
        
        
        Reader rhinoEnvReader = new InputStreamReader(
                AbstractRhinoTestMojo.class.getResourceAsStream("/env.rhino.js"));
        ctx.evaluateReader(scope, rhinoEnvReader, "env.rhino.js", 1, null);
        
 

        // FIXME do we want to provide qunit automatically? maybe if no others
        // have been loaded when the first test starts.
        //rt.execScriptFile(new File(workDirectory, "lib/qunit/qunit.js"));

        rhinoEnvReader = new InputStreamReader(
              QUnitTestMojo.class.getResourceAsStream("/qunit.js"));
        ctx.evaluateReader(scope, rhinoEnvReader, "qunit.js", 1, null);


        rhinoEnvReader = new InputStreamReader(
              QUnitTestMojo.class.getResourceAsStream("/qunit-runner.js"));
        ctx.evaluateReader(scope, rhinoEnvReader, "qunit-runner.js", 1, null);
        

        String code = "window.location = \"" + suite.getAbsolutePath() + "\";";
        Object result = ctx.evaluateString(scope, code, "<qunit-runner>", 1, null);

        return result;
      }
    });
    



        //rt.exec( "Envjs.wait();", "start" );
	}
}
