/*
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

// Edit game modal
angular.module('gamificationEngine.modals', [])
	.controller('EditGameModalInstanceCtrl', function ($scope, $uibModalInstance, game, gamesFactory) {
		$scope.newGame = {};
		$scope.newGame.name = game.name;

		if (game.expiration) {
			$scope.newGame.expiration = new Date(game.expiration);
		} else {
			$scope.newGame.neverending = true;
			$scope.newGame.expiration = new Date();
		}
		// Error alerts object
		$scope.alerts = {
			'editGameError': ''
		};

		$scope.closeAlert = function (alertName) {
			$scope.alerts[alertName] = '';
		}

		// OK button click event-handler
		$scope.ok = function () {

			var fields = {};
			fields.name = $scope.newGame.name;
			fields.expiration = $scope.newGame.expiration && !$scope.newGame.neverending ? $scope.newGame.expiration.getTime() : undefined;

			// Edit game
			gamesFactory.editGame(game, fields).then(
				function () {
					// Settings edited
					$uibModalInstance.close();
				},
				function (message) {
					// Show given error alert
					$scope.alerts.editGameError = message;
				}
			);
		};

		// CANCEL button click event-handler
		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	})

// Delete game modal
.controller('DeleteGameConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, $window, game, gamesFactory) {
	$scope.argument = game.name;
	$scope.isGame = true;

	$scope.alerts = {
		'deleteError': '',
	}

	$scope.closeAlert = function (alertName) {
		$scope.alerts[alertName] = '';
	}

	// DELETE button click event-handler
	$scope.delete = function () {
		// Delete game
		gamesFactory.deleteGame(game).then(function () {
			// Game has been deleted
			// Redirect to homepage
			$window.location.href = '#/home';
			$uibModalInstance.close();
		}, function (data) {
			$scope.alerts.deleteError = data;
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	}
})

// Delete instance modal
.controller('DeleteInstanceConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, $window, game, instance, instanceType, gamesFactory) {
	$scope.argument = instance.name;

	// DELETE button click event-handler
	$scope.delete = function () {
		gamesFactory.deleteInstance(game, instance, instanceType).then(function () {
			// Instance has been deleted
			// Redirect to homepage
			$window.location.href = '#/game/' + game.id;
			$uibModalInstance.close();
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})

// Edit points instance modal
.controller('EditPointsInstanceModalInstanceCtrl', function ($scope, $uibModalInstance, game, instance, gamesFactory) {
	$scope.points = {};
	$scope.points.name = instance.name;
	// $scope.points.typology = instance.typology || 'Skill points';

	// Error alerts object
	$scope.alerts = {
		'editInstanceError': ''
	};

	$scope.closeAlert = function (alertName) {
		$scope.alerts[alertName] = '';
	}

	// Functions to manage dropdown button

	$scope.dropdown = {
		isOpen: false
	};

	$scope.toggleDropdown = function ($event) {
		$event.preventDefault();
		$event.stopPropagation();
		$scope.dropdown.isOpen = !$scope.dropdown.isOpen;

	};

	$scope.setTypology = function (type, $event) {
		$scope.points.typology = type;
		$scope.toggleDropdown($event);
	};

	// SAVE button click event-handler
	$scope.save = function () {
		gamesFactory.editInstance(game, instance, 'points', $scope.points).then(function () {
			// Points instance edited
			$uibModalInstance.close();
		}, function (message) {
			// Show error alert
			$scope.alerts.editInstanceError = message;
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})

// Edit badges collection instance modal
.controller('EditBadgesCollectionInstanceModalInstanceCtrl', function ($scope, $uibModalInstance, game, instance, gamesFactory) {
	$scope.badges_collection = {};
	$scope.badges_collection.name = instance.name;

	// Error alerts object
	$scope.alerts = {
		'editInstanceError': ''
	};

	$scope.closeAlert = function (alertName) {
		$scope.alerts[alertName] = '';
	};

	// SAVE button click event-handler
	$scope.save = function () {
		gamesFactory.editInstance(game, instance, 'badges_collections', $scope.badges_collection).then(function () {
			// Badges collection instance edited
			$uibModalInstance.close();
		}, function (message) {
			// Show error alert
			$scope.alerts.editInstanceError = message;
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})

.controller('EditActionModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, action) {
	$scope.input = {};
	$scope.ok = function () {

		if (!!$scope.input.actionName && $scope.input.actionName.length > 0) {
			game.actions.push($scope.input.actionName);
		}

		gamesFactory.saveGame(game).then(
			function () {
				// Settings edited
				$uibModalInstance.close();
			},
			function (message) {
				// Show given error alert
				// $scope.alerts.editGameError = message;
			}
		);
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})

.controller('DeleteConceptConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, instance, game, type, gamesFactory) {
	$scope.argument = instance.name;

	// DELETE button click event-handler
	$scope.delete = function () {
		var idx = 0;
		var a = [];
		if (type === 'point') {
			a = game.pointConcept
		}
		if (type === 'badge') {
			a = game.badgeCollectionConcept;
		}
		a.forEach(function (c) {
			if (c.id === instance.id && c.name === instance.name) {
				a.splice(idx, 1);
			}
			idx++;
		});

		gamesFactory.saveGame(game).then(
			function () {

				$uibModalInstance.close();
			},
			function (message) {

			}
		);
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})

// Delete action modal
.controller('DeleteActionConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, argument, game, gamesFactory) {
	$scope.argument = argument;

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

			}
		);
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})

.controller('EditRuleModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, rule) {
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
})

.controller('EditTaskModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, task) {
	$scope.alerts = {
		'taskErr': '',
	};

	var t = {};
	$scope.input = {};

	// default value
	$scope.input.itemToNotificate = 3;

	$scope.game = game;

	if (task) {
		$scope.input.name = task.name;
		$scope.input.itemType = task.itemType;
		$scope.input.classificationName = task.classificationName;
		$scope.input.schedule = task.schedule ? task.schedule.cronExpression : task.cronExpression;
		$scope.input.itemToNotificate = task.itemsToNotificate;
		$scope.input.edit = true;
	}


	$scope.closeAlert = function (alertName) {
		$scope.alerts[alertName] = '';
	};

	$scope.ok = function () {
		var valid = $scope.input.name && $scope.input.itemType && $scope.input.classificationName && $scope.input.schedule && $scope.input.itemToNotificate;
		if (valid) {
			t.name = $scope.input.name;
			t.itemType = $scope.input.itemType;
			t.classificationName = $scope.input.classificationName;
			t.cronExpression = $scope.input.schedule;
			t.itemsToNotificate = $scope.input.itemToNotificate;

			if (!task) {
				gamesFactory.addTask(game, t).then(function (data) {
					if (!game.classificationTask) {
						game.classificationTask = [];
					}
					game.classificationTask.push(data);
					$uibModalInstance.close();
				}, function (msg) {
					$scope.alerts.taskErr = msg;
				});
			} else {
				gamesFactory.editTask(game, t).then(function () {
					var idx = -1;
					for (var i = 0; i < game.classificationTask.length; i++) {
						if (game.classificationTask[i].name === t.name) {
							idx = i;
							break;
						}
					}
					if (idx > -1) {
						game.classificationTask.splice(idx, 1, t);
					}
					$uibModalInstance.close();
				}, function (msg) {
					$scope.alerts.taskErr = msg;
				});

			}


		}

	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})

.controller('DeleteTaskModalInstanceCtrl', function ($scope, $uibModalInstance, task, game, gamesFactory) {
	$scope.argument = task.name;

	$scope.alerts = {
		'deleteError': '',
	};
	// DELETE button click event-handler
	$scope.delete = function () {
		if (game.classificationTask) {
			var idx = -1;
			for (var i = 0; i < game.classificationTask.length; i++) {
				if (game.classificationTask[i].name === task.name) {
					idx = i;
					break;
				}
			}
		}


		gamesFactory.deleteTask(game, task).then(
			function () {
				$uibModalInstance.close();
				if (idx > -1) {
					game.classificationTask.splice(idx, 1);
				}
			},
			function (message) {
				$scope.alerts.deleteError = message;
			}
		);
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});
