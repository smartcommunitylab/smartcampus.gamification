angular.module('gamificationEngine.settings', [])
	.controller('SettingsCtrl', function ($scope, $rootScope, $window, $stateParams, gamesFactory) {
		$rootScope.currentNav = 'settings';
		$rootScope.currentGameId = $stateParams.id;

		var game = $scope.game;
		$scope.newGame = {};

		$scope.newGame.name = game.name;
		$scope.newGame.domain = game.domain;

		if (game.expiration) {
			$scope.newGame.expiration = new Date(game.expiration);
		} else {
			$scope.newGame.neverending = true;
			$scope.newGame.expiration = new Date();
		}

		// Error alerts object
		$scope.alerts = {
			'editGameError': false,
			'nameError': '',
			'invalidTime': false,
			'settingsEdited': false
		};

		// OK button click event-handler
		$scope.saveSettings = function () {
			var limit = ($scope.alerts.nameError) ? 1 : 0;
			var valid = true;
			$scope.alerts.settingsEdited = false;
			$scope.alerts.editGameError = false;
			$scope.alerts.nameError = '';
			$scope.alerts.invalidTime = false;


			if (document.getElementsByClassName('has-error').length > limit) {
				$scope.alerts.invalidTime = true;
				valid = false;
			}
			if (!$scope.newGame.name) {
				$scope.alerts.nameError = 'messages:msg_game_name_error';
				valid = false;
			} else if (!!gamesFactory.getGameByName($scope.newGame.name) && game.name !== $scope.newGame.name) {
				$scope.alerts.nameError = 'messages:msg_game_name_exists_error';
				valid = false;
			}

			if (valid) {
				$scope.disabled = true;
				var fields = {};
				fields.name = $scope.newGame.name;
				fields.expiration = $scope.newGame.expiration && !$scope.newGame.neverending ? $scope.newGame.expiration.getTime() : undefined;
				fields.domain = $scope.newGame.domain;

				// Edit game
				gamesFactory.editGame(game, fields).then(
					function (data) {
						// Settings edited
						$scope.game.name = $scope.newGame.name;
						$scope.game.expiration = $scope.newGame.expiration;
						$scope.game.domain = $scope.newGame.domain;
						$scope.alerts.settingsEdited = true;
						$scope.disabled = false;
						if ($scope.new) {
							$scope.goToUrl('#/game/' + data.id);
						}
					},
					function (message) {
						// Show given error alert
						$scope.alerts.editGameError = 'messages:' + message;
						$scope.disabled = false;
					}
				);
			}
		};
		
		// CANCEL button click event-handler
		$scope.cancel = function () {
			$scope.goto('concepts');
		};
	});