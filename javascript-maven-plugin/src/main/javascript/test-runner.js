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

load('env.rhino.js');
load('qunit.js');


Envjs({
    scriptTypes: {
        '': true, //anonymous and inline
        'text/javascript': true
    },
    afterScriptLoad:{
        "qunit": function(script){
            var count = 0,
                module;
            // track test modules so we can include them in logs
            QUnit.moduleStart = function(name, settings) {
                module = name;
            };
            // hookinto QUnit log so we can log test results
            QUnit.log = function(result, message){
                console.log(
                     '{%s}(%s)[%s] %s ',
                     module,
                     count++,
                     result ? 'PASS' : 'FAIL',
                     message
                );
            };
            // hook into qunit.done and write resulting html to a
            // a new file.  Be careful to neutralize script tags so
            // opening the script in the browser allows the results
            // to act as a static report without re-running tests
            QUnit.done = function(fail, pass){
                console.log('PASSED: %s FAILED: %s', pass, fail);
                //Writing Results to File
                jQuery('script').each(function(){
                    this.type = 'text/envjs';
                });
                Envjs.writeToFile(
                    document.documentElement.outerHTML,
                    Envjs.uri('results.html')
                );
           };
        }
    }
});