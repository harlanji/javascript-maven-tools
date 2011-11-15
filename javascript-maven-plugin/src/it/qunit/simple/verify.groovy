

File workDir = new File( basedir, "target/test-work" );

File scriptsDir = new File( workDir, "scripts" );
File libsDir = new File( scriptsDir, "lib" );

// test scripts are copied
assert new File( workDir, "HelloTest.js" ).exists();
assert new File( workDir, "suite-basic.html" ).exists();
assert new File( workDir, "numbers/NumbersTest.js" ).exists();

// compiled JS is copied
assert new File( scriptsDir, "Hello.js" ).exists();
assert new File( scriptsDir, "numbers/One.js" ).exists();
assert new File( scriptsDir, "numbers/Two.js" ).exists();

// needed libraries are extracted
//assert new File( libsDir, "qunit/qunit.js" ).exists();
//assert new File( libsDir, "envjs-rhino/env.rhino.js" ).exists();


// reports are generated
File reportDir = new File( basedir, "target/surefire");
assert reportDir.exists();
assert new File( reportDir, "qunit.txt" ).exists();
assert new File( reportDir, "TEST-qunit.xml" ).exists();

return true;
