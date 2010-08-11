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


/*
load("classpath:jquery.js");
load("classpath:qunit.js");
*/

Envjs({
	scriptTypes : {
		'text/javascript' : true,
		'text/envjs' : false,
		'' : true
	},

	onExit: function() {
		print("EnvJS exiting")
	},


	javaEnabled: false
});

jQuery.each(['log', 'testStart', 'testDone', 'moduleStart', 'moduleDone', 'begin', 'done'], function(i, v) {
	QUnit[v] = jQuery.proxy($report[v], $report);
});