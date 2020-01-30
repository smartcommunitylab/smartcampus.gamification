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
			const challenges = state.state['ChallengeConcept'];
			let challengeIndex = -1;
			for(let index = 0; index < challenges.length; index++) {
				if( challenges[index].name == challenge.name) {
					challengeIndex = index;
					break;
				}
			}
			
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
					challengeIndex: function() {
						return challengeIndex;
					},
					state: function () {
						return state;
					}
				}
			});
			
			modalInstance.result.then(function (state) {
//				const now = moment();
//				let challenges = state.state['ChallengeConcept']
//				challenges.forEach(function(c) {
//					c.failed = c.state === 'FAILED';
//					c.isGroup = c.name.startsWith('p_');
//					c.isVisible = !c.visibility.hidden || moment(c.visibility.disclosureDate).isSameOrBefore(now);
//				});
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
.controller('EditChallengeModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, gameId, challenge, state, challengeIndex) {

	$scope.visibilityStates = ['Hidden','Visible'];
	$scope.states = ['PROPOSED','ASSIGNED', 'ACTIVE', 'FAILED', 'COMPLETED'];
	$scope.mode = new Array(Object.keys(challenge.fields).length).fill('normal');
	
	$scope.challengeIndex = challengeIndex;
	$scope.challenge = challenge;
	$scope.state = challenge.state;
	$scope.disclosureDate = challenge.visibility.disclosureDate;
	$scope.visibility = challenge.visibility.hidden ? $scope.visibilityStates[0] : $scope.visibilityStates[1];
	$scope.start = challenge.start;
	$scope.end = challenge.end;
	$scope.fields = Object.assign({}, challenge.fields);
	$scope.priority = challenge.priority;
	$scope.fieldData = {};
	
	$scope.alerts = {	
		'deleteError': false
	};


	$scope.fieldRemove = function(key) {
		delete $scope.fields[key];
	};
	
	$scope.fieldEdit = function(index, key, value) {
		$scope.mode[index] = 'edit';
		for(let i = 0; i < $scope.mode.length; i++) {
			if(i != index) {
				$scope.mode[i] = 'normal';
			}
		}

		$scope.fieldData['key'] = key;
		$scope.fieldData['value'] = value;
	}
	
	$scope.save = function (challengeIndex) {
		const now = moment();
		let challengeUpdate = {};
		challengeUpdate.name = challenge.name;
		challenge.visibility.hidden = $scope.visibility === 'Hidden';
		challenge.visibility.disclosureDate = $scope.disclosureDate;
		challengeUpdate.visibility = challenge.visibility;
		challenge.start = $scope.start;
		challengeUpdate.start = challenge.start;
		challenge.end = $scope.end;
		challengeUpdate.end = challenge.end;
		
		if(challenge.state != $scope.state) {
			challenge.state = $scope.state;
			challenge.stateDate[$scope.state] = now.valueOf();
			challenge.failed = challenge.state === 'FAILED';
			challengeUpdate.stateUpdate = {state : challenge.state, stateDate: challenge.stateDate[$scope.state] }; 
		}
		challenge.isVisible = !challenge.visibility.hidden || moment(challenge.visibility.disclosureDate).isSameOrBefore(now);
		challenge.fields = $scope.fields;
		challengeUpdate.fields = challenge.fields;
		challenge.priority = $scope.priority;
		challengeUpdate.priority = challenge.priority;

		gamesFactory.updateChallenge(gameId, state.playerId, challengeUpdate).then(function (data) {
			state.state['ChallengeConcept'].splice(challengeIndex, 1, challenge);
			$uibModalInstance.close(state);
		}, function(msg) {
			$scope.err = msg;
		});
	};

	
	// CANCEL button click event-handler
	$scope.cancelEditing = function (index) {
		$scope.mode[index] = 'normal';
	};
	
	$scope.saveEditing = function (index) {
		$scope.mode[index] = 'normal';
		const originalKey = Object.keys($scope.fields)[index];
		$scope.fieldRemove(originalKey);
		$scope.fields[$scope.fieldData.key] = $scope.fieldData.value;
	};
	
	
	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};

});