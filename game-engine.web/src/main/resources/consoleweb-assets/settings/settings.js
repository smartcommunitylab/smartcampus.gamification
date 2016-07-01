angular.module('gamificationEngine.settings', [])
	.controller('SettingsCtrl', function ($scope, $rootScope, $stateParams, gamesFactory) {
		$rootScope.currentNav = 'settings';
		$rootScope.currentGameId = $stateParams.id;

		var game = $scope.game;
		$scope.newGame = {};
		//$scope.newGame = angular.copy($scope.game);

		$scope.newGame.name = game.name;

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
			var valid = true;
			$scope.alerts.editGameError = false;
			$scope.alerts.nameError = '';
			$scope.alerts.invalidTime = false;


			if (document.getElementsByClassName('has-error').length > 0) {
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

				// Edit game
				gamesFactory.editGame(game, fields).then(
					function () {
						// Settings edited
						$scope.game.name = $scope.newGame.name;
						$scope.game.expiration = $scope.newGame.expiration;
						$scope.alerts.settingsEdited = true;
						$scope.disabled = false;
					},
					function (message) {
						// Show given error alert
						$scope.alerts.editGameError = 'messages:' + message;
						$scope.disabled = false;
					}
				);
			}
			/*if (document.getElementsByClassName('has-error').length == 0) {
				var fields = {};
				fields.name = $scope.newGame.name;
				fields.expiration = $scope.newGame.expiration && !$scope.newGame.neverending ? $scope.newGame.expiration.getTime() : undefined;

				// Edit game
				gamesFactory.editGame(game, fields).then(
					function () {
						// Settings edited
						$scope.game.name = $scope.newGame.name;
						$scope.game.expiration = $scope.newGame.expiration;
						$scope.alerts.settingsEdited = true;
						$scope.disabled = false;
					},
					function (message) {
						// Show given error alert
						$scope.alerts.editGameError = 'messages:' + message;
						$scope.disabled = false;
					}
				);
			} else {
				$scope.alerts.editGameError = 'messages:msg_invalid_time';
				$scope.disabled = false;
			}*/
		};

		// CANCEL button click event-handler
		$scope.cancel = function () {
			$scope.goto('concepts');
		};


		//Add action
		/*$scope.openAddActionModal = function () {
			var modalInstance = $uibModal.open({
				templateUrl: 'actions/modal_action_edit.html',
				controller: 'EditActionModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					action: function () {
						return $scope.actionName;
					}
				}
			});
		};

		$scope.deleteAction = function (action) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteActionConfirmModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					argument: function () {
						return action;
					}
				}
			});
		};

		// Load game
		gamesFactory.getGameById($stateParams.id).then(function (game) {
			$scope.game = game;
		}, function () {
			// Show error alert
			$scope.alerts.loadGameError = true;
		});*/
	});

// Edit action instance modal
/*modals.controller('EditActionModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, action) {
		$scope.input = {};

		$scope.alerts = {
			'editGameError': ''
		};

		$scope.ok = function () {
			$scope.disabled = true;
			if ($scope.input.actionName && $scope.input.actionName.length > 0) {
				var found = false;
				for (var i = 0; i < game.actions.length && !found; i++) {
					if ($scope.input.actionName == game.actions[i]) {
						found = true;
					}
				}

				if (!found) {
					game.actions.push($scope.input.actionName);
					gamesFactory.saveGame(game).then(
						function () {
							$uibModalInstance.close();
						},
						function (message) {
							game.actions.pop();
							$scope.alerts.editGameError = 'messages:' + message;
							$scope.disabled = false;
						});
				} else {
					$scope.alerts.editGameError = 'messages:msg_same_name_error';
					$scope.disabled = false;
				}
			} else {
				$scope.alerts.editGameError = 'messages:msg_empty_fields';
				$scope.disabled = false;
			}
		};

		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	})
	// Delete action modal
	.controller('DeleteActionConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, argument, game, gamesFactory) {
		$scope.argument = argument;

		$scope.alerts = {
			'deleteError': false
		};

		// DELETE button click event-handler
		$scope.delete = function () {
			var idx = game.actions.indexOf(argument);
			if (idx !== -1) {
				game.actions.splice(idx, 1);
			}

			gamesFactory.saveGame(game).then(
				function () {
					$uibModalInstance.close();
				},
				function (message) {
					if (idx !== -1) {
						game.actions.splice(idx, 0, argument);
						$scope.alerts.deleteError = true;
					}
				}
			);
		};

		// CANCEL button click event-handler
		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	});
*/
