angular.module('gamificationEngine.concepts', [])
	.controller('ConceptsCtrl', function ($scope, $rootScope, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'concepts';

		//var game = $scope.game;

		$scope.points = {};
		//var instance = {};
		//$scope.points.name = instance.name;

		$scope.badges_collection = {};
		//$scope.instance = {};
		//$scope.badges_collection.name = instance.name;

		// Error alerts object
		$scope.alerts = {
			'genericError': false,
			'editInstanceError': ''
		};

		// SAVE button click event-handler
		$scope.addPoint = function () {
			$scope.alerts.editInstanceError = '';
			$scope.alerts.genericError = false;

			if (!$scope.points.name) {
				$scope.alerts.editInstanceError = 'messages:msg_instance_name_error';
			} else if (gamesFactory.existsInstanceByName($scope.game, $scope.points.name, 'points')) {
				$scope.alerts.editInstanceError = 'messages:msg_instance_name_exists_error';
			} else {
				$scope.disabled = true;
				gamesFactory.editInstance($scope.game, 'points', $scope.points).then(function (instance) {
					// Points instance edited
					//$scope.game.pointConcept.push(instance);
					$scope.game.pointConcept.unshift(instance);
					$scope.points.name = '';
					$scope.disabled = false;
					//$uibModalInstance.close();
				}, function (message) {
					// Show error alert
					$scope.alerts.genericError = 'messages:' + message;
					$scope.disabled = false;
				});
			}
		};

		// SAVE button click event-handler
		$scope.addBadge = function () {
			$scope.alerts.editInstanceError = '';
			$scope.alerts.genericError = false;

			if (!$scope.badges_collection.name) {
				$scope.alerts.editInstanceError = 'messages:msg_instance_name_error';
			} else if (gamesFactory.existsInstanceByName($scope.game, $scope.badges_collection.name, 'badges_collections')) {
				$scope.alerts.editInstanceError = 'messages:msg_instance_name_exists_error';
			} else {
				$scope.disabled = true;
				gamesFactory.editInstance($scope.game, 'badges_collections', $scope.badges_collection).then(function (instance) {
					// Badges collection instance edited
					//$scope.game.badgeCollectionConcept.push(instance);
					$scope.game.badgeCollectionConcept.unshift(instance);
					$scope.badges_collection.name = '';
					$scope.disabled = false;
				}, function (message) {
					// Show error alert
					$scope.alerts.genericError = 'messages:' + message;
					$scope.disabled = false;
				});
			}
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

		gamesFactory.getPoints($rootScope.currentGameId).then(function (points) {
			$scope.points = points;
		});

		gamesFactory.getBadges($rootScope.currentGameId).then(function (badges) {
			$scope.badges = badges;
		});
	});

modals.controller('DeleteConceptConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, instance, game, type, gamesFactory) {
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
