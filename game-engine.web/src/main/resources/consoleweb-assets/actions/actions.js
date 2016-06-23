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
				templateUrl: 'modals/modal_action_edit.html',
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
