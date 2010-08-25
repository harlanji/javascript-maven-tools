

File workDir = new File( basedir, "target/test-work" );

// compiled JS is copied
assert new File( workDir, "Hello.js" ).exists();
assert new File( workDir, "numbers/One.js" ).exists();
assert new File( workDir, "numbers/Two.js" ).exists();

// needed libraries are extracted
assert new File( workDir, "lib/jquery/jquery.js" ).exists();
assert new File( workDir, "lib/qunit/qunit.js" ).exists();
assert new File( workDir, "lib/envjs-rhino/env.rhino.js" ).exists();


// reports are generated
File reportDir = new File( basedir, "target/surefire-reports");
assert reportDir.exists();
assert new File( reportDir, "qunit.txt" ).exists();
assert new File( reportDir, "TEST-qunit.xml" ).exists();

return true;