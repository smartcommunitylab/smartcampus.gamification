'use strict';

/* Filters */

angular.module('cpFilters', []).filter('truncate', function() {
	return function(text, length, end) {
		if (isNaN(length))
		length = 60;

		if (end === undefined)
			end = "...";

		if (text.length <= length || text.length - end.length <= length) {
			return text;
		} else {
			return String(text).substring(0, length - end.length) + end;
		}		

	};
}).filter('dateformat', function() {
	return function(text, length, end) {
		return new Date(text).toLocaleString();
	};
}).filter('startFrom', function() {
	return function(input, start) {
		start = +start; // parse to int
		if(input == null){
			return input;
		}
		return input.slice(start);
	};
}).filter('nullString', function() {
	return function(input) {		
		if(input=="null")
			return "";
		else
			return input;
	};
}).filter('getById', function() {
	return function(input, id) {
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (+input[i].id == +id) {
		        return input[i];
		    }
		}
		return null;
	};
}).filter('getByCode', function() {
	return function(input, code) {
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (+input[i].value == +code) {
		        return input[i];
		    }
		}
		return null;
	};
}).filter('dateToMillis', function() {
	return function(data){
		if(data=="")
			return "";
		else
			return Date.parse(data);
	};
}).filter('boolToString', function() {
	return function(input){
		return input ? 'SI' : 'NO';
	};
}).filter('cleanStrangeValues', function() {
	return function(input){
		if(input == null || input==""){
			return "";
		}
		else
		{
			var correct = input;
			if(correct.indexOf("&#9;") > -1){
				var end = correct.indexOf("&#9;");
				return correct.substring(0,end);
			}
			return input;
		}
	};
}).filter('idToMunicipality', function() {
	return function(input, id){
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (+input[i].idObj == +id) {
		        return input[i];
		    }
		}
		return null;
	};
}).filter('idToDescComune', function() {
	return function(id, input){
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (input[i].idObj == id) {
		        return input[i];
		    }
		}
		return null;
	};
}).filter('descComuneToId', function() {
	return function(desc, input){
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (input[i].descrizione == desc) {
		        return input[i];
		    }
		}
		return null;
	};
}).filter('codeToName', function() {
	return function(code, input){
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (input[i].value == code) {
		        return input[i].name;
		    }
		}
		return null;
	};
}).filter('millisToYears', function() {
	return function(millis){
		var years = 0;
		var totMillisInYear = 1000 * 60 * 60 * 24 * 360;
		var anniRes = millis/totMillisInYear;
       	years = Math.floor(anniRes);
		return (years > 0) ? years : "< 1";
	};
}).filter('millisToMonths', function() {
	return function(millis){
		var months = 0;
		var totMillisInMonth = 1000 * 60 * 60 * 24 * 30;
		var mesiRes = millis/totMillisInMonth;
       	months = Math.floor(mesiRes);
		return (months > 0) ? months : "< 1";
	};
}).filter('valueToTitle', function() {
	return function(value, input){
		var i=0, len=input.length;
		for (; i<len; i++) {
			if (input[i].value == value) {
		        return input[i].title;
		    }
		}
		return null;
	};
});
