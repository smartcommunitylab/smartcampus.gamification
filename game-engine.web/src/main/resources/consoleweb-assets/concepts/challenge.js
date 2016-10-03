concepts.controller('ChallengeCtrl', function ($scope, $rootScope, $timeout, $uibModal, gamesFactory) {
		$scope.challenge = initChallenge();
		
		
		$scope.savedChallenges = [];
		
		gamesFactory.readChallengeModels($rootScope.currentGameId).then(function (result) {
			$scope.savedChallenges = result;
		}, function (message) {
			console.log("Challenge model failure");
		});
		
		
		$scope.addField = function() {
			$scope.challenge.fields.push('');
		};
		
		$scope.deleteField = function(idx) {
			$scope.challenge.fields.splice(idx,1);
		};
		
		$scope.addModel = function() {
			gamesFactory.saveChallengeModel($rootScope.currentGameId, $scope.challenge).then(function (instance) {
				$scope.challenge = initChallenge();
				$scope.savedChallenges.unshift(instance);
			}, function (message) {
				console.log("Challenge model failure");
			});
		};
		
		$scope.deleteModel = function(idx) {
			gamesFactory.deleteChallengeModel($rootScope.currentGameId, $scope.savedChallenges[idx].id).then(function (instance) {
				$scope.savedChallenges.splice(idx,1);
			}, function (message) {
				console.log("Challenge model failure");
			});
		};
		
		function initChallenge() {
			return {name:'', fields: []};
		}
	});