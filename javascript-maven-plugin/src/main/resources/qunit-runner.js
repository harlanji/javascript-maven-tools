//load("lib/envjs-rhino/env.rhino.js");

(function() {





// bind QUnit events to the special $report object. its interface is
// modeled after QUnit. other test frameworks need to make an adapter.

QUnit.log = function(args) {
  $report.log(args.result, args.message);
}

QUnit.testStart = function(args) {
  $report.testStart(args.name);
}

QUnit.testDone = function(args) {
  $report.testDone(args.name, args.failed, args.total);
}

QUnit.moduleStart = function(args) {
  $report.moduleStart(args.name);
}

QUnit.moduleDone = function(args) {
  $report.moduleDone(args.name, args.failed, args.total);
}

QUnit.begin = function(args) {
  $report.begin();
}

QUnit.done = function(args) {
  $report.done(args.failed, args.total);
}
      
Envjs({
	scriptTypes : {
		'text/javascript' : true,
		'text/envjs' : false,
		'' : true
	},

	beforeScriptLoad: {

	},

	afterScriptLoad: {
    '.*': function(scriptNode) {
      Envjs.log('loaded script: '+scriptNode.src);
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

