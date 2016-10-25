angular.module('gamificationEngine.tasks', [])
	.controller('TasksCtrl', function ($scope, $rootScope, $stateParams, $timeout, $window, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'tasks';
		$rootScope.currentGameId = $stateParams.id;

		$scope.isCollapsed = true;
		$scope.title = "labels:title_add_task";
		var convertTask = function (task) {
			// convert in taskDto
			if (task.schedule) {
				task.cronExpression = task.schedule.cronExpression;
				task.schedule = undefined;
			}
			return task;
		}

		$scope.alerts = {
			'taskErr': false,
			'nameError': false,
			'itemsError': false,
			'cronError': false,
			'pointsError': false,
			'classError': false,
			'rankEdited': false,
			'rankDeleted': false
		};

		$scope.timeunit= [{label: 'Day', value: 'DAY'}, {label: 'Hour', value: 'HOUR'}, {label: 'Minute', value: 'MINUTE'}];
		
		//var t = {};
		$scope.input = {};
		$scope.delay = {showField: false};
		
		// default value
		$scope.input.itemToNotificate = 3;
		var game = $scope.game;
		var task;
		//$scope.game = game;

		$scope.resetDelayField = function() {
			if(!$scope.delay.showField) {
				delete $scope.delay.value;
				delete $scope.delay.unit;
			}
		}
		
		$scope.saveIncremental = function() {
			$scope.alerts.nameError = false;
			$scope.alerts.pointsError = false;
			$scope.alerts.classError = false;
			$scope.alerts.itemsError = false;
			$scope.alerts.taskErr = false;
			var valid = true;
			if (!$scope.input.name) {
				$scope.alerts.nameError = true;
				valid = false;
			}
			if (!$scope.input.itemType) {
				$scope.alerts.pointsError = true;
				valid = false;
			}
			if (!$scope.input.classificationName) {
				$scope.alerts.classError = true;
				valid = false;
			}
			
			if (!$scope.input.itemToNotificate) {
				$scope.alerts.itemsError = true;
				valid = false;
			}

			if (valid) {
				$scope.disabled = true;
				var t = {};
				t.name = $scope.input.name;
				t.itemType = $scope.input.itemType;
				t.classificationName = $scope.input.classificationName;
				t.periodName = $scope.input.periodName;
				t.itemsToNotificate = $scope.input.itemToNotificate;
				t.type = $scope.input.type;
				if($scope.delay.showField) {
					t.delayValue = $scope.delay.value;
					t.delayUnit = $scope.delay.unit.value;
				}
				
				if (!task) {
					var found = false;
					for (var i = 0; i < game.classificationTask.length && !found; i++) {
						if (game.classificationTask[i].name === t.name) {
							found = true;
						}
					}
					if (!found) {
						gamesFactory.addIncrementalClassification(game, t).then(function (data) {
							if (!game.classificationTask) {
								game.classificationTask = [];
							}
							game.classificationTask.unshift(data);
							$scope.isCollapsed = true;
							$scope.disabled = false;
							$scope.alerts.rankEdited = true;
						}, function (msg) {
							$scope.alerts.taskErr = 'messages:' + msg;
							$scope.disabled = false;
						});
					} else {
						$scope.alerts.taskErr = "messages:msg_error_exist";
						$scope.disabled = false;
					}
				} else {
					gamesFactory.editIncrementalClassification(game, t).then(function () {
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
						$scope.isCollapsed = true;
						$scope.disabled = false;
						$scope.alerts.rankEdited = true;
					}, function (msg) {
						$scope.alerts.taskErr = 'messages:' + msg;
						$scope.disabled = false;
					});
				}
			}
		}
		
		
		$scope.save = function () {
			$scope.alerts.nameError = false;
			$scope.alerts.pointsError = false;
			$scope.alerts.classError = false;
			$scope.alerts.cronError = false;
			$scope.alerts.itemsError = false;
			$scope.alerts.taskErr = false;

			//var valid = $scope.input.name && $scope.input.itemType && $scope.input.classificationName && $scope.input.schedule && $scope.input.itemToNotificate;
			var valid = true;
			if (!$scope.input.name) {
				$scope.alerts.nameError = true;
				valid = false;
			}
			if (!$scope.input.itemType) {
				$scope.alerts.pointsError = true;
				valid = false;
			}
			if (!$scope.input.classificationName) {
				$scope.alerts.classError = true;
				valid = false;
			}
			if (!$scope.input.schedule) {
				$scope.alerts.cronError = true;
				valid = false;
			}
			if (!$scope.input.itemToNotificate) {
				$scope.alerts.itemsError = true;
				valid = false;
			}

			if (valid) {
				$scope.disabled = true;
				var t = {};
				t.name = $scope.input.name;
				t.itemType = $scope.input.itemType;
				t.classificationName = $scope.input.classificationName;
				t.cronExpression = $scope.input.schedule;
				t.itemsToNotificate = $scope.input.itemToNotificate;
				t.type = $scope.input.type;

				if (!task) {
					var found = false;
					for (var i = 0; i < game.classificationTask.length && !found; i++) {
						if (game.classificationTask[i].name === t.name) {
							found = true;
						}
					}
					if (!found) {
						gamesFactory.addTask(game, t).then(function (data) {
							if (!game.classificationTask) {
								game.classificationTask = [];
							}
							//game.classificationTask.push(data);
							game.classificationTask.unshift(data);
							//$uibModalInstance.close();
							$scope.isCollapsed = true;
							$scope.disabled = false;
							$scope.alerts.rankEdited = true;
						}, function (msg) {
							$scope.alerts.taskErr = 'messages:' + msg;
							$scope.disabled = false;
						});
					} else {
						$scope.alerts.taskErr = "messages:msg_error_exist";
						$scope.disabled = false;
					}
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
						//$uibModalInstance.close();
						$scope.isCollapsed = true;
						$scope.disabled = false;
						$scope.alerts.rankEdited = true;
					}, function (msg) {
						$scope.alerts.taskErr = 'messages:' + msg;
						$scope.disabled = false;
					});
				}
			}
			/*else {
				$scope.alerts.taskErr = 'messages:msg_empty_fields';
				$scope.disabled = false;
			}*/

		};

		$scope.cancel = function () {
			$scope.alerts.nameError = false;
			$scope.alerts.pointsError = false;
			$scope.alerts.classError = false;
			$scope.alerts.cronError = false;
			$scope.alerts.itemsError = false;
			$scope.alerts.taskErr = false;
			$scope.isCollapsed = true;
		};

		$scope.addTask = function () {
			$scope.title = "labels:title_add_task";
			$scope.input = {};
			$scope.edit = false;
			$scope.isCollapsed = false;
			$scope.alerts.rankEdited = false;
			$scope.delay = {showField: false};
		};

		$scope.editTask = function (editingTask) {
			$scope.title = "labels:title_edit_task";
			task = convertTask(editingTask);

			$scope.input.name = task.name;
			$scope.input.itemType = task.itemType;
			$scope.input.classificationName = task.classificationName;
			$scope.input.itemToNotificate = task.itemsToNotificate;
			$scope.input.type = task.type;
			if(task.type === 'general') {
				$scope.input.schedule = task.schedule ? task.schedule.cronExpression : task.cronExpression;
			} else {
				$scope.input.periodName = task.periodName;
			}
			if(task.delayUnit) {
				for(var i = 0; i < $scope.timeunit.length; i++) {
					if($scope.timeunit[i].value === task.delayUnit){
						$scope.delay.unit = $scope.timeunit[i];
						break;
					}
				}
				$scope.delay.value = task.delayValue;
			}
			$scope.delay.showField = task.delayUnit ? true : false;
			$scope.edit = true;
			$scope.isCollapsed = false;
			$scope.alerts.rankEdited = false;
			
			$window.scrollTo(0, 0);
		};
		/*$scope.openAddTaskModal = function () {
			var modalInstance = $uibModal.open({
				templateUrl: 'tasks/modal_task_edit.html',
				controller: 'EditTaskModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					task: function () {
						return;
					}
				}
			});
		};

		$scope.editTask = function (task) {
			var modalInstance = $uibModal.open({
				templateUrl: 'tasks/modal_task_edit.html',
				controller: 'EditTaskModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					task: function () {
						return convertTask(task);
					}
				}
			});
		};*/

		$scope.deleteTask = function (task) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteTaskModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					task: function () {
						return convertTask(task);
					}
				}
			});
			
			modalInstance.result.then(function () {
				$scope.alerts.rankDeleted = true;
				$scope.isCollapsed = true;
				$timeout(function () {
					$scope.alerts.rankDeleted = false;
				}, 4000);
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

// Edit task instance modal
modals
/*.controller('EditTaskModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, task) {
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

		$scope.ok = function () {
			$scope.disabled = true;
			$scope.alerts.taskErr = '';
			var valid = $scope.input.name && $scope.input.itemType && $scope.input.classificationName && $scope.input.schedule && $scope.input.itemToNotificate;
			if (valid) {
				t.name = $scope.input.name;
				t.itemType = $scope.input.itemType;
				t.classificationName = $scope.input.classificationName;
				t.cronExpression = $scope.input.schedule;
				t.itemsToNotificate = $scope.input.itemToNotificate;

				if (!task) {
					var found = false;
					for (var i = 0; i < game.classificationTask.length && !found; i++) {
						if (game.classificationTask[i].name === t.name) {
							found = true;
						}
					}
					if (!found) {
						gamesFactory.addTask(game, t).then(function (data) {
							if (!game.classificationTask) {
								game.classificationTask = [];
							}
							game.classificationTask.push(data);
							$uibModalInstance.close();
						}, function (msg) {
							$scope.alerts.taskErr = 'messages:' + msg;
							$scope.disabled = false;
						});
					} else {
						$scope.alerts.taskErr = "messages:msg_error_exist";
						$scope.disabled = false;
					}
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
						$scope.alerts.taskErr = 'messages:' + msg;
						$scope.disabled = false;
					});
				}
			} else {
				$scope.alerts.taskErr = 'messages:msg_empty_fields';
				$scope.disabled = false;
			}

		};

		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	})*/
// Delete task modal
	.controller('DeleteTaskModalInstanceCtrl', function ($scope, $uibModalInstance, task, game, gamesFactory) {
	$scope.argument = task.name;

	$scope.alerts = {
		'deleteError': false,
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
		if(task.type === 'general'){
		gamesFactory.deleteTask(game, task).then(
			function () {
				$uibModalInstance.close();
				if (idx > -1) {
					game.classificationTask.splice(idx, 1);
				}
			},
			function (message) {
				$scope.alerts.deleteError = true;
			}
		);
		}else {
			gamesFactory.deleteIncrementalClassification(game, task).then(
					function () {
						$uibModalInstance.close();
						if (idx > -1) {
							game.classificationTask.splice(idx, 1);
						}
					},
					function (message) {
						$scope.alerts.deleteError = true;
					}
				);
		}
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});
