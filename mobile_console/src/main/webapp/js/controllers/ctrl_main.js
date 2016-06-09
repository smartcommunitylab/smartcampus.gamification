'use strict';

/* Controllers */
var cpControllers = angular.module('cpControllers');

cp.controller('MainCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 'localize', '$locale', '$dialogs', 'sharedDataService', '$filter','invokeWSServiceProxy','invokeWSNiksServiceProxy','$timeout', '$base64',
    function($scope, $http, $route, $routeParams, $rootScope, localize, $locale, $dialogs, sharedDataService, $filter, invokeWSServiceProxy, invokeWSNiksServiceProxy, $timeout, $base64) {

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
	$scope.CHAL_K = "ch_";
	$scope.CHAL_K_TYPE = "_type";
	$scope.CHAL_K_STS = "_startChTs";
	$scope.CHAL_K_ETS = "_endChTs";
	$scope.CHAL_K_WALKED_KM = "_Km_traveled_during_challenge";
	$scope.CHAL_K_EARNED_POINT = "_points_earned_during_challenges";
	$scope.CHAL_K_WEAKLY_EARNED_POINT = "_point_type_baseline";	//TODO check if in server prod we are aligned
	$scope.CHAL_K_WEAKLY_EARNED_POINT2 = "point_type_baseline";
	$scope.CHAL_K_EARNED_POINT_NEW = "gp_current";
	$scope.CHAL_K_TARGET = "_target";
	$scope.CHAL_K_BONUS = "_bonus";
	$scope.CHAL_K_RECOM = "_recommendations_sent_during_challenges";
	$scope.CHAL_K_SURVEY = "_survey_sent_during_challenges";
	$scope.CHAL_K_SUCCESS = "_success";
	$scope.CHAL_K_COUNTER = "_counter";
	$scope.CHAL_K_POINT_TYPE = "_point_type";
	$scope.CHAL_K_MODE = "_mode";	// possibility: walk, bike, bikesharing, train, bus, car
	$scope.CHAL_K_BADGE_COLL_NAME = "_badge_collection";
	$scope.CHAL_DESC_1 = "Fai almeno altri TARGET km MODE e avrai un bonus di BONUS punti POINT_TYPE";
	$scope.CHAL_DESC_2 = "Fai almeno TARGET viaggio senza usare MODE e avrai un bonus di BONUS punti POINT_TYPE";
	$scope.CHAL_DESC_3 = "Fai almeno TARGET viaggio MODE e avrai un bonus di BONUS punti POINT_TYPE";
	$scope.CHAL_DESC_5 = "Ottieni almeno TARGET punti POINT_TYPE durante la challenge e guadagni un ulteriore bonus di BONUS punti POINT_TYPE"
	$scope.CHAL_DESC_6 = "Ottieni almeno TARGET badge nella Badge Collection BADGE_COLL_NAME e vinci un bonus di BONUS punti POINT_TYPE";
	$scope.CHAL_DESC_7 = "Completa la Badge Collection BADGE_COLL_NAME e vinci un bonus di BONUS punti POINT_TYPE";
	$scope.CHAL_DESC_8 = "Compila il questionario di fine gioco e guadagni BONUS punti POINT_TYPE";
	$scope.CHAL_DESC_9 = "Raccomanda la App ad almeno TARGET utenti e guadagni BONUS punti POINT_TYPE";
	$scope.CHAL_TYPE_1 = "PERCENT";
	$scope.CHAL_TYPE_1A = "BSPERCENT";
	$scope.CHAL_TYPE_2 = "NEGATEDMODE"
	$scope.CHAL_TYPE_3 = "TRIPNUMBER";
	$scope.CHAL_TYPE_3A = "BSTRIPNUMBER";
	$scope.CHAL_TYPE_4 = "ZEROIMPACT";
	$scope.CHAL_TYPE_5 = "POINTSEARNED";
	$scope.CHAL_TYPE_6 = "NEXTBADGE";
	$scope.CHAL_TYPE_7 = "BADGECOLLECTION";
	$scope.CHAL_TYPE_8 = "SURVEYDATA";
	$scope.CHAL_TYPE_9 = "RECOMMENDATION";
	/*$scope.CHAL_TYPE_1 = "ch1";
	$scope.CHAL_TYPE_3 = "ch3";
	$scope.CHAL_TYPE_5 = "ch5";
	$scope.CHAL_TYPE_7 = "ch7";
	$scope.CHAL_TYPE_9 = "ch9";*/
	$scope.CHAL_ALLOWED_MODE_W = "walk";
	$scope.CHAL_ALLOWED_MODE_BK = "bike";
	$scope.CHAL_ALLOWED_MODE_BKS = "bikesharing";
	$scope.CHAL_ALLOWED_MODE_T = "train";
	$scope.CHAL_ALLOWED_MODE_B = "bus";
	$scope.CHAL_ALLOWED_MODE_C = "car";
	$scope.CHAL_ALLOWED_MODE_Z = "zeroimpact";
	$scope.CHAL_ALLOWED_MODE_P = "promoted";
	$scope.CHAL_ALLOWED_PT_GREEN = "green leaves";
	$scope.CHAL_ALLOWED_PT_HEALTH = "health";
	$scope.CHAL_ALLOWED_PT_PR = "pr";
	$scope.CHAL_PT_GREEN_STRING = "Punti Green Leaves";
	$scope.CHAL_PT_HEALTH_STRING = "Punti Salute";
	$scope.CHAL_PT_PR_STRING = "Punti Park&Ride";
	// badges_keys
	$scope.BG_GREEN_POINT = "_point_green";
	$scope.BG_BIKE_TRIP = "_bike_trip";
	$scope.BG_BIKE_SHARING = "_BSstation";
	$scope.BG_ZERO_IMPACT = "_zero_impact_trip";
	$scope.BG_PUBLIC_TRANSPORT = "_pt_trip";
	$scope.BG_PARK_AND_RIDE = "_parking";
	$scope.BG_RECOMMENDATION = "_recommendations";
	$scope.BG_SPECIAL_FIRST = "1st_of_the_week";
	$scope.BG_SPECIAL_SECOND = "2nd_of_the_week";
	$scope.BG_SPECIAL_THIRD = "3rd_of_the_week";
	// badges_collection_names
	$scope.BCN_GREEN = "green leaves";
	$scope.BCN_BIKE = "bike aficionado";
	$scope.BCN_BIKE_SHARING = "bike sharing pioneer";
	$scope.BCN_ZERO_IMPACT = "sustainable life";
	$scope.BCN_PUBLIC_TRANSPORT = "public transport aficionado";
	$scope.BCN_PARK_AND_RIDE = "park and ride pioneer";
	$scope.BCN_RECOMMENDATION = "recommendations";
	$scope.BCN_LEADERBOARD3 = "leaderboard top 3";
	$scope.BCN_HEALTH = "health";
	$scope.BCN_SPECIAL = "special";
	// start week2: 1461362460000 -> 23/04 00:01 
	// start week3: 1461967260000 -> 30/04 00:01 
	// start week4: 1462572060000 -> 07/05 00:01 
	// start week5: 1463176860000 -> 14/05 00:01 
	// start week6: 1463781660000 -> 21/05 00:01 
	// start week7: 1464386460000 -> 28/05 00:01 
	// start week8: 1464991260000 -> 04/06 00:01 
	// start week9: 1465596060000 -> 11/06 00:01 
	//   end week9: 1466200860000 -> 18/06 00:01
	$scope.start_week_1 = 1460757600000;
	$scope.start_week_2 = 1461362460000;
	$scope.start_week_3 = 1461967260000;
	$scope.start_week_4 = 1462572060000;
	$scope.start_week_5 = 1463176860000;
	$scope.start_week_6 = 1463781660000;
	$scope.start_week_7 = 1464386460000;
	$scope.start_week_8 = 1464991260000;
	$scope.start_week_9 = 1465596060000;
	$scope.week_classification = "green leaves week ";
	$scope.week_test_classification = "green leaves week test"
	$scope.useShortClassification = (conf_is_short_classification == "true") ? true : false;
	// max practices displayed in home list
    $scope.maxPlayers = 50;
    $scope.maxPlayersClassification = 500;
    $scope.maxPlayersClassShort = parseInt(conf_short_classification_size);
	
	
	//$scope.CHAL_TS_OFFSET = 1000 * 60 * 60 * 24 * 7;	// millis in a day (for test I use 7 days)
	$scope.CHAL_TS_OFFSET = 0;
    $scope.MILLIS_IN_DAY = 1000 * 60 * 60 * 24;
    var show_ch_details = false;
    $scope.actualChellenges = true;
	
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
    
    sharedDataService.setGameId(conf_gameid);
    $scope.app ;
    $scope.is_test = (conf_is_test=="true")?true:false;
    $scope.citizenId = userId;
    $scope.user_token = token;
    
    // Method used to retrieve the field value from the complete object string
    $scope.getFieldValue = function(fieldsObject){
    	var completeField = fieldsObject.split("=");
    	var lastPos = completeField[1].indexOf(".]");
    	if(lastPos > -1){
    		completeField[1] = completeField[1].substring(0, lastPos) + ".";
    	}
    	return completeField[1];
    };
    
    // Method used to retrieve the challenge objects description list from the relative string value
    $scope.convertDescriptionObjectToDescArray = function(object_array_list){
    	var chDescArray = [];
    	var challenges_data = object_array_list.split("ChallengeDescriptionData ");
    	for(var i = 1; i < challenges_data.length; i++){
    		// here I have a list of challenge object string
    		var fields = challenges_data[i].split(", ");
    		var id = $scope.getFieldValue(fields[0]);
    		var type = $scope.getFieldValue(fields[1]);
    		var mobility_mode = $scope.getFieldValue(fields[2]);
    		var complete_desc = "";
    		if(fields.length > 5){
    			for(var x = 3; x < fields.length; x++){
    				complete_desc += fields[x] + ", ";
    			}
    		} else {
    			complete_desc = fields[3];
    		}
    		var description = $scope.getFieldValue(complete_desc);
    		var correctedObject = {
    			id: id,
    			type: type,
    			mobility_mode: mobility_mode,
    			description: description
    		};
    		chDescArray.push(correctedObject);
    	}
    	return chDescArray;
    };
    
    var chall_description_data_object = conf_chall_messages;
    $scope.chall_description_array = $scope.convertDescriptionObjectToDescArray(chall_description_data_object);
    
    // Method used to retrieve the sponsor objects data list from the relative string value
    $scope.convertSponsorObjectToArray = function(object_array_list){
    	var sponsorArray = [];
    	var sponsors_data = object_array_list.split("SponsorBannerData ");
    	for(var i = 1; i < sponsors_data.length; i++){
    		// here I have a list of sponsor object string
    		var fields = sponsors_data[i].split(", ");
    		var id = $scope.getFieldValue(fields[0]);
    		var start_timestamp = $scope.getFieldValue(fields[1]);
    		var end_timestamp = $scope.getFieldValue(fields[2]);
    		var week_theme = $scope.getFieldValue(fields[3]);
    		var prize = $scope.getFieldValue(fields[4]);
    		if(fields[5].indexOf("]") > -1){
    			fields[5] = fields[5].replace(new RegExp("]", 'g'), "");
    		}
    		var sponsor = $scope.getFieldValue(fields[5]);
    		var correctedObject = {
    			id: id,
    			start_timestamp: start_timestamp,
    			end_timestamp: end_timestamp,
    			week_theme: week_theme,
    			prize: prize,
    			sponsor: sponsor
    		};
    		sponsorArray.push(correctedObject);
    	}
    	return sponsorArray;
    };
    
    var sponsors_banner_data_object = conf_week_sponsor_data;
    $scope.sponsor_banner_array = $scope.convertSponsorObjectToArray(sponsors_banner_data_object);
    $scope.sponsored_week = false;
    
    $scope.retrieveSponsorBannerFromSponsorName = function(sponsor_name){
    	var tmp_elements = [];
    	for(var i = 0; i < $scope.sponsor_banner_array.length; i++){
    		if($scope.sponsor_banner_array[i].sponsor == sponsor_name){
    			tmp_elements.push($scope.sponsor_banner_array[i]);
    		}
    	}
    	return tmp_elements;
    };
    
    $scope.checkIfCurrentSponsor = function(sponsor_name){
    	var is_actual_sponsor = false;
    	var now_current = new Date().getTime();
    	var sponsor_obj = $scope.retrieveSponsorBannerFromSponsorName(sponsor_name);
    	for(var i = 0; i < sponsor_obj.length; i++){
	    	if(now_current >= sponsor_obj[i].start_timestamp && now_current <= sponsor_obj[i].end_timestamp){
	    		is_actual_sponsor = true;
	    		$scope.sponsored_week = true;
	    	}
    	}
    	return is_actual_sponsor;
    };
    
    $scope.showBannerMART = false;
    $scope.showBannerAMI = false;
    $scope.showBannerCARSHARING = false;
    $scope.showBannerLENO2001 = false;
    $scope.showBannerORIENTEOCCIDENTALE = false;
    $scope.showBannerPAT = false;
    
    $scope.checksponsorbanner = function(){
    	$scope.showBannerAMI = $scope.checkIfCurrentSponsor('Associazione Mozart');
    	$scope.showBannerCARSHARING = $scope.checkIfCurrentSponsor('Cooperativa Car Sharing Trentino');
    	$scope.showBannerLENO2001 = $scope.checkIfCurrentSponsor('2001 TEAM');
    	$scope.showBannerMART = $scope.checkIfCurrentSponsor('MART');
    	$scope.showBannerORIENTEOCCIDENTE = $scope.checkIfCurrentSponsor('Oriente Occidente');
    	$scope.showBannerPAT = $scope.checkIfCurrentSponsor('PAT');
    };
    
        
    // Configure point type to show in pages
    $scope.point_types = conf_point_types;
    $scope.show_gpoint = false;
    $scope.show_hpoint = false;
    $scope.show_prpoint = false;
    $scope.show_otherpoint = false;
    $scope.show_specialpoint = false;
    var p_types = conf_point_types.split(",");
    for(var i = 0; i < p_types.length; i++){
    	if(p_types[i] == "green leaves"){
    		$scope.show_gpoint = true;
    	}
    	if(p_types[i] == "health"){
    		$scope.show_hpoint = true;
    	}
    	if(p_types[i] == "pr"){
    		$scope.show_prpoint = true;
    	}
    	if(p_types[i] == "others"){
    		$scope.show_otherpoint = true;
    	}
    	if(p_types[i] == "special"){
    		$scope.show_specialpoint = true;
    	}
    }
    
    // new elements for view
    $scope.currentView;
    $scope.editMode;
    $scope.currentViewDetails;

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
    };
    
    $scope.setItalianLanguage = function(){
    	$scope.used_lang = "i18n/angular-locale_it-IT.js";
    	itaLanguage = "active";
    	engLanguage = "";
    	//$scope.setUserLocale("it-IT");
    	//$locale.id = "it-IT";
    	localize.setLanguage('it-IT');
    	sharedDataService.setUsedLanguage('ita');
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
    
    // Home menu bar section
    var activeLinkProfile = "active";
    var activeLinkChalleng = "";
    var activeLinkClassification = "";
    var activeLinkRules = "";
    var activeLinkPrivacy = "";
    var activeLinkPrizes = "";
    var activeLinkCredits = "";
    
    $scope.showProfile = function(){
    	activeLinkProfile = "active";
    	activeLinkChalleng = "";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    	activeLinkPrivacy = "";
    	activeLinkPrizes = "";
    	activeLinkCredits = "";
    };
    
    $scope.showChalleng = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "active";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    	activeLinkPrivacy = "";
    	activeLinkPrizes = "";
    	activeLinkCredits = "";
    };
    
    $scope.showClassification = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "";
    	activeLinkClassification = "active";
    	activeLinkRules = "";
    	activeLinkPrivacy = "";
    	activeLinkPrizes = "";
    	activeLinkCredits = "";
    };
    
    $scope.showRules = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "";
    	activeLinkClassification = "";
    	activeLinkRules = "active";
    	activeLinkPrivacy = "";
    	activeLinkPrizes = "";
    	activeLinkCredits = "";
    };
    
    $scope.showPrivacy = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    	activeLinkPrivacy = "active";
    	activeLinkPrizes = "";
    	activeLinkCredits = "";
    };
    
    $scope.showPrizes = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    	activeLinkPrivacy = "";
    	activeLinkPrizes = "active";
    	activeLinkCredits = "";
    };
    
    $scope.showCredits = function(){
    	activeLinkProfile = "";
    	activeLinkChalleng = "";
    	activeLinkClassification = "";
    	activeLinkRules = "";
    	activeLinkPrivacy = "";
    	activeLinkPrizes = "";
    	activeLinkCredits = "active";
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
    
    $scope.isActivePrivacy = function(){
    	return activeLinkPrivacy;
    };
    
    $scope.isActivePrizes = function(){
    	return activeLinkPrizes;
    };
    
    $scope.isActiveCredits = function(){
    	return activeLinkCredits;
    };
    
    $scope.logout = function() {
    	// Clear some session variables
        //sharedDataService.setBase64(null);
        $scope.user_token = null;
        token = null;
        userId = null;
    	window.location.href = "gamificationweb/logout";
    };
                  		    
    $scope.getToken = function() {
        return 'Bearer ' + $scope.user_token;
    };
                  		    
    $scope.authHeaders = {
         'Authorization': $scope.getToken(),
         'Accept': 'application/json;charset=UTF-8'
    };
                  		    
    // ------------------- Player section ------------------
    
    // For user shared data
    sharedDataService.setUserId(userId);
    
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
    
    // ----------------------------- Classification Tabs Section ---------------------------
    var tabClassIndex = 0;
           
    $scope.classTabs = [ 
         { title:'Generale', index: 1, content:"partials/classifications/class_green_leaves.html" },
         /*{ title:'Health', index: 2, content:"partials/classifications/class_health.html", disabled:false },
         { title:'P+R', index: 3, content:"partials/classifications/class_pr.html", disabled:false },*/
         { title:'Settimana corrente', index: 2, content:"partials/classifications/class_green_leaves_actual_week.html", disabled:false },
         { title:'Settimana scorsa', index: 3, content:"partials/classifications/class_green_leaves_last_week.html", disabled:false }
    ];
    
    var isActiveG = false;
    var isActiveAW = true;
    var isActiveLW = false;
    
    $scope.showClass = function(page_id){
    	if(page_id == 'G'){
    		isActiveG = true;
    		isActiveAW = false;
    		isActiveLW = false;
    	} else if(page_id == 'AW'){
    		isActiveG = false;
    		isActiveAW = true;
    		isActiveLW = false;
    	} else if(page_id == 'LW'){
    		isActiveG = false;
    		isActiveAW = false;
    		isActiveLW = true;
    	}
    }
    
    $scope.isClassActive = function(page_id){
    	if(page_id == 'G'){
    		return isActiveG;
    	} else if(page_id == 'AW'){
    		return isActiveAW;
    	} else if(page_id == 'LW'){
    		return isActiveLW;
    	}
    }
    
    $scope.isClassActiveForClass = function(page_id){
    	var classActive = "";
    	if(page_id == 'G'){
    		if(isActiveG){
    			classActive = "active";
    		}
    	} else if(page_id == 'AW'){
    		if(isActiveAW){
    			classActive = "active";
    		}
    	} else if(page_id == 'LW'){
    		if(isActiveLW){
    			classActive = "active";
    		}
    	}
    	return classActive;
    }
    
    /*$scope.classTabs = [];
    $scope.init_class = function(){
        if($scope.show_gpoint){
        	$scope.classTabs.push({ title:'Green Leaves', index: 1, content:"partials/classifications/class_green_leaves.html" });
        }
        if($scope.show_hpoint){
        	$scope.classTabs.push({ title:'Health', index: 2, content:"partials/classifications/class_health.html", disabled:false });
        }
        if($scope.show_prpoint){
        	$scope.classTabs.push({ title:'P+R', index: 3, content:"partials/classifications/class_pr.html", disabled:false });
        }
    }*/
    
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
    var otherClass = "nav";
    
    $scope.activeProfTab = function(index){
    	switch(index){
    	case 0: 
    		greenClass = "nav active";
    		healthClass = "nav";
    	    prClass = "nav";
    	    specialClass = "nav";
    	    otherClass = "nav";
    	    $scope.isGreenActived = true;
    	    $scope.isHealthActived = false;
    	    $scope.isPRActived = false;
    	    $scope.isSpecialActived = false;
    	    $scope.isOtherActived = false;
    		break;
    	case 1:
    		greenClass = "nav";
    		healthClass = "nav active";
    	    prClass = "nav";
    	    specialClass = "nav";
    	    otherClass = "nav";
    	    $scope.isGreenActived = false;
    	    $scope.isHealthActived = true;
    	    $scope.isPRActived = false;
    	    $scope.isSpecialActived = false;
    	    $scope.isOtherActived = false;
    	    break;
    	case 2:
    		greenClass = "nav";
    		healthClass = "nav";
    	    prClass = "nav active";
    	    specialClass = "nav";
    	    otherClass = "nav";
    	    $scope.isGreenActived = false;
    	    $scope.isHealthActived = false;
    	    $scope.isPRActived = true;
    	    $scope.isSpecialActived = false;
    	    $scope.isOtherActived = false;
    		break;
    	case 3:
    		greenClass = "nav";
    		healthClass = "nav";
    	    prClass = "nav";
    	    specialClass = "nav active";
    	    otherClass = "nav";
    	    $scope.isGreenActived = false;
    	    $scope.isHealthActived = false;
    	    $scope.isPRActived = false;
    	    $scope.isSpecialActived = true;
    	    $scope.isOtherActived = false;
    		break;
    	case 4:
			greenClass = "nav";
			healthClass = "nav";
		    prClass = "nav";
		    specialClass = "nav";
		    otherClass = "nav active";
		    $scope.isGreenActived = false;
		    $scope.isHealthActived = false;
		    $scope.isPRActived = false;
		    $scope.isSpecialActived = false;
		    $scope.isOtherActived = true;
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
    
    $scope.isOtherClass = function(){
    	return otherClass;
    };
           
    $scope.profTabs = [ 
         { title:'Punti e Badge', index: 1, content:"partials/profiles/prof_points.html", activeClass:"active" },
         { title:'Health', index: 2, content:"partials/profiles/prof_health.html", activeClass:"", disabled:false },
         { title:'P+R', index: 3, content:"partials/profiles/prof_pr.html", activeClass:"", disabled:false },
         { title:'Special', index: 4, content:"partials/profiles/prof_special.html", activeClass:"", disabled:false },
         { title:'Others', index: 5, content:"partials/profiles/prof_other.html", activeClass:"", disabled:false },
         { title:'Challenge', index: 6, content:"partials/challeng.html", activeClass:"", disabled:false },
         { title:'Classifica', index: 7, content:"partials/classification.html", activeClass:"", disabled:false }
    ];
    
    $scope.setProfIndex = function($index){
    	$scope.profTabs[tabProfIndex].activeClass = "";
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
    
    // Method used to show the correct challenge description text when the info icon is clicked
    $scope.openInfoChPanel = function(ch){
    	var msg_to_show = "";
    	switch(ch.type){
    		case $scope.CHAL_TYPE_1: 
    			if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK + "Distance"){
    				msg_to_show=$scope.chall_description_array[0].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS + "Distance"){
    				msg_to_show=$scope.chall_description_array[1].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W + "Distance"){
    				msg_to_show=$scope.chall_description_array[2].description;
    			}
    			break;
    		case $scope.CHAL_TYPE_1A: 
    			if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK + "Distance"){
    				msg_to_show=$scope.chall_description_array[0].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS + "Distance"){
    				msg_to_show=$scope.chall_description_array[1].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W + "Distance"){
    				msg_to_show=$scope.chall_description_array[2].description;
    			}
    			break;
    		case $scope.CHAL_TYPE_2:
    			if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B + "Distance"){
    				msg_to_show=$scope.chall_description_array[21].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK + "Distance"){
    				msg_to_show=$scope.chall_description_array[23].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS + "Distance"){
    				msg_to_show=$scope.chall_description_array[20].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T + "Distance"){
    				msg_to_show=$scope.chall_description_array[22].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_C || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_C + "Distance"){
    				msg_to_show=$scope.chall_description_array[24].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W + "Distance"){
    				msg_to_show=$scope.chall_description_array[25].description;
    			} 
    			break;	
    		case $scope.CHAL_TYPE_3:
    			if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B + "Distance"){
    				msg_to_show=$scope.chall_description_array[4].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK + "Distance"){
    				msg_to_show=$scope.chall_description_array[8].description.replace("[bici, bike sharing, bus, treno]","la bici");
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS + "Distance"){
    				msg_to_show=$scope.chall_description_array[3].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T + "Distance"){
    				msg_to_show=$scope.chall_description_array[5].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_P || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_P + "Distance"){
    				msg_to_show=$scope.chall_description_array[7].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W + "Distance"){
    				msg_to_show=$scope.chall_description_array[26].description;
    			} 
    			break;
    		case $scope.CHAL_TYPE_3A:
    			if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B + "Distance"){
    				msg_to_show=$scope.chall_description_array[4].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BK + "Distance"){
    				msg_to_show=$scope.chall_description_array[8].description.replace("[bici, bike sharing, bus, treno]","la bici");
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS + "Distance"){
    				msg_to_show=$scope.chall_description_array[3].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T + "Distance"){
    				msg_to_show=$scope.chall_description_array[5].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_P || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_P + "Distance"){
    				msg_to_show=$scope.chall_description_array[7].description;
    			} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_W + "Distance"){
    				msg_to_show=$scope.chall_description_array[26].description;
    			}
    			break;	
    		case $scope.CHAL_TYPE_4:
    			//if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_B + "Distance"){
    			//	msg_to_show=$scope.chall_description_array[4].description;
    			//} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_BKS + "Distance"){
    			//	msg_to_show=$scope.chall_description_array[3].description;
    			//} else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_T + "Distance"){
    			//	msg_to_show=$scope.chall_description_array[5].description;
    			//} else 
    			if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_Z || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_Z + "Distance"){
    				msg_to_show=$scope.chall_description_array[6].description;
    			} //else if(ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_P || ch.mobilityMode == $scope.CHAL_ALLOWED_MODE_P + "Distance"){
    			//	msg_to_show=$scope.chall_description_array[7].description;
    			//} 
    			break;	
    		case $scope.CHAL_TYPE_5: 
    			msg_to_show=$scope.chall_description_array[10].description.replace("X", ch.target).replace("punti [green leaves, bici, salute, impatto 0]", ch.point_type);
    			break;
    		case $scope.CHAL_TYPE_6:
    			if(ch.mobilityMode == "green leaves"){
    				msg_to_show=$scope.chall_description_array[14].description;
    			} else if(ch.mobilityMode == "bike sharing pioneer"){
    				msg_to_show=$scope.chall_description_array[13].description;
    			} else if(ch.mobilityMode == "sustainable life"){
    				msg_to_show=$scope.chall_description_array[15].description;	// zero impact
    			} else if(ch.mobilityMode == "park and ride pioneer"){
    				msg_to_show=$scope.chall_description_array[12].description;	// park and ride
    			}
    			break;
    		case $scope.CHAL_TYPE_7: 
    			msg_to_show=$scope.chall_description_array[19].description;
    			break;
    		case $scope.CHAL_TYPE_8: 
    			msg_to_show=$scope.chall_description_array[27].description;	// survey data complete
    			break;	
    		case $scope.CHAL_TYPE_9: 
    			var racc_num = ch.target;
    			if(racc_num == 1){
    				msg_to_show=$scope.chall_description_array[9].description.replace("X", ch.target).replace("tuoi amici si devono", "tuo amico si deve");
    			} else {
    				msg_to_show=$scope.chall_description_array[9].description.replace("X", ch.target);
    			}
    			break;
    	}
    	$dialogs.notify("INFORMAZIONI UTILI", msg_to_show);
    };
    
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
    	$scope.gameId = gameId;
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
    		var pointConcept = [];
    		if(result.state){
    			pointConcept = result.state.PointConcept;
    		}
    		if(result.customData){
    			$scope.correctCustomData(result.customData, pointConcept);
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
    
    $scope.getClassificationPages = function(gameId, page) {
    	if(sharedDataService.getClassification() != null && sharedDataService.getClassification().length > 0){
    		$scope.GameClassificationTot = sharedDataService.getClassification();
    		$scope.GameClassificationActual = sharedDataService.getClassificationActual();
    		$scope.GameClassificationLast = sharedDataService.getClassificationLast();
    		$scope.playerClassTot = sharedDataService.getPlayerClassTot();
    		$scope.playerClassActual = sharedDataService.getPlayerClassActual();
    		$scope.playerClassLast = sharedDataService.getPlayerClassLast();
    	} else {
	    	$scope.setLoading(true);
	    	var method = 'GET';
	    	var params = {
	    		page: page,
	    		size: $scope.maxPlayersClassification
	    	};
	    	var wsRestUrl = "state/" + gameId;
	    	var myDataPromise = invokeWSServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
	    	myDataPromise.then(function(result){
	    		$scope.correctClassificationData(result);
	    		$scope.isLast = result.lastPage;
	    		$scope.showClassification();
	    	});
    	}
    };
    
    // Method used to retrieve all users niks
    $scope.getNiks = function(id) {
    	var now = new Date();
    	$scope.setLoading(true);
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "niks/";
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		if(result != null){
    			sharedDataService.setPlayerList(result.players);
    			var name = "";
    			var mail = "";
    			var surveyData = "";
    			var find = false;
    			for(var i = 0; (i < result.players.length) && !find; i++){
    				if(result.players[i].socialId == id){
    					name = result.players[i].nikName;
    					mail = result.players[i].mail;
    					surveyData = result.players[i].surveyData;
    					find = true;
    				}	
    			}
    			$scope.myNick = name;
    			$scope.playerMail = mail;
    			if(name != null && name != ''){
    				sharedDataService.setNickName(name);
    			}
    			if($scope.myNick == null || $scope.myNick == ""){
    				// manage Nick for player
    				$scope.retrieveNickForPlayer(result.players, mail);
    			} else if(mail == null || mail == ""){
    				// manage Nick for player
    				$scope.retrieveMailForPlayer();
    			}
    			if((surveyData == null || surveyData == "") && now.getTime() > $scope.start_week_8){	//TODO: change it to 9
    				// manage SurveyData for player
    				$scope.retrieveSurveyDataForPlayer();
    			}
    			
    		}
    	});
    	return myDataPromise;
    };
    
    // Method used to update an user nick and mail
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
    
    // Method used to update an user nick and mail
    $scope.updateMail = function(userData) {
    	var method = 'POST';
    	var params = {
    		playerid: $scope.userId,
    		mail: userData.mail
    	};
    	var wsRestUrl = "updateMail";
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		if(result != null){
    			console.log("Updated user mail" + JSON.stringify(result));
    		}
    	});
    	return myDataPromise;
    };
    
    // Method used to update an user survey data
    $scope.updateSurvey = function(userData) {
    	var method = 'POST';
    	var params = {
    		playerId: $scope.userId,
    	};
    	var data = userData.surveyData;
    	var wsRestUrl = "updateSurvey";
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, data);
    	myDataPromise.then(function(result){
    		if(result != null){
    			console.log("Updated user survey" + JSON.stringify(result));
    		}
    	});
    	return myDataPromise;
    };
    
    // Method correctPersonalDataBeforeSave: used to correct personal data object before post ws call
    $scope.correctPersonalDataBeforeSave = function(personaldata){
    	var correctedData = {};
    	if(personaldata.invitation != null && personaldata.invitation != ""){
	    	correctedData = {
	    		mail: personaldata.mail,
	    	    nickname: personaldata.nickname,
	    		age: $scope.correctAgeRange(personaldata.age),
	    		transport : (personaldata.transport == "yes") ? true : false,
	    		vehicle: $scope.correctVehicleArray(personaldata.vehicle),
	    		averagekm: personaldata.averagekm,
	    		invitation: personaldata.invitation
	    	}
    	} else {
    		correctedData = {
    			mail: personaldata.mail,
    	    	nickname: personaldata.nickname,
    	    	age: $scope.correctAgeRange(personaldata.age),
    	    	transport : (personaldata.transport == "yes") ? true : false,
    	    	vehicle: $scope.correctVehicleArray(personaldata.vehicle),
    	    	averagekm: personaldata.averagekm
    	    }
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
    
    // Method used to retrieve the player nickname by opening a modal form that the user has to complete
    $scope.retrieveNickForPlayer = function(nickList, mail){
    	var data = {
    		nickList : nickList,
    		mail: mail
    	}
    	var dlg = $dialogs.create('/dialogs/nickinput.html','nicknameDialogCtrl',data,'lg');
		dlg.result.then(function(user){
			console.log("Data retrieved from initial dialog " + JSON.stringify(user));
			$scope.myNick = user.nickname;
			sharedDataService.setNickName(user.nickname);
			$scope.updateNiks(user);		// commented for test
			//if(user.invitation != null && user.invitation != "") {
			//	var rec_player = $scope.retrievePlayerFromNick(user.invitation);
			//}
		});
    };
    
    // Method used to retrieve the player mail by opening a modal form that user has to complete
    $scope.retrieveMailForPlayer = function(){
    	var data = null;
    	var dlg = $dialogs.create('/dialogs/mailinput.html','mailDialogCtrl',data,'lg');
		dlg.result.then(function(user){
			console.log("Data retrieved from initial dialog " + JSON.stringify(user));
			$scope.playerMail = user.mail;
			$scope.updateMail(user);
		});
    };
    
    // Method used to retrieve the player surveyData by opening a modal form that user has to compile
    $scope.retrieveSurveyDataForPlayer = function(){
    	var data = null;
    	var dlg = $dialogs.create('/dialogs/surveyinput.html','surveyDialogCtrl',data,'lg');
		dlg.result.then(function(user){
			console.log("Data retrieved from initial dialog " + JSON.stringify(user));
			$scope.surveyData = user.surveyData;
			$scope.updateSurvey(user);
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
    		console.log(JSON.stringify(playerData));
    		var badgesStrings = $scope.checkBadges($scope.userProfile.badges);
    		$scope.profileDataToString(scores, badgesStrings[0], badgesStrings[1]);
    	}
    	$scope.setLoading(false);
    };
    
    $scope.loadOldChallenges = function(){
    	$scope.actualChellenges = false;
    };
    
    $scope.loadActualChallenges = function(){
    	$scope.actualChellenges = true;
    };
    
    $scope.profileDataToString = function(scores, b_string_fb, b_string_tw){
    	// here I have to create the correct string representing the user profile situation
    	var pointString = " " + scores[0].score + " punti Green Leaves; ";
    	var profileStringFb = pointString + b_string_fb;
    	var profileStringTw = pointString + b_string_tw;
    	$scope.text_fb = "ViaggiaRovereto Play&Go: " + profileStringFb;
        $scope.text_tw = "ViaggiaRovereto Play&Go: " + profileStringTw + " #viaggiaroveretoplayandgo";
        //$scope.text_gp = "Green Game Rovereto: " + profileString;
    };
    
    $scope.retrievePlayerFromNick = function(invitation){
    	var found = false;
    	var player = {};
    	var playerList = sharedDataService.getPlayersList();
		for(var i = 0; (i < playerList.length) && !found; i++){
			if(playerList[i].nikName == invitation){
				player = playerList[i];
				found = true;
			}
		}
		return player;
    };
    
    // Method used to load only the custom data from the user profile data
    $scope.correctCustomData = function(customdata, pointConcept){
    	var challIndxArray = [];
    	$scope.challenges = [];
    	$scope.oldChallenges = [];
    	if(customdata != null && customdata != ""){
    		
    		for(var keyName in customdata){
    			var key = keyName;
    			var value = customdata[keyName];
    			if(key.indexOf($scope.CHAL_K) > -1){
    				// case key is a challeng key
    				var challangeString = key.substring(3);
    				var partialIndex = challangeString.split("_");
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
    			if(ch_id.length <= 39){	// to solve error DAS in id
    			var ch_type = customdata[$scope.CHAL_K+ch_id+$scope.CHAL_K_TYPE];
    			var target = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_TARGET];
				var bonus = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_BONUS];
				if(bonus == null || bonus == "")bonus = 0;
				var endChTs = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_ETS];
				var startChTs =  customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_STS];
				if(endChTs == null || endChTs == "")endChTs = 0;
				if(startChTs == null || startChTs == "")startChTs = 0;
				var point_type = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_POINT_TYPE];
				if(point_type == null || point_type == "")point_type = $scope.CHAL_ALLOWED_PT_GREEN;
				var now = new Date().getTime();
				var daysToEnd = $scope.calculateRemainingDays(endChTs, now);
				var success = (customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_SUCCESS] != null) ? customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_SUCCESS] : false;
				if(success == null || success == "")success = false;
				var active = (now < endChTs);
				var status = 0;
				var row_status = 0;
    			var tmp_chall = {};
    			if(target == 0 || target == null)target = 1;	// to solve error DAS division by zero
    			if(ch_type == null || ch_type == "")ch_type = $scope.CHAL_TYPE_1;
    			switch(ch_type){
    				case $scope.CHAL_TYPE_1:
    					var walked_km = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_WALKED_KM];
    					if(walked_km == null || walked_km == "")walked_km = 0;
    					var mobility_mode = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_MODE];
    					if(mobility_mode == null || mobility_mode == "")mobility_mode = $scope.CHAL_ALLOWED_MODE_B;
    					status = walked_km * 100 / target;
    					if(walked_km < 1){
    						row_status = Math.round(walked_km * 10) / 10 + "/" + target;
    					} else {
    						row_status = Math.floor(walked_km) + "/" + target;
    					}
    					if(status > 100)status = 100;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type), //"img/health/healthLeavesTesto.svg",
    						desc: $scope.correctDesc($scope.CHAL_DESC_1, target, bonus, point_type, mobility_mode, null), //"Aumenta del 15% i km fatti a piedi e avrai 50 punti bonus",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						mobilityMode: mobility_mode,
    						Km_walked_during_challenge: walked_km,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;
    				case $scope.CHAL_TYPE_1A:
    					var walked_km = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_WALKED_KM];
    					if(walked_km == null || walked_km == "")walked_km = 0;
    					var mobility_mode = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_MODE];
    					if(mobility_mode == null || mobility_mode == "")mobility_mode = $scope.CHAL_ALLOWED_MODE_B;
    					status = walked_km * 100 / target;
    					if(walked_km < 1){
    						row_status = Math.round(walked_km * 10) / 10 + "/" + target;
    					} else {
    						row_status = Math.floor(walked_km) + "/" + target;
    					}
    					if(status > 100)status = 100;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type), //"img/health/healthLeavesTesto.svg",
    						desc: $scope.correctDesc($scope.CHAL_DESC_1, target, bonus, point_type, mobility_mode, null), //"Aumenta del 15% i km fatti a piedi e avrai 50 punti bonus",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						mobilityMode: mobility_mode,
    						Km_walked_during_challenge: walked_km,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;
    				case $scope.CHAL_TYPE_2:
    					var count = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_COUNTER];
    					if(count == null || count == "")count = 0;
    					var mobility_mode = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_MODE];
    					if(mobility_mode == null || mobility_mode == "")mobility_mode = $scope.CHAL_ALLOWED_MODE_B;
    					status = count * 100 / target;
    					row_status = count + "/" + target;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_2, target, bonus, point_type, mobility_mode), //"Fai almeno N viaggio/i senza usare 'la macchina' e vinci un bonus di 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						mobilityMode: mobility_mode,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						counter: count,
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;
    				case $scope.CHAL_TYPE_3:
    					var count = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_COUNTER];
    					if(count == null || count == "")count = 0;
    					var mobility_mode = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_MODE];
    					if(mobility_mode == null || mobility_mode == "")mobility_mode = $scope.CHAL_ALLOWED_MODE_B;
    					status = count * 100 / target;
    					row_status = count + "/" + target;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_3, target, bonus, point_type, mobility_mode), //"Fai almeno N viaggio/i con il 'Bike sharing' e vinci un bonus di 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						mobilityMode: mobility_mode,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						counter: count,
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;
    				case $scope.CHAL_TYPE_3A:
    					var count = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_COUNTER];
    					if(count == null || count == "")count = 0;
    					var mobility_mode = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_MODE];
    					if(mobility_mode == null || mobility_mode == "")mobility_mode = $scope.CHAL_ALLOWED_MODE_B;
    					status = count * 100 / target;
    					row_status = count + "/" + target;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_3, target, bonus, point_type, mobility_mode), //"Fai almeno N viaggio/i con il 'Bike sharing' e vinci un bonus di 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						mobilityMode: mobility_mode,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						counter: count,
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;	
    				case $scope.CHAL_TYPE_4:
    					var count = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_COUNTER];
    					if(count == null || count == "")count = 0;
    					//var mobility_mode = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_MODE];
    					var mobility_mode = $scope.CHAL_ALLOWED_MODE_Z;
    					status = count * 100 / target;
    					row_status = count + "/" + target;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_3, target, bonus, point_type, mobility_mode, null), //"Fai almeno N viaggio/i con il 'Bike sharing' e vinci un bonus di 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						mobilityMode: mobility_mode,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						counter: count,
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;	
    				case $scope.CHAL_TYPE_5:
    					var success = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_SUCCESS];
    					if(success == null || success == "")success = false;
    					var earned_points_type = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_WEAKLY_EARNED_POINT];	// key with _
    					if(earned_points_type == null){
    						earned_points_type = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_WEAKLY_EARNED_POINT2];	// key without _
    					}	
    					var earned_points = $scope.getWeekPointFromName(pointConcept,earned_points_type);
    					/*var earned_points = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_EARNED_POINT];
    					if(earned_points == null){
    						earned_points = customdata[$scope.CHAL_K_EARNED_POINT_NEW];
    					}
    					if(earned_points == null){
    						earned_points = 0;
    					}*/
    					status = earned_points * 100 / target;
    					row_status = earned_points + "/" + target;
    					if(status > 100)status = 100;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type), //"img/health/healthLeavesTesto.svg",
    						desc: $scope.correctDesc($scope.CHAL_DESC_5, target, bonus, point_type, "", null), //"Aumenta del 15% i km fatti a piedi e avrai 50 punti bonus",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						pt_earned_during_challenge: earned_points,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;
    				case $scope.CHAL_TYPE_6:
    					var count = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_COUNTER];
    					if(count == null || count == "")count = 0;
    					var badge_coll_name = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_BADGE_COLL_NAME];
    					if(badge_coll_name == null || badge_coll_name == "")badge_coll_name = "green leaves";
    					status = count * 100 / target;
    					row_status = count + "/" + target;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_6, target, bonus, point_type, null, badge_coll_name), //"Fai almeno N viaggio/i con il 'Bike sharing' e vinci un bonus di 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						mobilityMode: badge_coll_name,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						counter: count,
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;	
    				case $scope.CHAL_TYPE_7:
    					var success = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_SUCCESS];
    					if(success == null || success == "")success = false;
    					if(success){
    						status = 100;
    						row_status = "1/1";
    					} else {
    						row_status = "0/1";
    					}
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_7, target, bonus, point_type, "", null), //"completa una Badge Collection e vinci un bonus di 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;
    				case $scope.CHAL_TYPE_8:
    					var survey = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_SURVEY];
    					if(survey == null)survey = 0;
    					if(success){
    						survey = 1;
    					}
    					status = survey * 100 / target;
    					row_status = survey + "/" + target;
    					if(status > 100)status = 100;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_8, target, bonus, point_type, "", null), //"compila il questionario di fine gioco e guadagni 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						recommandation: recommandation,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;		
    				case $scope.CHAL_TYPE_9:
    					var recommandation = customdata[$scope.CHAL_K + ch_id + $scope.CHAL_K_RECOM];
    					if(recommandation == null || recommandation == "")recommandation = 0;
    					status = recommandation * 100 / target;
    					row_status = recommandation + "/" + target;
    					if(status > 100)status = 100;
    					tmp_chall = {
    						id: challIndxArray[i],
    						icon: $scope.getCorrectIcon(point_type),
    						desc: $scope.correctDesc($scope.CHAL_DESC_9, target, bonus, point_type, "", null), //"raccomanda la App ad almeno 10 utenti e guadagni 50 Green Points",
    						startChTs: startChTs,
    						endChTs: endChTs,
    						daysToEnd: daysToEnd,
    						recommandation: recommandation,
    						target: target,
    						bonus: bonus,
    						bonus_style: $scope.getWidthPosByIntValue(bonus),
    						bonus_style_small: $scope.getWidthPosByIntValue(bonus) + "_small",
    						bonus_success: $scope.getWidthPosByIntValueSucc(bonus),
    						bonus_success_small: $scope.getWidthPosByIntValueSucc(bonus) + "_small",
    						status: status,
    						row_status: row_status,
    						row_status_style: $scope.getWidthPosByStringLength(row_status.length),
    						row_status_style_small: $scope.getWidthPosByStringLength(row_status.length) + "_small",
    						active: active,
    						type: ch_type,
    						point_type: $scope.getCorrectTypeString(point_type),
    						success: success,
    						progress_img: $scope.convertStatusToIcon(status, success),
    						details: false
    					};
    					break;	
    				default: break;
    			}
    			if(now >= startChTs){
	    			if(now < (endChTs + $scope.CHAL_TS_OFFSET)){
	    				$scope.challenges.push(tmp_chall);
	    			} else {
	    				$scope.oldChallenges.push(tmp_chall);
	    			}
    			}
    			}	
    		}
    	}
    	$scope.setLoading(false);
    };
    
    $scope.getWeekPointFromName = function(list, name){
    	var found = false;
    	var points = 0;
    	for(var i = 0; (i < list.length) && !found; i++){
    		if(list[i].name == name){
    			found = true;
    			points = list[i].score;
    		}
    	}
    	return points;
    };
    
    $scope.getWidthPosByStringLength = function(stringlength){
    	var style_name = "";
    	switch(stringlength){
    		case 2: 
    			style_name = "oriz_space_2";
    			break;
    		case 3: 
    			style_name = "oriz_space_3";
    			break;
    		case 4: 
    			style_name = "oriz_space_4";
    			break;
    		case 5: 
    			style_name = "oriz_space_5";
    			break;	
    		default: 
    			style_name = "oriz_space_6";
    			break;
    	}
    	return style_name;
    };
    
    $scope.getWidthPosByIntValue = function(intvalue){
    	var style_name = "";
    	if(intvalue < 100){
    		style_name = "oriz_space_bonus_2";
    	} else if(intvalue < 1000){
    		style_name = "oriz_space_bonus_3";
    	} else {
    		style_name = "oriz_space_bonus_4";
    	}
    	return style_name;
    };
    
    $scope.getWidthPosByIntValueSucc = function(intvalue){
    	var style_name = "";
    	if(intvalue < 100){
    		style_name = "image-bonus-success-num_2";
    	} else {
    		style_name = "image-bonus-success-num_3";
    	}
    	return style_name;
    };
    
    $scope.getCorrectIcon = function(p_type){
    	if(p_type == $scope.CHAL_ALLOWED_PT_GREEN){
    		return "img/green/greenLeavesTesto.svg";
    	} else if(p_type == $scope.CHAL_ALLOWED_PT_HEALTH){
    		return "img/health/healthLeavesTesto.svg";
    	} else if(p_type == $scope.CHAL_ALLOWED_PT_PR){
    		return "img/pr/p&rLeavesBase.svg";
    	}
    };
    
    $scope.getCorrectTypeString = function(stringtype){
    	if(stringtype == $scope.CHAL_ALLOWED_PT_GREEN){
    		return $scope.CHAL_PT_GREEN_STRING;
    	}
    	if(stringtype == $scope.CHAL_ALLOWED_PT_HEALTH){
    		return $scope.CHAL_PT_HEALTH_STRING;
    	}
    	if(stringtype == $scope.CHAL_ALLOWED_PT_PR){
    		return $scope.CHAL_PT_PR_STRING;
    	}
    };
    
    $scope.convertStatusToIcon = function(status, success){
    	if(success){
    		return "img/challenges_icon/coppa_10_10_sfida_vinta.png";
    	} else {
	    	if(status < 10){
	    		return "img/challenges_icon/coppa_0_10.png";
	    	} else if(status >= 10 && status < 20){
	    		return "img/challenges_icon/coppa_1_10.png";
	    	} else if(status >= 20 && status < 30){
	    		return "img/challenges_icon/coppa_2_10.png";
	    	} else if(status >= 30 && status < 40){
	    		return "img/challenges_icon/coppa_3_10.png";
	    	} else if(status >= 40 && status < 50){
	    		return "img/challenges_icon/coppa_4_10.png";
	    	} else if(status >= 50 && status < 60){
	    		return "img/challenges_icon/coppa_5_10.png";
	    	} else if(status >= 60 && status < 70){
	    		return "img/challenges_icon/coppa_6_10.png";
	    	} else if(status >= 70 && status < 80){
	    		return "img/challenges_icon/coppa_7_10.png";
	    	} else if(status >= 80 && status < 90){
	    		return "img/challenges_icon/coppa_8_10.png";
	    	} else if(status >= 90 && status < 100){
	    		return "img/challenges_icon/coppa_9_10.png";
	    	} else if(status >= 100){
	    		return "img/challenges_icon/coppa_10_10_sfida_vinta.png";
	    	}
    	}
    };
    
    $scope.calculateRemainingDays = function(endTimeMillis, now){
    	var remainingDays = 0;
    	if(now < endTimeMillis){
    		var tmpMillis = endTimeMillis - now;
    		remainingDays = Math.ceil(tmpMillis / $scope.MILLIS_IN_DAY);
    	}
    	return remainingDays;
    };
    
    $scope.getChallStyle = function(active, status, success){
    	if(success || status == 100){
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
    
    $scope.correctDesc = function(desc, target, bonus, p_type, mode, coll_name){
    	if(desc.indexOf("TARGET") > -1){
    		desc = desc.replace("TARGET", target);
    	}
    	if(target == 1 && desc.indexOf('utenti') > -1){
    		desc = desc.replace("utenti", "utente");
    	}
    	if(target == 1 && desc.indexOf("altri") > -1){
    		desc = desc.replace("altri", "");
    		desc = desc.replace("km", "altro km");
    	}
    	if(target > 1 && desc.indexOf('viaggio') > -1){
    		desc = desc.replace("viaggio", "viaggi");
    	}
    	if(desc.indexOf("BONUS") > -1){
    		desc = desc.replace("BONUS", bonus);
    	}
    	if(desc.indexOf("POINT_TYPE") > -1){
    		desc = desc.replace(new RegExp("POINT_TYPE", 'g'), $scope.getCorrectPointType(p_type));
    	}
    	if(mode != null && mode != ""){
    		if(desc.indexOf("MODE") > -1){
    			if(desc.indexOf('senza') > -1){
    				desc = desc.replace("MODE", $scope.getCorrectMode(mode, 1));
    			} else {
    				desc = desc.replace("MODE", $scope.getCorrectMode(mode, 0));
    			}
    		}
    	}
    	if(coll_name != null && coll_name != ""){
    		if(desc.indexOf("BADGE_COLL_NAME") > -1){
    			desc = desc.replace("BADGE_COLL_NAME", coll_name);
    		}
    	}
    	return desc;
    };
    
    $scope.getCorrectMode = function(mode, type){
    	var corr_mode = "";
    	switch(mode){
	    	case $scope.CHAL_ALLOWED_MODE_W: 
	    		corr_mode = (type == 0) ? "a piedi" : "spostamenti a piedi";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_BK: 
	    		corr_mode = (type == 0) ? "in bici" : "la bici";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_BKS: 
	    		corr_mode = (type == 0) ? "con bici condivisa" : "la bici condivisa";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_T: 
	    		corr_mode = (type == 0) ? "in treno" : "il treno";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_B: 
	    		corr_mode = (type == 0) ? "in autobus" : "l'autobus";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_C: 
	    		corr_mode = (type == 0) ? "in auto" : "l'automobile";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_Z: 
	    		corr_mode = (type == 0) ? "a impatto zero" : "spostamenti a impatto zero";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_P: 
	    		corr_mode = (type == 0) ? "promoted" : "spostamenti promoted";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_W + "Distance": 
	    		corr_mode = (type == 0) ? "a piedi" : "spostamenti a piedi";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_BK + "Distance": 
	    		corr_mode = (type == 0) ? "in bici" : "la bici";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_BKS + "Distance": 
	    		corr_mode = (type == 0) ? "con bici condivisa" : "la bici condivisa";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_T + "Distance": 
	    		corr_mode = (type == 0) ? "in treno" : "il treno";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_B + "Distance": 
	    		corr_mode = (type == 0) ? "in autobus" : "l'autobus";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_C + "Distance": 
	    		corr_mode = (type == 0) ? "in auto" : "l'automobile";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_Z + "Distance": 
	    		corr_mode = (type == 0) ? "a impatto zero" : "spostamenti a impatto zero";
	    		break;
	    	case $scope.CHAL_ALLOWED_MODE_P + "Distance": 
	    		corr_mode = (type == 0) ? "promoted" : "spostamenti promoted";
	    		break;	
	    	default: break;
    	};
    	return corr_mode;
    };
    
    $scope.getCorrectPointType = function(ptype){
    	if(ptype == $scope.CHAL_ALLOWED_PT_GREEN){
    		return "Green Leaves";
    	} else if(ptype == $scope.CHAL_ALLOWED_PT_HEALTH){
    		return "Salute";
    	} else if(ptype == $scope.CHAL_ALLOWED_PT_PR){
    		return "Park & Ride";
    	}
    };
    
    // Method used to load only the used data from the players list
    $scope.correctClassificationData = function(object){
    	var list = (object) ? object.content : [];
    	$scope.GameClassification = [];
    	$scope.GameClassificationTot = [];
    	$scope.GameClassificationActual = [];
    	$scope.GameClassificationLast = [];
    	var listSize = null;
    	if(list != null && list.length > 0){
    		if($scope.useShortClassification){
    			listSize = $scope.maxPlayersClassShort;
    		}
    		for(var i = 0; i < list.length; i++){
        		//var badges = $scope.getBadgesList(list[i].state);
        		var scores = $scope.getStateList(list[i].state);
        		var playerData = {
        				id : list[i].playerId,
        				name: $scope.getPlayerNameById(list[i].playerId),
        				gameId : list[i].gameId,
        				//badges : badges,
        				score : scores,
        				class_pos_g : 1,
        				class_pos_h : 1,
        				class_pos_p : 1,
        				class_pos_aw : 1,
        				class_pos_lw : 1
        		};
        		//MB19112014: added check to name to consider only the players in list
        		if(playerData.name != null && playerData.name != ""){
        			$scope.GameClassification.push(playerData);
        		}
        	}
    		// Here I have to order the list by user scores
    		var greenClassificationTmp = $scope.orderByScores(1, $scope.GameClassification);
    		$scope.GameClassificationTot = $scope.resizeClassificationList($scope.setCorrectPosGreen(greenClassificationTmp), listSize);
    		$scope.playerClassTot = $scope.getPlayerClassificationPositionData($scope.GameClassificationTot);
    		sharedDataService.setClassification($scope.GameClassificationTot);
    		sharedDataService.setPlayerClassTot($scope.playerClassTot);
    		//var HealthClassificationTmp = $scope.orderByScores(2, $scope.GameClassification);
    		//$scope.GameClassification = $scope.setCorrectPosHealth(HealthClassificationTmp);
    		//var PRClassificationTmp = $scope.orderByScores(3, $scope.GameClassification);
    		//$scope.GameClassification = $scope.setCorrectPosPR(PRClassificationTmp);
    		var ActualWeekClassificationTmp = $scope.orderByScores(4, $scope.GameClassification);
    		$scope.GameClassificationActual = $scope.resizeClassificationList($scope.setCorrectPosActualWeek(ActualWeekClassificationTmp), listSize);
    		$scope.playerClassActual = $scope.getPlayerClassificationPositionData($scope.GameClassificationActual);
    		sharedDataService.setClassificationActual($scope.GameClassificationActual);
    		sharedDataService.setPlayerClassActual($scope.playerClassActual);
    		var LastWeekClassificationTmp = $scope.orderByScores(5, $scope.GameClassification);
    		$scope.GameClassificationLast = $scope.resizeClassificationList($scope.setCorrectPosLastWeek(LastWeekClassificationTmp), listSize);
    		$scope.playerClassLast = $scope.getPlayerClassificationPositionData($scope.GameClassificationLast);
    		sharedDataService.setClassificationLast($scope.GameClassificationLast);
    		sharedDataService.setPlayerClassLast($scope.playerClassLast);
    	}
    	$scope.setLoading(false);
    };
    
    // Method getPlayerClassificationPositionData: retrieve the player data (status and position) in a specific classification list
    $scope.getPlayerClassificationPositionData = function(list){
    	for(var i = 0; i < list.length; i++){
			if(list[i].id == $scope.userId){
				return list[i];
			}
		}
    	return null;
    }
    
    // Method resizeClassificationList: resize a list to a specific size
    $scope.resizeClassificationList = function(list, listSize){
    	if(listSize){
    		var found = false
    		var completeList = [];
    		angular.copy(list, completeList);
    		if(listSize > completeList.length)listSize = completeList.length;
    		var resizedList = completeList.splice(0, listSize);
    		for(var i = 0; i < listSize && !found; i++){
    			if($scope.userId == resizedList[i].id){
    				found = true;
    			}
    		}
    		if(!found){
    			for(var i = 0; i < list.length && !found; i++){
        			if($scope.userId == list[i].id){
        				found = true;
        				resizedList.push(list[i]);
        			}
        		}
    		}
    		return resizedList;
    	} else {
    		return list;
    	}
    };
    
    // Method used to load the different badges in a list
    $scope.getBadgesList = function(object){
    	var badge_list = (object) ? object.BadgeCollectionConcept : [];
    	var badges = [];
    	var badges_green = {};
    	var badges_health = {};
    	var badges_pr = {};
    	var badges_zero_impact = {};
    	var badges_public_transport = {};
    	var badges_bike = {};
    	var badges_recommendation = {};
    	var badges_bike_sharing = {};
    	var badges_leadertop = {};
    	var badges_special = {};
    	if(badge_list){
	    	for(var i = 0; i < badge_list.length; i++){
	    		if(badge_list[i].badgeEarned != null){ 
	    			if(badge_list[i].name == $scope.BCN_GREEN){
	    				badges_green = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_HEALTH){
	    				badges_health = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_PARK_AND_RIDE){
	    				badges_pr = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_SPECIAL){
	    				badges_special = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_BIKE){
	    				badges_bike = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_BIKE_SHARING){
	    				badges_bike_sharing = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_ZERO_IMPACT){
	    				badges_zero_impact = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_PUBLIC_TRANSPORT){
	    				badges_public_transport = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_RECOMMENDATION){
	    				badges_recommendation = badge_list[i];
	    			} else if(badge_list[i].name == $scope.BCN_LEADERBOARD3){
	    				badges_leadertop = badge_list[i];
	    			}
	    		}
	    	}
    	}
    	
    	badges.splice(0, 0, badges_green);
    	badges.splice(1, 0, badges_health);
    	badges.splice(2, 0, badges_pr);
    	badges.splice(3, 0, badges_special);
    	badges.splice(4, 0, badges_bike);				//bike
    	badges.splice(5, 0, badges_bike_sharing);		//bike sharing
    	badges.splice(6, 0, badges_zero_impact);		//zero impact
    	badges.splice(7, 0, badges_public_transport);	//public transport
    	badges.splice(8, 0, badges_recommendation);		//recommendation
    	badges.splice(9, 0, badges_leadertop);			//leadertop
  	
    	return badges;
    };
    
    // Method used to load the different score category in a list
    $scope.getStateList = function(object){
    	var state_list = (object) ? object.PointConcept : [];
    	var states = [];
    	var state_greenL = {};
    	var state_health = {};
    	var state_pr = {};
    	var state_aw = {};
    	var state_lw = {};
    	var week_id = $scope.getCorrectActualWeek(new Date().getTime());
    	var last_week_id = week_id - 1;
    	var actual_week = "";
    	var last_week = "";
    	if($scope.is_test){
    		actual_week = $scope.week_test_classification + "" + week_id;
    		last_week = $scope.week_test_classification + "" + last_week_id;
    	} else {
    		actual_week = $scope.week_classification + "" + week_id;
    		last_week = $scope.week_classification + "" + last_week_id;
    	}
    	
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
	    			} else if (state_list[i].name == "pr"){
		    			state_pr = {
		    					id : state_list[i].id,
		    					name : state_list[i].name,
		    					score : state_list[i].score
		    			};
	    			} else if (state_list[i].name == actual_week){
		    			state_aw = {
		    					id : state_list[i].id,
		    					name : state_list[i].name,
		    					score : state_list[i].score
		    			};
	    			} else if (state_list[i].name == last_week){
		    			state_lw = {
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
    	states.splice(3, 0, state_aw);
    	states.splice(4, 0, state_lw);
    	
    	return states;
    };
    
    // Method getCorrectActualWeek: used to retrieve the correct game week number
    $scope.getCorrectActualWeek = function(now_millis){
    	var actual_week = 0;
    	if(now_millis >= $scope.start_week_9){
    		actual_week = 9;
    	} else if(now_millis >= $scope.start_week_8){
    		actual_week = 8;
    	} else if(now_millis >= $scope.start_week_7){
    		actual_week = 7;
    	} else if(now_millis >= $scope.start_week_6){
    		actual_week = 6;
    	} else if(now_millis >= $scope.start_week_5){
    		actual_week = 5;
    	} else if(now_millis >= $scope.start_week_4){
    		actual_week = 4;
    	} else if(now_millis >= $scope.start_week_3){
    		actual_week = 3;
    	} else if(now_millis >= $scope.start_week_2){
    		actual_week = 2;
    	} else if(now_millis >= $scope.start_week_1){
    		actual_week = 1;
    	}
    	return actual_week;
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
    		case 4: // case actual week score
    			list.sort($scope.awScoreCompare);
    			break;
    		case 5: // case last week score
    			list.sort($scope.lwScoreCompare);
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
    
    $scope.awScoreCompare = function(a, b){
    	if(a.score[3].score > b.score[3].score)
    		return -1;
    	if(a.score[3].score < b.score[3].score)
    		return 1;
    	return 0;
    };
    
    $scope.lwScoreCompare = function(a, b){
    	if(a.score[4].score > b.score[4].score)
    		return -1;
    	if(a.score[4].score < b.score[4].score)
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
    
    $scope.setCorrectPosActualWeek = function(list){
    	for(var i = 1; i < list.length; i++){
    		if(list[i].score[3].score < list[i-1].score[3].score){
    			list[i].class_pos_aw = i + 1;
    		} else {
    			list[i].class_pos_aw = list[i - 1].class_pos_aw;
    		}
    	}
    	return list;
    };
    
    $scope.setCorrectPosLastWeek = function(list){
    	for(var i = 1; i < list.length; i++){
    		if(list[i].score[4].score < list[i-1].score[4].score){
    			list[i].class_pos_lw = i + 1;
    		} else {
    			list[i].class_pos_lw = list[i - 1].class_pos_lw;
    		}
    	}
    	return list;
    };
    // ------------------------------------------------------------------------------------------------------------------------------------
    
    $scope.checkBadges = function(list){
    	var greenBadges = 0;
    	var bikeTripBadges = 0;
    	var bikeShareBadges = 0;
    	var zeroImpactBadges = 0;
    	var publicTransportBadges = 0;
    	var parkRideBadges = 0;
    	var recommendationBadges = 0;
    	var leaderBoardBadges = 0;
    	
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
    	
    	// Green Leaves badges
    	$scope.userShowGreenLeaves50 = false;
    	$scope.userShowGreenLeaves100 = false;
    	$scope.userShowGreenLeaves200 = false;
    	$scope.userShowGreenLeaves400 = false;
    	$scope.userShowGreenLeaves800 = false;
    	$scope.userShowGreenLeaves1500 = false;
    	$scope.userShowGreenLeaves2500 = false;
    	$scope.userShowGreenLeaves5000 = false;
    	$scope.userShowGreenLeaves10000 = false;
    	$scope.userShowGreenLeaves20000 = false;
    	
    	// Bike aficionado
    	$scope.userShowBikeTrip1 = false;
    	$scope.userShowBikeTrip5 = false;
    	$scope.userShowBikeTrip10 = false;
    	$scope.userShowBikeTrip25 = false;
    	$scope.userShowBikeTrip50 = false;
    	$scope.userShowBikeTrip100 = false;
    	$scope.userShowBikeTrip200 = false;
    	$scope.userShowBikeTrip500 = false;
    	
    	// Bike sharing
    	$scope.userShowBikeSharingBrione = false;
    	$scope.userShowBikeSharingLizzana = false;
    	$scope.userShowBikeSharingMarco = false;
    	$scope.userShowBikeSharingMunicipio = false;
    	$scope.userShowBikeSharingNoriglio = false;
    	$scope.userShowBikeSharingOrsi = false;
    	$scope.userShowBikeSharingOspedale = false;
    	$scope.userShowBikeSharingPaoli = false;
    	$scope.userShowBikeSharingPRosmini = false;
    	$scope.userShowBikeSharingQuercia = false;
    	$scope.userShowBikeSharingSacco = false;
    	$scope.userShowBikeSharingStazione = false;
    	$scope.userShowBikeSharingZonaIndustriale = false;
    	$scope.userShowBikeSharingMart = false;
    	$scope.userBikeSharingPresent = false;
    	
    	// Zero impact
    	$scope.userShowZeroImpact1 = false;
    	$scope.userShowZeroImpact5 = false;
    	$scope.userShowZeroImpact10 = false;
    	$scope.userShowZeroImpact25 = false;
    	$scope.userShowZeroImpact50 = false;
    	$scope.userShowZeroImpact100 = false;
    	$scope.userShowZeroImpact200 = false;
    	$scope.userShowZeroImpact500 = false;
    	
    	// Public transport aficionado
    	$scope.userShowPublicTransport5 = false;
    	$scope.userShowPublicTransport10 = false;
    	$scope.userShowPublicTransport25 = false;
    	$scope.userShowPublicTransport50 = false;
    	$scope.userShowPublicTransport100 = false;
    	$scope.userShowPublicTransport200 = false;
    	$scope.userShowPublicTransport500 = false;
    	
    	// Park And Ride pioneer
    	$scope.userShowParkAndRideStadio = false;
    	$scope.userShowParkAndRideManifattura = false;
    	
    	// Recommendation
    	$scope.userShowRecommendation3 = false;
    	$scope.userShowRecommendation5 = false;
    	$scope.userShowRecommendation10 = false;
    	$scope.userShowRecommendation25 = false;
    	
    	// Special leaderboard
    	$scope.userShowFirstOfWeek = false;
    	$scope.userShowSecondOfWeek = false;
    	$scope.userShowThirdOfWeek = false;
    	$scope.userLeaderboardPresent = false;
    	
    	var badgeString = "badge guadagnati: ";
    	
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
	    				case "50" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves50 = true;
	    					badgeString += "50 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "100" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves100 = true;
	    					badgeString += "100 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "200" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves200 = true;
	    					badgeString += "200 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "400" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves400 = true;
	    					badgeString += "400 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "800" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves800 = true;
	    					badgeString += "800 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "1500" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves1500 = true;
	    					badgeString += "1500 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "2500" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves2500 = true;
	    					badgeString += "2500 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "5000" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves5000 = true;
	    					badgeString += "5000 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "10000" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves10000 = true;
	    					badgeString += "10000 punti green, ";
	    					greenBadges += 1;
	    					break;
	    				case "20000" + $scope.BG_GREEN_POINT :
	    					$scope.userShowGreenLeaves20000 = true;
	    					badgeString += "20000 punti green, ";
	    					greenBadges += 1;
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
		    			case "Stadio" + $scope.BG_PARK_AND_RIDE :
	    					$scope.userShowParkAndRideStadio = true;
	    					badgeString += "Parcheggio-Stadio, ";
	    					parkRideBadges += 1;
	    					break;
	    				case "Manifattura" + $scope.BG_PARK_AND_RIDE :
	    					$scope.userShowParkAndRideManifattura = true;
	    					badgeString += "Parcheggio-Manifattura, ";
	    					parkRideBadges += 1;
	    					break;
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
	    				/*case "MANIFATTURA-park" :
	    					$scope.userShowManifatturaPark = true;
	    					badgeString += "Parcheggio-Manifattura, ";
	    					break;
	    				case "STADIO-park" :
	    					$scope.userShowQuerciaPark = true;
	    					badgeString += "Parcheggio-Stadio, ";
	    					break;
	    				case "CENTRO STORICO-park" :
	    					$scope.userShowCentroStoricoPark = true;
	    					break;
	    				case "PARCHEGGIO CENTRO-park" :
	    			    	$scope.userShowParcheggioCentroPark = true;
	    					break;
	    				case "P.LE A.LEONI-park" :
	    			    	$scope.userShowALeoniPark = true;	
	    			    	break;*/	
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
    		// bike aficionado
    		if(list[4].badgeEarned){
	    		for(var i = 0; i < list[4].badgeEarned.length; i++){
	    			switch(list[4].badgeEarned[i]){
		    			case "1" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip1 = true;
	    					badgeString += "1 viaggio in bici, ";
	    					bikeTripBadges += 1;
	    					break;
	    				case "5" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip5 = true;
	    					badgeString += "5 viaggio in bici, ";
	    					bikeTripBadges += 1;
	    					break;
	    				case "10" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip10 = true;
	    					badgeString += "10 viaggio in bici, ";
	    					bikeTripBadges += 1;
	    					break;
	    				case "25" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip25 = true;
	    					badgeString += "25 viaggio in bici, ";
	    					bikeTripBadges += 1;
	    					break;
	    				case "50" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip50 = true;
	    					bikeTripBadges += "50 viaggio in bici, ";
	    					bikeShareBadges += 1;
	    					break;
	    				case "100" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip100 = true;
	    					badgeString += "100 viaggio in bici, ";
	    					bikeTripBadges += 1;
	    					break;
	    				case "200" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip200 = true;
	    					bikeTripBadges += "200 viaggio in bici, ";
	    					bikeShareBadges += 1;
	    					break;	
	    				case "500" + $scope.BG_BIKE_TRIP :
	    					$scope.userShowBikeTrip500 = true;
	    					bikeTripBadges += "500 viaggio in bici, ";
	    					bikeShareBadges += 1;
	    					break;
	    				default: break;
	    			}
	    		}
    		}
    		// bike sharing
    		if(list[5].badgeEarned){
	    		for(var i = 0; i < list[5].badgeEarned.length; i++){
	    			switch(list[5].badgeEarned[i]){
		    			case "Brione - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingBrione = true;
	    					badgeString += "Parcheggio Bike Sharing Brione, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Lizzana - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingLizzana = true;
	    					badgeString += "Parcheggio Bike Sharing di Lizzana, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Marco - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingMarco = true;
	    					badgeString += "Parcheggio Bike Sharing di Marco, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Municipio - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingMunicipio = true;
	    					badgeString += "Parcheggio Bike Sharing Municipio, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Noriglio - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingNoriglio = true;
	    					badgeString += "Parcheggio Bike Sharing di Noriglio, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Orsi - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingOrsi = true;
	    					badgeString += "Parcheggio Bike Sharing Orsi, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Ospedale - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingOspedale = true;
	    					badgeString += "Parcheggio Bike Sharing Ospedale, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Via Paoli - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingPaoli = true;
	    					badgeString += "Parcheggio Bike Sharing Paoli, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "P. Rosmini - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingPRosmini = true;
	    					badgeString += "Parcheggio Bike Sharing di P. Rosmini, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Quercia - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingQuercia = true;
	    					badgeString += "Parcheggio Bike Sharing Quercia, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Sacco - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingSacco = true;
	    					badgeString += "Parcheggio Bike Sharing Sacco, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Stazione FF.SS. - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingStazione = true;
	    					badgeString += "Parcheggio Bike Sharing Stazione Treni, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Zona Industriale - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingStazione = true;
	    					badgeString += "Parcheggio Bike Sharing Zona Industriale, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;
		    			case "Mart - Rovereto" + $scope.BG_BIKE_SHARING :
		    				$scope.userShowBikeSharingMart = true;
	    					badgeString += "Parcheggio Bike Sharing Mart, ";
	    					bikeShareBadges += 1;
	    					$scope.userBikeSharingPresent = true;
	    					break;	
		    			default: break;
	    			}
	    		}
    		}
    		// zero impact
    		if(list[6].badgeEarned){
	    		for(var i = 0; i < list[6].badgeEarned.length; i++){
	    			switch(list[6].badgeEarned[i]){
		    			case "1" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact1 = true;
	    					badgeString += "1 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;
	    				case "5" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact5 = true;
	    					badgeString += "5 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;
	    				case "10" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact10 = true;
	    					badgeString += "10 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;
	    				case "25" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact25 = true;
	    					badgeString += "25 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;
	    				case "50" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact50 = true;
	    					badgeString += "50 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;
	    				case "100" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact100 = true;
	    					badgeString += "100 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;
	    				case "200" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact200 = true;
	    					badgeString += "200 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;
	    				case "500" + $scope.BG_ZERO_IMPACT :
	    					$scope.userShowZeroImpact500 = true;
	    					badgeString += "500 viaggi a impatto zero, ";
	    					zeroImpactBadges += 1;
	    					break;	
	    				default: break;
	    			}
	    		}
    		}
    		// public transport
    		if(list[7].badgeEarned){
	    		for(var i = 0; i < list[7].badgeEarned.length; i++){
	    			switch(list[7].badgeEarned[i]){
		    			case "5" + $scope.BG_PUBLIC_TRANSPORT :
	    					$scope.userShowPublicTransport5 = true;
	    					badgeString += "5 viaggi trasporto pubblico, ";
	    					publicTransportBadges += 1;
	    					break;
	    				case "10" + $scope.BG_PUBLIC_TRANSPORT :
	    					$scope.userShowPublicTransport10 = true;
	    					badgeString += "10 viaggi trasporto pubblico, ";
	    					publicTransportBadges += 1;
	    					break;
	    				case "25" + $scope.BG_PUBLIC_TRANSPORT :
	    					$scope.userShowPublicTransport25 = true;
	    					badgeString += "25 viaggi trasporto pubblico, ";
	    					publicTransportBadges += 1;
	    					break;
	    				case "50" + $scope.BG_PUBLIC_TRANSPORT :
	    					$scope.userShowPublicTransport50 = true;
	    					badgeString += "50 viaggi trasporto pubblico, ";
	    					publicTransportBadges += 1;
	    					break;
	    				case "100" + $scope.BG_PUBLIC_TRANSPORT :
	    					$scope.userShowPublicTransport100 = true;
	    					badgeString += "100 viaggi trasporto pubblico, ";
	    					publicTransportBadges += 1;
	    					break;
	    				case "200" + $scope.BG_PUBLIC_TRANSPORT :
	    					$scope.userShowPublicTransport200 = true;
	    					badgeString += "200 viaggi trasporto pubblico, ";
	    					publicTransportBadges += 1;
	    					break;
	    				case "500" + $scope.BG_PUBLIC_TRANSPORT :
	    					$scope.userShowPublicTransport500 = true;
	    					badgeString += "500 viaggi trasporto pubblico, ";
	    					publicTransportBadges += 1;
	    					break;	
	    				default: break;
	    			}
	    		}
    		}
    		// recommendation
    		if(list[8].badgeEarned){
	    		for(var i = 0; i < list[8].badgeEarned.length; i++){
	    			switch(list[8].badgeEarned[i]){
		    			case "3" + $scope.BG_RECOMMENDATION :
	    					$scope.userShowRecommendation3 = true;
	    					badgeString += "3 amici invitati, ";
	    					recommendationBadges += 1;
	    					break;
	    				case "5" + $scope.BG_RECOMMENDATION :
	    					$scope.userShowRecommendation5 = true;
	    					badgeString += "5 amici invitati, ";
	    					recommendationBadges += 1;
	    					break;
	    				case "10" + $scope.BG_RECOMMENDATION :
	    					$scope.userShowRecommendation10 = true;
	    					badgeString += "10 amici invitati, ";
	    					recommendationBadges += 1;
	    					break;
	    				case "25" + $scope.BG_RECOMMENDATION :
	    					$scope.userShowRecommendation25 = true;
	    					badgeString += "25 amici invitati, ";
	    					recommendationBadges += 1;
	    					break;
	    				default: break;
	    			}
	    		}
    		}
    		// leaderboard top 3
    		if(list[9].badgeEarned){
	    		for(var i = 0; i < list[9].badgeEarned.length; i++){
	    			switch(list[9].badgeEarned[i]){
		    			case $scope.BG_SPECIAL_FIRST :
	    					$scope.userShowFirstOfWeek = true;
	    					badgeString += "Primo in classifica green leaves della settimana, ";
	    					leaderBoardBadges += 1;
	    					$scope.userLeaderboardPresent = true;
	    					break;
	    				case $scope.BG_SPECIAL_SECOND :
	    					$scope.userShowSecondOfWeek = true;
	    					badgeString += "Secondo in classifica green leaves della settimana, ";
	    					leaderBoardBadges += 1;
	    					$scope.userLeaderboardPresent = true;
	    					break;
	    				case $scope.BG_SPECIAL_THIRD :
	    					$scope.userShowThirdOfWeek = true;
	    					badgeString += "Terzo in classifica green leaves della settimana, ";
	    					leaderBoardBadges += 1;
	    					$scope.userLeaderboardPresent = true;
	    					break;
	    				default: break;
	    			}
	    		}
    		}
    	}
    	if(badgeString.length == 18){
    		badgeString = "";
    	} else {
    		badgeString = "badge guadagnati: ";
    	}
    	var badgeStringFB = "";
    	var badgeStringTW = "";
    	if(badgeString != ""){
    		var totalValue = Number(greenBadges) + Number(bikeTripBadges) + Number(bikeShareBadges) + Number(zeroImpactBadges) + Number(publicTransportBadges) + Number(parkRideBadges) + Number(recommendationBadges) + Number(leaderBoardBadges);
    		badgeStringFB = badgeString + greenBadges + " badges green, " + bikeTripBadges + " badges bike trip, " + bikeShareBadges + " badges bike share, " + zeroImpactBadges + " badges zero impact, " + publicTransportBadges + " badges public transport, " + parkRideBadges + " badges park&ride, " + recommendationBadges + " badges recommendation, " + leaderBoardBadges + " badges leader boards;";
    		badgeStringTW = badgeString + totalValue + " badges totali ";
    	}
    	var allBadges = [];
    	allBadges.push(badgeStringFB);
    	allBadges.push(badgeStringTW);
    	
    	return allBadges;
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
    
    $scope.callback = function(response){
        console.log(response);
    };
    
    $scope.image_url = "https://tn.smartcommunitylab.it/gamificationweb/img/foglia.png";//"https://www.iconexperience.com/_img/o_collection_png/green_dark_grey/256x256/plain/leaf.png";
    $scope.url = "https://tn.smartcommunitylab.it/gamificationweb";
    $scope.title = "Rovereto Play&Go";
    $scope.caption = "Play&Go";
    $scope.getTextFb_ch = function(ch){
    	return "Vinta la challenge: '" + ch.desc + "'";
    };
    $scope.getTextTw_ch = function(ch){
    	return "Vinta la challenge: '" + ch.desc + "' #playandgochallenges";
    };
    $scope.getPosFb_class = function(position, type){
    	if(position){
	    	if(type == 0){
	    		return "Play&Go: " + position.score[0].score + " punti, posizione in classifica generale: " + position.class_pos_g + "^o posto";
	    	} else {
	    		return "Play&Go: " + position.score[3].score + " punti, posizione in classifica settimanale: " + position.class_pos_aw + "^o posto";
	    	}
    	} else {
    		return "";
    	}
    };
    $scope.getPosTw_class = function(position, type){
    	if(position){
	    	if(type == 0){
	    		return "#Play&Go: " + position.score[0].score + " punti, posizione in classifica genelare: " + position.class_pos_g + "^o posto";
	    	} else {
	    		return "#Play&Go: " + position.score[3].score + " punti, posizione in classifica settimanale: " + position.class_pos_aw + "^o posto";
	    	}
    	} else {
    		return "";
    	}
    };

}]);
cp.controller('nicknameDialogCtrl',function($scope,$modalInstance,data){
	//-- Variables --//
	$scope.submitNumber = 0;
	$scope.accepted = false;
	$scope.mailPattern=/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	
	$scope.ages = [
	    {val: 1, label: '< 20 anni'},
	    {val: 2, label: '20 - 40 anni'},
	    {val: 3, label: '40 - 70 anni'},
	    {val: 4, label: '> 70 anni'}
	];
	
	$scope.user = {
		mail : data.mail,
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
				if($scope.user.invitation != null && $scope.user.invitation != ""){
					if($scope.checkIfNickAlreadyPresent($scope.user.invitation)){
						$modalInstance.close($scope.user);	// pass all the form data to the main controller
					} else {
						$scope.showInvitationMessages = true;
						$scope.errorInvitationMessages = "Nickname non trovato. Contatta chi ti ha invitato al gioco per avere il nickname corretto."
					}
				} else {
					$modalInstance.close($scope.user);	// pass all the form data to the main controller
				}
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
		for(var i = 0; i < data.nickList.length; i++){
			//if(data.nickList[i].nikName.toLowerCase() == nick.trim().toLowerCase()){	// case insensitive
			if(data.nickList[i].nikName == nick.trim()){								// case sensitive
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
}); // end controller(customDialogCtrl)
cp.controller('mailDialogCtrl',function($scope,$modalInstance,data){
	//-- Variables --//
	$scope.submitNumber = 0;
	$scope.accepted = false;
	$scope.mailPattern=/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	
	$scope.user = {
		mail : null,
	};

	//-- Methods --//
	
	$scope.cancel = function(){
		$modalInstance.dismiss('Canceled');
	}; // end cancel
	
	$scope.save = function(form){
		if(form.$valid){
			$scope.errorMessages = "";
			// check if nick already present
			$modalInstance.close($scope.user);	// pass all the form data to the main controller
		}
	}; // end save
	
	$scope.hitEnter = function(evt){
		if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.user.mail,null) || angular.equals($scope.user.mail,'')))
			$scope.save();
	};
}); // end controller(mailDialogCtrl)
cp.controller('surveyDialogCtrl',function($scope,$modalInstance,data){
	//-- Variables --//
	$scope.submitNumber = 0;
	$scope.accepted = false;
	$scope.mailPattern=/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	
	$scope.showSelect = true;
	
	var now = new Date();
	$scope.negative_exp = "negative";
	$scope.satisfying_exp = "satisfying";
	$scope.good_exp = "good";
	$scope.excellent_exp = "excellent";
	
	$scope.nothing_val = "nothing";
	$scope.little_val = "a little";
	$scope.enough_val = "enough";
	$scope.much_val = "much";
	
	$scope.no_val = "no";
	$scope.maybe_val = "maybe";
	$scope.yes_val = "yes";
	
	$scope.walk_mode = "walk";
	$scope.bike_mode = "bike";
	$scope.public_transport_mode = "public transport";
	$scope.car_mode = "car";
	
	$scope.all_trips = "all trips";
	$scope.commuters_trips = "commuters trips";
	$scope.long_trips = "long trips";
	$scope.short_trips = "short trips";
	
	$scope.no_mode = "nothing";
	$scope.bike_sharing_mode = "bike sharing";
	$scope.park_and_ride_mode = "park and ride";
	$scope.bike_mode = "bike";
	$scope.transport_mode = "public transport";

	$scope.user = {
		surveyData : {
			gamimg_experience: "",
			change_of_habits: "",
			new_habits_maintaining: "",
			job_transport_mode: "",
			free_time_transport_mode: "",
			trip_type: "",
			new_mode_type: "",
			point_interest_in_game: "",
			badges_interest_in_game: "",
			challenges_interest_in_game: "",
			prize_interest_in_game: "",
			game_improve_suggestion: "",
			app_improve_suggestion: "",
			timestamp: now.getTime()
		}
	};

	//-- Methods --//
	
	$scope.cancel = function(){
		$modalInstance.dismiss('Canceled');
	}; // end cancel
	
	$scope.save = function(form){
		if(form.$valid){
			$scope.errorMessages = "";
			// check if nick already present
			$modalInstance.close($scope.user);	// pass all the form data to the main controller
		}
	}; // end save
	
	$scope.hitEnter = function(evt){
		if(angular.equals(evt.keyCode,13) && !(angular.equals($scope.user.mail,null) || angular.equals($scope.user.mail,'')))
			$scope.save();
	};
}); // end controller(mailDialogCtrl)