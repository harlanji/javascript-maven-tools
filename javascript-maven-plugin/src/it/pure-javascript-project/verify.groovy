    
	assert new File( basedir, "target/scripts/Hello.js" ).exists();
    assert ! new File( basedir, "target/scripts/One.js" ).exists();
    assert ! new File( basedir, "target/scripts/Two.js" ).exists();
    assert new File( basedir, "target/scripts/assembled.js" ).exists();
	assert new File( basedir, "target/pure-javascript-project-0.99-SNAPSHOT.jar" ).exists();
	assert new File( basedir, "target/surefire-reports/TEST-HelloTest.xml" ).exists();
	return true;