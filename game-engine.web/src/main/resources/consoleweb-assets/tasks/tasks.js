angular.module('gamificationEngine.tasks', [])
	.controller('TasksCtrl', function ($scope, $rootScope, $stateParams, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'tasks';
		$rootScope.currentGameId = $stateParams.id;

		// Error alerts object
		$scope.alerts = {
			'success': '',
			'error': ''
		};

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
				templateUrl: 'modals/modal_task_edit.html',
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
				templateUrl: 'modals/modal_task_edit.html',
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
			//$scope.alerts.loadGameError = true;
		});
	});
