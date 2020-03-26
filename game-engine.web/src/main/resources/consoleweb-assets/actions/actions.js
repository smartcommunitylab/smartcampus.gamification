angular.module('gamificationEngine.actions', [])
	.controller('ActionsCtrl', function ($scope, $rootScope, $stateParams, $timeout, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'actions';
		$rootScope.currentGameId = $stateParams.id;

		$scope.input = {};

		// Load game
		gamesFactory.getGameById($stateParams.id).then(function (game) {
			$scope.game = game;
		}, function () {
			// Show error alert
			$scope.alerts.loadGameError = true;
		});

		$scope.alerts = {
			'actionError': '',
			'genericError': '',
			'actionDeleted': false
		};

		$scope.addAction = function () {
			$scope.alerts.genericError = '';
			$scope.alerts.actionError = '';
			if ($scope.input.actionName && $scope.input.actionName.length > 0) {
				var found = false;
				for (var i = 0; i < $scope.game.actions.length && !found; i++) {
					if ($scope.input.actionName == $scope.game.actions[i]) {
						found = true;
					}
				}

				if (!found) {
					$scope.disabled = true;
					$scope.game.actions.unshift($scope.input.actionName);
					gamesFactory.saveGame($scope.game).then(
						function () {
							$scope.disabled = false;
							$scope.input.actionName = '';
						},
						function (message) {
							game.actions.shift();
							$scope.alerts.genericError = 'messages:' + message;
							$scope.disabled = false;
						});
				} else {
					$scope.alerts.actionError = 'messages:msg_same_name_error';
				}
			} else {
				$scope.alerts.actionError = 'messages:msg_empty_fields';
			}
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
			
			modalInstance.result.then(function () {
				$scope.alerts.actionDeleted = true;
				
				$timeout(function () {
					$scope.alerts.actionDeleted = false;
				}, 4000);
			})
		};
	});

// Edit action instance modal
modals
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
