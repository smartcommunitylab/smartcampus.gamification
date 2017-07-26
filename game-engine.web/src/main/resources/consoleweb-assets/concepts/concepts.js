var concepts = angular.module('gamificationEngine.concepts', [])
	.controller('ConceptsCtrl', function ($scope, $rootScope, $timeout, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'concepts';

		$scope.points = {};
		$scope.pointsView = [];

		$scope.badges_collection = {};

		// Error alerts object
		$scope.alerts = {
			'genericError': false,
			'editInstanceError': '',
			'pointDeleted': false,
			'badgeDeleted': false,
			'challengeDeleted': false
		};

		$scope.hide = true;
		$scope.tmpPeriods = [];
		
		$scope.addPeriod = function() {
			let initialDate = new Date();
			initialDate.setHours(0);
			initialDate.setMinutes(0);
			initialDate.setSeconds(0);
			initialDate.setMilliseconds(0);
			$scope.tmpPeriods.push({name:'', start: initialDate, period : 1});
		}
		
		$scope.deleteTmpPeriod =function(index) {
			$scope.tmpPeriods.splice(index,1);
		}
		// SAVE button click event-handler
		$scope.addPoint = function () {
			$scope.alerts.editInstanceError = '';
			$scope.alerts.genericError = false;

			if (!$scope.points.name) {
				$scope.alerts.editInstanceError = 'messages:msg_instance_name_error';
			} else if (gamesFactory.existsInstanceByName($scope.game, $scope.points.name, 'points')) {
				$scope.alerts.editInstanceError = 'messages:msg_instance_name_exists_error';
			} else {
				let validationError = false;
				if($scope.tmpPeriods.length > 0){
					$scope.tmpPeriods.forEach(function(xx) {
						if(!xx.name) {
							$scope.alerts.editInstanceError = 'messages:msg_period_name_error';
							validationError = true;
						}
						if(xx.period < 0) {
							$scope.alerts.editInstanceError = 'messages:msg_period_negative_error';
							validationError = true;
						}
					});
				}
				if(!validationError) {
					$scope.disabled = true;
					// added periods to point to save
					$scope.points.periods = $scope.tmpPeriods;
					gamesFactory.editInstance($scope.game, 'points', $scope.points).then(function (instance) {
						// Points instance edited
						$scope.pointsView.unshift(processPointPeriods(instance));
						$scope.game.pointConcept.unshift(instance);
						$scope.points.name = '';
						$scope.tmpPeriods = [];
						$scope.disabled = false;
					}, function (message) {
						// Show error alert
						$scope.alerts.genericError = 'messages:' + message;
						$scope.disabled = false;
					});
				}
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
					},
					pointsView : function() {
						return $scope.pointsView;
					}
				}
			});

			modalInstance.result.then(function () {
				if (type == 'point') {
					$scope.alerts.pointDeleted = true;

					$timeout(function () {
						$scope.alerts.pointDeleted = false;
					}, 4000);

				} else if (type == 'badge') {
					$scope.alerts.badgeDeleted = true;

					$timeout(function () {
						$scope.alerts.badgeDeleted = false;
					}, 4000);
				} else if (type == 'challenge') {
					$scope.alerts.challengeDeleted = true;

					$timeout(function () {
						$scope.alerts.challengeDeleted = false;
					}, 4000);
				}
			});
		};

		$scope.process = function(p) {
			p.identifier = 'ciccio';
		};
		gamesFactory.getPoints($rootScope.currentGameId).then(function (points) {
			$scope.game.pointConcept = points;
			// process periods data to view state
			var processedPoints = [];
			points.forEach(function(point) {
				processedPoints.push(processPointPeriods(point));
			});
			$scope.points = processedPoints;
			$scope.pointsView = processedPoints;
		});

		gamesFactory.getBadges($rootScope.currentGameId).then(function (badges) {
			$scope.badges = badges;
		});
	});

	function processPointPeriods(point) {
		var processedPoint = angular.copy(point);
		if(processedPoint.periods) {
			let periodKeys = Object.keys(processedPoint.periods);
			processedPoint.periodList = [];
			periodKeys.forEach(function(k) {
				processedPoint.periods[k].period = processedPoint.periods[k].period / (24 * 3600 * 1000);
				processedPoint.periodList.push(processedPoint.periods[k]);
			});
		}
		return processedPoint;
	}

modals.controller('DeleteConceptConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, instance, game, type, pointsView, gamesFactory) {
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
				if (type === 'point') {
					pointsView.splice(idx,1);
				}
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
