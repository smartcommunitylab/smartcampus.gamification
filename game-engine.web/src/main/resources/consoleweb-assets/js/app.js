/*
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

var app = angular.module('gamificationEngine', ['ui.router', 'ui.bootstrap', 'jm.i18next', 'toggle-switch','ui.bootstrap.datetimepicker']);

// Load and set up the translation library i18next
app.config(function ($i18nextProvider) {
  'use strict';
  $i18nextProvider.options = {
    useCookie: false,
    useLocalStorage: false,
    fallbackLng: 'en',
    resGetPath: 'locales/__lng__/__ns__.json',
    ns: { 
        namespaces: ['messages', 'labels'], 
        defaultNs: 'labels'
      } 
  };
});

// Switch application views and states
app.config(
  function ($stateProvider, $urlRouterProvider) {
    $stateProvider
    .state('home', {
      url: '/home',
      templateUrl: 'templates/home.html',
      controller: 'HomeCtrl',
      data : {
    	  page : 'home'
      }
    })

    .state('game', {
      url: '/game/:id?tab',
      templateUrl: 'templates/game.html',
      controller: 'GameCtrl',
      data : {
    	  page : 'game'
      }
    })
    
    .state('game-monitor', {
      url: '/game-monitor/:id',
      templateUrl: 'templates/game-monitor.html',
      controller: 'MonitorCtrl',
      data : {
    	  page : 'monitor'
      }
    })
    
    $urlRouterProvider.otherwise("/home");
  }
);

app.directive('autofocus', ['$timeout', function($timeout) {
	  return {
		    restrict: 'A',
		    link : function($scope, $element) {
		      $timeout(function() {
		        $element[0].focus();
		      });
		    }
		  }
		}]);
