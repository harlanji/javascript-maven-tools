
;;; var logger = log4javascript.getDefaultLogger();

var Hello = Class.create();
Hello.prototype =
{
    /**
     * Constructor
     */
    initialize : function( name )
    {
        this.name = name;
;;;     logger.info( "new Hello instance " + name );
    },

    sayHello : function()
    {
        return "hello " + this.name
    }
}