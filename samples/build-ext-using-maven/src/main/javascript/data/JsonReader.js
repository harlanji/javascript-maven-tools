/*
 * Ext JS Library 1.1.1
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://www.extjs.com/license
 */

/**
 * @class Ext.data.JsonReader
 * @extends Ext.data.DataReader
 * Data reader class to create an Array of Ext.data.Record objects from a JSON response
 * based on mappings in a provided Ext.data.Record constructor.
 * <p>
 * Example code:
 * <pre><code>
var RecordDef = Ext.data.Record.create([
    {name: 'name', mapping: 'name'},     // "mapping" property not needed if it's the same as "name"
    {name: 'occupation'}                 // This field will use "occupation" as the mapping.
]);
var myReader = new Ext.data.JsonReader({
    totalProperty: "results",    // The property which contains the total dataset size (optional)
    root: "rows",                // The property which contains an Array of row objects
    id: "id"                     // The property within each row object that provides an ID for the record (optional)
}, RecordDef);
</code></pre>
 * <p>
 * This would consume a JSON file like this:
 * <pre><code>
{ 'results': 2, 'rows': [
    { 'id': 1, 'name': 'Bill', occupation: 'Gardener' },
    { 'id': 2, 'name': 'Ben', occupation: 'Horticulturalist' } ]
}
</code></pre>
 * @cfg {String} totalProperty Name of the property from which to retrieve the total number of records
 * in the dataset. This is only needed if the whole dataset is not passed in one go, but is being
 * paged from the remote server.
 * @cfg {String} successProperty Name of the property from which to retrieve the success attribute used by forms.
 * @cfg {String} root name of the property which contains the Array of row objects.
 * @cfg {String} id Name of the property within a row object that contains a record identifier value.
 * @constructor
 * Create a new JsonReader
 * @param {Object} meta Metadata configuration options
 * @param {Object} recordType Either an Array of field definition objects,
 * or an {@link Ext.data.Record} object created using {@link Ext.data.Record#create}.
 */
Ext.data.JsonReader = function(meta, recordType){
    meta = meta || {};
    Ext.data.JsonReader.superclass.constructor.call(this, meta, recordType||meta.fields);
};
Ext.extend(Ext.data.JsonReader, Ext.data.DataReader, {
    /**
     * This method is only used by a DataProxy which has retrieved data from a remote server.
     * @param {Object} response The XHR object which contains the JSON data in its responseText.
     * @return {Object} data A data block which is used by an Ext.data.Store object as
     * a cache of Ext.data.Records.
     */
    read : function(response){
        var json = response.responseText;
        var o = eval("("+json+")");
        if(!o) {
            throw {message: "JsonReader.read: Json object not found"};
        }
        if(o.metaData){
            delete this.ef;
            this.meta = o.metaData;
            this.recordType = Ext.data.Record.create(o.metaData.fields);
            this.onMetaChange(this.meta, this.recordType, o);
        }
        return this.readRecords(o);
    },

    // private function a store will implement
    onMetaChange : function(meta, recordType, o){

    },

    /**
	 * @ignore
	 */
    simpleAccess: function(obj, subsc) {
    	return obj[subsc];
    },

	/**
	 * @ignore
	 */
    getJsonAccessor: function(){
        var re = /[\[\.]/;
        return function(expr) {
            try {
                return(re.test(expr))
                    ? new Function("obj", "return obj." + expr)
                    : function(obj){
                        return obj[expr];
                    };
            } catch(e){}
            return Ext.emptyFn;
        };
    }(),

    /**
     * Create a data block containing Ext.data.Records from an XML document.
     * @param {Object} o An object which contains an Array of row objects in the property specified
     * in the config as 'root, and optionally a property, specified in the config as 'totalProperty'
     * which contains the total size of the dataset.
     * @return {Object} data A data block which is used by an Ext.data.Store object as
     * a cache of Ext.data.Records.
     */
    readRecords : function(o){
        /**
         * After any data loads, the raw JSON data is available for further custom processing.
         * @type Object
         */
        this.jsonData = o;
        var s = this.meta, Record = this.recordType,
            f = Record.prototype.fields, fi = f.items, fl = f.length;

//      Generate extraction functions for the totalProperty, the root, the id, and for each field
        if (!this.ef) {
            if(s.totalProperty) {
	            this.getTotal = this.getJsonAccessor(s.totalProperty);
	        }
	        if(s.successProperty) {
	            this.getSuccess = this.getJsonAccessor(s.successProperty);
	        }
	        this.getRoot = s.root ? this.getJsonAccessor(s.root) : function(p){return p;};
	        if (s.id) {
	        	var g = this.getJsonAccessor(s.id);
	        	this.getId = function(rec) {
	        		var r = g(rec);
		        	return (r === undefined || r === "") ? null : r;
	        	};
	        } else {
	        	this.getId = function(){return null;};
	        }
            this.ef = [];
            for(var i = 0; i < fl; i++){
                f = fi[i];
                var map = (f.mapping !== undefined && f.mapping !== null) ? f.mapping : f.name;
                this.ef[i] = this.getJsonAccessor(map);
            }
        }

    	var root = this.getRoot(o), c = root.length, totalRecords = c, success = true;
    	if(s.totalProperty){
            var v = parseInt(this.getTotal(o), 10);
            if(!isNaN(v)){
                totalRecords = v;
            }
        }
        if(s.successProperty){
            var v = this.getSuccess(o);
            if(v === false || v === 'false'){
                success = false;
            }
        }
        var records = [];
	    for(var i = 0; i < c; i++){
		    var n = root[i];
	        var values = {};
	        var id = this.getId(n);
	        for(var j = 0; j < fl; j++){
	            f = fi[j];
                var v = this.ef[j](n);
                values[f.name] = f.convert((v !== undefined) ? v : f.defaultValue);
	        }
	        var record = new Record(values, id);
	        record.json = n;
	        records[i] = record;
	    }
	    return {
	        success : success,
	        records : records,
	        totalRecords : totalRecords
	    };
    }
});