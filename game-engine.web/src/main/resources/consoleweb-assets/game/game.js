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

		// Navbar names array
		$scope.pluginNames = {
			'points': 'points',
			'badges_collections': 'badges collections',
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
			$scope.alerts[alertName] = false;
		};

		$scope.goto = function (path) {
			$scope.viewName = path;
			$scope.menuItem = path;
		};

		$scope.goToTab = function (tab) {
			$window.location.href = '#/game/' + $scope.game.id + '?tab=' + tab;
		};

		$scope.countActive = function (game, type) {
			return utilsFactory.countActive(game, type);
		};

		$scope.getLength = function (game, type) {
			return utilsFactory.getLength(game, type);
		};

		$scope.openEditModal = function (id) {
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
		};

		$scope.openAddInstanceModal = function () {
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
				templateUrl: 'modals/modal_points_instance_edit.html',
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
				templateUrl: 'modals/modal_badges_collection_instance_edit.html',
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
		};

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
