angular.module('gamificationEngine.game', [])
	.controller('GameCtrl', function ($scope, $rootScope, $window, $stateParams, gamesFactory, $state) {
		$rootScope.currentNav = 'concepts';
		$rootScope.currentGameId = $stateParams.id;

		// default
		if ($stateParams.id) {
			$scope.viewName = 'concepts';
			$scope.menuItem = 'concepts';
			$scope.new = false;
		} else {
			$scope.viewName = 'settings';
			$scope.menuItem = 'settings';
			$scope.new = true;
		}

		if ($state.current.data) {
			$rootScope.page = $state.current.data.page;
		}

		// Error alerts object
		$scope.alerts = {
			'loadGameError': false
		};

		$scope.game = {};

		// Load games
		gamesFactory.getGameById($stateParams.id).then(function (game) {
			$scope.game = game;
			$rootScope.gameName = game.name;
		}, function () {
			// Show error alert
			$scope.alerts.loadGameError = true;
		});


		$scope.goto = function (path) {
			$scope.viewName = path;
			$scope.menuItem = path;
		};

		$scope.goToUrl = function (url) {
			$window.location.href = url;
		}

	

	});