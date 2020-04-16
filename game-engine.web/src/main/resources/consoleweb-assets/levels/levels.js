angular.module('gamificationEngine.levels', [])
	.controller('LevelsCtrl', function ($scope, $rootScope, $uibModal, $stateParams, gamesFactory) {
		$rootScope.currentNav = 'levels';

		const groupChallengeModels = ["groupCooperative", "groupCompetitiveTime", "groupCompetitivePerformance"];
		
		$scope.levels = [];
		$scope.input = {};
		$scope.thresholds = [];
		$scope.threshold = {};

		// $scope.hide = true;
		
		$scope.editMode = false;
		$scope.loadedLevelIdx = -1;
		
		const gameId = $stateParams.id;
		
		// Load games
		gamesFactory.getGameById(gameId).then(function (game) {
			$scope.levels = game.levels.map(l => levelVm(l));
		}, function () {
			// do nothing
		});
		
		let challengeModelsNames;
		gamesFactory.readChallengeModels(gameId)
			.then(
				models => {
					challengeModelsNames = models.map(m => m.name).concat(groupChallengeModels);
				},
				() => {}
			);
		
		$scope.addLevel = () => {
			$scope.showForm = true;
		};
		
		$scope.toggleLevel = (idx) => {
			$scope.levels[idx].show = !$scope.levels[idx].show;
			if($scope.loadedLevelIdx > -1 && $scope.loadedLevelIdx != idx) {
				$scope.levels[$scope.loadedLevelIdx].show = false;
			}
			$scope.loadedLevelIdx = idx;
			$scope.thresholds = $scope.levels[idx].thresholds.map(t => thresholdVm(t)); 
		};
		
		$scope.pointConcepts = $scope.game.pointConcept.map(pc => pc.name);
		
		$scope.oldThreshold;
		$scope.editingThresholdIdx = -1;
		
		
		$scope.toggleConfigPanel = (idx) => {
			const modalInstance = $uibModal.open({
				templateUrl: 'levels/threshold-config.html',
				controller: 'ThresholdConfigCtrl',
				backdrop: "static",
				resolve: {
					threshold : function() {
						return angular.copy($scope.thresholds[idx]);
					},
					challengeModels : function() {
						return challengeModelsNames;
					}
				}
			 });
			
			modalInstance.result.then((newConfig) => {
				const level = $scope.levels[$scope.loadedLevelIdx];
				const levelIdx = $scope.loadedLevelIdx;
				level.thresholds[idx].config = newConfig;
				gamesFactory.saveLevel(gameId, level).then( function(level) {
					$scope.thresholds[idx].config = newConfig;
					// $scope.levels[levelIdx].thresholds = $scope.thresholds;
					$scope.levels.splice(levelIdx, 1, level);
				}, function(msg) {
					$scope.errors = 'messages:'+msg;
				});
				
			});
		};
    
		const levelVm = (model) => {
	      return {
	    	 name : model.name,
	    	 thresholds : model.thresholds,
	    	 pointConcept : model.pointConcept,
	      	 show : false
	      };
		};
		
	    const thresholdVm = (model) => {
	      return {
	    	 name : model.name,
	    	 value : model.value,
	    	 config : model.config,
	    	 edit : false,
	      };
	    };
		
		$scope.updateLevel = () => {
			$scope.errors = '';
			
			if(!$scope.input.levelName || !$scope.input.pointConcept) {
				$scope.errors = 'messages:msg_empty_fields';
			} 
			
			const elemIdx = $scope.levels.findIndex(level => level.name.toUpperCase() === $scope.input.levelName.toUpperCase());
			
			let duplicatedValue = false;
			$scope.levels.forEach( (elem, index ) => {
				if(elem.pointConcept == $scope.input.pointConcept && index != $scope.loadedLevelIdx) {
					duplicatedValue = true;
					return;
				}
			});
			
			if(duplicatedValue) {
				$scope.errors = 'messages:msg_level_pointConcept';
				return;
			}
			
			if(elemIdx > -1) {
				restoreThresholds();
				const lev = {name: $scope.input.levelName, pointConcept: $scope.input.pointConcept, thresholds: $scope.thresholds};
				gamesFactory.saveLevel(gameId, lev).then(function(level) {
					$scope.levels.splice(elemIdx,1,level);
				}, function(msg) {
					$scope.errors = 'messages:'+msg;
				});
				
			}
			
			collapseForm();
			
		}
    
		$scope.saveLevel = () => {
			$scope.errors = '';
			if(!$scope.input.levelName || !$scope.input.pointConcept) {
				$scope.errors = 'messages:msg_empty_fields';
			} else {
				const nameAlreadySavedIdx = $scope.levels.findIndex(level => level.name.toUpperCase() === $scope.input.levelName.toUpperCase());
				if(nameAlreadySavedIdx > -1) {
					$scope.errors = 'messages:msg_level_name';
					return;
				}
				const pointConceptAlreadyUsedIdx = $scope.levels.findIndex(level => level.pointConcept === $scope.input.pointConcept);
				if(pointConceptAlreadyUsedIdx > -1) {
					$scope.errors = 'messages:msg_level_pointConcept';
					return;
				}
				restoreThresholds();
				const lev = {name: $scope.input.levelName, pointConcept: $scope.input.pointConcept, thresholds: $scope.thresholds};
				gamesFactory.saveLevel(gameId, lev).then(function(level) {
					$scope.levels.push(level);
				}, function(msg) {
					$scope.errors = 'messages:'+msg;
				});
				

				collapseForm();
			}
		};
		
		restoreThresholds = () => {
			if($scope.editingThresholdIdx > -1) {
				$scope.thresholds.splice($scope.editingThresholdIdx,1,$scope.oldThreshold);
				$scope.thresholds[$scope.editingThresholdIdx].edit = false;
			}
		};
		
		$scope.editThreshold = (thresIdx) => {
			$scope.thresholds[thresIdx].edit = true;
			$scope.oldThreshold = Object.assign({}, $scope.thresholds[thresIdx]);
			$scope.editingThresholdIdx = thresIdx;
			
		};
		
		$scope.cancelThresholdEditing = (idx) => {
			$scope.thresholds[idx].edit = false;
			$scope.thresholds[idx].validationMsg = '';
			$scope.thresholds[idx].value = $scope.oldThreshold.value;
			$scope.editingThresholdIdx = -1;
		};
		
		$scope.editLevel = (idx) => {
			const level = $scope.levels[idx];
			$scope.editMode = true;
			$scope.loadedLevelIdx = idx; 
			$scope.input.levelName = level.name;
			$scope.input.pointConcept = level.pointConcept;
			$scope.thresholds = level.thresholds.map(thres => Object.assign({},thres));
			$scope.threshold = {};
			$scope.showForm = true;
			$scope.errors = '';
			$scope.validationMsg = '';
		};
		
		
		$scope.updateThreshold = (idx) => {
			const editedThreshold = $scope.thresholds[idx];
			
			let duplicatedValue = false;
			$scope.thresholds.forEach( (elem, index ) => {
				if(elem.value == editedThreshold.value && index != idx) {
					duplicatedValue = true;
					return;
				}
			});
			
			if(duplicatedValue) {
				$scope.thresholds[idx].validationMsg = 'messages:msg_threshold_value';
				return;
			}
			if( Number.isNaN(Number.parseFloat(editedThreshold.value)) || Number.parseFloat(editedThreshold.value) < 0 ) {
				$scope.thresholds[idx].validationMsg = 'messages:msg_threshold_valid_value';
				return;
			}
			
			editedThreshold.value = Number.parseFloat(editedThreshold.value);
			$scope.thresholds[idx].edit = false;
			$scope.thresholds[idx].validationMsg = '';
			$scope.editingThresholdIdx = -1;
		}
		
		$scope.saveThreshold = () => {
			
			$scope.validationMsg = '';

			if(!$scope.threshold.name || !$scope.threshold.value) {
				$scope.validationMsg = 'messages:msg_empty_fields';
				return;
			}
			
			const nameAlreadyPresentIdx = $scope.thresholds.findIndex(thres => thres.name.toUpperCase() === $scope.threshold.name.toUpperCase());
			
			if(nameAlreadyPresentIdx > -1) {
				$scope.validationMsg = 'messages:msg_threshold_name';
				return;
			}
			
			const valueAlreadyPresentIdx = $scope.thresholds.findIndex(thres => thres.value == $scope.threshold.value);
			if(valueAlreadyPresentIdx > -1) {
				$scope.validationMsg = 'messages:msg_threshold_value';
				return;
			}
			if( Number.isNaN(Number.parseFloat($scope.threshold.value)) || Number.parseFloat($scope.threshold.value) < 0 ) {
				$scope.validationMsg = 'messages:msg_threshold_valid_value';
				return;
			}
			$scope.threshold.value = Number.parseFloat($scope.threshold.value);
			$scope.thresholds.push($scope.threshold);
			$scope.threshold = {};
		};
		
		$scope.cancel = () => {
			restoreThresholds();
			collapseForm();
		};
		
		$scope.deleteThreshold = (thresholdIdx) => {
			const modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteThresholdModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					threshold : function() {
						return $scope.thresholds[thresholdIdx];
					}
				}
			 });
			
			modalInstance.result.then( () => {
				$scope.thresholds.splice(thresholdIdx,1);
			});
			
		};
		
		
		$scope.deleteLevel = (levelIdx) => {
			const modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteLevelModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					level : function() {
						return $scope.levels[levelIdx];
					}
				}
			 });
			
			modalInstance.result.then( () => {
				gamesFactory.deleteLevel(gameId , $scope.levels[levelIdx]).then(
					function() {
						$scope.levels.splice(levelIdx,1);
						if($scope.loadedLevelIdx == levelIdx) {
							collapseForm();
						}
					},
					function(msg) {
						$scope.errors = 'messages:'+msg;
					}
				);
			});
			
		};
		
		const collapseForm = () => {
			$scope.editMode = false;
			$scope.loadedLevelIdx = -1; 
			$scope.showForm = false;
			$scope.errors = '';
			$scope.validationMsg = '';
			$scope.input = {};
			$scope.thresholds = [];
			$scope.threshold = {};
		};
	});

// ************************************************* MODALS CONTROLLERS

modals
.controller('DeleteLevelModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, level) {
	$scope.alerts = {
		'deleteError': '',
	};
	
	$scope.argument = level.name;

	$scope.delete = function () {
		$uibModalInstance.close();
		
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});


modals
.controller('DeleteThresholdModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, threshold) {
	$scope.alerts = {
		'deleteError': '',
	};
	
	$scope.argument = threshold.name;

	$scope.delete = function () {
		$uibModalInstance.close();
		
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});

modals
.controller('ThresholdConfigCtrl', function ($scope, $uibModalInstance, gamesFactory, threshold, challengeModels) {

	$scope.config = threshold.config;
	$scope.thresholdName = threshold.name;
	$scope.availableChallengeModelsOptions = challengeModels;
	$scope.activeChallengeModelsOptions =  challengeModels;
	
	$scope.$watchCollection('config.availableModels', (newValues) => {
		if(newValues) {
			$scope.activeChallengeModelsOptions = challengeModels.filter(m => !newValues.includes(m));
		}
	});
	
	$scope.$watchCollection('config.activeModels', (newValues) => {
		if(newValues) {
			$scope.availableChallengeModelsOptions = challengeModels.filter(m => !newValues.includes(m));
		}
	});
	
	$scope.save = function () {
		$uibModalInstance.close($scope.config);
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};

});
