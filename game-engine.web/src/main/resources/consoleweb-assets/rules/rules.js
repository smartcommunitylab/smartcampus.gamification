'use strict'
var ruleModule = angular.module('gamificationEngine.rules', [])
	.controller('RulesCtrl', function ($scope, $rootScope, $timeout, $window, $stateParams, $uibModal, gamesFactory, ruleService) {
		const self = this;
		
		$rootScope.currentNav = 'rules';
		$rootScope.currentGameId = $stateParams.id;

		self.list = true;
		self.title = "labels:title_add_rule";
		self.action = "labels:btn_publish";
		self.input = {};
		self.alerts = {
			'nameError': false,
			'contentError': false,
			'ruleError': '',
			'ruleEdited': false,
			'ruleDeleted': false
		};
		
		const createRuleVm = (rule = {}) => {
			return {
				id : rule.id,
				name : rule.name ? rule.name : rule.id,
				content : rule.content,
				show : false,
				editing : false,
				position : -1
			};
		};
		
		self.rules = [];
		self.ruleForm;
		let loadedRule;
		
		
		// Load game
		gamesFactory.getGameById($stateParams.id).then(function (game) {
			self.game = game;
			self.rules = game.rules.map(r => createRuleVm(r));
		}, function () {
			// Show error alert
			self.alerts.loadGameError = true;
		});
		
		self.save = function () {
			self.alerts.nameError = false;
			self.alerts.contentError = false;
			self.alerts.ruleError = '';
			self.alerts.ruleValidation = '';
			let valid = true;

			// rule validation
			if (!self.ruleForm.content || self.ruleForm.content.length == 0) {
				self.alerts.contentError = true;
				valid = false;
			}
			if (!self.ruleForm.name || self.ruleForm.name.length == 0) {
				self.alerts.nameError = true;
				valid = false;
			}
			
			if (valid) {
				//check if already exist
				let found = false;
				if (self.ruleForm.editing == false || loadedRule.name != self.ruleForm.name) {
					for (var i = 0; i < self.rules.length && !found; i++) {
						found = self.rules[i].name == self.ruleForm.name;
					}
				}

				if (!found) {
					self.disabled = true;
					if (self.ruleForm.name != 'constants') {
						ruleService.validate(self.ruleForm.content).then(function (data) {
							if (data.length > 0) {
								self.alerts.ruleValidation = data;
								self.disabled = false;
							}
							else {
								addRuleAPI(self.game, self.ruleForm);
							}
						}, function (msg) {
							self.ruleForm.name = loadedRule.name;
							self.alerts.ruleError = 'messages:' + msg;
							self.disabled = false;
						});
					}
					else {
						addRuleAPI(self.game, self.ruleForm);
					}
				} else {
					self.alerts.ruleError = 'messages:msg_error_exist';
				}
			}
		};

		function addRuleAPI(game, rule) {
			ruleService.create(game, rule).then(
				function (data) {
					if (!self.ruleForm.editing) {
						self.rules.unshift(data);
					} else {
						self.rules[self.ruleForm.position] = createRuleVm(data);
					}
					self.disabled = false;
					self.list = true;
					self.alerts.ruleEdited = true;
					$timeout(function () {
						self.alerts.ruleEdited = false;
					}, 2000);
				},
				function (message) {
					self.ruleForm.name = loadedRule.name;
					// Show given error alert
					self.alerts.ruleError = 'messages:' + message;
					self.disabled = false;
				});
		}

		self.cancel = function () {
			self.list = true;
			self.alerts.nameError = false;
			self.alerts.contentError = false;
			self.alerts.ruleError = '';
			self.alerts.ruleValidation = '';
		}

		self.addRule = function () {
			self.input = {};
			loadedRule = null;
			self.alerts.ruleEdited = false;
			self.list = false;
			self.title = "labels:title_add_rule";
			self.action = "labels:btn_publish";
			
			self.ruleForm = createRuleVm();
			loadedRule = undefined;
		}

		self.editRule = function (ruleIndex) {
			
			loadedRule = angular.copy(self.rules[ruleIndex]);
			self.alerts.ruleEdited = false;
			self.list = false;
			self.title = "labels:title_edit_rule";
			self.action = "labels:btn_save";

			ruleService.read(self.game, loadedRule.id).then(
				function (data) {
					if (data) {
						$window.scrollTo(0, 0);
						self.ruleForm = createRuleVm(data);
						self.ruleForm.editing = true;
						self.ruleForm.position = ruleIndex;
					}
				},
				function (message) {
					// Show given error alert
					self.alerts.ruleError = 'messages:' + message;
				});
		};
	
		self.showRule = function (index) {
			var rule = self.rules[index];
			ruleService.read(self.game, rule.id).then(
				function (data) {
					if (data) {
						rule.content = data.content;
					}
				},
				function (message) {
					// Show given error alert
					self.rules[index] = '';
					self.alerts.ruleError = 'messages:' + message;
				});
			}

		self.deleteRule = function (ruleIndex) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteRuleModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return self.game;
					},
					rule: function () {
						return angular.copy(self.rules[ruleIndex]);
					}
				}
			});
			
			modalInstance.result.then(function () {
				self.rules.splice(ruleIndex, 1);
				self.alerts.ruleDeleted = true;
				
				$timeout(function () {
					self.alerts.ruleDeleted = false;
				}, 2000);
			});
		};
	
	});

// Delete rule modal
modals
	.controller('DeleteRuleModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, ruleService, game, rule) {
	$scope.alerts = {
		'deleteError': '',
	};
	$scope.argument = rule.name;

	$scope.delete = function () {
		ruleService.remove(game, rule.id).then(
			function (data) {
				if(data) {
					$uibModalInstance.close();
				} else {
					$scope.alerts.deleteError = 'Error deleting the rule';
				}
			},
			function (message) {
				$scope.alerts.deleteError = message;
			})
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});
