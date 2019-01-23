angular.module('gamificationEngine.home', [])
	.controller('HomeCtrl', function ($scope, $rootScope, $window, $uibModal, gamesFactory, utilsFactory) {
		$rootScope.currentNav = 'home';
		$rootScope.currentGameId = 1;

		// Error alerts object
		$scope.alerts = {
			'loadGameError': false,
			'error': false
		};

		// Load games
		gamesFactory.getGamesByDomain().then(function () {
			$rootScope.games.forEach(function (g) {
				g.terminated = g.expiration && g.expiration <= new Date().getTime();
			});
		}, function () {
			// Reject: show error alert
			$scope.alerts.loadGameError = true;
		});

		$scope.deleteGame = function (game) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteGameConfirmModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return game;
					}
				}
			});
		};

		$scope.cloneGame = function (game) {
			var g = angular.copy(game);
			g.id = null;
			var fields = {};
			fields.name = game.name + '_cloned';
			fields.expiration = game.expiration && game.neverending ? game.expiration.getTime() : undefined;

			// Edit game
			gamesFactory.editGame(g, fields).then(
				function (data) {
					g.name = data.name;
					g.id = data.id;
					gamesFactory.saveGame(g).then(function () {
						$rootScope.games[0] = g;
						$scope.alerts.error = false;
					}, function () {
						$scope.alerts.error = true;
					});
				},
				function (message) {
					$scope.alerts.error = true;
				});
		};

		$scope.updateStatus = function (game) {
			var g = angular.copy(game);
			g.active = !g.active;
			gamesFactory.saveGame(g).then(function () {
				game.active = !game.active;
				$scope.alerts.error = false;
			}, function () {
				$scope.alerts.error = true;
			});
		};

		$scope.stopGame = function (game) {
			var modalInstance = $uibModal.open({
				templateUrl: 'home/modal_stop_confirm.html',
				controller: 'StopGameConfirmModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return game;
					}
				}
			});
		};

		$scope.goto = function (path) {
			$window.location.href = path;
		};
	});

modals.controller('StopGameConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, game, gamesFactory) {
	$scope.argument = game.name;

	$scope.error = false;

	// DELETE button click event-handler
	$scope.stop = function () {
		var g = angular.copy(game);
		g.stopped = true;
		gamesFactory.saveGame(g).then(function () {
			game.stopped = true;
			$uibModalInstance.close();
		}, function () {
			$scope.error = true;
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	}
});
