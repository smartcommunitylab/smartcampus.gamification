angular.module('gamificationEngine.home', [])
	.controller('HomeCtrl', function ($scope, $rootScope, $window, $uibModal, gamesFactory, utilsFactory) {
		$rootScope.currentNav = 'home';
		$rootScope.currentGameId = 1;

		// Error alerts object
		$scope.alerts = {
			'loadGameError': false
		};

		// Load games
		gamesFactory.getGames().then(function () {
			$rootScope.games.forEach(function (g) {
				g.terminated = g.expiration && g.expiration <= new Date().getTime();
			});

		}, function () {
			// Reject: show error alert
			$scope.alerts.loadGameError = true;
		});

		$scope.openGameModal = function () {
			// Add new game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_game_edit.html',
				controller: 'EditGameModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return {};
					}
				}
			});
		};

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

		$scope.getLength = function (game, type) {
			return utilsFactory.getLength(game, type);
		};

		$scope.goto = function (path) {
			$window.location.href = path;
		};
	});
