var app = angular.module('gamificationEngine', ['ui.bootstrap', 'localization']);

app.config(function ($routeProvider) {
    $routeProvider.when('/login', {
        templateUrl: 'templates/login.html',
        controller: 'TmpCtrl'
    });
    $routeProvider.otherwise({
        redirectTo: '/login'
    });

});

function TmpCtrl($scope) {

}
