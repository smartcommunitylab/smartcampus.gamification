var app = angular.module('gamificationEngine', ['ui.router', 'ui.bootstrap', 'jm.i18next', 'toggle-switch']);

app.config(function ($i18nextProvider) {
  'use strict';
  $i18nextProvider.options = {
    useCookie: false,
    useLocalStorage: false,
    fallbackLng: 'default',
    resGetPath: '../locales/__lng__/__ns__.json'
  };
});

app.config(
  function ($stateProvider, $urlRouterProvider) {
    $stateProvider
      .state('login', {
        url: '/login',
        templateUrl: 'templates/login.html',
        controller: 'LoginCtrl'
      })

    .state('home', {
      url: '/home',
      templateUrl: 'templates/home.html',
      controller: 'HomeCtrl'
    })

    .state('game', {
      url: '/game/:id',
      templateUrl: 'templates/game.html',
      controller: 'GameCtrl'
    })

    .state('points', {
      url: '/game/:id/points/:idPoints',
      templateUrl: 'templates/game_points.html',
      controller: 'GamePointsCtrl'
    })

    .state('badges_collection', {
      url: '/game/:id/badges_collections/:idBadgesCollection',
      templateUrl: 'templates/game_badges_collection.html',
      controller: 'GameBadgesCollectionCtrl'
    })

    .state('leaderboard', {
      url: '/game/:id/leaderboards/:idLeaderboard',
      templateUrl: 'templates/game_leaderboard.html',
      controller: 'GameLeaderboardCtrl'
    })

    .state('actions', {
      url: '/game/:id/actions',
      templateUrl: 'templates/actions.html',
      controller: 'ActionsCtrl'
    })

    $urlRouterProvider.otherwise("/login");
  }
);
