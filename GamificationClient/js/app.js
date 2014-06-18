var app = angular.module('gamificationEngine', ['ngRoute', 'ui.bootstrap', 'localization', 'jm.i18next', 'toggle-switch']);

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
  function ($routeProvider) {
    $routeProvider.when('/login', {
      templateUrl: 'templates/login.html',
      controller: 'LoginCtrl'
    })

    .when('/home', {
      templateUrl: 'templates/home.html',
      controller: 'HomeCtrl'
    })

    .when('/game/:id', {
      templateUrl: 'templates/game.html',
      controller: 'GameCtrl'
    })

    .when('/game/:id/actions', {
      templateUrl: 'templates/actions.html',
      controller: 'ActionsCtrl'
    })

    .otherwise({
      redirectTo: '/login'
    });
  }
);
