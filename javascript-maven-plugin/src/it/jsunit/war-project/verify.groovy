    assert new File( basedir, "target/war-project-0.99-SNAPSHOT.war" ).exists();
    assert new File( basedir, "target/war-project-0.99-SNAPSHOT/scripts/lib/prototype.js" ).exists();
    assert new File( basedir, "target/war-project-0.99-SNAPSHOT/scripts/Hello.js" ).exists();
	return true;