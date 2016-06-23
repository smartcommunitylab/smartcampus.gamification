angular.module('gamificationEngine.monitor', [])
	.controller('MonitorCtrl', function ($scope, $rootScope, $stateParams, $uibModal, gamesFactory, $state) {
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
			$scope.err = 'msg_generic_error';
		});

		var enrichData = function (data) {
			data.forEach(function (p) {
				var badges = p.state['BadgeCollectionConcept'] ? p.state['BadgeCollectionConcept'] : [];
				var score = p.state['PointConcept'] ? p.state['PointConcept'] : [];
				p.totalBadges = 0;
				p.totalScore = 0;
				badges.forEach(function (b) {
					p.totalBadges += b.badgeEarned.length;
				});

				score.forEach(function (s) {
					p.totalScore += s.score;
				});
				p.hasCustomData = Object.keys(p.customData).length > 0;

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



		gamesFactory.getPlayersState($rootScope.currentGameId, $scope.playerIdFilter, $scope.currentPage, $scope.items4Page).then(function (data) {
			data.content = enrichData(data.content);
			$scope.playerStates = data;
			$scope.totalItems = data.totalElements;
		}, function (msg) {
			$scope.err = msg;
		});

		$scope.expand = false;

		$scope.filter = function () {
			$rootScope.monitorFilter = $scope.playerIdFilter;
			gamesFactory.getPlayersState($rootScope.currentGameId, $scope.playerIdFilter, $scope.currentPage, $scope.items4Page).then(function (data) {
				data.content = enrichData(data.content);
				$scope.playerStates = data;
				$scope.totalItems = data.totalElements;
			}, function (msg) {
				$scope.err = msg;
			});
		}
		$scope.openDetails = function (idx) {
			if (idx != $scope.expandIdx) {
				$scope.expandIdx = idx;
			} else {
				$scope.expandIdx = -1000;
			}
		};

		$scope.update = function () {
			$scope.expandIdx = -1000;
			gamesFactory.getPlayersState($rootScope.currentGameId, $scope.playerIdFilter, $scope.currentPage, $scope.items4Page).then(function (data) {
				data.content = enrichData(data.content);
				$scope.playerStates = data;
				$scope.totalItems = data.totalElements;
			}, function (msg) {
				$scope.err = msg;
			});
		};
	});
