angular.module('gamificationEngine.actions', [])
	.controller('ActionsCtrl', function ($scope, $rootScope, $stateParams, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'actions';
		$rootScope.currentGameId = $stateParams.id;

		// Error alerts object
		$scope.alerts = {
			'success': '',
			'error': ''
		};

		$scope.closeAlert = function (alertName) {
			$scope.alerts[alertName] = '';
		}

		//Add action
		$scope.openAddActionModal = function () {
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
		});

		$scope.closeAlert = function (alertName) {
			$scope.alerts[alertName] = false;
		}

		$scope.choose = function () {
			$scope.path = "OK";
		};

		$scope.uploadImport = function () {
			$scope.dataImported = {};
		};

		$scope.clear = function () {
			$scope.dataImported = undefined;
		};

		$scope.confirm = function () {};
	});

// Edit action instance modal
modals.controller('EditActionModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, action) {
		$scope.input = {};

		$scope.alerts = {
			'editGameError': ''
		};

		$scope.ok = function () {
			$scope.disabled = true;
			console.log(game.actions);
			if ($scope.input.actionName && $scope.input.actionName.length > 0) {
				var found = false;
				for	(var i = 0; i < game.actions.length && !found; i++) {
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
				}
				else {
					$scope.alerts.editGameError = 'messages:msg_same_name_error';
					$scope.disabled = false;
				}
			}
			else {
				$scope.alerts.editGameError = 'messages:msg_empty_fields';
				$scope.disabled = false;
			}
			/*if (!!$scope.input.actionName && $scope.input.actionName.length > 0) {
				game.actions.push($scope.input.actionName);
			}

			gamesFactory.saveGame(game).then(
				function () {
					// Settings edited
					$uibModalInstance.close();
				},
				function (message) {
					game.actions.pop();
					// Show given error alert
					$scope.alerts.editGameError = 'messages:' + message;
				}
			);*/
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
