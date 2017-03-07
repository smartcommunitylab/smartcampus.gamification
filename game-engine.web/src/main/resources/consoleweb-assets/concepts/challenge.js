concepts.controller('ChallengeCtrl', function ($scope, $rootScope, $timeout, $uibModal, gamesFactory) {
		$scope.challenge = initChallenge();
		$scope.savedChallenges = [];

		// maintain copy of challenge objects in edit mode.
		$scope.editedChallenges = [];
		
		$scope.fieldName = [];
		$scope.hideEditMode = [];

		gamesFactory.readChallengeModels($rootScope.currentGameId).then(function (result) {
				$scope.savedChallenges = result;
				for(var i = 0; i < $scope.savedChallenges.length; i++) {
					$scope.hideEditMode[i] = true;
				}
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
				controller: 'DeleteChallengeConfirmModalCtrl',
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
			});
		}

		function initChallenge() {
			return {name:'', fields: []};
		}
		
		$scope.initViewMode = function (challengeId) {
			if ($scope.editedChallenges[challengeId]) {
				if ($scope.editedChallenges[challengeId].variables.length != $scope.savedChallenges[challengeId].variables.length
						| !($scope.arraysIdentical($scope.editedChallenges[challengeId].variables, $scope.savedChallenges[challengeId].variables))) {
					$scope.savedChallenges[challengeId] = angular.copy($scope.editedChallenges[challengeId]); 
				} 
			}
		}
		
		$scope.arraysIdentical = function (a, b) {
		    var i = a.length;
		    if (i != b.length) return false;
		    while (i--) {
		        if (a[i] !== b[i]) return false;
		    }
		    return true;
		};
		
		$scope.editChallenge = function (index) {
			var modelToEdit = $scope.savedChallenges[index];
			modelToEdit.fields = $scope.savedChallenges[index].variables;
			gamesFactory.saveChallengeModel($rootScope.currentGameId, modelToEdit).then(function (instance) {
				$scope.editedChallenges[index] = angular.copy($scope.savedChallenges[index]);
				$scope.fieldName[index] = null;
				$scope.hideEditMode[index] = true;
			}, function (message) {
				console.log("Challenge modification failure");
			});
		}
		
		$scope.deleteFieldInEditTab = function (idxC, idxF) {
			if ($scope.editedChallenges[idxC] == null) {
				$scope.editedChallenges[idxC] = angular.copy($scope.savedChallenges[idxC]);
			}
			var deletedField = $scope.savedChallenges[idxC].variables[idxF];
			$scope.savedChallenges[idxC].variables.splice(idxF, 1);
		}
		
		$scope.addFieldInEditMode = function(idxC, fieldName) {
			if ($scope.editedChallenges[idxC] == null) {
				$scope.editedChallenges[idxC] = angular.copy($scope.savedChallenges[idxC]); 
			}
			$scope.savedChallenges[idxC].variables.push(fieldName);
			$scope.fieldName[idxC] = null;
		};
		
		$scope.cancel = function(index) {
			$scope.hideEditMode[index]= !$scope.hideEditMode[index];
			$scope.fieldName[index] = null;
		}
	});

modals.controller('DeleteChallengeConfirmModalCtrl', function ($scope, $rootScope, $uibModalInstance, game, challengeId, savedChallenges, argument, gamesFactory) {
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