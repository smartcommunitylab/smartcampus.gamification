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
    fallbackLng: 'default',
    // attention change resGetPath if http console mapping changing
    resGetPath: 'locales/__lng__/__ns__.json'
  };
});

// Switch application views and states
app.config(
  function ($stateProvider, $urlRouterProvider) {
    $stateProvider
    .state('home', {
      url: '/home',
      templateUrl: 'templates/home.html',
      controller: 'HomeCtrl'
    })

    .state('game', {
      url: '/game/:id?tab',
      templateUrl: 'templates/game.html',
      controller: 'GameCtrl'
    })
    
    .state('game-monitor', {
      url: '/game-monitor/:id',
      templateUrl: 'templates/game-monitor.html',
      controller: 'MonitorCtrl'
    })
    
    /*.state('points', {
      url: '/game/:id/points/:idPoints',
      templateUrl: 'templates/game_points.html',
      controller: 'GamePointsCtrl'
    })

    .state('badges_collection', {
      url: '/game/:id/badges_collections/:idBadgesCollection?tab',
      templateUrl: 'templates/game_badges_collection.html',
      controller: 'GameBadgesCollectionCtrl'
    })

    .state('actions', {
      url: '/game/:id/actions',
      templateUrl: 'templates/actions.html',
      controller: 'ActionsCtrl'
    })
    .state('rules', {
      url: '/game/:id/rules',
      templateUrl: 'templates/rules.html',
      controller: 'RulesCtrl'
    })
     .state('tasks', {
      url: '/game/:id/tasks',
      templateUrl: 'templates/tasks.html',
      controller: 'TasksCtrl'
    })*/

    $urlRouterProvider.otherwise("/home");
  }
);
