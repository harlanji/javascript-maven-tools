document.write( "<script src='lib/prototype.js'></script> ");
document.write( "<script src='lib/log4javascript.js'></script> ");
document.write( "<script src='Hello.js'></script> ");

function testSayHello()
{
   var world = new Hello( "world" );
   assertEquals( world.sayHello(), "hello world" );
}
