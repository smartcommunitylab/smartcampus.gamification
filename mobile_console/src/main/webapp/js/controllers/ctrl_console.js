'use strict';

/* Controllers */
var cpControllers = angular.module('cpControllers');

cp.controller('ConsoleCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 'localize', '$locale', '$dialogs', 'sharedDataService', '$filter','invokeWSServiceProxy','invokeWSNiksServiceProxy','$timeout', '$base64',
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
	$scope.CHECK_IN = "checkin";
	$scope.CHECK_IN_NU = "checkin_new_user_Trento_Fiera";
	

    
    // new elements for view
    $scope.currentView;
    $scope.editMode;
    $scope.currentViewDetails;
    $scope.maxPlayers = 20
    
    
    $scope.retrieveUsedLang = function(){
    	return sharedDataService.getUsedLanguage();
    }
    
                  		    
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
    // -------------------------------------------------------------------------------------
    
    
    $scope.events = [
        {
        	"id":"01",
        	"name":"Fa' la cosa giusta",
        	"value":"Fai la cosa giusta"
        }
    ];
    
    $scope.game_event = {"id":"01","name":"Fa' la cosa giusta","value":"Fai la cosa giusta"};
    $scope.showPFilter = false;
    
    // Methods to show/hide player name filter
    $scope.showPlayerNameFilter = function(){
    	$scope.showPFilter = true;
    };
    
    $scope.hidePlayerNameFilter = function(){
    	$scope.showPFilter = false;
    };
    
    // ---------------------------------- WS Section Start ---------------------------------
    
    $scope.gamificationUsers = [];
    $scope.currentPos = 0;
    
    $scope.setLoading = function(value){
    	$scope.isLoading = value;
    };
    
    $scope.initConsoleData = function(){
    	$scope.getNiks();
    };
    
    $scope.eventAlreadyChecked = function(playerData){
    	var alreadyChecked = "";
    	if($scope.game_event){
	    	if(playerData && playerData.eventsCheckIn){
	    		for(var i = 0; i < playerData.eventsCheckIn.length; i++){
	    			var ev = playerData.eventsCheckIn[i];
	    			if(ev.name == $scope.game_event.value){
	    				if(ev.type == $scope.CHECK_IN){
	    					alreadyChecked = $scope.CHECK_IN;
	    				} else {
	    					alreadyChecked = $scope.CHECK_IN_NU;
	    				}
	    			}
	    		}
	    		/*if(playerData.eventsCheckIn.indexOf( $scope.game_event.value) !== -1){
	    			alreadyChecked = true
	    		}*/
	    	}
    	}
    	return alreadyChecked;
    }
    
    
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
    
    
    // Method used to retrieve all users niks
    $scope.getNiks = function() {
    	var now = new Date();
    	$scope.setLoading(true);
    	var method = 'GET';
    	var params = null;
    	var wsRestUrl = "niks/";
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		if(result != null){
    			sharedDataService.setPlayerList(result);
    			$scope.gamificationUsers = $scope.correctUsersForList(result);
    		}
    		$scope.setLoading(false);
    	});
    	return myDataPromise;
    };
    
    $scope.correctUsersForList = function(list){
    	if(list){
    		for(var i = 0; i < list.length; i++){
    			if(list[i].nikName){
    				list[i].nikName = list[i].nikName.trim();	// to remove space that can create problems in player ordering
    			}
    		}
    	}
    	return list;
    };
    
    
    
    // Method used to invoke user checkIn method to an event
    $scope.userCheckIn = function(userData) {
    	var method = 'POST';
    	var params = {
    		playerId: userData.pid,
    		event: ($scope.game_event) ? $scope.game_event.value : null
    	};
    	var wsRestUrl = "/console/sendUserCheckIn/" + $scope.CHECK_IN;
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		if(result != null){
    			console.log("Send checkin to server" + JSON.stringify(result));
    		}
    		$scope.getNiks();
    	});
    	return myDataPromise;
    };
    
    // Method used to invoke new user checkIn method
    $scope.newUserCheckIn = function(userData) {
    	var method = 'POST';
    	var params = {
    		playerId: userData.pid,
    		event: ($scope.game_event) ? $scope.game_event.value : null
    	};
    	var wsRestUrl = "/console/sendUserCheckIn/" + $scope.CHECK_IN_NU;
    	var myDataPromise = invokeWSNiksServiceProxy.getProxy(method, wsRestUrl, params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		if(result != null){
    			console.log("Send new user checkin to server" + JSON.stringify(result));
    		}
    		$scope.getNiks();
    	});
    	return myDataPromise;
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
		if($scope.gamificationUsers == null || $scope.gamificationUsers.length == 0){
			return 1;
		}
		return Math.ceil($scope.gamificationUsers.length/$scope.maxPlayers);
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