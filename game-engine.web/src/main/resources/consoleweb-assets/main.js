angular.module('gamificationEngine.main', [])
	.controller('MainCtrl', function ($scope, $rootScope, $state, gamesFactory) {
		$rootScope.games = [];
		if ($state.current.data) {
			$rootScope.page = $state.current.data.page;
		}

		gamesFactory.userProfile().then(function (profile) {
			$rootScope.userProfile = profile;
			$rootScope.domain = profile.domains[0];
			if ($rootScope.domain != 'ROLE_ADMIN') {
				gamesFactory.setUrl('../consoleapi/' + $rootScope.domain);
			}
		});

		$scope.goHome = function () {
			$rootScope.page = 'home';
		}
	});
