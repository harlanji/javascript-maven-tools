warDir = new File( basedir, "target/simple-0.99-SNAPSHOT" );
scriptsDir = new File( warDir, "scripts" );

// for webapp packaging, original should NOT exist. contrary to normal
// lib packaging. I think this will change eventually, as compression
// and packaging are generalized.
def assertCompressed(String file) {
	original = new File( basedir, "target/classes/" + file );
	compressed = new File( scriptsDir, file );
	assert ! original.exists();
	assert compressed.exists();
	//assert compressed.length() < original.length();
}

assert scriptsDir.exists();

assertCompressed("Hello.js");
assertCompressed("numbers/One.js");
assertCompressed("numbers/Two.js");

File libDir = new File( scriptsDir, "lib" );
assert libDir.exists();

assert new File( libDir, "prototype/prototype.js" ).exists();
assert ! new File( libDir, "log4javascript" ).exists();

return true;