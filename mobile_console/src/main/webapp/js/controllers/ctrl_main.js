'use strict';

/* Controllers */
var cpControllers = angular.module('cpControllers');

cp.controller('MainCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 'localize', '$locale', '$dialogs', 'sharedDataService', '$filter', 'invokeWSService','invokeWSServiceProxy','invokePdfServiceProxy', 'invokeWSNiksServiceProxy','getMyMessages','$timeout',
    function($scope, $http, $route, $routeParams, $rootScope, localize, $locale, $dialogs, sharedDataService, $filter, invokeWSService, invokeWSServiceProxy, invokePdfServiceProxy, invokeWSNiksServiceProxy, getMyMessages, $timeout) {

    //$rootScope.frameOpened = false;
	var cod_ente = "24";
    
    $scope.setFrameOpened = function(value){
    	$rootScope.frameOpened = value;
    };
    
    $scope.setViewTabs = function(){
    	//$scope.setViewIndex(0);
    	$scope.hideHome();
    	$scope.setNextButtonViewLabel("Chiudi");
    	$scope.setFrameOpened(true);
    };
    
    $scope.setNextButtonViewLabel = function(value){
    	$rootScope.buttonNextViewLabel = value;
    };

    $scope.$route = $route;
    //$scope.$location = $location;
    $scope.$routeParams = $routeParams;
    $scope.params = $routeParams;
    
    $scope.userCF = sharedDataService.getUserIdentity(); 
    
    $scope.app ;
                  			
    $scope.citizenId = userId;
    $scope.user_token = token;
                  			
    // new elements for view
    $scope.currentView;
    $scope.editMode;
    $scope.currentViewDetails;
                  			
    // max practices displayed in home list
    $scope.maxPlayers = 10;

    // for language icons
    var itaLanguage = "active";
    var engLanguage = "";
    
	// for localization
    $scope.setEnglishLanguage = function(){
    	$scope.used_lang = "i18n/angular-locale_en-EN.js";
    	itaLanguage = "";
    	engLanguage = "active";
    	//$scope.setUserLocale("en-US");
    	//$locale.id = "en-US";
    	localize.setLanguage('en-US');
    	sharedDataService.setUsedLanguage('eng');
    	var myDataMsg = getMyMessages.promiseToHaveData('eng');
    	myDataMsg.then(function(result){
    		sharedDataService.inithializeAllMessages(result);
    	});
    };
    
    $scope.setItalianLanguage = function(){
    	$scope.used_lang = "i18n/angular-locale_it-IT.js";
    	itaLanguage = "active";
    	engLanguage = "";
    	//$scope.setUserLocale("it-IT");
    	//$locale.id = "it-IT";
    	localize.setLanguage('it-IT');
    	sharedDataService.setUsedLanguage('ita');
    	var myDataMsg = getMyMessages.promiseToHaveData('ita');
    	myDataMsg.then(function(result){
    	    sharedDataService.inithializeAllMessages(result);
    	});
    };
    
    $scope.setUserLocale = function(lan){
    	var lan_uri = '';
    	if(lan == "it-IT"){
    		lan_uri = 'i18n/angular-locale_it-IT.js';
    	} else if (lan == "en-US"){
    		lan_uri = 'i18n/angular-locale_en-EN.js';
    	}
//    	$http.get(lan_uri).then(function(results){
//    		console.log("Risultato get locale " + results);
//    		angular.copy(results.data, $locale);
//    	});
    	$http.get(lan_uri)
    		.success(function(results){
    			console.log("Success get locale " + results);
    			$locale = results;
    			//angular.copy(results, $locale);
    			$locale.id;
    		})
    		.error(function(results) {
        	console.log("Error get locale " + results);
        });
    };
    
    $scope.isActiveItaLang = function(){
        return itaLanguage;
    };
                  			
    $scope.isActiveEngLang = function(){
    	return engLanguage;
    };
    
    // for services selection
    var activeLinkProfile = "active";
    var activeLinkClassification = "";
    var activeLinkRules = "";
    
    $scope.showProfile = function(){
    	activeLinkProfile = "active";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    };
    
    $scope.showClassification = function(){
    	activeLinkProfile = "";
    	activeLinkClassification = "active";
    	activeLinkRules = "";
    };
    
    $scope.showRules = function(){
    	activeLinkProfile = "";
    	activeLinkClassification = "";
    	activeLinkRules = "active";
    };
    
    $scope.isActiveProfile = function(){
    	return activeLinkProfile;
    };
    
    $scope.isActiveClassification = function(){
    	return activeLinkClassification;
    };
    
    $scope.isActiveRules = function(){
    	return activeLinkRules;
    };
                  			
    $scope.logout = function() {
    	// Clear some session variables
    	sharedDataService.setName(null);
        sharedDataService.setSurname(null);
        sharedDataService.setBase64(null);
        $scope.user_token = null;
        token = null;
        userId = null;
        user_name = null;
        user_surname = null;
        
    	window.location.href = "gamificationweb/logout";
    };
                  		    
    $scope.getToken = function() {
        return 'Bearer ' + $scope.user_token;
    };
                  		    
    $scope.authHeaders = {
         'Authorization': $scope.getToken(),
         'Accept': 'application/json;charset=UTF-8'
    };
                  		    
    // ------------------- User section ------------------
    
    // For user shared data
    sharedDataService.setName(user_name);
    sharedDataService.setSurname(user_surname);
    sharedDataService.setUserId(userId);
    sharedDataService.setBase64(base64);
    //sharedDataService.setBase64('MIIE6TCCA9GgAwIBAgIDBzMlMA0GCSqGSIb3DQEBBQUAMIGBMQswCQYDVQQGEwJJVDEYMBYGA1UECgwPUG9zdGVjb20gUy5wLkEuMSIwIAYDVQQLDBlTZXJ2aXppIGRpIENlcnRpZmljYXppb25lMTQwMgYDVQQDDCtQcm92aW5jaWEgQXV0b25vbWEgZGkgVHJlbnRvIC0gQ0EgQ2l0dGFkaW5pMB4XDTExMTEyMzAwMjQ0MloXDTE3MTEyMjAwNTk1OVowgY4xCzAJBgNVBAYTAklUMQ8wDQYDVQQKDAZUUy1DTlMxJTAjBgNVBAsMHFByb3ZpbmNpYSBBdXRvbm9tYSBkaSBUcmVudG8xRzBFBgNVBAMMPkJSVE1UVDg1TDAxTDM3OFMvNjA0MjExMDE5NzU3MTAwNy53aTRldjVNeCtFeWJtWnJkTllhMVA3ZUtkY1U9MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsF81BDJjAQat9Lfo/1weA0eePTsEbwTe/0QqlArfOTG3hfLEiSd+mDNsBUJo+cRXZMp677y9a1kYlB+IDY3LGH36Bs1QxM14KA6WB67KX4ZaXENew6Qm7NnkMRboKQiIOUmw1l4OiTETfqKWyFqfAtnyLHd8ZZ6qfjgSsJoSHoQIDAQABo4IB3TCCAdkwge0GA1UdIASB5TCB4jCBrAYFK0wQAgEwgaIwgZ8GCCsGAQUFBwICMIGSDIGPSWRlbnRpZmllcyBYLjUwOSBhdXRoZW50aWNhdGlvbiBjZXJ0aWZpY2F0ZXMgaXNzdWVkIGZvciB0aGUgaXRhbGlhbiBOYXRpb25hbCBTZXJ2aWNlIENhcmQgKENOUykgcHJvamVjdCBpbiBhY2NvcmRpbmcgdG8gdGhlIGl0YWxpYW4gcmVndWxhdGlvbiAwMQYGK0wLAQMBMCcwJQYIKwYBBQUHAgEWGWh0dHA6Ly9wb3N0ZWNlcnQucG9zdGUuaXQwOgYIKwYBBQUHAQEELjAsMCoGCCsGAQUFBzABhh5odHRwOi8vcG9zdGVjZXJ0LnBvc3RlLml0L29jc3AwDgYDVR0PAQH/BAQDAgeAMBMGA1UdJQQMMAoGCCsGAQUFBwMCMB8GA1UdIwQYMBaAFO5h8R6jQnz/4EeFe3FeW6ksaogHMEYGA1UdHwQ/MD0wO6A5oDeGNWh0dHA6Ly9wb3N0ZWNlcnQucG9zdGUuaXQvY25zL3Byb3ZpbmNpYXRyZW50by9jcmwuY3JsMB0GA1UdDgQWBBRF3Z13QZAmn85HIYPyIg3QE8WM2DANBgkqhkiG9w0BAQUFAAOCAQEAErn/asyA6AhJAwOBmxu90umMNF9ti9SX5X+3+pcqLbvKOgCNfjhGJZ02ruuTMO9uIi0DIDvR/9z8Usyf1aDktYvyrMeDZER+TyjviA3ntYpFWWIh1DiRnAxuGYf6Pt6HNehodf1lhR7TP+iejH24kS2LkqUyiP4J/45sTK6JNMXPVT3dk/BAGE1cFCO9FI3QyckstPp64SEba2+LTunEEA4CKPbTQe7iG4FKpuU6rqxLQlSXiPVWZkFK57bAUpVL/CLc7unlFzIccjG/MMvjWcym9L3LaU//46AV2hR8pUfZevh440wAP/WYtomffkITrMNYuD1nWxL7rUTUMkvykw==');
    //sharedDataService.setMail(user_mail);
    sharedDataService.setUtente(nome, cognome, sesso, dataNascita, provinciaNascita, luogoNascita, codiceFiscale, cellulare, email, indirizzoRes, capRes, cittaRes, provinciaRes );
    
    $scope.gameId = sharedDataService.getGameId();
    $scope.userId = sharedDataService.getUserId();
    
    $scope.getUserName = function(){
  	  return sharedDataService.getName();
    };
    
    $scope.getUserSurname = function(){
  	  return sharedDataService.getSurname();
    };
    
    $scope.getMail = function(){
      return sharedDataService.getMail();
    };
    
    $scope.setMail = function(value){
    	sharedDataService.setMail(value);
    };
    
    $scope.translateUserGender = function(value){
    	if(sharedDataService.getUsedLanguage() == 'eng'){
    		if(value == 'maschio'){
    			return 'male';
    		} else {
    			return 'female';
    		}
    	} else {
    		return value;
    	}
    };
    
    // ----------------------------- Classification Tabs Section ---------------------------
    var tabClassIndex = 0;
           
    $scope.classTabs = [ 
         { title:'Green Leaves', index: 1, content:"partials/classifications/class_green_leaves.html" },
         { title:'Health', index: 2, content:"partials/classifications/class_health.html", disabled:false },
         { title:'P+R', index: 3, content:"partials/classifications/class_pr.html", disabled:false }
    ];
    
    $scope.setClassIndex = function($index){
        //$scope.tabEditIndex = $index;
        tabClassIndex = $index;
    };
    // -------------------------------------------------------------------------------------
    
    // ----------------------------- Profile Tabs Section --------------------------- 
    var tabProfIndex = 0;
    
    var greenClass = "nav active";
    var healthClass = "nav";
    var prClass = "nav";
    var specialClass = "nav";
    
    $scope.activeProfTab = function(index){
    	switch(index){
    	case 0: 
    		greenClass = "nav active";
    		healthClass = "nav";
    	    prClass = "nav";
    	    specialClass = "nav";
    	    $scope.isGreenActived = true;
    	    $scope.isHealthActived = false;
    	    $scope.isPRActived = false;
    	    $scope.isSpecialActived = false;
    		break;
    	case 1:
    		greenClass = "nav";
    		healthClass = "nav active";
    	    prClass = "nav";
    	    specialClass = "nav";
    	    $scope.isGreenActived = false;
    	    $scope.isHealthActived = true;
    	    $scope.isPRActived = false;
    	    $scope.isSpecialActived = false;
    	    break;
    	case 2:
    		greenClass = "nav";
    		healthClass = "nav";
    	    prClass = "nav active";
    	    specialClass = "nav";
    	    $scope.isGreenActived = false;
    	    $scope.isHealthActived = false;
    	    $scope.isPRActived = true;
    	    $scope.isSpecialActived = false;
    		break;
    	case 3:
    		greenClass = "nav";
    		healthClass = "nav";
    	    prClass = "nav";
    	    specialClass = "nav active";
    	    $scope.isGreenActived = false;
    	    $scope.isHealthActived = false;
    	    $scope.isPRActived = false;
    	    $scope.isSpecialActived = true;
    		break;
    	}
    };
    
    $scope.isGreenClass = function(){
    	return greenClass;
    };
    
    $scope.isHealthClass = function(){
    	return healthClass;
    };
    
    $scope.isPRClass = function(){
    	return prClass;
    };
    
    $scope.isSpecialClass = function(){
    	return specialClass;
    };
           
    $scope.profTabs = [ 
         { title:'Green Leaves', index: 1, content:"partials/profiles/prof_green_leaves.html", activeClass:"active" },
         { title:'Health', index: 2, content:"partials/profiles/prof_health.html", activeClass:"", disabled:false },
         { title:'P+R', index: 3, content:"partials/profiles/prof_pr.html", activeClass:"", disabled:false },
         { title:'Special', index: 4, content:"partials/profiles/prof_special.html", activeClass:"", disabled:false }
    ];
    
    $scope.setProfIndex = function($index){
    	$scope.profTabs[tabProfIndex].activeClass = "";
        //$scope.tabEditIndex = $index;
        tabProfIndex = $index;
        $scope.profTabs[tabProfIndex].activeClass = "active";
    };
    
    $scope.getActiveClass = function(index){
    	return $scope.profTabs[index].activeClass;
    };
    
    // -------------------------------------------------------------------------------------
    
    
    // ---------------------------------- WS Section Start ---------------------------------
    
    $scope.currentPos = 0;
    $scope.userProfile = {};
    $scope.GameClassification = [];
    $scope.sTypes = sharedDataService.getScoreTypes();
    $scope.scoreTypes = "green leaves";
    
    $scope.setLoading = function(value){
    	$scope.isLoading = value;
    };
    
    $scope.getProfile = function(gameId) {
    	
    	$scope.getNiks();
    	
    	if(gameId == null){
    		gameId = $scope.gameId;
    	}
    	//window.location.reload(true);	// To force the page refresh - this goes in a loop
    	$scope.setLoading(true);
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "state/" + gameId + "/" + $scope.userId;
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		//$scope.userProfile = result.state;
    		$scope.correctProfileData(result);
    		$scope.showProfile();
    	});
    };
    
    $scope.getClassification = function(gameId) {
    	//window.location.reload(true);	// To force the page refresh - this goes in a loop
    	$scope.setLoading(true);
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "state/" + gameId;
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		//$scope.GameClassification = result;
    		$scope.correctClassificationData(result);
    		$scope.showClassification();
    	});
    };
    
    // Method used to retrieve all users niks
    $scope.getNiks = function() {
    	//window.location.reload(true);	// To force the page refresh - this goes in a loop
    	$scope.setLoading(true);
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "niks/";
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		//$scope.GameClassification = result;
    		//$scope.correctClassificationData(result);
    		//$scope.showClassification();
    		if(result != null){
    			sharedDataService.setPlayerList(result.players);
    		}
    	});
    };
    
    // Method used to load only the used data from the players list
    $scope.correctProfileData = function(profile){
    	if(profile != null && profile != "" && profile.state.length > 0){
    		//var badge = (profile.state[0].badgeEarned != null) ? profile.state[0].badgeEarned : profile.state[1].badgeEarned;
    		//var badge = $scope.getBadgesList(profile.state);
    		//var badgeEarned = badge[0].badgeEarned;
    		var badges = $scope.getBadgesList(profile.state);
    		var scores = $scope.getStateList(profile.state);
    		var playerData = {
    				id : profile.playerId,
    				gameId : profile.gameId,
    				badges : badges,		//badge : badgeEarned[badgeEarned.length-1],
    				scores : scores
    		};
    		angular.copy(playerData, $scope.userProfile);
    		$scope.checkBadges($scope.userProfile.badges);
    	}
    	$scope.setLoading(false);
    };
    
    // Method used to load only the used data from the players list
    $scope.correctClassificationData = function(list){
    	$scope.GameClassification = [];
    	if(list != null && list.length > 0){
    		for(var i = 0; i < list.length; i++){
    			//var badge = (list[i].state[0].badgeEarned != null) ? list[i].state[0].badgeEarned : list[i].state[1].badgeEarned;
    			//var badge = $scope.getBadgesList(list[i].state);
    			//var badgeEarned = badge[0].badgeEarned;
    			var badges = $scope.getBadgesList(list[i].state);
    			var scores = $scope.getStateList(list[i].state);
    			var playerData = {
    					id : list[i].playerId,
    					name: $scope.getPlayerNameById(list[i].playerId),
    					gameId : list[i].gameId,
    					badges : badges,		//badge : badgeEarned[badgeEarned.length-1],
    					score : scores
    			};
    			$scope.GameClassification.push(playerData);
    		}
    	}
    	$scope.setLoading(false);
    };
    
    // Method used to load the different badges in a list
    $scope.getBadgesList = function(badge_list){
    	var badges = [];
    	var badges_green = {};
    	var badges_health = {};
    	var badges_pr = {};
    	var badges_special = {};
    	
    	for(var i = 0; i < badge_list.length; i++){
    		if(badge_list[i].badgeEarned != null){
    			if(badge_list[i].name == "green leaves"){
    				badges_green = badge_list[i];
    			} else if(badge_list[i].name == "health"){
    				badges_health = badge_list[i];
    			} else if(badge_list[i].name == "p+r"){
    				badges_pr = badge_list[i];
    			} else if(badge_list[i].name == "special"){
    				badges_special = badge_list[i];
    			}
    		}
    	}
    	
    	badges.splice(0, 0, badges_green);
    	badges.splice(1, 0, badges_health);
    	badges.splice(2, 0, badges_pr);
    	badges.splice(3, 0, badges_special);
  	
    	return badges;
    };
    
    // Method used to load the different score category in a list
    $scope.getStateList = function(state_list){
    	var states = [];
    	var state_greenL = {};
    	var state_health = {};
    	var state_pr = {}; 
    	for(var i = 0; i < state_list.length; i++){
    		if(state_list[i].score != null){
    			if(state_list[i].name == "green leaves"){
	    			state_greenL = {
	    					id : state_list[i].id,
	    					name : state_list[i].name,
	    					score : state_list[i].score
	    			};
    			} else if (state_list[i].name == "health"){
	    			state_health = {
	    					id : state_list[i].id,
	    					name : state_list[i].name,
	    					score : state_list[i].score
	    			};
    			} else {
	    			state_pr = {
	    					id : state_list[i].id,
	    					name : state_list[i].name,
	    					score : state_list[i].score
	    			};
    			}
    		}
    	}
    	states.splice(0, 0, state_greenL);
    	states.splice(1, 0, state_health);
    	states.splice(2, 0, state_pr);
    	
    	return states;
    };
    
    // Method used to find a player name by the id
    $scope.getPlayerNameById = function(id){
    	var find = false;
    	var name = "";
    	var playersList = sharedDataService.getPlayersList();
    	for(var i = 0; (i < playersList.length) && !find; i++){
    		if(playersList[i].socialId == id){
    			name = playersList[i].nikName;
    			find = true;
    		}
    	}
    	return name;
    };
    
    $scope.checkBadges = function(list){
    	
    	$scope.userShowGreenGoldMedal = false;
    	$scope.userShowGreenSilverMedal = false;
    	$scope.userShowGreenBronzeMedal = false;
    	$scope.userShowGreenKingWeek = false;
    	
    	$scope.userShowHealthGoldMedal = false;
    	$scope.userShowHealthSilverMedal = false;
    	$scope.userShowHealthBronzeMedal = false;
    	$scope.userShowHealthKingWeek = false;
    	
    	$scope.userShowPRGoldMedal = false;
    	$scope.userShowPRSilverMedal = false;
    	$scope.userShowPRBronzeMedal = false;
    	$scope.userShowPRKingWeek = false;
    	
    	$scope.userShowEMotion = false;
    	$scope.userShowZeroImpact = false;
    	$scope.userShowManifatturaPark = false;
    	$scope.userShowQuerciaPark = false;
    	$scope.userShowSpecialBike = false;
    	$scope.userShowSpecialWalking = false;
    	$scope.userNoSpecialBadge = false;
    	
    	if(list != null && list.length > 0){
    		
    		for(var i = 0; i < list[0].badgeEarned.length; i++){
    			switch(list[0].badgeEarned[i]){
    				case "gold-medal-green" :
    					$scope.userShowGreenGoldMedal = true;
    					break;
    				case "silver-medal-green" :
    					$scope.userShowGreenSilverMedal = true;
    					break;
    				case "bronze-medal-green" :
    					$scope.userShowGreenBronzeMedal = true;
    					break;
    				case "king-week-green" :
    					$scope.userShowGreenKingWeek = true;
    					break;	
    				default: 
    					break;
    			}
    		}
    		for(var i = 0; i < list[1].badgeEarned.length; i++){
    			switch(list[1].badgeEarned[i]){
    				case "gold-medal-health" :
    					$scope.userShowHealthGoldMedal = true;
    					break;
    				case "silver-medal-health" :
    					$scope.userShowHealthSilverMedal = true;
    					break;
    				case "bronze-medal-health" :
    					$scope.userShowHealthBronzeMedal = true;
    					break;
    				case "king-week-health" :
    					$scope.userShowHealthKingWeek = true;
    					break;	
    				default: 
    					break;
    			}
    		}
    		for(var i = 0; i < list[2].badgeEarned.length; i++){
    			switch(list[2].badgeEarned[i]){
    				case "gold-medal-pr" :
    					$scope.userShowPRGoldMedal = true;
    					break;
    				case "silver-medal-pr" :
    					$scope.userShowPRSilverMedal = true;
    					break;
    				case "bronze-medal-pr" :
    					$scope.userShowPRBronzeMedal = true;
    					break;
    				case "king-week-pr" :
    					$scope.userShowPRKingWeek = true;
    					break;	
    				default: 
    					break;
    			}
    		}
    		for(var i = 0; i < list[3].badgeEarned.length; i++){
    			switch(list[3].badgeEarned[i]){
    				case "e-motion" :
    					$scope.userShowEMotion = true;
    					break;
    				case "zero-impact" :
    					$scope.userShowZeroImpact = true;
    					break;
    				case "bike" :
    					$scope.userShowSpecialBike = true;
    					break;
    				case "walking" :
    					$scope.userShowSpecialWalking = true;
    					break;
    				case "Ex Manifattura-park" :
    					$scope.userShowManifatturaPark = true;
    					break;
    				case "Stadio-park" :
    					$scope.userShowQuerciaPark = true;
    					break;
    				default: 
//    					if(list[3].badgeEarned[i].indexOf("-park") > -1){
//    						$scope.userShowPark = true;
//    						var tmpParkName = list[3].badgeEarned[i].split("-");
//    						$scope.userParkName = tmpParkName[0];
//    					}
    					break;
    			}
    		}
    		if(list[3].badgeEarned.length == 0){
    			$scope.userNoSpecialBadge = true;
    		}
    	}
    };
    
    // ----------------------------------- WS Section End ----------------------------------
    
    // Method used to find the distance in milliseconds between two dates
    $scope.getDifferenceBetweenDates = function(dataDa, dataA){
    	var dateDa = $scope.correctDate(dataDa);
   		var dateA = $scope.correctDate(dataA);
   		var fromDate = $scope.castToDate(dateDa);
   		var toDate = $scope.castToDate(dateA);
   		if($scope.showLog){
   			console.log("Data da " + fromDate);
   			console.log("Data a " + toDate);
   		}
   		var difference = toDate.getTime() - fromDate.getTime();
   		return difference;
    };
    
    $scope.correctDate = function(date){
       	if(date!= null){
       		if(date instanceof Date){
       			var correct = "";
       			var day = date.getDate();
       			var month = date.getMonth() + 1;
       			var year = date.getFullYear();
       			correct = year + "-" + month + "-" + day;
       			return correct;
       		} else {
       			var res = date.split("/");
       			correct = res[2] + "-" + res[1] + "-" + res[0];
       			return correct;
       		}
       	} else {
       		return date;
       	}
    };
            
    $scope.correctDateIt = function(date){
    	if(date != null){
	    	if(date instanceof Date){
	    		// if date is a Date
	    		var correct = "";
	       		var day = date.getDate();
	       		var month = date.getMonth() + 1;
	       		var year = date.getFullYear();
	       		correct = day + "/" + month + "/" + year;
	       		return correct;
	    	} else {
	    		// if date is a String
		       	if(date.indexOf("/") > -1){
		       		return date;
		      	} else {
		        	if(date != null){
		        		var res = date.split("-");
		        		var correct = "";
		        	   	correct=res[2] + "/" + res[1] + "/" + res[0];
		        	   	return correct;
		        	} else {
		            	return date;
		            }
		        }
	    	}
    	} else {
    		return date;
    	}
    };
            
    $scope.castToDate = function(stringDate){
    	if(stringDate != null && stringDate!= "" ){
    		var res = stringDate.split("-");
    		var month = parseInt(res[1]) - 1; // the month start from 0 to 11;
    		return new Date(res[0], month, res[2]);
    	} else {
    		return null;
    	}
    };
    
                  			
    // for next and prev in practice list
    $scope.currentPage = 0;
	
	$scope.numberOfPages = function(){
		if($scope.GameClassification == null){
			return 0;
		}
		return Math.ceil($scope.GameClassification.length/$scope.maxPlayers);
	};
                  			
    var newPractice = false;
                  			
    $scope.newPracticeShow = function(){
    	newPractice = true;
    };
                  			
    $scope.newPracticeHide = function(){
    	newPractice = false;
    };
                  			
   	$scope.isNewPractice = function(){
   		return newPractice;
    };
      
    $scope.utenteCS = sharedDataService.getUtente();
                  			
}]);