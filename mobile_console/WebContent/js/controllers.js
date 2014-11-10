'use strict';

/* Controllers */

var cpControllers = angular.module('cpControllers', []);

cp.controller('MainCtrl',['$scope', '$http', '$route', '$routeParams', '$rootScope', 
    function($scope, $http, $route, $routeParams, $rootScope, $location, $filter) { // , $location 

    $rootScope.frameOpened = false;

    $scope.$route = $route;
    $scope.$location = $location;
    $scope.$routeParams = $routeParams;
    //this.params = $routeParams;
                  	
    // The tab directive will use this data
    $scope.tabs = [  'Practices', 'Building', 'Other Services' ];
    $scope.tabs.index = 0;
    $scope.tabs.active = function() {
    	return $scope.tabs[$scope.tabs.index];
    };

    $scope.app ;
                  			
    $scope.citizenId = userId;
    $scope.user_token = token;
                  			
    // new elements for view
    $scope.currentView;
    $scope.editMode;
    $scope.currentViewDetails;
                  			
    // max practices displayed in home list
    $scope.maxPractices = 5;
               			
    var homeShowed = true;
    var activeLinkEdil = "";
    var activeLinkAss = "";
                  			
    //this.backUrl = "";
                  			
    $scope.showHome = function(){
    	homeShowed = true;
    };
                  			
    $scope.showPractices = function(type){
        homeShowed = false;
        if(type == 1){
            activeLinkEdil="active";
            activeLinkAss="";
        } else {
            activeLinkEdil="";
            activeLinkAss="active";
        }
    };
                  			
    $scope.consolidedPractices = function(item){
        if(item.state < 4){
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
                  			
    $scope.isHomeShowed = function(){
    	console.log("Is home showed " + homeShowed);
    	return homeShowed;
    };
                  			
    $scope.logout = function() {
        window.document.location = "./logout";
    };
                  		    
    $scope.home = function() {
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
    $scope.user;
    $scope.getUser = function() {
    	console.log("user id " + $scope.citizenId );
    	$http({
        	method : 'GET',
        	url : 'rest/citizen/user/' + $scope.citizenId,
        	params : {},
            headers : $scope.authHeaders
        }).success(function(data) {
        	$scope.user = data;
        }).error(function(data) {
        	// alert("Error");
        });
    };
                  			
    $scope.services = [];
    $scope.getServices = function() {
    	console.log("user id " + $scope.citizenId );
    	$http({
    		method : 'GET',
    		url : 'rest/citizen/user/' + $scope.citizenId + "/services",
    		params : {},
    		headers : $scope.authHeaders
    	}).success(function(data) {
    		$scope.services = data;
       	}).error(function(data) {
        	// alert("Error");
       	});
    };              		    
                  		    
    // ----------------- Practice sections ----------------
    $scope.practices = [];
    $scope.getPractices = function() {
    	$http({
    		method : 'GET',
    		url : 'rest/citizen/' + $scope.citizenId + '/practice/all',
    		params : {},
    		headers : $scope.authHeaders
    	}).success(function(data) {
            $scope.practices = data;
        }).error(function(data) {
            // alert("Error");
        });
    };
                  			
    $scope.practice;
    $scope.getPractice = function(id) {
    	console.log("req id " + id + " ,citizenId " + $scope.citizenId );
    		$http({
    		method : 'GET',
    		url : 'rest/citizen/' + $scope.citizenId + '/practice/' + id,
     		params : {},
     		headers : $scope.authHeaders
     	}).success(function(data) {
        	$scope.practice = data;
        }).error(function(data) {
            // alert("Error");
        });
    };
                  			
    // for next and prev in practice list
    $scope.currentPage = 0;
    $scope.numberOfPages = function(){
    	var consolidedPractices = [];
    	for(var i=0; i < $scope.practices.length; i++){
    		if($scope.practices[i].state < 4){
    			consolidedPractices.push($scope.practices[i]);
    		}
    	}
		return Math.ceil(consolidedPractices.length/$scope.maxPractices);
	};
                  			
    $scope.practiceType = {
    	edil: "Edilizia abitativa",
    	ass: "Assegni familiari"
    };
                  			
    var newPractice = false;
                  			
    $scope.newPracticeShow = function(){
    	newPractice = true;
    	console.log("I am in new practice show - crationMode = " + newPractice );
    };
                  			
    $scope.newPracticeHide = function(){
    	newPractice = false;
    	console.log("I am in new practice hide - crationMode = " + newPractice );
    };
                  			
   	$scope.isNewPractice = function(){
   		return newPractice;
    };
                  			
    $scope.getPracticesByType = function(type) {
        $http({
            method : 'GET',
            url : 'rest/citizen/' + $scope.citizenId + '/practice/type/' + type,
            params : {},
            headers : $scope.authHeaders
        }).success(function(data) {
        	$scope.practices = data;
        }).error(function(data) {
        	alert("Error");
        });
     };
                  			
     // adding practices functions
     $scope.checkId = function(id){
    	 if(id < 5){
             return "Id already exists";
         }
     };
                  			
     $scope.states = [
         {value: 1, text: 'approvata'},
         {value: 2, text: 'da approvare'},
         {value: 3, text: 'consolidata'},
         {value: 4, text: 'da consolidare'},
         {value: 5, text: 'rifiutata'}
     ];
                  			
     $scope.showStates = function(practice){
         var selected = [];
         if(practice){
            selected = $filter('filter')($scope.states, {value: practice.state});
         }
         return selected.length ? selected[0].text : 'Not set';
     };
                  			
     $scope.savePractice = function(){
         console.log("Practice saved!!" );
     };
                  			
     $scope.editPractice = function(id, code, name, type, openingdate, state){
    	 //console.log("I am in editPractice: id = " + id + ", code = "  + code + ", name = "  + name + ", openingdate = "  + openingdate + ", state = " + state);
    	 $http({
    		 method : 'PUT',
    		 url : 'rest/citizen/' + $scope.citizenId + '/practice/' + id,
    		 params : {
    			 "code" : code,
    			 "name" : name,
    			 "type" : type,
    			 "openingdate" : openingdate,
    			 "state" : state
    		 },
    		 headers : $scope.authHeaders
         	}).success(function(data) {
         	}).error(function(data) {
         });
      };			
                  			
      document.getElementById("user_name").innerHTML=user_name;
      document.getElementById("user_surname").innerHTML=user_surname;
                  			
      //document.getElementById("manualCount").innerHTML=$scope.remoteComment.length+" Manual Content";
      //document.getElementById("keywordCount").innerHTML=$scope.localComment.length+" KeyWord Content";
                  			
}]);

cp.controller('PracticeCtrl', ['$scope', '$http', '$routeParams', '$rootScope', '$route', '$location', 
                       function($scope, $http, $routeParams, $rootScope, $route, $location, $filter) { 
	this.$scope = $scope;
    $scope.params = $routeParams;

    //$rootScope.frameOpened = $location.path().endsWith('/Practice/new/add');
    $rootScope.frameOpened = $location.path().match("^/Practice/new/add");
                  	
    $scope.showPractice = function(){
    	sharedData.setShowHome(true);
    };
                  	
    $scope.newPracticeJsp = function(){
        window.location.href = 'html/new_practice.jsp';
    };
                  	
                  	
//  $scope.newPractice = function() {
//      console.log("I'm in new practice url redirect");
//      window.document.location = "./#/Practice/add";
//      //$scope.showHome();
//  };
                  	
    // for next and prev in practice list
    $scope.currentPage = 0;
    $scope.numberOfPages = function(){
        return Math.ceil($scope.practices.length/$scope.maxPractices);
    };
                      
    $scope.practices = [];
    $scope.getPractices = function() {
        $http({
        	method : 'GET',
        	url : 'rest/citizen/' + $scope.citizenId + '/practice/all',
            params : {},
            headers : $scope.authHeaders
        }).success(function(data) {
        	$scope.practices = data;
        }).error(function(data) {
        	alert("Error");
        });
    };
                  	
    $scope.practice;
    $scope.getPractice = function(id) {
    	$http({
    		method : 'GET',
    		url : 'rest/citizen/' + $scope.citizenId + '/practice/' + id,
    		params : {},
    		headers : $scope.authHeaders
    	}).success(function(data) {
    		$scope.practice = data;
    	}).error(function(data) {
    		alert("Error");
    	});
    };
                  	
    $scope.getPracticesByType = function(type) {
    	$http({
    		method : 'GET',
    		url : 'rest/citizen/' + $scope.citizenId + '/practice/type/' + type,
    		params : {},
    		headers : $scope.authHeaders
    	}).success(function(data) {
    		$scope.practices = data;
    	}).error(function(data) {
    		alert("Error");
    	});
    };
                  	
    // adding practices functions
    $scope.checkId = function(id){
    	if(id < 5){
    		return "Id already exists";
    	}
    };
                  	
    $scope.states = [
        {value: 1, text: 'approvata'},
        {value: 2, text: 'da approvare'},
        {value: 3, text: 'consolidata'},
        {value: 4, text: 'da consolidare'},
        {value: 5, text: 'rifiutata'}
    ];
                  	
    $scope.showStates = function(practice){
    	var selected = [];
    	if(practice){
        	selected = $filter('filter')($scope.states, {value: practice.state});
        }
        return selected.length ? selected[0].text : 'Not set';
    };
                  	
    $scope.savePractice = function(){
      	console.log("Practice saved!!" );
    };
                  	
    $scope.editPractice = function(id, code, name, type, openingdate, state){
        //console.log("I am in editPractice: id = " + id + ", code = "  + code + ", name = "  + name + ", openingdate = "  + openingdate + ", state = " + state);
        $http({
            method : 'PUT',
            url : 'rest/citizen/' + $scope.citizenId + '/practice/' + id,
            params : {
            	"code" : code,
            	"name" : name,
            	"type" : type,
                "openingdate" : openingdate,
                "state" : state
            },
            headers : $scope.authHeaders
        }).success(function(data) {
        }).error(function(data) {
        });
    };
                  	
    $scope.deletePractice = function(id){
    	console.log("I am in deletePractice: id = " + id);
        var delete_p = confirm("Confermi la cancellazione di questa pratica?");
        if(delete_p){
        	$http({
            	method : 'DELETE',
            	url : 'rest/citizen/' + $scope.citizenId + '/practice/' + id,
            	params : {
            	},
            	headers : $scope.authHeaders
        	}).success(function(data) {
            	$route.reload();
            	alert("Pratica Id:" + id + " cancellata.");
        	}).error(function(data) {
            	alert("Errore nella rimozione della pratica Id:" + id);
            });
        }
    };
                  	
    // new practice creation functions
    $scope.prepareUserData = function(){
    	console.log("I am in prepareUserData " + sharedData.getName());
    	$scope.username = sharedData.getName();
    	$scope.usersurname = "Ciao";
    };
                  	
}]);