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
			// Delete a challenge.
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteConceptConfirmModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					challengeId: function () {
						return idx;
					},
					savedChallenges: function () {
						return $scope.savedChallenges;
					}
					,
					argument:  function () {
						return $scope.savedChallenges[idx].name;
					}
				}
			});
		}

		function initChallenge() {
			return {name:'', fields: []};
		}
	});

modals.controller('DeleteConceptConfirmModalInstanceCtrl', function ($scope, $rootScope, $uibModalInstance, game, challengeId, savedChallenges, argument, gamesFactory) {
	$scope.argument = argument;

	$scope.alerts = {
		'deleteError': false
	};

	// DELETE button click event-handler
	$scope.delete = function () {
		gamesFactory.deleteChallengeModel($rootScope.currentGameId, savedChallenges[challengeId].id).then(
				function (success) {
					savedChallenges.splice(challengeId,1);
					$uibModalInstance.close();
				}
				, function (message) {
					console.log("Challenge delete operation failed");	
				});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});