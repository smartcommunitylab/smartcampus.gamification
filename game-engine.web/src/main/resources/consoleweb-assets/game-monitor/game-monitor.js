angular.module('gamificationEngine.monitor', [])
	.controller('MonitorCtrl', function ($scope, $rootScope, $stateParams, $window, $uibModal, gamesFactory, $state) {
		$rootScope.currentGameId = $stateParams.id;
		$scope.currentPage = 1;
		$scope.items4Page = 10;

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
					p.totalScore += s.score;
				});
				
				p.hasCustomData = Object.keys(p.customData).length > 0;
				p.hasChallenges = challenges.length > 0;
				var referenceTimestamp = new Date().getTime();
				challenges.forEach(function(c) {
					c.active = referenceTimestamp < c.end;
					c.failed = referenceTimestamp > c.end && !c.completed;
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
			gamesFactory.getPlayersState($rootScope.currentGameId, $scope.playerIdFilter, $scope.currentPage, $scope.items4Page).then(function (data) {
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
	});
