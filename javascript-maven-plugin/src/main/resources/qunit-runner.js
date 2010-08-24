//load("lib/envjs-rhino/env.rhino.js");

Envjs({
	scriptTypes : {
		'text/javascript' : true,
		'text/envjs' : false,
		'' : true
	},

	beforeScriptLoad: {
		'jquery[\-\d|\.js]': function(scriptNode) {
			//scriptNode.src = "scripts/lib/jquery/jquery.js";
			//scriptNode.src = "";

			//print("Use our own jQuery...\n");

		},

		'qunit[\-\d|\.js]': function(scriptNode) {
			//scriptNode.src = "scripts/lib/qunit/qunit.js";
			//scriptNode.src = "";

			//print("Use our own QUnit...\n");
		}
	},

	afterScriptLoad: {

	},

	onExit: function() {
		print("EnvJS exiting\n")
	},


	javaEnabled: false
});

jQuery.each(['log', 'testStart', 'testDone', 'moduleStart', 'moduleDone', 'begin', 'done'], function(i, v) {
	QUnit[v] = jQuery.proxy($report[v], $report);
});

print("Installed QUnit hooks\n")
