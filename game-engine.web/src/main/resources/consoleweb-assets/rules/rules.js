angular.module('gamificationEngine.rules', [])
	.controller('RulesCtrl', function ($scope, $rootScope, $stateParams, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'rules';
		$rootScope.currentGameId = $stateParams.id;

		$scope.isCollapsed = true;
		$scope.title = "labels:title_add_rule";
		$scope.action = "labels:btn_publish";

		$scope.input = {};
		$scope.alerts = {
			'ruleError': ''
		};
		var previousName = '';

		var rule;
		var game = $scope.game;
		/*if (rule) {
			gamesFactory.getRule(game, rule.id).then(
				function (data) {
					if (data) {
						$scope.input.name = data.name;
						$scope.input.ruleContent = data.content;

						previousName = data.name;
						//previousContent = data.content;
					}
				},
				function (message) {
					// Show given error alert
					$scope.alerts.ruleError = 'messages:' + message;
				});
		}*/

		$scope.save = function () {
			$scope.disabled = true;
			$scope.alerts.ruleError = '';

			if (!!$scope.input.ruleContent && $scope.input.ruleContent.length > 0 && $scope.input.name && $scope.input.name.length > 0) {

				//check if already exist
				var found = false;
				if (game.rules && $scope.input.name && (!rule || rule && rule.name !== $scope.input.name)) {
					for (var i = 0; i < game.rules.length; i++) {
						found = game.rules[i].name == $scope.input.name;
						if (found) break;
					}
				}

				if (found) {
					$scope.alerts.ruleError = 'messages:msg_error_exist';
					$scope.disabled = false;
					return;
				}

				var r = {};

				if (rule) {
					r = rule;
				}
				r.name = $scope.input.name;
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
							$scope.disabled = false;
							$scope.isCollapsed = true;
						},
						function (message) {
							r.name = previousName;
							// Show given error alert
							$scope.alerts.ruleError = 'messages:' + message;
							$scope.disabled = false;
						});
				} else {
					gamesFactory.validateRule($scope.input.ruleContent).then(function (data) {
						if (data.length > 0) {
							$scope.alerts.ruleValidation = data;
							$scope.disabled = false;
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
								$scope.disabled = false;
								$scope.isCollapsed = true;
							},
							function (message) {
								r.name = previousName;
								// Show given error alert
								$scope.alerts.ruleError = 'messages:' + message;
								$scope.disabled = false;
							});
					}, function (msg) {
						r.name = previousName;
						$scope.alerts.ruleError = 'messages:' + msg;
						$scope.disabled = false;
					});
				}


			} else {
				$scope.alerts.ruleError = 'messages:msg_empty_fields';
				$scope.disabled = false;
			}
		};

		$scope.cancel = function () {
			$scope.isCollapsed = true;
		}

		$scope.addRule = function () {
			$scope.isCollapsed = false;
			$scope.title = "labels:title_add_rule";
			$scope.action = "labels:btn:publish";
		}

		$scope.editRule = function (editingRule) {
			$scope.isCollapsed = false;
			rule = editingRule;
			$scope.title = "labels:title_edit_rule";
			$scope.action = "labels:btn_save";

			if (rule) {
				gamesFactory.getRule(game, rule.id).then(
					function (data) {
						if (data) {
							$scope.input.name = data.name;
							$scope.input.ruleContent = data.content;

							previousName = data.name;
							//previousContent = data.content;
						}
					},
					function (message) {
						// Show given error alert
						$scope.alerts.ruleError = 'messages:' + message;
					});
			}
		}
			//Add action
			/*$scope.openAddRuleModal = function () {
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
			};*/

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
	});

// Edit rule instance modal
modals.controller('EditRuleModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, rule) {
		$scope.input = {};
		$scope.alerts = {
			'ruleError': ''
		};
		var previousName = '';

		if (rule) {
			gamesFactory.getRule(game, rule.id).then(
				function (data) {
					if (data) {
						$scope.input.name = data.name;
						$scope.input.ruleContent = data.content;

						previousName = data.name;
						//previousContent = data.content;
					}
				},
				function (message) {
					// Show given error alert
					$scope.alerts.ruleError = 'messages:' + message;
				});
		}

		$scope.save = function () {
			$scope.disabled = true;
			$scope.alerts.ruleError = '';

			if (!!$scope.input.ruleContent && $scope.input.ruleContent.length > 0 && $scope.input.name && $scope.input.name.length > 0) {

				//check if already exist
				var found = false;
				if (game.rules && $scope.input.name && (!rule || rule && rule.name !== $scope.input.name)) {
					for (var i = 0; i < game.rules.length; i++) {
						found = game.rules[i].name == $scope.input.name;
						if (found) break;
					}
				}

				if (found) {
					$scope.alerts.ruleError = 'messages:msg_error_exist';
					$scope.disabled = false;
					return;
				}

				var r = {};

				if (rule) {
					r = rule;
				}
				r.name = $scope.input.name;
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
							r.name = previousName;
							// Show given error alert
							$scope.alerts.ruleError = 'messages:' + message;
							$scope.disabled = false;
						});
				} else {
					gamesFactory.validateRule($scope.input.ruleContent).then(function (data) {
						if (data.length > 0) {
							$scope.alerts.ruleValidation = data;
							$scope.disabled = false;
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
								r.name = previousName;
								// Show given error alert
								$scope.alerts.ruleError = 'messages:' + message;
								$scope.disabled = false;
							});
					}, function (msg) {
						r.name = previousName;
						$scope.alerts.ruleError = 'messages:' + msg;
						$scope.disabled = false;
					});
				}


			} else {
				$scope.alerts.ruleError = 'messages:msg_empty_fields';
				$scope.disabled = false;
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
