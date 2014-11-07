'use strict';

/* Services */
var cpServices = angular.module('cpServices', ['ngResource']);
cp.service('sharedData', function() {
	var userName = 'test';
	var userSurname = 'test2';
	var showHome = true;
	
		this.getName = function(){
			return userName;
		};
		
		this.setName = function(name){
			this.userName = name;
		};
		
		this.getSurName = function(){
			return userSurname;
		};
		
		this.setSurName = function(surname){
			this.userSurname = surname;
		};
		
		this.getShowHome = function(){
			return showHome;
		};
		
		this.setShowHome = function(value){
			this.showHome = value;
		};
});

