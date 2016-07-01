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
			'editInstanceError': ''
		};

		// SAVE button click event-handler
		$scope.addPoint = function () {
			var game = $scope.game;
			$scope.disabled = true;
			gamesFactory.editInstance(game, 'points', $scope.points).then(function () {
				// Points instance edited
				$scope.disabled = false;
				$scope.points.name = '';
				$scope.alerts.editInstanceError = '';
				//$uibModalInstance.close();
			}, function (message) {
				// Show error alert
				$scope.alerts.editInstanceError = 'messages:' + message;
				$scope.disabled = false;
			});
		};

		// SAVE button click event-handler
		$scope.addBadge = function () {
			var game = $scope.game;
			$scope.disabled = true;
			gamesFactory.editInstance(game, 'badges_collections', $scope.badges_collection).then(function () {
				// Badges collection instance edited
				$scope.disabled = false;
				$scope.badges = '';
				$scope.alerts.editInstanceError = '';
			}, function (message) {
				// Show error alert
				$scope.alerts.editInstanceError = 'messages:' + message;
				$scope.disabled = false;
			});
		};

		gamesFactory.getPoints($rootScope.currentGameId).then(function (points) {
			$scope.points = points;
		});

		gamesFactory.getBadges($rootScope.currentGameId).then(function (badges) {
			$scope.badges = badges;
		});
	});
