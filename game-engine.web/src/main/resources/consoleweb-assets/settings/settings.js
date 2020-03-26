angular.module('gamificationEngine.settings', [])
	.controller('SettingsCtrl', function ($scope, $rootScope, $window, $stateParams, gamesFactory) {
		$rootScope.currentNav = 'settings';
		$rootScope.currentGameId = $stateParams.id;
		
		const extractChallengeSettings = (game) => {
			const originalSettings = game.settings ? game.settings.challengeSettings : {};
			const challengeSettings =  Object.assign(originalSettings);
			challengeSettings.startDate = new Date(originalSettings.startDate); 
			return challengeSettings;
		};

		const isChallengesHidden = (game) => {
			const challengeSettings = extractChallengeSettings(game);
			return challengeSettings != undefined && 
				   challengeSettings.disclosure != undefined && 
				   challengeSettings.disclosure.startDate != undefined;
		};
		
		// Load game
		if($stateParams.id) {
			gamesFactory.getGameById($stateParams.id).then(function (game) {
				$scope.game = game;
				$scope.newGame = Object.create($scope.game);

				$scope.newGame.hideChallenges = isChallengesHidden($scope.game);
				$scope.newGame.challengeSettings = extractChallengeSettings($scope.game);
				
				if ($scope.game.expiration) {
					$scope.newGame.expiration = new Date($scope.game.expiration);
				} else {
					$scope.newGame.neverending = true;
					$scope.newGame.expiration = new Date();
				}
			}, function () {
				//do nothing
			});
		} else {
			$scope.game = {};
		}

		// Error alerts object
		$scope.alerts = {
			'editGameError': false,
			'nameError': '',
			'invalidTime': false,
			'settingsEdited': false,
			'error' : ''
		};

		const resetAlerts = () => {
			$scope.alerts.settingsEdited = false;
			$scope.alerts.editGameError = false;
			$scope.alerts.nameError = '';
			$scope.alerts.invalidTime = false;
			$scope.alerts.error = '';
		};
		
		// OK button click event-handler
		$scope.saveSettings = function () {
			var limit = ($scope.alerts.nameError) ? 1 : 0;
			var valid = true;
			resetAlerts();

			if (document.getElementsByClassName('has-error').length > limit) {
				$scope.alerts.invalidTime = true;
				valid = false;
			}
			if (!$scope.newGame.name) {
				$scope.alerts.nameError = 'messages:msg_game_name_error';
				valid = false;
			} else if (!!gamesFactory.getGameByName($scope.newGame.name) && $scope.game.name !== $scope.newGame.name) {
				$scope.alerts.nameError = 'messages:msg_game_name_exists_error';
				valid = false;
			}

			// check hideChallenges fields validity
			if($scope.newGame.hideChallenges) {
				const disclosureStartDate = $scope.newGame.challengeSettings.disclosure.startDate;
				const frequencyValue = $scope.newGame.challengeSettings.disclosure.frequency && $scope.newGame.challengeSettings.disclosure.frequency.value;
				const frequencyUnit = $scope.newGame.challengeSettings.disclosure.frequency && $scope.newGame.challengeSettings.disclosure.frequency.unit;
				if(disclosureStartDate == undefined){
					$scope.alerts.error = 'Disclosure date is required';
					valid = false;
				} else if (frequencyValue <= 0) {
					$scope.alerts.error = 'Frequency is required and should be a positive number';
					valid = false;
				} else if (! frequencyUnit) {
					$scope.alerts.error = 'Frequency unit is required';
					valid = false;
				}
			}
			
			if (valid) {
				$scope.disabled = true;
				var fields = {};
				fields.name = $scope.newGame.name;
				fields.expiration = $scope.newGame.expiration && !$scope.newGame.neverending ? $scope.newGame.expiration.getTime() : undefined;
				fields.domain = $scope.newGame.domain;
				if($scope.newGame.hideChallenges) {
					fields.challengeSettings = $scope.newGame.challengeSettings;
				} else {
					fields.challengeSettings = {};
				}

				// Edit game
				gamesFactory.editGame($scope.game, fields).then(
					function (data) {
						// Settings edited
						$scope.game.name = $scope.newGame.name;
						$scope.game.expiration = data.expiration;
						$scope.game.domain = $scope.newGame.domain;
						$scope.game.challengeSettings = data.challengeSettings;
						$scope.alerts.settingsEdited = true;
						$scope.disabled = false;
						if ($scope.new) {
							$scope.goToUrl('#/game/' + data.id);
						}
					},
					function (message) {
						// Show given error alert
						$scope.alerts.editGameError = 'messages:' + message;
						$scope.disabled = false;
					}
				);
			}
		};
		
		$scope.frequencyUnits = [
			{
				label : "days",
				value : "DAY"
			},
			{
				label : "hours",
				value : "HOUR"
			},
			{
				label : "minutes",
				value : "MINUTE"
			}
		];
		// CANCEL button click event-handler
		$scope.cancel = function () {
			$scope.goto('concepts');
		};
			 
	});