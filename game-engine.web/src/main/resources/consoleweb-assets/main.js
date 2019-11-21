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

		$scope.update = function () {
			$rootScope.domain = $scope.domain;
			gamesFactory.setUrl('../consoleapi/' + $rootScope.domain);
			$rootScope.games = [];
			// Load games
			gamesFactory.getGames().then(function () {
				$rootScope.games.forEach(function (g) {
					g.terminated = g.expiration && g.expiration <= new Date().getTime();
				});
			}, function () {
				// Reject: show error alert
				$scope.alerts.loadGameError = true;
			});
		}

		this.$onInit = function () {
			gamesFactory.userProfile().then(function (profile) {
				//profile.domains[1] = "test";
				$rootScope.userProfile = profile;
				$rootScope.domain = profile.domains[0];
				if ($rootScope.domain != 'ROLE_ADMIN') {
					gamesFactory.setUrl('../consoleapi/' + $rootScope.domain);
					// Load games
					gamesFactory.getGames().then(function () {
						$rootScope.games.forEach(function (g) {
							g.terminated = g.expiration && g.expiration <= new Date().getTime();
						});
					}, function () {
						// Reject: show error alert
						$scope.alerts.loadGameError = true;
					});
				}
			});
		}

	});
