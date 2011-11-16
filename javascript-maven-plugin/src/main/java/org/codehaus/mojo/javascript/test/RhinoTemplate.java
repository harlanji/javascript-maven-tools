/*
 * Copyright 2011 harlan.
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
package org.codehaus.mojo.javascript.test;

import java.util.Iterator;
import java.util.Map;
import org.apache.maven.plugin.logging.Log;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.tools.shell.Global;

/**
 *
 * @author harlan
 */
public class RhinoTemplate {
  int languageVersion;
  Log log;

  public RhinoTemplate(int languageVersion, Log log) {
    this.languageVersion = languageVersion;
    this.log = log;
  }

  public Object evalScript(Map context, String[] args, RhinoCallBack callback) throws Exception {
    // Creates and enters a Context. The Context stores information
    // about the execution environment of a script.
    Context ctx = Context.enter();
    ctx.setLanguageVersion(languageVersion);
    ctx.setOptimizationLevel(-1);
    ctx.initStandardObjects();
    try {
      // Initialize the standard objects (Object, Function, etc.)
      // This must be done before scripts can be executed. Returns
      // a scope object that we use in later calls.
      // Use a "Golbal" scope to allow use of importClass in scripts
      Global scope = new Global();
      scope.init(ctx);
      if (context != null) {
        for (Iterator iterator = context.entrySet().iterator(); iterator.hasNext();) {
          Map.Entry entry = (Map.Entry) iterator.next();
          Scriptable jsObject = Context.toObject(entry.getValue(), scope);
          String key = (String) entry.getKey();
          log.debug("set object available to javascript " + key + "=" + jsObject);
          scope.put(key, scope, jsObject);
        }
      }
      if (args != null) {
        scope.defineProperty("arguments", args, ScriptableObject.DONTENUM);
      }
      return callback.doWithContext(ctx, scope);
    } finally {
      Context.exit();
    }
  }
  
}
