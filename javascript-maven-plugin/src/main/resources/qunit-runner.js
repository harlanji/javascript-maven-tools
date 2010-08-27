//load("lib/envjs-rhino/env.rhino.js");

(function() {


function bind(fn, context) {
	return function() {
		fn.apply(context, arguments);
	};
};


Envjs({
	scriptTypes : {
		'text/javascript' : true,
		'text/envjs' : false,
		'' : true
	},

	beforeScriptLoad: {

	},

	afterScriptLoad: {
		'qunit[\-\d|\.js]': function(scriptNode) {

			// bind QUnit events to the special $report object. its interface is
			// modeled after QUnit. other test frameworks need to make an adapter.

			var bindList = ['log', 'testStart', 'testDone', 'moduleStart', 'moduleDone', 'begin', 'done'];
			for(var i in bindList) {
				var k = bindList[i];
				QUnit[k] = bind($report[k], $report);
			};

			print("Installed QUnit hooks\n")
		}
	},

	onScriptLoadError: function(script, e) {
		print("Error loading script. src=" + script.src + ", error=" + e.getMessage() + ".");
	},

	onExit: function() {
		print("EnvJS exiting\n")
	},


	javaEnabled: false
});

})();

