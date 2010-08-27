package org.codehaus.mojo.javascript.test.qunit;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;

import java.io.*;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.tools.shell.Global;

public class RhinoRuntime {

	protected Global global;
	protected Context context;
	protected Scriptable scope;

	public RhinoRuntime() {
		createAndInitializeContext();
	}

	public void close() {
		if( context != null ) {
			Context.exit();
			scope = null;
			global = null;
			context = null;
		}
	}

	protected void finalize() throws Throwable {
		try {
			close();
		} finally {
			super.finalize();
		}
	}



	public void putGlobal(String name, Object obj) {
		scope.put(name, scope, Context.toObject(obj, scope));
	}

	public Object getGlobal(String name) {
		return scope.get(name, scope);
	}

	public String execStringFunction(String function, String name, Object... args) {
		Function fn = context.compileFunction(scope, "function() {" + function + "}", name, 1, null);
		return (String) fn.call(context, scope, scope, args);
	}

	public String[] execStringArrayFunction(String function, String name, Object... args) {
		NativeArray results = execNativeArrayFunction(function, name, context, scope, args);
		if (results == null) {
			return null;
		}

		String[] strArray = new String[(int) results.getLength()];
		for (int i = 0; i < strArray.length; i++) {
			strArray[i] = (String) results.get(i, results);
		}
		return strArray;
	}

	public NativeArray execNativeArrayFunction(String function, String name, Object... args) {
		Function fn = context.compileFunction(scope, "function() {" + function + "}", name, 1, null);
		return (NativeArray) fn.call(context, scope, scope, args);
	}

	public void exec(String script, String name) {
		context.compileString(script, name, 1, null).exec(context, scope);
	}

	public void execClasspathScript(String path)
			throws IOException {
		Reader in = new InputStreamReader(context.getClass().getClassLoader().getResourceAsStream(path));
		compileAndExec(in, "classpath:" + path);
		in.close();
	}

	public void execScriptFile(File file)
			throws IOException {
		Reader in = new FileReader(file);
		compileAndExec(in, file.getName());
		in.close();
	}

	public void compileAndExec(Reader in, String name)
			throws IOException {
		context.compileReader(in, name, 1, null).exec(context, scope);
	}


	private void createAndInitializeContext() {
		context = Context.enter();
		global = new Global();
		global.init(context);
		scope = context.initStandardObjects(global);

		context.setOptimizationLevel(-1);
		context.setLanguageVersion(Context.VERSION_1_5);
	}
}
