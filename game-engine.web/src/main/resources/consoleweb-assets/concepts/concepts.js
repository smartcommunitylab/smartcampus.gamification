angular.module('gamificationEngine.concepts', [])
	.controller('ConceptsCtrl', function ($scope, $rootScope, gamesFactory) {
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
					$scope.game.pointConcept.push(instance);
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
					$scope.game.badgeCollectionConcept.push(instance);
					$scope.badges_collection.name = '';
					$scope.disabled = false;
				}, function (message) {
					// Show error alert
					$scope.alerts.genericError = 'messages:' + message;
					$scope.disabled = false;
				});
			}
		};

		gamesFactory.getPoints($rootScope.currentGameId).then(function (points) {
			$scope.points = points;
		});

		gamesFactory.getBadges($rootScope.currentGameId).then(function (badges) {
			$scope.badges = badges;
		});
	});
