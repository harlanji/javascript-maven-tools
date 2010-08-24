
File warDir = new File( basedir, "target/jslib-descriptor-0.99-SNAPSHOT" );

File scriptsDir = new File( warDir, "scripts" );
assert scriptsDir.exists();
assert new File( scriptsDir, "Hello.js" ).exists();
assert new File( scriptsDir, "numbers.js" ).exists();

// we put all scripts from 'numbers' dir into an assembly, so it should ot exist
assert ! new File( scriptsDir, "numbers" ).exists();

File libDir = new File( scriptsDir, "lib" );
assert libDir.exists();

assert new File( libDir, "prototype/prototype.js" ).exists();
assert ! new File( libDir, "log4javascript" ).exists();

return true;