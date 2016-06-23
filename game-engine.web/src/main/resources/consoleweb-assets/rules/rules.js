angular.module('gamificationEngine.rules', [])
	.controller('RulesCtrl', function ($scope, $rootScope, $stateParams, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'rules';
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
		$scope.openAddRuleModal = function () {
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_rule_edit.html',
				controller: 'EditRuleModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					rule: function () {}
				}
			});
		};


		//Add action
		$scope.editRule = function (rule) {
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_rule_edit.html',
				controller: 'EditRuleModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					rule: function () {
						return rule;
					}
				}
			});
		};

		$scope.deleteRule = function (rule) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteRuleModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					rule: function () {
						return rule;
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
