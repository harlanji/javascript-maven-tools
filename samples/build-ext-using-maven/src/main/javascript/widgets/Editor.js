/*
 * Ext JS Library 1.1.1
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://www.extjs.com/license
 */

/**
 * @class Ext.Editor
 * @extends Ext.Component
 * A base editor field that handles displaying/hiding on demand and has some built-in sizing and event handling logic.
 * @constructor
 * Create a new Editor
 * @param {Ext.form.Field} field The Field object (or descendant)
 * @param {Object} config The config object
 */
Ext.Editor = function(field, config){
    Ext.Editor.superclass.constructor.call(this, config);
    this.field = field;
    this.addEvents({
        /**
	     * @event beforestartedit
	     * Fires when editing is initiated, but before the value changes.  Editing can be canceled by returning
	     * false from the handler of this event.
	     * @param {Editor} this
	     * @param {Ext.Element} boundEl The underlying element bound to this editor
	     * @param {Mixed} value The field value being set
	     */
        "beforestartedit" : true,
        /**
	     * @event startedit
	     * Fires when this editor is displayed
	     * @param {Ext.Element} boundEl The underlying element bound to this editor
	     * @param {Mixed} value The starting field value
	     */
        "startedit" : true,
        /**
	     * @event beforecomplete
	     * Fires after a change has been made to the field, but before the change is reflected in the underlying
	     * field.  Saving the change to the field can be canceled by returning false from the handler of this event.
	     * Note that if the value has not changed and ignoreNoChange = true, the editing will still end but this
	     * event will not fire since no edit actually occurred.
	     * @param {Editor} this
	     * @param {Mixed} value The current field value
	     * @param {Mixed} startValue The original field value
	     */
        "beforecomplete" : true,
        /**
	     * @event complete
	     * Fires after editing is complete and any changed value has been written to the underlying field.
	     * @param {Editor} this
	     * @param {Mixed} value The current field value
	     * @param {Mixed} startValue The original field value
	     */
        "complete" : true,
        /**
         * @event specialkey
         * Fires when any key related to navigation (arrows, tab, enter, esc, etc.) is pressed.  You can check
         * {@link Ext.EventObject#getKey} to determine which key was pressed.
         * @param {Ext.form.Field} this
         * @param {Ext.EventObject} e The event object
         */
        "specialkey" : true
    });
};

Ext.extend(Ext.Editor, Ext.Component, {
    /**
     * @cfg {Boolean/String} autosize
     * True for the editor to automatically adopt the size of the underlying field, "width" to adopt the width only,
     * or "height" to adopt the height only (defaults to false)
     */
    /**
     * @cfg {Boolean} revertInvalid
     * True to automatically revert the field value and cancel the edit when the user completes an edit and the field
     * validation fails (defaults to true)
     */
    /**
     * @cfg {Boolean} ignoreNoChange
     * True to skip the the edit completion process (no save, no events fired) if the user completes an edit and
     * the value has not changed (defaults to false).  Applies only to string values - edits for other data types
     * will never be ignored.
     */
    /**
     * @cfg {Boolean} hideEl
     * False to keep the bound element visible while the editor is displayed (defaults to true)
     */
    /**
     * @cfg {Mixed} value
     * The data value of the underlying field (defaults to "")
     */
    value : "",
    /**
     * @cfg {String} alignment
     * The position to align to (see {@link Ext.Element#alignTo} for more details, defaults to "c-c?").
     */
    alignment: "c-c?",
    /**
     * @cfg {Boolean/String} shadow "sides" for sides/bottom only, "frame" for 4-way shadow, and "drop"
     * for bottom-right shadow (defaults to "frame")
     */
    shadow : "frame",
    /**
     * @cfg {Boolean} constrain True to constrain the editor to the viewport
     */
    constrain : false,
    /**
     * @cfg {Boolean} completeOnEnter True to complete the edit when the enter key is pressed (defaults to false)
     */
    completeOnEnter : false,
    /**
     * @cfg {Boolean} cancelOnEsc True to cancel the edit when the escape key is pressed (defaults to false)
     */
    cancelOnEsc : false,
    /**
     * @cfg {Boolean} updateEl True to update the innerHTML of the bound element when the update completes (defaults to false)
     */
    updateEl : false,

    // private
    onRender : function(ct, position){
        this.el = new Ext.Layer({
            shadow: this.shadow,
            cls: "x-editor",
            parentEl : ct,
            shim : this.shim,
            shadowOffset:4,
            id: this.id,
            constrain: this.constrain
        });
        this.el.setStyle("overflow", Ext.isGecko ? "auto" : "hidden");
        if(this.field.msgTarget != 'title'){
            this.field.msgTarget = 'qtip';
        }
        this.field.render(this.el);
        if(Ext.isGecko){
            this.field.el.dom.setAttribute('autocomplete', 'off');
        }
        this.field.on("specialkey", this.onSpecialKey, this);
        if(this.swallowKeys){
            this.field.el.swallowEvent(['keydown','keypress']);
        }
        this.field.show();
        this.field.on("blur", this.onBlur, this);
        if(this.field.grow){
            this.field.on("autosize", this.el.sync,  this.el, {delay:1});
        }
    },

    onSpecialKey : function(field, e){
        if(this.completeOnEnter && e.getKey() == e.ENTER){
            e.stopEvent();
            this.completeEdit();
        }else if(this.cancelOnEsc && e.getKey() == e.ESC){
            this.cancelEdit();
        }else{
            this.fireEvent('specialkey', field, e);
        }
    },

    /**
     * Starts the editing process and shows the editor.
     * @param {String/HTMLElement/Element} el The element to edit
     * @param {String} value (optional) A value to initialize the editor with. If a value is not provided, it defaults
      * to the innerHTML of el.
     */
    startEdit : function(el, value){
        if(this.editing){
            this.completeEdit();
        }
        this.boundEl = Ext.get(el);
        var v = value !== undefined ? value : this.boundEl.dom.innerHTML;
        if(!this.rendered){
            this.render(this.parentEl || document.body);
        }
        if(this.fireEvent("beforestartedit", this, this.boundEl, v) === false){
            return;
        }
        this.startValue = v;
        this.field.setValue(v);
        if(this.autoSize){
            var sz = this.boundEl.getSize();
            switch(this.autoSize){
                case "width":
                this.setSize(sz.width,  "");
                break;
                case "height":
                this.setSize("",  sz.height);
                break;
                default:
                this.setSize(sz.width,  sz.height);
            }
        }
        this.el.alignTo(this.boundEl, this.alignment);
        this.editing = true;
        if(Ext.QuickTips){
            Ext.QuickTips.disable();
        }
        this.show();
    },

    /**
     * Sets the height and width of this editor.
     * @param {Number} width The new width
     * @param {Number} height The new height
     */
    setSize : function(w, h){
        this.field.setSize(w, h);
        if(this.el){
            this.el.sync();
        }
    },

    /**
     * Realigns the editor to the bound field based on the current alignment config value.
     */
    realign : function(){
        this.el.alignTo(this.boundEl, this.alignment);
    },

    /**
     * Ends the editing process, persists the changed value to the underlying field, and hides the editor.
     * @param {Boolean} remainVisible Override the default behavior and keep the editor visible after edit (defaults to false)
     */
    completeEdit : function(remainVisible){
        if(!this.editing){
            return;
        }
        var v = this.getValue();
        if(this.revertInvalid !== false && !this.field.isValid()){
            v = this.startValue;
            this.cancelEdit(true);
        }
        if(String(v) === String(this.startValue) && this.ignoreNoChange){
            this.editing = false;
            this.hide();
            return;
        }
        if(this.fireEvent("beforecomplete", this, v, this.startValue) !== false){
            this.editing = false;
            if(this.updateEl && this.boundEl){
                this.boundEl.update(v);
            }
            if(remainVisible !== true){
                this.hide();
            }
            this.fireEvent("complete", this, v, this.startValue);
        }
    },

    // private
    onShow : function(){
        this.el.show();
        if(this.hideEl !== false){
            this.boundEl.hide();
        }
        this.field.show();
        if(Ext.isIE && !this.fixIEFocus){ // IE has problems with focusing the first time
            this.fixIEFocus = true;
            this.deferredFocus.defer(50, this);
        }else{
            this.field.focus();
        }
        this.fireEvent("startedit", this.boundEl, this.startValue);
    },

    deferredFocus : function(){
        if(this.editing){
            this.field.focus();
        }
    },

    /**
     * Cancels the editing process and hides the editor without persisting any changes.  The field value will be
     * reverted to the original starting value.
     * @param {Boolean} remainVisible Override the default behavior and keep the editor visible after
     * cancel (defaults to false)
     */
    cancelEdit : function(remainVisible){
        if(this.editing){
            this.setValue(this.startValue);
            if(remainVisible !== true){
                this.hide();
            }
        }
    },

    // private
    onBlur : function(){
        if(this.allowBlur !== true && this.editing){
            this.completeEdit();
        }
    },

    // private
    onHide : function(){
        if(this.editing){
            this.completeEdit();
            return;
        }
        this.field.blur();
        if(this.field.collapse){
            this.field.collapse();
        }
        this.el.hide();
        if(this.hideEl !== false){
            this.boundEl.show();
        }
        if(Ext.QuickTips){
            Ext.QuickTips.enable();
        }
    },

    /**
     * Sets the data value of the editor
     * @param {Mixed} value Any valid value supported by the underlying field
     */
    setValue : function(v){
        this.field.setValue(v);
    },

    /**
     * Gets the data value of the editor
     * @return {Mixed} The data value
     */
    getValue : function(){
        return this.field.getValue();
    }
});