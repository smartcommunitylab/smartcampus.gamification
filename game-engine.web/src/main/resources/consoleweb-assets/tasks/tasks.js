angular.module('gamificationEngine.tasks', [])
	.controller('TasksCtrl', function ($scope, $rootScope, $stateParams, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'tasks';
		$rootScope.currentGameId = $stateParams.id;

		var convertTask = function (task) {
			// convert in taskDto
			if (task.schedule) {
				task.cronExpression = task.schedule.cronExpression;
				task.schedule = undefined;
			}
			return task;
		}

		$scope.openAddTaskModal = function () {
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
		};

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
modals.controller('EditTaskModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, task) {
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
	})
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
		};

		// CANCEL button click event-handler
		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	});
