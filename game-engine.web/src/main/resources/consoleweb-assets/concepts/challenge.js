concepts.controller('ChallengeCtrl', function ($scope, $rootScope, $timeout, $uibModal, gamesFactory) {
		$scope.challenge = initChallenge();
		$scope.hideEditMode = true;
		
		$scope.savedChallenges = [];
		
		$scope.refreshChallengeModel = function() {
			gamesFactory.readChallengeModels($rootScope.currentGameId).then(function (result) {
				$scope.savedChallenges = result;
			}, function (message) {
				console.log("Challenge model failure");
			});
		}
		
		$scope.refreshChallengeModel();
		
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
			
			modalInstance.result.then(function () {
				$scope.challenge.deleted = true;
				
				$timeout(function () {
					$scope.challenge.deleted = false;
				}, 4000);
			});
		}

		function initChallenge() {
			return {name:'', fields: []};
		}
		
		$scope.editChallenge = function (challengeId) {
			var modelToEdit = $scope.savedChallenges[challengeId];
			modelToEdit.fields = $scope.savedChallenges[challengeId].variables;
			gamesFactory.saveChallengeModel($rootScope.currentGameId, modelToEdit).then(function (instance) {
				console.log("Challenge: " + challengeId +  " edited successfully");
				$scope.challenge.fieldEdited = true;
				$scope.refreshChallengeModel();
				$timeout(function () {
					$scope.challenge.fieldEdited = false;
				}, 4000);
			}, function (message) {
				console.log("Challenge modification failure");
			});
		}
		
		$scope.deleteFieldInEditTab = function (idxC, idxF) {
			var deletedField = $scope.savedChallenges[idxC].variables[idxF];
			$scope.savedChallenges[idxC].variables.splice(idxF, 1);
			$scope.refreshChallengeModel();
		}
		
		$scope.addFieldInEditMode = function(idxC, fieldName) {
			$scope.savedChallenges[idxC].variables.push(fieldName);
		};
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