/*
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

// Edit game modal
var modals = angular.module('gamificationEngine.modals', [])
	// Edit game modal
	.controller('EditGameModalInstanceCtrl', function ($scope, $uibModalInstance, game, gamesFactory) {
		$scope.newGame = {};
		$scope.newGame.name = game.name;

		if (game.expiration) {
			$scope.newGame.expiration = new Date(game.expiration);
		} else {
			$scope.newGame.neverending = true;
			$scope.newGame.expiration = new Date();
		}
		// Error alerts object
		$scope.alerts = {
			'editGameError': '',
		};

		// OK button click event-handler
		$scope.ok = function () {
			$scope.disabled = true;
			$scope.alerts.editGameError = '';
			if (document.getElementsByClassName('has-error').length == 0) {
				var fields = {};
				fields.name = $scope.newGame.name;
				fields.expiration = $scope.newGame.expiration && !$scope.newGame.neverending ? $scope.newGame.expiration.getTime() : undefined;

				// Edit game
				gamesFactory.editGame(game, fields).then(
					function () {
						// Settings edited
						$uibModalInstance.close();
					},
					function (message) {
						// Show given error alert
						$scope.alerts.editGameError = 'messages:' + message;
						$scope.disabled = false;
					}
				);
			} else {
				$scope.alerts.editGameError = 'messages:msg_invalid_time';
				$scope.disabled = false;
			}
		};

		// CANCEL button click event-handler
		$scope.cancel = function () {
			$uibModalInstance.dismiss('cancel');
		};
	})

// Delete game modal
.controller('DeleteGameConfirmModalInstanceCtrl', function ($scope, $uibModalInstance, $window, game, gamesFactory) {
	$scope.argument = game.name;

	$scope.alerts = {
		'deleteError': false,
	}

	// DELETE button click event-handler
	$scope.delete = function () {
		// Delete game
		gamesFactory.deleteGame(game).then(function () {
			// Game has been deleted
			// Redirect to homepage
			$window.location.href = '#/home';
			$uibModalInstance.close();
		}, function (data) {
			$scope.alerts.deleteError = true;
		});
	};

	// CANCEL button click event-handler
	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	}
});
