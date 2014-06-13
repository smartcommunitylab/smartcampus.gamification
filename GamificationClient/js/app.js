var app = angular.module('gamificationEngine', ['ngRoute', 'ui.bootstrap', 'localization']);

app.config(
  function ($routeProvider) {
    $routeProvider.when('/login', {
      templateUrl: 'templates/login.html',
      controller: 'TmpCtrl'
    })

    .otherwise({
      redirectTo: '/login'
    });
  }
);

app.controller('TmpCtrl',
  function ($scope) {
    // TODO
  }
);
