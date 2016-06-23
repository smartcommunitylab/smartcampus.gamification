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
				templateUrl: 'rules/modal_rule_edit.html',
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
				templateUrl: 'rules/modal_rule_edit.html',
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

// Edit rule instance modal
modals.controller('EditRuleModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, rule) {
		$scope.input = {};
		$scope.alerts = {};

		if (rule) {
			gamesFactory.getRule(game, rule.id).then(
				function (data) {
					if (data) {
						$scope.input.name = data.name;
						$scope.input.ruleContent = data.content;
					}
				},
				function (message) {
					// Show given error alert
					$scope.alerts.ruleError = message;
				});
		}


		$scope.save = function () {

			if (!!$scope.input.ruleContent && $scope.input.ruleContent.length > 0) {

				//check if already exist
				var found = false;
				if (game.rules && $scope.input.name && (!rule || rule && rule.name !== $scope.input.name)) {
					for (var i = 0; i < game.rules.length; i++) {
						found = game.rules[i].name == $scope.input.name;
						if (found) break;
					}
				}

				if (found) {
					$scope.alerts.ruleError = 'msg_error_exist';
					return;
				}

				var r = {};

				if (rule) {
					r = rule;
				}
				var id = 1;
				if (!$scope.input.name) {
					if (game.rules) {
						var a = [];
						game.rules.forEach(function (r) {
							if (r.name.indexOf('rule ') === 0) {
								a.push(r);
							}
						});

						a.sort(function (a, b) {
							return a.id > b.id;
						});

						var last = a.slice(-1)[0];
						if (last) {
							var name = last.name;
						}

						var idx = 0;
						if (name) {
							idx = name.substring(5);
						}
					}
					r.name = 'rule ' + (parseInt(idx) + 1);
				} else {
					r.name = $scope.input.name;
				}

				r.content = $scope.input.ruleContent;

				if ($scope.input.name === 'constants') {
					gamesFactory.addRule(game, r).then(
						function (data) {
							if (!game.rules) {
								game.rules = [];
							}
							if (!rule) {
								game.rules.push(data);
							}
							$uibModalInstance.close();
						},
						function (message) {
							// Show given error alert
							$scope.alerts.ruleError = message;
						});
				} else {
					gamesFactory.validateRule($scope.input.ruleContent).then(function (data) {
						if (data.length > 0) {
							$scope.alerts.ruleValidation = data;
							return;
						}
						gamesFactory.addRule(game, r).then(
							function (data) {
								if (!game.rules) {
									game.rules = [];
								}
								if (!rule) {
									game.rules.push(data);
								}
								$uibModalInstance.close();
							},
							function (message) {
								// Show given error alert
								$scope.alerts.ruleError = message;
							});
					}, function (msg) {
						$scope.alerts.ruleError = msg;
					});
				}


			}
		};

		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	})
	// Delete rule modal
	.controller('DeleteRuleModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, rule) {
		$scope.alerts = {
			'deleteError': '',
		};
		$scope.argument = rule.name;

		$scope.closeAlert = function (alertName) {
			$scope.alerts[alertName] = '';
		};

		$scope.delete = function () {
			gamesFactory.deleteRule(game, rule.id).then(
				function (data) {
					if (data) {
						var idx = 0;
						for (idx = 0; idx < game.rules.length; idx++) {
							if (game.rules[idx].id == rule.id) {
								break;
							}
						}
						game.rules.splice(idx, 1);
						$uibModalInstance.close();
					}
				},
				function (message) {
					$scope.alerts.deleteError = message;
				})
		};

		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	});
