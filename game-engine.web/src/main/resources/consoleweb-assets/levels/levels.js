angular.module('gamificationEngine.levels', [])
	.controller('LevelsCtrl', function ($scope, $rootScope, $uibModal) {
		$rootScope.currentNav = 'levels';
		
		$scope.levels = [];
		$scope.input = {};
		$scope.thresholds = [];
		$scope.threshold = {};

		$scope.hide = true;
		
		$scope.editMode = false;
		$scope.loadedLevelIdx = -1;
		
		$scope.levels.push({name: 'Explorer', pointConcept:'green', thresholds: [{name :'Beginner', value:0.0},{name :'Expert', value:1000.0}]})
		$scope.levels.push({name: 'Viking', pointConcept:'yellow', thresholds: [{name :'Child', value:0.0},{name :'Warrior', value:1000.0}]})
		$scope.levels.push({name: 'Druid', pointConcept:'black', thresholds: []})
		
		$scope.addLevel = () => {
			$scope.showForm = true;
		};
		
		
		$scope.pointConcepts = $scope.game.pointConcept.map(pc => pc.name);
		
		$scope.oldThreshold;
		$scope.editingThresholdIdx = -1;
		
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
				$scope.levels.splice(elemIdx,1,{name: $scope.input.levelName, pointConcept: $scope.input.pointConcept, thresholds: $scope.thresholds});
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
				$scope.levels.push({name: $scope.input.levelName, pointConcept: $scope.input.pointConcept, thresholds: $scope.thresholds});

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
				$scope.levels.splice(levelIdx,1);
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