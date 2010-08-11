
var Hello = Class.create();
Hello.prototype =
{
    /**
     * Constructor
     */
    initialize : function( name )
    {
        this.name = name;
    },

    sayHello : function()
    {
        return "hello " + this.name
    }
}