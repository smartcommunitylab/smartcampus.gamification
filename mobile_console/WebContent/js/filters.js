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
		return input.slice(start);
	};
}).filter('nullString', function() {
	return function(input) {		
		if(input=="null")
			return "";
		else
			return input;
	};
});
