'use strict';

/* Services */
var cpServices = angular.module('cpServices', ['ngResource']);
cp.service('sharedDataService', function(){
	// This section is shared between all the controllers
	
	// Shared field description
	this.usedLanguage = 'ita';
	this.name = '';
	this.surname = '';
	this.nickname = '';
	this.loading = false;
	this.userId = '';
	
	this.gameId = 'game1';
	this.isTest = false;
	
	// Shared time variables
	//-------------------------------------------------------------
	this.three_years_millis = 1000 * 60 * 60 * 24 * 360 * 3;	// I consider an year of 360 days
	this.two_years_millis = 1000 * 60 * 60 * 24 * 360 * 2;
	this.one_year_millis = 1000 * 60 * 60 * 24 * 360; 			// I consider an year of 360 days (12 month of 30 days)
	this.one_year_365_millis = 1000 * 60 * 60 * 24 * 365; 		// I consider an year of 365 days
	this.one_month_millis = 1000 * 60 * 60 * 24 * 30;			// Milliseconds of a month
	this.one_day_millis = 1000 * 60 * 60 * 24 * 2; 				// Milliseconds of a day
	this.six_hours_millis = 1000 * 60 * 60 * 6;					// Milliseconds in six hours
	//-------------------------------------------------------------
	
//	this.searchTab = '';
//	this.searchOpt = '';
//	this.searchVal = '';
//	this.searchList = [];
	
	this.utente = {};
	this.idDomanda = '';
    
    this.scoreTypes = [
          {code: "green leaves", title:"Green Leaves"},
          {code: "health", title:"Health"},
          {code: "p+r", title:"P+R"}
    ];
            
    this.yes_no = [
         {code: 'true' , title: 'Si'},
         {code: 'false' , title: 'No'}
    ];    
    
    this.yes_no_val = [
         {value: true , title: 'Si'},
         {value: false , title: 'No'}
    ];
    
    this.playerList = [];
    this.classification = [];
    this.classificationActual = [];
    this.classificationLast = [];
    this.playerClassTot = {};
    this.playerClassActual = {};
    this.playerClassLast = {};
	
	// Get and Set methods
	this.getUsedLanguage = function(){
		var value = sessionStorage.language;
		return this.usedLanguage;
	};
	
	this.setUsedLanguage = function(value){
		sessionStorage.language = value;
		this.usedLanguage = value;
	};
	
	this.getPlayersList = function(){
		return this.playersList;
	};
	
	this.setPlayerList = function(list){
		this.playersList = list;
	};
	
	this.getName = function(){
		return this.name;
	};
	
	this.setName = function(value){
		this.name = value;
	};
	
	this.getSurname = function(){
		return this.surname;
	};
	
	this.setSurname = function(value){
		this.surname = value;
	};
	
	this.getNickName = function(){
		return this.nickname;
	};
	
	this.setNickName = function(value){
		this.nickname = value;
	};
	
	this.isLoading = function(){
		return this.loading;
	};
	
	this.setLoading = function(value){
		this.loading = value;
	};
	
	this.setUserIdentity = function(value){
		this.utente.codiceFiscale;
	};
	
	this.getScoreTypes = function(){
		return this.scoreTypes;
	};
	
	
	this.setMail = function(value){
		this.utente.email = value;
	};	
	
	this.getMail = function(){
		return this.utente.email;
	};
	
	this.getYesNo = function(){
		return this.yes_no;
	};
	
	this.getYesNoVal = function(){
		return this.yes_no_val;
	};
	
	this.getThreeYearsMillis = function(){
		return this.three_years_millis;
	};
	
	this.getTwoYearsMillis = function(){
		return this.two_years_millis;
	};
	
	this.getOneYearMillis = function(){
		return this.one_year_millis;
	};
	
	this.getOneYear365Millis = function(){
		return this.one_year_365_millis;
	};
	
	this.getOneMonthMillis = function(){
		return this.one_month_millis;
	};
	
	this.getOneDayMillis = function(){
		return this.one_day_millis;
	};
	
	this.getSixHoursMillis = function(){
		return this.six_hours_millis;
	};
	
	this.setIsTest = function(value){
		this.isTest = value;
	};
	
	this.getIsTest = function(){
		return this.isTest;
	};
	
	this.setUserId = function(value){
		this.userId = value;
	};
	
	this.getUserId = function(){
		return this.userId;
	};
	
	this.setGameId = function(value){
		this.gameId = value;
	};
	
	this.getGameId = function(){
		return this.gameId;
	};
	
	this.getClassification = function(){
		return this.classification;
	}
	
	this.setClassification = function(classification){
		this.classification = classification;
	}
	
	this.getClassificationActual = function(){
		return this.classificationActual;
	}
	
	this.setClassificationActual = function(classificationActual){
		this.classificationActual = classificationActual;
	}
	
	this.getClassificationLast = function(){
		return this.classificationLast;
	}
	
	this.setClassificationLast = function(classificationLast){
		this.classificationLast = classificationLast;
	}
	
	this.getPlayerClassTot = function(){
		return this.playerClassTot;
	}
	
	this.setPlayerClassTot = function(playerClassTot){
		this.playerClassTot = playerClassTot;
	}
	
	this.getPlayerClassActual = function(){
		return this.playerClassActual;
	}
	
	this.setPlayerClassActual = function(playerClassActual){
		this.playerClassActual = playerClassActual;
	}
	
	this.getPlayerClassLast = function(){
		return this.playerClassLast;
	}
	
	this.setPlayerClassLast = function(playerClassLast){
		this.playerClassLast = playerClassLast;
	}
});


// Proxy Methods section
cp.factory('invokeWSServiceProxy', function($http, $q) {
	var getProxy = function(method, funcName, params, headers, data){
		var deferred = $q.defer();
		var urlWS = funcName;
		if(params != null){
			urlWS += '?';
			for(var propertyName in params) {
				urlWS += propertyName + '=' + params[propertyName];
				urlWS += '&';
			};
			urlWS = urlWS.substring(0, urlWS.length - 1);
		}
		if(method == 'GET' && params != null){
			$http({
				method : method,
				url : 'rest/allGet',
				params : {
					"urlWS" : urlWS + '&noCache=' + new Date().getTime()
				},
				headers : headers
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else if(method == 'GET' && params == null){
			$http({
				method : method,
				url : 'rest/allGet',
				params : {
					"urlWS" : urlWS + '?noCache=' + new Date().getTime()
				},
				headers : headers
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else {
			// Post method not permitted
		}
		return deferred.promise;
	};
	return {getProxy : getProxy};
});

cp.factory('invokeWSNiksServiceProxy', function($http, $q) {
	var getProxy = function(method, funcName, params, headers, data){
		var deferred = $q.defer();
		
		var urlWS = funcName;
		if(params != null){
			urlWS += '?';
			for(var propertyName in params) {
				urlWS += propertyName + '=' + params[propertyName];
				urlWS += '&';
			};
			urlWS = urlWS.substring(0, urlWS.length - 1); // I remove the last '&'
		}
		if(method == 'GET' && params != null){
			$http({
				method : method,
				url : 'rest/allNiks',
				params : {
					"urlWS" : urlWS + '&noCache=' + new Date().getTime()
				},
				headers : headers
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else if(method == 'GET' && params == null){
			$http({
				method : method,
				url : 'rest/allNiks',
				params : {
					"urlWS" : urlWS + '?noCache=' + new Date().getTime()
				},
				headers : headers
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		} else {
			$http({
				method : method,
				url : 'rest/' + funcName,
				params : {
					"urlWS" : urlWS,
				},
				headers : headers,
				data : data
			}).success(function(data) {
				deferred.resolve(data);
			}).error(function(data) {
				console.log("Returned data FAIL: " + JSON.stringify(data));
				deferred.resolve(data);
			});
		}
		return deferred.promise;
	};
	return {getProxy : getProxy};
});
