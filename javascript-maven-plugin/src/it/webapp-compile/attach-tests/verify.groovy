
File warDir = new File( basedir, "target/attach-tests-0.99-SNAPSHOT" );
File libsDir = new File( warDir, "scripts/lib" );
File testsDir = warDir;

// suite files are present
assert new File(testsDir, "suite-basic.html").exists();
assert new File(testsDir, "HelloTest.js").exists();
assert new File(testsDir, "numbers/NumbersTest.js").exists();

// test scoped dependencies are present
assert new File(libsDir, "qunit/qunit.js").exists();

return true;