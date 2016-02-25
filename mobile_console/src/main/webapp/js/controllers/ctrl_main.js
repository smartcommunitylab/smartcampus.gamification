'use strict';

/* Controllers */
var cpControllers = angular.module('cpControllers');

cp.controller('MainCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 'localize', '$locale', '$dialogs', 'sharedDataService', '$filter', 'invokeWSService','invokeWSServiceProxy','invokePdfServiceProxy', 'invokeWSNiksServiceProxy','getMyMessages','$timeout',
    function($scope, $http, $route, $routeParams, $rootScope, localize, $locale, $dialogs, sharedDataService, $filter, invokeWSService, invokeWSServiceProxy, invokePdfServiceProxy, invokeWSNiksServiceProxy, getMyMessages, $timeout) {

    //$rootScope.frameOpened = false;
	var cod_ente = "24";
	$scope.TRAIN_TRANS = 0;
	$scope.BUS_TRANS = 1;
	$scope.SAUTO_TRANS = 2;
	$scope.SBIKE_TRANS = 3;
	$scope.AUTO_PRIVAT = 4;
	$scope.BIKE_PRIVAT = 5;
	$scope.WALK_PRIVAT = 6;
	$scope.MINOR_20 = 1;
	$scope.FROM_20_TO_40 = 2;
	$scope.FROM_40_TO_70 = 3;
	$scope.MAIOR_70 = 4;
	// challeng_keys
	$scope.CHAL_K = "ch-";
	$scope.CHAL_K_TYPE = "-type";
	$scope.CHAL_K_STS = "-startChTs";
	$scope.CHAL_K_ETS = "-endChTs";
	$scope.CHAL_K_WALKED_KM = "-Km_walked_during_challenge";
	$scope.CHAL_K_TARGET = "-target";
	$scope.CHAL_K_BONUS = "-bonus";
	$scope.CHAL_K_RECOM = "-recommandation";
	$scope.CHAL_DESC_1 = "Fai almeno altri TARGET km a piedi e avrai BONUS punti bonus";
	$scope.CHAL_DESC_3 = "Fai almeno TARGET viaggio con il Bike sharing e vinci un bonus di BONUS Green Points";
	$scope.CHAL_DESC_7 = "Completa una Badge Collection e vinci un bonus di BONUS Green Points";
	$scope.CHAL_DESC_9 = "Raccomanda la App ad almeno TARGET utenti e guadagni BONUS Green Points";
    $scope.CHAL_TS_OFFSET = 1000 * 60 * 60 * 24 * 2;	// millis in a day (for test I use 2 days)
    var show_ch_details = false;
	
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
    var activeLinkChalleng = "";
    var activeLinkClassification = "";
    var activeLinkRules = "";
    
    $scope.showProfile = function(){
    	activeLinkProfile = "active";
    	activeLinkChalleng = "";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    };
    
    $scope.showChalleng = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "active";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    };
    
    $scope.showClassification = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "";
    	activeLinkClassification = "active";
    	activeLinkRules = "";
    };
    
    $scope.showRules = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "";
    	activeLinkClassification = "";
    	activeLinkRules = "active";
    };
    
    $scope.isActiveProfile = function(){
    	return activeLinkProfile;
    };
    
    $scope.isActiveChalleng = function(){
    	return activeLinkChalleng;
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
    sharedDataService.setUtente(nome, cognome, sesso, dataNascita, provinciaNascita, luogoNascita, codiceFiscale, cellulare, email, indirizzoRes, capRes, cittaRes, provinciaRes );
    
    $scope.gameId = sharedDataService.getGameId();
    $scope.userId = sharedDataService.getUserId();
    
    $scope.getUserName = function(){
  	  return sharedDataService.getName();
    };
    
    $scope.getNikName = function(){
    	  return sharedDataService.getNickName();
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
    	if(gameId == null){
    		gameId = $scope.gameId;
    	}
    	$scope.setLoading(true);
    	var nicksList = sharedDataService.getPlayersList();
    	if(nicksList != null && nicksList.length != null && nicksList.length > 0){
    		$scope.myNick = $scope.getPlayerNameById($scope.userId);
    		$scope.getProfilesData(gameId);
    	} else {
	    	$scope.getNiks($scope.userId).then(function(result){	// wait until the nickname function return	        	
	    		$scope.getProfilesData(gameId);
	    	});
    	}
    };
    
    $scope.getChalleng = function(gameId){
    	$scope.setLoading(true);
    	$scope.getChallengData(gameId);
    };
    
    // Method getPrifilesData: used to retrieve the user profile data from DB
    $scope.getProfilesData = function(gameId){
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "state/" + gameId + "/" + $scope.userId;
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		$scope.correctProfileData(result);
    		$scope.showProfile();
    	});
    }
    
    // Method getChallengData: used to retrieve the user challeng customData from DB
    $scope.getChallengData = function(gameId){
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "state/" + gameId + "/" + $scope.userId;
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		if(result.customData){
    			$scope.correctCustomData(result.customData);
    		}
    		$scope.showChalleng();
    	});
    }
    
    // Method getClassification: used to retrieve the classificaton data from DB
    $scope.getClassification = function(gameId) {
    	$scope.setLoading(true);
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "state/" + gameId;
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		$scope.correctClassificationData(result);
    		$scope.showClassification();
    	});
    };
    
    // Method used to retrieve all users niks
    $scope.getNiks = function(id) {
    	$scope.setLoading(true);
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "niks/";
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		if(result != null){
    			sharedDataService.setPlayerList(result.players);
    			var name = "";
    			var find = false;
    			for(var i = 0; (i < result.players.length) && !find; i++){
    				if(result.players[i].socialId == id){
    					name = result.players[i].nikName;
    					find = true;
    				}	
    			}
    			$scope.myNick = name;
    			if(name != null && name != ''){
    				sharedDataService.setNickName(name);
    			}
    			if($scope.myNick == null || $scope.myNick == ""){
    				// manage Nick for player
    				$scope.retrieveNickForPlayer(result.players);
    			}
    		}
    	});
    	return myDataPromise;
    };
    
    // Method used to retrieve all users niks
    $scope.updateNiks = function(personalData) {
    	var method = 'POST';
    	var params = null;
    	var wsRestUrl = "updateNick/";
    	var data = {
    		id: $scope.userId,
    		personalData: $scope.correctPersonalDataBeforeSave(personalData)
    	};
    	var value = JSON.stringify(data);
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, value);
    	myDataPromise.then(function(result){
    		if(result != null){
    			console.log("Updated user nick" + JSON.stringify(result));
    			var playerList = sharedDataService.getPlayersList();
    			for(var i = 0; i < playerList.length; i++){
    				if(playerList[i].socialId == result.socialId){
    					playerList[i].nikName = result.nikName;
    				}
    			}
    			sharedDataService.setPlayerList(playerList);
    		}
    	});
    	return myDataPromise;
    };  
    
    // Method correctPersonalDataBeforeSave: used to correct personal data object before post ws call
    $scope.correctPersonalDataBeforeSave = function(personaldata){
    	var correctedData = {
    	    nickname: personaldata.nickname,
    		age: $scope.correctAgeRange(personaldata.age),
    		transport : (personaldata.transport == "yes") ? true : false,
    		vehicle: $scope.correctVehicleArray(personaldata.vehicle),
    		averagekm: personaldata.averagekm,
    		invitation: personaldata.invitation
    	}
    	return correctedData;
    };
    	
    // Method correctAgeRange: used to correct the age value to a descriptive string
    $scope.correctAgeRange = function(ageval){
    	var age_range = ""
    	switch(ageval){
    		case "1": age_range = "0-20";
    			break;
    		case "2": age_range = "20-40";
    			break;
    		case "3": age_range = "40-70";
    			break;
    		case "4": age_range = "70-100";
    			break;
    		default: break;
    	}
    	return age_range;
    };
    
    $scope.correctVehicleArray = function(vehiclearr){
    	var corr_vehicle_arr = [];
    	//for(var i = 0;i < vehiclearr.length; i++){
    		if(vehiclearr[0]){
    			corr_vehicle_arr.push("train");
    		}
    		if(vehiclearr[1]){
    			corr_vehicle_arr.push("bus");
    		}
    		if(vehiclearr[2]){
    			corr_vehicle_arr.push("shared car");
    		}
    		if(vehiclearr[3]){
    			corr_vehicle_arr.push("shared bike");
    		}
    		if(vehiclearr[4]){
    			corr_vehicle_arr.push("private car");
    		}
    		if(vehiclearr[5]){
    			corr_vehicle_arr.push("private bike");
    		}
    		if(vehiclearr[6]){
    			corr_vehicle_arr.push("walk");
    		}
    	//}
    	
    	return corr_vehicle_arr;
    };
    
    $scope.retrieveNickForPlayer = function(nickList){
    	var dlg = $dialogs.create('/dialogs/nickinput.html','nicknameDialogCtrl',nickList,'lg');
		dlg.result.then(function(user){
			console.log("Data retrieved from initial dialog " + JSON.stringify(user));
			$scope.myNick = user.nickname;
			sharedDataService.setNickName(user.nickname);
			$scope.updateNiks(user);		// commented for test
		});
    };
    
    // Method used to load only the used data from the players list
    $scope.correctProfileData = function(profile){
    	if(profile != null && profile != "" && profile.state != null){
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
    
    // Method used to load only the custom data from the user profile data
    $scope.correctCustomData = function(customdata){
    	var challIndxArray = [];
    	$scope.challenges = [];
    	if(customdata != null && customdata != ""){
    		
    		for(var keyName in customdata){
    			var key = keyName;
    			var value = customdata[keyName];
    			if(key.indexOf('ch-') > -1){
    				// case key is a challeng key
    				var challangeString = key.substring(3);
    				var partialIndex = challangeString.split("-");
    				var chal_Indx = partialIndex[0];
    				if(challIndxArray.length == 0){
    					challIndxArray.push(chal_Indx);
    				} else {
    					if($scope.isNewInArray(challIndxArray, chal_Indx)){
    						challIndxArray.push(chal_Indx);
    					}
    				}
    			}
    			console.log("key: " + key + ", value: " + value );
    		}
    		for(var i = 0; i < challIndxArray.length; i++){
    			var ch_id = challIndxArray[i];
    			var ch_type = customdata[$scope.CHAL_K+ch_id+$scope.CHAL_K_TYPE];
    			var target = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_TARGET];
				var bonus = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_BONUS];
				var endChTs = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_ETS];
				var now = new Date().getTime();
				var active = (now < endChTs);
				var status = 0;
    			var tmp_chall = {};
    			switch(ch_type){
    				case 'ch1':
    					var walked_km = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_WALKED_KM];
    					status = walked_km * 100 / target;
    					if(status > 100)status = 100;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: "img/green/greenLeavesTesto.svg", //"img/health/healthLeavesTesto.svg",
    						desc: $scope.correctDesc($scope.CHAL_DESC_1, target, bonus), //"Aumenta del 15% i km fatti a piedi e avrai 50 punti bonus",
    						startChTs: customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_STS],
    						endChTs: endChTs,
    						Km_walked_during_challenge: walked_km,
    						target: target,
    						bonus: bonus,
    						status: status,
    						active: active,
    						type: ch_type,
    						details: false
    					};
    					break;
    				case 'ch3':
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: "img/green/greenLeavesTesto.svg",
    						desc: $scope.correctDesc($scope.CHAL_DESC_3, target, bonus), //"Fai almeno un viaggio con il Bike sharing e vinci un bonus di 50 Green Points",
    						startChTs: customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_STS],
    						endChTs: endChTs,
    						target: target,
    						bonus: bonus,
    						status: status,
    						active: active,
    						type: ch_type,
    						details: false
    					};
    					break;
    				case 'ch7':
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: "img/green/greenLeavesTesto.svg",
    						desc: $scope.correctDesc($scope.CHAL_DESC_7, target, bonus), //"completa una Badge Collection e vinci un bonus di 50 Green Points",
    						startChTs: customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_STS],
    						endChTs: endChTs,
    						target: target,
    						bonus: bonus,
    						status: status,
    						active: active,
    						type: ch_type,
    						details: false
    					};
    					break;
    				case 'ch9':
    					var recommandation = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_RECOM];
    					status = recommandation * 100 / target;
    					if(status > 100)status = 100;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: "img/green/greenLeavesTesto.svg",
    						desc: $scope.correctDesc($scope.CHAL_DESC_9, target, bonus), //"raccomanda la App ad almeno 10 utenti e guadagni 50 Green Points",
    						startChTs: customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_STS],
    						endChTs: endChTs,
    						recommandation: recommandation,
    						target: target,
    						bonus: bonus,
    						status: status,
    						active: active,
    						type: ch_type,
    						details: false
    					};
    					break;	
    				default: break;
    			}
    			if(now < (endChTs + $scope.CHAL_TS_OFFSET)){
    				$scope.challenges.push(tmp_chall);
    			}
    		}
    	}
    	$scope.setLoading(false);
    };
    
    $scope.getChallStyle = function(active, status){
    	if(status == 100){
    		return "panel panel-success success";
    	} else { 
    		if(active){
    			return "panel panel-success";
    		}
    		if(!active){
    			return "panel panel-success failed";
    		}
    	}
    };
    
    $scope.showDetails = function(challeng){
    	for(var i = 0; i < $scope.challenges.length; i++){
    		if(challeng.id == $scope.challenges[i].id){
    			$scope.challenges[i].details = true;
    		}
    	}
    };
    
    $scope.hideDetails = function(challeng){
    	for(var i = 0; i < $scope.challenges.length; i++){
    		if(challeng.id == $scope.challenges[i].id){
    			$scope.challenges[i].details = false;
    		}
    	}
    };
    
    $scope.isShowedDetails = function(challengid){
    	var isshowed = false;
    	var found = false;
    	for(var i = 0; (i < $scope.challenges.length && !found); i++){
    		if(challengid == $scope.challenges[i].id){
    			isshowed = $scope.challenges[i].details;
    			found = true;
    		}
    	}
    	return isshowed;
    };
    
    $scope.isNewInArray = function(arr, elem){
    	var contained = false;
    	for(var i = 0; (i < arr.length) && !contained; i++){
    		if(arr[i] == elem){
    			contained = true;
    		}
    	}
    	return !contained;
    };
    
    $scope.correctDesc = function(desc, target, bonus){
    	if(desc.indexOf("TARGET") > -1){
    		desc = desc.replace("TARGET", target);
    	}
    	if(desc.indexOf("BONUS") > -1){
    		desc = desc.replace("BONUS", bonus);
    	}
    	return desc;
    };
    
    // Method used to load only the used data from the players list
    $scope.correctClassificationData = function(object){
    	var list = (object) ? object.content : [];
    	$scope.GameClassification = [];
    	if(list != null && list.length > 0){
    		for(var i = 0; i < list.length; i++){
    			var badges = $scope.getBadgesList(list[i].state);
    			var scores = $scope.getStateList(list[i].state);
    			var playerData = {
    					id : list[i].playerId,
    					name: $scope.getPlayerNameById(list[i].playerId),
    					gameId : list[i].gameId,
    					badges : badges,		//badge : badgeEarned[badgeEarned.length-1],
    					score : scores,
    					class_pos_g : 1,
    					class_pos_h : 1,
    					class_pos_p : 1
    			};
    			//MB19112014: added check to name to consider only the players in list
    			if(playerData.name != null && playerData.name != ""){
    				$scope.GameClassification.push(playerData);
    			}
    		}
    		// Here I have to order the list by user scores
    		var greenClassificationTmp = $scope.orderByScores(1, $scope.GameClassification);
    		$scope.GameClassification = $scope.setCorrectPosGreen(greenClassificationTmp);
    		var HealthClassificationTmp = $scope.orderByScores(2, $scope.GameClassification);
    		$scope.GameClassification = $scope.setCorrectPosHealth(HealthClassificationTmp);
    		var PRClassificationTmp = $scope.orderByScores(3, $scope.GameClassification);
    		$scope.GameClassification = $scope.setCorrectPosPR(PRClassificationTmp);
    	}
    	$scope.setLoading(false);
    };
    
    // Method used to load the different badges in a list
    $scope.getBadgesList = function(object){
    	var badge_list = (object) ? object.BadgeCollectionConcept : [];
    	var badges = [];
    	var badges_green = {};
    	var badges_health = {};
    	var badges_pr = {};
    	var badges_special = {};
    	if(badge_list){
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
    	}
    	
    	badges.splice(0, 0, badges_green);
    	badges.splice(1, 0, badges_health);
    	badges.splice(2, 0, badges_pr);
    	badges.splice(3, 0, badges_special);
  	
    	return badges;
    };
    
    // Method used to load the different score category in a list
    $scope.getStateList = function(object){
    	var state_list = (object) ? object.PointConcept : [];
    	var states = [];
    	var state_greenL = {};
    	var state_health = {};
    	var state_pr = {}; 
    	if(state_list){
	    	for(var i = 0; i < state_list.length; i++){
	    		if(state_list[i].score != null){	//.score
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
    
    // Method used to order the list in a specific score order
    $scope.orderByScores = function(type, list){
    	switch(type){
    		case 1: // case green score
    			list.sort($scope.greenScoreCompare);
    			break;
    		case 2: // case health score
    			list.sort($scope.healthScoreCompare);
    			break;
    		case 3: // case pr score
    			list.sort($scope.prScoreCompare);
    			break;
    		default:
    			break;
    	}
    	return list;
    };
    
    $scope.greenScoreCompare = function(a, b){
    	if(a.score[0].score > b.score[0].score)
    		return -1;
    	if(a.score[0].score < b.score[0].score)
    		return 1;
    	return 0;
    };
    
    $scope.healthScoreCompare = function(a, b){
    	if(a.score[1].score > b.score[1].score)
    		return -1;
    	if(a.score[1].score < b.score[1].score)
    		return 1;
    	return 0;
    };
    
    $scope.prScoreCompare = function(a, b){
    	if(a.score[2].score > b.score[2].score)
    		return -1;
    	if(a.score[2].score < b.score[2].score)
    		return 1;
    	return 0;
    };
    
    // -- Methods used to correct the user position: if a user has the same score of another user I keep the position of previous user --
    $scope.setCorrectPosGreen = function(list){
    	for(var i = 1; i < list.length; i++){
    		if(list[i].score[0].score < list[i-1].score[0].score){
    			list[i].class_pos_g = i + 1;
    		} else {
    			list[i].class_pos_g = list[i - 1].class_pos_g;
    		}
    	}
    	return list;
    };
    
    $scope.setCorrectPosHealth = function(list){
    	for(var i = 1; i < list.length; i++){
    		if(list[i].score[1].score < list[i-1].score[1].score){
    			list[i].class_pos_h = i + 1;
    		} else {
    			list[i].class_pos_h = list[i - 1].class_pos_h;
    		}
    	}
    	return list;
    };
    
    $scope.setCorrectPosPR = function(list){
    	for(var i = 1; i < list.length; i++){
    		if(list[i].score[2].score < list[i-1].score[2].score){
    			list[i].class_pos_p = i + 1;
    		} else {
    			list[i].class_pos_p = list[i - 1].class_pos_p;
    		}
    	}
    	return list;
    };
    // ------------------------------------------------------------------------------------------------------------------------------------
    
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
    	$scope.userShowCentroStoricoPark = false;
    	$scope.userShowParcheggioCentroPark = false;
    	$scope.userShowALeoniPark = false;
    	$scope.userShowSpecialBike = false;
    	$scope.userShowSpecialWalking = false;
    	$scope.userNoSpecialBadge = false;
    	
    	if(list != null && list.length > 0){
    		if(list[0].badgeEarned){
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
    		}
    		if(list[1].badgeEarned){
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
    		}
    		if(list[2].badgeEarned){
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
    		}
    		if(list[3].badgeEarned){
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
	    				case "Centro Storico-park" :
	    					$scope.userShowCentroStoricoPark = true;
	    					break;
	    				case "Parcheggio Centro-park" :
	    			    	$scope.userShowParcheggioCentroPark = true;
	    					break;
	    				case "P.le A.Leoni-park" :
	    			    	$scope.userShowALeoniPark = true;
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
			return 1;
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
cp.controller('nicknameDialogCtrl',function($scope,$modalInstance,data){
	//-- Variables --//
	$scope.submitNumber = 0;
	
	$scope.ages = [
	    {val: 1, label: '< 20 anni'},
	    {val: 2, label: '20 - 40 anni'},
	    {val: 3, label: '40 - 70 anni'},
	    {val: 4, label: '> 70 anni'}
	];
	
	$scope.user = {
		nickname : '',
		age: '',
		transport: null,
		vehicle: [
		   false,
		   false,
		   false,
		   false,
		   false,
		   false,
		   false
		   ],
		averagekm: 0,
		invitation: ''
	};
	$scope.showMessages = false;

	//-- Methods --//
	
	$scope.cancel = function(){
		$modalInstance.dismiss('Canceled');
	}; // end cancel
	
	$scope.save = function(form){
		if(form.$valid){
			$scope.errorMessages = "";
			// check if nick already present
			if(!$scope.checkIfNickAlreadyPresent($scope.user.nickname)){
				$modalInstance.close($scope.user);	// pass all the form data to the main controller
			} else {
				$scope.showMessages = true;
				$scope.errorMessages = "Nickname gia' usato da un altro utente. Scegli un altro nick che ti rappresenti."
			}
		}
	}; // end save
	
	$scope.hitEnter = function(evt){
		if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.user.nickname,null) || angular.equals($scope.user.nickname,'')))
			$scope.save();
	};
	
	$scope.clearErroMessages = function(){
		$scope.errorMessages = "";
		$scope.showMessages = false;
	}
	
	// Method checkIfNickAlreadyPresent: used to control if a nickname is already used in the player's table
	// (added lower case transformation to consider string with same chars but different formatting
	$scope.checkIfNickAlreadyPresent = function(nick){
		for(var i = 0; i < data.length; i++){
			if(data[i].nikName.toLowerCase() == nick.toLowerCase()){
				return true;
			}
		}
		return false;
	}
	
	$scope.checkAllUnselected = function(check_group){
		if(check_group){
			if(check_group.train != null){
				if(!check_group.train && !check_group.bus && !check_group.sauto && !check_group.sbike){
					return true;
				} else {
					return false;
				}
			} else {
				if(!check_group.auto && !check_group.bike && !check_group.walk){
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}
	
	$scope.clearVehicle = function(){
		$scope.user.vehicle = [false, false, false, false, false, false, false];
	};
	
	$scope.someSelectedTrans = function (object) {
		var somes = false;
		if(object){
			for(var i = 0; i < object.length; i++){
				if(object[i]){
					somes = true;
				}
			}
		}
		return somes;
	}
	
	$scope.someSelectedPrivat = function (object) {
		var somes = false;
		if(object){
			for(var i = 0; i < object.length; i++){
				if(object[i]){
					somes = true;
				}
			}
		}
		return somes;
	}
	
	
}) // end controller(customDialogCtrl)