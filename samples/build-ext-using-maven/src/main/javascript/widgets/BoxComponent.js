/*
 * Ext JS Library 1.1.1
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://www.extjs.com/license
 */

/**
 * @class Ext.BoxComponent
 * @extends Ext.Component
 * Base class for any visual {@link Ext.Component} that uses a box container.  BoxComponent provides automatic box
 * model adjustments for sizing and positioning and will work correctly withnin the Component rendering model.  All
 * container classes should subclass BoxComponent so that they will work consistently when nested within other Ext
 * layout containers.
 * @constructor
 * @param {Ext.Element/String/Object} config The configuration options.
 */
Ext.BoxComponent = function(config){
    Ext.BoxComponent.superclass.constructor.call(this, config);
    this.addEvents({
        /**
         * @event resize
         * Fires after the component is resized.
	     * @param {Ext.Component} this
	     * @param {Number} adjWidth The box-adjusted width that was set
	     * @param {Number} adjHeight The box-adjusted height that was set
	     * @param {Number} rawWidth The width that was originally specified
	     * @param {Number} rawHeight The height that was originally specified
	     */
        resize : true,
        /**
         * @event move
         * Fires after the component is moved.
	     * @param {Ext.Component} this
	     * @param {Number} x The new x position
	     * @param {Number} y The new y position
	     */
        move : true
    });
};

Ext.extend(Ext.BoxComponent, Ext.Component, {
    // private, set in afterRender to signify that the component has been rendered
    boxReady : false,
    // private, used to defer height settings to subclasses
    deferHeight: false,

    /**
     * Sets the width and height of the component.  This method fires the resize event.  This method can accept
     * either width and height as separate numeric arguments, or you can pass a size object like {width:10, height:20}.
     * @param {Number/Object} width The new width to set, or a size object in the format {width, height}
     * @param {Number} height The new height to set (not required if a size object is passed as the first arg)
     * @return {Ext.BoxComponent} this
     */
    setSize : function(w, h){
        // support for standard size objects
        if(typeof w == 'object'){
            h = w.height;
            w = w.width;
        }
        // not rendered
        if(!this.boxReady){
            this.width = w;
            this.height = h;
            return this;
        }

        // prevent recalcs when not needed
        if(this.lastSize && this.lastSize.width == w && this.lastSize.height == h){
            return this;
        }
        this.lastSize = {width: w, height: h};

        var adj = this.adjustSize(w, h);
        var aw = adj.width, ah = adj.height;
        if(aw !== undefined || ah !== undefined){ // this code is nasty but performs better with floaters
            var rz = this.getResizeEl();
            if(!this.deferHeight && aw !== undefined && ah !== undefined){
                rz.setSize(aw, ah);
            }else if(!this.deferHeight && ah !== undefined){
                rz.setHeight(ah);
            }else if(aw !== undefined){
                rz.setWidth(aw);
            }
            this.onResize(aw, ah, w, h);
            this.fireEvent('resize', this, aw, ah, w, h);
        }
        return this;
    },

    /**
     * Gets the current size of the component's underlying element.
     * @return {Object} An object containing the element's size {width: (element width), height: (element height)}
     */
    getSize : function(){
        return this.el.getSize();
    },

    /**
     * Gets the current XY position of the component's underlying element.
     * @param {Boolean} local (optional) If true the element's left and top are returned instead of page XY (defaults to false)
     * @return {Array} The XY position of the element (e.g., [100, 200])
     */
    getPosition : function(local){
        if(local === true){
            return [this.el.getLeft(true), this.el.getTop(true)];
        }
        return this.xy || this.el.getXY();
    },

    /**
     * Gets the current box measurements of the component's underlying element.
     * @param {Boolean} local (optional) If true the element's left and top are returned instead of page XY (defaults to false)
     * @returns {Object} box An object in the format {x, y, width, height}
     */
    getBox : function(local){
        var s = this.el.getSize();
        if(local){
            s.x = this.el.getLeft(true);
            s.y = this.el.getTop(true);
        }else{
            var xy = this.xy || this.el.getXY();
            s.x = xy[0];
            s.y = xy[1];
        }
        return s;
    },

    /**
     * Sets the current box measurements of the component's underlying element.
     * @param {Object} box An object in the format {x, y, width, height}
     * @returns {Ext.BoxComponent} this
     */
    updateBox : function(box){
        this.setSize(box.width, box.height);
        this.setPagePosition(box.x, box.y);
        return this;
    },

    // protected
    getResizeEl : function(){
        return this.resizeEl || this.el;
    },

    // protected
    getPositionEl : function(){
        return this.positionEl || this.el;
    },

    /**
     * Sets the left and top of the component.  To set the page XY position instead, use {@link #setPagePosition}.
     * This method fires the move event.
     * @param {Number} left The new left
     * @param {Number} top The new top
     * @returns {Ext.BoxComponent} this
     */
    setPosition : function(x, y){
        this.x = x;
        this.y = y;
        if(!this.boxReady){
            return this;
        }
        var adj = this.adjustPosition(x, y);
        var ax = adj.x, ay = adj.y;

        var el = this.getPositionEl();
        if(ax !== undefined || ay !== undefined){
            if(ax !== undefined && ay !== undefined){
                el.setLeftTop(ax, ay);
            }else if(ax !== undefined){
                el.setLeft(ax);
            }else if(ay !== undefined){
                el.setTop(ay);
            }
            this.onPosition(ax, ay);
            this.fireEvent('move', this, ax, ay);
        }
        return this;
    },

    /**
     * Sets the page XY position of the component.  To set the left and top instead, use {@link #setPosition}.
     * This method fires the move event.
     * @param {Number} x The new x position
     * @param {Number} y The new y position
     * @returns {Ext.BoxComponent} this
     */
    setPagePosition : function(x, y){
        this.pageX = x;
        this.pageY = y;
        if(!this.boxReady){
            return;
        }
        if(x === undefined || y === undefined){ // cannot translate undefined points
            return;
        }
        var p = this.el.translatePoints(x, y);
        this.setPosition(p.left, p.top);
        return this;
    },

    // private
    onRender : function(ct, position){
        Ext.BoxComponent.superclass.onRender.call(this, ct, position);
        if(this.resizeEl){
            this.resizeEl = Ext.get(this.resizeEl);
        }
        if(this.positionEl){
            this.positionEl = Ext.get(this.positionEl);
        }
    },

    // private
    afterRender : function(){
        Ext.BoxComponent.superclass.afterRender.call(this);
        this.boxReady = true;
        this.setSize(this.width, this.height);
        if(this.x || this.y){
            this.setPosition(this.x, this.y);
        }
        if(this.pageX || this.pageY){
            this.setPagePosition(this.pageX, this.pageY);
        }
    },

    /**
     * Force the component's size to recalculate based on the underlying element's current height and width.
     * @returns {Ext.BoxComponent} this
     */
    syncSize : function(){
        delete this.lastSize;
        this.setSize(this.el.getWidth(), this.el.getHeight());
        return this;
    },

    /**
     * Called after the component is resized, this method is empty by default but can be implemented by any
     * subclass that needs to perform custom logic after a resize occurs.
     * @param {Number} adjWidth The box-adjusted width that was set
     * @param {Number} adjHeight The box-adjusted height that was set
     * @param {Number} rawWidth The width that was originally specified
     * @param {Number} rawHeight The height that was originally specified
     */
    onResize : function(adjWidth, adjHeight, rawWidth, rawHeight){

    },

    /**
     * Called after the component is moved, this method is empty by default but can be implemented by any
     * subclass that needs to perform custom logic after a move occurs.
     * @param {Number} x The new x position
     * @param {Number} y The new y position
     */
    onPosition : function(x, y){

    },

    // private
    adjustSize : function(w, h){
        if(this.autoWidth){
            w = 'auto';
        }
        if(this.autoHeight){
            h = 'auto';
        }
        return {width : w, height: h};
    },

    // private
    adjustPosition : function(x, y){
        return {x : x, y: y};
    }
});