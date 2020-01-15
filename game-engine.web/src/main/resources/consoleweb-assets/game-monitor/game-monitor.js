angular.module('gamificationEngine.monitor', [])
	.controller('MonitorCtrl', function ($scope, $rootScope, $stateParams, $window, $uibModal, gamesFactory, $state) {
		$scope.orderByAttribute = 'start';
		$rootScope.currentGameId = $stateParams.id;
		$scope.currentPage = 1;
		$scope.items4Page = 10;
		$scope.challengeNameLimit = 30;

		if ($state.current.data) {
			$rootScope.page = $state.current.data.page;
		}

		if ($rootScope.monitorFilter) {
			$scope.playerIdFilter = $rootScope.monitorFilter;
		}


		// Load games
		gamesFactory.getGameById($stateParams.id).then(function (game) {
			$scope.game = game;
		}, function () {
			// Show error alert
			$scope.err = 'messages:msg_generic_error';
		});

		var enrichData = function (data) {
			data.forEach(function (p) {
				var badges = p.state['BadgeCollectionConcept'] ? p.state['BadgeCollectionConcept'] : [];
				var score = p.state['PointConcept'] ? p.state['PointConcept'] : [];
				var challenges = p.state['ChallengeConcept'] ? p.state['ChallengeConcept'] : [];
				p.totalBadges = 0;
				p.totalScore = 0;
				badges.forEach(function (b) {
					p.totalBadges += b.badgeEarned.length;
				});

				score.forEach(function (s) {
					s.periodKeys = Object.keys(s.periods)
					p.totalScore += s.score;
				});
				
				p.hasCustomData = Object.keys(p.customData).length > 0;
				p.hasLevels = Object.keys(p.levels).length > 0;
				p.hasChallenges = challenges.length > 0;
				p.hasInventories = Object.keys(p.inventory.challengeChoices).length > 0;
				var referenceTimestamp = new Date().getTime();
				const now = moment();
				challenges.forEach(function(c) {
					c.failed = c.state === 'FAILED';
					c.isGroup = c.name.startsWith('p_');
					c.isVisible = !c.visibility.hidden || moment(c.visibility.disclosureDate).isSameOrBefore(now);
					
				});
				
				/* patch for an explicit request of peppo
				if custom data key contains '_startTs' or '_endTs' format it as a date
				THIS MUST TO BE RETHINK FOR A GENERIC PURPOSE
				*/
			   
				Object.keys(p.customData).forEach(function (k) {
					if (k.indexOf('_startChTs') != -1 || k.indexOf('_endChTs') != -1) {
						p.customData[k] = moment(p.customData[k]).format('DD/MM/YYYY, HH:mm:ss');
					}
				});
			});

			return data;
		}

		
		$scope.hideDetails = true;
		$scope.hidePeriods = true;
		$scope.hideInstances = true;
		
		$scope.limitPeriodInstances = 3;


		gamesFactory.getPlayersState($rootScope.currentGameId, $scope.playerIdFilter, $scope.currentPage, $scope.items4Page).then(function (data) {
			data.content = enrichData(data.content);
			$scope.playerStates = data;
			$scope.totalItems = data.totalElements;
		}, function (msg) {
			$scope.err = 'messages:' + msg;
		});

		//$scope.expand = false;
		$scope.playerIdFilter = '';

		$scope.filter = function () {
			$rootScope.monitorFilter = $scope.playerIdFilter;
			gamesFactory.getPlayersState($rootScope.currentGameId, $scope.playerIdFilter, $scope.currentPage, $scope.items4Page).then(function (data) {
				data.content = enrichData(data.content);
				$scope.playerStates = data;
				$scope.totalItems = data.totalElements;
			}, function (msg) {
				$scope.err = 'messages:' + msg;
			});
		}

		$scope.update = function () {
			//$scope.expandIdx = -1000;
			gamesFactory.playersSearch($rootScope.currentGameId, $scope.playerIdFilter, $scope.currentPage, $scope.items4Page).then(function (data) {
				data.content = enrichData(data.content);
				$scope.playerStates = data;
				$scope.totalItems = data.totalElements;
			}, function (msg) {
				$scope.err = 'messages:' + msg;
			});
		};

		$scope.goToUrl = function (url) {
			$window.location.href = url;
		}
		
		$scope.deleteChallenge = function(state, challenge) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteChallengeConfirmModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					gameId: function () {
						return $rootScope.currentGameId;
					},
					challenge: function () {
						return challenge;
					},
					state: function () {
						return state;
					}
				}
			});
			
			modalInstance.result.then(function () {
			});
		};
		
		$scope.editChallenge = function(state, challenge) {
			
			var modalInstance = $uibModal.open({
				templateUrl: 'game-monitor/edit-challenge.html',
				controller: 'EditChallengeModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					gameId: function () {
						return $rootScope.currentGameId;
					},
					challenge: function () {
						return challenge;
					},
					state: function () {
						return state;
					}
				}
			});
			
			modalInstance.result.then(function () {
			});
		};
	});


modals
.controller('DeleteChallengeConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, gameId, challenge, state) {

$scope.argument = challenge.name;
$scope.monitorPage = true;

$scope.alerts = {
	'deleteError': false
};

// DELETE button click event-handler
$scope.delete = function () {
	gamesFactory.deleteChallenge(gameId, state.playerId, challenge).then(function (data) {
		const challengeIndex = state.state['ChallengeConcept'].indexOf(challenge);
		state.state['ChallengeConcept'].splice(challengeIndex, 1);
		$uibModalInstance.close();
	}, function (msg) {
		$scope.err = 'messages:' + msg;
	});	
};

// CANCEL button click event-handler
$scope.cancel = function () {
	$uibModalInstance.dismiss('cancel');
};

});
modals
.controller('EditChallengeModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, gameId, challenge, state) {

	$scope.visibilityStates = ['Hidden','Visible'];
	$scope.challenge = challenge;

	$scope.disclosureDate = challenge.visibility.disclosureDate;
	$scope.visibility = challenge.visibility.hidden ? $scope.visibilityStates[0] : $scope.visibilityStates[1];

	$scope.alerts = {	
		'deleteError': false
	};


	$scope.save = function () {
	
//	gamesFactory.deleteChallenge(gameId, state.playerId, challenge).then(function (data) {
//		const challengeIndex = state.state['ChallengeConcept'].indexOf(challenge);
//		state.state['ChallengeConcept'].splice(challengeIndex, 1);
//		$uibModalInstance.close();
//	}, function (msg) {
//		$scope.err = 'messages:' + msg;
//	});	
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};

});