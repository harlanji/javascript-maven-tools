function popUp(url) {
	window.open(url,"redRef","height=600,width=550,channelmode=0,dependent=0,directories=0,fullscreen=0,location=0,menubar=0," + "resizable=0,scrollbars=1,status=1,toolbar=0");
}

function toggleHelp(show,hide,block) {
	if (document.getElementById(block).style.display == "none") {
		document.getElementById(show).style.display  = "none";
		document.getElementById(hide).style.display = "inline";
	    new Effect.BlindDown(block, {duration: 0.4,
	      afterFinish: function() {
	        Element.undoClipping(block);
	        $(block).style.width = "auto";
	        $(block).style.height = "auto";
	      }})
	} else {
		document.getElementById(show).style.display  = "inline";
		document.getElementById(hide).style.display = "none";
	    new Effect.BlindUp(block, {duration: 0.4,
	      afterFinish: function() {
	        Element.undoClipping(block);
	        Element.hide(block);
	      }})
	}
}

function newPage(web_address) {
	if (val = prompt("Enter the name of the page you would like to create:", "")) {
		document.location = "/" + web_address + "/page/new/" + encodeURIComponent(val);
		return false;
	}

	return false;
}

function newList(web_address, page_id) {
	if (val = prompt("Enter the name of the task list you would like to create:", "")) {
		new Ajax.Request('/' + web_address + '/tasks/create/' + page_id + '?title=' + encodeURIComponent(val), {asynchronous:true, evalScripts:true});
		return false;
	}

	return false;
}

function toggleTags() {
	var elements = document.getElementsByClassName('display_tag');

	for(var i = 0; i < elements.length; i++) {
 		if (elements[i].style.display == "none") {
			elements[i].style.display = "block";
		} else {
			elements[i].style.display = "none";
		}
	}	
}

function hideByClass(cla) {
	var elements = document.getElementsByClassName(cla);
	
	for(var i = 0; i < elements.length; i++) {
		elements[i].style.display = "none";
	}	
}

function showByClass(cla) {
	var elements = document.getElementsByClassName(cla);
	
	for(var i = 0; i < elements.length; i++) {
		elements[i].style.display = "block";
	}	
}

function toggleByClass(cla) {
	var elements = document.getElementsByClassName(cla);
	
	for(var i = 0; i < elements.length; i++) {
 		if (elements[i].style.display == "none") {
			new Effect.Appear(elements[i]);
		} else {
			new Effect.Fade(elements[i]);
		}
	}	
}

// Make sure they are all set to the same.  Called after creating a new item, among other places.
function checkToggleSync(cla, check_id) {
	var elements = document.getElementsByClassName(cla);

	// grab the first element.
	if (elements[0].style.display == "none") {
		new Effect.Fade(check_id);
	} else {
		new Effect.Appear(check_id);
	}

}

function reorder(id, request_addr) {
	if ($('task_list_' + id + '_reorder').style.display == 'none') {
		Element.show('task_list_' + id + '_reorder');
		Element.hide('task_list_' + id + '_master_completed');
		Element.hide('master_add_item_' + id);
		for (var i = 0; $('task_list_' + id + '_items').childNodes[i]; i++) {
			Element.addClassName($('task_list_' + id + '_items').childNodes[i].id, 'ReorderList');
		}

		Sortable.create('task_list_' + id + '_items', {onUpdate:function(){new Ajax.Request(request_addr, {asynchronous:true, evalScripts:true, onComplete:function(request){new Effect.Highlight('task_list_' + id + '_items',{});}, parameters:Sortable.serialize('task_list_' + id + '_items')})}})
	} else {
		Element.hide('task_list_' + id + '_reorder');

		Element.show('task_list_' + id + '_master_completed');
		Element.show('master_add_item_' + id);
		for (var i = 0; $('task_list_' + id + '_items').childNodes[i]; i++) {
			Element.removeClassName($('task_list_' + id + '_items').childNodes[i].id, 'ReorderList');
		}

		Sortable.destroy('task_list_' + id + '_items');
	}
}

var taskEditors = {};
function edit_tasks(id, request_addr) {
	if ($('task_list_' + id + '_edit').style.display == 'none') {
		Element.show('task_list_' + id + '_edit');
		Element.hide('task_list_' + id + '_master_completed');
		Element.hide('master_add_item_' + id);

		for (var i = 0; $('task_list_' + id + '_items').childNodes[i]; i++) {
			if ($('task_list_' + id + '_items').childNodes[i].id) {
				taskEditors[i] = new Ajax.InPlaceEditor($('task_list_' + id + '_items').childNodes[i].id + '_content', '/' + request_addr + '/tasks/set_task_item_content/' + $('task_list_' + id + '_items').childNodes[i].id, {loadTextURL:'/' + request_addr + '/tasks/task_item_content_unformatted_text/' + $('task_list_' + id + '_items').childNodes[i].id})
			}
		}
	} else {
		Element.hide('task_list_' + id + '_edit');
		Element.show('task_list_' + id + '_master_completed');
		Element.show('master_add_item_' + id);

		for (var i = 0; $('task_list_' + id + '_items').childNodes[i]; i++) {
			if ($('task_list_' + id + '_items').childNodes[i].id) {
				taskEditors[i].dispose();
			}
		}
	}
}

function toggleListDetail() {
	var compl = document.getElementsByClassName('CompletedByline');
	var act = document.getElementsByClassName('AddedByline');
	var yay = new Date();

	yay.setTime(Date.parse('March, 15 2008 07:04:11'));

	if (compl.length != 0) {
		if (compl[0].style.display == 'none') {
			setCookie('show_list_detail', 'false', yay);
		} else {
			setCookie('show_list_detail', 'true', yay);
		}
	} else if (act.length != 0) {
		if (act[0].style.display == 'none') {
			setCookie('show_list_detail', 'false', yay);
		} else {
			setCookie('show_list_detail', 'true', yay);
		}
	}
}

function jumpto(x){
	document.location.href = x;
}

function toggleWikis() {
	if (document.getElementById("wikiList").style.display == "none") {
		document.getElementById("wikiList").style.display  = "block";
		document.getElementById("show_wiki_list").style.display  = "none";
		document.getElementById("hide_wiki_list").style.display = "inline";
	} else {
		document.getElementById("wikiList").style.display  = "none";
		document.getElementById("show_wiki_list").style.display  = "inline";
		document.getElementById("hide_wiki_list").style.display = "none";
	}
	
}

function showTrashIcon(asset_id) {
	if (!Draggables.activeDraggable) {
		Element.show(asset_id);
	}
}

function hideTrashIcon(asset_id) {
	Element.hide(asset_id);
}

function showRestoreIcon(asset_id) {
	Element.show('list_restore_icon_' + asset_id);
}

function hideRestoreIcon(asset_id) {
	Element.hide('list_restore_icon_' + asset_id);
}

var trashHighlights = new Array();
var listHighlights = new Array();
var iconHighlights = new Array();

function waitToHighlight(what, asset_id) {
	what[what.length] = asset_id;
}

function runHighlights(what) {
	if (what.length == 0) {
		return;
	}
	
	for (var i = 0; i < what.length; i++) {
		new Effect.Highlight(what[i], {duration: 5});
	}

	$A(what).clear();
}

function countrySelect(value) {
	if (value == 'US') {
		
	} else if (value == 'CA') {
		Element.hide('account_state');
		Element.show('account_province');
	} else {
		Effect.Appear('IntlMessage');
		$('city_input_text').innerHTML = 'City / Municipality';
	}
}

function UpdateAmounts(plan, period) {
	if (plan == 'Free') {
		var amount = "0.00";
	} else if (plan == 'Personal') {
		var amount = "4.95";
	} else if (plan == 'Power') {
		var amount = "9.95";
	} else if (plan == 'Team') {
		var amount = "14.95";
	} else {
		var amount = "NoWay";
	}

	switch(period) {
		case 'Monthly': var retval = "$" + amount + "/month"; break
		case 'HalfYear': var retval = "$" + ((amount*6)-amount).toFixed(2); retval += "/half-year"; break 
		case 'Yearly': var retval = "$" + ((amount*12)-(amount*2)).toFixed(2); retval += "/yearly"; break 
	}

	$('to_amount').innerHTML = retval;
	new Effect.Shake('to_amount');
}

function show_dates_as_local_time() {
	var spans = document.getElementsByTagName('span');
	for (var i=0; i<spans.length; i++) {
		if (spans[i].className.match(/\bstiki_date\b/i)) {
			spans[i].innerHTML = get_local_time_for_date(spans[i].title);
		}
	}
}

function get_local_time_for_date(time) {
	system_date = new Date(time);
	user_date = new Date();
	delta_minutes = Math.floor((user_date - system_date) / (60 * 1000));
	if (Math.abs(delta_minutes) <= (8*7*24*60)) { // eight weeks... I'm lazy to count days for longer than that
		distance = distance_of_time_in_words(delta_minutes);
		if (delta_minutes < 0) {
			return distance + ' from now';
		} else {
			return distance + ' ago';
		}
	} else {
		return 'on ' + system_date.toLocaleDateString();
	}
}

// a vague copy of rails' inbuilt function, 
// but a bit more friendly with the hours.
function distance_of_time_in_words(minutes) {
	if (minutes.isNaN) return "";
	minutes = Math.abs(minutes);
	if (minutes < 1) return ('less than a minute');
	if (minutes < 50) return (minutes + ' minute' + (minutes == 1 ? '' : 's'));
	if (minutes < 90) return ('about one hour');
	if (minutes < 1080) return (Math.round(minutes / 60) + ' hours');
	if (minutes < 1440) return ('one day');
	if (minutes < 2880) return ('about one day');
	else return (Math.round(minutes / 1440) + ' days')
}

function cleanAuthorName() {
  if (document.getElementById('authorName').value == "") {
    document.getElementById('authorName').value = 'Anonymous';
  }
}

/*
 * Dynamic Table of Contents script
 * by Matt Whitlock <http://www.whitsoftdev.com/>
 */

function createLink(href, innerHTML) {
  var a = document.createElement("a");
  a.setAttribute("href", href);
  a.innerHTML = innerHTML;
  return a;
}

function generateTOC(toc) {
  var i2 = 0, i3 = 0, i4 = 0;
  toc = toc.appendChild(document.createElement("ul"));
  for (var i = 0; i < $('revision').childNodes.length; ++i) {
    var node = $('revision').childNodes[i];
    var tagName = node.nodeName.toLowerCase();
    if (tagName == "h4") {
      ++i4;
      if (i4 == 1) toc.lastChild.lastChild.lastChild.appendChild(document.createElement("ul"));
      var section = i2 + "." + i3 + "." + i4;
      node.insertBefore(document.createTextNode(section + ". "), node.firstChild);
      node.id = "section" + section;
      toc.lastChild.lastChild.lastChild.lastChild.appendChild(document.createElement("li")).appendChild(createLink("#section" + section, node.innerHTML));
    } else if (tagName == "h3") {
      ++i3, i4 = 0;
      if (i3 == 1) toc.lastChild.appendChild(document.createElement("ul"));
      var section = i2 + "." + i3;
      node.insertBefore(document.createTextNode(section + ". "), node.firstChild);
      node.id = "section" + section;
      toc.lastChild.lastChild.appendChild(document.createElement("li")).appendChild(createLink("#section" + section, node.innerHTML));
    } else if (tagName == "h2") {
      ++i2, i3 = 0, i4 = 0;
      var section = i2;
      node.insertBefore(document.createTextNode(section + ". "), node.firstChild);
      node.id = "section" + section;
      toc.appendChild(h2item = document.createElement("li")).appendChild(createLink("#section" + section, node.innerHTML));
    }
  }
}