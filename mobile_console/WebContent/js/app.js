'use strict';

/* App Module */

var cp = angular.module('cp', [ 
	'ngRoute',
	'ngSanitize',
	
	'cpControllers',
	'cpFilters',
	'cpServices',
	
	'ngCookies',
	'xeditable',
	"ui.bootstrap"
]);

cp.config(['$routeProvider',
    function($routeProvider) {
  	$routeProvider
  		.when('/', {
    		templateUrl: 'partials/home.html',
    		controller: 'MainCtrl',
    		controllerAs: 'main'
    	})
    	.when('/Practice/:id', {
    		templateUrl: 'partials/practice.html',
    		controller: 'PracticeCtrl',
    		controllerAs: 'practice_ctrl'
    	})
    	.when('/Practice/new/add', {
    		templateUrl: 'partials/new_practice.html',
    		controller: 'PracticeCtrl',
    		controllerAs: 'practice_ctrl'
    	})
    	.when('/PracticeList/edil', {
    		templateUrl: 'partials/practice_edil_list.html',
    		controller: 'PracticeCtrl',
    		controllerAs: 'practice_ctrl'
    	})
    	.when('/PracticeList/ass', {
    		templateUrl: 'partials/practice_ass_list.html',
    		controller: 'PracticeCtrl',
    		controllerAs: 'practice_ctrl'
    	})
    	.when('/Practice/:id/edit', {
    		templateUrl: 'partials/edit/edit_practice.html',
    		controller: 'MainCtrl',
    		controllerAs: 'main'
    	})
    	.otherwise({
    		redirectTo:'/'
    	});
  			
  	//$locationProvider.html5Mode(true);
}]);