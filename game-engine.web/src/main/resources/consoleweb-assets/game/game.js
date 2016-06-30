angular.module('gamificationEngine.game', [])
	.controller('GameCtrl', function ($scope, $rootScope, $window, $stateParams, $uibModal, gamesFactory, utilsFactory, $state) {
		$rootScope.currentNav = 'configure';
		$rootScope.currentGameId = $stateParams.id;

		if ($state.current.data) {
			$rootScope.page = $state.current.data.page;
		}

		// Error alerts object
		$scope.alerts = {
			'cantCreateLeaderboards': false,
			'loadGameError': false,
			'settingsEdited': false
		};

		// Tab switching
		$scope.active = {
			'points': false,
			'badges_collections': false,
		};

		// Read the tab param to select the right tab. If it isn't given, choose the dafualt tab
		var tab = $stateParams.tab;

		if (!!tab && (tab == 'points' || tab == 'badges_collections')) {
			// User choice (tab)
			$scope.active[tab] = true;
			$scope.selectedInstance = tab;
		} else {
			// Default choice = 'points'
			$scope.selectedInstance = 'points';
		}

		$scope.game = {};

		// Load games
		gamesFactory.getGameById($stateParams.id).then(function (game) {
			$scope.game = game;
		}, function () {
			// Show error alert
			$scope.alerts.loadGameError = true;
		});

		// default
		$scope.viewName = 'concepts';
		$scope.menuItem = 'concepts';

		gamesFactory.getPoints($rootScope.currentGameId).then(function (points) {
			$scope.points = points;
		});

		gamesFactory.getBadges($rootScope.currentGameId).then(function (badges) {
			$scope.badges = badges;
		});


		$scope.closeAlert = function (alertName) {
			$scope.alerts[alertName] = '';
		};

		$scope.goto = function (path) {
			$scope.viewName = path;
			$scope.menuItem = path;
		};

		$scope.goToTab = function (tab) {
			$window.location.href = '#/game/' + $scope.game.id + '?tab=' + tab;
		};

		$scope.points = {};
		var instance = {};
		$scope.points.name = instance.name;

		// Error alerts object
		$scope.alerts = {
			'editInstanceError': ''
		};

		// SAVE button click event-handler
		$scope.addPoint = function () {
			var game = $scope.game;
			$scope.disabled = true;
			gamesFactory.editInstance(game, instance, 'points', $scope.points).then(function () {
				// Points instance edited
				$scope.disabled = false;
				//$uibModalInstance.close();
			}, function (message) {
				// Show error alert
				$scope.alerts.editInstanceError = 'messages:' + message;
				$scope.disabled = false;
			});
		};

		$scope.badges_collection = {};
		$scope.instance = {};
		$scope.badges_collection.name = instance.name;

		// Error alerts object
		$scope.alerts = {
			'editInstanceError': ''
		};

		// SAVE button click event-handler
		$scope.addBadge = function () {
			var game = $scope.game;
			$scope.disabled = true;
			gamesFactory.editInstance(game, instance, 'badges_collections', $scope.badges_collection).then(function () {
				// Badges collection instance edited
				//$uibModalInstance.close();
				$scope.disabled = false;
			}, function (message) {
				// Show error alert
				$scope.alerts.editInstanceError = 'messages:' + message;
				$scope.disabled = false;
			});
		};

		/*$scope.openEditModal = function (id) {
			// Edit a game
			var modalInstance = $uibModal.open({
			templateUrl: 'modals/modal_game_edit.html',
				controller: 'EditGameModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					}
				}
			});

			modalInstance.result.then(function () {
				// Show 'settings successfully edited' alert
				$scope.alerts.settingsEdited = true;
			});
		};*/

		/*$scope.openAddInstanceModal = function () {
			// Add new plugin instance
			switch ($scope.selectedInstance) {
				case 'points':
					$scope.openAddPointsInstanceModal();
					break;
				case 'badges_collections':
					$scope.openAddBadgesCollectionsInstanceModal();
					break;
			}
		};

		$scope.openAddPointsInstanceModal = function () {
			// Add a new points instance
			var modalInstance = $uibModal.open({
				templateUrl: 'game/modal_points_instance_edit.html',
				controller: 'EditPointsInstanceModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					instance: function () {
						return {};
					}
				}
			});
		};

		$scope.openAddBadgesCollectionsInstanceModal = function () {
			// Add a new badges collection instance
			var modalInstance = $uibModal.open({
				templateUrl: 'game/modal_badges_collection_instance_edit.html',
				controller: 'EditBadgesCollectionInstanceModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					instance: function () {
						return {};
					}
				}
			});
		};*/

		$scope.deleteConcept = function (instance, type) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteConceptConfirmModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					instance: function () {
						return instance;
					},
					type: function () {
						return type;
					}
				}
			});
		};
	});

// Edit points instance modal
modals
/*.controller('EditPointsInstanceModalInstanceCtrl', function ($scope, $uibModalInstance, game, instance, gamesFactory) {
	$scope.points = {};
	$scope.points.name = instance.name;

	// Error alerts object
	$scope.alerts = {
		'editInstanceError': ''
	};

	// SAVE button click event-handler
	$scope.save = function () {
		$scope.disabled = true;
		gamesFactory.editInstance(game, instance, 'points', $scope.points).then(function () {
			// Points instance edited
			$uibModalInstance.close();
		}, function (message) {
			// Show error alert
			$scope.alerts.editInstanceError = 'messages:' + message;
			$scope.disabled = false;
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})*/

// Edit badges collection instance modal
/*.controller('EditBadgesCollectionInstanceModalInstanceCtrl', function ($scope, $uibModalInstance, game, instance, gamesFactory) {
	$scope.badges_collection = {};
	$scope.badges_collection.name = instance.name;

	// Error alerts object
	$scope.alerts = {
		'editInstanceError': ''
	};

	// SAVE button click event-handler
	$scope.save = function () {
		$scope.disabled = true;
		gamesFactory.editInstance(game, instance, 'badges_collections', $scope.badges_collection).then(function () {
			// Badges collection instance edited
			$uibModalInstance.close();
		}, function (message) {
			// Show error alert
			$scope.alerts.editInstanceError = 'messages:' + message;
			$scope.disabled = false;
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
})*/

// Delete concept modal
	.controller('DeleteConceptConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, instance, game, type, gamesFactory) {
	$scope.argument = instance.name;

	$scope.alerts = {
		'deleteError': false,
	}

	// DELETE button click event-handler
	$scope.delete = function () {
		var idx = 0;
		var a = [];

		var tmpGame = angular.copy(game);
		if (type === 'point') {
			a = tmpGame.pointConcept;
		}
		if (type === 'badge') {
			a = tmpGame.badgeCollectionConcept;
		}
		a.forEach(function (c) {
			if (c.id === instance.id && c.name === instance.name) {
				a.splice(idx, 1);
			}
			idx++;
		});

		gamesFactory.saveGame(tmpGame).then(
			function () {
				if (type === 'point') {
					game.pointConcept = a;
				}
				if (type === 'badge') {
					game.badgeCollectionConcept = a;
				}
				$uibModalInstance.close();
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
