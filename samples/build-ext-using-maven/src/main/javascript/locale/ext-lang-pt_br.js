/*
 * Ext JS Library 1.1.1
 * Copyright(c) 2006-2007, Ext JS, LLC.
 * licensing@extjs.com
 * 
 * http://www.extjs.com/license
 */

/*
 * Portuguese/Brazil Translation by Weber Souza
 * 08 April 2007
 *
 * Actualized by Allan Brazute Alves (EthraZa)
 * 02 August 2007
 */

Ext.UpdateManager.defaults.indicatorText = '<div class="loading-indicator">Carregando...</div>';

if(Ext.View){
   Ext.View.prototype.emptyText = "";
}

if(Ext.grid.Grid){
   Ext.grid.Grid.prototype.ddText = "{0} linha(s) selecionada(s)";
}

if(Ext.TabPanelItem){
   Ext.TabPanelItem.prototype.closeText = "Fechar";
}

if(Ext.form.Field){
   Ext.form.Field.prototype.invalidText = "O valor para este campo &eacute; inv&aacute;lido";
}

if(Ext.LoadMask){
    Ext.LoadMask.prototype.msg = "Carregando...";
}

Date.monthNames = [
   "Janeiro",
   "Fevereiro",
   "Mar&ccedil;o",
   "Abril",
   "Maio",
   "Junho",
   "Julho",
   "Agosto",
   "Setembro",
   "Outubro",
   "Novembro",
   "Dezembro"
];

Date.dayNames = [
   "Domingo",
   "Segunda",
   "Ter&ccedil;a",
   "Quarta",
   "Quinta",
   "Sexta",
   "S&aacute;bado"
];

if(Ext.MessageBox){
   Ext.MessageBox.buttonText = {
      ok     : "OK",
      cancel : "Cancelar",
      yes    : "Sim",
      no     : "N&atilde;o"
   };
}

if(Ext.util.Format){
   Ext.util.Format.date = function(v, format){
      if(!v) return "";
      if(!(v instanceof Date)) v = new Date(Date.parse(v));
      return v.dateFormat(format || "m/d/Y");
   };
}

if(Ext.DatePicker){
   Ext.apply(Ext.DatePicker.prototype, {
      todayText         : "Hoje",
      minText           : "Esta data &eacute; anterior a menor data",
      maxText           : "Esta data &eacute; posterior a maior data",
      disabledDaysText  : "",
      disabledDatesText : "",
      monthNames        : Date.monthNames,
      dayNames          : Date.dayNames,
      nextText          : 'Pr&oacute;ximo M&ecirc;s (Control+Direita)',
      prevText          : 'M&ecirc;s Anterior (Control+Esquerda)',
      monthYearText     : 'Escolha um M&ecirc;s (Control+Cima/Baixo para mover entre os anos)',
      todayTip          : "{0} (Espa&ccedil;o)",
      format            : "m/d/y"
   });
}

if(Ext.PagingToolbar){
   Ext.apply(Ext.PagingToolbar.prototype, {
      beforePageText : "P&aacute;gina",
      afterPageText  : "de {0}",
      firstText      : "Primeira P&aacute;gina",
      prevText       : "P&aacute;gina Anterior",
      nextText       : "Pr&oacute;xima P&aacute;gina",
      lastText       : "&Uacute;ltima P&aacute;gina",
      refreshText    : "Atualizar",
      displayMsg     : "<b>{0} &agrave; {1} de {2} registro(s)</b>",
      emptyMsg       : 'Sem registros para exibir'
   });
}

if(Ext.form.TextField){
   Ext.apply(Ext.form.TextField.prototype, {
      minLengthText : "O tamanho m&iacute;nimo para este campo &eacute; {0}",
      maxLengthText : "O tamanho m&aacute;ximo para este campo &eacute; {0}",
      blankText     : "Este campo &eacute; obrigat&oacute;rio.",
      regexText     : "",
      emptyText     : null
   });
}

if(Ext.form.NumberField){
   Ext.apply(Ext.form.NumberField.prototype, {
      minText : "O valor m&iacute;nimo para este campo &eacute; {0}",
      maxText : "O valor m&aacute;ximo para este campo &eacute; {0}",
      nanText : "{0} n&atilde;o &eacute; um n&uacute;mero v&aacute;lido"
   });
}

if(Ext.form.DateField){
   Ext.apply(Ext.form.DateField.prototype, {
      disabledDaysText  : "Desabilitado",
      disabledDatesText : "Desabilitado",
      minText           : "A data deste campo deve ser posterior a {0}",
      maxText           : "A data deste campo deve ser anterior a {0}",
      invalidText       : "{0} n&atilde;o &eacute; uma data v&aacute;lida - deve ser informado no formato {1}",
      format            : "d/m/Y"
   });
}

if(Ext.form.ComboBox){
   Ext.apply(Ext.form.ComboBox.prototype, {
      loadingText       : "Carregando...",
      valueNotFoundText : undefined
   });
}

if(Ext.form.VTypes){
   Ext.apply(Ext.form.VTypes, {
      emailText    : 'Este campo deve ser um endere&ccedil;o de e-mail v&aacute;lido, no formado "usuario@dominio.com.br"',
      urlText      : 'Este campo deve ser uma URL no formato "http:/'+'/www.dominio.com.br"',
      alphaText    : 'Este campo deve conter apenas letras e _',
      alphanumText : 'Este campo deve conter apenas letras, n&uacute;meros e _'
   });
}

if(Ext.grid.GridView){
   Ext.apply(Ext.grid.GridView.prototype, {
      sortAscText  : "Ordem Ascendente",
      sortDescText : "Ordem Descendente",
      lockText     : "Bloquear Coluna",
      unlockText   : "Desbloquear Coluna",
      columnsText  : "Colunas"
   });
}

if(Ext.grid.PropertyColumnModel){
   Ext.apply(Ext.grid.PropertyColumnModel.prototype, {
      nameText   : "Nome",
      valueText  : "Valor",
      dateFormat : "d/m/Y"
   });
}

if(Ext.SplitLayoutRegion){
   Ext.apply(Ext.SplitLayoutRegion.prototype, {
      splitTip            : "Arraste para redimencionar.",
      collapsibleSplitTip : "Arraste para redimencionar. Duplo clique para esconder."
   });
}
