angular.module('gamificationEngine.main', [])
	.controller('MainCtrl', function ($scope, $rootScope, $state) {
		$rootScope.games = [];
		if ($state.current.data) {
			$rootScope.page = $state.current.data.page;
		}

		$scope.goHome = function () {
			$rootScope.page = 'home';
		}
	});
