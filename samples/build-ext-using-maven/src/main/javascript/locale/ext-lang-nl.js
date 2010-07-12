/*
 * Ext JS Library 1.1.1
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://www.extjs.com/license
 */

/*
 * Dutch Translations
 * by Bas van Oostveen (04 April 2007)
 */

/* Ext Core translations */
Ext.UpdateManager.defaults.indicatorText = '<div class="loading-indicator">Bezig met laden...</div>';

/* Ext single string translations */
if(Ext.View){
    Ext.View.prototype.emptyText = "";
}

if(Ext.grid.Grid){
    Ext.grid.Grid.prototype.ddText = "{0} geselecteerde rij(en)";
}

if(Ext.TabPanelItem){
    Ext.TabPanelItem.prototype.closeText = "Sluit dit tabblad";
}

if(Ext.form.Field){
    Ext.form.Field.prototype.invalidText = "De waarde in dit veld is onjuist";
}

if(Ext.LoadMask){
    Ext.LoadMask.prototype.msg = "Bezig met laden...";
}

/* Javascript month and days translations */
Date.monthNames = [
   "Januari",
   "Februari",
   "Maart",
   "April",
   "Mei",
   "Juni",
   "Juli",
   "Augustus",
   "September",
   "Oktober",
   "November",
   "December"
];

Date.dayNames = [
   "Zondag",
   "Maandag",
   "Dinsdag",
   "Woensdag",
   "Donderdag",
   "Vrijdag",
   "Zaterdag"
];

/* Ext components translations */
if(Ext.MessageBox){
    Ext.MessageBox.buttonText = {
       ok     : "OK",
       cancel : "Annuleren",
       yes    : "Ja",
       no     : "Nee"
    };
}

if(Ext.util.Format){
    Ext.util.Format.date = function(v, format){
       if(!v) return "";
       if(!(v instanceof Date)) v = new Date(Date.parse(v));
       return v.dateFormat(format || "d-m-y");
    };
}

if(Ext.DatePicker){
    Ext.apply(Ext.DatePicker.prototype, {
       todayText         : "Vandaag",
       minText           : "Deze datum is eerder dan de minimum datum",
       maxText           : "Deze datum is later dan de maximum datum",
       disabledDaysText  : "",
       disabledDatesText : "",
       monthNames	 : Date.monthNames,
       dayNames		 : Date.dayNames,
       nextText          : 'Volgende Maand (Control+Rechts)',
       prevText          : 'Vorige Maand (Control+Links)',
       monthYearText     : 'Kies een maand (Control+Omhoog/Beneden volgend/vorige jaar)',
       todayTip          : "{0} (Spatie)",
       format            : "d-m-y",
       okText		 : "&#160;OK&#160;",
       cancelText	 : "Annuleren",
       startDay          : 1
    });
}

if(Ext.PagingToolbar){
    Ext.apply(Ext.PagingToolbar.prototype, {
       beforePageText : "Pagina",
       afterPageText  : "van {0}",
       firstText      : "Eerste Pagina",
       prevText       : "Vorige Pagina",
       nextText       : "Volgende Pagina",
       lastText       : "Laatste Pagina",
       refreshText    : "Ververs",
       displayMsg     : "Getoond {0} - {1} van {2}",
       emptyMsg       : 'Geen gegeven om weer te geven'
    });
}

if(Ext.form.TextField){
    Ext.apply(Ext.form.TextField.prototype, {
       minLengthText : "De minimale lengte voor dit veld is {0}",
       maxLengthText : "De maximale lengte voor dit veld is {0}",
       blankText     : "Dit veld is verplicht",
       regexText     : "",
       emptyText     : null
    });
}

if(Ext.form.NumberField){
    Ext.apply(Ext.form.NumberField.prototype, {
       minText : "De minimale waarde voor dit veld is {0}",
       maxText : "De maximale waarde voor dit veld is {0}",
       nanText : "{0} is geen geldig getal"
    });
}

if(Ext.form.DateField){
    Ext.apply(Ext.form.DateField.prototype, {
       disabledDaysText  : "Uitgeschakeld",
       disabledDatesText : "Uitgeschakeld",
       minText           : "De datum in dit veld moet na {0} liggen",
       maxText           : "De datum in dit veld moet voor {0} liggen",
       invalidText       : "{0} is geen geldige datum - formaat voor datum is {1}",
       format            : "d/m/y"
    });
}

if(Ext.form.ComboBox){
    Ext.apply(Ext.form.ComboBox.prototype, {
       loadingText       : "Bezig met laden...",
       valueNotFoundText : undefined
    });
}

if(Ext.form.VTypes){
    Ext.apply(Ext.form.VTypes, {
       emailText    : 'Dit veld moet een e-mail adres zijn in het formaat "gebruiker@domein.nl"',
       urlText      : 'Dit veld moet een URL zijn in het formaat "http:/'+'/www.domein.nl"',
       alphaText    : 'Dit veld mag alleen letters en _ bevatten',
       alphanumText : 'Dit veld mag alleen letters, cijfers en _ bevatten'
    });
}

if(Ext.form.HtmlEditor){
   Ext.apply(Ext.form.HtmlEditor.prototype, {
	createLinkText : 'Vul hier het Internet adres voor de link in:',
	buttonTips : {
            bold : {
                title: 'Vet (Ctrl+B)',
                text: 'Maak de geselecteerde tekst vet gedrukt.',
                cls: 'x-html-editor-tip'
            },
            italic : {
                title: 'Cursief (Ctrl+I)',
                text: 'Maak de geselecteerde tekst cursief.',
                cls: 'x-html-editor-tip'
            },
            underline : {
                title: 'Onderstrepen (Ctrl+U)',
                text: 'Onderstreep de geselecteerde tekst.',
                cls: 'x-html-editor-tip'
            },
            increasefontsize : {
                title: 'Tekst Vergroten',
                text: 'Vergroot het lettertype.',
                cls: 'x-html-editor-tip'
            },
            decreasefontsize : {
                title: 'Tekst Verkleinen',
                text: 'Verklein het lettertype.',
                cls: 'x-html-editor-tip'
            },
            backcolor : {
                title: 'Tekst Achtergrond Kleur',
                text: 'Verander de achtergrond kleur van de geselecteerde tekst.',
                cls: 'x-html-editor-tip'
            },
            forecolor : {
                title: 'Lettertype Kleur',
                text: 'Verander de kleur van de geselecteerde tekst.',
                cls: 'x-html-editor-tip'
            },
            justifyleft : {
                title: 'Tekst Links Uitlijnen',
                text: 'Lijn de tekst links uit.',
                cls: 'x-html-editor-tip'
            },
            justifycenter : {
                title: 'Tekst Centreren',
                text: 'Centreer de tekst in de editor.',
                cls: 'x-html-editor-tip'
            },
            justifyright : {
                title: 'Tekst Richts Uitlijnen',
                text: 'Lijn de tekst rechts uit.',
                cls: 'x-html-editor-tip'
            },
            insertunorderedlist : {
                title: 'Punten Lijst',
                text: 'Begin een ongenummerde lijst.',
                cls: 'x-html-editor-tip'
            },
            insertorderedlist : {
                title: 'Genummerde Lijst',
                text: 'Begin een genummerde lijst.',
                cls: 'x-html-editor-tip'
            },
            createlink : {
                title: 'Hyperlink',
                text: 'Maak van de geselecteerde tekst een hyperlink.',
                cls: 'x-html-editor-tip'
            },
            sourceedit : {
                title: 'Bron Aanpassen',
                text: 'Schakel modus over naar bron aanpassen.',
                cls: 'x-html-editor-tip'
            }
        }
    });
}

if(Ext.grid.GridView){
    Ext.apply(Ext.grid.GridView.prototype, {
       sortAscText  : "Sorteer Oplopend",
       sortDescText : "Sorteer Aflopend",
       lockText     : "Kolom Vastzetten",
       unlockText   : "Kolom Vrijgeven",
       columnsText  : "Kolommen"
    });
}

if(Ext.grid.PropertyColumnModel){
    Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
       nameText   : "Naam",
       valueText  : "Waarde",
       dateFormat : "Y-m-j"
    });
}

if(Ext.SplitLayoutRegion){
    Ext.apply(Ext.SplitLayoutRegion.prototype, {
       splitTip            : "Sleep om grootte aan te passen.",
       collapsibleSplitTip : "Sleep om grootte aan te passen. Dubbel klikken om te verbergen."
    });
}
