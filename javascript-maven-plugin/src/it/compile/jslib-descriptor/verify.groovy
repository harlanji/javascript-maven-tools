// the unassembled file should exist
assert new File( basedir, "target/scripts/Hello.js" ).exists();
// the assembled file should exist
assert new File( basedir, "target/scripts/numbers.js" ).exists();

// since we assembled all files in the numbers dir, it should not exist.
assert ! new File( basedir, "target/scripts/numbers" ).exists();

return true;