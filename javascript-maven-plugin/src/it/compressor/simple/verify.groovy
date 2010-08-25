// both regular and compressed scripts should exist, and compressed should be
// smaller than the original
// (no scripts in this test are minimal size)
def assertCompressed(String file) {
	original = new File( basedir, "target/scripts/" + file );
	compressed = new File( basedir, "target/compressed/" + file );
	assert original.exists();
	assert compressed.exists();
	assert compressed.length() < original.length();
}

assertCompressed("Hello.js");
assertCompressed("numbers/One.js");
assertCompressed("numbers/Two.js");

// compressed resources should not exist
assert ! new File( basedir, "target/compressed/wait.gif" ).exists();

// compressed artifact should exist
assert new File( basedir, "target/simple-0.99-SNAPSHOT-compressed.jar" ).exists();

return true;