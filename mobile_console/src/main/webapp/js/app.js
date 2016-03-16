'use strict';

/* App Module */

var cp = angular.module('cp', [ 
	'localization',
	'ngRoute',
	'ngSanitize',
	
	'cpServices',
	'cpControllers',
	'cpFilters',
	'cpDirectives',
	
	'ngCookies',
	'xeditable',
	'dialogs',
	'ui.bootstrap',
	'base64'
]);

cp.config(['$routeProvider', '$locationProvider',
    function($routeProvider, $locationProvider) {
  	$routeProvider
  		.when('/', {
    		templateUrl: 'partials/profile.html',
    		controller: 'MainCtrl',
    		controllerAs: 'main'
    	})
    	.when('/profile/:id', {
    		templateUrl: 'partials/profile.html',
    		controller: 'MainCtrl',
    		controllerAs: 'main'
    	})
    	.when('/challeng/:id', {
    		templateUrl: 'partials/challeng.html',
    		controller: 'MainCtrl',
    		controllerAs: 'main'
    	})
    	.when('/classification/:id', {
    		templateUrl: 'partials/classification.html',
    		controller: 'MainCtrl',
    		controllerAs: 'main'
    	})
    	.when('/rules', {
    		templateUrl: 'partials/rules.html',
    		controller: 'MainCtrl',
    		controllerAs: 'main'
    	})
    	.otherwise({
    		redirectTo:'/'
    	});
  			
  	$locationProvider.html5Mode(true);
}]);
cp.config(['$compileProvider',
    function( $compileProvider )
    {  
		$compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|ftp|mailto|data|file):/);
        // Angular before v1.2 uses $compileProvider.urlSanitizationWhitelist(...)
    }
]);