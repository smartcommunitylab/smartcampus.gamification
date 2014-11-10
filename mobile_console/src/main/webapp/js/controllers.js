'use strict';

/* Controllers */
var cpControllers = angular.module('cpControllers', []);

cp.controller('LoginCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 'localize', 'sharedDataService','invokeWSService','invokeWSServiceProxy',
                          function($scope, $http, $route, $routeParams, $rootScope, localize, sharedDataService, invokeWSService, invokeWSServiceProxy, $location, $filter) {
	
	// for language icons
    var itaLanguage = "active";
    var engLanguage = "";
	
	// for localization
    $scope.setEnglishLanguage = function(){
    	itaLanguage = "";
    	engLanguage = "active";
    	localize.setLanguage('en-US');
    	sharedDataService.setUsedLanguage('eng');
    };
    
    $scope.setItalianLanguage = function(){
    	itaLanguage = "active";
    	engLanguage = "";
    	localize.setLanguage('it-IT');
    	sharedDataService.setUsedLanguage('ita');
    };
    
    $scope.isActiveItaLang = function(){
        return itaLanguage;
    };
                  			
    $scope.isActiveEngLang = function(){
    	return engLanguage;
    };
    
    $scope.getOldLogin = function(){
    	window.document.location = "./login";
    };
	
    $scope.getLogin = function(){
    	window.document.location = "./adc_login";
    };
    
}]);

cp.controller('MainCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 'localize', 'sharedDataService','invokeWSService','invokeWSServiceProxy',
    function($scope, $http, $route, $routeParams, $rootScope, localize, sharedDataService, invokeWSService, invokeWSServiceProxy, $location, $filter) { // , $location 

    //$rootScope.frameOpened = false;
    
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
    
    //$scope.isPracticeFrameOpened = function(){
    //	return sharedDataService.isOpenPracticeFrame();
    //};

    $scope.$route = $route;
    $scope.$location = $location;
    $scope.$routeParams = $routeParams;
    //this.params = $routeParams;
    
    $scope.userCF = sharedDataService.getUserIdentity(); 
    
    $scope.app ;
                  			
    $scope.citizenId = userId;
    $scope.user_token = token;
                  			
    // new elements for view
    $scope.currentView;
    $scope.editMode;
    $scope.currentViewDetails;
                  			
    // max practices displayed in home list
    $scope.maxPractices = 10;
    $scope.practicesWSM = [];

    // for language icons
    var itaLanguage = "active";
    var engLanguage = "";
    
	// for localization
    $scope.setEnglishLanguage = function(){
    	itaLanguage = "";
    	engLanguage = "active";
    	localize.setLanguage('en-US');
    	sharedDataService.setUsedLanguage('eng');
    };
    
    $scope.setItalianLanguage = function(){
    	itaLanguage = "active";
    	engLanguage = "";
    	localize.setLanguage('it-IT');
    	sharedDataService.setUsedLanguage('ita');
    };
    
    $scope.isActiveItaLang = function(){
        return itaLanguage;
    };
                  			
    $scope.isActiveEngLang = function(){
    	return engLanguage;
    };
    
    // for services selection
    var homeShowed = true;
    var activeLinkEdil = "";
    var activeLinkAss = "";
    var activeLinkEdilExtra = "";
    var activeLinkAssExtra = "";
        
    $scope.hideHome = function(){
    	homeShowed = false;   		
    };
    
    $scope.showHome = function(){
    	homeShowed = true;
    };
                  			
    $scope.showPractices = function(type,isEu){
        homeShowed = false;
        sharedDataService.setUeCitizen(isEu);
        if(type == 1){
        	if(sharedDataService.getUeCitizen()){
        		activeLinkEdil="active";
            	activeLinkAss="";
            	activeLinkEdilExtra = "";
                activeLinkAssExtra = "";
        	} else {
        		activeLinkEdil="";
            	activeLinkAss="";
        		activeLinkEdilExtra = "active";
        		activeLinkAssExtra="";
        	}
            sharedDataService.setFamilyAllowances(false);
        } else {
        	if(sharedDataService.getUeCitizen()){
        		activeLinkEdil="";
            	activeLinkAss="active";
        		activeLinkEdilExtra="";
                activeLinkAssExtra="";
                
        	} else {
        		activeLinkEdil="";
            	activeLinkAss="";
        		activeLinkEdilExtra = "";
        		activeLinkAssExtra="active";
        	}
            
            sharedDataService.setFamilyAllowances(true);
        }
        $scope.setFrameOpened(false);
    };
                  			
    $scope.consolidedPractices = function(item){
        if(item.statoDomanda == 'IDONEA'){
            return true;
        } else {
            return false;
        }
    };
                  			
    $scope.isActiveLinkEdil = function(){
        return activeLinkEdil;
    };
                  			
    $scope.isActiveLinkAss = function(){
    	return activeLinkAss;
    };
    
    $scope.isActiveLinkEdilExtra = function(){
    	return activeLinkEdilExtra;
    };
    
    $scope.isActiveLinkAssExtra = function(){
    	return activeLinkAssExtra;
    };
                  			
    $scope.isHomeShowed = function(){
    	return homeShowed;
    };
                  			
    $scope.logout = function() {
        window.document.location = "./logout";
    };
                  		    
    $scope.home = function() {
    	$scope.setFrameOpened(false);
        window.document.location = "./";
        $scope.showHome();
    };
                  		    
    $scope.getToken = function() {
        return 'Bearer ' + $scope.user_token;
    };
                  		    
    $scope.authHeaders = {
         'Authorization': $scope.getToken(),
         'Accept': 'application/json;charset=UTF-8'
    };
                  		    
    // ------------------- User section ------------------
    //$scope.retrieveUserData = function() {
    	//$scope.getUser();				// retrieve user data
    	//$scope.getUserUeNationality();	// retrieve the user ue/extraue Nationality
    //};
    
//    $scope.user;
//    $scope.getUser = function() {
//    	console.log("user id " + $scope.citizenId );
//    	$http({
//        	method : 'GET',
//        	url : 'rest/citizen/user/' + $scope.citizenId,
//        	params : {},
//            headers : $scope.authHeaders
//        }).success(function(data) {
//        	$scope.user = data;
//        }).error(function(data) {
//        });
//    };
    
    // For user shared data
    sharedDataService.setName(user_name);
    sharedDataService.setSurname(user_surname);
    sharedDataService.setBase64(base64);
    //sharedDataService.setBase64('MIIE6TCCA9GgAwIBAgIDBzMlMA0GCSqGSIb3DQEBBQUAMIGBMQswCQYDVQQGEwJJVDEYMBYGA1UECgwPUG9zdGVjb20gUy5wLkEuMSIwIAYDVQQLDBlTZXJ2aXppIGRpIENlcnRpZmljYXppb25lMTQwMgYDVQQDDCtQcm92aW5jaWEgQXV0b25vbWEgZGkgVHJlbnRvIC0gQ0EgQ2l0dGFkaW5pMB4XDTExMTEyMzAwMjQ0MloXDTE3MTEyMjAwNTk1OVowgY4xCzAJBgNVBAYTAklUMQ8wDQYDVQQKDAZUUy1DTlMxJTAjBgNVBAsMHFByb3ZpbmNpYSBBdXRvbm9tYSBkaSBUcmVudG8xRzBFBgNVBAMMPkJSVE1UVDg1TDAxTDM3OFMvNjA0MjExMDE5NzU3MTAwNy53aTRldjVNeCtFeWJtWnJkTllhMVA3ZUtkY1U9MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCsF81BDJjAQat9Lfo/1weA0eePTsEbwTe/0QqlArfOTG3hfLEiSd+mDNsBUJo+cRXZMp677y9a1kYlB+IDY3LGH36Bs1QxM14KA6WB67KX4ZaXENew6Qm7NnkMRboKQiIOUmw1l4OiTETfqKWyFqfAtnyLHd8ZZ6qfjgSsJoSHoQIDAQABo4IB3TCCAdkwge0GA1UdIASB5TCB4jCBrAYFK0wQAgEwgaIwgZ8GCCsGAQUFBwICMIGSDIGPSWRlbnRpZmllcyBYLjUwOSBhdXRoZW50aWNhdGlvbiBjZXJ0aWZpY2F0ZXMgaXNzdWVkIGZvciB0aGUgaXRhbGlhbiBOYXRpb25hbCBTZXJ2aWNlIENhcmQgKENOUykgcHJvamVjdCBpbiBhY2NvcmRpbmcgdG8gdGhlIGl0YWxpYW4gcmVndWxhdGlvbiAwMQYGK0wLAQMBMCcwJQYIKwYBBQUHAgEWGWh0dHA6Ly9wb3N0ZWNlcnQucG9zdGUuaXQwOgYIKwYBBQUHAQEELjAsMCoGCCsGAQUFBzABhh5odHRwOi8vcG9zdGVjZXJ0LnBvc3RlLml0L29jc3AwDgYDVR0PAQH/BAQDAgeAMBMGA1UdJQQMMAoGCCsGAQUFBwMCMB8GA1UdIwQYMBaAFO5h8R6jQnz/4EeFe3FeW6ksaogHMEYGA1UdHwQ/MD0wO6A5oDeGNWh0dHA6Ly9wb3N0ZWNlcnQucG9zdGUuaXQvY25zL3Byb3ZpbmNpYXRyZW50by9jcmwuY3JsMB0GA1UdDgQWBBRF3Z13QZAmn85HIYPyIg3QE8WM2DANBgkqhkiG9w0BAQUFAAOCAQEAErn/asyA6AhJAwOBmxu90umMNF9ti9SX5X+3+pcqLbvKOgCNfjhGJZ02ruuTMO9uIi0DIDvR/9z8Usyf1aDktYvyrMeDZER+TyjviA3ntYpFWWIh1DiRnAxuGYf6Pt6HNehodf1lhR7TP+iejH24kS2LkqUyiP4J/45sTK6JNMXPVT3dk/BAGE1cFCO9FI3QyckstPp64SEba2+LTunEEA4CKPbTQe7iG4FKpuU6rqxLQlSXiPVWZkFK57bAUpVL/CLc7unlFzIccjG/MMvjWcym9L3LaU//46AV2hR8pUfZevh440wAP/WYtomffkITrMNYuD1nWxL7rUTUMkvykw==');
    //sharedDataService.setMail(user_mail);
    sharedDataService.setUtente(nome, cognome, sesso, dataNascita, provinciaNascita, luogoNascita, codiceFiscale, cellulare, email, indirizzoRes, capRes, cittaRes, provinciaRes );
    
    // NB qui andrebbe fatta una funzione che verifica se esiste o meno la mail e in caso ne chiede l'inserimento
    
    
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
    
    
    
    
//    $scope.isUeCitizen = function(){
//    	return sharedDataService.getUeCitizen();
//    };
    
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
    
    // ----------------- Practice sections ----------------
    
    $scope.setLoadingPractice = function(value){
    	$scope.isLoadingPractice = value;
    };
    
 // Method that read the list of the practices from the ws of infoTn
    $scope.getPracticesWS = function() {
    	$scope.setLoadingPractice(true);
    	var method = 'GET';
    	var params = {
			idEnte:"24",
			userIdentity: $scope.userCF
		};
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "RicercaPratiche", params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		$scope.practicesWS = result.domanda;
    		//console.log("Pratiche recuperate da ws: " + $scope.practicesWS);
    		$scope.getPracticesMyWeb();
    		$scope.setLoadingPractice(false);
    	});
    };
    
    // Method that read the list of the practices from the local mongo DB
    $scope.getPracticesMyWeb = function() {
    	$scope.setLoadingPractice(true);
    	var method = 'GET';
    	var params = {
			userIdentity: $scope.userCF
		};
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "GetPraticheMyWeb", params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		$scope.practicesMy = result;
    		//console.log("Pratiche recuperate da myweb: " + $scope.practicesMy);
    		$scope.mergePracticesData($scope.practicesWS, $scope.practicesMy);
    		$scope.setLoadingPractice(false);
    	});
    };
    
    // Method that add the correct status value to every practice in list
    // It merge the value from two lists: practices from ws and practices from local mongo
    $scope.mergePracticesData = function(practiceListWs, practiceListMy){
    	for(var i = 0; i < practiceListWs.length; i++){
    		for(var j = 0; j < practiceListMy.length; j++){
    			if(practiceListWs[i].idObj == practiceListMy[j].idDomanda){
    				practiceListWs[i].myStatus = practiceListMy[j].status;
    				if(practiceListMy[j].status == 'ACCETTATA'){
    					$scope.practicesWSM.push(practiceListWs[i]);
    				}
    				break;
    			}
    		}
    	}
    	// I consider only the practices that has been accepted
    	//$scope.practicesWSM = practiceListWs;
    };
    
                  			
    // for next and prev in practice list
    $scope.currentPage = 0;
//    $scope.numberOfPages = function(){
//    	var consolidedPractices = [];
//    	for(var i=0; i < $scope.practices.length; i++){
//    		if($scope.practices[i].state < 4){
//    			consolidedPractices.push($scope.practices[i]);
//    		}
//    	}
//		return Math.ceil(consolidedPractices.length/$scope.maxPractices);
//	};
	
	$scope.numberOfPages = function(){
		if($scope.practicesWS == null){
			return 0;
		}
//		var consolidedPractices = [];
//    	for(var i=0; i < $scope.practicesWSM.length; i++){
//    		if($scope.practicesWSM[i].myStatus == 'ACCETTATA'){
//    			consolidedPractices.push($scope.practicesWSM[i]);
//    		}
//    	}
		return Math.ceil($scope.practicesWSM.length/$scope.maxPractices);
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

cp.controller('PracticeCtrl', ['$scope', '$http', '$routeParams', '$rootScope', '$route', '$location', '$dialogs', 'sharedDataService', '$filter', 'invokeWSService', 'invokeWSServiceProxy','$base64',
                       function($scope, $http, $routeParams, $rootScope, $route, $location, $dialogs, sharedDataService, $filter, invokeWSService, invokeWSServiceProxy, $base64, $timeout) { 
	this.$scope = $scope;
    $scope.params = $routeParams;
    
    //$rootScope.frameOpened = $location.path().endsWith('/Practice/new/add');
    //$rootScope.frameOpened = $location.path().match("^/Practice/new/add");
    
    // ------------------ Start datetimepicker section -----------------------
    $scope.today = function() {
        $scope.dt = new Date();
      };
      $scope.today();

      $scope.clear = function () {
        //$scope.dt = null;
      };

      // Disable weekend selection
      $scope.disabled = function(date, mode) {
        return ( mode === 'day' && ( date.getDay() === 0 || date.getDay() === 6 ) );
      };

      $scope.toggleMin = function() {
        $scope.minDate = $scope.minDate ? null : new Date();
      };
      $scope.toggleMin();

//      $scope.open = function($event) {
//        $event.preventDefault();
//        $event.stopPropagation();
//
//        $scope.opened = true;
//      };

      $scope.dateOptions = {
        formatYear: 'yyyy',
        startingDay: 1
      };

      $scope.initDate = new Date();
      $scope.formats = ['dd/MM/yyyy','dd-MMMM-yyyy', 'yyyy/MM/dd', 'dd.MM.yyyy', 'shortDate'];
      $scope.format = $scope.formats[0];
      
      //---------------- End datetimepicker section------------
    
    $scope.info_panel_ass = true;
    
    $scope.hideInfoAss = function(){
    	$scope.info_panel_ass = false;
    };
    
    $scope.showInfoAss = function(){
    	$scope.info_panel_ass = true;
    };
    
    $scope.info_panel_loc = true;
    
    $scope.hideInfoLoc = function(){
    	$scope.info_panel_loc = false;
    };
    
    $scope.showInfoLoc = function(){
    	$scope.info_panel_loc = true;
    };
    
    $scope.setFrameOpened = function(value){
    	$rootScope.frameOpened = value;
    };
    
    $scope.isUeCitizen = function(){
    	return sharedDataService.getUeCitizen();
    };
 
    $scope.userCF = sharedDataService.getUserIdentity();
    
    $scope.getNameSurname = function(name, surname){
    	return name + ' ' + surname;
    };
    
    $scope.extracomunitariType = {};
    $scope.residenzaType = {};
    $scope.componenteTmpEdit = {};
    
    $scope.getFamilyAllowaces = function(){
    	var tmp = sharedDataService.isFamilyAllowances();
    	//console.log("Get Family Allowances: " + tmp);
    	return tmp;
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
    
    // The tab directive will use this data
    $scope.tabs = [ 
        { title:'Creazione', index: 1, content:"partials/practice/create_form.html" },
        { title:'Dettaglio', index: 2, content:"partials/practice/details_form.html", disabled:true },
        { title:'Nucleo - Richiedente', index: 3, content:"partials/practice/family_form_ric.html", disabled:true },
        { title:'Nucleo - Componenti', index: 4, content:"partials/practice/family_form_comp.html", disabled:true },
        { title:'Nucleo - Dettagli', index: 5, content:"partials/practice/family_form_det.html", disabled:true },
        { title:'Nucleo - Assegnazione', index: 6, content:"partials/practice/family_form_ass.html", disabled:true },
        { title:'Verifica Domanda', index: 7, content:"partials/practice/practice_state.html", disabled:true },
        { title:'Paga', index: 8, content:"partials/practice/practice_sale.html", disabled:true },
        { title:'Sottometti', index: 9, content:"partials/practice/practice_cons.html", disabled:true }
    ];
    
    // For test all the tabs are active
//    $scope.tabs = [ 
//        { title:'Creazione', index: 1, content:"partials/practice/create_form.html" },
//        { title:'Dettaglio', index: 2, content:"partials/practice/details_form.html" },
//        { title:'Nucleo - Richiedente', index: 3, content:"partials/practice/family_form_ric.html" },
//        { title:'Nucleo - Componenti', index: 4, content:"partials/practice/family_form_comp.html" },
//        { title:'Nucleo - Dettagli', index: 5, content:"partials/practice/family_form_det.html" },
//        { title:'Nucleo - Assegnazione', index: 6, content:"partials/practice/family_form_ass.html" },
//        { title:'Verifica Domanda', index: 7, content:"partials/practice/practice_state.html" },
//        { title:'Paga', index: 8, content:"partials/practice/practice_sale.html" },
//        { title:'Sottometti', index: 9, content:"partials/practice/practice_cons.html" }
//    ];
    
    //$scope.tabIndex = 0;
    $scope.checkMail = function(){
    	if((sharedDataService.getMail() == null) || (sharedDataService.getMail() == '')){
    		return false;
    	} else {
    		return true;
    	}
    };
    
    $scope.tmp_user = {};
    
    $scope.setCreationTabs = function(){
    	$scope.getElenchi();
    	$scope.setFrameOpened(true);
    };
    
    $scope.setNextButtonLabel = function(value){
    	$scope.buttonNextLabel = value;
    };
    
    var fInit = true;
    $scope.initForm = function(){
    	$scope.setNextButtonLabel("Avanti");
    	return fInit;
    };
    
    $scope.setDefaultTabs = function(){
    	$scope.setFrameOpened(false);
    };
    
    // Method nextTab to switch the input forms to the next tab and to call the correct functions
    $scope.nextTab = function(value, type, param1, param2, param3, param4){
    	//var creationRes = true;
    	fInit = false;
    	if(!value){		// check form invalid
    		switch(type){
    			case 1:	// CreaPratica
    				if(!$scope.checkStoricoStruct()){
    					$dialogs.error("Nessuna struttura di recupero inserita! E' obbigatorio specificarne i dati per il componente che ne e' stato ospite.");
    					break;
    				}
    				$scope.setLoading(true);
    				$scope.createPractice(param1, param2, param3, param4); //Test
    				break;
    			case 2:
    				$scope.setLoading(true);
    				if(param2 == true){
    					$scope.updateAlloggioOccupato(param3, param1);
    				} else {
    					$scope.updateAmbitoTerritoriale();
    					//$scope.updateResidenza(param3);
    				}
    				$scope.getComponenteRichiedente();
    				
    				$scope.setCFRichiedente(false);	// to disable the button "next"
    				$scope.continueNextTab();
    				break;
    			case 3:
    				//$scope.updateNucleoFamiliare(param1);
    				$scope.setCompEdited(false);
    				$scope.continueNextTab();
    				break;
    			case 4:
    				$scope.initFamilyTabs();
    				$scope.continueNextTab();
    				break;
    			case 5:
    				if($scope.checkComponentsData() == true){
    					// Controllo mail richiedente == mail cps
    					$scope.checkMergingMail(param1);
    					$scope.continueNextTab();
    				} else {
    					$dialogs.error($scope.getCheckDateContinuosError());
    				}
    				break;
    			case 6:
    				$scope.stampaScheda($scope.practice.idObj);
    				$scope.continueNextTab();
    				break;
    			case 7:
    				$scope.continueNextTab();
    				break;	
    			case 8:
    				$scope.setLoading(true);
    				$scope.payPratica(1);
    				//$scope.continueNextTab();
    				break;
    			case 9:
    				$scope.protocolla();
    				break;
    			case 10:
    				$scope.rifiuta();
    				break;	
    			default:
    				break;
    		}
    		
    		fInit = true;
    	}
    };
    
    $scope.continueNextTab = function(){
    	// After the end of all operations the tab is swithced
		if($scope.tabIndex !== ($scope.tabs.length -1)){
				$scope.tabs[$scope.tabIndex].active = false;	// deactive actual tab
				$scope.tabIndex++;								// increment tab index
				$scope.tabs[$scope.tabIndex].active = true;		// active new tab
				$scope.tabs[$scope.tabIndex].disabled = false;
		} else {
			$scope.setNextButtonLabel("Termina");
		}
    };
    
    $scope.prevTab = function(){
    	if($scope.tabIndex !== 0 ){
    		$scope.getPracticeData(sharedDataService.getIdDomanda(),3);
    		$scope.setNextButtonLabel("Avanti");
    	    $scope.tabs[$scope.tabIndex].active = false;	// deactive actual tab
    	    $scope.tabIndex--;								// increment tab index
    	    $scope.tabs[$scope.tabIndex].active = true;		// active new tab	
    	}
    };
    
    $scope.setIndex = function($index){
    	$scope.tabIndex = $index;
    };
    
    // -------------------------For edit tabs -----------------------

    var tabEditIndex = 0;
    
    $scope.setEditTabs = function(practiceIdToEdit){
    	$scope.getElenchi();
    	$scope.setFrameOpened(true);
    };
    
    $scope.editTabs = [ 
        { title:'Dettaglio', index: 1, content:"partials/edit/details_form.html" },
        { title:'Nucleo - Richiedente', index: 2, content:"partials/edit/family_form_ric.html", disabled:true },
        { title:'Nucleo - Componenti', index: 3, content:"partials/edit/family_form_comp.html", disabled:true },
        { title:'Nucleo - Dettagli', index: 4, content:"partials/edit/family_form_det.html", disabled:true },
        { title:'Nucleo - Assegnazione', index: 5, content:"partials/edit/family_form_ass.html", disabled:true },
        { title:'Verifica Domanda', index: 6, content:"partials/edit/practice_state.html", disabled:true },
        { title:'Paga', index: 7, content:"partials/edit/practice_sale.html", disabled:true },
        { title:'Sottometti', index: 8, content:"partials/edit/practice_cons.html", disabled:true }
    ];
    
    // Method nextEditTab to switch the input forms to the next tab and to call the correct functions
    $scope.nextEditTab = function(value, type, param1, param2, param3, param4){
    	fInit = false;
    	if(!value){		// check form invalid
    		switch(type){
    			case 2:
    				if(!$scope.checkStoricoStruct()){
    					$dialogs.error("Nessuna struttura di recupero inserita! E' obbigatorio specificarne i dati per il componente specifico.");
    					break;
    				}
    				$scope.setLoading(true);
    				if(param2 == true){
    					$scope.updateAlloggioOccupato(param3, param1);
    				} else {
    					$scope.updateAmbitoTerritoriale();
    				}
    				$scope.getComponenteRichiedente();
    				$scope.continueNextEditTab();
    				$scope.setCFRichiedente(false);	// to disable the button "next"
    				break;
    			case 3:
    				//$scope.updateNucleoFamiliare(param1);
    				$scope.setCompEdited(false);
    				$scope.continueNextEditTab();
    				break;
    			case 4:
    				$scope.initFamilyTabs();
    				$scope.continueNextEditTab();
    				break;
    			case 5:
    				if($scope.checkComponentsData() == true){
    					// Controllo mail richiedente == mail cps
    					$scope.checkMergingMail(param1);
    					$scope.continueNextEditTab();
    				} else {
    					$dialogs.error($scope.getCheckDateContinuosError());
    				}
    				break;	
    			case 6:
    				$scope.stampaScheda($scope.practice.idObj);
    				$scope.continueNextEditTab();
    				break;
    			case 7:
    				$scope.continueNextEditTab();
    				break;	
    			case 8:
    				$scope.setLoading(true);
    				$scope.payPratica(2);
    				//$scope.continueNextEditTab();
    				break;
    			case 9:
    				$scope.protocolla();
    				break;
    			case 10:
    				$scope.rifiuta();
    				break;	
    			default:
    				break;
    		}	
    		fInit = true;
    	}
    };
    
    $scope.continueNextEditTab = function(){
    	if(tabEditIndex !== ($scope.editTabs.length -1)){
	    	$scope.editTabs[tabEditIndex].active = false;		// deactive actual tab
	    	tabEditIndex = tabEditIndex+1;						// increment tab index
	    	$scope.editTabs[tabEditIndex].active = true;		// active new tab
	    	$scope.editTabs[tabEditIndex].disabled = false;	
		} else {
			$scope.setNextButtonLabel("Termina");
		}
    };
    
    $scope.prevEditTab = function(){
    	if(tabEditIndex !== 0 ){
    		$scope.getPracticeData(sharedDataService.getIdDomanda(),3);
    		$scope.setNextButtonLabel("Avanti");
    	    $scope.editTabs[tabEditIndex].active = false;	// deactive actual tab
    	    tabEditIndex--;								// increment tab index
    	    $scope.editTabs[tabEditIndex].active = true;		// active new tab	
    	}
    };
    
    $scope.setEditIndex = function($index){
    	//$scope.tabEditIndex = $index;
    	tabEditIndex = $index;
    };
    
    // ----------------------- For view tabs -----------------------
    $scope.setNextButtonViewLabel = function(value){
    	$rootScope.buttonNextViewLabel = value;
    };

    $scope.setViewTabs = function(){
    	$scope.setViewIndex(0);
    	$scope.setNextButtonViewLabel("Chiudi");
    	$scope.setFrameOpened(true);
    };
    
    $scope.viewTabs = [ 
        { title:'Dettagli Domanda', index: 1, content:"partials/view/practice_state.html" }
    ];
    
    // Method nextTab to switch the input forms to the next tab and to call the correct functions
    $scope.nextViewTab = function(value, type, param1, param2, param3, param4){
    	fInit = false;
    	if(!value){		// check form invalid
    		switch(type){
    			case 1: $scope.setFrameOpened(false);
    				break;
    			default:
    				break;
    		}
    		// After the end of all operations the tab is swithced
    		if($scope.tabViewIndex !== ($scope.viewTabs.length -1) ){
    	    	$scope.viewTabs[$scope.tabViewIndex].active = false;	// deactive actual tab
    	    	$scope.tabViewIndex++;								// increment tab index
    	    	$scope.viewTabs[$scope.tabViewIndex].active = true;		// active new tab
    	    	$scope.viewTabs[$scope.tabViewIndex].disabled = false;	
    		} else {
    			$scope.setNextButtonViewLabel("Chiudi");
    		}
    		fInit = true;
    	}
    };
    
    $scope.prevViewTab = function(){
    	if($scope.tabViewIndex !== 0 ){
    		$scope.setNextButtonViewLabel("Avanti");
    	    $scope.viewTabs[$scope.tabViewIndex].active = false;	// deactive actual tab
    	    $scope.tabViewIndex--;								// increment tab index
    	    $scope.viewTabs[$scope.tabviewIndex].active = true;		// active new tab	
    	}
    };
    
    $scope.setViewIndex = function($index){
    	$scope.tabViewIndex = $index;
    };
    
    // -------------------For manage components tabs-----------------
    
    $scope.setComponentsEdited = function(value){
    	$scope.allComponentsEdited = value;
    };
    
    $scope.hideArrow = function(value){
    	$scope.isArrowHide = value;
    };
    
    $scope.setFInitFam = function(value){
    	$scope.fInitFam=value;
    };
    
    var fInitFam = true;
    $scope.initFamForm = function(){
    	//console.log("fInitFam value: " + fInitFam);
    	return fInitFam;
    };
    
    $scope.initFamilyTabs = function(){
    	fInitFam = false;
    	$scope.setNextLabel("Prossimo Componente");
    	$scope.family_tabs = [];
    	for(var i = 0; i < $scope.componenti.length; i++){
    		$scope.family_tabs.push({
    			title : (i + 1) + " - " + $scope.componenti[i].persona.nome + " " + $scope.componenti[i].persona.cognome,
    			index : i + 1,
    			disabled : (i == 0 ? false : true),
    			content : $scope.componenti[i],
    			disability : {
    				catDis : $scope.componenti[i].variazioniComponente.categoriaInvalidita,
    				gradoDis : $scope.componenti[i].variazioniComponente.gradoInvalidita,
    				cieco : false,
    				sordoMuto : false
    			}
    		});	
    	}
    	if($scope.family_tabs.length == 1){
    		$scope.setNextLabel("Salva Componente");
			$scope.hideArrow(true);
    	}
    	$scope.setComponentsEdited(false);
    };
    
    $scope.setNextLabel = function(value){
    	$scope.buttonNextLabelFamily = value;
    };
    
    $scope.setIndexFamily = function($index){
    	$scope.tabFamilyIndex = $index;
    };

    $scope.checkInvalidFields = function(comp_index){
    	var check = true;
    	var anni_res = $scope.family_tabs[comp_index].content.variazioniComponente.anniResidenza;
    	if(anni_res != null && anni_res != ''){
    		if($scope.storicoResidenza.length == 0){
    			check = false;
	    		$scope.showNoStoricoMessage = true;
	    	} else {
	    		$scope.showNoStoricoMessage = false;
	    	}
    	}
    	var richiedente = $scope.family_tabs[comp_index].content.richiedente;
    	var phone = $scope.family_tabs[comp_index].content.variazioniComponente.telefono;
    	var mail = $scope.tmp_user.mail;
    	var municipality = $scope.family_tabs[comp_index].content.variazioniComponente.idComuneResidenza;
    	var residence = $scope.family_tabs[comp_index].content.variazioniComponente.indirizzoResidenza;
    	var civic = $scope.family_tabs[comp_index].content.variazioniComponente.numeroCivico;
    	var nationality = $scope.family_tabs[comp_index].content.variazioniComponente.decsrCittadinanza;
    	if(richiedente == true){
	    	if((phone == null) || (phone == "") || (phone == "0461/")){
	    		check = false;
	    		$scope.showPhoneMessage = true;
	    	} else {
	    		$scope.showPhoneMessage = false;
	    	}
	    	if((mail == null) || (mail == "")){
	    		check = false;
	    		$scope.showMailMessage = true;
	    	} else {
	    		$scope.showMailMessage = false;
	    	}
	    	if(municipality == null || municipality == ''){
	    		check = false;
	    		$scope.showMunicipalityMessage = true;
	    	} else {
	    		$scope.showMunicipalityMessage = false;
	    	}
	    	if(residence == null || residence == ''){
	    		check = false;
	    		$scope.showResidenceMessage = true;
	    	} else {
	    		$scope.showResidenceMessage = false;
	    	}
	    	if(civic == null || civic == ''){
	    		check = false;
	    		$scope.showCivicMessage = true;
	    	} else {
	    		$scope.showCivicMessage = false;
	    	}
    	}
    	if(nationality == null || nationality == ''){
    		check = false;
    		$scope.showNationalityMessage = true;
    	} else {
    		$scope.showNationalityMessage = false;
    	}
    	
    	
    	return check;
    };
    
    $scope.checkMergingMail = function(value){
    	if(value != null && value != ''){
	    	if(value != sharedDataService.getMail()){
	    		sharedDataService.setMail(value);
	    	}
    	}
    };
    
    $scope.checkStoricoStruct = function(){
    	var check = true;
    	var components = $scope.residenzaType.numeroComponenti;
    	if((components != null) && (components != '') && (components > 0)){
    		if($scope.struttureRec.length == 0){
    			check = false;
    		}
    	}
    	return check;
    };
    
    $scope.checkScadenzaPermesso = function(value){
    	var tmp_date = $scope.correctDate(value);
    	var date = $scope.castToDate(tmp_date);
    	var now = new Date();
    	if(date.getTime() > now.getTime()){
    		$scope.showUserId = false;
    	} else {
    		$scope.showUserId = true;
    	}
    };
    
    $scope.checkPhonePattern = function(value){
    	var check = true;
    	if(!($scope.phonePattern.test(value))){
    		$scope.showPhonePatternMessage = true;
    	} else {
    		$scope.showPhonePatternMessage = false;
    	}
    	return check;
    };
    
    $scope.checkMailPattern = function(value){
    	var check = true;
    	if(!($scope.mailPattern.test(value))){
    		$scope.showMailPatternMessage = true;
    	} else {
    		$scope.showMailPatternMessage = false;
    	}
    	return check;
    };
    
    $scope.nextFamilyTab = function(value, componenteVar, disability, invalidAge){
    	fInitFam = false;
    	if($scope.checkInvalidFields($scope.tabFamilyIndex)){
	    	if(!value){		// check form invalid
	    		if(invalidAge == 'noDis'){
	    			disability = null;
	    		}
	    		console.log("Invalid Age: " + invalidAge);
	    		$scope.salvaComponente(componenteVar, disability);
		    	// After the end of all operations the tab is swithced
		    	if($scope.tabFamilyIndex !== ($scope.componenti.length -1) ){
		    		if($scope.tabFamilyIndex == ($scope.componenti.length -2)) {
		    			$scope.setNextLabel("Salva");
		    			$scope.hideArrow(true);
		    		}
		    	   	$scope.family_tabs[$scope.tabFamilyIndex].active = false;	// deactive actual tab
		    	   	$scope.tabFamilyIndex++;									// increment tab index
		    	   	$scope.family_tabs[$scope.tabFamilyIndex].active = true;		// active new tab
		    	   	$scope.family_tabs[$scope.tabFamilyIndex].disabled = false;	
		    	} else {
		    		$scope.setComponentsEdited(true);
		    	}
		    	fInitFam = true;
	    	}
    	}
    };
    
    $scope.prevFamilyTab = function(){
    	if($scope.tabFamilyIndex !== 0 ){
    		$scope.setNextLabel("Prossimo Componente");
    		$scope.hideArrow(false);
    	    $scope.family_tabs[$scope.tabFamilyIndex].active = false;	// deactive actual tab
    	    $scope.tabFamilyIndex--;									// increment tab index
    	    $scope.family_tabs[$scope.tabFamilyIndex].active = true;		// active new tab	
    	}
    };
    // --------------------------------------------------------------
    
    $scope.temp = {};
    
    $scope.reset = function(){
    	$scope.practice = angular.copy($scope.temp);
    };
    
    $scope.jobs = sharedDataService.getJobs();
    
    $scope.permissions = sharedDataService.getPermissions();    
    
    $scope.rtypes = sharedDataService.getRtypes();
    
    $scope.rtypes_inidoneo = sharedDataService.getRtypesInidoneo();
    
    $scope.rtypes_all = sharedDataService.getRtypesAll();
    
    $scope.genders = sharedDataService.getGenders();
    
    $scope.municipalities = sharedDataService.getMunicipalities();
    
    $scope.contracts = sharedDataService.getContracts();
    
    $scope.disabilities_under18 = sharedDataService.getDisabilities_under18();
    
    $scope.disabilities_over65 = sharedDataService.getDisabilities_over65();
    
    $scope.disabilities_all = sharedDataService.getDisabilities_all();
    
    $scope.citizenships = sharedDataService.getCitizenships();
    
    $scope.yes_no = sharedDataService.getYesNo();
    
    $scope.affinities = sharedDataService.getAffinities();
    
    $scope.maritals = sharedDataService.getMaritals();
    
    //{value: 'SENT_SEP', name: 'Coniugato/a con sentenza di separazione'}
    
    $scope.onlyNumbers = /^\d+$/;
    $scope.decimalNumbers = /^([0-9]+)[\,]{0,1}[0-9]{0,2}$/;
    $scope.datePatternIt=/^\d{1,2}\/\d{1,2}\/\d{4}$/;
    $scope.datePattern=/^[0-9]{2}\-[0-9]{2}\-[0-9]{4}$/i;
    $scope.datePattern2=/^[0-9]{2}\/[0-9]{2}\/[0-9]{4}$/i;
    $scope.datePattern3=/^[0-9]{4}\/[0-9]{2}\/[0-9]{2}$/i;
    $scope.timePattern=/^(?:(?:([01]?\d|2[0-3]):)?([0-5]?\d):)?([0-5]?\d)$/;
    $scope.phonePattern=/^[(]{0,1}[0-9]{3}[)\.\- ]{0,1}[0-9]{3}[\.\- ]{0,1}[0-9]{4}$/;
    $scope.mailPattern=/^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    
    // ----------------------------- Section for Separation, Anni Residenza, Anzianità lavorativa e Disabilità ----------------------------
    $scope.sep = {};
    $scope.setSep = function(value){
    	$scope.sep = value;
    };
    
    $scope.getSep = function(){
    	return $scope.sep;
    };
    
    $scope.setSeparation = function(value){
    	$scope.isSeparationVisible = value;
    };
    
    $scope.hideSeparation = function(){
    	$scope.setSeparation(false);
    };
    
    $scope.checkSeparationSent = function(value){
    	if(value == 'SENT_SEP'){
    		$scope.setSeparation(true);
    	}
    };
    
    // method that check the correctness of a family state with two spouses ecc... If it
    // found only a consort it return an error
    $scope.checkFamilyState = function(){
    	var check = true;
    	var sc_count = 0;
    	for (var i = 0; i < $scope.componenti.length; i++){
    		if($scope.componenti[i].statoCivile == 'CONIUGATO_A'){
    			sc_count++;
    		}
    	}
    	if(sc_count == 1){
    		var sep = $scope.getSep();
    		if((sep == null) || ((sep.consensual == null) && (sep.judicial == null) && (sep.tmp == null))){
    			$scope.setSeparation(true);
    			check = false;
    		} else {
    			check = true;
    		}
    	}
    	return check;
    };
    
    $scope.isAllFamilyState = function(){
    	var check = true;
    	for (var i = 0; i < $scope.componenti.length; i++){
    		if($scope.componenti[i].statoCivile == null ||  $scope.componenti[i].statoCivile == ''){
    			check = false;
    			break;
    		}
    	}
    	return check;
    };
    
    $scope.salvaSeparazione = function(){
    	//$scope.setSep(value);
    	if(($scope.sep == null) || (($scope.sep.consensual == null) && ($scope.sep.judicial == null) && ($scope.sep.tmp == null))){
    		$dialogs.error("Stato civile dichiarato non idoneo. Richiedi un altro ICEF per poter effettuare una domanda idonea.");
    	} else {
    		console.log("Stato separazione : " + $scope.sep);
    		$scope.hideSeparation();
    	}
    };
    
    $scope.resetSep = function(){
    	$scope.setSep({});
    	//$scope.sep = {};
    };
    
    $scope.storicoResidenza = [];
    $scope.componenteMaxResidenza = "";
    $scope.componenteMaxResidenza_Obj = {};
    $scope.componenteAIRE = "";
    $scope.residenzaAnni = 0;
    $scope.aireAnni = 0;

    $scope.sr = {};
    
    $scope.setErrorsStoricoRes = function(value){
    	$scope.isErrorStoricoRes = value;
    };
    
    $scope.showSRForm = function(value){
    	if($scope.componenteMaxResidenza_Obj != {}){
    		if(($scope.storicoResidenza.length != 0) && (value.idObj != $scope.componenteMaxResidenza_Obj.idObj)){
    			$dialogs.notify("Attenzione", "Anni Residenza gia' inseriti per un altro componente. Inserendo nuovi dati i vecchi dati verrano cancellati");
    		}
    	}
    	//$scope.sr.dataDa = new Date(value);
    	$scope.setSRFormVisible(true);
    };
    
    $scope.hideSRForm = function(){
    	$scope.setSRFormVisible(false);
    };
    
    $scope.setSRFormVisible = function(value){
    	$scope.isSRFormVisible = value;
    };
    
    $scope.addStoricoRes = function(value, person){
    	// Method that check if the inserted date are corrects
    	if($scope.checkDates(null, value.idComuneResidenza, value.dataDa, value.dataA, 1, null, person)){
    		$scope.setErrorsStoricoRes(false);
    		var dateDa = $scope.correctDate(value.dataDa);
    		var dateA = $scope.correctDate(value.dataA);
    		var fromDate = $scope.castToDate(dateDa);
    		var toDate = $scope.castToDate(dateA);
//    		console.log("Data da " + fromDate);
//    		console.log("Data a " + toDate);
    		value.id = $scope.storicoResidenza.length;
    		value.difference = toDate.getTime() - fromDate.getTime();
    		var newStorico = angular.copy(value);
    		$scope.storicoResidenza.push(newStorico);
    		value.dataDa = value.dataA; // Update the new date with the end of the last date
    		value.idComuneResidenza = "";
    		value.isAire = "";
    		value.dataA = "";
    	} else {
    		$scope.setErrorsStoricoRes(true);
    	}
    };
    
    $scope.checkDates = function(nome, comune, data1, data2, type, comp, person){
    	var check_ok = true;
    	if(type == 1){
	    	if((comune == null || comune == '') && (data1 == null || data1 == '') && (data2 == null || data2 == '')){
	    		$scope.setErrorMessageStoricoRes("Nessun valore inserito nei campi 'Comune', 'Data Dal' e 'Data Al'. I campi sono obbligatori");
	    		check_ok = false;
	    	} else {
		    	if(comune == null || comune == ''){
		    		$scope.setErrorMessageStoricoRes("Campo Comune obbligatorio");
		    		check_ok = false;
		    	} else  if(data1 == null || data1 == '' || data2 == null || data2 == ''){
		    		$scope.setErrorMessageStoricoRes("Campi Data Da/A obbligatori");
		    		check_ok = false;
		    	} else {
		    		var tmpDataDa = $scope.correctDate(data1);
		    		var tmpDataA = $scope.correctDate(data2);
		    		var dataDa = $scope.castToDate(tmpDataDa);
		        	var dataA = $scope.castToDate(tmpDataA);
		    		if(dataDa > dataA){
		    			$scope.setErrorMessageStoricoRes("Data di inizio maggiore di data di fine");
		    			check_ok = false;
		    		}
		    		if(dataDa.getTime() < person.persona.dataNascita){
		    			$scope.setErrorMessageStoricoRes("Data di inizio inferiore alla data di nascita del componente");
		    			check_ok = false;
		    		}
		    		if(dataA.getTime() > $scope.practice.dataPresentazione){
		    			$scope.setErrorMessageStoricoRes("Data di fine superiore alla data di presentazione della domanda");
		    			check_ok = false;
		    		}
		    	}
	    	}
    	} else {
    		if((nome == null || nome == '') && (comune == null || comune == '') && (data1 == null || data1 == '') && (data2 == null || data2 == '')){
	    		$scope.setErrorMessageStoricoStruct("Nessun valore inserito nei campi 'Nome Struttura', 'Luogo', 'Data Dal' e 'Data Al'. I campi sono obbligatori", comp);
	    		check_ok = false;
	    	} else {
	    		if(nome == null || nome == ''){
		    		$scope.setErrorMessageStoricoStruct("Campo 'Nome Struttura' obbligatorio", comp);
		    		check_ok = false;
		    	} else if(comune == null || comune == ''){
		    		$scope.setErrorMessageStoricoStruct("Campo 'Luogo' obbligatorio", comp);
		    		check_ok = false;
		    	} else  if(data1 == null || data1 == '' || data2 == null || data2 == ''){
		    		$scope.setErrorMessageStoricoStruct("Campi Data Da/A obbligatori", comp);
		    		check_ok = false;
		    	} else {
		    		//var dataDa = new Date(data1);
		        	//var dataA = new Date(data2);
		        	var tmpDataDa = $scope.correctDate(data1);
		    		var tmpDataA = $scope.correctDate(data2);
		    		var dataDa = $scope.castToDate(tmpDataDa);
		        	var dataA = $scope.castToDate(tmpDataA);
		    		if(dataDa > dataA){
		    			$scope.setErrorMessageStoricoStruct("Data di inizio maggiore di data di fine", comp);
		    			check_ok = false;
		    		} else {
		    		var now = new Date();
		    		var two_years = 1000 * 60 * 60 * 24 * 360 * 2;
		    			if(dataA.getTime() < (now.getTime() - two_years)){
		    				$scope.setErrorMessageStoricoStruct("Il periodo inserito e' precedente agli ultimi due anni", comp);
			    			check_ok = false;
		    			}
		    		}
		    	}
	    	}
    	}
    	return check_ok;
    };
    
    $scope.deleteStoricoRes = function(value){
    	$scope.storicoResidenza.splice(value.id, 1);
    };
    
    $scope.calcolaStoricoRes = function(ft_component){
    	$scope.showNoStoricoMessage = false;			 // I use this variable in the editing of a component: when I add a storicoResidenza I have to set to False
    	var totMillis = 0;
    	var totMillisInYear = 1000 * 60 * 60 * 24 * 360; // I consider an year of 360 days (12 month of 30 days)
    	for(var i = 0; i < $scope.storicoResidenza.length; i++){
    		totMillis += $scope.storicoResidenza[i].difference;
    	}
    	var anniRes = totMillis/totMillisInYear;
    	$scope.setAnni(Math.floor(anniRes), ft_component, 1);
    	$scope.setSRFormVisible(false);
    };
    
    $scope.setErrorMessageStoricoRes = function(value){
    	$scope.errorsMessageStoricoRes = value;
    };
    
    // Method setAnni: used with param type == 1 -> to update "anniResidenza";
    // 				   used with param type == 2 -> to update "anniLavoro";	
    //				   used with param type == 3 -> to update "anniAIRE";
    $scope.setAnni = function(value, ft_component, type){
    	// find the righ componente in $scope.componenti
    	for(var i = 0; i < $scope.componenti.length; i++){
    		if($scope.componenti[i].idObj == ft_component.idObj){
    			if(type == 1){
    				$scope.componenti[i].variazioniComponente.anniResidenza = value;
    				$scope.componenteMaxResidenza = $scope.componenti[i].persona.cognome  + ", " + $scope.componenti[i].persona.nome;
    				$scope.componenteMaxResidenza_Obj = angular.copy($scope.componenti[i]);
    				$scope.residenzaAnni = value;
    			} else if(type == 2){
    				$scope.componenti[i].variazioniComponente.anniLavoro = value;
    			} else {
    				$scope.componenti[i].variazioniComponente.anniAire = value;
    				$scope.componenteAIRE = $scope.componenti[i].persona.cognome  + ", " + $scope.componenti[i].persona.nome;
    				$scope.aireAnni = value;
    			}
    		}
    	}
    };
    
    $scope.showALForm = function(){
    	$scope.setALFormVisible(true);
    };
    
    $scope.hideALForm = function(){
    	$scope.setALFormVisible(false);
    };
    
    $scope.setALFormVisible = function(value){
    	$scope.isALFormVisible = value;
    };
    
    $scope.calcolaAnzianitaLav = function(value, ft_component){
    	if(value.mesiLavoro > 6){
    		value.anniLavoro +=1;
    	} else if((value.mesiLavoro == 6) && (value.giorniLavoro > 0)){
    		value.anniLavoro +=1;
    	}
    	$scope.setAnni(value.anniLavoro, ft_component, 2);
    	$scope.setALFormVisible(false);
    };
    
    $scope.checkMonths = function(months){
    	if(months == 6){
    		$scope.setDaysVisible(true);
    	} else {
    		$scope.setDaysVisible(false);
    	}
    };
    
    $scope.setDaysVisible = function(value){
    	$scope.isDaysVisible = value;
    };

    $scope.showDisForm = function(componente){
    	if(componente.disability.catDis == null && componente.disability.gradoDis == null){
    		$scope.invalid_age = 'noDis';
    	}
    	var today = new Date();
    	var dNascita = new Date(componente.content.persona.dataNascita);
    	
    	var totMillisInYear = 1000 * 60 * 60 * 24 * 365; // I consider an year of 365 days
    	var difference = today.getTime() - dNascita.getTime();
    	$scope.anniComp = Math.round(difference/totMillisInYear);
    	
    	$scope.setDisFormVisible(true);
    };
    
    $scope.hideDisForm = function(){
    	$scope.setDisFormVisible(false);
    };
    
    $scope.setDisFormVisible = function(value){
    	$scope.isDisFormVisible = value;
    };
    
    $scope.calcolaCategoriaGradoDisabilita = function(){
    	$scope.hideDisForm();
    };
    
    $scope.resetDisabilita = function(component){
    	$scope.invalid_age = 'noDis';
    };
    
    $scope.checkComponentsData = function(){
    	var control = false;
    	if($scope.componenteMaxResidenza_Obj == {} || $scope.storicoResidenza.length == 0){
    		$scope.setCheckDateContinuosError("Attenzione: Non e' stata inserita la durata della residenza per nessun componente. Specificare il valore per il componente con il periodo di residenza maggiore");
    	} else {
	    	for(var i = 0; i < $scope.componenti.length; i++){
	    		if($scope.componenti[i].idObj == $scope.componenteMaxResidenza_Obj.idObj){
	    			if($scope.componenti[i].variazioniComponente.anniResidenza >= 3){
		    			// Here I have to check the continuity of the date from now to last tree years
		    			var end_period = new Date($scope.practice.dataPresentazione);	
	    				var totMillisInThreeYear = 1000 * 60 * 60 * 24 * 360 * 3; // I consider an year of 360 days
		    			var startMillis = end_period.getTime() - totMillisInThreeYear;
		    			var start_period = new Date(startMillis);
		    			
		    			if($scope.checkAnniContinui(start_period, end_period, $scope.storicoResidenza)){
		    				control = true;
		    			}	
	    			} else {
	    				$scope.setCheckDateContinuosError("Attenzione: non possiedi i requisiti per continuare con la creazione di una nuova domanda: durata della residenza in Trentino minore di 3 anni.");
	    			}
	    			break;
	    		}	
	    	}
    	}
    	
    	return control;
    };
    
    $scope.setCheckDateContinuosError = function(value){
    	$scope.errorMessageCheckDate = value;
    };
    
    $scope.getCheckDateContinuosError = function(){
    	return $scope.errorMessageCheckDate;
    };
    
 // ------------------------------------  Recovery Structure Data ------------------------------------
    
    $scope.checkRecoveryStruct = function(){
    	if($scope.residenzaType.numeroComponenti > 0 && $scope.residenzaType.numeroComponenti < 3){
    		$scope.setRecoveryStruct(true);
    	} else {
    		$scope.hideRecoveryStruct();
    	}
    };
    
    $scope.strutturaRec = {};
    $scope.strutturaRec2 = {};
    $scope.struttureRec = [];
    $scope.setStrutturaRec = function(value){
    	$scope.setStrutturaRec = value;
    };
    
    $scope.setErrorMessageStoricoStruct = function(value, comp){
    	if(comp == 1){
    		$scope.errorMessagesStoricoStruct = value;
    	} else {
    		$scope.errorMessagesStoricoStruct2 = value;
    	}
    };
    
    $scope.setRecoveryStruct = function(value){
    	$scope.isRecoveryStructVisible = value;
    };
    
    $scope.hideRecoveryStruct = function(){
    	$scope.setRecoveryStruct(false);
    };
    
    $scope.resetStrutturaRec = function(){
    	//$scope.setSep(null);
    	$scope.strutturaRec = {};
    };
    
    $scope.setErroreStoricoStruct = function(value, comp){
    	if(comp == 1){
    		$scope.isErroreStoricoStruct = value;
    	} else {
    		$scope.isErroreStoricoStruct2 = value;
    	}
    };
    
    $scope.addStoricoStruct = function(value, comp){
    	// Method that check if the inserted date are corrects
    	if($scope.checkDates(value.structName, value.structPlace, value.dataDa, value.dataA, 2, comp, null)){
	    		$scope.setErroreStoricoStruct(false, comp);
	    		var dateDa = $scope.correctDate(value.dataDa);
	    		var dateA = $scope.correctDate(value.dataA);
	    		var fromDate = $scope.castToDate(dateDa);
	    		var toDate = $scope.castToDate(dateA);
//	    		console.log("Data da " + fromDate);
//	    		console.log("Data a " + toDate);
	    		value.id = $scope.struttureRec.length;
	    		// devo fare la differenza dalla data di fine a quella di presentazione domanda ($scope.practice.dataPresentazione) - now
	    		value.distance = toDate.getTime() - fromDate.getTime();
	    		var newStrutturaRec = angular.copy(value);
	    		$scope.struttureRec.push(newStrutturaRec);
	    		value.dataDa = value.dataA; // Update the new date with the end of the last date
	    		value.structName = "";
	    		value.structPlace = "";
	    		value.dataA = "";
    	} else {
    		$scope.setErroreStoricoStruct(true, comp);
    	}
    };
    
    $scope.deleteStoricoStruct = function(value){
    	$scope.struttureRec.splice(value.id, 1);
    };
    
    $scope.controllaStoricoStruct = function(value, componenti){
    	if(value.length == 0){
    		// errore nessuna struttura inserita
    		$dialogs.error("Non hai inserito nessuna struttura. Devi specificare i dati della/delle strutture di recupero.");
    	} else {
    		// controllo sui 6 mesi spezzati negli ultimi 2 anni per i vari componenti
    		var now = new Date();
    		var two_years = 1000 * 60 * 60 * 24 * 360 * 2;
    		var from_date = new Date(now.getTime() - two_years);
    		
    		var check_message = $scope.checkMesiSpezzati(from_date, now, value, componenti);
    		if(check_message != ""){
    			$dialogs.error(check_message);
    		} else {
    			$scope.hideRecoveryStruct();
    		}	
    	}
    };
    
    $scope.checkICEF = function(practiceLoad){
    	var checkRes = false;
    	if(practiceLoad != null){
    		var icef = parseFloat(practiceLoad.nucleo.indicatoreEconomico.icefaccesso);
    		if(icef < 0.23){
    			checkRes = true;
    		}
    	}
    	return checkRes;
    };
    
    // --------------------------------------------------------------------------------------------------
    
    $scope.checkAnniContinui = function(data_da, data_a, periodi){
    	var continuous_years = false;
    	var new_end = data_a;
    	for(var i = periodi.length-1; i >= 0; i--){
    		var tmp_end = $scope.correctDate(periodi[i].dataA);
    		var tmp_start = $scope.correctDate(periodi[i].dataDa);
    		var end = $scope.castToDate(tmp_end);
        	var start = $scope.castToDate(tmp_start);
        	var distance_end = new_end.getTime() - end.getTime();
        	var distance_start = data_da.getTime() - start.getTime();
        	var oneDay = 1000 * 60 * 60 * 24 * 2; // millisenconds of a day
    	
        	if(distance_end < oneDay){
        		if(distance_start > 0){
        			continuous_years = true;
        			break;
        		} else {
        			if(distance_start > (oneDay * -1)){
        				continuous_years = true;
            			break;
        			}
        		}
        	} else {
        		// there is an empty period: exit with false
        		$scope.setCheckDateContinuosError("Attenzione: non possiedi i requisiti per continuare con la creazione di una nuova domanda: esistono periodi di non residenza in Trentino negli ultimi 3 anni.");
        		break;
        	}
        	new_end = start;	// I have to update the period end
    	}
    	if(continuous_years == false){
    		$scope.setCheckDateContinuosError("Attenzione: non possiedi i requisiti per continuare con la creazione di una nuova domanda: esistono periodi di non residenza in Trentino negli ultimi 3 anni.");
    	}
    	return continuous_years;
    };
    
    $scope.checkMesiSpezzati = function(data_da, data_a, periodi, componenti){
    	var errorMessages = "Il totale dei mesi effettuati in una struttura di recupero e' inferiore a 6 quindi non sufficiente.";
    	var totTimesC1 = 0;
    	var totTimesC2 = 0;
    	var nameComp = [];
    	if(componenti == 1){
	    	for(var i = 0; i < periodi.length; i++){
	    		var tmp_end = $scope.correctDate(periodi[i].dataA);
	    		var tmp_start = $scope.correctDate(periodi[i].dataDa);
	    		var end = $scope.castToDate(tmp_end);
	        	var start = $scope.castToDate(tmp_start);
	        	if(start.getTime() > data_da.getTime()){
	        		totTimesC1 = totTimesC1 + periodi[i].distance;
	        	} else {
	        		if(end.getTime() > data_da.getTime()){
	        			var tmp_diff = end.getTime() - data_da.getTime();
	        			totTimesC1 = totTimesC1 + tmp_diff;
	        		}
	        	}		
	    	}
    	} else {
    		// case of 2 components
    		for(var i = 0; i < periodi.length; i++){
    			if(i == 0){
    				nameComp[0] = periodi[i].componenteName;
    			} else {
    				if(periodi[i].componenteName != nameComp[0]){
    					nameComp[1] = periodi[i].componenteName;
    					break;
    				}
    			}
    		}
    		
    		for(var i = 0; i < periodi.length; i++){
    			var tmp_end = $scope.correctDate(periodi[i].dataA);
	    		var tmp_start = $scope.correctDate(periodi[i].dataDa);
	    		var end = $scope.castToDate(tmp_end);
	        	var start = $scope.castToDate(tmp_start);
	        	if(start.getTime() > data_da.getTime()){
	        		if(periodi[i].componenteName == nameComp[0]){
	        			totTimesC1 = totTimesC1 + periodi[i].distance;
	        		} else {
	        			totTimesC2 = totTimesC2 + periodi[i].distance;
	        		}
	        	} else {
	        		if(end.getTime() > data_da.getTime()){
	        			var tmp_diff = end.getTime() - data_da.getTime();
	        			if(periodi[i].componenteName == nameComp[0]){
	        				totTimesC1 = totTimesC1 + tmp_diff;
	        			} else {
	        				totTimesC2 = totTimesC2 + tmp_diff;
	        			}
	        		}
	        	}		
	    	}
    	}
    	var month = 1000 * 60 * 60 * 24 * 30;
    	if(componenti == 1){
    		if(Math.floor(totTimesC1/month) >= 6 ){
    			errorMessages = "";
    		}
    	} else {
    		if((Math.floor(totTimesC1/month) >= 6) && (Math.floor(totTimesC2/month) >= 6) ){
    			errorMessages = "";
    		} else {
    			if((Math.floor(totTimesC1/month) < 6) && (Math.floor(totTimesC2/month) < 6)){
    				errorMessages = errorMessages + " I componenti '" + nameComp[0] + "' e '" + nameComp[1] + "' non possiedono i requisiti richiesti.";
    			} else if((Math.floor(totTimesC1/month) < 6)){
    				errorMessages = errorMessages + " Il componente '" + nameComp[0] + "' non possiede i requisiti richiesti.";
    			}  else {
    				errorMessages = errorMessages + " Il componente '" + nameComp[1] + "' non possiede i requisiti richiesti.";
    			}
    		}
    	}
    	return errorMessages;
    };

    // --------------------------- End Section for Anni Residenza, Anzianità lavorativa e Disabilità -------------------------
    
    // Object and Method to check the correct relation between the rooms and the family components
    $scope.infoAlloggio = {};
    $scope.checkInidoneo = function(){
    	var suggestRooms = 0;
    	var correctRooms = false;
    	// Algoritm:
    	// Componenti - Stanze da letto
    	//    1 2 	  - 1
		//    3 4 5   - 2
		//    6 7 8   - 3
		//    9       - 4
		//    10      - 5
    	if($scope.infoAlloggio.ocupantiAlloggio < 3){
    		suggestRooms = 1;
    		if($scope.infoAlloggio.stanzeLetto >= suggestRooms){
    			correctRooms = true; 
    		} else {
    			correctRooms = false;
    		}
    	} else if($scope.infoAlloggio.ocupantiAlloggio >= 3 && $scope.infoAlloggio.ocupantiAlloggio < 6){
    		suggestRooms = 2;
    		if($scope.infoAlloggio.stanzeLetto >= suggestRooms){
    			correctRooms = true; 
    		} else {
    			correctRooms = false;
    		}
    	} else if($scope.infoAlloggio.ocupantiAlloggio >= 6 && $scope.infoAlloggio.ocupantiAlloggio < 9){
    		suggestRooms = 3;
    		if($scope.infoAlloggio.stanzeLetto >= suggestRooms){
    			correctRooms = true; 
    		} else {
    			correctRooms = false;
    		}
    	} else if($scope.infoAlloggio.ocupantiAlloggio == 9){
    		suggestRooms = 4;
    		if($scope.infoAlloggio.stanzeLetto >= suggestRooms){
    			correctRooms = true; 
    		} else {
    			correctRooms = false;
    		}
    	} else if($scope.infoAlloggio.ocupantiAlloggio == 10){
    		suggestRooms = 5;
    		if($scope.infoAlloggio.stanzeLetto >= suggestRooms){
    			correctRooms = true; 
    		} else {
    			correctRooms = false;
    		}
    	}
    	$scope.isInidoneoForRoomsNum = !correctRooms;
    };
    
    // Variabili usate in familyForm per visualizzare/nascondere i vari blocchi
    $scope.showMembers = false;
    $scope.applicantInserted = false;
    $scope.newMemberShow = false;
    $scope.newMemberInserted = false;
    $scope.showEditComponents = false;
    
    $scope.checkRequirement = function(){
    	if(($scope.residenzaType.residenzaTN != 'true') || ($scope.residenzaType.alloggioAdeguato == 'true')){
    		$dialogs.notify("Attenzione", "Non sei in possesso dei requisiti minimi per poter effettuare una domanda idonea. Vedi documento specifico sul portale della provincia di Trento");
    		return false;
    	} else {
    		return true;
    	}
    };
    
    // ---------------------------------- Metodi richiamo WS INFOTN ----------------------------------------
    $scope.setPracticeLoaded = function(value){
    	$scope.practiceLoaded = value;
    };
    
    $scope.getPracticesEpu = function() {
    	$scope.setPracticeLoaded(false);
    	var method = 'GET';
    	var params = {
    		idEnte:"24",
    		userIdentity: $scope.userCF
    	};
    	
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "RicercaPratiche", params, $scope.authHeaders, null);	

    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
	    		$scope.practicesEpu = result.domanda;
	    		console.log("Recupero domande utente " + $scope.practicesEpu);
	    		//$dialogs.notify("Successo","Creazione Pratica " + result.domanda.identificativo + " avvenuta con successo.");
    		} else {
	    		$dialogs.error("Errore Recupero pratiche utente loggato.");
    		}
    		$scope.setPracticeLoaded(true);
    	});
    };
    
    $scope.correctDecimal = function(num){
    	var res = '';
    	if(num != null && num != ''){
    		res = num.replace(",",".");
    	}
    	return res;
    };
    
    $scope.correctDate = function(date){
    	if(date != null && date.indexOf("/") > -1){
    		var res = date.split("/");
    		var correct = "";
        	correct=res[2] + "-" + res[1] + "-" + res[0];
        	return correct;
    	} else {
    		return date;
    	}
    };
    
    $scope.correctDateIt = function(date){
    	if(date != null && date.indexOf("/") > -1){
    		return date;
    	} else {
	    	if(date != null){
	    		var res = date.split("-");
	    		var correct = "";
	        	correct=res[2] + "/" + res[1] + "/" + res[0];
	        	return correct;
    		} else {
    			return null;
    		}
    	}
    };
    
    $scope.castToDate = function(stringDate){
    	var res = stringDate.split("-");
    	var month = parseInt(res[1]) - 1; // the month start from 0 to 11;
        return new Date(res[0], month, res[2]);
    };
    
    $scope.createPractice = function(ec_type, res_type, dom_type, practice){
    	var tmp_scadenza = $scope.correctDate(ec_type.scadenzaPermessoSoggiorno);
    	var scad = null;
    	if(tmp_scadenza != null){
    		scad = $scope.castToDate(tmp_scadenza);
    		var now = new Date();
    		if(scad.getTime() < now.getTime()){
    			//$scope.setLoading(false);
    			//$dialogs.notify("Attenzione", "Non sei in possesso di un permesso di soggiorno valido. Non puoi proseguire con la creazione di una nuova domanda");
    			//return false;
    			var oneDay = 1000 * 60 * 60 * 24 * 1;
    			scad = new Date(now.getTime() + oneDay);
    		}
    	}
    	
    	if(sharedDataService.getMail() == null || sharedDataService.getMail() == ''){
    		sharedDataService.setMail($scope.tmp_user.mail);	// I set the mail for the user that do not have the info in the card
    	} else {
    		$scope.tmp_user.mail = sharedDataService.getMail();
    		//sharedDataService.setMail($scope.tmp_user.mail);
    	}
    	
    	if($scope.checkRequirement() == false){
    		$scope.setLoading(false);
    		return false;
    	}
    	var extraComType = {};
    	if(ec_type != null){
	    	extraComType = {
	    			permesso: ec_type.permesso,
	    			lavoro : ec_type.lavoro,
	    			ricevutaPermessoSoggiorno : ec_type.ricevutaPermessoSoggiorno,
	    			scadenzaPermessoSoggiorno : (scad != null)?scad.getTime():scad
	    	};
    	}
    	
    	res_type.cittadinanzaUE = $scope.isUeCitizen();
    	var edizione = $scope.getCorrectEdizioneFinanziataTest($scope.getFamilyAllowaces(), sharedDataService.getUeCitizen());
    	var pratica = {	
    			input:{
    				domandaType : {
    					extracomunitariType: extraComType, //extraComType,//ec_type,
    					idEdizioneFinanziata : edizione,
    					numeroDomandaICEF : dom_type.numeroDomandaIcef,
    					residenzaType : res_type
    				},
    				idEnte : "24",
    				userIdentity : $scope.userCF
    			},
    			cpsData : {
    				email : sharedDataService.getMail(), //(sharedDataService.getMail() == null || sharedDataService.getMail() == '')? $scope.tmp_user.mail : sharedDataService.getMail(),
    				nome : sharedDataService.getName(),
    				cognome : sharedDataService.getSurname(),
    				codiceFiscale : sharedDataService.getUserIdentity(),
    				certBase64 : sharedDataService.getBase64()
    			}
    	};
    	
    	var value = JSON.stringify(pratica);
    	console.log("Json value " + value);
    	
    	var method = 'POST';
    	//var myDataPromise = invokeWSService.getProxy(method, "CreaPratica", null, $scope.authHeaders, value);	
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "CreaPratica", null, $scope.authHeaders, value);	
    	
    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
    			// Here I call the getPracticeMethod
    			sharedDataService.setIdDomanda(result.domanda.idObj);
    			//idDomandaAll = result.domanda.idObj; //5563259; //returned.domanda.idObj;
            	$scope.getPracticeData(result.domanda.idObj,1);
            	// Retrieve the elenchi info
                $scope.getElenchi();
    		} else {
    			$scope.setLoading(false);
    			$dialogs.error("Creazione Pratica non riuscita. Identificativo ICEF non corretto");
    			return false;
    		}
    	});	
    	
    };
    
    // Used to create a Practice without call the web-service
    $scope.createPracticeTest = function(ec_type, res_type, dom_type, practice){
    	
    	//sharedDataService.setLoading(true);
    	$scope.setLoading(true);
    	var pratica = {
    		domandaType : {
    				extracomunitariType: ec_type,
    				idEdizioneFinanziata : 5526558,
    				numeroDomandaICEF : dom_type.numeroDomandaIcef,
    				residenzaType : res_type
    			},
    		idEnte : "24",
    		userIdentity : $scope.userCF
    	};
    	
        // Here I call the getPracticeMethod // old 5562993
    	sharedDataService.setIdDomanda(5563259);
    	//idDomandaAll = 5563259;	// Multi componente 5563259
        $scope.getPracticeData(5563259,1); 
        // Retrieve the elenchi info
        $scope.getElenchi();
    };
	
	$scope.setLoading = function(loading) {
		$scope.isLoading = loading;
	};
	
	$scope.setLoadingRic = function(loading) {
		$scope.isLoadingRic = loading;
	};
	
	$scope.setLoadingPSC = function(loading) {
		$scope.isLoadingPSC = loading;
	};
	
	$scope.setLoadingAss = function(loading) {
		$scope.isLoadingAss = loading;
	};
	
    // Method to obtain the Practice data from the id of the request
    $scope.getPracticeData = function(idDomanda, type) {
    	
    	if(type == 2){
    		sharedDataService.setIdDomanda(idDomanda);
    	}
    		
    	var method = 'GET';
    	var params = {
    		idDomanda:idDomanda,
    		idEnte:"24",
    		userIdentity: $scope.userCF
    	};
    	
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "GetDatiPratica", params, $scope.authHeaders, null);	

    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
	    		$scope.practice = result.domanda;
	    		if(type == 2){
	    			$scope.tmpAmbitoTerritoriale = $scope.practice.ambitoTerritoriale1;
	    			if($scope.tmpAmbitoTerritoriale != null && $scope.tmpAmbitoTerritoriale != ''){
	    				$scope.myAmbito={
	    						idObj: $scope.tmpAmbitoTerritoriale.idObj,
	    						descrizione: $scope.getComuneById($scope.tmpAmbitoTerritoriale.idObj, 3)
	    				};
	    			}
	    			$scope.tmp_user.mail = sharedDataService.getMail();
	    		}
	    		
	    		// split practice data into differents objects
	    		$scope.extracomunitariType = $scope.practice.extracomunitariType;
	    		$scope.residenzaType = $scope.practice.residenzaType;
	    		$scope.checkRecoveryStruct();	// to check the presence of components from recovery structs
	    		$scope.nucleo = $scope.practice.nucleo;
	    		$scope.setComponenti($scope.nucleo.componente);
	    		$scope.indicatoreEco = $scope.nucleo.indicatoreEconomico;
	    		
	    		$scope.setLoading(false);
	    		if(type == 1){
	    			if($scope.checkICEF($scope.practice) == true){
	    				$dialogs.notify("Successo","Creazione Pratica " + result.domanda.identificativo + " avvenuta con successo.");
	    				$scope.continueNextTab();
	    			} else {
	    				$dialogs.error("Non possiedi i requisiti per creare una nuova domanda. Il tuo indicatore ICEF  di accesso risulta essere troppo alto.");
	    			}
	    		} //else {
	    		//	$dialogs.notify("Successo","Caricamento Dati Pratica " + result.domanda.identificativo + " avvenuta con successo.");
	    		//}
	    	} else {
    			$scope.setLoading(false);
    		}
    	});
    	
    };
    
    $scope.setComponenti = function(value){
    	$scope.componenti = value;
    };
    
    var listaEdizioniFinanziate = [];
    
    // Method to full the "elenchi" used in the app
    $scope.getElenchi = function() {
    	
    	var tmp_ambiti = sharedDataService.getStaticAmbiti();
    	var tmp_comuni = sharedDataService.getStaticComuni();
    	
    	var method = 'GET';
    	var params = {
			idEnte:"24",
			userIdentity: $scope.userCF
		};
    	
    	if(tmp_ambiti.length == 0 && tmp_comuni.length == 0){
	    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "Elenchi", params, $scope.authHeaders, null);
	    	myDataPromise.then(function(result){
	    		sharedDataService.setStaticAmbiti(result.ambitiTerritoriali);
	    		sharedDataService.setStaticComuni(result.comuni);
	        	listaEdizioniFinanziate = result.edizioniFinanziate;
	        	// the first time I use the portal the lists are initialized
	        	$scope.listaComuni = result.comuni;
	    		$scope.listaComuniVallaganina = $scope.getOnlyComunity(result.comuni);
	    		$scope.listaAmbiti = result.ambitiTerritoriali;
	    	});
    	} else {
    		$scope.listaComuni = sharedDataService.getStaticComuni();
    		$scope.listaComuniVallaganina = $scope.getOnlyComunity(sharedDataService.getStaticComuni());
    		$scope.listaAmbiti = sharedDataService.getStaticAmbiti();
    	}
    };
    
    $scope.getOnlyComunity = function(list){
    	var correctList = [];
    	var vallagarinaList = sharedDataService.getVallagarinaMunicipality();
    	if(list != null && list.length > 0){
    		for(var i = 0; i < list.length; i++){
    			for(var y = 0; y < vallagarinaList.length; y++){
	    			if(list[i].descrizione == vallagarinaList[y]){
	    				correctList.push(list[i]);
	    				break;
	    			}
    			}
    		}
    	}
    	return correctList;
    };
    
    $scope.getCorrectEdizioneFinanziata = function(isAss, isUE){
    	var found = false;
    	var edFin = "";
    	
    	if(isAss == true && isUE == true){
    		for(var i = 0; (i < listaEdizioniFinanziate.length) && (!found); i++){
    			if(listaEdizioniFinanziate[i].descrizione == "Contributo integrativo su libero mercato, comunitari"){
    				found = true;
    				edFin = listaEdizioniFinanziate[i].idObj;
    			}
    		}
    	}
    	if(isAss == true && isUE == false){
    		for(var i = 0; (i < listaEdizioniFinanziate.length) && (!found); i++){
    			if(listaEdizioniFinanziate[i].descrizione == "Contributo integrativo su libero mercato, extracomunitari"){
    				found = true;
    				edFin = listaEdizioniFinanziate[i].idObj;
    			}
    		}
    	}
    	if(isAss == false && isUE == true){
    		for(var i = 0; (i < listaEdizioniFinanziate.length) && (!found); i++){
    			if(listaEdizioniFinanziate[i].descrizione == "Locazione di alloggio pubblico, comunitari"){
    				found = true;
    				edFin = listaEdizioniFinanziate[i].idObj;
    			}
    		}
    	}
    	if(isAss == false && isUE == false){
    		for(var i = 0; (i < listaEdizioniFinanziate.length) && (!found); i++){
    			if(listaEdizioniFinanziate[i].descrizione == "Locazione di alloggio pubblico, extracomunitari"){
    				found = true;
    				edFin = listaEdizioniFinanziate[i].idObj;
    			}
    		}
    	}
    	
    	return edFin;
    };
    
    $scope.getCorrectEdizioneFinanziataTest = function(isAss, isUE){
    	var edFin = "";
    	// Per VAS-DEV
    	var alloggioUE = '5526551';
    	var alloggioExtraUE = '5526553';
    	var contributoUE = '5526550';
    	var contributoExtraUE = '5526552';
    	// Per Prod
//    	var alloggioUE = '5651335';
//    	var alloggioExtraUE = '5651336';
//    	var contributoUE = '"5651331';
//    	var contributoExtraUE = '5651332';
    	
    	if(isAss == true && isUE == true){
    		edFin = contributoUE;
    	}
    	if(isAss == true && isUE == false){
    		edFin = contributoExtraUE;
    	}
    	if(isAss == false && isUE == true){
    		edFin = alloggioUE;
    	}
    	if(isAss == false && isUE == false){
    		edFin = alloggioExtraUE;
    	}
    	
    	return edFin;
    };
    
    // Used to update the alloggioOccupato data
    $scope.updateAlloggioOccupato = function(residenza,alloggioOccupato){
    	
    	var allog = null;
    	if(alloggioOccupato != null){
    		var importo = $scope.correctDecimal(alloggioOccupato.importoCanone);
    		allog = {
	    		comuneAlloggio : alloggioOccupato.comuneAlloggio,
	    		indirizzoAlloggio : alloggioOccupato.indirizzoAlloggio,
	    		superficieAlloggio : alloggioOccupato.superficieAlloggio,
	    		numeroStanze : alloggioOccupato.numeroStanze,
	    		tipoContratto :	alloggioOccupato.tipoContratto,
	    		dataContratto : $scope.correctDate(alloggioOccupato.dataContratto),
	    		importoCanone : importo
	    	};
    	}
    	var alloggio = {
        	domandaType : {
        		residenzaType : residenza,
        		alloggioOccupatoType : allog,	//alloggioOccupato,
        		idDomanda : $scope.practice.idObj,
        		versione: $scope.practice.versione
        	},
        	idEnte : "24",
        	userIdentity : $scope.userCF
        };
    	
    	var value = JSON.stringify(alloggio);
    	console.log("Alloggio Occupato : " + value);
    	var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
    	
    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
    			$scope.setLoading(false);
    			$dialogs.notify("Successo","Modifica Alloggio Occupato avvenuta con successo.");
    		} else {
    			$scope.setLoading(false);
    			$dialogs.error("Errore Modifica Pratica - Alloggio Occupato");
    		}
    	});
    };
    
    // Method to update the "residenzaType" of an element 
    $scope.updateResidenza = function(residenza){
    	var residenzaCor = {
        	domandaType : {
        		residenzaType : residenza,
        		idDomanda : $scope.practice.idObj,
        		versione: $scope.practice.versione
        	},
        	idEnte : "24",
        	userIdentity : $scope.userCF
        };
    	var value = JSON.stringify(residenzaCor);
    	
    	var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
    	
    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
    			$scope.setLoading(false);
    			$dialogs.notify("Successo","Modifica Residenza avvenuta con successo.");
    		} else {
    			$scope.setLoading(false);
    			$dialogs.error("Errore Modifica Pratica - Residenza");
    		}
    	});
    };
   
    // Method to update the "ambitoTerritoriale" of an element 
    $scope.updateAmbitoTerritoriale = function(){
    	if($scope.practice == null || $scope.practice.ambitoTerritoriale1 == null || $scope.practice.ambitoTerritoriale1 == ""){
    		$dialogs.notify("Attenzione","Non hai effettuato nessuna scelta riguardo al comune o alla circoscrizione: attenzione. La mancata indicazione di una preferenza e' intesa come scelta indifferenziata di tutti i comuni");
    		$scope.setLoading(false);
    	} else if($scope.practice.ambitoTerritoriale1.descrizione != 'dalla combobox'){
	    	var ambitoTerritoriale = {
	    		domandaType : {
	    			ambitoTerritoriale1 : $scope.practice.ambitoTerritoriale1,
	    			idDomanda : $scope.practice.idObj,
	        		versione: $scope.practice.versione
	    		},
	    		idEnte : "24",
	        	userIdentity : $scope.userCF
	    	};
	    	var value = JSON.stringify(ambitoTerritoriale);
	    	//console.log("Ambito territoriale da modificare " + JSON.stringify(value));
	    	
	    	var method = 'POST';
	    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
	    	
	    	myDataPromise.then(function(result){
	    		if(result.esito == 'OK'){
	    			//console.log("Ambito territoriale modificato " + JSON.stringify(result.domanda.ambitoTerritoriale1));
	    			$scope.setLoading(false);
	    			$dialogs.notify("Successo","Settaggio Ambito territoriale avvenuto con successo.");
	    		} else {
	    			$scope.setLoading(false);
	    			$dialogs.error("Errore settaggio Ambito");
	    		}
	    	});
    	} else {
    		$scope.setLoading(false);
			$dialogs.notify("Successo","Settaggio Ambito territoriale avvenuto con successo.");
    	}
    };
    
    // Method to update the "parentelaStatoCivile" data of every family member 
    $scope.salvaModificheSC = function(){
    	if(!$scope.isAllFamilyState()){
    		$scope.showSCFamError = true;
    		$scope.setLoadingPSC(false);
    	} else {
    		$scope.showSCFamError = false;	
	    	$scope.setCompEdited(true);
	    	// check correctness of family state
	    	if($scope.checkFamilyState()){
		    	$scope.setLoadingPSC(true);
		    	var onlyParentelaESC = [];
		    	for (var i = 0; i < $scope.componenti.length; i++){
		    		var p_sc = {
		    			idNucleoFamiliare: 	$scope.componenti[i].idNucleoFamiliare,
		    			idObj: $scope.componenti[i].idObj,
						richiedente: $scope.componenti[i].richiedente,
						parentela: $scope.componenti[i].parentela,
						statoCivile: $scope.componenti[i].statoCivile
						//statoCivile: ($scope.componenti[i].statoCivile == 'SENT_SEP') ? 'GIA_CONIUGATO_A' : $scope.componenti[i].statoCivile
		    		};
		    		onlyParentelaESC.push(p_sc);
		    	}
		    	var nucleo = {
		    	    domandaType : {
		    	    	parentelaStatoCivileModificareType : {
		    	    		componenteModificareType : onlyParentelaESC,
		    	    		idDomanda: $scope.practice.idObj,
			    			idObj: $scope.nucleo.idObj
		    	    	},
		    	    	idDomanda : $scope.practice.idObj,
		    	    	versione: $scope.practice.versione
		    	    },
		    	    idEnte : "24",
		    	    userIdentity : $scope.userCF
		    	};
		    
		    	var value = JSON.stringify(nucleo);
				console.log("Modifica Parentela e SC : " + value);
				
				var method = 'POST';
		    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
		    	
		    	myDataPromise.then(function(result){
		    		if(result.esito == 'OK'){
		    			$dialogs.notify("Successo","Modifica Dati di parentela e stato civile dei Componenti avvenuta con successo.");
		    		} else {
		    			$dialogs.error("Modifica Dati di parentela e stato civile dei Componenti non riuscita.");
		    		}
		    		$scope.setLoadingPSC(false);
		    		
		    	});
	    	}
    	}
    };
    
    // Method to update the "componenteNucleoFamiliare" data
    $scope.updateComponenteVariazioni = function(componenteVariazioni, disability){
    	
    	// for extra disability: blind and/or mute    	
    	if(disability != null){
	    	if(disability.cieco || disability.sordoMuto){
	    		componenteVariazioni.variazioniComponente.gradoInvalidita = 100;
	    	}
	    	
	    	if(disability.catDis != null){
	    		if(disability.catDis == 'CATEGORIA_INVALIDA_1'){
	    			componenteVariazioni.variazioniComponente.gradoInvalidita = 0;
	    		} else {
	    			componenteVariazioni.variazioniComponente.gradoInvalidita = 100;
	    		}
	    	} else {
	    		if(!disability.cieco && !disability.sordoMuto){
	    			componenteVariazioni.variazioniComponente.categoriaInvalidita = null;
	    			componenteVariazioni.variazioniComponente.gradoInvalidita = disability.gradoDis;
	    		}
	    	}
    	} else {
    		componenteVariazioni.variazioniComponente.categoriaInvalidita = null;
    		componenteVariazioni.variazioniComponente.gradoInvalidita = null;
    	}
    	
    	// model for "variazioniComponente"
    	var variazioniComponenteCorr = {
    		anniLavoro: componenteVariazioni.variazioniComponente.anniLavoro,
            anniResidenza: componenteVariazioni.variazioniComponente.anniResidenza,
            anniResidenzaComune: componenteVariazioni.variazioniComponente.anniResidenzaComune,
            categoriaInvalidita: componenteVariazioni.variazioniComponente.categoriaInvalidita,
            donnaLavoratrice: componenteVariazioni.variazioniComponente.donnaLavoratrice,
            flagResidenza: componenteVariazioni.variazioniComponente.flagResidenza,
            frazione: componenteVariazioni.variazioniComponente.frazione,
            fuoriAlloggio: componenteVariazioni.variazioniComponente.fuoriAlloggio,
            gradoInvalidita: componenteVariazioni.variazioniComponente.gradoInvalidita,
            idComponente: componenteVariazioni.variazioniComponente.idComponente,
            idComuneResidenza: componenteVariazioni.variazioniComponente.idComuneResidenza,
            idObj: componenteVariazioni.variazioniComponente.idObj, // idObj (variazioniComponente)
            indirizzoResidenza: componenteVariazioni.variazioniComponente.indirizzoResidenza,
            //note: componenteVariazioni.variazioniComponente.note,
            decsrCittadinanza: componenteVariazioni.variazioniComponente.decsrCittadinanza,
            numeroCivico: componenteVariazioni.variazioniComponente.numeroCivico,
            ospite: componenteVariazioni.variazioniComponente.ospite,
            pensionato: componenteVariazioni.variazioniComponente.pensionato,
            provinciaResidenza: componenteVariazioni.variazioniComponente.provinciaResidenza,
            telefono: componenteVariazioni.variazioniComponente.telefono
    	};
    	
    	// model for nucleo
		var nucleo = {
	    	domandaType : {
	    		nucleoFamiliareComponentiModificareType : {
	    			componenteModificareType : [{
	    				idNucleoFamiliare: $scope.nucleo.idObj,
	    				idObj: componenteVariazioni.idObj,
	    				variazioniComponenteModificare: variazioniComponenteCorr
	    			}],
	    			idDomanda: $scope.practice.idObj,
	    			idObj: $scope.nucleo.idObj
	    		},
	    		idDomanda : $scope.practice.idObj,
	    		versione: $scope.practice.versione
	    	},
	    	idEnte : "24",
	    	userIdentity : $scope.userCF
	    };
		
		var value = JSON.stringify(nucleo);
		console.log("Nucleo Familiare : " + value);
		
		var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
    	
    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
    			$scope.setLoading(false);
    			$dialogs.notify("Successo","Modifica dati Componente avvenuta con successo.");
    		} else {
    			$scope.setLoading(false);
    			$dialogs.error("Modifica Dati Componente non riuscita.");
    		}
    	});
    };
    
    // Method to update the extra info of "nucleo familiare"
    $scope.updateNFVarie = function(nucleoFam){
    	var nucleoCor = {
    		domandaType : {
    			nucleoFamiliareModificareType : {
	    			alloggioSbarrierato: nucleoFam.alloggioSbarrierato,
	    			componentiExtraIcef: nucleoFam.componentiExtraIcef,
	    			numeroStanze: nucleoFam.numeroStanze,
	    			idDomanda: $scope.practice.idObj,
	    			idObj: $scope.nucleo.idObj
	    		},
	    		idDomanda : $scope.practice.idObj,
	    		versione: $scope.practice.versione
	    	},
	    	idEnte : "24",
	    	userIdentity : $scope.userCF
	    };
    	
    	var value = JSON.stringify(nucleoCor);
    	console.log("Nucleo Extra Info : " + value);
    	
    	var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
    	
    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
    			$dialogs.notify("Successo","Modifica Nucleo avvenuta con successo.");
    		} else {
    			$dialogs.error("Modifica Nucleo non riuscita.");
    		}
    		$scope.setLoadingAss(false);
    	});
    	
    };
    
    $scope.setComponenteRichiedente = function(value){
    	$scope.richiedente = value;
    };
    
    // Method to retrieve the practice "richiedente"
    $scope.getComponenteRichiedente = function(){
    	var componentiLength = $scope.componenti.length;
    	var trovato = false;
    	for(var i = 0; i < componentiLength && !trovato; i++){
    		if($scope.componenti[i].richiedente == true){
    			$scope.setComponenteRichiedente($scope.componenti[i]);
    		}
    	}
    };
    
    $scope.edit_parentelaSCiv = false;
    
    $scope.editParentelaSCiv = function(){
    	$scope.edit_parentelaSCiv = true;
    };
    
    $scope.saveParentelaSCiv = function(){
    	$scope.edit_parentelaSCiv = false;
    };
    
    // Method to edit the component variations
    $scope.editComponente = function(componente){
    	$scope.showEditComponents = true;
    	var componentiLength = $scope.componenti.length;
    	var trovato = false;
    	for(var i = 0; i < componentiLength && !trovato; i++){
    		if($scope.componenti[i].idObj == componente.idObj){
    			$scope.componenteTmpEdit = componente; // Load the component
    		}
    	}
    };
    
    // Method to save the component variations
    $scope.salvaComponente = function(componenteVariazioni, disability){
    	$scope.setLoading(true);
    	$scope.showEditComponents = false;
    	// richiamo a modifica nucleo famigliare componenti
    	$scope.updateComponenteVariazioni(componenteVariazioni, disability);
    };
    
    // Method to get the "comune" description by the id
    $scope.getComuneById = function(id, type){
    		if(id != null){
    		var description = "";
    		if(type == 1 || type == 2){
	    		if($scope.listaComuni != null){
	    			var found;
	    			if(type == 1){
	    				found = $filter('idToMunicipality')($scope.listaComuni, id);
	    			} else {
	    				found = $filter('idToDescComune')(id, $scope.listaComuni);
	    			} 
	    			if(found != null){
	    				description = found.descrizione;
	    			}
	    		}
    		}
    		if(type == 3){
	    		if($scope.listaAmbiti != null){
	    			var found;
	    			found = $filter('idToDescComune')(id, $scope.listaAmbiti);
	    			if(found != null){
	    				description = found.descrizione;
	    			}
	    		}
    		}
    		//$scope.comuneById = description;
    		return description;
    	} else {
    		//$scope.comuneById = "";
    		return "";
    	}
    };
    
    //---------- Cambia Richiedente Section -----------
    $scope.setChangeRichiedente = function(value){
    	 $scope.cambiaRichiedente = value;
    };
    
    $scope.setCFRichiedente = function(value){
    	$scope.checkCFRich = value;
    };
    
    $scope.setLoadingRic = function(value){
    	$scope.isLoadingRic = value;
    };
    
    $scope.confermaRichiedente = function(){
    	// Here i call the service to update the value of "monoGenitore"
    	$scope.setLoadingRic(true);
    	$scope.updateMonoGen();	// We have to wait that InfoTN activate the field update
    	//if($scope.richiedente.persona.codiceFiscale == sharedDataService.getUserIdentity()){
    	//	$scope.setCFRichiedente(true);
    	//} else {
    	//	$scope.setCFRichiedente(false);
    	//}
    	$scope.setCFRichiedente(true);
    };
    
    // Method to update the monoGenitore field of "nucleo familiare"
    $scope.updateMonoGen = function(){
    	var nucleoCor = {
    		domandaType : {
    			nucleoFamiliareModificareType : {
	    			monoGenitore: $scope.nucleo.monoGenitore,
	    			idDomanda: $scope.practice.idObj,
	    			idObj: $scope.nucleo.idObj
	    		},
	    		idDomanda : $scope.practice.idObj,
	    		versione: $scope.practice.versione
	    	},
	    	idEnte : "24",
	    	userIdentity : $scope.userCF
	    };
    	
    	var value = JSON.stringify(nucleoCor);
    	console.log("Nucleo Mono Genitore : " + value);
    	
    	var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
    	
    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
    			$dialogs.notify("Successo","Modifica Nucleo avvenuta con successo.");
    		} else {
    			$dialogs.error("Modifica Nucleo non riuscita.");
    		}
    		$scope.setLoadingRic(false);
    	});
    		
    	//$scope.setLoadingRic(false);	
    	
    };
    
    $scope.changeRichiedente = function(){
    	$scope.OldRichiedente = angular.copy($scope.richiedente.idObj);
    	//$scope.IdRichiedente = $scope.richiedente.idObj;
    	$scope.setChangeRichiedente(true);
    };
    
    $scope.hideChangeRichiedente = function(){
    	$scope.setChangeRichiedente(false);
    };
    
    $scope.saveRichiedente = function(){
    	$scope.setLoadingRic(true);
    	$scope.switchRichiedente();
    	$scope.getComponenteRichiedente();
    	$scope.setChangeRichiedente(false);
    };
    
    // Function to swith user "richiedente" between the family members
    $scope.switchRichiedente = function(){
    	
    	var new_richiedente = $scope.richiedente.idObj;
    	
    	var nucleo = {
    	    	domandaType : {
    	    		parentelaStatoCivileModificareType : {
    	    			componenteModificareType : [{
    	    				idNucleoFamiliare: $scope.nucleo.idObj,
    	    				idObj: $scope.OldRichiedente,
    	    				richiedente: false,
    	    				parentela: $scope.affinities[0].value
    	    			},{
    	    				idNucleoFamiliare: $scope.nucleo.idObj,
    	    				idObj: new_richiedente,
    	    				richiedente: true,
    	    				parentela: null
    	    			}],
    	    			idDomanda: $scope.practice.idObj,
    	    			idObj: $scope.nucleo.idObj
    	    		},
    	    		idDomanda : $scope.practice.idObj,
    	    		versione: $scope.practice.versione
    	    	},
    	    	idEnte : "24",
    	    	userIdentity : $scope.userCF
    	    };
    	
    	var value = JSON.stringify(nucleo);
		console.log("Richiedente : " + value);
		
		var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "AggiornaPratica", null, $scope.authHeaders, value);
    	
    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
    			$dialogs.notify("Successo","Cambio richiedente avvenuto con successo.");
    			$scope.setComponenti(result.domanda.nucleo.componente);
    			$scope.getComponenteRichiedente();
    			//$scope.setComponenteRichiedente(result.domanda.nucleo.componente[0]);
    			//console.log("Componente richiedente risposta : " + JSON.stringify(result.domanda.nucleo.componente[0]));
    		} else {
    			$dialogs.error("Cambio richiedente non riuscito.");
    		}
    		$scope.setLoadingRic(false);
    	});
    	
    };
    
    $scope.setCompEdited = function(value){
    	$scope.compEdited = value;
    };
    
    //------------------------------------------------
    
    //---------------Eco_index Section----------------
    $scope.edit_ecoIndex = false;
    $scope.setEcoInfoDetails = function(value){
    	$scope.ecoInfoDetails = value;
    };
    
    $scope.showEcoInfo = function(){
    	$scope.setEcoInfoDetails(true);
    };
    
    $scope.hideEcoInfo = function(){
    	$scope.setEcoInfoDetails(false);
    };
    
    $scope.editEcoIndex = function(){
    	$scope.edit_ecoIndex = true;
    };
    
    $scope.saveEcoIndex = function(data){
    	$scope.edit_ecoIndex = false;
    };    
    //---------------End Eco_index Section------------
    
    //---------------Practice Family Info-------------
    $scope.setEditInfoAss = function(value){
    	$scope.edit_infoAssegnaz = value;
    };
    
    $scope.edit_info = function(){
    	$scope.setEditInfoAss(true);
    };
    
    $scope.close_edit_info = function(){
    	$scope.setEditInfoAss(false);
    };
    
    $scope.save_info = function(nucleo){
    	$scope.setLoadingAss(true);
    	$scope.updateNFVarie(nucleo);
    	$scope.edit_infoAssegnaz = false;
    };
    //------------ End Practice Family Info-------------
    
    $scope.updateProtocolla = function(){
    	$scope.showProtocolla(true);
    };
    
    $scope.showProtocolla = function(value){
    	$scope.isProtocollaShow = value;
    };
    
    
    // ------------------------------------------------------------------------------------------------------------------

    //---------------Sezione Stampa dati Domanda e link PDF e Paga -----------
    $scope.stampaScheda = function(idPratica){
    	$scope.setLoading(true);
    	
    	var stampaScheda = {
        	userIdentity: $scope.userCF,
        	idDomanda: idPratica
        };
    	
    	var value = JSON.stringify(stampaScheda);
    	//console.log("Dati scheda domanda : " + value);
    	
    	var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "StampaJSON", null, $scope.authHeaders, value);	

    	myDataPromise.then(function(result){
    		$scope.scheda = result.domanda.assegnazioneAlloggio;
    		$scope.punteggi = result.domanda.dati_punteggi_domanda.punteggi;
    		$scope.punteggiTotali = $scope.cleanTotal(result.domanda.dati_punteggi_domanda.punteggi.punteggio_totale.totale_PUNTEGGIO.dettaglio.calcolo); //$scope.cleanTotal() + ",00"
    		//console.log("Punteggi " + JSON.stringify($scope.punteggi));
	    	$scope.setLoading(false);
    	});
    };
    
    $scope.cleanParentela = function(value){
    	if(value == null){
    		return null;
    	}
    	var parentela = value + "";
    	parentela = parentela.replace("&#9;","");
    	return parentela;
    };
    
    $scope.cleanTotal = function(value){
    	//console.log("Value Before Clean : " + value);
    	var str = value;
    	str = str.substring(0,str.length-3); //to remove the ",00"
    	str = str.replace(".", "");
    	var num = parseFloat(str);
    	var correct = num/100;
    	//console.log("Value After Clean : " + correct);
    	correct = correct.toFixed(2);
    	str = correct.toString();
    	str = str.replace(".", ",");
    	return str;
    };
    
    var BASE64_MARKER = ';base64,';

    function convertDataURIToBinary(dataURI) {
      var base64Index = dataURI.indexOf(BASE64_MARKER) + BASE64_MARKER.length;
      var base64 = dataURI.substring(base64Index);
      var raw = window.atob(base64);
      var rawLength = raw.length;
      var array = new Uint8Array(new ArrayBuffer(rawLength));

      for(i = 0; i < rawLength; i++) {
        array[i] = raw.charCodeAt(i);
      }
      return array;
    }    
    
    // method to obtain the link to the pdf of the practice
    $scope.getSchedaPDF = function(type){
    	var periodoRes = [];
    	if($scope.storicoResidenza != null){
    		///periodoRes.push({});	// first empty value for resolve the "dalla nascita" problem
		    	for(var i = 0; i < $scope.storicoResidenza.length; i++){
		    		if(i == 0){
		    			// case "dalla nascita"
		    			var dataNascita = new Date($scope.componenteMaxResidenza_Obj.persona.dataNascita);
		    			var tmp_Da = $scope.correctDate($scope.storicoResidenza[0].dataDa);
		    			var firstDataDa = $scope.castToDate(tmp_Da);
		    			var diff = firstDataDa.getTime()-dataNascita.getTime();
		    			var oneDay = 1000 * 60 * 60 * 24;
		    			var firstStorico = {};
		    			if(diff <= oneDay){
		    				firstStorico = {
		    						aire : $scope.storicoResidenza[i].isAire, 
		        					comune : $scope.getComuneById($scope.storicoResidenza[i].idComuneResidenza,2),
		    	    				dal : "",
		    	    				al : $scope.correctDateIt($scope.storicoResidenza[i].dataA)
		        			};
		    			} else {
		    				periodoRes.push({});	// first empty value
		    				firstStorico = {
		    						aire : $scope.storicoResidenza[i].isAire, 
		        					comune : $scope.getComuneById($scope.storicoResidenza[i].idComuneResidenza,2),
		        					dal : $scope.correctDateIt($scope.storicoResidenza[i].dataDa),
		    	    				al : $scope.correctDateIt($scope.storicoResidenza[i].dataA)
		        			};
		    			}
		    			periodoRes.push(firstStorico);
		    		} else {
			    		var res = {
			    				aire : $scope.storicoResidenza[i].isAire, 
			    				comune : $scope.getComuneById($scope.storicoResidenza[i].idComuneResidenza,2),
			    				dal : $scope.correctDateIt($scope.storicoResidenza[i].dataDa),
			    				al : $scope.correctDateIt($scope.storicoResidenza[i].dataA)
			    		};
			    		periodoRes.push(res);
		    		}
		    	};
    		}
    	//}
    	
    	var componenti_strutt = [];
    	var comp1 = {};
    	var comp2 = {};
    	var nameComp = [];
    	var strutture1 = [];
    	var strutture2 = [];
    	if($scope.struttureRec != null){
    		for(var i = 0; i < $scope.struttureRec.length; i++){
    			if(i == 0){
    				nameComp[0] = $scope.struttureRec[i].componenteName;
    			} else {
    				if($scope.struttureRec[i].componenteName != nameComp[0]){
    					nameComp[1] = $scope.struttureRec[i].componenteName;
    					break;
    				}
    			}
    		}
    		
    		for(var i = 0; i < $scope.struttureRec.length; i++){
    			var nomeStrutt = $scope.struttureRec[i].structName + " (" + $scope.struttureRec[i].structPlace + ")";
    			var strut = {
    				nome : nomeStrutt,
    				dal : $scope.correctDateIt($scope.struttureRec[i].dataDa),
    				al : $scope.correctDateIt($scope.struttureRec[i].dataA)
    			};
    			if($scope.struttureRec[i].componenteName == nameComp[0]){
    				strutture1.push(strut);
    			} else {
    				strutture2.push(strut);
    			}
    		}
    		
    		comp1 = {
    			nominativo : nameComp[0],
    			strutture : strutture1
    		};
    		componenti_strutt.push(comp1);
    		if(strutture2.length > 0){
    			comp2 = {
        				nominativo : nameComp[1],
        				strutture : strutture2
        		};
    			componenti_strutt.push(comp2);
    		}
    	}
    	
    	var sepCons = {};
    	var sepJui = {};
    	var sepTmp = {};
    	if($scope.sep != null){
    		sepCons = $scope.sep.consensual;
    		sepJui = $scope.sep.judicial;
    		sepTmp = $scope.sep.tmp;
    	}
    	
    	var getPDF = {
    		domandaInfo : {
    			idDomanda: $scope.practice.idObj,	
    	       	userIdentity: $scope.userCF,
    	       	version : $scope.practice.versione
    		},
      		autocertificazione : {
      			periodiResidenza : periodoRes,  			
      			componenteMaggiorResidenza : $scope.componenteMaxResidenza,
      			totaleAnni : $scope.residenzaAnni,
      			//totaleMesi : 2,
      			//iscrittoAIRE : $scope.componenteAire,
      			//aireanni : $scope.aireAnni,
    		    //airemesi : 4,
    		    //airecomuni : comuniAIRE,
    		    dataConsensuale : (sepCons != null) ? $scope.correctDateIt(sepCons.data) : null,
    		    tribunaleConsensuale : (sepCons != null) ? sepCons.trib : null,
    		    dataGiudiziale : (sepJui != null) ? $scope.correctDateIt(sepJui.data) : null,
    		    tribunaleGiudiziale : (sepJui != null) ? sepJui.trib : null,
    		    dataTemporaneo : (sepTmp != null) ? $scope.correctDateIt(sepTmp.data) : null,
    		    tribunaleTemporaneo : (sepTmp != null) ? sepTmp.trib : null,
    		    componenti : (componenti_strutt.length > 0) ? componenti_strutt : null
      		}
    	};      	
    	
    	var value = JSON.stringify(getPDF);
    	console.log("Dati richiesta PDF : " + value);
    	
    	//var pdfAsDataUri = "data:application/pdf;base64,JVBERi0xLjcNJeLjz9MNCjQ3IDAgb2JqCjw8L0xpbmVhcml6ZWQgMS9MIDE3NDExOS9IWzY4MCAyNjVdL08gNDkvRSAzNTA0OS9OIDkvVCAxNzMwNjQ%2BPgplbmRvYmoKICAgICAgIAp4cmVmCjQ3IDEzCjAwMDAwMDAwMTYgMDAwMDAgbg0KMDAwMDAwMDk0NSAwMDAwMCBuDQowMDAwMDAxMDg3IDAwMDAwIG4NCjAwMDAwMDEyODIgMDAwMDAgbg0KMDAwMDAwMjk0OCAwMDAwMCBuDQowMDAwMDAzMTA4IDAwMDAwIG4NCjAwMDAwMDMzMTIgMDAwMDAgbg0KMDAwMDAxNzg0NSAwMDAwMCBuDQowMDAwMDE4ODg3IDAwMDAwIG4NCjAwMDAwMTkwNTIgMDAwMDAgbg0KMDAwMDAxOTI2MyAwMDAwMCBuDQowMDAwMDM0MDA3IDAwMDAwIG4NCjAwMDAwMDA2ODAgMDAwMDAgbg0KdHJhaWxlcgo8PC9Sb290IDQ4IDAgUi9JbmZvIDQyIDAgUi9JRFs8QkIyQkUzODkxMzc5NzQ0Mzk5RTYwMzhFNDgzOTVFQkI%2BPDg4MEJFNjY0NzRGOEM1MDg5MDg0QTY2Qjg4RkZGMUI0Pl0vU2l6ZSA2MC9QcmV2IDE3MzA1NT4%2BCnN0YXJ0eHJlZgowCiUlRU9GCiAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgICAgIAo1OSAwIG9iago8PC9MZW5ndGggICAgMTgyL1MgMjE5L0kgMjk2L0ZpbHRlci9GbGF0ZURlY29kZT4%2Bc3RyZWFtCnjaY2BgYGJgYDZjYGFgYFnLwM%2BAAPwMrEDIwsDR4ACk%2F%2F9ibrVJSDhwtGG%2F4yL2FQwMQYJuTj0NDC5GIsedEETINtPzLalWYdePuRiJeifH%2FHBxVgSL5dakQFnYZMF6HRgoARgOBAIpBob314A0F4QNBAuA3nIT7wD6%2BjjLgjsNTA3GhR94TzA4MPsbN9w5wNZgdegPkDuDRW19wp0G9gaHzhNALgRoMTDF2QNpRiCyAgDm40g4CmVuZHN0cmVhbQplbmRvYmoKNDggMCBvYmoKPDwvRXh0ZW5zaW9uczw8L0FEQkU8PC9CYXNlVmVyc2lvbi8xLjcvRXh0ZW5zaW9uTGV2ZWwgOD4%2BPj4vTWV0YWRhdGEgNDQgMCBSL1BhZ2VzIDQzIDAgUi9UeXBlL0NhdGFsb2cvT3V0cHV0SW50ZW50c1s0NSAwIFJdPj4KZW5kb2JqCjQ5IDAgb2JqCjw8L0NvbnRlbnRzIDUwIDAgUi9NZWRpYUJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9QYXJlbnQgNDMgMCBSL1Jlc291cmNlczw8L0ZvbnQ8PC9UMV8wIDUxIDAgUi9UMV8xIDU1IDAgUj4%2BL1Byb2NTZXRbL1BERi9UZXh0XT4%2BL1R5cGUvUGFnZS9Dcm9wQm94WzAuMCAwLjAgNjEyLjAgNzkyLjBdL1JvdGF0ZSAwPj4KZW5kb2JqCjUwIDAgb2JqCjw8L0ZpbHRlci9GbGF0ZURlY29kZS9MZW5ndGggMTU5Nz4%2Bc3RyZWFtCkiJnFdbU%2BM2FH73r9Bb2ZmNortsnhogw6bDrSHLEzMd4RiqjmNRx0ln%2BPU9kuxg4rDbFIZL5HP99J2LzxYJQf67fknG86I0jd0W5650tV0VTW1zVNtkvKB%2FEETR4jmhURr%2BsDTFjEukCeZphhar5OTOvNjKIPpl8VcyYhwTytCIYinh8TI5uXArUy0NWlpUuty8WVcVyJSle3mxDo3QuW0as7SVRblbbSrbmNoib4t4K5SnaHGRnFzhO4w0qty2WD3VBWKEyK%2BowohKLysU5oQjginTwSvJxkSPGaHCP552%2Bc4v4R8LchL9gxj6DSV%2FQ4YhNyQ1wzLLUApPiUD5KuiskpGkEmdEwYcyuU9%2BT84WCQUjASDaApRGIylSHIykSIsUE8YCQOe3199vZovJL%2BhienU1QQ8T%2BH05mc9uJj44gCvosp0uS7HWadB9sAbdYLRwq5VZF85nDHnPbx%2Bm8%2BnidgeTYAGlRVFiRIQSSKSCUdp7roLAdLQytkS2ena%2FdnAvi7I0W7gR8wLQVwY3FbbNDjZAy%2BMkFVaZQjJVmAkAi2hMkQYyoLoAVH6ICU09sgopwXBKZEjsx7Tw3jXmQLSOA3Cn4XQEcegPpz%2BhRp%2FG70hLLFSGpM9Kxlua2%2FxPWyyLqilOO702E40lZDEiQO3o8%2Fxqej%2B7%2FYrOpvObyfzids%2FPqFVgmGgVFGbern22ua80N7TPg3nKojQVIzaSEBjjYmjay1KcqRjKXe0alzsA7qBViJmJIOiq0lbFPiIScxUya6UuTGOQeS3qZlMb9FpDvLnZt0wJ1tKbztSBYutHyzkWmQeCk2j%2FvjHNZ5FSqOA2pe3WrqEZDdqA%2BQwOqqPudGkDlQYhMzAf75BFSUK7oBF0IcTpmLKDOXSqFPMsa5OoNyu4UOfZa4Ew9dZ%2FOuQTSsX71DQoXu2YDoo7sr9unp5Km7tDjr0%2BOBYy8s40xQvgMryRXnpR8kBX%2FSwxjllLphu3KqCec%2FNqD91SpsWwCn5QBEGeYhVDmlVLW9u3t4FhiRXt232YTdBiPr1ZQIXRjOxTNoN7YOhd3INa2uaRC3IaZ5DEhIQZpERk3bnHYMCJfb%2FR5547BlxPRc%2Fd%2BeRu3xAAyD8gsx%2FziGWAQtoDAzp18eyqiAWD9ijBR3gyKZvaoab%2FvO3DiYS5APpUMawJdC8iYC5BDFBlWujPG%2FGu7cFMAGgE4pBV2k6nQ7EKEWhBSazvuw1wPFC1cY0pI5ChLpUMZdmxzVRvBn2vAsOnm9q9FgOmcgW12sfqfjZoGlHET614ffNibaGBgm1bQU9yW1vlNoyORR3K8PHElFCQDnFkqso%2BfvncK9c%2F88pVLLY7t14X67X7UKswLV820ML27Uuss1h%2F7Vi6GdRDJwMe0pjXbJ3XsV2FHQbDBtKRYDafBmZglabt2TI0P0BFdVSJSftTjhVvD5u6iHKSdpoek6%2FRnOw82CpKUd5Z29rWgeKdUO5galUbP7RMMDCC6qMyJJq19%2BcXB7T0Y8Pj1E2OsFR082MIVpr5ZS0V9HOsvAgMDqayXgmPc1vnbgcbWudF2bjxkGUCa%2BZHFKWx2z%2F4MOv1%2Fvx4X2x5JmD%2BZrB8acx03FFuIZHHk2%2FfTq%2BvT%2B%2FvH7%2B0teh9UL86CykxI34Zhr0GWih0U%2Bi%2F8FfBVQpfj8%2BJH5U%2B1%2Fffux1UZBq20GBFwLkmUHFAQQqrleTUWyG6rer3LVXAksih2LW%2Fiay3pvp6jBpxS%2B2pwLKeQcs8SiUFGhznxW%2BG6XFeMo4zdpQXSWgok6NUMszocV7gPFXHeWFQv%2BQ4LxwGsDzOC8%2BgiRznRWjo58d5AUaS9D94Oeu9R%2B6mzqC0hNRhlGggNqfyfeqQ8JoTW03WHijVHqTtQdr1J90eiK47qc6Gbg9kZ6P9LDoB2h7wTqAzweLBzkd8Y4KmK%2FZMQBf66EPuR5HqfpweJ5bGN1J54BXg87fSD8yF%2BoAdRcOC4NfW3T1IYHSYFcMX0gH6kkJj86XM%2FEIWwdenXJxS1t8tDngVcUn4f16hFUq4GC0U1rBiB7ey%2F5owojDKYOP3M1PFeXhpXV35aX55Ob6%2BHk%2FgKzZduBHU%2Ffi%2B6694JDFVcZivTJ37GYSe%2FHsQatP6V4ABAJz08LQKZW5kc3RyZWFtCmVuZG9iago1MSAwIG9iago8PC9CYXNlRm9udC9KVkNYSFcrSGVsdmV0aWNhL0VuY29kaW5nL1dpbkFuc2lFbmNvZGluZy9TdWJ0eXBlL1RydWVUeXBlL1R5cGUvRm9udC9Gb250RGVzY3JpcHRvciA1MiAwIFIvRmlyc3RDaGFyIDAvTGFzdENoYXIgMjU1L1dpZHRocyA1NCAwIFI%2BPgplbmRvYmoKNTIgMCBvYmoKPDwvVHlwZS9Gb250RGVzY3JpcHRvci9Gb250TmFtZS9KVkNYSFcrSGVsdmV0aWNhL0ZsYWdzIDQ4L0ZvbnRCQm94Wy0xNjYgLTIyNSAxMDAwIDkzMV0vSXRhbGljQW5nbGUgMC9Bc2NlbnQgNzE4L0Rlc2NlbnQgLTIwNy9DYXBIZWlnaHQgNzE4L1hIZWlnaHQgNTIzL1N0ZW1WIDg4L1N0ZW1IIDc2L0ZvbnRGaWxlMyA1MyAwIFI%2BPgplbmRvYmoKNTMgMCBvYmoKPDwvTGVuZ3RoIDE0NDQ4L1N1YnR5cGUvVHlwZTFDL0ZpbHRlci9GbGF0ZURlY29kZT4%2Bc3RyZWFtCnjazXx7YBTVvfDvzMy%2BX7Pvd7KbyYNkeS0hkEhg1zwQiFVE8e5GrQlJEBR8gaK22lSr1ICP4gPqk6pV1Kozuwkk6KVrm1rqVasWq7bW2mrtrV%2FstVZti2Hz%2Fc6Z3U0I0Pv1%2B%2F74bibnzJk558yc83v%2Fzu%2FMAgEAM%2FQDD8nTz5wzr%2BW5dTfjnZcwnd2zsftSYbNQDUAq8frOnis3R9ZsX%2FcbvB4G4FJrL71g445fL7wUQBDxITsu2HD12rpPZ0kA4iMAK%2BrW9XX3vvC1Zf8B0Pkq9l%2BwDm9YKzWnApyjx%2BvKdRs3X3V4xPNfeD0Tr%2F%2B%2B4ZKebtAv%2FwjgXPq%2B8Y3dV10qjOvmAZwXwevIxd0b%2Bxa8%2FKgPr5N4%2FfNLL%2B%2B7tOnp3EqAr%2BL7uSgQ%2FtvkdtCAVXOPph5HGVTP%2FGuwlnPoNZxJK3Acx3MCTPtbtjISgSREvuQ0v8ifQep1m8jLePuOUgOC8FEhJcC38VwGIt6xQgRq4SQ4GZbDSlgN6%2BFSuBKugmvgPfjHxAQdM8yAmVjfCqfCKuiGDXD5ZP3E7yfemfjVxKGJX0y8NvHqxE8mfjzxo4nnJ3IT50y0sbf9f%2F0TQbdJt0l7muaA5kPNbjwOAGhuQFwf0BwSZgunIRw%2F53gszeZX8wluMR61as%2BJ9yfenngrn87X5%2BtJhkQ0B7jLNNeUHvwGvMFtEWpBhp%2FCI5hkdvwcXoEcfAcGYRT2wzMwBLfAZhiBnbANroOtCNvrsfZfa%2F0yPMeOH7L8O3Ab3Ao3H3X3h6W7MtyL%2BL4DHoL7YTdcgccWfMrx7%2F7f%2FUX%2Br1Gx9J%2FWkv%2B29pfkVoTN25i%2F9N89J9mxYvmyU5a2t7W2nJxMLFncvOikpsaFCxrm18%2BLz50ze9bMWF3tjJrqqkqpIhopLwuHggG%2Fz%2Btxu5wOu2izWswmo0Gv02oEniMwU3bHZs2MLB0YWC73r%2BmaNVMua00RmcQGBjDrjsiwMiWTrvQ6mY8Fo%2BpfGjvIsDolc1X2RiytSjUEo%2BmGYLFuoKVVVMA1cKlriBAOiOsTZ3NqDjZxuiKKZWJgtdTaGZGtUkuqXWrvkvn2LinVl1YEvkqqWt4rRQYGert7h%2BH5lSlJPIk%2BNa2%2BOZ3lCAGPOBBp7u6TxOZUZJjAatnaem4njtEjRnLBaCSDr1wueRrTEntlECvUYXWcmcoSFCvuZe7lONvC1PrXrFMEcEuNMnFnCAh250LskS7OpVVqjQyoh4S9VuCpWxE01VI1znN4YuKsVDQqQxDrlw4s7Ro4ZeCUrm450sMgRLqk9vVts2a2S0slfFt1tyxUS8uWsRtdeN1VvMaRLY%2FI%2FatSSn9yAGe25tr1Pql9ezDansb3tUnRNhxOe1t7Gz5W9mFrWUfRo4tJ3ev7sEVjY7CEmHal1hWTWqKzZmYiFtIitT2LUkIEFwhtdUpFdKBNwhF1nCF1rOrEh7napbaMi7jau%2FAusTsiTZE5ICVmzRxB1u1tPyMlyV1npiLtcte2tiBDA74C508RJXUj8CIDKcR%2FEEGZTtGR9LGh0Om3UgAMSC3YfnldLOYbAUImbrxlL6FSmoO2Nnxjigvyab3d0QQ4FtKNhDNrpjoTIw5neW8PtgH1RK8GBvBioGc7Xipmoaplq%2BxrTckRBIi5V%2BrdNpyENVJ7RO5amYoGI%2FJuPMn9PcH1crK7K50OtqeV8JyBtiDNEZSxIvqCcjhGByzSp0WVOdAavuYPPrkuFkRKXDHC9UNSDrZu295zEraK0gF1Symc8bY01jfOmilFIohIxGm3DNsb2UQkinuskaMxPClVWNtuVGpm4Mk0a6ZZqa6ieLAoM2pUfKRuopBdga%2FpkUTGUNuCUt92fB%2FykaaVjrK5OUgB%2FMEHwQ9G01hRK6YiuTnB2WnMS82ao83B2g9oN1lqT%2BHFGKWYdHuY0ZicDMr96QAOCHhsHBF11uZoUIxK%2BEYVEOSsFAlCmr1AVFtFKeljw%2BZgodWUZqzdAdBDL6rYWlTOvRBrv7J9vW9bS6vUvqYlY9DXDiMZDeC0WqVuRC5KjBTCqz2SkvWtPbKhtSvd1j7QPpBagyxEKT%2Btki8lIlpi5BafHYtl%2B%2BbUxlvq5DiW5tHSrJknJ6APqMq6gBCWU9l4AUywPM%2FyIywfZ%2FmXLD%2FM8n%2Bw%2FO8sP8TyX7D8dZa%2FxvJXWf5zlr%2FC8pdZ%2FhLL0WA6%2BRM8v8iufsbygyz%2FKctfYPlPWD7K8h%2Bz%2FEcsf57lOZb%2FkOWouHE2PSyHY%2FKnWf4Uy3%2FA8idZ%2FgTLH2f5nhP0HWH5MMv3sXwvy4dYPsjy7JT2l7H8YmTSPriIlS9k%2BXp25wJWXot5L%2FGx3MPyIMv9NGfPOTrfxfKdLL%2Bb5Xex%2FE6W38HyHSfo%2BzDLH2L591i%2Bm%2BUPsvwBlt%2FP8qtxhEf3vYLlm1m%2BieWXs%2FxSll%2FC8o2s1wZWXsfKfayMz1DpNSXPD8r16dhUmpQygCVUDcgR7W1BVFZ17T0oe9ZLAz1UeFOm6RqG8q6Bge5hiHQPRNoHtg1sx%2BI2Cal%2FfXevXNu%2BkgrWyLa2aFBqo5wUjWZqSW1rF%2BWT5o5VyLMynJGKSlTBDFDBBqsGBlahqgNkwXQam0XaKdtKTC%2BcHEvFUvKiGP6ryquorSc1A7hb6QMisoeJZbe3daDjzE76HBzuN2nWLfOrUK0zARYR6Z1VqQhpjkAzlWrDEx%2BH0ukoykClSV8lxVKAAm5AMdAykStoETVjLKjoyurSsqd1SruKyXa0i%2FFE7Vjl5POM%2F%2Bx5xsnnTX8vArZ%2FDcq1l9sktBE6UY9F5FvapHRE%2FpiVv8LKt7OyhZUjK2kZsEznxwDNZKBAxSpKv1GUdrNmoqzeD3aogdPae1e17AcHNMBKteiE%2BXA6LT6LynYZHitQ2nfjdbauqbapvYUSR3dLst7Z6FrILVvkWgGnwFJ%2B2TJ7wp7klrc5VsJX7Kc5TxeWdTgWwBL0R1rQI2nQLmvStKOwozIzkorIta1r5LrWni65jAp0TdWVkXU4dsRXpGtg%2BzZfOi1rqqOR9YhIlKJt9J1Um4xAOUSCI2jelqcZlmdKLV3yLKlFXlrHgBVr7RqItHamFLR4BrZJrdvTONNhmItymqplZoUMT%2FwlpBoAw3D3ZEXLlIqTOcpaGY7z1lEiiUDbfjAiT9liqZNdJSM2CcmjzlP%2FjGCNBfejD2c8Qaf%2B43Qyq50sWPjnnaZ2taidbFiY2ilZ6JQ8bieb2knEwn%2FXafIsqp3stNv%2FcSe72smFhf%2FzTi61kxcLR3fq%2FyedvGonHxYmOyULjZPH6USvfdgpXfybhTc5WsM1CYfR59aBIEOsghT%2B6H2AIz%2BlIpWgesE28KUWz3d%2FiYpVmId9UT2R6zXPoylhhmH0SFIdchka53BLWiEGNPKTBqInFiibYMsaGYNNiCnEJBvwvplYSfG%2BSRRikPUkwbhiW1uWbzJ3JBJENogjyE4kFx4BE%2FGMo2U35IJov8mwXFqD1jwxrNje2zakcZmIyyB%2BIH6Q0RGNw96EqlGriwQWzzl40BcA%2BgiziI8wEj99BA5wVWoY%2BgPpTBI8Y4H43LSTt9fX23kprqt3S3%2Ba09M7p6HvxRfJedy95Gf5hUf6bnsFWWFbPs31at4FK6TpPBUCxenZStODwvSATU%2BcNj2%2ByXQGTmuIF8Bk1A3DpoxgBHGrb8z3Tk58J0cHMt%2BxsJ7j3C6HU6quqd62Z%2B%2FXdmzduuNre886iwsdJi9%2FmZ%2FIz%2Fvr5%2Fl5E%2FDlH%2F9IvcqlOKryqaPijx0Vz0YFMj9tVMBGZWri2ahMRuAFNiqjcPSoiIvTSQ5Hw3yOr%2Fd4PVz51GEFD%2Bfrv0RaeeWvn5NXCBsWgUz%2BdbIVfoV80zENVvZpsBIKsHKURiXgqAa5s3S2DkgcGssK5xmAiKNjOI6FDfX1bpdWF2%2BIL1i4YKEkZS66cM2mhM8p8Bdt3Lj33ntD55sWzToX399JfsWt5O5Aij5lGlSEaVAhBahoSu8nFCoKLW%2FK8FxbIjfOMBN1RzvJF3kjd8cIhXsGs2%2FD2%2FiGr0yboTBthlxhhpNv4PAN1KnyKzzOcWwEOOKRcZJsjkiBmV%2F%2B8u23gUy8O%2FEWd47mA3xHAbfclHfoCs8q4JYrvsM2BbdDwDdxpCORS%2BSIwoOYw4w%2BBa7GmYViPMUtkQh3Tt58M%2FlM88E%2FPtOakadPnXhbqNHsRo7248COhl9gGvwMBfgFS7Mz0Dc%2FCyG0d4OwL6s1l5liivn0hKJtTAw5Q1pL0CyOiWOL9xlE3iN6tGb0IpvmjM4Z9ZEszxsMYk58lXa%2FG7t%2FL2vgsfugGDJ4gtiJxOcq4kmx%2FTAbJdnsvUmLMyl6grGY7G5NZbUG0TkMs5Nm0al1ioZmTNrGWJrV8Vqnh9V5sM7DL8YTX6wD3uNX6%2Fzg9%2FBLMEGxzmC2iOozLVgyJPBkwLq0pgLsItTPo7nUMM%2FBOzipguMdjnquaSw%2FQcjYnwk6NH%2FetYuQXbt23k3I3TtrSD%2FpweOb%2BW%2Fk78ej%2FyESIVF65H%2FLjveQrtBt4X6LsLfCpSrkrZP8HC1A2FqQMtYCP0enShnF0pFQDI0JhTeI74yAAJeNKSZ8SChDLNUxohhNw3BpFkWvVcxt9Y37KGUgfyEpRO3xaIMHGas%2Bbo9LEhfJP3PXeV%2Bddcf5XXz%2Bdr617dbDXZTyG1Dy23CEUXhKHaGpOMKKEm2Y2AiNsomNUCrRhpGN0IgjLGtMZMwmo3gop5jFxhFww1OZMr1hBG17ozieE8cytjIjUkbSwIONx4NUYc9AmThKFDCJh2QQ5cAhxe8nz8Pzij%2BgtY6AH3Z%2BTKhph%2F6KOJY06MGkxwOqYicxKabV0SOOnByf76hasKBhfrVUgTekeDWi0usRbA%2F%2F7rN0lY%2B3jlf%2FnizcefEj%2FTd9V%2FOMQLQfHez%2FBv%2F3B1%2F%2B%2Bk2XzG1%2B5n0SffKs7Rdv2rFvz5%2BJbcuVTA4jz8xCqDTA2ypUyotQWVCCSjmDik%2BtMJOFJbD4GFjMiLTg6YkRqIQROSiOZV3OIGMOJRZrmjMC8%2BCpsWxsXmxeJmg2Iw%2Fw6FeaxXGlPCiOKnozQkYuF%2BXyQ0okwoASKadAicCuj1mF%2FpCi07EKnZ5W6Ci0FJ9PfHUEzeJdY8rscvFQxudy5nIi6sgzUvLSK9Oy7ZCP%2BqE7qWwiJXjNX1A%2Fz%2BN2UWh66PokSBXVNfH6uMdTPw%2FBWoO6q0LX4JmHxLSAT957df%2B3dc98%2FPbLX17nC82fsTX%2Fn%2B98%2BoXp38Ud99z4xKqlF9efY%2FOcO%2BvfFiwX%2FGu%2Fef99v%2F%2Fby3seu8AleiMXDr6Yn%2FjTJ3u%2B892rrluR1PPCAxqtaXYzSqh6pME5mgfRmPOSc6fJRl8J3qpstBTh7S%2FB28LgrUF4I7MkDV5RtKDd4NCI%2BpyAAJ%2BTUwy6XC7DE9swXDKOpSQfCRwMKEZzLjek1WoEQSPm6B9RtBbxEDJUu6IVlq9KZZICbRhUBE7MpWWLmNWZtRZdc7NsGM06RJsVmyoWLeU7dEsIvgHb5UYm8E%2Fx8sNwWUbgQzFsoc84%2BHCMJyTT5XAuj%2FQOnJliq0tot6yJZBwOu15qzDidHjyx1b00ZLq8nmMaer0ibejxOCcbypqlsmPpypSC821O0ysnvXKyK7JU5peuSlGR0JymKI82NBC0jeKoluJ2yd6AoiFOdHHu6SM3cvd0dLz00lC%2BwUjSZ53FPzB%2B1j35x0j6Hu47R661Up5oQZ4Ia26ASnKOiiNPEUdVJRx5CvrLw1BUfbRtovgWoRyjLGFGluDFsUHeVxb2IcjsDsXnGIanM%2B5oFJXHuCj7rx1FSg0gvrKoPOaXlefKFB0vjnLM3qMraJBWHOblZ6bUGzm6pKZEHcvPQEiYxWbZod7vD8rJdGM6PbVbIHrcbtEAdotO6daH%2FTLg1mFrRecWmzNJh5s2dCcbg5mkzkzLuiRtlSaK14P62K0bzSkhjxL2UOR7PaFYxhtGxMseUdYdUvR6xrB6HWVYPeVklH5AOdaGHCv7R5lcQ%2B6rnMJ9jElrnEzO8fGFC5hoC%2Bf%2F19tv5Q%2FbnrU9svX67%2F%2FgjtPvqt61jvvoyP1LtFu%2B9f5NN5Kf%2Fyb%2Fx7cOXHXD3tvuVk5dZOThmfyVwqrrrkVuOw3xWIWyLQS1xKdi0l%2FEZF0Jk36GSafsZ5iMlTDpVO0pZDbP6QlEtEhl0r6xrCh6gDLbCFTDD8b2g4EukIljilVEuKAFOkomrQCnH60A2S9mLZZwkCot5DynU3xVkUJjslOUhUOKRsNApREoqDQQRNlmCYs5yEQFETlTEZKNGcDimayYRuJWfFMsiUh1v1RdtCREtL6Z1peqxWrJ2YxJLFoETk9EUusiWHIm8OSkFgGZ5%2FFSTYLQp2IRoe5gcpGziygDXSXU6LRc94Hf6TPG%2Fose6zxNf9OrL%2F0j%2F5ffjE1sG2h9uL%2F%2FYUyrztDsfnnwos27bx57Mv%2BPN9%2FMHyYbyRnkihdfHPdsk%2BVt25R0CrnrVCYDb0BL4QIVJ9pjLX8tw4le1k6z%2FPUMJ1bUw3rkMJ1ezGW0gkYVdiRjNeKNUUWwKkAFVsZoVW3FqMPpQLJqiLrdHM%2FxJNR%2B144Px9%2Fl3x%2B7%2FfYl5EIyn9QR8zMrV%2BY%2Fyr%2BSfzm%2FC1BFUdqRkHbCUAcLyeZpFkPjNItBU7AYmkoj1bCRepF69Kpq%2FIHijY%2FAPtBTI3JfLCbaCHhpJIL%2BITGVIW1xGYjFEdOZ%2FrIY5blYXGxWYknGnZVlUyoqy7CijHEkyQbQkRRHxdFnUYrcjcbE97IaE5LdJBUGXHiZtEZDgZqga35AM9vv1ZvErWMiO44mqP7KmmS0pkhQ3kC0khFNtNJbGQ00Y%2FIWCcrlraxhdTVYV%2BNajCcX1pUeFJ%2BdnD%2B7%2BCC9aXacNZ4d18dnm5Zg0hcfpNHH57O6%2BVg3X5PAk4ZRpqqjBZQSDfNRI8dROzeUNHhVUWS4XV6Pl56KOpyfd9LcrflPf%2FOb%2FF%2B3zmmqjF791xde%2BOs1kfzvLt6%2B%2FeKLty9devG3HnvsW9%2Fag07g4UDNxuyLX3zxYvbiar%2FL99V7nn7jjafv%2Baq%2F6Wvd3V%2F%2Fenf3ksX33f%2B1a%2B6775qvnX4aXXE4e%2BIt1AwPMokSUGnCeaxEcTKa8MvOaRLFz2jCgzQBVKIAShQUHmMF0fIDKlrAUxQt%2B44vUfxO6pb8tzKEHCV0ihJFFFSJ0tdI9ZQqUvqOlSnVkX4pUsTcNLmxuCA3jidvlhTkDXoZJauK4k2L0qRhPpqqDruo0zoLWGOVnPOH7%2BufNt1w8UOppPOmnx78nBh%2F9Z9ffvvbSx649toHMJ1%2B%2BoyXBy%2B5emjrO98nujffJNr8vvxr%2BfXvvMN%2FVBIpiBfm1QoSWwnaMs2v1Z%2FArzUc5dcCGpbEn9G0kWHm3ybGEmOo%2BsjyyMDKVJ%2Bc7IrIOqlR1jBzhIyAlnjQWBqmDrA4dqjoAdtVL5j6wYL0Nh1ZGECzSHMARLT4tGxkIE5afBWFEYh0ZCDIYsHgq5iypgAK15TY6%2FXYOK2tIzFKJcYQz3OEmWD4NwIm6BtyOjmb10LlyZwxyDpNTlPGxpljQ0aTy6NzooGIBKCgT0j9KVt1bK9I6OGJ7bWYLF4nFhSdwCq1WCnweNBKnVHncWGBKEQUX0Vzcm7gncCg4HIRcUXbNZ%2F5FK9IHwrrM0REE8AkhmOoN9EOUDwCfSCsy%2FACVhgFrHBhRsVxPRpmBB01VPJU0UsSUQthLnbZ5Zx40UV3HRm8Z%2B0F3B1j5Ofvaw4cbiFP5M8WPjyymdtOrbPOibc0mzQfQpi0qJjWFeFZBmWCCjedakHLOgbPclJmnmpACysT1OnKjiHI0QUxLsBLJ%2BxBhjNSN02no4aKB%2B5XjMLWMYLWsAyHlHCY8VgYKI%2BF4f6PWYX1kGKzsQqblVbYaAVkIrYwclcWkmHkL%2BSwo93%2FfqcH%2F4sMpjOWXHws6dDFN%2BqKDCboSi6%2B4Pfo0MXXCVNlrM2J%2F8UHGa02dR3Bhg%2ByWZsxGUtrBYI%2FzOrC%2BKAwJPAEqozlIBqhqwHReVC0A%2FDSgbJ0YVyzqTr%2FZP7L%2FKv5nWQLaf4HSUZyoTcf%2F03%2B8F%2F%2F%2BOTvq7hw%2FpP8f5CH2erA9yfgdzsezD%2Bb%2Fzj%2FTv6VX%2FwIaf9RpP0%2BpH0rAvvLaasCAYhqpq4K6AurAsESrlRdr2lKKKaOxKCGJ5qW2KDJbDW1xBSfdxj2yBp0uBXRPgxZ2YTFrGj3eUUkdJLx%2B9AD6p26WpDTELRKKWJdsAPHsXWMz4DLxnSq3kV1qsuGOtVVMLGmYsvrwv8ikDV6l5cB0uXVeF36ZkyaIpD1JrtLrbNjSZ%2FAE1VyaeqGxAtQjsalhfEolYScFI0%2F2kP%2BQXykI%2F9U%2Fr28t%2BenZMMv%2F%2FTeK%2FkbNAfyl%2BWfycv5y59A38JKbKQGKV9Gy%2BnPCE8%2F%2FG2ajJuEpirj3AUZNwlNN1szhQ6UJQnZl9sPbhpZRS%2Bcenz70UZfC5w4jhqIKxi3FujDS4toyQDKkX1Ot0M0WaiBpR5CBtDBQxp3J50qjWfAaKI39EljsrGxeGVJmtRqNFQcJqOTooVD2iiiJafJ2AQ3EUdHFR0aguVo61ajI4l0EsG5VDNUkzJ0TqpjVHSgGxeNu92YYaF4pjdkoX78Tj4y%2Fh6%2FRXMglconMDlTqQLUnkCoOeHdaVBzTYMaX4CauwQ1nkGNNDk7EGo8wkmHcCIIJ2R6pwonM4WTzWwzZ5wE5a3dZjQbdCqMFAcwkeqsjpGs3WhwMJrEIU1OnmStVp5BXNFq2PR5nLPZwqZvrY6hKNFS349kgJ0zSasdiTVpsFmtFrPZjF6Z5KV%2BGQXOsbCRZP7w%2BMvcy0fqe4pQOdKDA1iDMvR7KENr4PC0ddIZJRnKF9bC1HXS2hJM1LUw7QK0YlYmlIh7hAbzxbFRGtnaA1r0hIxacZQGALNyBK8q0IBBaohQcwMpAz1Inqd86EABq4Wt2Dw0jMWoUTw0Jsrlo7JRlI2HFJOJiVWTkYpVExWr09jS5sD%2FIlvyWpuDsZ7DhiV%2BMZ6mrJM63GqdG9wOfgkmJvs0x5F7wCyWBQsaGqi%2FYJdq4jotb0W34CZyPWn8CzmnLBf6YO%2Bb%2BS8I%2Bej97R6HL5qv57719bmmoRnkSXIOOZf8IP%2FpL1AOZvOf5n%2BXf%2BHRQLhme8KxZ%2FyLEcsbVHs9gDIxhtQYhOdVyAeLkA%2BVqDHIIG%2BTgwzyYRK1qPdtbG3czDfZA2gIJEazHg8fUKlQR6nQo%2FPoMgHeHMvqdW4Po0CCSj2cIZSojBZWNFOeCjK6DGARwUaLdkqiGq3Xx0g0SIKTJJo1GlyMbS02i23ytkpw9SpHFgqo09UrSXqgp5eb6Ont6T1Ceno1B458nzvncAuXPfIVhMF%2BBMQ3EQaGogdYCrgZS2vFNOBGzSw6HAMxqO8lWYPWoN1Pl53AjIyp7%2BBRnE2dpNbApqOn0xG0tJ%2BYmzJSSdpPx0SNChzHfSgZTsVxWOCqaZLBOk0yGAqSwUbKrFOiBShPBSpPzTlFZxhRBWpO4TRsOAJdtdbopgk8ophNTKAZSgKtviTV7kOg%2FQkH9ykKLhzf3QBaL44vTFLTuLSsND6VSx0FLi0v0YqDji%2Fj4pBQFB3qUBsmpJohnzcc4nSq2zkUDAb8%2FoBqRmaNQZOf0YxGz8avo3JYZEUqh4FnkOWwiOxEi65qFUPJrOjgNSt6BwZW9G5f1ZLRhwEnjRpbo%2BXF1lRzcDRdVMEZi9dN60SHqHUcXZcJOHgc1j6T35gM%2BgP6gHjwrQAOR11kDOmpkWnUh2LqtTFIr4Oh4rXPQq9NluK1yU%2Bv%2Fb5QjHTIvpUpxe1frgRhOd0qGVH8wYFGiVpl6ahKs4gCEsf%2FSSomaJbGyb91dd92G3mEfO%2B227q6891dXfkuzYEvw8L7h1uEd76sFN5SqYhxtBden4Yl3wmw5D8aS%2FtMXIetyYNYyiUSOQV1zZDL5XQ4nCpipuJjChImUYOMy2t4pC%2FFg4C%2FkIJT0SP7E8WEULkQsYMHo75XieIyUQzzehVMHj010U0IVkV0FO6ZXPSe2xWKTYePSqoUMHFyblfXlqu497q6Nm48YkKYrBaeRJg8g4S%2BEnXLn1C3nCAKJ5wgCmeeFoXbgyIyW4zCoaJBhfOvROF2YPf7%2F8dG4VTNo%2BYNajBO1T58KJ%2FLbybbSZK0kG35K%2FIHPiSB%2FIcf%2FiGP5z9M6piz8o%2Fkv59fLZMVCD0LGpBD%2Bb%2FmP8vvRfhTe%2Bcw0qMD5YZfhb%2FjWLnhKFjdjmkekmp12zsSCopWanXzaG6HgiOID57a2C4Xby8aiNnxrMvismTsVOWYLC4XEx%2BTGgX74TW9TZSwapA7SIEUc1mLw0pDNqMoQ3xoklPjnNrkvrBqk%2FuoTe4Lo03uO86ypyvY7wtO2uSo0CiYfUFN0KdvxjTFJnf51DoXlvQJPE2xyelSklaq1DGud%2BI1W1OKxuUeLvLLv%2Fzll%2Fk%2FPtzT%2B1Oy7d7bb783f4XmwJsHXhsj%2FJG7ec8N27%2B%2Bifr3g8j9VyO0JZhN5qrQlorQnlOCtlSAtsSgPXfaembF0dCme6EL0K6q4itUaJdRaFeVVZVlKhDaSlWViBA0V5WxOJu7At3YiMTgXlEdoz7O24q7lj6Kwn52hMFeIlIR9mVSeWUB9rVF2GfCZrTucxm3nZ5kxJzfrgSQfnszoj2ELr3LLuYQQbWzVQTVUgTVzkYE1R4HQVUz%2B2tnTiKodiZDQu1MzcxafTMmzdFIoEvPXo%2FX6fF43VJ1dU1NNUXDNKwM9nDLP%2FrsG5Gq6E0aPVcW%2BxzBJwjX3ZafQZE0NtC%2FdUde0hz4j9HnNwbd4lf25j9rOckdcew6b%2FPNVxy5ia%2B%2B%2BoZvsJhn78RbwhcooeKcVd354yru%2FKkrom5eSVDVMdSJch1DXX2JUUS63aNfrKufx%2Fb2n5UarC9crOg%2BMzUCdRM5hAooTpRdsxYk9lMMghPtYb9TRFtZQBTPEsey1SbnLIbD%2BCyGDh3cP%2BidMctZJ27NjY4TolBnQvGjXyv7Rdl%2FSAkEmF0c8FO7OEDt4hGIwo7BudZZTpF1kqWcEp%2BLHeKiHD%2Bk4IxpB4jTDgA7aIck9LavPzM1DF2KSGNEWVO1WMdWlkzV9NBrl8si3W0vV0vUdcuQaJDa79EgojuK6M4QYNEi0OENwBvoqFQH6JP8yUDBH4wA9Viy8STWT1sCyfZHywKloEVZVF1jLsOSmMCTOLnGke3XCcWWdbPoPh9sqROwVLcYT3VTV0NAwP8izc2Kg8AaAzaGeDOmWaVYiD9QxuoC%2BMqAfwkmNRbC3AB1RVmjLj5HIzXV6AxMDRFXsXXoyvp5gjcuhCzPP7zn6ZNOeobAK%2BhgvZY%2Fkn%2FvoQf2ad59Yd%2BBxJLnJuBHZ55Jyj%2F7O6k2Cq5tO6%2B%2FdnVdXe3ieNf5Tx%2F8aOuNfzTtfvq6ay6snzdr8exVq77zi7%2F9%2FVdIn2GU4C8wCb5zmvx2ltYyHap9bDtKqKKLbXPYptjHTSjIE4N6vVbfEsuarYQfQf9Vj5IlN1VOk4yWPmFUsaJ9n3E5tMvbMnqXTfxA4VWfVIuy3GRkRT0zW%2BmWizj1kYr%2BpiRx1rxNeDxvQ%2BvamsK%2Fw5%2FiTB5CW6CG2Ub%2FNW2tbtI2UtfqPIW1uknbyMNkowHtVjcmfmWCOpiobpGFdEZm%2B6AJraBipkU32yJipralVqfVIs%2FMYZJOcTKnGzzgEV9ldzQZcBqpBHM4jWJzc9aRdB5DnuhcmvF%2F0rk02xi5mG28zaxdgmmK%2Bi85nliCBJ7UhTVVv6smkxqaYGVNDTqTn%2BT%2FkH%2Brp%2FdH%2B%2Fc%2FjxZ%2FEK9fJjUkxD88vuHlN954mb%2BTapYVE%2B8LS4TTYA60kA%2Bm2VGt03YzhQt2VBspE9X7YQa9mtPQS8fkT2JaNEI34qP1NAIiPD42qDU0xEE8lHs1RzelzMyhjYyeerJhEWXshkVic6Zf20CFfAMNA%2FcvAlpehMwdHDIktUm820hXHgTFBW0ZEA3YbchFGpIGxv9t6IutTilaFmCQXe0pZR4tCpmo20CjEG4a13TTTrRYiFtnklPuIHk3wu7BUNgvonWb2zpGqNGxIRsKh8LiKEUmOl4o4eS5OTmWk2e%2Bysu8KLsPocXBJJ7LTSWeC3Z%2FTJTKsPgSYQONxhvp6xvjdH6NPJ1TI51Tpj8cZ8uApC2tDjgTn9tIKSSenFugkKlxssaG%2FobGSQppaGRU0NiAJX4xnvipkqlxUf%2BiUmPgGxepjRfBokZ%2BCSYoRE0a5i9YWFh%2FKO1ZWcAiXioNLXRrtVIFyqA4UrmzAXMk9kIohR7kwfuHn3r087KgVH395kuutTxqeffnP34pGtKXOwInudbOLrO4Alsz8xZ%2F%2F%2Fqr1nTeuqbVI3B8eM9ttz%2FhLud1hgW1hrO7L1grK48%2FdGQwGBWE643mxqo2v%2Fdq8pPNPoPPsXp5Z%2FdXFrRZQmGkzy1In6htkbWqyenT9knUQIVh6j4JU2GfxAxSZp%2B658uKtKlPJuiC%2BhOK3jsCQ2Cl5r3CNySoI4SeivhWJhn2UqIIeynK9GGKpTAjSa%2BVlr3qzgQFuGESZQtoJGsyAV0AeZWqyDsV8NB1fI9IC3Rr2QZKO46crMnJkVEs8Fk30kkbUnEgwowbU4A%2BOBBB5RZINlKawLdFPGxPhEofbiToSADrI8m%2B6bSR9Ib7A%2BHJ0GdAXXoPhPXhgKkZUyn0abJ6A2qdF0umBJ5MlBScqie6wDvP4RY5xHhN3I2kMBnzjMfjqKS2dHZe%2Bezrn%2F%2Ft10MX2R8nr92yY%2FuOu25esUJz4MgfnuLyv%2F0i%2F6f8j7d5vvWNN1762fO%2FGnvyvPOo9bN44n3%2BNZQrx42fmE4QPxGPip%2BcxuIng8X4SZLFTx6fHj954J%2FFTx44UfzkgRPET3z%2FQ%2BMnlCE5tnBY2kdRiEUvjPOvhT784Rt5onv3%2BQ%2F1T5kGNn373ttvXX%2BLl3zzxUNkHkFzn9T%2FYmTTdb%2FOvfTK1q8jTy1D3OSRp3xQSc6fJvOroMwwVeaLBZlfXYoWiuqOFeQpbZJh6ElFW14U%2BoidAk%2B5QXyLrtG%2Bmkn6yylj%2BcuZrPdT8vYzxipnsr68wFgmC2MsazVdJRgmdahBKY%2FxPA1QU1yHYLdiFSmuRVE2DcMGekV5zJWTjTm5Iie7p%2FBYRYjxWIjJ3pALeSiELyowmbViGpNVhLBBRfIYHiv394f8k%2FLXH2LoCfmxxC%2FG0xQNHSpX68qhPMQvwVQQt5x9vqN%2Bntc7Zfmyga73Tm4mqObCb%2Ba%2FeOEnm6ydnWee5b3kiZ3bd26%2FYffOMDmZ2P9CZjzm4mKHW3ZEb%2BV%2F9vzrL%2FzyJw%2F9%2BmPE4iqUjFHkMD9i8YfHYlF%2FvBWQ6hKPGaZiUamzD8OgEgUxPBZTPIDsVoezeVyJaukdurHkoqzZDFrqJY5nzZXmShopiGUDlSYzdcczyfJKqu%2FKK8XmbCBZyfgpA5XMNDclzYXgTAbMdkoKdjM2o7cbS41oJ6roi%2Bv2QbhjKGg0GCTLMLK4FphVJb6ay1EDVDaIsueQ4nYznnZ7KE%2B7KU%2BTTDIo0YEEJcRnEPGZrZTMFrrEhexucquhJA8bTrBwA82AY3cn9JdXJoOVpVUbY1BFbrBcWx40NmOasmpTrm5eqcS6Sn4xno5SynYL%2Fk8qZYudNbbYwW7hl2AqUAk0sA3Tbh0auugTMMauR7Z30v0o6qYprun9%2FDhx%2FufrhOPGPzUtOfWRC%2B6xZ2Z8com%2BZfktt65utYZJJE%2BsJJE%2FmE%2FuPmvt5k55hLvprjPPvfDMraq9z%2Fk0v0BVNaZSi6ZILcHSDgENtfdljZjRmM05VGIcU2Ikq%2BE0XMngz4hCR2KOKozpvruLFJMoHhrP0u3EGUGkqzQmPQvhDQLofb4cltT1aomtV8OUPYGKw0dR4PAds8WQDcNg1uVyLxGZEzN%2B4s2Njip0uVK2jxKiNu4KypG0Ymea246aW7ZPuc%2FWXklxrVVqqI%2FTjT1edw2ynjte7yZ9nZ133rl9%2B%2BmnzG%2FM8t8dv4D%2F7l17t1zFP8S3Lrn%2BLmodr8o383nksQjMJHeoUDMWoTarJCmNhWiokfHY7JL1waKhig55zNWWUCzJRMZ3Rs0wDCm66Ag8ARYqLxPUW0YdZ7eguBR01A6J1jAnuIaKS0uUbaVk4rJGR8s1qrgUMujEY7v90I8PYMtYwGqUkE8cpbVMt4WTIZX3SNZodLlVQVoJD6LZyixdHjZkXG63KkftObmOmStGUQ5N6tKQqksfRPYqSde6SjrGyjq6i7PSuHwllielq0ucJl3rUCwodcdI1%2F5oJf6XFKOlsAusMool42I8lRSjzljYBVZZo6upNC7BpFO3FaINq2O6sSBk0Ym2c1xpw7VzqpS13PsQuST7Zv7wi89fZnmcfPrl3l96hsWrv7lz285tN%2Bz%2BTv61Hz8rkVuLQtd5z3g0%2F5f8%2BJ7dZ58nvJ177SdvjH7v%2FfepbXMjAE8j5OXwl2m72SMli7S8EF0rZzQRJRX2KdE1RUAlSVc%2FTSh7yzoS9NO2HjCh9tSbxIMKahLKKmXVMSbSo2q0mnrSkupJK0HUQxuyaDKVF9baFE2IRoJd9LbVhh52yQ0NOagedDlQwTXvsyZtSVfSQX0qJIlp2HB58b%2BIDZO1tAPB5HVZmzGZiotpVIuxnbPoMDDeKliL9OLGzs7yK2Ql19m5dtO3d3d2opX42xk355QjEiffeMVP9hz5MeWrmxGMJ7PYnAX2TYvOWUvSqBSd23Di6JyiRzAKiUTGZBHEd15iAKtQAUajdNK0KB2RtSJ68Ideis%2FF55iNp0inFH7dgl0ulZZ2DdBfvOimy1LrBonAflhjoNGdJQQEh2u%2Ba8hgtWl9mtiUaB9f75a2deIfNzwszP7ydc2Bl5BK6BwXszleekz88V%2BaISUWOssptHC8qcXnDhmMpmlDu5kOiwUiVbrV1qA0m0%2FeU0c0vziihhLdzmd065XnM7pdUKJbL6NbvwpsRrdOav2VQ4%2FinDkMPUMmnVBd35EYxaFQMzA%2Bn42wHgeLrEuL1QVyLmAHXRJ6188iS7q5cUbQ88n8Uuw3K9okFAgb9tbW1NbUeOezTw8KdUQJBWgH1JTeQgck9TgVnJnamjglddFWKdXWJLVeumekki4aZkIBlEPNQ15PKJAU8P4w9CveBSirJm8p2DKi4IMiA6tTXWenBllNn3xM3crUXtFWUyuh2JPxHcF9hSvtv9IwWFH4n86KoXL8n9wbWTItneUhTzOm0t5Ik6205m2aWWtrxjSFTd3xwgZ374nY9WjeXX121dV7bl9d7Q5v%2Ft6efZ2dvV1XDHR2nvNvV323s1OY%2FeDc21ZXx6u27ttzxM5lrrlu771HHuN%2B07v%2Bpe8hUxekIlLX%2F4NUTPwLUrHif55UdJ9YKq4%2Be4YqFTdec%2F0uCswHGlShuPuWa5lQZF6ZEEP4nSCiqT9BRFOcFtF8HE3nwWJEUzXv%2F5WI5h3Y%2FYHjRDR9%2F0O%2BK2Q7fIF%2BO9AwuTXbU881vZv%2FmDjffY848n9%2B79Y9e2699bHHbg2TxiN50oj28JH8wXvffe653%2F72uefepatK%2BbQgIax9UEM6p%2B3KmFGiVnVXhrmwK6O2ZNeZGbXaELgGVQY%2BoRj8dFXJxkSf94yEwqPB57RRa67cTy2lcj%2B15gzlqsNLrTm%2FTXWEJ1eVKtRtWS7PMKnKgJcKRrPZC0Xn907FC9T5BZEW6JIjW2ASmMVWMSo7pzi%2FoQrm%2FJpDzPmtYM5v0ToT4Pi%2B77ELTP6pUshgLkkhQ3nIjFKI4YTVmW0lxxhL5gSezKUFJjdjitIKE%2BMNctQSk2fL6rMNm%2F9dXWLSdnYW15juFGbvsjxZXGHSH3mfqy6sMe1X9zhxexgOd6sY9BUx6C9h0McwaJV9DIOBEgatiEGiWNFSVqWOGZnE7NQL3o6EePCgqPoQ0XBaqUM67BmLoYCy0qiz28ekjfcoGUSGzFaNzuVmwsdHfCUNRs0N2%2BTGIJzrAiaOmQOi7l9afbY3sPYuf6Bqx4NUKuyLSTejan%2FmISZRWybe5%2FfStfljIogzilOdW1pMm1FYrpnBphovCYZCBHFGfO5kBFG9UCOIM4oRRCTnWBKFbggGWQTRW4ggPg4xcSxbaXLGWARxTqwQQXxg0FMdc844KoJIv9qSvaLsPaT4fMx78Hmp9%2BCjzjnllDsGZ1tjxQhiNKfMmY0d5ojynMkI4hw1gnjH9AhiOVtLqBRnqBHESnpMRhArCxHESXajfHbcCGIldTmz3qTv6AjinFIE0TclglgemowgFhigPIQlMYGnE0QQZ8RKEUQszViMpxknjCDG5pQiiDEB5jRjipUiiF6fylc%2BfKXPuwTTMRFEL4sgao%2F5xLSm9B2LN859pP%2FmxkuvqK29Zv99rS1%2F%2BPHv3zU%2FZdm67epra2u%2Fvn9nMvnm8LO%2F0XF%2FT685raMpHA7PrG5t2fLg%2Fmzu8rXnphrpjcqTk5t3Knt3UysyPPFXzqrpRJzeNG0dwV%2BKF2lUuxZFVMbq0oiqVatxaVyTVi3XkciKZltbYjSnGI1o7unsOscwXDSey73Kdl73oFAUR8dGyaDFKjpdYm40l8votRoaQjSZcjnF50JKRO6qopNvUB18e%2FFjTNQJ9Zz13Loru%2B%2B8c%2FDxx5vmJol13Te45%2B8gM%2FOH7jiSqKyjc9mGHPaRMBv9%2B8%2FUuYSKc4mWZEioIENCjLEqSjaLlWmBCAp6Myb%2Facx2yfjddhTYdPm%2FZ0wxmJiMoJshw%2BWsGMGiVaRCBNhmyHAoHKJ7qiL4yA1s04TLYdBTSWK2mq1Tti9DhIassg69IYImS3PSaE2a0WTRJw3MbDnGtRYI%2FhdpzB8iKo0RwS%2BQ0BJMfkZHhZjhUUuTNOijArS%2BniTWXUHtldVnOzybntq7l8qoI6dt3fKzH3CLvnz94XDg5r17ud9SSIpo%2FX2CkLTB5%2Br3JyV9KkKZcYo%2BheLvf9hJmfOoKE1HYsjItWk1VvGllxI5gW1Dq2Tb0PbS36UB%2BqkITzeh0e9I9CZWacRKkwUPWqnX4oGFIZvNakGiUT9CztosJmCiy2Jb3itXr0xlk5Zqmxg4GEgrGlB4GIaNGRvQzSdaE30wXJyxmPCSZLq0mmO%2BGtZq9fSrYY2GL301TH8BY%2BruPiJJ4vr1aIjO6OnNX9bVLcwev5Pf8OXrCCcvwmkM4RQmd6pwCh8bFwkzODnkcDEu4piyj2%2FIomlzhdSduSIJadjMAmEGjRBCI0zogdAI%2BPCgoKIbKSvZRsq9bi8etJJu1nPQSl7LKjVYybPPFrGSglFLK%2BkvI1SyX0bYazLgQSutIh70Yx18uyPM3s6HxVHF6hBH1a19Wp4ClKOfaotWfUbPPtVWq2iM6uIMCas7AdWPeQtVbgftJTpCNNCMVY7JKhwG9hKttIrHB1oL334b9Mcgx2AwUeTQvQhF5GBDr%2B843367aUOfLzDZkH01pGLQGY8XMMq%2BIWKIlSQv%2BXp%2B1%2BbNX%2Fz9kkvzu8jmSy%2FLf54%2FfNnl3AxOm7%2BBXHvkb0d%2BTXbl11F%2BSCCen0c8O%2BHX077gc6l4nhgvfMEnFr7gc5fwzCIsWUeHvi2RGBQdDkdLLOMwW1A4Kg6HmBvPiWgpUgM%2B4wQHngpbLzJ6DiF%2FUcZIF19ZK6OFo60MRvrFFtln1BtELbAdn6PiIZSgdONFtbrxwmJjRbE6ljUbOKdY2hPLGSlezMbinlczYxcO2MbNhaSwQ8PtJoVtGg0J7vojT%2FOtR67jvjN%2B3S28qaNj6Snjn2OOgp%2FtNtC8S2qgmv3sqw6auUrhACygMMr6ob21QmmH4m8QBYchmVZa0U0oj8n1wxCJ2WWuYR8HbUvbwjwXSwPJ058oPBc9TB7K6TNkMgey3GroSLDVTXSkiDgm8wdxrBKpd8byn%2F12kETy7%2BFYmOxnY1lYGstJpbFE%2FFK0QpGmjSUamToW0rCPQEVlhTVEx6L%2Bim0NCNe%2FP%2F%2B9823Nn4NJz35XSpG%2B0VU8j39yZJcedPTHCNGhK%2Fx%2BLua6DfmTQa8vzz%2BVf0p%2F7O%2FqLuHuVwv0F6cwDXNPwDZMS7GcwXMnOzdNvCtsglMx%2FRxTA6almOoxtWA6rVBHz2fT9pqzIaz5KXRiehTLsvA%2ByNomWIPXD2D9fmx3n%2FYJuBuv78P7K2kbLA%2FiuRfb0r4PYZsVmLYIAIvxvAzTKuwbpmdMN5Kfws004XNupNeFNltwzA9gn5ZC221YFjF5MSXoMwHyP6P3C%2FNvnkyE%2Fib4cwiFVZheRy6bj2mX%2Bjvgmu0A2tkIzTYEbwbAmAIwRwAsewGs5%2BIrLgdwGDE9AuD8AsCNBo3nEwAfPtN%2FEkBgEAARDaEkpvcAym4CKL8bIOoBqPgWQGUjQPVbiOSHAGa8CxC7DmDWAoDZOI65OIZ5BwHqxwEWOAAWngXQiO0bXwRYNAOHjmJh8SGcHrZLXgPQiuNtfQegHd%2B99DSAZfic5adiQr9m%2BVv0N8wZFSyBc9DWPg%2FtCQ6hDtABtHgl0jvHfp2srEQri4B%2BUU4EA56vhEWtl1x69eXrL1i3OTKjpzYSb2qaN6tpYWRZ35YNfZs3z1rZ3XNR9%2BW9syMnb9gQYc02RS7v29R3%2BZV9vbOhFS6BS%2BFquBzWwwWwDjazHxPvgVo8x6EJj3kwC%2FOFeL0M%2BmALbMB8Mx6zYCV0Y8uLML8ceoGaVidj7QY8Tz5tE7vqw3Mfnq%2FEnLY82YqzQYCjAZPGTjFohzp8XC3aFlporVPKfB1ntig8f36SH2gim5KW2qblbRc2rWvb0%2FR2k3Zzkg80kSvOzzS1NbVLsbZ981YtWtbWpNXXKTx3ftK%2Bq%2Ffuvj29j%2FYN9%2B7tO9j7Qp9%2BU1KzrPdvvdzmpM7Tt6t3sI%2B%2FQj75q0mbrk%2B3VneBbo8uq9uhu1%2B3STZ89Xy6waO3vWU%2FbMQ5XEx%2F2bV9GNa17yc%2B2ECC7PpZRFMvzMRmbS3K3IZYKoYqZQQBgjfYoHV7Lhy%2B8OCF%2FKZk1Sl9p6w95YJT9pySPaVnbd%2FatWsvWLtnbXbt42sHL%2BjZ%2BvjWwccef2zQgePauWHnAzvv4K84Hw3C9vXSmshAz8BAzwg6mcEB%2BZQr17dJ69t8IwhSR2wEB0bzjSy%2FGHPFF0F47Q0l2fcX9PdqW1ql1jUt9BdrFW%2FJuepsGYbuDN%2B5IEbPwjn0XJ3RnLuAhX3ntSkWwBa%2BDnSIV8TSQyYJjCgVz5Taz2lRc%2FqTvNkKiEJbW3CooqItGm27sn09S750JpzUtbavP6uF%2Fb7zyT74LmL0Hkz3YnoE0%2FcxPYrpGUwyJgXTfkzPYnoOVrZN%2BaMi9n8D70KfkQplbmRzdHJlYW0KZW5kb2JqCjU0IDAgb2JqClszMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDI3OCAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzMzIDMzMyAzMjcgMzI3IDI3OCAzMzMgMjc4IDI3OCA1NTYgNTU2IDU1NiA1NTYgNTU2IDU1NiA1NTYgNTU2IDU1NiA1NTYgMjc4IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDY2NyAzMjcgNzIyIDcyMiA2NjcgNjExIDc3OCA3MjIgMjc4IDMyNyAzMjcgNTU2IDgzMyA3MjIgNzc4IDY2NyAzMjcgNzIyIDY2NyA2MTEgNzIyIDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgNTU2IDU1NiA1MDAgNTU2IDU1NiAyNzggNTU2IDU1NiAyMjIgMzI3IDMyNyAyMjIgODMzIDU1NiA1NTYgNTU2IDMyNyAzMzMgNTAwIDI3OCA1NTYgNTAwIDMyNyA1MDAgMzI3IDUwMCAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMjc4IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMzMyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDU1NiAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyA1NTYgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjddCmVuZG9iago1NSAwIG9iago8PC9CYXNlRm9udC9VT0FSQlorSGVsdmV0aWNhLUJvbGQvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL1N1YnR5cGUvVHJ1ZVR5cGUvVHlwZS9Gb250L0ZvbnREZXNjcmlwdG9yIDU2IDAgUi9GaXJzdENoYXIgMC9MYXN0Q2hhciAyNTUvV2lkdGhzIDU4IDAgUj4%2BCmVuZG9iago1NiAwIG9iago8PC9UeXBlL0ZvbnREZXNjcmlwdG9yL0ZvbnROYW1lL1VPQVJCWitIZWx2ZXRpY2EtQm9sZC9GbGFncyA0OC9Gb250QkJveFstMTcwIC0yMjggMTAwMyA5NjJdL0l0YWxpY0FuZ2xlIDAvQXNjZW50IDcxOC9EZXNjZW50IC0yMDcvQ2FwSGVpZ2h0IDcxOC9YSGVpZ2h0IDUzMi9TdGVtViAxNDAvU3RlbUggMTE4L0ZvbnRGaWxlMyA1NyAwIFI%2BPgplbmRvYmoKNTcgMCBvYmoKPDwvTGVuZ3RoIDE0NjU5L1N1YnR5cGUvVHlwZTFDL0ZpbHRlci9GbGF0ZURlY29kZT4%2Bc3RyZWFtCnjazXwJfBzFlXdVH3MfPfc9mtFII0tjWRrJurDsGeswtpUQY3PMOIAlWwIbDBjbGMwp7BCzslk73KchJGE5Q%2FeMZUuGOJMgEhMuB5Rrk2VzGMIGsUkIgWBLre9V9czosJ182d%2F3%2B307PV1d3VXVXf2O%2F3uvqmYQRggZUD9iUfJLq2rqOv5y%2BWdw5XXYL1h3Zc8mFrEcQrgMzr%2BybtvW0BZxewzOn0KISV266bIrV%2FoP1SLEkZvcddnG7Ze%2BMfEDG0LCPQh9Yev6vp7eH%2BxY%2BiBCF2ugQuN6uGDyc3%2BF8yScl62%2Fcuv17E0OI5z3wvnYxqvX9SD1R5C9ZAUkf7qy5%2FpN3OdqC0JrSP3QVT1X9jW%2B8aQbzrvh%2FK1Nm%2Fs2VbxS9g5C3dAHPIEwO8q8hHhk4h%2Fi6%2BGKTzmyP0aXMlYNz%2Bg1KlbNcAzp7ozP0hWhEILtJMO%2FI5%2BL69UZDHdFdxcrYKCPQikOXQ9HLxLgig7aVKAWdDZahlagC1AKbUCb0DZ0w%2BQk6S%2BUzUVJKPsCWgllPWgj2kzKJn87%2BR%2BTv5h8Z%2FLtyWOTb02%2BOfmDyZHJlydz9An%2F3z4CUmfUGdV5%2FBH%2Bff7bfIY%2FghB%2FL%2FD2p%2FwfuCR3MaNmOfw65JLsOraJ6YRtrtJy8vjkv0%2F%2BXL5ArparmfNwlD%2FCXM%2FfVSRdPS5hdnCVmMf16I%2BwT0AO8rgKl6BDuASOfvQX7EXfRTdjExpDP0MvoNdAIn8Npf9kbVyR34K4Ao2iHHoFHT3DVR79Cf0Ntg%2Fg%2BDv0MPo6egDdQ6%2BOwTbj6v%2BQoBX%2FY1a0%2Ft1S%2FI9KcT1zKS7DVZDq%2FtF9kl3Lly09e0lnR3vb4mRi0cLWBWe1NDc1Nsyvr4vX1syrnhurqpxTES0vi5SGQyXBgN%2Fn9bhdTofdZrUIZpPRoNdpNWoVz7EMRnNFR6x6bmjJwMAysX9td%2FVcMdiewiKODQxA0hMS0YqUiLvT60U25gsrnzQ0ENH5KZEptzRDbmWqwRdON%2FgKZQNt7YKE7AOb7IMYMwjb%2F2RrTdVAFZs9JBknB86PtK8OiaZIW6oz0tktsp3dkVRfWuLY8kj5st5IaGCgt6d3CH1vRSoinEXumlaenM4yGCOnMBBq7emLCK2p0BBG54um9otWQx%2BdQijnC4cy8MhlEWdzOkIf6YMCpVtdq1JZDDDiWOpYBm%2Bbf7X%2BteslDjkizSJ2ZDDiLLYmaJEuvEt7pD00oGwRaLUcDj0Sx0cjUXjPocnJ81LhsIh8UL5kYEn3wNkDZ3f3iKF1lEK4O9K5oaN6bmdkSQSeFu0RuWhk6VJ6oRvOuwvn0LNlIbF%2FZUrqTw7Am629eYM70rnHF%2B5Mw%2FM6IuEO6E5nR2cH3FZ0Q21RTdijjkV6NvRBjeZmX5ExnVKlPRZpC1fPzYSMuC3S8SKghIDsiOuokkrDAx0R6FHXuZGulavhZvbOSEfGju2d3XAVW6yhllANiiSq5w6DyvV2npuKiN2rUqFOsXt3h4%2ByAR4B708YFekB4oUGUsB%2FH5AynSI96aNdIa%2FfTggwEGmD%2BsuqYjH3MMJ48vY7D2KCygzq6IAnphgfm9ZYrC0I%2BoJ7QHCq5ypvooPuLOtdB3WQciBnAwNwMrBuD5xKBq68bZfobk%2BJISCIoTfSu3soidZGOkNi94pU2BcSH4eD2L%2FOt0FM9nSn077OtBSoGejwkRRIGSuwzycGYqTDArlbWKpB7YEb3nOLVTEfSOLyYaYfJUVf%2B%2B49686CWmHSoZ5ICt54dxrKm6vnRkIhYCTwtEdEe5rpi0QI76FEDMfgIJVDaadOqpgDB331XIMULSd8MEpzKhR%2BpL5KKLscHrMuIlCF2u2L9O2B54Ee8e2kl62tPkLg48d9x0fSUFAppEK5Gt%2B8NKTFaq3hVl%2FlcdJMjHSm4GSMSEy6M0BlTEz6xP60FzqEWKgcEtSm1rBPCEfgiQoh8Hkp7ENp%2BgBBqRUmog8VW335WtOq0XpHkAb1okrYknCMdW7r3ODe3dYe6VzbltFqKodAjAbgtdojPcBcQIwU0KszlBI17etEbXt3uqNzoHMgtRZUiEh%2BWhFfIkQkR8UtPi8Wy%2FbVVMbbqsQ45OpIrnru4gTqQzcAHl6GMU0JNl6GJmkq03SCpuM0PUnTEzT9nKZ%2Fo%2BkoTd%2Bh6ds0%2FTFNj9H0LZq%2BSdM3aPo6TV9DaPGf4PgjevYqTY%2FS9Ic0%2FQFNX6HpCE1fpun3afo9muZo%2Bl2aguGGt1lHU3RK%2Bm2aPk%2FT52j6LE2foenTNH3qDG2HaTpE00M0PUjTQZoeoGl2Wv1raHoVKGkfuoLmL6fpBnrlMpq%2FFNJe7Kapk6Y%2BmnpISu8zM32ApvfT9D6a3kvTe2h6N03vOkPbb9D0CZp%2BnaaP0%2FQxmu6n6aM03Q49nNn2WppupekWmm6m6SaaXk3TK2mrjTS%2Fnub7aB7uochrSpzvE%2BvTsekyGckgyIFpAI3o7PCBsarqXAfYsyEysI6AN1Ga7iFU0j0w0DOEQj0Doc6B3QN7ILs7AtK%2FoadXrOxcQYA1tLsj7It0EE0KhzOVuLK9m%2BhJa9dK0FkRnZsKR4iBGSDAhlYODKwEU4dABdNpqBbqJGoboXZhcSwVS4kLYvBVjFfBWk9ZBuRoJzcIiU4Kyw5X%2B0DXqtXkPtDd20jSI7IrwaxTAAsJ5MrKVAi3hlArQbWhyY%2F86XQYMFBq0ZRHYikEADcgaUkei6UkC5Yx5pPUwaq06GyfVq90qh5pojtTPVo4dT%2Fd37ufbup%2Bs58LhO1fC7j2RkcEfITVYMdC4p0dkXRI%2FIjmv0jz%2B2jeSPOhFSSPIE%2FejxKaYiBHYBXQbwTQrnouYPVhZAE38ZzO3pVth5EVNaEVStaG5qMvkeyLYGyXwrYc0L4HzrNVLZUtnW1EOHrakvW2ZnsTs3SBfTmEIEvYpUstCUuSWdZhXYG%2BaDnH9iVuaZe1ES1Ci1EbakcNqqUtfCeAHcHMUCokVravFava13WLQQLofPm20HroO%2FAr1D2wZ7c7nRb5aDi0ARgJKNpBnkmsyTAqQSHfMIQ3JWnK5bmRtm6xOtImLqmixIq1dw%2BE2lenJPB4BnZH2vek4U2HUC3gNDHL1AsZmvyzX3EAhtB9UwVt0woWM0S1MgzjqiJCEkIdhyHgwsgcSy22F53YJErOOE7%2F6JAp5jsMMZvuDI36i42nPgalkREy0xsl842Sp21kVBqZIXO6JyWndXPqY1YaCZD5R42mjoLSyEKanbZ7p2tkURrZIfOPCDF1tCuNXJCZ2aj%2F7zRyKY3ckJlqlMxTLXmaRuTcDY3ShU81XGRICdPCnYAYW404EcVKcf5DriM08UMCqRjMC9RBJ1VwvO8kGFauDtqCecKf8IfBlTCgIYhIUl1iEJxzdGdawlpw8pNarMFGFJxEGKpmtGYuJmG9qIXrBmzChet6gYuhrDOJdMt3d2TZFkNXIoFFrTAM6oRzgWGkx85x8OwG7Sjcr9cui6wFbx5rl%2B%2Fp7Rjk7Xps1wrHheMZNeatlhYwjSp1yLuw5uhRtxeRWxgEuIUOe8gtoIMrU0Oo35vOJJFzzBuvTWPWUl9vYSNxddgSxvpV%2B%2Fev%2FMKDuE5%2BE9%2FNfB%2F3yQ9PLNos%2Fyeow172HpyCWJ1FZ5N3lTAqvCJXfEVEX5EREX1FvviKDH1FfB7bkhjDGeZiJIzBo22WesveV9l7mKqJn5Iocaf8b%2FggrgLh6Zr1BOusJ3D5J9iKT%2BDgCQeY89RCF0qMjmW5i7UICyPkKU0N8H52lTreEG9samyKRyI7H3rwvkcvK%2FPy51x0773y5GPfiV4VaZ93IfSgCR9itjCvTL0je%2Bo7srQHWGRnvSMm7yiR%2FJYMy3QkcuM58o5A1SamdWKEeUX%2BmLwjvOggvCOLvvg%2FoiIJSDwSC285NowY7BThNcfyz9lJ2VYl%2FxThye9M%2Fpy5iX8fnpNWnsNMe446fz%2F6HCQyheeY888nbzKI2BYGdyVyiRyWWCTkICF3Qdvh7fwxlogOjmDmJrl6DL%2FDv%2F%2F5xyoT6ETb5L9zcT4D%2BOSFzs2koW8WDbV5GvqLb6glT34R%2BcFf9KFDWZUxqI9Jxi8lJFVzYtDuV5l8RmFMGFt4SGdhXRaXyghRWEvNSM2IG2dZVqcVcsIx0vw%2BaP71rJaF5gcsfp3LB41wvFYSzoodRvMACeYdTJrsSYvLF4uJjvZUVqWz2IfQvKQBJMVu0bXCrmqOpWkZq7K7aJkLylzsQjiwhTLEurxKmRd5Xewi2FGhTGc0WZR7miCnS8BBB2VpPsSwVms4ZLU4rEy4oa7RYo1GSlWs4KxnWibRpxh%2FihFmPsWfyfKWS%2FCaLZsvxpdsjuIr8Ha8A2%2BW%2F1W%2BXS3vku%2F94H3sxiUffij%2FRv7gfaI%2FdyLE6oD2JrRJobypQHkzCucpbMqDkIlSXsDh6SAkGbsSkrY5IbFa4VfDiEPXjEl6uIk%2Fg43RGJZ0%2BiG0KQvQZRJyu9zjbiIZoGUgCmFLPNzgBPWqj1tAw%2FCH8hNfvfyKpg%2F37mM%2FekPd1nbjiXNIDxsAOV3QwxL0vNLDIkyGZsGkJg%2BT4aJsaGgPNdBDf3Mio9NqhNGcpBOah8GJeT7jV6mHwTfWCOM5YSxj9JP4PKllkZGFDZdDS7dfGMES0gqjIoKoeVRyufD30Pckl1tlGgYzcv9HmNhI8PeFsaRWhbQq2FB57Cwi6YAfKrU67AjH51vLGxsb5hOOueLheBTV17mcnOvhn7yyFDO3jAd%2FhF03X7rporVXcfLv1Vj9%2B5%2F9yx3sB0%2F%2B%2Bx039zHjR3Fw5MKvpXu23XPrR9jZ308QB%2FQlDBSZj36hUCRYoEhDkSJBShGXUmDAjUWSuChJDMAw75cSwyiChkWvMJa1Wb0GohhSVVVLzTCKo%2BfHslXxqnjGazCA%2FLMQkxmEcSnoFUYkjQGoIgYFMTgqlZRQgpQECUFK0AMf0QLNqKRW0wK1hhSoCaWAeMKxYRRFD4xJ1UFhNOOyWXM5AezLuSlxyba0aB51kxjufoJNmFJLDdRqbKyvczrsanXcSYb2uEhpWQVcdjrr64CmFdGKaFTd4CQw3cj8bO25l1zGyH9jTx4fObGjoi56s%2Fzn9z%2BST7LYxVzRt%2Fb61oZzq7%2BoN5wzt2NOHac9f8N1V%2F3nn49nH17ntZX0vPyz8c%2Ffv%2FG2HZdf3BJXsWS0nuU1FbWAT00ggY28BK6QC180CxndRYoryGgsUNxTpLiRUpwHioOqJLUuQTCC1bXygibHAclrcpJWnctlWGweQlePQy7JhrxHvZLOkMsNqlQ8x%2FFCjnywpDIKo6BOnZKKW7YylUlypKJP4hghlxaNQlZtUBnVra2idiRrFcwmqCoZVUTrwKnH8ASolxuehI%2FkYofQNRmO9QNWqjQZKxuIsRhnuq22ZaHegVUpOjYDVn9tKGO1WjSR5ozN5oQDHRtLo0y3y3lKReAvqeh02qYqivwS0bpkRUqC921NkzMbObPRM7xEZJesTBFAaE0TpocbGjBY3ni9IxK3hC0NAAxxrI4z1098l%2FE9%2Fvg3vym%2FLz9mwItvuom9fnzrcfm7ePFx5pKJg%2FaiVtyLIvjLCo%2BcBR6VFXnkzFsvJ2VReZFF1HpJ7gWAYkQpDKAUrDB2gHUHA24gmcUqua1D6NsZRygEpmNcED03j4CseoFfWTAd84MluaCkZoURhnpLZPwJpSWrYdmqlHIhRwakpJB12blACYPQKlqV6%2F0%2BMZluTqenN%2FOGTtss5IVmoWnN%2BqBdBjnUUFtSO4TWTNLqIBUdyWZfJqk2kLw6SWqlQfecYI0d6pGc5HdKASdhvj%2Fgj2VcAWC86BRE9aik0VCV1aiJymqILgP2IaKzZtBZ0TNCUQ1FSqPTFJCqaYUtTnCOjTdSZAvLv%2Fnry7IDh%2FSXX3TJ1m3drZf5x9ewgQmp7vo937%2FpRvybP8ofvDfS03fXdTfc2zjPwLr%2FIr%2FW9ZWvEN86AVys5LMoiGLYrfDRW%2BDj3CIfvZSPdtFL%2BVhd5KOd8jEIqub6UgKJLvCHS9HQWNZicQWJqg2jOQBsJFR7HgWFMclsAapwQYAz4gHcDx7AE1m7FzwAwMSsyVTiIwYL9M5uF45J5f4x0S6I3KjE85RQPEcIxSMfYJupRMihTJizgF5KXLI5gyC7imbTINqSe5oXUTqnv3xOwYuw2MvnUItfPscyp9zeCrul4A3YXaXlSlkp5OwJONiJN4DrnMSONEYjkSjYGEJ1q4MwhrEITY31rL3IGrWKuXTwA438Hj63w3Dbwlrd1m9%2B%2FzP597%2F6dPLOnZ9%2FeONFG67a1tOzYAGfefvZjpX45p7DN8h%2F%2FfG78vv4GrwO73pleNy95YE7dm7evaQTNKwecPAs0DATukzhjGrKVyhwQJW3xKq8rzDTEpvAEmtAy9QaIZdRcbwCeDhj0sGFEYkzSYiAVkZnUrzFsNVmbQIvIexwqFgGACp01x2vTVRz6nf37PHjpXgxjr03vvHKj%2BUj8ndlsB1s0Zf0o0rUiLcq%2FdQX%2BtlU7Kee9pMX9bSfzcV%2B8rSfTpAgjWIen5OctcPoEHgJ4A8eqqoSzBg5iadAPiBQAXRojMmgqlrgdqY%2FUEW0rqpWaJWqklQ%2FI4FpBZEAFASoTuKsh7fphRFh5EXAkfuQB5xPXg%2BiN%2BWLemxwmjSF%2FJ6oz1bv5as9To1e2DUm0G2mUPVHoslQtCBUTk8oQgUnFHFGQp5W2J0FobI5I1FaFoWyqG0hHGxQVrxRbXWyvrpwI42%2BupZWrq7V1FbrF8GuKdyI19TW07J6KKvnE3DgqXROGeqG%2BSR6qog2zM8bcVV5ATMcdpfTRQ4FO85%2BsbZqQP7wd7%2BVP9w9p7b5lvE33xy%2FxSe%2FtWrt2lWr1sbjq7o3beruvqalmVW5S685%2FMaJE28c3lzqLF%2FzcPaXv8w%2BvMZ%2Bds%2Bq83t7z18Vr%2F3pTRsu2779sg2LFgGiLJz8BVcNEkEQxavIg%2F1URLFTefCK9lmI4qXy4AJ5CBJECQKiAHiM5aHleQItQVcBWoZOjyheOwlJ%2FiGG4BmgU0AUC6cgSh9ACuRXKflTMGVOaX95aYFrs3BjYR43Toc3i%2FJ4k%2BYVVFc56%2BuJTx5XAZo0zAdPFVkEtSpiyzMNuAV%2BFnvovyA0w6s6Tf2Lag3Xfevlz7H33c8w%2FtevnPjvmy654urr165tbY1STLmp56Ubsf7H72KvvFd%2BVN7%2ByjD7QRFTGPTx5KjqFxBzNqDFODLLbrfNstv%2BvN1uL3LHT6POuUlP9BxbTaIGvCtD0maAjfjgSW1rMtoKm3KiSnpUsCknjcm5jbCRk2yjqtVQA59sSbR2LijkaFKLkiWIbFAsohosVTrJVW3SqYVNuUNLsrIFNnIitWhraiBwSPpZ2GgjtiZTW%2B%2BHaCOpTSTrE7DRy4kaMZFjJQfqyKBECwWGlkoCDC0JAIYWYKwv01%2BfoFYcd4DhFh2dKSnRAoUJgBNif4myANIDV4A3ZU31Ki4SIidhSIkJAIeY8C7aBFEGWImmeD2rcuKCojU1RvnFl%2F6rnPv9CflJvOKT%2BzG%2FaDAlP%2FqafEBuwkfxchzH35R%2FcKwtZGR7t33tJYwh3LrzPMcPb1fp5Xe691244tydTudq%2FOZ7T76B%2FfIj8iRsj6xd8dBL%2BGW8BLaX5dvl%2F35ffnKBy3PFT755FGKxG9dMPOeJ4N89temCi2%2Fr6%2BsnVr4SIX4%2FfwRCJxdWUa4jYcqjLs1zVyBcR5wo5B3q0mnjNkhiWhIHXU4zozJ3JUYIHg%2ByLIOpiwufYaRHfYM2G2N2GQla14yhrE1v02fMjCE2qNPbnWobOOCgYpJRoNGqORo7KGCyOWMHjXqjywYZSc3RQhUUcixspFCtUzvtkMESFoRj4K7Xen%2FlPcDZ7VhY3nHDJ27JJZCbog0ZLICLpRcCsYwNElZycuSGaH2G5aBAx0GBHRJi6urB8cUQBoMTRRwpiIeVTCWjyWQY7Te%2BsW7i7Z79jzEHX8a7RvgjJ9pwTP4Jd92ExHyJ2L1HgKKrgaIuQKVqnFE0yVWg6TwU5vPhH9Uki%2BiiNK3BQYNy3UJp6uhKHLA7XI62mFQeHUZZ5IDYWGJbEgd4iIjgaml4GD2FWLiara5mHQrwedFTY9lqb7U342ANsazPO7daAMoeMBoM3mqoIuS4DKouJ9hVTrArg0q95KQURDobLQ%2BXkto46%2FZEyoZQbxa7sCs%2FVpDL8cMA3HdlNRpL5RC6S7JYsHBsTBg9NsZOd5mDnlkusyZIdCjoAZc5eGZPu7JsVjMLVcbKMmhWObPZLGsbDMO3ALe8JhimkBoM8%2BGgphV2frpFrYzCd8rfi1bSypVRyFkScKD4S6KeeFgxkoVQNxyOR7AFrlKbGbZMZR%2B5m2n4yZ92OX3z59wp%2F5f8ycTf7sZRMuxz%2FXb5eXzt9dffeKP8Nf7IKy8cWiMY7f6ew7%2F5kPue%2FJ0LLuzqmviBfP36DRd9mYytrJ78Ob8d0DeA2xSJURckJoiCnCIZaiWuFdVUYkqKEqOEtdwK8JI8KDsGiiqMSLrGBBlbeWpMMunI0IlaTcIHJ3pU0nG7xjDEqCIalQIBavsCiNi%2BAHr0I1pgGpXMZlpgNpECMylAmZCZeFBZlCR%2BUx8BwelDcv02J3wLxFXrbE5KXKcNcuqFcFAXDB%2BndnqUMg%2FncaoXwc5N55LZBt%2FCjXQms41WNsONzKZW2HXF8TvOE6BlAbhRACXggBS%2FR0VRmGIxHecBk8k47FaCu3F%2Bu19%2BWv5M%2FpF8N74SN2IVTgXl180v7Hr2%2B997btcLZoadlEfxftyD1%2BN%2Fk9%2F7%2BLo98h8%2FHpP%2F%2BsjtgJdEu28E7TYBqU%2FOGqfzFnXblPe9lXE6X5FTiu%2FNtyQkfRfVZL4tdkBvMOlBod2uIfSUyBM1FyxDKCvqiW4LFreLKmXG4zZTnZwav8vxGCJFwlY7KCQ27RpjM8hupsZMYyf6YzeDvbLnA5%2FpvHLZ4TulNfkhUruLd9k1rbDzBRJr9PmhVbsFcpoEHDRTSqLQGHSjvKgP8UcexL8COFsgD8sj8sIHcT1mX3zx2y%2FIx%2FkjYKWelr8qXzcxMfHee0TqgZrc34CaHvTZrPH0KVoq4%2BmO%2FHj6FC0ddFYCdYH1SYju3GHkICsFhHE6BkOmdS5FjDCeFQQGKeBoRH1wahSMGQSW55DNYRX0RhLuKBsgo9VG5NuRtCnynUE6PbmgSeqSzc2FM2NSrxRD4GDV62yEKQxIxhRQZsycAwsjI5IawrISiD%2BjMckIUhKCd4lSRuNgBjmiMWJs4gAlcYcDEsgUjuTCI9xZ458zb03E2Ur%2ByI6d8tFbbpVv27EzTzWy7tKG3p1FNfssqrF5qjmKVGOVuZwWWxdQjQU6qYFOGOgECm9T6GQgdDIbzIaMDYOFtph1Bq1aoZFkRdQI26IxnLXotFYqkdClqZfH4DizlOKSiqevz8I7G4z09U3RGMCIiozG4Ayix0zSZAFRTWrNJhOYKYM62RdxkZESQpxTaRN5hHNOBJivTNz0IKVK5pZbJ%2B7K4%2BcBwM8oOjFr3qKiiJ%2FKvIUuP28xp0gTHdVKVWNCQisgunAQc4uEsREyU%2FsUUgljkk4ljBB8zIpkrCKEhBGQhiCxnCAZaSyxLNFCK4CrCu2C6r4hyJbohNExQQyMiDpB1I1Kej2FVL2OQKqeQOospTRb4VtQSlZltlLFs5ohxy6Ew7R5C6tDKXMgh5VdBDtS5iZOh3kRiBOaGkjsbolUxNWqMGuHGH0XvgOfA2T4ckB%2B3ZDbevS3v%2FgxzvQ2eeUnmfVvBK0Py5%2FNKWLgJ8e7vyZ%2F%2BOkJ%2BeQdX1zPToz%2F%2Foj7rfxs2EGQQ21hFKI4H6ArzliQ%2BQA8jFRETrRYq8gJzpLh%2BcNk%2BBMZQBw1XeDYJCQMHl4gg4m8qLRUzjREzjgVaSfkFIGoV%2BYodj740IMPEecrrw%2FPQD%2BM6PpZ%2BmCapQ%2FavD6YcdA0bc4KUIQjKGLISWrtsAIjOYnhaXc4MnfCq2epOZYMeqrG2qIa1xd1%2BZEHH2J%2ByR85efaOnfmZHVUJ9C%2BAU7NkM1jsnyKb1rxsluCwUbluJf3L2BnwrCU12A0z7N6uxKDbFfAzamXwY9Dn83o8XsXdzup8eg%2FVVV5D%2B68m6CPQLEEfxFLKMpAFISJZe1ThUDIrWFl%2Bee%2FAwPLePSvbMpoAGkKXg5XiVazQnmr1jaQLZidjdDlImWAVVNaZZRmvlYVuHdJ7dEmfx6vxCkd%2F7oXuKIPdfg0Z79Rp%2FDHlnGjKNRmfv3DuNpJzvbFwrveQc4%2FbH8NdonsFBGOeZZIPLSMLXkOSxzfQHKHBWJj465QFOA7fvJBAgsF9j%2BPn9u77zW%2BArpp33927T%2F7o%2Fvvlj4A9W7g7T7Rxoyfncn9UuMS3Ur%2F97Vlccp%2BBS56ZXDqkZ7rMLU7gUi6RyEmAsIN2u81qtSmMmc6PaUyYYg3OAqlZkC%2FJSQhPyClpnGD6JT1Q5XLgDmxU%2Bo5hya4nHGY1CpmcGhLK6IGskmDNX9PbyTWH3R%2BbTR9FVAlhKGW%2B%2FnXmnL37Hnxw4gmgyeecGmgSAUEniPqfgKgG5DllLtg7C1MLc8FT1rk4F%2FwU8qFsVmUgAy8GgFeA2UGbX2X0GZS5YK3AOgWnyjBjLlhbnAu%2BC5o%2Fmp8LFvxa5%2Bnmgo22pFB0PFVaQfEXBZvKJmhbYZ82F1x0SiHHglM6HVOLTinyOFlwSqfmgrUGEkSSexohp03AQTsDb0lqqbM2zGfymMv65ZfkHfgW3AGR%2BM3yTvnwS6OjL704%2Bs5LU6ialh%2BQH5PXYh4b%2F%2FgJtstjn%2FxR%2FgvK%2B5gcSKIVEMOjUN56KmJY8z6mdVY0oPiYlq7E9GjR7ytGi3Y7ayk4RNnxrN1oN2YsJFrUG%2B12ChxgeIh0WqKkHZyTy1gKKO6nFeeFMJc1Wk22XA6Ci2HkBgeUuKLEA3Ur46waN%2FFA3WSc1X2aoXe7r9%2Ftm%2FJA3T5KYLeP97k1rbBP80DtbqXMDjlNAg7TPNA6joxvRtXKxPUML%2FTNk1glnziZ2fHgQ%2BCEotvgM4n4Ix%2F%2B5r8%2Fw%2BzEX1nd5i0bNhTofQXQuwzV4FqF3mUFetcW6V2Wp3cZpXd81nh6ZCa951UX6R2NshGF3iWE3tGSaEkmAvSWolEBaGiIltDZXmcEgrZwGaV8JBobBnf2F5KzktyKUL8mTKlfhssK1C8pC5XnqV9VoH4maAB%2FNpdxWslBtOQkr1XyQTTRm7FY%2FTFWAp83ByyqqlFYpIyR1xTGyGexKFrdX1U9xaKqasqGqmq%2BukrTCjs%2Fkw35aNnlcjpdjnA0WlERJZw4hTHMF97GeG%2B01ue8hVcz3nL5eYMVM8w1O%2BU3Kad6r7vummvkffyR3wyNrvVa9cYu6c9LW60VrvsvvGPdeRMvsq09a3t6ALFXAEJpAKGqGZOyfsteWL8VnRpuKQBVlLLPLEZnDbeYydKjfnO0Zh79hcZ5qQP1%2BZPlPatSwyg6mQPKIMkK2FXZmDhM4AxZwQt0WgXwEDlgc6Uwli3VWyspH6srKUvU6NEDjrJKa1TYlRsZx1giLrTkdAg5MsXnnFq%2F4FTWL4A3SMdXDsw1VlrNtJEYyknVc6FBtSBWj0rgSZAGqJo0QOgu0iCJejs3rEoNoW7JTNzRrL7UHKUjcPpSsmlUy0SB%2FGZCLI2QgCWDg27itQbdwPIgsDyDEZ21RGq4gOACuOelLnInZ9KVj4JCiMY91UkonxX0Z%2FuDPm9eQMB1DSpjLz7ImRNwME9F9dl%2BNVeoGa3k1LSmmoNcdCEcotPjf8TBtyB3ldWIo5URVEbVrbBXFqDB6nQpsOGCR7qci2C3KvF%2FtDixwSnjNyE0c6lCGZkKKQvXgavMDmue2nXtVzo7%2FgMv3rhRPiz%2FQf4v%2BQd6PId%2FfO%2Fd31pw1hH5iW3X4kas%2BQw3Wjh%2Bw42XXdRWVhZfesnFb771F8z897W3rDlvVUVF1TlfXv3dn56Ux%2F%2BD%2BHwB8EmPUgy%2FfxaC24pj6VbFNzbPgFUIKs1W8zTfuAWgPHFAo1Fp2mJZgwmzwxCxkZUyuelIjTMqcocRyWQWchm7VbWsI6OB8P%2B4xCpRmArQXK%2BjWQ11Wcmin3hDw1SEBWr6C7mDe1buePAhbnjHzh07T54Nb3I3aFkj9Yv%2BOGtkasovUkamnPmRqSm%2FyEmxUQs%2BqwN2dkWChFRgakF91Drq94D7LIFRJlkHXaRkALA6oFKrVKAvNRTpJBsNM5ETOYVj9AqfQTYdQTCrTSe0tmatELE3nzIeZTbAdyqcMpipqBjMrNmgWgT7NNNfDLUghxJwUIaRrMS2s4q7pMyO0TzfiEFCXpHflv8DP%2FjQ3n0PfQX8%2FSq48F84gmvYe8bX7t%2F%2FtWfZ%2FUQOKsC21AD1zOivyuh6MTYRUFA1LTZB%2BnxsYimu5NMXZmoHdUyLijcJr7%2BeyHHUeQxQ5%2FEgWROMyEA4S1xHMkqu0dNCHRTqjbCRQhAdlQYyg2azyWg05ZewZM1GPaKQZTQv6xWjK1LZpDFqFrxHvWmJRxKLiP9oRsRsqPTkxmhDxqiHU5zpVvGnrDlRqTRkzQkPTmlhzQlZ2TjdJwdnvOIb38DtuG3%2FY7LprrvA33yUW0djONAXvoXGSPcodApMG%2F%2FMr3gMUDpZxUDB4zFN874HjXyL3Q9hUmIkK2A%2FT9%2FMG6D64QdqBDDZgBpeN2yEVFPhz0GHCzZSSFxsKylkVZSOPBSyDJnwhkJCRhUpJKvqAnRV3UG9FjZSaBJgI1MR8HRrgD6dDYBJN4GZUBxyFUsIypCFPoJJk9HQhT5KEfSTzFEEFP9dWQqSL3JYSSsBrLeEA1BknSqCbkArwUSKWLihKb9ySKs5hTlarZ4wh6BIgTlQ0eU%2BzcohB6nodnunKtI5kTwHC5yM0xkSehqJBPCv5I7Bg5jFBkmSk%2FjXh4bkX8vvDh5kPmB%2BKpfjX04snsD4hKwi3taCyeNcM3cxqkRn4dmR1oJZa0wD%2BbiiFQeFvAwUvS10TkLyJGFvGCY%2FL4JoYhgJ6OmxAyptTQwJo7ljObJUcE4OYkbgQym4VpG8a1UOrlVZJWTCkCmtjGWSNQ3ECtY0CK2ZflUN8YpqyNqd%2FgZE8g1kOnBQm1Ql4WozGZziJDuZORS00GzQjmuSWmosOySV9vyUpKLzwqK9MyXNJVkA%2BTr0%2BAF%2FwCNAfJfbNYbJKNvGrD%2FgBxEhkCYacmJsVKzKiRXwPcaKrCA6RsHzpjbf7iA2344eJza%2FHH0qRSoE%2Bls7EaXCI77j6TR5lU%2FJooaZlzHtZjhWR2au62Lk7epY8kZ1ygRnIEZHiZUJTuhuJlZVRwA1lqzKA%2Br0qZa6hv6GugKgIraugYJmXQNqqGMXwY7ys9cN8xubyCLARrIkcMbaQUBRl8MBggPOocrWEFepAejzU9lNjXjg%2FqEnH%2FvO8rjDxaoq4xetvIiVP9J%2B%2FIfXPnSXPfywTYV37ahbeXnXOUsSAysFvd7%2F%2FJ33Zl1enVs33zfPW7Ji2XePPHvPxOPLFlhwRVuCeRx%2F%2B2a%2FxW7qalm8vMdWXk5k72aQvceoLavAX5o1MzcHlWqnz8wZ8jNzlThoUa4bqOyZQe60yQTx1p6RtO5hNIjMJJSV2IaEZAfBhBj%2B55nklKOV6dcqc2BUotxmGhYpq8EkxAzhMB0ixVmDAZGh%2FmPD4GLeIyEXmaVxCSRDFvNuJFJiy4l8TgyPQIbNOkAiOkAIfWHqzBt85Ma%2BMDhyvmQzYSo8LeyaNYMd9kF5ONk3m7lJd7DfFywwV2vI%2B3K%2BoDboM7TCri1YS4M5H6v53JAzJOBgIIy3KaMuTXQJlACBcEVFnDgWU%2BtM4vE4eGQ3792n2nrkrfHxt75zrUH%2BPcNv3bFjy5Ydi5P8kYlPNN%2BQ3zwhfya%2F9ICw59jzDz0qHXzi3vPOA%2FuwBDgXB9Q47fyY%2FgzzY8KM%2BbFz6PzYgcL8WJLOjz09e35s%2F9%2BbH9t%2Fpvmx%2FWeYH3P%2Fb50f4woLF7jCWHFhXUKciztl%2Ba3P5L9g10dvY4NW%2FhV3xXkbr7lm4zkbtfju147jKBawDrd88J1ze59%2F8omnN6ZAswh%2F1oNmuVEZXjML1ctRUDsd1YU8qkeL6wgE5dcXoFmqJOXSs5KqpADrwCHQLBtolgMJPydj8ccySU8JUS9PCQVsDxFyD1WvEgrYJXn10hupepmixK4O4SrwG4mmsaxJUDTNjx6XTALhtyCI4OFsJGdE0%2Bw5UZcTS3OiY5qmlfqppvkphPrtoEl%2BeFBe1Uyls1St1A8VSpOnaFqJp9%2FvmfJLPX7KIr8HcuxCOEzzS%2F0lSlkJKvGzi2DPQyxjmW%2Btr3O54sXx8HhDXK1ip1ZxRRnHsUn5reFrDHv37d7NX7d1y47btoCm%2BXEH1p7AdY8IjPFE22714%2Bxt0qMPPQ9q9hRwsQa4eBZomReV4%2B%2FO4mIUBTWnG%2FOrKOqZdjoXpSpwXA5IYSQExmKSC4HKVYWG0NNSWEWuEDt2RdZoRCoyNjKeNZYby8mMUCzrKzcYyTBUJhkqJ2YrVC60Zn3JcqpTGVRuJDpmSBrzk3AZZLQSUbAaoRq53FysRBoRa12Yn%2FGjuwf9eq22DPym%2FWSmhsQSwrFcjoRdolYQXaOS00n12ukieu0keo0zSX8Z6Yi%2FjDK8OZ0tL1O8aFB5g3N6sOzLX5Ccp1kZ1h8qT%2FrLi%2BOUen9IYW5IFfLrW2GfNk4Zyq8ag7JydiEc2OkAYDXBd8oQm5ToxWRFVhO7CPa8lCgKjixOYoCdLkW7w6D7tjg1zGTAhpn%2FN1B0618%2Bx0ZGPeHT1bTuWXy1HpeVy3%2Bx6Kqatl59Vpngx3XYh3lcIb8j%2FyEUvi25Yvn8J19inlvbtOz8xgtJlGMH47qbfxsM15giNXxBavzFNUQ8iXZFXsjwBkMOTBpDTRrO8gzPFMPdjMB1JWqIr%2FP0GFn5fIWkF4TR8axeo9dkOIGMUuo1dMr2AEIajycHOWWmJkJnatC0tSKSjaxFgVRoFW0z1pDQbmgN6lzudSwyQsaL3bmREclOxsysIxgrlbt9YigtWakdt7qnVorT68oSsMIsQ6ShPk5WVja6HARIHfF6B357777HHvu3Z76w4Mst5cPsdeP%2Fwl736pcffQhr3pt3dvxV4o2slNu5jaBtpWgevnvW%2BtiaImYq62Nd%2BfWxtUVvxFX8RY2zIyGZkomM99zKITQoaSLD6BlkIsiZICNFYPFsJgBOTkP8kkgleZ9IJQFOU4QuhaXAWamhC24U4OQyQHuodxj1k38xIiWIltCfnZBSAr%2FZkmRQ0UIMbHHmnZcoegzUh%2Fq4LNqYcbpcCqKC7zKXui%2F60%2F1c5TFQtCLOzo2SPkbnkpX0Uf2yFZCfwlmndRbOzo2CXs49BWf7I1H4FrREbyqutYWcfiEc9FMjzPmFQNFKTWVUvwh2OsLMA5ASWAVT2SAQvHW6AGbDFkEVLvzyxTYdcTdtv9y1JjMqn5TfOtRnE7BpUv74XfmEFjdza1ZvuuWWq6%2B59dbfXrvrq5GdOH4CI9x8uyf42MkTsvzX5x9srufOfvaBe%2F%2Ftmfvue5po1O0IcZeAPS1Bf1Yko2Tqt1YFP7UkP6RZkv%2BtVallakgT%2FB0wmmQWQA9YHOxKkJ%2ForkN6sKYavXBUAstCVCYYjVGIDyurFMh4UkQZT5J8YJc2ZsGNKsmPOEu8n6wAsJPLJrPJPDUY4ycInLFbweC1HjIlzUl70koCpWYSO87gid0F3ymeFNed6F12Uyvs%2BsKQMrFq1JeEoIHqWN6FJCe3793nuvxHz728d9%2BFq6%2FZvncfOI5%2FC%2B16fXCijHmqr%2BeO%2FomHiHbdCWSU%2BFeRFhkLqKSdmhcunT0%2FvfHM89NUybgEmDT9MPahKyUjJ%2FyKWDVCuVKFcmTCOjJrwhqLKohJr0SG0YBUpRvCbmii0y4rRtuX0lxI1EWaRQNE26Vp%2BMZr4eEG3dmRs%2FN%2F7UNPl0SWdA%2BQv%2FvpIaO56w9gjv6r0ECzI4sx4qz2%2BfZBrcmscvOxaZPkbNgS3rcXNhaXgReXPJnjX5U%2FVeY68XN05n7TKTP3%2FxRliJAR6kyTodNRIl47qNXpZ%2FXuTtIzOoWvrCVQPQJY2IB%2FrfSoodCjxqK8N1B5d4sNVN6bivLupvLuzTPJAPJuJ15kCK2T7NVDaN2gQc1VzO9KjEBXiDsJgSvp4XzobPkcmq2IzmAmBDjkqpfOyarjdVQRGnBDcZVL1iKUgYXeeLBqTtWcOe4G%2BuOxfBmWAj7SwOV2ufMNQEXq5hAVqZpDwutDFqG8rGpOUuUia4zKfaQk4AM3p5VM6%2FuSHFwH%2BZDcTYB0U5ckqBmS4EahgfNT3RekDtCSPvGUshWpgxZhTlUZuEwiPMN3KH%2Bm%2BmcqKtII39kqHAjBd2p9e0DxYgIheyjgaoW9uL7dIBRnjAzVVUIr7IYp9YZosLGpUQHVM%2Bj5jJOdd%2FyLLbBmcN9FZY5I%2Bhnp6N59K85bvWbvvvMuvPDivfu45Avzym7oKa9uueHtwxM1zK03XHv1%2BokNzLfXXkKORUQFCQsVEDVUkLBwUcJC%2BfgkRCWstChhSnxCpMvapUhYCUFUNyCqgSCqARAVCEGEpmSmKAk2etVKENUfoIIUwqEiogYAUW0OctksmIUpRA2QlWgZhy1AxMWcFJIOOsR9GkR1uOFbYIfB7FDmSh1ug9thboX9NCQ%2FHaTe8S8W%2B3RM5ZJfD3hPwVQS5T0GNDzDmgDNGdYECLPWBDwNjuKBwpoAJVz4Z9YE3A3N959mTYD7f8WaAMXtVtYENEz9xsZZz2yelN%2FB1ZMY47nyqDzZ%2F9Wv9vfffns%2Fca85ElDLP5Q%2FlcflN%2F%2F01Ne%2F9eyz3%2Fo6ichulS%2BgFHejCrx61uqmqRErlB%2BxQn9%2FxKqEjFh5pkasXOcmJBacR5uZeIYl1F0u8dARqxIljCaeoceshNdTI1alyqI%2Bu3MIQ9DmUgavIMTLh9T3QLhHQmokkAxx5ungFUe9v9KZg1f%2BUmXwyk9D6lIlwsp7ehw6fUR96uCVp6TfXzI1eFUMm7UlfkMr7NMGr4rhNuQMCThMDV45Zg5eUSXBM0avnLcCCm098tbJk299Z6tq7z6GI2H1lttuA3XxFYeuNBNvMzfkB6%2BeUmwbM055%2BLjCQXeBg54iB935sSs35aC3yEEydoUlo1sYQQXrNmiwaThXV0I4elRQIpJwIC1VgTSuG4tlTIKRrOEAHCC445qBRnjQYOTVdoo32I3dRXtGfBXT1AK7PFC4LDPg12zre3xxtHbnbQQdXg4Fn8DyL7kdX5l4jaDrQkCGCnjHvz8Xrz%2FDXLzwT87Fg0BXJulc%2FIEZc%2FFPn2Eufv8%2FNRe%2FX5mLv%2Fv%2Fei7%2B7v9%2Fc%2FHu%2F%2F1z8fNnzcWf8rcBZfmfJXKOOPPR%2BgtWXdww%2F3asSaflidGT8p%2B1uIy99coLempqtsljF1%2F851fexQJzIvGFlvoyrye4YMmS%2Bx5%2FOTfUdf6ChnKvx7uos2PvEz98%2BXE6KjH5CbOd7wO9%2B%2BqsUQlPcZ6JV7xcgKiMyc4Lio%2FL23n7lI%2FLdCWygsHckRjJSTodOH9qi9o6hK4Yz%2BWO0VX76wAUhZGxEXzAaBJsdiE3kstlNCqeTMfr9bmc5LaDHIJ28eTnYA3KcIElnP9xPViGemb70g0P3H7%2F%2FbjkhReqz16gUl15E%2FPQq7hc%2FuWrE%2FdF3fAuu0C%2FLuKS4L18oryL%2F1TvxU%2FVyiT6Z3kvJmoFQgD0Btg959C4MONxWACwydTCujFJq6cYYQC4CJTQbAiyJoGACCLhYDbgD%2FjJ2sQQ3HIjXYBkt2o1BEkMJoNp2uJ3FCKTYlmrRhsC56U1qTMlDRAOapJa6sCcEqZzGL4FCfP4sSJhmPNw2L8Idg%2BVovz8%2B4wBz7hKnSdofT1%2B8oL0puv37gOYclz%2B6nPfIxg1cWFv9x39TN%2FJ3DeC3l2vHWDoygwf%2BIEpoORpZ%2BR1Z5iRt506I98xc0a%2B7O%2FNyJf9P5uRv3LmjPxV%2F8MZeVs8bsvPyPv2P%2FbBH%2B6%2FX77u3vu45PhP2NhJ8g%2FTlQixP%2BPI33v9ctYvdR0KjE9%2Bnv%2BlriX%2FS10nDlqn%2FYIta%2BvSdiQSByw2m60tlrEZTUOoT7LZhNx4TgBrRP78J2NH5DcHB7RatRbqaFngfV9GTzw9WktvIkuJMzq9Fg74kF6rs6gRUbDciDAKesWphnA0g9R5UY1SUc0adSxdx6hMkrN6QjRCJeXcSInIIrostgk35RfBOBw4vxKmoZKpUE%2BcZDdO%2FJppGV%2F5n8w2zTWbr902cd%2B1265W%2Fg25AnHPHXuldo259a9Ir6H%2FTyZFbu0uHMc%2FnSjXIHUG6oJDnP8fZkjVG%2BXFSKNB8rfk6zWn%2Fj%2FzAuZRJUP%2BuYx5BqTy92gv5HdCvokeWya%2Fw21BbbDfCXsD7E3T9gTs9fnyheo30Mf8D1El7I%2FAvpoeL0CPcL9Dj6ha0GpyP5p%2FBt0JZXeSa6Q8X28FlAUgfzfkK2APwD0XwLWb4bgEjkvgWAP9skN%2BJey3w%2F3uJLs6QO97O63zO3QrS86hP%2Fm6uyDv4xCRruI%2FWOd3XAX7HnhzE%2BybYP85SOAK0NXXEeKvQkjVDxT8E0La%2BxDSX4SQ4W8IGR9ByPxthCxfQMjWhpD9HoScP0LIvRkhTw4h76cI%2BXUIBW5BZLENCj6JUAjqhqF96U8RKluCULmM0BwIbip7Eap6AqEYtK8%2BgFBtLULxPyBUP4BQA%2FSj6Q6Emo8gdNa%2FQ3f9CC1shh3qJucitBj62%2FZbhDrg%2Fp1Qf2kQdmi7fD5CXavJf9xTbi9AKfBFzgfEZVAvUKGL%2FCM%2B041Y%2Bk92DLgcBZlYgMgvSTGnheM2tKD96k3bN2%2B4bP3W0Jx1laF4S0tddUtTaGnfdRv7tm6tXtGz7oqezb3zQos3bgzRaltCm%2Fu29G3e1tc7D7Wjq9EmtB1tRhvQZWg92gomZA7ErZVwjKMW2OpQNaRNcL4U9aHr0EZIt8JWjVagHqh5BaSbUS%2FgdQgthtKNcJy62xZ61gfHPjhug5TUXGyCt0EQcCRRGhrFUCeqgttVIgGpUHsVOD1dq9okll2T5Je1DLQwW5LaypalCza0PNXCbU3y3pbhFuZasfWSJKtpwVtE7SVrMi0dLZ2RWMehupULlna0qDRVtLnH2%2Bvoq%2Bwt72vpbeh7uvdbfcO9g32v9r7SZ9iSVC%2Fr7ey7oo%2FdmlT%2FtO8nl%2F7kMvbaNeQ3sr2dbYfRlfAGV5H%2FAe4cQus7D2M32oh99PxFYFIvmgvVOtqk2oZYKgbAOQzkgAv0meaqjRVPVWQrLq3oq7jsyqvV0OELN164ibl2DZjDzg2RtaGBdQMD64bBxfYNiGdv29AR2dDhHgZyWWPD8FiSXknTqyCV3CGgxUF%2Fkv6Kg%2Fx3cVt7pH1tG%2Fn3YslVdCxXtw2hngy7ujFGjtyXyTGa4S9qhNuUoboOADao4e6CcGB5LD2ojyBdBHWtinR%2BuU1Jyd8zZ0tRGMIu32BpaUc43LGtcwPd3elMIKlu79xwXhv9r%2B%2FFbvQgcOsh2B%2BG%2FZuwfwv2J2F%2FAXYRdgn2w7C%2FCPtLaEXHtE8aGP9%2FAHqa%2F2oKZW5kc3RyZWFtCmVuZG9iago1OCAwIG9iagpbMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAyNzggMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMjM4IDMyNyAzMjcgMzI3IDMyNyAyNzggMzMzIDI3OCAyNzggNTU2IDU1NiA1NTYgNTU2IDU1NiA1NTYgNTU2IDU1NiA1NTYgNTU2IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDk3NSA3MjIgNzIyIDcyMiA3MjIgNjY3IDYxMSA3NzggMzI3IDI3OCAzMjcgMzI3IDYxMSA4MzMgNzIyIDc3OCA2NjcgMzI3IDcyMiA2NjcgNjExIDcyMiA2NjcgMzI3IDY2NyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDU1NiA2MTEgNTU2IDYxMSA1NTYgMzMzIDYxMSA2MTEgMjc4IDMyNyAzMjcgMjc4IDg4OSA2MTEgNjExIDYxMSAzMjcgMzg5IDU1NiAzMzMgNjExIDU1NiAzMjcgMzI3IDMyNyA1MDAgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDI3OCAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMzMgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3XQplbmRvYmoKMSAwIG9iago8PC9Db250ZW50cyAyIDAgUi9NZWRpYUJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9QYXJlbnQgNDMgMCBSL1Jlc291cmNlczw8L0ZvbnQ8PC9UMV8wIDUxIDAgUi9UMV8xIDU1IDAgUj4%2BL1Byb2NTZXRbL1BERi9UZXh0XT4%2BL1R5cGUvUGFnZS9Dcm9wQm94WzAuMCAwLjAgNjEyLjAgNzkyLjBdL1JvdGF0ZSAwPj4KZW5kb2JqCjIgMCBvYmoKPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAxMTYxPj5zdHJlYW0KSImcVktv4zYQvgvof5hjCsQMSYl67KmJ7S5cZL1bR%2BgpQMGVGC8LiUwp2Vvk13dEyo7r2N6g8EGWOO%2F55hvelRGF4efW0c1KNbLXWzW1jXW6Vb3TFTgd3ZTsTwoMyqeIBWl88DwnPBaQURLnBZRtdPVFrrWRwH8u%2F4omPCaUcZgwIgQe19HVzLbS1BJqDY2t5Iu2RoFsGrteawsTmOq%2Bl7U2GirbbozupdMw2KKDFRbnUM6iq3vyhUAGxm5V%2B9Up4JSKazAEmBhkk5TENAZKGM%2B8V1rc0OyGU5YMx%2FNdvquP%2BEejnIDvwOE3iP7GDH1uIDJORFFAjqc0gar1Om00EUyQgqb40kQP0e%2F4GfVEStIihYzhM0NlmhEGvCAJOIVSd2XE0JEvIhuLmARHCaQxOsohQ%2FGcZr6IU2tqHWrzJFvdaOnUEPlhE3jQ55BidWkBacFJlqZef6U6XSvzIj%2FstEavGUkEKhIap74wo2CvQJvXNjw7vbVDizrlthgHWOyG2b%2FptVZGVxpU1ytn9FFkk%2BAE25WI3HtZblrlvEFs6jNmZXqNmMKHQ6yBUQ2YTdUo%2B5otIEYM%2BnzWjzEWFFXrjXr8aUQCJQVPPBKkQahg4Igo2XtYPTvVoeURWrXyZwF113iIkDHaB4Aeut5t%2Bn7j1PWhaRZANghdA9pRplbYx1BSLRvtM9k4tCe7TmMRsNL4v6rsutHhxalq8zwkbd%2BEfOgXKukq5RDk6rhVMcIsy4CTPIt9Ffkeu3oPOJGnRCT%2FB3ACEN0kTWIPmOVR%2Bc%2BDLSYs5SBSTlLKR7Due6r%2BwY7CYjr%2F9cN%2BZkUe0j52EbDQ9XZoxAbRhdhTPb62cgChU8aotz1BSazpDhajjoZWG2Sr4xLylCRZjnDP88A%2BD4tAS%2Fg9QYTGOKwBoStdfdMqTMIEZgMqpZFrJ58Q50fFoIQXCUw4iVlQntq1sa2CE%2B5ZMrinNLif3s8fFp%2BPx2UUw1IVY5jLC9awGHT0ezdfLW9XswsGU5xDn7fqOnvJYhD7JDssQ3Pc%2FQODnmB8yrWukJx0V8nmDXRPhLoqP5VlLu4pu4%2Bz%2FOG8B0ED2mfjOBuMCbfApdiDAo0HjmdFJi7UI4gud%2BTwHvNjBovy9n5xe950ku0qg2vrkunJiJ%2BYcMHOgQ%2Bpo7MGqeY09tBdEjbbbl%2FKE2R%2FLonlpTRw1x1QtqdXzMWdWyhvfRQXejui53agUdztj3GCW1Ru7bAGtu%2Bwnf7Y9swavH6MRvHuos4aZZkIqfoBQifIo5OcCBaGoVzNl%2BXJ2cIpfQ3ptd%2F7Gp0bNKToJD5weSoRQTI%2BAkn2ao2kJnE1bxEJ9VivkVk5XkIKz4l%2BUvDGhJgG2z0j5JogOkpiU1nmJT86Wfs1%2FB%2BLO6rGTSPEq8UnjWmdspcQPmwklPs8nKqdfkEKMTK9x06lt7o6zTpYwYOusoLuI01Sb%2BKPxS28swELvCo5%2FfJi34NTVhCesx%2F3FwEVj7wyn7RSN%2BfSQBiNhNVKXESSfLWut41sVa1%2F%2BWb7N1fXwRjB1Tcu838FGABi7AOQCmVuZHN0cmVhbQplbmRvYmoKMyAwIG9iago8PC9Db250ZW50cyA4IDAgUi9NZWRpYUJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9QYXJlbnQgNDMgMCBSL1Jlc291cmNlczw8L0ZvbnQ8PC9UMV8wIDUxIDAgUi9UMV8xIDU1IDAgUi9UMV8yIDQgMCBSPj4vUHJvY1NldFsvUERGL1RleHRdPj4vVHlwZS9QYWdlL0Nyb3BCb3hbMC4wIDAuMCA2MTIuMCA3OTIuMF0vUm90YXRlIDA%2BPgplbmRvYmoKNCAwIG9iago8PC9CYXNlRm9udC9DQ1JRWVorSGVsdmV0aWNhLU9ibGlxdWUvRW5jb2RpbmcvV2luQW5zaUVuY29kaW5nL1N1YnR5cGUvVHJ1ZVR5cGUvVHlwZS9Gb250L0ZvbnREZXNjcmlwdG9yIDUgMCBSL0ZpcnN0Q2hhciAwL0xhc3RDaGFyIDI1NS9XaWR0aHMgNyAwIFI%2BPgplbmRvYmoKNSAwIG9iago8PC9UeXBlL0ZvbnREZXNjcmlwdG9yL0ZvbnROYW1lL0NDUlFZWitIZWx2ZXRpY2EtT2JsaXF1ZS9GbGFncyAxMTIvRm9udEJCb3hbLTE3MCAtMjI1IDExMTYgOTMxXS9JdGFsaWNBbmdsZSAtMTIvQXNjZW50IDcxOC9EZXNjZW50IC0yMDcvQ2FwSGVpZ2h0IDcxOC9YSGVpZ2h0IDUyMy9TdGVtViA4OC9TdGVtSCA3Ni9Gb250RmlsZTMgNiAwIFI%2BPgplbmRvYmoKNiAwIG9iago8PC9MZW5ndGggODE0OC9TdWJ0eXBlL1R5cGUxQy9GaWx0ZXIvRmxhdGVEZWNvZGU%2BPnN0cmVhbQp42sV6e3gb1bXv2tLobUtjWZJlS5ZHHluOJT9iy%2FEjMbFiSXm5gElIKjmU2LGdxMSBkBcEyMEc3goUSKCkQAKUR8kDmJHiRE5KUGggPAKEkhZKyXc4p7T3nnPdW9oLl16SyHftPXISQun57vnnajRr9nNm77XWb6219wwQAMiDEVBD6PKF9Y2dv1j5FpYcx3Nx%2F%2Bq%2BNaq1qh8AkArMr%2B%2FfuF5YumPFKsxvB1DFlq9ZsfrwETkCoP4CwLhtxfCm5TBsfgzAfAdA6T0rB%2FsGjn0y9xWAmjzs37wSC6b8z5E%2FYB77QMXK1etvHHzN3I35lZhfOXxdfx%2BQwcswfwLzw6v7blzDfaxrA6htx7xwbd%2FqweZ3n3di%2FirMv79m7eCazSveTQPU1eCYzgBR30MeBA2YNY9pguAhLnbtUX8Ay1VWvUZl0nIqlcqo4uCi39xuQQACwmmV5sPsFSSoW0fexeJt5xoQ5I%2FCKQ7oWB3AY4kWBIhANyyEPlgBQ7AG1sPGiQk6VgjDZay8H8uHYS0tn%2Fi3iSMT6Yn9E7smXpj4%2BcRzE89OPMPu%2Bv%2Flx4NunW6d9lLNYc0fNU%2FhcRhAczsA96rmJDeV61ZpVV%2Bp1Jiaql6k7lBdgke10nPi9xOfTHycjWeD2SDZR8o1h1XXq946d%2BMP4SMyi6uGl%2BEYPAu%2FxCs9PoTX4BA8CKPwBuzF%2FCj8GLmyFx6FLfBPcBcsRx18EHsord%2FH41XW%2BjU4gPkxeAhbH8i1vhdlQFu%2FjiUpPA6xMwEPwP1w%2B%2FeUvghPwNPwDB6UbkBJbUCZ0dIn4PGLSv9rP%2BG%2FLIpZ%2F7CW%2FKe1H5Ef45w%2FQfrOf3afUNf8eXPnzI5Gwp2zQh0zL2mfMb2ttaV5WlOwsWFqfV1tTcBfPaXKV1khlnuFMk%2Bp21VS7Cxy2G2F1gLeYs7PMxkNep1Ww6lVBGoke6C2RpidSMyTRpb11tZInnCMSCSQSCDpEyTojkmkN75SUgdcXuUXxw4SLIpJqsqCVkwtiE1zeePTXJN1ic4wL4MtscY2SkAFxPZFYXusHpsU2gQ5fyKxSAz3CJJZ7IxFxWivpI72irHBuMypK8XKeQOikEgM9A2k4Uh3TOSn07vGlSfHUypCwMEnhPa%2BQZFvjwlpAoskc%2FiqHhyjgxcyLq%2BQJEQ1T3S0xkX2SBdWKMPqWhhLETQb9rn2eTjb3NRGlq2UObCLrRKxJwlwBYUt2CM%2BOZewGBYSyiFir%2Fl46ZM5jU%2F04TzTExNXxrxeCVxYPzsxuzcxJzGnt08S%2BhmHSK8YHYrU1kTF2SI%2BzdcncT5x7lxW0Iv53sk8jmyeII0siMkjoQTObNnmIacYvc%2FljcbxeRHRG8HhRCPRCN5WcmJrSUfFowuIfUOD2KK11XVOMFG52hYQO73YEs4RW1SMJG3EFu3F4ZACq9Am1IPYUVszBhkYiF4RE6XehTEhKvVuibgYt%2FFOOE0qD7EPeSQkYihmF3IsHqMPHGRPpLMM03kmxE5sP88fCDjHgJCJO%2B%2FfT6ixVUEkgk%2BMqVzquL7A2gY4FtKH%2BlFbowzYiMOZN9CPbUC50FwigZlE%2F32YlfO4ys67JWc4Jgk477wBcWBLOgTLxKgg9XbHvC5Begov0ki%2Fa0gK9fXG465oXC6tT0RclCLHApNSckmlATpgnt7NK9dDuPSmPzglf8CFCjd%2FTDUCIckV3nJf%2F3Rs5aUD6hNjOOMtcaxvra0RBQHlhaLrk%2BC%2BVjYRkYoYayRvAC9yJdZGjXLVFLyYamvyZF9lIiJG8uUpVfSKfLiLcnY%2BPqZf5BlutrjEwfvweQgXTZiOsr3dRRn8%2Beeuz4%2FGsaKajwmZelddHOm5Zu3edlf157SbJEZjmBmnihGPljJVkkIuaSReggMCNTYWeJ253evivSI%2BUWEEuTJGXBBnD%2BCVVl6q4diw3ZVrdUEz1u4w6GEAqvEI4TUQ3Rgdcm7pDIvRZZ1Jg746jWqUwGmFxT4ULhqGGPIrKsQkfbhfMoR745FoIpqILUOkUAWPK1pKlYimmLo11AUCqcH66oZOv9SAqUaaqq2Z1QGDcBOavRWEMEpN4AqYYDTL6FlGzzB6mtFvGP0%2FjP6N0ZOMfsjorxj9gNETjL7P6HuMvsvocUbRFs%2F6Aq9vs9xbjL7J6DFG32D0dUaPMvpLRl9j9AijGUZfZRT9M86mn1H4Dn2J0RcZ3cvoHkZ3M7qL0Re%2Bp%2B8Yo2lGDzC6n9FRRvcxmrqg%2FfWMXosgHYRVLH0No0OsZAVLL0c6wNoPEMcF6eLz6W%2FR7Yw%2ByuhPGH2E0YcZ3cbo1u%2Fp%2BwyjP2P0aUafYvRJRncyuoPRTTjCb%2FfdwOh6RtcxupbRNYxex%2Bhq1muYpVey9CBL4z0UfY1JTS4pGA9cqJNiEjCFHgAREY240Cf5o%2F1oe4bERD%2B10RQ0vWko600k%2BtIg9CWEaGJL4j5MbhFR%2B4f6BqTqaDc1rMKWiNclRiiSvN5kNakO91KctHctQMxKcEXMK1I%2FkqCGDRYkEgvQowFCMB7HZkKUwlZk5n9WIBaISTMC%2BFd81KRTPu8AwB6mNxAkBzPL9qJwomthD70PDvc2Svok9QL03syACTwtWRATSLsA7dSqpSf%2B5I7HvWgD5TZ9pRiIARq4hGygaSKV0yQ6wIBL1nn8cckRvqBd%2Bfl2tIvx%2B9qxyvP3M%2F6j%2BxnP3%2B%2Fi5yJjR5ahXXs3ImIo0IN%2BTJDuj4hxQfoTS1%2FK0g%2BydD5LC900DZim82OMZjaQo2YVrd9RtHa1NWirD0IBVMFl0YEFnQfBCtOgW0kWQhNcTpOHwAZz8ZiP1r4P8yl%2FW3VbtJMqR19nKFjYamtRzZ1hmw9zYLZ67tyCjoKQal7E2g2XFlxWeDk3t8vaDDMxbOzEZcY07dw2TRSNHbWZQkyQqsPLJH%2B4v1fyUIOuqdworMSxo7yE3sR9W5zxuKTxeYUhFCRa0Qh9JvUmY1AGgmsMo9iyOJNyjdjZK9WKndJsP2NWINybEMI9MRkDm8QWMXxfHGeahqlop6lbZsFGeuIvbiUASMNPzld0XlAxS4XQIkmVqshPowvircWlJIWiqo37BtdUOuAkCJST3I%2BWA5w9RrFG0O5gGzitxetPTqPF5RqxL9otlaw5gj4mD%2B7BiDTWJXkwOIP74zIxYJAXMhA9yQfPBJD0xK%2BTBgsXkIlJMmB5HjETVg6QNPFcAFKOEBjnb4mk1G15XR0dRDLwaZglkUzpGJiI44ys8QcyaC9nQR6PRUZSfAajAHzgglgaRkriyRA4xksapsZJYWEwqFaLDbqgTiR1sd7B5b3p5tve8L9BrlY9RH6VrTt73QPvoy05NfGxNqr5I3L%2FFTp2megmhyyAh6ND%2FmlSx4ZslnRsyF7iyaPlFUkzHbLMdXeMgQtS46iJ%2FFHZ2IxZO7wwLvNG%2FiiRdTr%2BxBg4YYds5O4eJwdcnN3IGc06DKna%2BAyfkcy8BCflsjJyBI7IZaA1U1XY8SdCK%2FiTckEBqyjgaUUBrYCkUFA2DyNiCJWFBjGei9MJN6jAK0ABjxTXC7oGsaHZJ2KB1hFsrGhp0Iwvz96cncieyf4r%2BZoME%2FVdt5Bf6sZSWPRj8tqYTPLnqoTsf8%2BeICUEyC5V1%2BZ175c%2F%2FFRWyCYPv%2Fmb11HOzwNoXbhKNUMx7GS8AvMkr0rAq1HEaGa80ktmxitXjldNST3jlaatQzZ1dezTqImmM7DPlGc2dQZkZ1EaXpA0%2FJmMzBekISWZMJniC5xFlEEkWey0YIySQmUx85m7b%2FrSmcloZEL4jGzGAiLr9ZTFNtiKQrp7HJkRJA3BBm8jZ0dueBvEFkxDQZNP9DY8T6b2k9%2BeJuZsXfbpbFn%2Ff5DNfyGX79%2BV3aI5fPrfs7Gz771IDpPwnjRdrz2BM16CMy6CU8p81ZPzdZ6br5rOF6ySms23mHjzlXIrne8Bk6rL0ubQtXVkOjoycmFXx6gNV3LWQj5Df7JGnyalSaLzBdi93UmiwmQ%2Bz0otvgBJadQaNc5ZdiDMrrnpC2dG1jvolE35mE%2FxVjyQI184TxDZZkKGaNT6sQn8yQ59GlYmTXp3QOatuTKTjZbZbe4ASdqsZGziBBQGkF3IIrEBWRZE0lDIrqKISbJkaS%2BZun4D%2BfPflvZmT1x%2FfdaIXFrE7fmmk3sZNeIjxM5jiB0zqv9jCnrOccidQ8%2FDCoeIIceh0pxGtCYNlEOHwI1hEaInpTV7TAHZ3N0ha5s7Ru1urcVl5sf5cZJSq40GVIQTtO1WbLsjZVBj2%2F1Wt9Hp0qLEsdGo2ajWGtUIKvynLFqzVqPj2yV7O05Pk4MF2O08AW%2BLwHRBgYbakf0qe1ylIzeSfNKS%2FQbxcZeIinbH7w5nv8zeSn77iymkjmjIk6r27KenueyS7KnsqRfJByR85F%2ByzdnXjv0B%2BSABaP4V9aQAB3hA0ZSCST6UntOUghwyChgfPAofshM5ZPBdHbK6jSFDjZBwlYwhX9QUB4WFah4nVT%2BG9jV1JlWYV5iX5NV5gZQpr7CQAkQ2FzDd4X20H%2BZpMZHdZgaaAlKQA00qryDfmslQo0TBM4Z6vZVh5wLIOIq0gDBRN1AF8DY2T2OokRA1qvDTY4Rk3%2FksG7i1f4BcQxK7jt28JtujObz34c%2Byb5%2Fdrrand1weo7hJT3ysGUO9mE66FH9gm%2FQHdZNsmZFTj9ZkHWMLL9UxtrTn1GN7kmdscaA%2BBJs7DkI5RsAOflwudfBHDwKHzAny4ym%2FyRGkhlSeHsRpjaH32rGvpCboqOPvzhz9GyGyQYf8KEWmSKW8VHpS9niYNfWUUmvqodZ0DCph674Wc9DBs05SVUae3oIdpvPS9JMyzod2gOm0A8BW2gFXUdGhhehxemVrJTXEJj9fR4cxavLTQ6%2BdJ%2FF06S35xdZ4nCRJJbXXcmUZ3y5XhlrjSQI6WgCoozJgAZp0v4feqTTkUUx6UoA8WjA9hPU5C48GvVyr0xY5ivDvsNt0mPEKVb6WphYUHjX0zS3NVb4q%2FPumNbU0VwQF1qyB%2B3PpEy8%2B%2B3pJYX39k18dmV2XVy2%2FlD2b%2FfT2pcT8Svn%2Bt0Y%2FKi4KNr78VTJcZ6gh3X8l%2Bg2L8jnb%2FdvvuLUn4K9sq102qy84480NG%2B5Y%2BviM6M9evu3mNcFgdZv%2FyrZYVe2HX2%2BKbWmdiTjYPPF7bi3XDY0QJXddZA9m5wKAD3MWsyxnD%2BYQD0%2FLP0mWMYH7L%2BuQAU9nCM%2BZY3QlhFZgDCywa3yfVt%2FaBPzJzImMBLxUn5FtTv5oMtQ6kzKzdSbfnhzRts67AtOhVldyZCbQ9ExkqGtUH9KGsBRZGY9zsg0iSbDosduojbSG9IznEVmrXxSTtZjBIM0WjcnTaJJLem36eQtisi3UmgQb7UST8TgLO5KhC0pQN2bAU%2FvcZU4LWu0MunsK2uGUu8xdxh%2BlIJRMGanppBTMSHUZqf6EWlLzku0kIpZpWaGNalkhPPUnIvvK%2BOOEDdTbNIM%2BfkYTnd8MNZ3TDDqn5EhZE83YCK5N2ICTTcEZfHt7qikUzCmNptw3rbm5paWFEkV%2FaGTQHGwscugcOrWiNFSRxHJUmwZMkBakOq3WbsO6lmZ6kMfu%2BCn5SPrd1VNnmoh1xeY1D1U9e8nX7x27sp0Q42aNy6X3Wl0lgqdwWYWdFJc%2BTSzOhVfvXvNPC7seWHuFqNMbSm%2B%2FfscnDpdZN6tVvXhB%2F4r07u7pfVNqz%2F68pJzj%2Ftmg006vDDkdm8hbG0Sz03LjVTNmXdo8z%2BJ2U2uyHbXqGtQq33citCrwmKjujOUiNGsuQpuS06mliheWucswJCuHfbkIzRzCbDHqk2wzU2NoYhGaG3bKZhahlXPFZs5sPR%2BhWVmE5vMxGflYhOaDnRihWZnw7HZWYWfCs9MKhLPdp0RovgsiNC0nojim%2BZpbrEAFUJSL0iioFXY3tzRw3mj2f8ipd4hJPx2hKF3RSbiXtAPLHz5Wo9evH1r%2BrJ%2FccPg3b%2BzlKmLVpIjMGFzwwOz1N37yK998z8bbbt9Ao%2Fm9E7%2FXNKE%2FckIF8uTbOKwEj4Hy5sUcDvkcDn2kPBeIK4aXYlCrMGqPrC2bBOK4bJ7WQQMZ2Q78x7JRy59IhoqZdSsuY%2FgrpipZzPBXxvBXxvAUpyEL8SbB7KMRSZr4k1BIIxy12sxT704l8JRs5lECEs9LpjQM0xzFjC0jGTNSeUayZ9QpOwZ6EURvuRsfmhxxMzy4bWhI3fggCgZ8srmcltoVYNgRyeVubFAeOocJngqgKBfs0PCnpUGnLSxX5aTQ7FMV7zpK5vzvIz80ru9Zkj2xeHGp8%2BHyvZ%2BsH7r1nfK6vtKPDpHp5Ok9dlXdN50POh9Rv5194%2BtNd777s88%2BU%2FjPPYMaWwYBEr2I%2FzXg0VP%2Bp3NxUV6O%2F7U5nR1K5p3jvwFtoB9j432yF%2FjS8YDsBlRfvy8Nu2SvgZaMQQBWpXgeDDRCOJPiA3wgCQYMDoSAhbI1kwz5AtR6%2BAJ8e0oIBZg6JiHAU%2FW0hHhFPeNJ4IuoEIt4bEaLW881op2o2SSyVk3F5IVto15zXp4fQ%2FWdsgFYbMGfyGTwSqQ8XnKflF0uBgmXm0LCRSFBkiGvnw7E60dJeFESqYCfL6CBMKLF4qJPcoXcbDiuXIHsYqqjiKyqSYGNw%2B7QNXhRWpOAQc%2FHFTawSKUcDVmDKnjgfWI0IHj%2BsvcV4j3zV43mhhs2jFW8HMqe%2Boq7aePdqy4LlB6Ty3%2Fop%2FB5PXXXjeVV5TfeeEPfy6%2Br7g74N9y07srN1O5gVKeOa36NajmuxHSa8zHdJFY0KEOCK5ekJi8vc%2FcYyn6YciGlUWlUB2nMDXkBRBTX1VFPTcOu8TFcJq%2BSTTx%2F8kzKpDfpkxxPIzkThmEorH0A%2BpISDNFwbaDCx4lJwvkCwByNNOKSQnG5sIQyprAEg9vCC8pb42wYhjxdJnOcSCo%2B6SLFmcxR2Y73kqxHCVEa97okIS5bi%2BlNrMV4E%2BsF5TkzRYM%2FRIbYEmxoYZ5C10RNlK4hqFNVvNqz5NX7PfeLrue314pT5raHeecRj%2FrxM4Pqxx954fabtE9ykUvXPUKt0OJsO%2Fc7RIEANWS2ggLjJAdrc1ZIShqZFbJLRoaCOuIpoOV%2FSdoZCnSIAlukQ84PdSSdV1SlYVTWecdgN%2BRTW9RBIzK06gX5aIo4Hf8x6lgVnZm3ipqifC81Al5miqp0NF2lmCIuiYEitjsII3gDJ60BViO7MZ6gtaVKLKaoY5ykjEabXTFSFfCkbLMzz66G4aQN1xTMRhVkJH9G0qClYgAoLWUAKGUAKIUnEQDnLJe%2Fgo6xAnGQDFUY53Vj%2BrzlsvEXWS5%2FBeLFr1gugvreQl21b5pVMWCo%2FmoVYIESAZJyVdGkBSOnHh%2BrzFNvfXnPK4REvnx1kcm790RT9r1vXhhFtbue3Lz7t2Ttis1veuv7sr9OHWxRb3s7TWaR3S86njljzWazZ156fOGPuE%2Byb311yz1v7fzs36lM70WyFj2LAfJBVlBxbovHfA4VdIsH%2BaPFyIcYiEFZfpCUQWvQngOFrEcfwnV0JE35HP%2FpcWYhy5OA6i5rDUzz9dQ7cFraH5czkpZPGhFk9ccbpuKd8oxzxDm5d3YsO1uc3Zug7%2FH6aOC9ch%2Fh2OvCRKs9RQhwVluTLR5k5p6tbYI68d5TPUvwT4L7%2Ffu5qadPaA6%2Fg6inM7yWzXDlRfMz%2Fr%2FNj0M%2FSedIJ%2Bb9%2Fonlll6KF7qXTKXOpmeJ5vA3ndQG3QmgfRcx1ILmiI2mZXI0rVBuUEbTwhBUIrUwBLWR8gKlvIQhyK2wWTahLykMsTioXy6sT0P%2FqEnHVTd3dRzFYVDH3tTCRteMA63ys2Q1Ji%2BQS0kpK3WzfQldsClN599CWib3ZDIp3uJDjA7vr%2FHX%2BP0lLdQnTdYRuayUdigqKSrJdZCrSucJcpN%2FnpDoGxAkHs2Q0J9IdMcG4%2Ft5i7%2FGV6XzSdqiVlc51X1FeLoGtEksdspdcXmj2KtccPudzJ2nFi8un%2FrwrociZaapjz703JwG046eJY8itDalPSM9Sx63XbJw64GaVT1LuKnPB7ctmtJSe3fPJb21084aVJ8uv%2BblB85uVf2590fvf3T2m5xEuDuZb88oEik7v184KZEyJhGLVJbbL5yUiEWJRlEaBV2KRDxduLQtgn4woQD0Jv5N2V3GmOz5NustVlZagKxHp8oYX0bKJvmoceMi12qjxWaL2cKfYMWaJLitNEayWTH4aT9gDllCtpCVroHQpF3MUYfu73Ou4ZE9Bx%2F2Pdqz5KHiH6zccUTh08%2Bbth06eLZS9eqm9QpfVLAbY549yJf8v78XxGIe%2Bbt7QSzmWX1%2BL2gXdt%2BX0ubTvaB8JQQdtaERdeV%2Fdy9oG40scntBBWwvKF%2FZC8q%2FcC%2FIrM1X9oJs7ediPy9GEXYreFsarZNhA1pNVdP%2Bk2Q10Y2dJNmHsl8Le8ZvuX73xvXk5jWlnx4mU8kzv97PZY9nFz9BZv7xnx%2FYuePzO7bSmZdm49yLOPMi8JF8RSfg%2FAqF6cQEijEX7cHkCoX5ueFctGdRoj26JbFbNjjHYBQsDJSOKzpkNTpAq4V6N4%2BTeg6Pk3o3g4c6Cg%2Fzbk4LTTtzgTaomN6oUW8K7WlSmQQHhWxengMUH%2BaCh2UH0EAbeJqgS04at0gc82Deo5L1gkDb5WWBdp6LPsPlRW%2FkCk16Kw4u8lZeF4vuJlc8TL%2BYetGt2PIqBZ4kx3bUswbqtK5YtEh0P3346bHs49mzL%2F3w2p4lqvBLpzYO3348IF7PTd3reen9fdlj2csfPPtHlY9EJq6%2F5a%2Fv73qDonEnxmiPIO%2Bd8JTCeeck54vPodHJ0GiWnIzzJTnOm9juPZHN6PEVTOahquUV6rmirg7%2BzTd5JS7ylsZlP4%2FWcjyA8DXT3Ta7k2Gx6FsIJaN5Zo3OZmfQdBLnOZtIXZclcyHampnh0p2D2U6EWXVd3c33u5z%2BWTf9smSEIuyNjrpNukPP3nJ2K87yNsTWdJxl63f202onp9uWWwpfm6zN7afVsulOV96vTCRzyzo7KlpjCI2OF%2FaBHRXMbVf203ZBIz%2BeqjbZG9l%2BWmtjbj9t577iQKO99lv7ae5iXBS7%2F16cs5Nuj1XAtn3N5kZ7bj%2FNl5Fbm7FDKy%2B1nt9Pa1X207ZdvJ9WwfbTqvlaZT%2Btmh7n99Oqc%2FtpFR4WQnlQ2Sq%2BZz%2BtmsVw7lDpt%2FfTWv%2FhfhpHd0Gmfe92WiN7s6JOGIauXX07bxK9a6Wt0yr0ZdnxQ%2F%2BtN2J6ybL2gU0PFuRV%2B%2B%2BRNtd6dB6il9LxS1VqbnFP96XT3K6y%2Bqo59XO8VdsO%2FSi8pq557fLFCy4RhMr68pn%2BmWXitkNXzr89gPL%2BYOJ%2Fqf5FcxVqdW4XTXNeqxVxnlBWHvSlx3DSbNPwSgSisWls5yMQVVdHis%2BzRDqOZmSjEd2zrkBnTcOqM5nMCfbOpR9NBX90%2FCjZl2%2FmC2185mgmk9RrNZiQTSZcgzhtqAX0BQENOVuUJUFhUI0LAbGBbh0FVaoldesuL3kQVwQdox17PHseNNS2XpK%2F7jbVvkdITfbkI2d%2FUFeDM%2FoSveZJbipY4KuLLCQPHuMFFhJMOQtZQDyFSjl7MSibuzpGjaqIVmPmjx%2FvyHDshUkFe2GyH29rAeII7FfT1yWYQFfKKo1YacrHg1bqtXhgYtRiMefjhNkrGJKy5JuAqXy%2BZd6A5OuOpUL5Pgtf8mZJXNaArIY0rE5awB1Qy1oTvTFcm8w3YZYke7WaecJAYmGMfQ%2BUhpFlQlKr1evF1qRGo8YL%2Bx6L6hrDfmFD7iWLKH65YuUy8h%2FL%2Bs%2Fe1dvLTT2zVX3t6RPKF4JVwH15x9s%2FWWpp%2FwpMevaRhCze2jt5Pfujs%2Ffov9CtQ7%2BDLjX3bSJS3XB2FuhRE587u0r%2FxWYo%2FrdvfXtYotqhJOjbXDzTmmNwCs%2Fn8XxC2wYfaRaDhGeaWwebMXDejte99MS2El4Xk2NwLz21u%2BFOzNNzN56lqt2wE9vfhu0%2BwOuXuee1X3DS8KAC7bQDdaAZQDMTY9rLcKE7G8D4BIDpPYB8zFtsAPyVANYpAIVGPLdj%2FO4HcGAgXIT3caItL0Z77%2FoxgLuXft%2FMZl8CS9FK9aIFU8EAjqALaHIjLs7oW20VOtTJ7zdn4KkGwhnwuhFmhK9bs2nt0IqV64Up%2FdVCQ1tbY21bizB38IbhwfXra7v7%2Blf1rR2oE2YNDwus2Tph7eC6wbUbBwfqIAzXwRrYBGthCFbASliPi9wpGMVV47UB2vBohFqkLZifC4NwAwwjXY9HLXSzj5JXIV0LA1CHLWZh7TBez99tHcsN4nWQfrqMlLacZcbZAEaeIYhjpwBEwY%2B3qwYelyJhP8YFXQs7ZbV6aUiTaHunTbUupKtuG2p7oU29PqQraRtrO92m3iB1XB1SG9vIOslw9dJkW6QtKgYiBxoXzJgbadPq%2FbJauzSkPhAgG5ayOxkeG3hkcPfAc4PculD%2B%2FIHo4DUDg4PfDHw1qF0fMtgHf4q1vxnkNiwdQ0EMRDsPwmqcxLX0e7FoGlZGDxInDBMXyx9COQ1ADTaLdMpTpwViAcTGGHIEC5RRb7%2FmhWtw1IY51y0fXL58%2BQoOn%2FHo8KNrDg0cHsZnoG%2BNDonLhAQuEvrH0Mu6EtKcjUMRcSiCEdNKsAbG8OGUrmb0WqSyU0Cm7HeHXK6S4mL6pVtnWAwv66TfuslFMOl7ejrT0JdU9zQH6JVbQq%2B%2BpOaq5gB1Z40ROR86R00iGEXoWihGl3QqlH7ClypHXxqJuEbLyyNeb2RjdIidzniyNKQLR4eu7GSffc5ywk9RWI%2Fh%2BTiez%2BL5HJ7P4%2FkynhKeMp4H8TyE5y%2BgO3LBL%2F5%2FAalUor0KZW5kc3RyZWFtCmVuZG9iago3IDAgb2JqClszMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDI3OCAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgNzIyIDcyMiAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyA3MjIgNzc4IDY2NyAzMjcgMzI3IDY2NyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgNTU2IDMyNyA1MDAgNTU2IDU1NiAyNzggNTU2IDMyNyAyMjIgMzI3IDMyNyAyMjIgODMzIDU1NiA1NTYgNTU2IDMyNyAzMzMgNTAwIDI3OCAzMjcgNTAwIDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMjc4IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjcgMzI3IDMyNyAzMjddCmVuZG9iago4IDAgb2JqCjw8L0ZpbHRlci9GbGF0ZURlY29kZS9MZW5ndGggMTM5OT4%2Bc3RyZWFtCkiJrJddb%2BI4FIbv8yt82ZFa428ne9cPOmJFaZeiai9GWqUhRV6FuBvSrjS%2Ffk4S2oY4xIBGMxIzIu85r53zPjZXi4Cg6k%2BxCkbzNItL855e28wWZp2WhUlQYYLRgv5DEEWLl4A2T8MHC0PMuESaYB5GaLEOzh7ilcljxL8t%2Fg0uGMeEMnRBsZTw9TI4u7HrOF%2FGaGlQZpP4p7F5iuIss6uVsegCXZuyjJcmNyix67fclHFhUFWLVFUoD9HiJjib4geMNMrte7p%2BLlLECJHnKMeIyupZoTAnHBFMma67kmhE9IgRKqqvxx%2FrnX%2BHfxh4TqL%2FEUN%2FouA%2FWGG9NiQ1wzKKUAjfEoGSda1ZBxeSShwRBf%2FJgsfgr%2BBqEVAoUm8Q3W4Qa4owpBjmiiEtJA6lrDfo2q5fYdF5aT69gIVOc4WF4EhzaK74V3MKvZn46u0qCMOM6v2KtlvWfZ2aYxEppGn1uTW7yu06RSmawUdl%2BAy1bLcMUA2vB2bA8RxKLHnUY%2FlT0fXcUQxapqHCVEW157C2fBOXMcrjTQKz02%2BVwWYxFvZYhWlWtMfqp8KxuqsYtMo0WA15y%2BpjGZcWJebdZGm%2FVS44jvp2FRLXb%2FVT0bXaUQxa5VJjTdu7%2BhAXMLLAhn6fAh7U0rUJZvrn9UPQddkRDLoUjOCItTf0fvNqyj1bKaqIVNPthkphLfom9FPipmpXMmwzbJb66fLaLk2SohezSeJ9L%2F7QPDsKBSEONT2MALTrVUlMtUIKIiikaNxOx4%2BTe3Q1ns8u5zf3nvR7suwqHL8D6XfsUshfBGdC5ZdHDbIIr1hPIy096fdk2VW4Vven37HKNFiU8H4iisn2rExsbt5WwKtRfcJh2pyV9Sm3%2FPraDnH30Ly7Cmc5A4RwlsMVxYqKnZ2%2FNavM2JEHEZ7EOwLH5gAiHJuCaRDznYGe7Znhg9PuSnpCtx8QrkklcAgbv5u6x6vZXMsp4VOuw9kgJbyZdxVwIZFMnkoJGFUFq4ZTgpKwTYnbyfcpfNyNbybjYVL4ct%2Bj6Ho%2BiRTgmUAMa1LQEfyFuyMbJoUv9z0Kx%2BpRpCCYwJWmbTV%2Fe4Y7wihJM%2FPsuSr4Yt2j6Lo9CQQttweBwJdrV9C1eRIIWjPrA4E31T0SN1dHgQAAQLvBeryd3hF2SWgFgsUwCHyxdhUUWunoRBBoDfAHcHEB4GI9IGjONQE0bZ1r4%2Bn4bjxbXM4nHkR4At%2Bj6K7mJESwEGuidxGhPYjwBL5H4Vg9BREtq8cgwhP4HkXX7VGIECHcuEUzJnw7JvXdJkW5zbdDEumdy88mfY0Lz93nQDC4gu5qTiJJtfeKHUYSHxZ6JG4wjyKJqlfUNlmTZEr0liR%2FD5PExwVHIWGAQ85PJYmCEQGSEFITsPL7NLmcjtHTeH4%2Fu%2FSAwhN7V%2BGYPQkUYBaOv51fHSHxgMITe1fhWj0FFC2rx4DCE3tX4bjdqzgwi47A6XBSeFtz5g2vL4mupCcLR4VXY8U7YZhOnuazkExF%2FXtg53INTVXVlFW3B4EkAb%2BIRVigIoVufb1E06tiMiaVljeIrlpN8qVJgLyFQSn8BLVrk5iqXaUnWz1r9AzIhiOl655CNTeWyzy3aGmg%2BXJpSov%2B%2BBBvmzOYa0DSBUwmbVjPCOWdDhcfT1HIpaifeihsaRObZRZNrse3%2FrIqjKjgdKAy%2FIipHryJyxi9Fukmzcv4p7F5ur8247WEkxFRcDGgYqC8%2FKqexBlYtwOmjyisqrsWPFttA%2FpxFidJutnYH9%2F2VIfipBGQc7jKkIHCRLULw9svC%2FP8Vv6W2qFu135PC%2FMCc%2FZbXDcbPd8OXGLXrxnsiHkf2u%2FtkMCdBIqfkw9u%2FxJgAPPMQh4KZW5kc3RyZWFtCmVuZG9iago5IDAgb2JqCjw8L0NvbnRlbnRzIDEwIDAgUi9NZWRpYUJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9QYXJlbnQgNDMgMCBSL1Jlc291cmNlczw8L0ZvbnQ8PC9UMV8wIDUxIDAgUi9UVDAgMjYgMCBSL1RUMSAzMCAwIFIvVFQyIDM0IDAgUi9UVDMgMzggMCBSPj4vUHJvY1NldFsvUERGL1RleHRdPj4vVHlwZS9QYWdlL0Nyb3BCb3hbMC4wIDAuMCA2MTIuMCA3OTIuMF0vUm90YXRlIDA%2BPgplbmRvYmoKMTAgMCBvYmoKPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAyNDUwPj5zdHJlYW0KeNqtWM1u3DgSfoJ%2BB97iAG5ZlET9zJw6tpHphWNn2j05DAwMaInd4UItdfTjAXKfF8gTzjGHPe0TTJFiFWV3EmCBdZC2m6Sqil%2F9faU320XIzL9uv7jYqFoO%2BkldtnXb6YMaOl2yTi8utvyPkHG23S34dBp%2BRXkeRLFgWRjEecG2h8XZe7nXjWTJa7b992IZxUHII7bkgRCwXy3OrtqDbCrJKs3qtpSfddsoJuu63e91y5bsUg%2BDrHSjWdkexkYPstPMCguNGB7nbHu1OLsJ3gcsY037pA6PnWJRGIpz1gSMC3s4SYM4jFkY8CizesPiIswuopBPll3jnTdv4Q%2B9CAPB%2FlxE7F%2BLTws%2BXY%2BJLApEUbAcNsOElQf7yGGxFFwERZjCl3pxv%2Fh18WYLzxiItohQNImIGE8EXD1lWZwHeZ5YiK7Wl7%2BsV5vV7%2Bu722t2f3e%2FXW9%2F264%2FrNjV9c3Nq9V2e8eu1uz2bnu3WV9vV6%2BsxVGQ5jlbhkFeJPZGD2eyG3QJfmJJxipVsytVdmpo2Y3a6966sQUXAQQBwBNaeJJEPLyefFOAJMGWUZBbcevawRwUqUHArPXtMLR92YFP2mk3CTJ4aNq9vLm%2BX9%2Bd4wY%2B9OZ6c7vaXOFGGvAQtxo5tBeSuftkcJFpXTe4yoMcxW8317eAhJMe%2BcPeUOsD693YeJcXmcDzfk%2Fhca%2BvU72uVDO4rRiu7Hbk6eHv2fGk5cWEZAIBIkx0RnZj18nPAV6Sk%2BgP6xUtZs9kE4Qix41G1TUBFccoox8JpyzCxU%2BjrPXwN96Fc7Sw0niYZ96l%2B70id8ZBluMOJPpHrWawJEEei1PQk9ytPapG7XSpKTQ4j93WcXx8rCE2UUteRD%2BwqRxpMSXjJQEAMUEA2Lx3sJCHM%2Bb8wJ%2F5gYoDoitQ9lQtnJic5DQBKoxpjeNBPjtYtk0vj%2BqprZ1wESQFwgKZiMsRLBc%2B5o7msUftnQW5gRgcVQNexMcKemyGTZblBO9%2FCQRBoQtltaOIjiJOyQXmDl3rEzVGOZJkRyS7V02vGQV2OAPU3OwVFR1MNEqILCVpqccC15IcQ%2BAKfLghLxZ0AahNF1OhwlzLc7oDignJ%2BFL2LaEQZyfx5U9WJrJlZ5uNJofh7kEBcKVG4FLa8IUjTH4gfSfrnjwazQ43au89SpDIYSAhIv6BrvKj8qFXvHQZJCIB%2Br%2B7DJ%2FEegnHRf4tnxFGFZTMA0mIU4xP13POCb4snpWltpMUkIJcJE8VTCFa175ikIZn%2BRSF0Q%2BdSmFcQfL3PaVDEiGs6qC6vfKJUtAjtfTQ4lLbtR7YJC7m1aVhT6rTYAd4%2FxwacCmr7m9WgYewMjL1BOUUcDjYqgqXLMEozSCcH2VveY9izy%2BCYgcNNp5PMGVBBkkCmrN86tVXbM0u2S%2FwuWIbtpoMhGMJVLjZMYggBl5ooNqjEvgT7NsZflXZFnOEugTGOSL2bA2%2BOrLWaVO7KmOwMa4NrEbgO3ziO5aa5VxYarZudm13MOxDGl1TcJqgqhw9qWf0BNqjyfn4nFGQ8hilOzYVZUFo3Aef3N5rUp9bjpWz1DAjwURSgJczy7BWM7X1l5nkiSYF7GbfBzPdN4qBzY2WB2MdPLsf4f7GT4Db5JifkIMCActTe1EeYFBSlwTvD%2BYDJOpJJHwFxDrZNC0bAP%2FBHFB9WY89AERxARDvlOHbxlmA8rFrS1W570fVMWjAJqEU%2B%2Bh9ZrAEJt2W44Fc%2BLMj3sYoSxYnyKJTW2tnz6SjlxC7arcDbjAawcamfjwe2858AQhlqVqmLloAxfm3bE%2BUpVZ0%2FC1lINDfsFIOqq%2BsNUxhDyo7Pd0Uuhgc7nqDgw1Gg8TY2ZHhfjT6B0gNyO0T7WJqIyfaBz20temNxvvzS39l9RfrATOKALaXd%2B9%2Bu10D47ZMfMU%2BrODz7Wqzvl19R5k4UTbr899SWAGlDtg7KIwQW2xVw0Xgr86MPvdqD%2FkhDQ5voYB0YNFLrfk0zKSnADes3UNSH9pJkYm%2FYzsYn%2FaqK80YpRhkxdgC8NoQeig1SluvdK2JdWvqd4A6f5bINqPM9DVP6r49gouMosGVh9kQFHHLR5JMBHkU2xT9C3%2Bms2lQEPP76%2BVP4ApcHAgo1mYKdAXuAXrJIN1AY8okMbSHs52GIJ1MdLUPHI2zTwQDXJqb4SfMp3L%2Bx%2F%2F%2Fxw%2BaMF%2FCZCnSIIUMAaoVCJgARJjBzARzYcw6BXOknyKjkzk7gzk6YzGHeRYQMPgt2erDh%2BvN9vr29xVbvqiYy2giKDZkpvv9avuA63kxL150UbdlOntMTAOpDjE8GwI4lGSe5AHPwVVq2xAjgwsE4HjYUJMUGXZK1Mc13ZwCIMnwUCY8CUDOVPDn1MPdSdCsAuMv5gcnjuTnYJxtUpq10EwexZ6KIvPKUMLXEzvdKIWgegqK8kT0kt6cI7WNUVVjiulEEByJSRO8iXTUw3Q2PpWd2HHfw9FUexQnyMpKnmB3BFconEeibEarvlyjVpgKPPE0NKVSfquY1XFkSEU8v5p%2BHCmCRBJTuMBmhfhkYe6ZoY9FTlNZP0zDEe6kwhNGHA9pWDBDxFv4jywux7gD%2FLybYSPmJ26mkbdYRtkFhQscjjwG%2FX8c%2BBA4cTSlUuH2DgoKbaWB4mGrBDT1o%2BocjTKGsRxQqyqgGVBkEmMSf1kX4xg4ZcSSgluCZ9J63TxpS9IGKL9AHKALQu80XVjadmLKcPekP1uS8RORZZ56Yvpms3233ebiJuQ3cZbfY22IX1YVkQcp3CwJTYIKq%2F7m1TiACz5%2FJh5e8FkK%2BlcSkRA0hoFt9KYiRf9A236CyVPL2r9NCD2lpxEoyTKKluliOJanvpZfvr9%2FeI0bKc3GLQ1NZI1%2FZxBSht0fwER6OkEXX8quIrtpDn8wAe3HSkpgBSTS%2BZsqpfjGVEjVcxwMhcQ3nG7cD%2BkFD5rugVzikqCxV30a9dFUZ4lKvfmy8qMbihgb%2F3roWw4ylYQnyWyIsebNfORH7pmPUnqX4Hz08JrmPUHNwXA3VE4wQHc5lYJ4QJGiWvEVb5%2FTkh%2F6OQXJsQXi1%2FezlxmCXmz1QF9G3ZeKNk1eUJWiWdor8C%2B1CK2TSZei7cmMs8q%2FtsB7wzgDABIgIVVGH4ve%2FomVOBmCWhBES7vv5I78HHs%2FAwfvBxqj%2FTP96MxfChiN8tx7FWrTWM9Y%2B8MZlic34L3VI8wLbJ6j5tW5eQlUWWIuWRSxvR73MLVEIY8eXgcvKMZzdgeVMckKesN9%2BrL9H%2FnIZVYKZW5kc3RyZWFtCmVuZG9iagoxMSAwIG9iago8PC9Db250ZW50cyAxMiAwIFIvTWVkaWFCb3hbMC4wIDAuMCA2MTIuMCA3OTIuMF0vUGFyZW50IDQzIDAgUi9SZXNvdXJjZXM8PC9Gb250PDwvVDFfMCA1MSAwIFIvVFQwIDMwIDAgUi9UVDEgMjYgMCBSPj4vUHJvY1NldFsvUERGL1RleHRdPj4vVHlwZS9QYWdlL0Nyb3BCb3hbMC4wIDAuMCA2MTIuMCA3OTIuMF0vUm90YXRlIDA%2BPgplbmRvYmoKMTIgMCBvYmoKPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAyNTAzPj5zdHJlYW0KeNqlWN1u2zgWfgK%2FA%2B82M7AVUf9qr5zE0%2FUiTTqOOheDAgtGYlwuZNHVTxboY%2FQJ93Ku9wnmI0VSip12B9gEiGOROjznO9%2F541Wx8In6bfeLyx2vWS%2Be%2BbWsZSsOvG9FSVqxuCzoP31CSfG0oONufARZ5gVhTFLfC7OcFIfFxQe2Fw0j8U%2Bk%2BNdiFYSeTwOyol4cY71aXNzIA2sqRipBalmyr0I2nLC6lvu9kGRFrkXfs0o0gpTyMDSiZ60gWpivxNAwI8XN4uLW%2B%2BCRlDTymR8eW04C34%2BXpPEIHU%2BOEi%2F0Q%2BJ7NEj1uX5%2B6aeXgU8jvb6xNu%2Fe4R%2Bx8L2Y%2FHsRkH8svizoaB6J08CL85xkWPQjUh70K4fFKqaxl%2FsJvtSLh8Wvi6sC7yiICotQMIoISEBzD3qkse9FeaoR2txsb7e%2Fb9dkfbUt1sX2tzX58PHq6nZ7vdaqQQwdxQQejQCe72VpqI3QVvsnZs%2BsXmVeGmXqDQP2hw96NV2l8eUt33sK9or1TKlWidIKoZTs%2BF7W7MCbXqpNvOPloJ0zwQWUgE%2Bce1mQkSSB64OExH7qAarEC2LScsDxAzAo3oU7Uj%2FxfD%2FQYNxsr%2F%2B%2BXe%2FWv2%2Fv7zbk4f6h2BYfNSY3W3K92RXbX4CLXtWKwK3pCEkeaRM3auO6KO7V5919cb%2Fbbor1txGO0MvH3SMcny5Y2%2FceiRLCSZSSitfkBpDuPDB5jofvay5FUfzppxOfrCikKYwDL09GsduadLLvZVe2IK8kl4xc324etvdLcrXZ3a13N%2FekYb3Ec9HgT7Hb3EFfURM%2FVKSkeaqw60QF9BEMZsOSYPuM%2BoEm%2FpeB1aL%2Fj%2FKR1czgDGNBVl%2Br1CGcuNIF0ftZcC34xJDMi%2BGycTtRyjzyhj%2BJElF4HB4fa1FqIpSDUOHJyA8ibnmqShAjUCMjHJhjEzEeSb0kzow5WESQHxgJT3S7OBV4UXPluPLMHVC9lE3HjvxZ1lx5FH%2BB5VE9fBQaqiNvgJm25Dj8l6j0A%2F1Fgxf7Vi4JE6TjTSf029%2BgLkyvJUmTUYvi58XFjCkgxeXIEHinZJ1GqVIws1anM0EQRRUrEURq6YnVndKi4XsowfpePS8%2F8%2B%2BfG7889wD3deIgyQ0vW95DYcUB2SKHspqMRiB%2FGttfagIk8Agu6%2FDJD7zdc5gOb%2BJ9CV8201HPvBV4GaouIapkVQuSQUVLC8KfwSKcfNAsxbklpAqFwiPrdA4%2FPV3JV2J7gVOXJy6lmZcHgYojP4%2FHbGVywcgUmnhJrktHakKdnnkfZQUp3g%2BoXq%2BEXga1aGa4p%2BxuR%2BYHXpYl5rFoznYeJbZ2ndQLCKUoMAsVt2KTxD5r%2BZdBwKvC7E7twrHlz6Jzz6PQimczD48FystCsxYvzQE0tzGjw8IICTP7NBiTwQvlEJrGvDCzT2uO6DdPE%2BpUk8%2FgvGA1twokVoPUBCdSOsr4LLu%2BFu1vxs3APdKBnGbZaKJxj3bKaIWp5az5ykaSf2x0vd8MrTxy9tZ4Gi%2B4HP14JsVkxq%2FMqB1Qaydr2L5lIKddSvzg%2Bx4e7KNohvJgClzoJT%2BE1KHnlLBiX2Fd0aoyauRSp1PFzM4ZKxBM8szPvaEsVHWMYE0jlkatiJ6ZSWMr89nouApjFOq5M1WqEM2g2ju2HJO67gWQpI4qUTS9acc0BEgbulN76SQn69zZsqnE1DEofltIOdbkAdxfHRn6yQNyg2UhqmkQRBP0LtSsOc1Q1txhSe3WJ3ZAcmetPcyP7WFIOkZI7hzdDUekIel209Q5wOJnnzyr1GqDJ4%2BSUy%2FT2O70l0HokA6QWxyHNdUF4ChR9Y0w1SBZr22vN7%2BYE2KXkzqFUmUtTR1rkOxnZHImWfGWvS4lNLqYIBnv7WoMwlg4qcs1qT34OFj5gZeeZpqJ5pNvnA6zntEc5KcngK1CZXg2p87UWhqi6c6Cxpd%2B%2FDrXqjOuMeRqmxAQOOGZl3yXqnvRQ8dWlTWb2h3f0BwB8qEUaBS4sSB2RAJl7LHJVDh63kqXWs%2FqyXRuyY52H3XlwdHNd%2B6d8Rt8cxR5ye9QY2GCybgmDZIp8ozYNMicaU9cjW%2FWNRHWTrRQrgnpbFpAaHJkGUup2JEf2aEce8il9fNMWG3TWuYSmEor1ng%2FOU%2BUgWtTTds85Y3%2FlYZ1nrKopMmUhafGw2iJISP9QZJ25SCcNun%2B3YATYfZLXvK2rIdOPFtA%2FeRMOg1m9eIIOC3rUDAcDkM3PLVD70I3jy1s0rg2c8ijhTVJ2YiJXJ7vBmuSo%2F9kkgtRO1nbNOG4zyq%2BH9grSvysn2R6ZsswJYE56EgjisCn44j%2FCb2iMRm1KHK9Vlly3ffZJJukjoxH2fbMuc0BZ%2BJmFeOQTPd6YURN%2BNQlt3E%2FDZAYH%2FI4J5GP0phRrY130leu4LhIy8ro2FcGf7VvnGaWl32jC1ZmHeRPLUQDAs%2B6k3zKAJ9tFU%2BdP4URkISz2Kow27iO0VW40x5zeqXCrDN1DY4OaMa7nk81LnNMETZCo%2FSVqD2PWT84Y7afRi9DcMbKVYjMhSCZp%2Bx5B6GmHUy1X4bxob3ZsZc%2FenxSA5sw81plKpgm7pl7Q%2BXaJBldu22eZHvQHc2LcUpNMGpWIiiDotM3WpLQPFGTW4jex85aNPROx2JD%2FARVM45JGKlwjDTT1q%2FPazQ0Fwn1vvNmh9xyAuXQ6hyUGnh1PwA3NTBVxnbxxk73Cjmq53vqka1qy9Q9DUSIUQa%2BYpBq0QRK0rcYItUGm5CYm8jILO2jwUNbJ5G4zXfUMzXmq%2BGRk8%2FM9XwKJeWrcjg4v76dKxZrxQJPvT0ePgrsGEZE%2FvQE%2BqpMohVAp6ViHV%2BADyu5JPxSwmTjplLOJWeplhxqyXh70h1D1wjCH0SquwjVy7RitEFP76VsOzn2sKORQ6up9TCo01QYIIKX01k5IladFXnEdAS6%2B3hhzx%2Bk%2FqaRVBeTwOj6%2Fv3Hu22x%2Fhu52dzerslva%2Fx9t95t79avWBF783uH16RXUt1hvMfkDgaQdQ0V8V%2Brbj0f%2BB50ZcrEd5i3Wxz%2F9pwbiaembblXVwxylKoYcpS9cgQyVqmuSznRMKioUvUMYzgXGt1W9f%2BjXt%2BB4JU7CXXnMw%2BoDnWNEULUUb2JzilBm6gJMqQ4NE0qam4HuZfqKsTmF5rPepEnAV6oAy5NyjB13Nb4ldm9QidhYt77yz%2FjcQmqnk2d3v%2F%2FY6dfGiNjeEGUmkSEuUsFXQ84EaogJzity57mgoK1fUafo8L6DbFdHTpIdxV2tSveF0UW3%2Fr0NkyzB2M%2FBilVm73Qj1%2B7yF4B8TBVLPH9MffuMLMrV9mL2z8BcG9%2BXwplbmRzdHJlYW0KZW5kb2JqCjEzIDAgb2JqCjw8L0NvbnRlbnRzIDE0IDAgUi9NZWRpYUJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9QYXJlbnQgNDMgMCBSL1Jlc291cmNlczw8L0ZvbnQ8PC9UMV8wIDUxIDAgUi9UVDAgMzAgMCBSL1RUMSAyNiAwIFIvVFQyIDM4IDAgUj4%2BL1Byb2NTZXRbL1BERi9UZXh0XT4%2BL1R5cGUvUGFnZS9Dcm9wQm94WzAuMCAwLjAgNjEyLjAgNzkyLjBdL1JvdGF0ZSAwPj4KZW5kb2JqCjE0IDAgb2JqCjw8L0ZpbHRlci9GbGF0ZURlY29kZS9MZW5ndGggMTY3Nz4%2Bc3RyZWFtCnjahVjJctw2EP0C%2FgNulqo0EAgSXORTLLtcSengZY6qSmE40BgJN3NRqnTPDzj%2BQB%2Bda265pUGim5RoOzWHGQHo%2Fb1uQC%2F2gWDu052Cy3em1IO9N9dN2XS2MkNnC9bZ4HIf%2FipYyPZ3QTifhi%2BZZVxGiqWCR1nO9lVw9kafbK1Zcs72vwU7GXERSrYLuVKwfwzOXjaVro%2BaHS0rm0I%2F2KY2TJdlczrZhu3YtR0GfbS1ZUVTjbUddGfZpEw4NWGUsf3L4OyGv%2BEsZXVzb6pDZ5gUQl2wmrNQTYfjhEciYoKHMp3sivxSpJdShPG0%2FwpjfvcafthAcMX%2BCCT7JfgYhHN4TKWSqzxnGWyKmBXVJFIFOxUqnosE%2FiiD98Hb4MUeZFyK9pghOauQLIl4KhOWZOB6mE4Z%2Bun%2B3nSDqR%2F01eQKiIWzWD5J5SCXcxk7KcFzGU1Sb0ddD80kEHGZO%2BMurKMtPljdab8V8yiO%2FZa201LIRSr9Um%2Fqfl6Vq4NHcyppVeFZ3Q0D9xpymfnVOPFLy0EzV4enOa7EKR5a2Sj9mkpQ18s377zVJZ44VlAkIXw0IRn%2BurECiDEDpWRlCuNehIumHroGQHbh9UqBW3XTVbqsTD3McSieSMrgxmahq9YhlvTEEVqdD%2B8ULALkdlA5CNTttJBJwyAB5adXYAVwXRfm6AwyW7KDqc2dLQD77Xg4lLaYA8omKGRMRgmPIdUJYFEk8QQFviAYgAuQVQlPIIMqSbgKc6ZEykOW5DxjnQGA%2FgieAGSVskRmQNAZnq%2BKwjhW2u%2BhEyEd5vA9gzO8PWcQJWfywtG20kxeff4T5aWXB0ZCZrkQmc98zXpHc8Dk0XS6bhjgEHuBy00xWjbYATqRaxdH27fg1sGWdvgCkpoVpu8B%2BUyz3pxGOOiOmd4U49xWbFU17rjuzOcnwcSSp1nsyoTuPPf1gzxn2VS%2FKJk25O35BlAuWL%2BIyJVbsLhMIGMRU9HVhpV%2FPUmU4HGcrRJ18wn7o0cdUY9IEaEXGsg8Lt1AkN3ebILYSh%2BbAWUlz%2BLsKaOINb3p7u0DNo1coBV7sqYGNG%2F6lAW4dzUJkLtACt06hmhHwdl0upIzGx8BNt6b78fRr8KQ8bdb5S5WPJLrQsPcOjl4AVnBL%2B2Q3JphIqoeB5iGw5fPz5%2FgSEwK5DyUIo8UWMniLVLihEDgjyUq%2BQZWBEWvrlAfhfZ%2FYPm53rgA5GpOGlNP8GzHfxDC5EbbAX1qmLnGAyiOJGFjGtzex4zQa0tMf0hj5nFrVtSaa5oCUUqRj3azVo9FaQiHVME7Xc1sRidoByGglujmSMzGhxHhE65yD42FDser1BUebA4sMWTpEVrudWmPAApvYmEB4BljWjET87TwKvRzbj0UW9MhXIhthaPGhU9HnNHyB%2BR0TAWsjWuKcGvyTq24bTf2i7FDDatFmJW2HqdLoJ%2BIInxKR7XUujWFhUT0A9DLcC9BlbzRm7bzGEhLu%2Fy60Y1o9NHEq77gtnpECCWvfEZV3MVQ9GiumJw34bpZGi9CDvYjQnKVFqyeouxB9wL%2BQw%2BgzvoUegug3UWjWPXgaEOVhT3l2JwwjAW8S8PNF5p0SOGQsNyOXfHh703bGA%2BW7MPNiPKJ95mM6tnafzHUHxlXiNNF1%2B8VtrV0qSy0zWePZhWwhjhq643etmvazprhC96nIkr42I933bg0keUKiu07hxpkq%2FrqA8B%2BGvx8nvbLO0VlPIEqpHEGD4jpxnLzbByglzw8NAwNhGq5q0L3x%2FGhFN39YErhcpSsYri3teOAYT6KTOSkyj9eAF5p%2Bnh4oqqMVN2eXb95D1cphHWOaryTADLyBj2EdEZ4B31fgYskTSi51t2R%2FA5DMgZMx%2BUFowW8TezdhB8MJ0ooM3ZrFEYjkM7iU27aT%2BCmgNGi60sid7ikqJWZj6Nt3WzWaHRxXx%2FR6KJirCn89FsFcuhwNIEX4wyOenJvVSNJtlc1SmT4uEa35xeID5XHS4sm45QGuFtstWA%2B3EsID35l1IcjYgYeCwkkbdP3rsVdoMMEz76BXjuMti8MbWarYUbKVgZKNJpTtip4g%2FS2WqpMaIO51nQGoZErjLs0J0ggJUTk0QaLi%2F93Ft5VqEMl6YKW5tTpO6rz6r3aQsxYwGglAy3aV1WJ%2BXKOVa2a4wiPg6nwhWnYreOuPcBjgs0kfm1HmB9szVH3PwKZZtOLwuFNSnay4wleH1KEcNen19V%2Fbt5N%2BgplbmRzdHJlYW0KZW5kb2JqCjE1IDAgb2JqCjw8L0NvbnRlbnRzIDE2IDAgUi9NZWRpYUJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9QYXJlbnQgNDMgMCBSL1Jlc291cmNlczw8L0ZvbnQ8PC9UMV8wIDUxIDAgUi9UVDAgMzAgMCBSL1RUMSAyNiAwIFIvVFQyIDM0IDAgUi9UVDMgMzggMCBSPj4vUHJvY1NldFsvUERGL1RleHRdPj4vVHlwZS9QYWdlL0Nyb3BCb3hbMC4wIDAuMCA2MTIuMCA3OTIuMF0vUm90YXRlIDA%2BPgplbmRvYmoKMTYgMCBvYmoKPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAxNzM3Pj5zdHJlYW0KeNqtV9tu40YS%2FQL9Q73Fs7BaZPOePMmWdpeB1vLITB6CARZtsq30gmQrJKUA8xnzhfs4z%2FmCVHU3KY2cCWaQ2IZls7tup05deFfMPKDvbj9b7GQtBnWS97rWnWrk0KkSOjVbFP5%2FPfCheJn59jZ%2B8DRlPIgg8ViQZlA0s5tHsVetgOQNFP%2BbzXnAPJ%2FD3GdRhOfV7GalG9FWAioFtS7Fe6VbCaKu9X6vNMzhXg2DqFSroNTNsVWD6BQYZR6p8YMUitXsZsMeGSTQ6pNsnjsJ3POiW2gZ%2BJG5HMYs8ALwmM8TY9fLFl6y4J4fmvP1GPPuX%2FiHmnksgl9nHL6f%2FTLzbXgQJZxFWQYpHnohlI0RaWbzyI9Y5sX4Tz17mr2d3RUoQxAVI0LcquDAecgC1JEEGfMjC9F6lW%2Fyn%2FIlLO%2FyYlnkPy7h8Ye7u01%2BvzS%2BoR7f6uHMDxE9j6VJYKIwYXtXcV%2BEPU9ZEqYk4dB%2BfDSnyTyJFhu5Z4R7JQZBvlWqHJX4PuzkXteike2g6ZLsZXk02TnjhTAhQHHE%2FCyBOIpZmqYQeQlDrGLGI%2Bgk4vEnaPhxyOIggzjDzzgwaKzy%2B3%2Fny93yp3z7sIan7VORFz8YUFY53K93Rf5PBMacGk8wsYnFJAtNjGu6uCyKLX0%2BbIvtLl8Xyw8Wj4Bl9rbF492N6IaBQRiDhDCBStawQkx3DLl8CYjnGTaFYfTuzVVS5j5q4wGQbt%2BqzWvo9TDovuyQvhoWAu4366d8ewt3693DcrfaQisGjc9VDV5AVEQMkbGqhV%2BOolbD%2Fwn00ZIDjmcsRc54xkaPFSJJORbkz0pWmCg5CXDnmpPAQuFGyF7wWRI5LT4zT6YDPh7U4qIe0ZVje65KokOlavVeCRDPWJLYIAQcjs%2FPtSoFOKT%2FxCr%2FnFVEo9Qt9pjnI0amMKR9R9o1HGRHWB3EfuQkpqoUrXPvs74eOvQNr18n7cI9tNiLgzzpWpJW%2FN3J%2FkAPn5XJxEG2mBIojwqD%2FA2oXyEpVGtc1bcgFPSy7ZWR%2FoCMUiV2S0jiSz6dexY3HQuptLC8wpyXojewVpRL0ZlQFGCglSgRbDp6EXVPzrRyj76IYaDn5c%2Fy8%2BYjY97YLf4xu2mQI71qNKxk2ckB%2FSam6Q57r3Cwa0TNQfCpJwgIPsIm0%2BOnbGS3l4iA4Uinsfu0cJKdQhn08PZsspKlqDqkMrr4LFv5oiiaE%2BYPLTeGsWi3RK2KUHgWven919ZH%2FYNCq7dXReEnjCoaB4vr7GMDeVWmeDGMYM5ZwIOvrAbE%2BXUFv0PmNMgTEwdB3R7LWmrMVIO8IYpgcNAI4mFHECos0veCIn3s9An5o8QFVgqKjpj97g18hH6w7eEq2ISlge98cgovqt5FetNKYsT%2BiIcK7s3YNFkVF3Ync9%2B6eo0QxsSgk1p0jKAEYgdKtqLH5GK91xdMjqPAcNldvdutH4rtQ36%2FNZSyfc1MJPrPX%2BAPjhbvLB8m%2FFJ%2BuVlawemqEUw%2FmdToIQ9Dks5S4yd1BkpETTQ6YWm0GG42gRJYUIy9LIuNPX1COulrgnCWxgRAgmn%2FenpcZeqm%2FkZR%2B58Wmm%2BW%2BW59ZRInRxBwpwQb%2BQuRHDEGZEClXkflAXyS2%2FKPc2txcsqnNjdlHL77Ky2aYv1IHD5obAa97Vs98ZC4bf4%2BuMo1BUCtCatdgm6wK%2B1p10Bhs3OYZBfU7Fthiv6LPAv%2BFs%2F26ljRAKtNP%2Bv7s1cjbf3wyr9pIl90cYvol7gdfpXbh444Wik76waJfaYTrbRj7xPIrPt6ct%2FMExwPsheTw9RfPgPEd9fExQKgHRkbauIawZKqxch%2Be8XfzCxyGeAWzjzsBX5KG3Jo9ri3R4GuG4GA8Sx20Y6d3R3hNozFbI%2BEctB4yVgTZq6Zp%2FziYkUjcHwajXfNJuc0ZLi9Odhj9%2Bh80W6wHkuy8UmYjJcubNTuWRSPulaPO2f1HM84xl00%2FmT44ysrbmFzkFyYGuM%2BC0%2FT%2BNbp5d541OquccPTnEUs5hOCr2yWojlQmic9YTBaFa7v40PKNy6xsWuoiKTdhD6spxFtJw1uYOMc1%2BPCZwNKDRVS4GGE1EZKJEggfBUkKrisTGt%2FirMmjcAPE5ZFqbmyOeq9pkUHKWxvZ9iHOb2wJXasvyiMm7xa2HY3EmnEYe4kzASzDZx98Zc1mTBvgpn99S%2BXDC%2BKAZllaylvT4rWUSzQTmBZUeHiVqu7wZSzae7dCRsTIgFuNAcs9eNzZ7nbFf8pijTaeP4mSNInF33EQmIUvhEHf%2FSCi%2FM9DEyaPc%2B%2BJ%2B1wnaM9cHyf%2Bx0nwVhzCmVuZHN0cmVhbQplbmRvYmoKMTcgMCBvYmoKPDwvQ29udGVudHMgMjMgMCBSL01lZGlhQm94WzAuMCAwLjAgNjEyLjAgNzkyLjBdL1BhcmVudCA0MyAwIFIvUmVzb3VyY2VzPDwvRm9udDw8L1QxXzAgNTEgMCBSL1QxXzEgMTggMCBSL1RUMCAzMCAwIFIvVFQxIDI2IDAgUj4%2BL1Byb2NTZXRbL1BERi9UZXh0XT4%2BL1R5cGUvUGFnZS9Dcm9wQm94WzAuMCAwLjAgNjEyLjAgNzkyLjBdL1JvdGF0ZSAwPj4KZW5kb2JqCjE4IDAgb2JqCjw8L0Jhc2VGb250L1pVRUdKVCtTeW1ib2wvU3VidHlwZS9UeXBlMS9UeXBlL0ZvbnQvRm9udERlc2NyaXB0b3IgMTkgMCBSL0VuY29kaW5nIDIxIDAgUi9GaXJzdENoYXIgMC9MYXN0Q2hhciAyNTUvV2lkdGhzIDIyIDAgUj4%2BCmVuZG9iagoxOSAwIG9iago8PC9UeXBlL0ZvbnREZXNjcmlwdG9yL0ZvbnROYW1lL1pVRUdKVCtTeW1ib2wvRmxhZ3MgMjAvRm9udEJCb3hbLTE4MCAtMjkzIDEwOTAgMTAxMF0vSXRhbGljQW5nbGUgMC9Bc2NlbnQgMC9EZXNjZW50IDAvQ2FwSGVpZ2h0IDAvWEhlaWdodCAwL1N0ZW1WIDg1L1N0ZW1IIDkyL0ZvbnRGaWxlMyAyMCAwIFIvQ2hhclNldCgvLm5vdGRlZi9idWxsZXQpPj4KZW5kb2JqCjIwIDAgb2JqCjw8L0xlbmd0aCAyNjcvU3VidHlwZS9UeXBlMUMvRmlsdGVyL0ZsYXRlRGVjb2RlPj5zdHJlYW0KeNpjZGBhYWBkYWBgYARi9uDK3KT8HISA9Q9Zxh%2FSTD%2BkmX9I86j9EGf57fF7569rv9pYZRkYln%2Fh%2FSHHIwpkMXzmB5HfBEEkL5BgVBUCGQw1FYj5gDgTiBshNhgYGOoZGFg45xdUFmWmZ5QoaCRrKhhaWpjqgEhzMGkJIi0NwKS5gmNKflKqQnBlcUlqbrGCZ15yflFBflFiSWqKnoJjTo4C2JhihaLU4tSiMqCgvj9QxC0%2FrySksiBVQR9EGiqkpKYB3cBQwsDEyMii9b2P70fCd9bvfxj3fv%2FD%2FEPv%2B07Rd1Z3NNStrDR65NRvW73vkXt35%2FZ7eb6KeT9NZ%2F2OncH2nOsBNwBp7FqmCmVuZHN0cmVhbQplbmRvYmoKMjEgMCBvYmoKPDwvVHlwZS9FbmNvZGluZy9EaWZmZXJlbmNlc1sxODMvYnVsbGV0XT4%2BCmVuZG9iagoyMiAwIG9iagpbMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAwIDI1MCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMCAwIDAgMjUwIDAgMCAwIDAgMjUwIDAgNDYwIDAgMCAwIDAgMCAwIDI1MCAwIDI1MCAwIDAgMCAwIDAgMCAwIDAgMjUwIDAgMCAyNTAgMCAwIDAgMCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMjUwIDI1MCAyNTAgMCAyNTAgMCAyNTAgMjUwIDI1MCAyNTAgMCAwIDAgMCAyNTAgMjUwIDI1MCAyNTAgMjUwIDAgMjUwIDI1MCAyNTAgMCAyNTAgMjUwIDAgMCAwIDAgMjUwIDI1MCAyNTAgMjUwXQplbmRvYmoKMjMgMCBvYmoKPDwvRmlsdGVyL0ZsYXRlRGVjb2RlL0xlbmd0aCAxMzUxPj5zdHJlYW0KeNqdV11u4zYQPoHuwLdmgZghKVE%2FBfrgON7AhWOnjrcPRYAFIzFeFpLoleVsscfoSfrW6%2FQafeuIP1ISx%2BtFESSRqZlv%2Fr6ZoS%2FXAUHdT7MJLlayFK16khNd6kZVsm1UjhoVXKzpR4IoWj8G1ErDP5ammIUcJQSHaYbWVXB2KzaqFih9h9a%2FByMWYkIZGlHMObwvgrMrXYm6EKhQqNS5%2BKp0LZEoS73ZKI1GaKLaVhSqVijX1b5WrWgUMmCkg6FhitZXwdkc32KUoFo%2FyeqhkYgRws9RjRHlRjiKcUhCRDBlibFLsguSXDBCI%2FN%2B6mNeXcODCgjm6EvA0M%2FB54Da8BBPGOZZhlJ4SSKUV0alCkaccpyRGD6UwV3wCxy%2FUItxElHISYKT7JlalKV40DmQj2OO0yw%2BJn%2B5BoWuDGtfhdTqpwaAZihOGU5Taqpwl3%2BSkOTtvm4lJBZtZYMKm3kTPsBQB8MsDOthGMck4QbmSrQCCVBu9w2gNcCMXPzoEZwjGbidQaYJ5OiNTA%2BmRlZyxHAYp0Z0Cu4dhePcyEyWNx8Ws%2FX4B3Q1nc%2FH6Ncx%2FL0er2aL8VH8iFqy3bXNvpJ1q982MgIrjBrJec9FIGZPx%2B3%2B4aFUuT4eSGjZNSvAinqE7EDvnLZGoxEb8ZgzFn4jSVFshG8b3epcg0%2FHcanNp65LVctvIIZGbKXyT0p2LsuTrk7m07vZEl1OV4vx6mqJ7uHk7nKxSvichPMwSRf3704aXEAVGt119BZSDJk6aTY6mfK7VnxHXWcFGBQnHbz9sFhPr69nSz9pCEhEZtKsl%2BvxfHqMpWliAWjMzzmxQ8%2BhRzjLmH2J0WS5uJr9NlsupmgKz8ub2WQMuZweJA9GXNYNOsJtmLeuhbXp4VJAEutCWapKeNYVkM5gsAxnrOtmr6bMcWiGlz3l9JyQV5GMwgjHNO5sJqENhr3w9%2F34ZjafjVdT8Pe989fMYsJikyHhDhlmjDtLk77UncNI1U%2BiVIVq%2F0E5TBPbKQdlAcgs7JDD2PY%2B%2FWvgjLw%2F2zSi0M%2BxfoIlRJz1EGa9mckvw4dhxkIfPnkr%2FBiz%2BIXVhxfxdGQKbSfeqFo3sobdtG3kzgRXyxLV%2B7yUGj2KSpVKwDK6P6vEHyjiZv6qw%2F7o4qSv4qwcdqcNw1iAle8PjHIf2CjksPIMg3hihwJ7CZ78b%2Bw3ksYwTS1n8mNJW7zOjqnortUIFtS%2BRjsYtbKFj5XoaG7dlPBGI%2BC8%2BtcLqMPW9GEpd9mwpqGDuO274phPt9BK6o3CQVWVGYuuhH0FffvTj76GgzFm0%2Fz3QSNHdo%2Bldi2quot2WC0NLIpu2exk8wT9jNRGyVrlCmnTMQfHctfKxoX63BFrpyNTGp9wxE4UdwUAhdBPC1HCltRuXIS%2B8sVeOsk09BzpuHPukhrytNcvxYFo4TucwTDzlgrl5Gjidd31wlmP415felQwxbwpdwk59856aVW%2FBi73evPJUyPCEdwcgQL2YqCqbaOhBKIy1YYL54OCbhYFEvBgbwLHMx2F9LsyPfjEhkyb%2BjvvKeu9V92i8ucx8xNbFA4Cetqe6N22uxRLP9yphyhtg2AvaNntxEhvqme7e5Px6CCFvbuf9wJuTx5jKI2G5TukNmF9Vz4vMck8zN4jhzTqveuuBH0V49f8GJR3rai%2FSkeEtAfoacz6AMpujnjKZUNYj7LpBoszlvCezC5jw8nnPZBOH9iCQaMq72zE4p6L8kntnMnuu06SPONYAbX%2BE37lBi4qiFltyCLc8TlcPOxGGub2szU9DGL4mvIfoU52aAplbmRzdHJlYW0KZW5kb2JqCjI0IDAgb2JqCjw8L0NvbnRlbnRzIDI1IDAgUi9NZWRpYUJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9QYXJlbnQgNDMgMCBSL1Jlc291cmNlczw8L0ZvbnQ8PC9UMV8wIDUxIDAgUi9UMV8xIDU1IDAgUi9UVDAgMzAgMCBSL1RUMSAyNiAwIFI%2BPi9Qcm9jU2V0Wy9QREYvVGV4dF0%2BPi9UeXBlL1BhZ2UvQ3JvcEJveFswLjAgMC4wIDYxMi4wIDc5Mi4wXS9Sb3RhdGUgMD4%2BCmVuZG9iagoyNSAwIG9iago8PC9GaWx0ZXIvRmxhdGVEZWNvZGUvTGVuZ3RoIDkxMj4%2Bc3RyZWFtCnjalVXbbuM2EP0C%2FcM8JqjF8CLqskAesk5SuDCSdCvsQxBgwZUZg4VEurTkFvnafkpHFOVk0wS7CwOWRHLOzJw5M%2FxYJxTGn98mZ590q3pz0EvXOm863XvTgDfJWc2%2BUGBQPyZsOo0PXpaECwkFJaKsoO6Skzu1NVZBdQr1n0nKBaGMQ8qIlLi%2FSU4uXafsRsHGQOsa9WSc1aDa1m23xkEKS9P3amOsgcZ1gzW98gYCGB1hmCihvkxO1uSOQAHWHXT31WvglMoFWAJMhsNZTgQVQAnjRfBLqzNanHHKsrB%2FNef86Vd8MQklEv5OOPyW%2FJWwKT2QBSeyqqDETZpB0wWTLkklk6SiOX60yR%2FJ78nHGm1GiuqZIT5BcJA5yascCl4RkfPAkH44DSFwwnkOKSVU5CHEO6%2F32j4FbjD5HTJje4Pk48NjUQBp2%2Fd%2B6PvB6wWMmwtABrXdaEBbs0Fro1oTAAavFjNvlJRVFnhT%2B73Z96MXNG0at23N9OF1M%2By0d%2BCefUCjfKM9VkDDw0mn%2FhlrvhvQb0whjTkIIngWcnh8L7mls2bYaoRvnD2gwGyvoVUHh5mhxDRo2L3IvzMW5QdWt2CHptUOHlVnWqO8%2FsYzJ8WkK0FgeXtzubpf3d5cwfp2ebFe3d9f1KvPFxj7OoYVNETRMHDxXqwX1gYOZ04VGAt33h2MbUwIr%2FYYv4ukcPkNKSgDNskAEbH2o8eMvQN8HjuFl6QsStRMEMKIFpYFKWQeV5lYSDp7iEJLRUYqGSpQxAb7%2Br2sVI%2F9bfp%2FZ%2FbNQc2JvF1didLN5iJuzNSyGsvoOtOMtlfRgI3dnsVwz6clwso5LckWdEogZRnhNMwFio9X0MdKI%2FT1DM1IKfj%2FoOeVSj5DT0G8DT2OnNY8Pc1Zr38sclbN1L%2BIvKrKqVqR7pGk9wCQI4BfAK7D%2F%2BwVqyZlwBI%2FhTVJmQjxktiAHHgIbyHkh1NAhQHLJZFROewLm6VDiql38zKOZtWr46koME5YjgJEdqYA4cNrHJajcnkQ2kT3tfHdGzgFTojjmQiDCqMVGwnIcFiPGyucDQo7C0Xq1UH7vQPTws75XrUaNtrAXvsDVnO6Ewpkjh3vBbTHEfkBjndPIQVwksUGeHUFPPepQPuMYx%2BTLDYRo0c9iTD8XzdmigM9L4u4fjN04%2Bh8MbTPgccqVVRigJio%2FG4XnkP%2Bo8Mgn%2BPDi%2Bw%2FPFv9jQplbmRzdHJlYW0KZW5kb2JqCjI2IDAgb2JqCjw8L0Jhc2VGb250L1JLWEdOWCtUaW1lc05ld1JvbWFuUFNNVC9FbmNvZGluZy9XaW5BbnNpRW5jb2RpbmcvRmlyc3RDaGFyIDAvRm9udERlc2NyaXB0b3IgMjcgMCBSL0xhc3RDaGFyIDI1NS9TdWJ0eXBlL1RydWVUeXBlL1R5cGUvRm9udC9XaWR0aHMgMjkgMCBSPj4KZW5kb2JqCjI3IDAgb2JqCjw8L1R5cGUvRm9udERlc2NyaXB0b3IvRm9udE5hbWUvUktYR05YK1RpbWVzTmV3Um9tYW5QU01UL0ZsYWdzIDUwL0ZvbnRCQm94Wy0xNjggLTIxOCAxMDAwIDg5OF0vSXRhbGljQW5nbGUgMC9Bc2NlbnQgNjgzL0Rlc2NlbnQgLTIxNy9DYXBIZWlnaHQgNjYyL1hIZWlnaHQgNDUwL1N0ZW1WIDg0L1N0ZW1IIDI4L0ZvbnRGaWxlMyAyOCAwIFI%2BPgplbmRvYmoKMjggMCBvYmoKPDwvTGVuZ3RoIDMxMjA3L1N1YnR5cGUvVHlwZTFDL0ZpbHRlci9GbGF0ZURlY29kZT4%2Bc3RyZWFtCnjajHwJfBTl3f%2FzPHPtvbO72fua2TvZJLtkdwMbAhkI4b6UM0hKuFFQkoAoKCWeQFBBq4hHEat4QWu4A1pFq9bzFVtr0VZNW7R4pOVt0Wohu%2F%2FfMxtE%2B%2F7f%2F%2BefYZ555plnZp%2F5Hd%2Ff8TwDwgghI%2BpEDJo6ZVqqxjZj9mJo%2BSPsrQuvnN8Wu2z0Awjhejh%2FduGa1VIh%2FdEZOP8GIeGDJW1Lr%2FzVY354gvZdhPhBS1esXfKzX7hfQ0jUIHRLbNni%2BYs%2BGvfzjxC6Jw331y6DBuvftVfD%2BTI4jyy7cvW1g18vuw7Ob4NnKitWLpyPrL8gCG1%2FE85HXTn%2F2jYhwv8coR0i9Jeumn%2Fl4n8%2B1X4ezuF55MdtK1et5jfn9iB0fzdc%2F0tbx%2BK2%2FKevGhF64G6EzE2IYXJ4G%2BKQhrufy8ATo6UjswstIVbMEcIzHMsRhu1FFcXj6NoR8BQt7Gj6pEYJKUgqnuduK4zGGUHGzygIF4tFuPtubiIdDWKhxEA3SkEDnNExJNR2DZQVaCgahsahmWgOmocuR2vQOujRi06jL9E%2FJLfkl2L0adAzjiqh5wg0Ue05H634j54%2B2rP4l%2BKHxT8W%2F1A8WXyv%2BNvi28X%2FKr5ZfK14d3FxcV5xbnF2b01vurdcHcv%2F1x93B%2BwTURB2H3M38iJU%2FBPsp2A%2FXRgP770chQtXFHsZG3T%2B%2BcCOUBRtRw%2BhCDqDB6EX0XE0Hj0GI5%2BK7kZj0NvoaWRCa%2FEbQIEwGoWeQFEcRASNRk7MofvQ%2B2gu6kCfwJsl0AT0EbbCc5pQG3KgfPEzKCegTcWj0EuHGtEv0DG8Ak9DKaiPJZU4Cb%2B8FVjkRIniW8WTcPZT9AmOFPehsVD7FFmAihvQnciKrkCvF89T6UIL0OP4evwZklEr2sJm2a7icuDJIfQ7PAFqk9Ba7qT2EFD7TvQIduLjxY%2BLf0XPsRgthifdiDbBiPej46SaaeR2AZdiwKPJwJ3F6Dr0PrbhQYxSjBdHFu%2BD1sfRP0iSvMIIMI4k8Hweuh09DNR4D51CX2E9zuGf4j2wvYP%2Fxp2EsU1AVwOPO2Hkj8G9e9FRPAgPIk7iBGo5UTmaAde2ot3w%2BwfQCTwBN%2BPj%2BAVmN5cuNBTLivbiX0FyKtBsGOFD6AX4jbM4DX3gF5gQs5oNsKu5mv4b4A0XoQfRCfQOjOMjoPtX6BtcAdufyI%2FJhuKs4hPFT2AsGhREQ9AlIHsrQUavQT8Drr6IXkL%2Fjc8RLfR8m32ZW8edKd4FtI2hkTD2KdB7Gjx7C3BpP%2BqB7T14SwuW4C2G4Mn4UrwUb8XbcQ9%2BH79PeCKTdvI50828wfyRreW4Yh08yYEC8LthNAstAw78GKh9F7zvE%2Bhl9Bq24xiugjd6D%2B7%2Fmgwlo2B7hLxNPmJuYbay57lbC72FLwrnil1IACkbA3S4Gj0FVPg7dsAYyvEVeBX%2BC4x8GznImBiRCTM5ZgQznWlmNjF3M68y%2F8V2sHvYD7hx3HxujzC%2FcFXhneKE4s2qNvMwLqqPWTQY5GcJSNNyGF8bbB3oenQD6kJ3gLzchXahPfDez6PX0O%2FQh6CnZzHCMoz5cvj1K0HqbsF3wHYf3otfwC%2Fj1%2FCf8Nd0IyHYEqSWNJBGMposJbfAdjc5Qd4jpxkfs5DZwHTCtpM5zLzPIpZli1wNbGO5Ldzj%2FBtCQhgrLNC8eb6vv6K%2Fuf%2BjAip4CpcVthdeKPy1OLO4FsYfRVWoGka6EUZ5H8jgbtieAkk8jF5Bb6Lfq2P9ByaYA4l34TBIQyVwrQGPweNgm4QvgW0GbLPwHNjm4wV4GWwbcCe%2BEd%2BEb8a343vUbQe82278JD4M2xF8DLbf4Y%2Fxp%2Fhz%2FA8CQkwYkOYoiZMUycObNpIxZAq5FLalZCVsbaSDrAEOPU4OkKPkPcbGRJkqZj7TztzH%2FIJ5kXmX%2BZYlbCWbYuvZmexS9ib2bfYd9iR7jgtyTdwybif3Iu%2Fls%2FwM%2Fgp%2BB%2F80f5o%2FL%2FDCVGGBcL3wrlDURAGtfg3vfegHkJfi38aruDL2WvIx6IWLaeM24hlAMZ5MZ1YwdzC%2F4ZbgM4yEP8BdzOXM8uIjzGjyDbMSzyTP4xAT5OqYJeg2VMR7yJ%2FIWfJX1o6nk89wgr0THyErmUbCq7j6W9bO3sSdBvv0e1RH1uPj5GXmJuam4i9RHbcTf8ztJO8gie0lNvQxaPVGci%2Fc9F%2FkcrIFzWaz3Dl0OdD9Se5aoPdwsglXMO%2ByO9EnTJj8E5%2FB2wE13sLj2Qj5EcnjPYC4%2FTiA%2BnA7asP3IAU%2Fgz%2FEPQjjJ5jH8URiAG51EyMeDEbhLUbG7zI61EzHiGPEjqeSM2QG8yx%2FAmwkBpT4DVqHGZwG2bnwV0BXgQbcTeKAaU2AJr%2FFNciF7gW8P1t4liI2d5LbAnL2MFOJLkVp1ELeQHWgG5%2FANhvdimrQMZDBTShNdqDri514EeD%2BJMBPgnrwFSiF9YCWThjbBrAXDhICLJwHv%2FoN4P%2FrgPoT8N%2FQNVgCzTqOEiy9chvbBMjUCvi7BbZFqAXOHkR38Ye436Ip2IkQKxV2gpT%2FEf0IbM5f4Pc9qB7GNwc9zFbCqCVA5na448HCWLDtCozwDUzQehjzcNDzqexYQN7txSvgDS8HGzURbOJr6PLivagReHdp8abiFjSv%2BHBxLlqKphWfAPxdU9yPatFGrpnM5JJsFjD2NfwS2KM%2F4C2A22PRB4BHUexCn8P2Cxj%2FcO4Z1MX%2BHrCzoXhb8XfIDvQIAYUWgBU9ha5EfwO6jWWOo0xhMtlXHM20gYX6GF1SfLwYxDq0rLgCkPdZtFvgAHs6UYDbDbK7hV1C0jDecuTAKWidyz2EkDJyxnSlYfiw%2BqF1%2BSGDa3PZTM2gdKq6qjJZUZ6Ix6KRcEiWggG%2Fz%2Btxu5yOMpvVIppNRoNep9UIPMcyBKPKpvDoVqk71trNxsJjx1bR8%2FB8aJj%2FvYbWbgmaRv%2BwT7fUqnaTfthTgZ5L%2FqOnUuqpfNcTi1I9qq%2BqlJrCUvdbo8JSD55zyWyo3z4q3Cx196n1SWp9m1o3Ql2W4QapybVslNSNW6Wm7tFrlnU1tY6Cx%2B3T6xrDjYt1VZVon04PVT3Uup3htn3YORyrFeJsqttHkMYIg%2Br2hEc1dbvDo%2BgIuplo0%2FxF3VMvmd00yivLzVWV3bhxYXhBNwqP7DYn1S6oUf2Zbr6xW1B%2FRrqcvg3aIu2rPN51W4%2BIFrQmDYvCi%2BbPnd3NzG%2Bmv2FJwu%2BO6nauO%2BW6eAoPtzbO3vj9q16mq8l1uURPu7o2St27Lpn9%2FasyLZub4RndJDq6tWs0%2FPBtQMIJ0yT4LXJL8%2BxufAv8oETfg75T6e0Wh5toS%2BsVUrc2PDK8rOuKVmCMp6sbXbpW3u%2FxKEeLvcjTJHVNnx2Wuxu84eb5o3z7ylDXpWsPuBXJ%2FcMrVZX7REuJrPtM5oGKwfj9yuLvrqk1tTutTbj0O7piOqLwOBCHbmmhBCOZHYZ3GkKLxUNQ18Ih0A3%2BmjHc1b0I%2BHF5t7axtUusg3aR3t%2FNRcWw1PUVAv6H%2B778Ycv8gRY%2BKn6FaJVKyXeCBtcv1LuTye6KCiogQiNwFMY4XD3PVVWu6SHd4TZRggOQD00F2s5vrksB8WWZsndLj4IWwEl35yWzS%2BcSWuDdj5RUsrmbtNIrxy9csc%2BgVzovXPnu9tYwyPFB1YO3d2ti3%2F0ziw5b07K6buz4f1xeXLo%2BYVp4wiVzZktNXa0DtJ0w%2FQdnpetDvrs2UMOlC0DwbjYKlBoXBtG7dM5s2gD%2FuOjocNPlrWNB1WCM3bbG2YyXNJdqxMuojwL5nfvdk%2BnJbAN9FhvlVflf1CNoQIDVFiyN7hZbx5bKZp0s%2F3%2Fe1FM8Q%2B9SDxdvG3in7rrkD8%2BH%2FuD8B8MzdDEwYDZGJkyf09Wl%2B8G10QBWXV2jw9Lortau%2BT3FzgVhSQx3HWVmM7O72ppaL7C%2Fp3hsi7d79G3N8BLLcB2INkEj94Xxpkv2KXjTtDmzj0LMKm2aPns%2FwaSxdWTzvghcm31UAnxWWwltpY30RKInYPNAK%2FYTjdrfe1RBqFO9yqoN6vnCHozUNs2FNowW9pBSm3ihjUAbW2pT1Db6R5Gicfrs78uAqljNVdTaE%2BwD78XHQYALfvWkfQQ%2FQ54Df1ggz%2B9HHNtDnjvIIJ1AK4cwcmt47nm4ThCDy5EWL8c%2FQq6k%2BHV9f%2F1k8Wz9pP561AB18TwUg9KyRbZEocA%2BFp2XmOPnFQ6dAy%2FoOAz%2BEiaMPRBtGtAlildn6gwsrdW7KnJZwxAoevSv6k%2FqT%2BtZA%2FgM04%2FwjMnp9GhRD5NTdAaD9kqm0zj9Ufq7fflU32SxafGoT1HDJPqTuCOZTNqYWC5bm6lx2MsEZmetM1tVNdRTCz%2BYuC6p1A1NR%2B8svE%2B9%2FumF8eR6iIhtqE4Jb7c8biG3GjZbiG6H1oJ2QKyHkE77hCk0lcd8Z9n0H9EfbOnrr68X4S37GvoGge%2BDW7A9Fo%2BRnIgG23me2MucAUKuv3fxtgdxzdfX7Zwse8avL6yMTlxyJ%2B56F9fi4lUVo74sbH%2F5vae7Hr8fxlANY5ipjiGvRMrZCs1YjoEft8AgbODSaXUwAIlP8wrP8J322Y%2F%2Bz0HgFlvO4XRY7SIScrW11lw2Xk2qdyze%2BmDh7X9d99Ak2T3hem5RxYQldxWu%2BV3h9QK%2BKtr0BV7%2B8u%2B6ux6jI7iTTAWf%2BA7w5T5XbrdPv8V8We1R0%2FOeA6OPXPq251ejP%2FK8O1ozmBtqGmKu8wyN5QbXjs5cqinziyGxvmxE2ciyxkpvZdMw77Cmyd7JTfO885rWulb5VjWuHbvJdavvlsbNY3e4tvvuadwx9inXY77djXvGvR57fbB0ybjGPFszMTu6lvUk4xG%2FU2SDOiOK19awuiQbbKi%2BPlTfg48oYWt2yvUCQjuMv7Nng7%2BL76j9XUODNDE98e2JJyayE2%2BZdsU6lST1Yn9%2F%2F9n%2BPtRw9mx%2F%2FSlssTrzFms%2BP3C8UFV51t6SxMC0CzLCC7WDay9UeQFoSWvhEHSJw4laV3vXDqbX6RU4GUw32mXgOQ78N9YVGhQJVuf4wZWjRobkqpE3T8pkJ6ydUBUIjBtePozYI2lvNGCvNnBDK8dFPb5Qurzc2zp8aO2E6%2FxVVQF5%2FFVsWdOoBVH4pZqqh0eFa6bEqoLROr%2FF4TU5RsYDlRXjBiXzjWuSicF%2Be3V6Y6Ymnr7UIVb73IOthjKj22v22OWUt6piA5Xuqwp78A70KnKiaUq8mTQ7X3IwWmer%2B4Sb0WIksKxZY0WHrYpBz9aZ7UF7p52x9%2BAKRR80zzMTs9v1IAgbaHXLpP4WIGvfKWsel0gIQt9uA1EDSYuFQxcIUiLgVUvbtYKgj1rLBtVNqB25dGthT2Vo61SbUVumrcsMGr1q3tJ9dHTTcCeZTZyAOg2KRLhO%2F6LaDRxEJAR1MwwiIp4K1nob3oVPYB734Owh1MlOn0NZ3d9CZT%2FVByUdStIm2%2BVphOs%2FR5z30iffWTyFV0KMoEdJxYcUXs8oWqUup1UacvO0%2BCHt01qivcVQEpt2gAv6boPS0ZoBpsObYJRSRlRXjxjxolpWpxQ141c8RYaDnjDoUkWLuDeCS2tBQXuYuGIkTBkhMGxAUz0gVlApk5g008q0MbuYXoZnnsE%2FJ2%2BwPXjlvo9V1DpLCVrfUL%2BRq06uF18alE5iCMrJ8IJ9Kv6Cu%2BPfM7mn4FlofPE0c4RbhkQUQcf2z9eAW8zv5zg7PRiNnh5sVqxaD4opMaLEWmO7Yr0xNmahzaZ5aCUEWFvRLsQhd%2FQYDgBpB7gJgNnS%2FvWkvgH4aFyrTMSRcCQUITyBUJDwQtTn9XsDXoa3xcxRfczldroJL7OWBSjIexbgMhPUHAaoRbC0AHs1UFhF%2BwLk1kEB%2BJvEtKhQ94qKG2xZ62CQDqfDUkZUVRosOh2ZGtAfS5bqDxUhMv621XNaH7z%2BgU2%2FXfDiDVe%2B1JRvr10dqE5H8uV1o3Jjs2TnaTzl0hEPvVx4%2BsvC4Xs%2BeeFfhdP77pnfsRfnTz%2BwKi0Pm1Z4EHh0BswYDxRzoHuVMsXV6trl6nWxyKW4yBoI%2FIhphA1fjkeA5doFMRij1jVQD6u5bTO%2BHOKpEVD%2Fh2LCZjPREsxpNQbCoGP4X9B9nGI1mcyKJZc2bzBvM%2B8ys2a38xiJ4FMDxE3WTxL7TlFoBu5aqMLk0Vd95%2FFXyeQA8tiiGUuZw%2BG0y7nhJEcJQN%2F%2FDB4v2%2BrnFkjrEIdOiHqiI9lfP3xuY8eQAIlGiX%2FQOvLHuyukQJDKYSW84x54xwBeptwouPR5p8s3LOtSoHDTwhxwOMqFemGc8KTAK9Jl7BzNZc45ruWa1ZbV1gf1PzXdZ9mr32t6jXvN%2Barrfef7rl7pW%2FZbp92O%2Fayb89rdDrfT7xK0Tr1L78%2B6x7g3O7dKgstNiNPjNrh5I%2BMmHO%2BiwCjYWGMPDEOrVcoMDZ1arO1hMopB5Dxb3fgh99Nu4j7GZIBwtx%2FAxBDowbcrRsT%2FeYptnm2lbYONtfVgQbFRX8aDJEXqlJhWaZdEJPcz%2BFvQMyNWlLJ5ZCXZQLaS58nb5GPyd3CA3MFj%2BI6L8nyqviTRLZNArUSqWH39Le31Df3t%2B3jq%2BBzZqsXPa9%2FWEtTS3pw8NWAQMLUIRCx1Objefbsbrjeb6jeK3PqXTKCSuL2jBTgGQoySmJFzCOWywCpeCA%2FYCjAFRJBrwCowe%2Bad74WAQtp51aKHYlH32w%2Fs%2FjA9%2FrFvh%2BMFK2aN9mCucC6KR%2BIdT97w2NXtR195d9vSpT87VDgzRBxURZEQtHwm8LMGTzyKdMXe%2FYa8tqd4XKk35Edom3Sj9RNC7NtaXF4%2BpFzJtmbfzvZm%2F6UTUBaP0G4Ir6t%2BKnI0cqz6teqPwx9H%2F1D9eeizqGGcprwH33YgkRBRDzl14EQap3uY7CGGEx3Y0YMfOuRXkqmsvwc3HhCN5Yln8DJUhrTkL4p%2BKvCAbFN5AJw80G3Ahh68DdqrOqvItqpdVaQK2g%2FNEzbAu%2FeQTxSdksW7ssezJAu4N%2FyIYnveRmzuDAWc098xSOVOX0v7WVqcAj8RoCfZ19HQ19JnBQdOxaDa6lQgpjOzfEgOyxE5KrM8FzXFYjoAlxRbtQAHzFCT9fEFWKet5tMLcNDop2gj1pfgJllxA%2FypOtaB2sEJrFUxB%2FjkUJklDxgpsO4q%2BuRU7ImFw1QPKWeFZXX7bn5k1shj6zvb7ip8sXlhSnZ7LNc6oxVL7g17gsntk6UpD429ofWBZez4zfdcMWXO3TsHHb6u%2B4YnRsX9lRqugdfvXDFlwhB%2FYkRA96Obpyzd8BjFcPD7maPAXR0yot8rCYcRm1GTUTEzihlXGLBdAMDFjJbjMWvQGxFrMLK8wQha5VOsgqZMEDQahhV4gwYFjdj4DH4QfHM9fkgxcpjXanhew7EGA%2FsMHgf6osFLFL1Wa2bwQ8zTDGF68L8UF25Q1cuMWwGves2MmVcELLhN39Oh9nqVQ%2FWgQFD9VKRefEM%2BJYKFFfvE%2Fo56S96iKszG6iQL9opWzWYzIFoHOMDtHdgetoQtcg5n4ICZo4d3979Irr5qdyGCz95RuB8v6WRuPH8bebh%2FHsWvBSDva7mJSMYBpfFRFlubA5cHNnAb%2BA3%2B29jb%2FUKO5OQZzAxplrzct4Zb69tIujxdvkeYJ7S7wr1hMwpjs2ix2uwOp6YMLC9DSWWRZDC5rCR7vD5GcLEctD50QJJk2zFAEhdjU4Cm%2BM%2BI%2FFmWEQtoPhx58ZhDncIuKsf4K5DjMFbCrWESBgX59rBIdslYpg9RtJIi7hKJ6A4dw%2Ffgz1SKnWoBmBdbKHVU0T4FoAN1sKeqQAPqU5TZqKlOckAuRE9KQKMYO3AH6ZBuxDeSGyUeEIcCDeAMhMCKfjm70roo0Ma1%2BbmWZnCyBFlgVf%2BT%2F56PNSC81PPEzNrJhWXNWPvALbNuvmTV2nUrq8OeeGrCpKv37dxy5bOY5SY%2BdTi%2Bc1PP8sOd8cHTanxJUc7u23Dd7%2BqqBGKm0jkbeLEPpNOFEui8UnG1do3uGtON2vejn0V5nsHrmXXsOsctTrZek%2BA5JuxOuHlGmqfBGsCOw1IMx2JmcM5uP%2BBCHHVODpiNGIirUB4pVr0HVSgVRKlordhV0VvBVrhLdIdLyCbaJFvapti22XbZBJu7%2FKKLch4czlMDPooKFQDoQNWWvg4gI75Iy4N63ssTlYSAH5W%2BqNbq9wV8hLdEjbGoNgwIIXoXINkEtYgutgD7rNICFDJAgS74KBQ0VMjAdhMjXMB16qNYstZIbQbz9rLvKA7gz2y%2F%2BfFHlke23bnlzaXXv7ll%2FnN3YfM3y%2FvftI4ZnRk3a%2FOm9bFZ3LKoccrPfr15YW%2F3U7c9NfcA9h%2FGYwuz%2B0dtnNb6p5GpR3fs%2BbcEWjCxeIrZDVqgRy8cRWyx94DNO5zrKfYqSai4NZhjKrQjkWJsNe4yvo5fIyfxSdJrBJJiPUZGxcgQjgWP8ieKhyFlDENYxsgpY3LcnzEPB%2F7PGMS8B993eJce690G7hg5jRjyV8WAWJFV2KnsLpZjnyWfIsMA3WlUeUqF67PUgibFvmTJP91oWv%2FSgPBqV3Or%2BZu5m3l2QHDBQnYAHcEDB%2FdVBjdOiP8X%2BX2hvg3fU9jSnp6e8XMTY%2F9%2Bjn3ZW92qpxmG60HeukDe3CiGMnidcqwZQtxMMFMRX5lZF%2BrUdxo6PZ3eG6Odsa7Mk67dnsejBwwHPUdiz8Rf1r2s%2F73RISAd5o3Eo407jE5P1Bg1TcC34ZuMt5ieRKahqA5PQBPwuMQ8fFl8buYKdAW%2BnCyNXRFflrkOXx9fU3l9Ziu7lesUOjU3Wm60bi3b6tjBbtfcbdlufcDxWOzn8Z9netjDms%2F0nxs%2BM30W%2F6ymXDBq43Uoj4fUcKM0yOCJs2ohOlVfnOeq6MFm9I%2FQAq5rQfLpnoa6CFgsopySI0quNbcr15tjc%2BFn4QIDOlABOqBLOxXnNifjdGeP4b8NAAt1z8%2BqoNJ36mzJQ6cCj2nUBUJek0wFQhYHq7FHZS4M7rjgX4AryyoWoGorWMQQCyYyQN3xpKNqAUpZqkqiPiDr1D5SsGmnXIt9L%2Fz9QZAbrR2QdSr5Np4eBqwl3vxwy5tPPvrqij3d%2BYkf7Hthxcy1eNC1ypolSzpzg2qnTb39yhU3xsaQPTfvmnnz8%2Fs7Ju5cvmnykvatb6ydv2rOvvdWrJ9y%2BTVrpmSXpQp%2FHb279YYH1s0am78CMOgS0IQnQCacKI4NSua6%2BPvc70Pvx9ll7FpuvWad9hrDtca1tmukLZqbbDqtZms5Garh4i457uKYQJRFAncML0QurByMTwXLBsikaFPRlVHwnFGAssfEAUbddtDpREYXRSAPNh9BVtEqWRlrD14MaFSulHeWM0p5a%2Fmu8t5ythxTDJOhm6J7Xkd07sQP%2FJm%2BkkPTX0L9hgFwEs%2F2qXkGZ8m1VPlV4Y1oLIaYGPXFwrGgUV6A%2FGYaNmmgJukDEDtZoAhpo9%2BHJMoo1SY4aTZncAn5Bw84MwTQCVMGlTikQtOKG3vfKf%2Fphq1vLrnulcevueujVx5%2BjmSsI9dOar61ecS86h%2F7ouRqHHl68YdH9m95smvPuT8X1t5wBTl64%2BT5f7p2187fXjOzkkbdEDVvY7oBj5xo5D7G3YOjit%2B4tHabexcEfwoSDADoZsUOwXR2m32XndifxVGwG7%2FBqIQeZ1XfeyAjlcTfC6dt3w%2BtZRpQw16ZGjGSHpnuUoxdPaLfNrJUG0kt0x2FPRBb74PxhNFCxStHX7AsrX3F%2FFKIGIxem13UGg67DHRcZT3MZCUYUFwQ55u1QQjua71inVkOyp0yI7%2FqdUdoqA%2BMo2kMUY28%2B2GUKfGUyieVVbj9BwNm%2FpfcBvYMDP5H%2F5nkYPYppbEr%2F%2F73%2F0x3EBTFf2DnMqvUHOv1%2B3ihh4kf5qaw81jCPsdMpOlUJk5z%2FMVOZWgdhBqdiEyFYhc6gXohgud5jiNExPgExmms4F2YQVjEEoTrc7UCyzIMmquZrSYo61Wv7esWcMvUKmpoaU%2FW99eDe0fzozRZQrcoO%2Fjca3RnVl169lIYgIgQ180tRz4UJK59RMV7Kw4GSMCPwKoifxCDbS17jvkzcsIuwK5j%2Fqw4NcQXYMwan8OPgm24ExOMNWaiQakGqhZvnXgrlaI6Ifb1%2Fe1LnCr9ies3vvSSCPugtFfxakxms1HUBbTBqTJvN9tEj8Xj9fpcfl6GMGh%2FNEcPB9Kzs%2BoxWa0e95eXmqVYqdkTKDU71eb9dvWg3CvaskazHh6eN483jxbHBabIzeZZ4oyy2YErzEvFZYE1Yie70dRl3ihutG4ObAo%2BYH5AvM%2FyQOCo%2Baj4S8%2FRwBvm18VX%2Fa8H%2FmA%2BKX5hPi2eDnxr%2Fkb81v9toFJrnuAlQbCeQCTkDwR8WpPOq3X4nF6Hhghejd1S5rVfGzCLkhjw%2BUIWsczSZsF0ytXUQ15TLCRQRkgg6N%2BNUIlwPfiQYtCIZsbucGg0Wo2vB%2F9b0ZrhHrLbpFh6SPrAlAAO9JAvFZOkmKaazpgY0%2BPS8i5V%2F9wekG%2BXh7qkNAamvIfyLE2M1m80lTzRjS2maldyI0S4SRcS%2B7B4%2FH%2BWG8X1L9UL9fBPdU2TF%2F5wB%2FiksqCaBbuco9lPnMGlTIaqLnrCPNn%2Fz7mhoQsKM2a4M8Pxh2F8Mt8yrf%2BzS%2FKJqz79Er%2Fy3pR4MCVEo2ZX%2Bifs3HM7Nl3CRaNstVw5DxtJpP%2BP1EMIIcR%2BCn5RACXRELJeSc9BcwKb0abA5sx9np%2FG93r2xj%2FzfB7%2Fa8owBK2Lr83cX3NfZnfkqcxJz8n4yYSOreshfz1gXlpbR6XCF8rSo%2FIXuzObUeRKKNyBbI0STkDh9WdHRUZFN3vex%2B9FPsh8EhXYCI4aa0TGzns9ZQFHxJGwp6trmiLjs7PwbPec%2BHZiEZFYNwPPibTWtdV11u2q03jSnpqpiBEFTySQcKdYnjABZ2BKZlPk%2Fsj7GUGqU%2Bqm1i0kC5lWrpVvFVrTa%2FhVnlXetsDqyKr4usTN%2FK3eWwNbM511r6c%2BSH0R%2BXfE3awxB71aOSQGvQ45nIkghq1EuWQwwoTKh1RmmOpQIpfTOsoTTqeDVCeopGwDT5yKfV1OPYykh84DDSOy9PRA42j1qJRB%2B8R5PqwLpH3EN4NNBodUDqIXxKacVQGPkCAoelmGpY06oyWLWCyxGNzMd5RoJW%2BzkRmVBrOZlkYjlCGQZbNIZpglemrema97Fr%2BDZDQfu8AmJCefTSbrJ%2FWB7PQDBrW0N84%2BigYxVZ951UNfM4Tu9VRCO%2FpUAesomVPYLTSIUtM0zlLg6czT%2FBkY1BGpbDjhCmDB43V7Cc%2FHImDmM7GEK5bBKWFQBocDsQyTxYMyTNxbnsFprjqDov5QBgVqmFwGPF2xPln%2FPVNbyhpAEIs7OjpQR%2Ft37hKiyZ%2BSY8SH5VymZnCtmq0Lh3NyTWmGIOqg1rfkLQmWgSBBTREx%2B28fPb%2Fz40%2F6OzMzok5%2FfFKGjH904fad1%2FdfF52Xv%2Bsnk188tmjq6vZDz818cevw2V5yMDBy7i2Lj86I1oY7mBU%2FliujrsiRa5Y8bBaEhhsnXfOE49xK7yPXTrlrOsvRCHp88U%2BcGbA6gokyUhtI4RRJMangdvN9gUfMj1gPm49Y9ZoAjB4CuOvs1zpuZ7ocP2W2e%2FYyzzBaA2NiiX8s08xwKY1oiXghTOAOES%2FGx1APM%2BGwdD%2BX8DG4h3x8yJLsFrHYw4w4tNX4kJEYe5iUkirTkr0IY1wj7n3agoOWBguxeBQQQG295MJmV9BFXKp4uMZFFy1ULW%2BypUPNOn%2Fd0Q7ubTu1wO1nW85%2B2tD35VmAHOrxvqayV7J7eYMQ9cT0MUeU92qrkMEOhcbNVWGd01hFvST8fR%2BpAyI3W1glOrGXWdWsspNnwxJ1Za0R6jNRzg1m3wkGh3%2F68MYP1q%2Fp23Hz62uDSwpnnik8fbTrMG745U%2B2Vli9ZR49t7yQefvw5sK7H%2FcU%2FrGt%2FYmyQ0%2F8%2B9j5N%2FD0Z8Y6bF669JjmiTmau3CAd8gozXqv3n%2BreI%2F4O5FbI64p2yjusN1nf837mv9dUeOyWMv8AUaw442eTQGS0PBBL4IYKeg1ymGn7A4mTCYjcSccDqTx1U%2Bx4pJLmrYqVs7aU%2FzoMKWhdVyY6uLwhpwSxlIYt4VpDoQJy05VG52qNjpVcjvBWTGIoI282sh7aCO%2FMzR%2FgAdUF%2FvVErzXjuTXKlMuqlz%2Bgor5PAGzXYyWxQJm30zssUPhtwRnYq%2FNPfMC%2BWnQDBrT0p75oWJIrNUuCrwcB6ojwErQi3BmZsThoxqQANdl2At7Xyhc%2FYcNM0%2FjmsJ%2FnZmzKjpYXsWs2CBVRrsKz%2F228Mlz7y7w4dHYid14lJ%2FKegXYg4NA8QyuVRqU3FLfNb4H0k%2B69qafSffmNDPdbXybsEGzQdvJdwpbNVu12kjQ65dD0aA3KYc1CiWIRjaZglqvRqCklGmLIBMS5L2CT%2FQSHAb%2Fw59Bu5PVqEqkCU7yWzAVlUkQqN1%2B72mfz6%2FR7tVo%2BL0NNOuJBFGYIjDwrE%2BVqeqz1lTvrUwGq1Jw6wrPXgk8mo%2B9jHfa1FwbhH1MDokqq0SVK6LKKjEUjaisiqiNEZVVkZ3Z3qN4o%2BpMUzapvAKdaek723KqH9jV0levZrfFL8Giw6GgmnaASnDsqCsr9n2JxK%2BSeOA4MOPQgi0y1YCMJaymO2U6%2B5BRZ18GZ5gSsF1kINUlqOG9uGJ1PMtHoyaT9dIZhffExJBPVy1LDx%2BRuPrcF%2Bl0UnJ6ItPTrN0ct2dqEos50n86XL26kFjoCycKI%2BbEnVJq%2BPrC3qhTVBYy7TcEEtHC75dPtZspR2XgKF1%2FXoUr9iVSPTigDI4uqtWyWl13itmRPJZ8Jfk%2B89vkZ%2BxnunPsOZ22jWvjNwCPO7lOfivwWCPotBVEkA2GHhxTjBqv4A96nXKIB6bSlnLOy5tU2xkIemNyOFmZ0GkMLDjQOAzkd1ahcAwlxARJUE5H4%2FEYcTg18WRiLyrHqDwNwWAbxIDbeD4o4CkCfl4NKg8p1cikctKkMs2kctIUCvhVTvrVRr%2FKSf%2FO6v%2BhdGdVf%2FzrlnY1agTu%2Fa3lO%2BaVpqcpC5MD3Ou%2FcAQWttMUaxJbKMuAidUkHLZA3AfAlrF%2Fzy5d4B9cx4%2F8a8YUYzSK402j%2FmXUSZXpQf3H0tNjLqMuCELB%2FLcx7GlafAUw7YsJKwu5KeOjhZlLZbfVFY0OktYxK0r1wnvzmhOUX2PB2jwF1iaLW5TpOnZ0NXHHPQkiukQ3kWqV2tbaazVtrjb3tRXbXNvc3a5ut74qtUa%2FUc%2B4aqs9U2vbam9jf8721rIG5lb98VpmrAb44vpnyEq5Fs6q9ueAan%2FwAfAAJyiNg%2B6vdLpcIT5RyZgSIS1OBgMGSvmASuQAT4kcCFksU63brMRsnWIlFDs3WItW1spSblgBQE8dVAG0h3yj6HX1U2PYHAvGCDhCZxSRPiYm0uuxcblFXQO8AkAEPUslVVapXDulBrSUS%2BIFSzWAklkpKYiaaCJeHq%2BIM7wBHBGzbBmKpaBoEZK6KmQMQyFKpqFIG%2BersD5qqhoI8alnrk6NqiYsqeoodT2oIQMuStTFLlkyC3UncrKdxqF2C%2FghqlmjCw8u5IEHs58B26evfa7Qv7F9%2Bz87J9w2IjjiUmJ0T%2FaXrerdXLjmzftmLtl%2Fzxvj164cYrN5GTBx03ddcvVbP%2F%2F7i4Xj98SieNOSBjkWy0avLMwfXnf%2Bl%2F868OivLp%2FlKreHM8B5au1%2BCprahK8pRYRHxiiUaCjaU%2Fz6EOVINNtTPK9YaTWryn5WZVHWBh0UG2224ZDKu5CqL6Ge4mlFVZiQ2jHkGSFCJOmHvRL2FOzVyAClFvYG2OshxtQPQ5FI9TBS7dMR1JBSI8u3IKD88ku1wClqko6%2FlaTHD5PHB6WTXqW9bcyuMSfG9I5hbWN2%2BpTaqVAlIHF6ORQKen1yKBv0VsuhpqB3uBwiQa9ODtuCXq8cBsNRJYdzQe8wOQwUCEci3uHDhun1OlJdVeXzeTVWW4goIfxxCEuhdKgttCt0ItQb4kM9RFI84pjWMcfHMNIYPKYpGspNzbZmSXbn6Pl%2FdCUniWc76AIpsb1DBQN1qdRAlAZbCQoueKWlHAoNuC7qtioGIAf"; // shortened
		//var pdfAsArray = convertDataURIToBinary(pdfAsDataUri);
		//$scope.linkPdf = pdfAsArray;
		//$scope.setLoading(false);
    	
    	var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "GetPDF", null, $scope.authHeaders, value);	

    	myDataPromise.then(function(result){
    		if(result.error != null){
    			$dialogs.notify("Attenzione", JSON.stringify(result.error));
    			$scope.setPdfCorrect(false);
    			$scope.setLoading(false);
    		} else {		
    			//$scope.pdfResponse = result.result;
    			$scope.linkPdf = 'data:application/pdf;base64,' + encodeURIComponent($base64.encode(result));//result.result.link;
    			//$scope.linkPdf = 'data:application/octet-stream; Content-Disposition: attachment;base64,' + encodeURIComponent($base64.encode(result));//result.result.link;
    			//$scope.namePdf = result.result.attachment.name;
    			//console.log("Respons Pdf " + JSON.stringify(result));
    			//console.log("Url Pdf " + JSON.stringify($scope.linkPdf));
    			
    			//var pdfAsDataUri = "data:application/pdf;base64," + encodeURIComponent($base64.encode(result)); // shortened
    			//var pdfAsArray = convertDataURIToBinary(pdfAsDataUri);
    			//$scope.linkPdf = pdfAsArray;
    			$scope.setPdfCorrect(true);
    			if(type == 1){
    				$scope.continueNextTab();
    			} else {
    				$scope.continueNextEditTab();
    			}
	    		$scope.setLoading(false);
    		}
    	});
    };
    
    $scope.setPdfCorrect = function(value){
    	$scope.isPdfCorrectly = value;
    };
    
    // Method used to pay
    $scope.pagamento = {};
    $scope.payPratica = function(type){
    	var paga = {
    		idDomanda: $scope.practice.idObj,	
    		identificativo: $scope.pagamento.cf,
    		oraEmissione: $scope.pagamento.ora,
    		giornoEmissione: $scope.correctDateIt($scope.pagamento.giorno)
    	};
    	
    	var value = JSON.stringify(paga);
    	console.log("Dati pagamento : " + value);
    	
    	var method = 'POST';
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "Pagamento", null, $scope.authHeaders, value);	

    	myDataPromise.then(function(result){
    		console.log("Respons pagamento " + JSON.stringify(result));
    		$scope.getSchedaPDF(type);	// I call here the function for PDF becouse I need to wait the response of pay before proceed
	    	//$scope.setLoading(false);
    	});
    };
    
    $scope.protocolla = function(){
    	$scope.setLoading(true);
    	
    	var method = 'GET';
    	var params = {
    		idDomanda:$scope.practice.idObj,
    		idEnte:"24",
    		userIdentity: $scope.userCF
    	};
    	
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "GetDatiPratica", params, $scope.authHeaders, null);	

    	myDataPromise.then(function(result){
    		if(result.esito == 'OK'){
	    		$scope.practice = result.domanda;
	    		$scope.accetta(result.domanda);
    		} else {
    			$dialogs.notify("Errore","Errore nella conferma della pratica.");
        	   	$scope.setLoading(false);
    		}
    		
    		
    	});

    };
    
    $scope.accetta = function(value){
    		var domandaData = {
            	idDomanda: value.idObj,	
            	userIdentity: $scope.userCF,
            	version : value.versione
        	};
        	       	
            //var value = JSON.stringify($scope.pdfResponse);
        	var value = JSON.stringify(domandaData);
            console.log("Dati protocollazione : " + value);
            	
            var method = 'POST';
            var myDataPromise = invokeWSServiceProxy.getProxy(method, "Accetta", null, $scope.authHeaders, value);	

            //{"segnalazioni":null,"result":"Rifiutata","exception":null,"error":null}
            
            myDataPromise.then(function(result){
            	if(result.result == null && result.segnalazioni != null){
            		var messaggio = '';
            		for(var i = 0; i < result.segnalazioni; i++){
            			messaggio = messaggio.concat("1 - " + JSON.stringify(result.segnalazioni[i]) + " ;<br>"); 
            		}
            		console.log("Errore in protocolla " + messaggio);
            		$dialogs.notify("Insuccesso","Domanda non confermata. Lista errori: " + messaggio);
            	} else if((result.exception != null) && (result.exception != '')){
            		console.log("Errore in protocolla " + result.exception);
            		$dialogs.notify("Insuccesso","Domanda non confermata. Eccezione: " + result.exception);
            	} else {
            		console.log("Respons Protocolla " + JSON.stringify(result));
            		$dialogs.notify("Successo","Domanda creata e confermata dall'utente.");
            	}
        	   	$scope.setLoading(false);
            });
    };
    
    $scope.rifiuta = function(){
    	$scope.setLoading(true);
    	
    	var domandaData = {
            	idDomanda: $scope.practice.idObj,	
                userIdentity: $scope.userCF,
                version : $scope.practice.versione
        };
       	
        //var value = JSON.stringify($scope.pdfResponse);
    	var value = JSON.stringify(domandaData);
        console.log("Dati rifiuta : " + value);
        	
        var method = 'POST';
        var myDataPromise = invokeWSServiceProxy.getProxy(method, "Rifiuta", null, $scope.authHeaders, value);	

        myDataPromise.then(function(result){
        	console.log("Respons Rifiuta " + JSON.stringify(result));
        	$dialogs.notify("Rifiutata","Domanda rifiutata dall'utente.");
    	   	$scope.setLoading(false);
        });

    };
    
    //------------------------------------------------------------------------
      
    
    // This method will connect to a ws. Actually it work locally
    $scope.getMunicipalityById = function(cod){
    	var found = $filter('getById')($scope.municipalities, cod);
    	console.log(found);
        //$scope.selected = JSON.stringify(found);
        return found.name;
    };
    
    
    $scope.isPracticeFrameOpened = function(){
    	return sharedDataService.isOpenPracticeFrame();
    };
                  	
    $scope.showPractice = function(){
    	sharedData.setShowHome(true);
    };
    
    // For user shared data
    $scope.getUserName = function(){
  	  return sharedDataService.getName();
    };
    
    $scope.getUserSurname = function(){
  	  return sharedDataService.getSurname();
    };
                  
    // Used for modal dialog
    $scope.modalShown = false;
    $scope.toggleModal = function() {
      $scope.modalShown = !$scope.modalShown;
    };
                  	
    // for next and prev in practice list
    $scope.currentPage = 0;
    $scope.numberOfPages = function(type){
    	if(type == 1){
    		if($scope.practicesEdilWS != null){
    			return Math.ceil($scope.practicesEdilWS.length/$scope.maxPractices);
    		} else {
    			return 0;
    		}
    	} else {
    		if($scope.practicesAssWS != null){
    			return Math.ceil($scope.practicesAssWS.length/$scope.maxPractices);
    		} else {
    			return 0;
    		}
    	}
    };
                      
    $scope.practices = [];
    
    $scope.getPracticesByTypeWS = function(type) {
    	$scope.setLoadingPractice(true);
    	var method = 'GET';
    	var params = {
			idEnte:"24",
			userIdentity: $scope.userCF
		};
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "RicercaPratiche", params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		$scope.practicesWS = result.domanda;
    		$scope.getPracticesMyWebByType(type);
    	});
    };
    
 // Method that read the list of the practices from the local mongo DB
    $scope.getPracticesMyWebByType = function(type) {
    	$scope.setLoadingPractice(true);
    	var method = 'GET';
    	var params = {
			userIdentity: $scope.userCF
		};
    	var myDataPromise = invokeWSServiceProxy.getProxy(method, "GetPraticheMyWeb", params, $scope.authHeaders, null);
    	myDataPromise.then(function(result){
    		$scope.practicesMy = result;
    		//console.log("Pratiche recuperate da myweb: " + $scope.practicesMy);
    		$scope.mergePracticesData($scope.practicesWS, $scope.practicesMy, type);
    		$scope.setLoadingPractice(false);
    	});
    };
    
    // Method that add the correct status value to every practice in list
    // It merge the value from two lists: practices from ws and practices from local mongo
    $scope.mergePracticesData = function(practiceListWs, practiceListMy, type){
    	var practicesWSM = [];
    	for(var i = 0; i < practiceListWs.length; i++){
    		for(var j = 0; j < practiceListMy.length; j++){
    			if(practiceListWs[i].idObj == practiceListMy[j].idDomanda){
    				practiceListWs[i].myStatus = practiceListMy[j].status;
    				if((practiceListMy[j].status == 'EDITABILE') || (practiceListMy[j].status == 'PAGATA') ||  (practiceListMy[j].status == 'ACCETTATA')  ||  (practiceListMy[j].status == 'RIFIUTATA')){
    					practicesWSM.push(practiceListWs[i]);
    				}
    				break;
    			}
    		}
    	}
    	
    	if(type == 1){
			$scope.practicesEdilWS = $scope.getPracticeEdil(practicesWSM, sharedDataService.getUeCitizen());
		} else {
			$scope.practicesAssWS = $scope.getPracticeAss(practicesWSM, sharedDataService.getUeCitizen());
		}
		$scope.setLoadingPractice(false);
    	// I consider only the practices that has been accepted
    	//$scope.practicesWSM = practiceListWs;
    };
    
    $scope.getPracticeAss = function(lista, ue){
    	var pAss = [];
    	for(var i = 0; i < lista.length; i++){
    		if(ue){
    			if((lista[i].edizioneFinanziata.edizione.strumento.tipoStrumento == 'CONTRIBUTO_ALL_PRIVATO') && (lista[i].edizioneFinanziata.categoria == 'COMUNITARI')){
    				pAss.push(lista[i]);
    			}
    		} else {
    			if((lista[i].edizioneFinanziata.edizione.strumento.tipoStrumento == 'CONTRIBUTO_ALL_PRIVATO') && (lista[i].edizioneFinanziata.categoria == 'EXTRACOMUNITARI')){
    				pAss.push(lista[i]);
    			}
    		}
    	}
    	return pAss;
    };
    
    $scope.getPracticeEdil = function(lista, ue){
    	var pEdil = [];
    	for(var i = 0; i < lista.length; i++){
    		if(ue){
    			if((lista[i].edizioneFinanziata.edizione.strumento.tipoStrumento == 'LOCAZIONE_ALL_PUBBLICO') && (lista[i].edizioneFinanziata.categoria == 'COMUNITARI')){
    				pEdil.push(lista[i]);
    			}
    		} else {
    			if((lista[i].edizioneFinanziata.edizione.strumento.tipoStrumento == 'LOCAZIONE_ALL_PUBBLICO') && (lista[i].edizioneFinanziata.categoria == 'EXTRACOMUNITARI')){
    				pEdil.push(lista[i]);
    			}
    		}
    	}
    	return pEdil;
    };
                  	
    // adding practices functions
    $scope.checkId = function(id){
    	if(id < 5){
    		return "Id already exists";
    	}
    };
                  	
    $scope.showStates = function(practice){
    	var selected = [];
    	if(practice){
        	selected = $filter('filter')($scope.states, {value: practice.state});
        }
        return selected.length ? selected[0].text : 'Not set';
    };
    
    //---------------Practice State Section----------------
    $scope.practiceState_editMode = false;
    $scope.insertedEcoIndex = false;
    
    $scope.editPracticeState = function(){
    	$scope.practiceState_editMode = true;
    };
    //---------------EndPractice Section------------
    
    //-------------- Part for dialogs ----------------
    
    $scope.launchDialog = function(which, id, language){
      var dlg = null;
      switch(which){
          
        // Error Dialog
        case 'error':
          dlg = $dialogs.error('This is my error message');
          break;
          
        // Wait / Progress Dialog
        case 'wait':
          dlg = $dialogs.wait(msgs[i++],progress);
          fakeProgress();
          break;
          
        // Notify Dialog
        case 'notify':
          dlg = $dialogs.notify('Something Happened!','Something happened that I need to tell you.');
          break;
          
        // Confirm Dialog
        case 'confirm':
        	if(language == 'active'){
        		dlg = $dialogs.confirm("Conferma cancellazione","Vuoi cancellare la pratica selezionata?");
        	} else {
        		dlg = $dialogs.confirm("Please Confirm","Do you confirm the practice deleting?");
        	}
          dlg.result.then(function(btn){
        	  // yes case
            $scope.deletePractice(id);
          },function(btn){
        	  // no case
          });
          break;
         
        // Create Your Own Dialog
        case 'create':
          dlg = $dialogs.create('/dialogs/whatsyourname.html','whatsYourNameCtrl',{},{key: false,back: 'static'});
          dlg.result.then(function(name){
          },function(){
          });
          
          break;
      }; // end switch
    }; // end launch
    
    // for faking the progress bar in the wait dialog
    var progress = 25;
    var msgs = [
      'Hey! I\'m waiting here...',
      'About half way done...',
      'Almost there?',
      'Woo Hoo! I made it!'
    ];
    var i = 0;
    
    var fakeProgress = function(){
      $timeout(function(){
        if(progress < 100){
          progress += 25;
          $rootScope.$broadcast('dialogs.wait.progress',{msg: msgs[i++],'progress': progress});
          fakeProgress();
        }else{
          $rootScope.$broadcast('dialogs.wait.complete');
        }
      },1000);
    }; // end fakeProgress 
    
    
}]);

cp.controller('InfoCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 'localize', 'sharedDataService',
                          function($scope, $http, $route, $routeParams, $rootScope, localize, sharedDataService, $location, $filter) { // , $location 

	$scope.showInfo = function(value){
    	switch(value){
    		case 0:
    			$scope.showInfo_0 = true;
    			break;
    		case 1:
    			$scope.showInfo_1 = true;
    			break;
    		case 2:
    			$scope.showInfo_2 = true;
    			break;
    		case 3:
    			$scope.showInfo_3 = true;
    			break;
    		case 4:
    			$scope.showInfo_4 = true;
    			break;
    		case 5:
    			$scope.showInfo_5 = true;
    			break;
    		case 6:
    			$scope.showInfo_6 = true;
    			break;
    		case 7:
    			$scope.showInfo_7 = true;
    			break;
    		case 71:
    			$scope.showInfo_71 = true;
    			break;
    		case 72:
    			$scope.showInfo_72 = true;
    			break;	
    		case 8:
    			$scope.showInfo_8 = true;
    			break;		
    		case 9:
    			$scope.showInfo_9 = true;
    			break;
    		case 10:
    			$scope.showInfo_10 = true;
    			break;
    		case 11:
    			$scope.showInfo_11 = true;
    			break;
    		case 12:
    			$scope.showInfo_12 = true;
    			break;
    		case 13:
    			$scope.showInfo_13 = true;
    			break;
    		case 14:
    			$scope.showInfo_14 = true;
    			break;
    		case 15:
    			$scope.showInfo_15 = true;
    			break;
    		case 19:
    			$scope.showInfo_19 = true;
    			break;
    		case 20:
    			$scope.showInfo_20 = true;
    			break;
    		case 111:
    			$scope.showInfo_111 = true;
    			break;	
    		case 112:
    			$scope.showInfo_112 = true;
    			break;	
    		default:
				break;
    	}	
    };
    
    $scope.hideInfo = function(value){
    	switch(value){
    		case 0:
    			$scope.showInfo_0 = false;
    			break;
    		case 1:
    			$scope.showInfo_1 = false;
    			break;
    		case 2:
    			$scope.showInfo_2 = false;
    			break;
    		case 3:
    			$scope.showInfo_3 = false;
    			break;
    		case 4:
    			$scope.showInfo_4 = false;
    			break;
    		case 5:
    			$scope.showInfo_5 = false;
    			break;
    		case 6:
    			$scope.showInfo_6 = false;
    			break;	
    		case 7:
    			$scope.showInfo_7 = false;
    			break;
    		case 71:
    			$scope.showInfo_71 = false;
    			break;
    		case 72:
    			$scope.showInfo_72 = false;
    			break;	
    		case 8:
    			$scope.showInfo_8 = false;
    			break;
    		case 9:
    			$scope.showInfo_9 = false;
    			break;
    		case 10:
    			$scope.showInfo_10 = false;
    			break;
    		case 11:
    			$scope.showInfo_11 = false;
    			break;
    		case 12:
    			$scope.showInfo_12 = false;
    			break;
    		case 13:
    			$scope.showInfo_13 = false;
    			break;
    		case 14:
    			$scope.showInfo_14 = false;
    			break;
    		case 15:
    			$scope.showInfo_15 = false;
    			break;
    		case 19:
    			$scope.showInfo_19 = false;
    			break;	
    		case 20:
    			$scope.showInfo_20 = false;
    			break;
    		case 111:
    			$scope.showInfo_111 = false;
    			break;
    		case 112:
    			$scope.showInfo_112 = false;
    			break;	
    		default:
				break;
    	}	
    };
	
}]);