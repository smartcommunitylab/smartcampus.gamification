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

// Main controller (index.html)
function MainCtrl($scope, $rootScope,$state) {
  $rootScope.games = [];
  if($state.current.data) {
	  $rootScope.page = $state.current.data.page;
  }
  
  $scope.goHome = function() {
	  $rootScope.page = 'home';
  }
}

// Login controller (login.html)
function LoginCtrl($scope, $rootScope) {
  $rootScope.currentNav = 'login';
}

// Home controller (home.html)
function HomeCtrl($scope, $rootScope, $window, $modal, gamesFactory, utilsFactory) {
  $rootScope.currentNav = 'home';
  $rootScope.currentGameId = 1;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false
  };

  // Load games
  gamesFactory.getGames().then(function () {
	  $rootScope.games.forEach(function(g) {
		 g.terminated = g.expiration && g.expiration <= new Date().getTime();
	  });
	  
  }, function () {
    // Reject: show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  };

  $scope.openGameModal = function () {
    // Add new game
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_game_edit.html',
      controller: EditGameModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        game: function () {
          return {};
        }
      }
    });
  };
  
  
  $scope.deleteGame = function (game) {
	    // Delete a game
	    var modalInstance = $modal.open({
	      templateUrl: 'templates/modals/modal_delete_confirm.html',
	      controller: DeleteGameConfirmModalInstanceCtrl,
	      backdrop: "static",
	      resolve: {
	        game: function () {
	          return game;
	        }
	      }
	    });
	  };

  $scope.countActive = function (game, type) {
    return utilsFactory.countActive(game, type);
  };

  $scope.getLength = function (game, type) {
    return utilsFactory.getLength(game, type);
  };

  $scope.goto = function (path) {
    $window.location.href = path;
  };
}
// Game controller (game.html)
function GameCtrl($scope, $rootScope, $window, $stateParams, $modal, gamesFactory, utilsFactory,$state) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $stateParams.id;

  if($state.current.data) {
	  $rootScope.page = $state.current.data.page;
  }
  
  // Error alerts object
  $scope.alerts = {
    'cantCreateLeaderboards': false,
    'loadGameError': false,
    'settingsEdited': false
  };

  // Navbar names array
  $scope.pluginNames = {
    'points': 'points',
    'badges_collections': 'badges collections',
  };

  // Tab switching
  $scope.active = {
    'points': false,
    'badges_collections': false,
  };

  // Read the tab param to select the right tab. If it isn't given, choose the dafualt tab
  var tab = $stateParams.tab;

  if (!!tab && (tab == 'points' || tab == 'badges_collections')) {
    // User choice (tab)
    $scope.active[tab] = true;
    $scope.selectedInstance = tab;
  } else {
    // Default choice = 'points'
    $scope.selectedInstance = 'points';
  }

  $scope.game = {};

  // Load games
  gamesFactory.getGameById($stateParams.id).then(function (game) {
    $scope.game = game;
  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  // default
  $scope.viewName = 'concepts';
  $scope.menuItem = 'concepts';
  
  gamesFactory.getPoints($rootScope.currentGameId).then(function (points) {
	  $scope.points = points;
  });
  
  gamesFactory.getBadges($rootScope.currentGameId).then(function (badges) {
	  $scope.badges = badges;
  });
  
  
  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  };

  $scope.goto = function (path) {
	$scope.viewName = path;
	$scope.menuItem = path;
  };

  $scope.goToTab = function (tab) {
    $window.location.href = '#/game/' + $scope.game.id + '?tab=' + tab;
  };

  $scope.countActive = function (game, type) {
    return utilsFactory.countActive(game, type);
  };

  $scope.getLength = function (game, type) {
    return utilsFactory.getLength(game, type);
  };

  $scope.openEditModal = function (id) {
    // Edit a game
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_game_edit.html',
      controller: EditGameModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        game: function () {
          return $scope.game;
        }
      }
    });

    modalInstance.result.then(function () {
      // Show 'settings successfully edited' alert
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.openAddInstanceModal = function () {
    // Add new plugin instance
    switch ($scope.selectedInstance) {
    case 'points':
      $scope.openAddPointsInstanceModal();
      break;
    case 'badges_collections':
      $scope.openAddBadgesCollectionsInstanceModal();
      break;
    }
  };

  $scope.openAddPointsInstanceModal = function () {
    // Add a new points instance
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_points_instance_edit.html',
      controller: EditPointsInstanceModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        game: function () {
          return $scope.game;
        },
        instance: function () {
          return {};
        }
      }
    });
  };

  $scope.openAddBadgesCollectionsInstanceModal = function () {
    // Add a new badges collection instance
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_badges_collection_instance_edit.html',
      controller: EditBadgesCollectionInstanceModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        game: function () {
          return $scope.game;
        },
        instance: function () {
          return {};
        }
      }
    });
  };
  
  $scope.deleteConcept = function (instance,type) {
	    // Delete a game
	    var modalInstance = $modal.open({
	      templateUrl: 'templates/modals/modal_delete_confirm.html',
	      controller: DeleteConceptConfirmModalInstanceCtrl,
	      backdrop: "static",
	      resolve: {
	        game: function () {
	          return $scope.game;
	        },
	        instance: function () {
	          return instance;
	        },
	        type: function () {
	          return type;
	        }
	      }
	    });
	  };
}

// Points instance controller (game_points.html)
function GamePointsCtrl($scope, $rootScope, $stateParams, $modal, $window, gamesFactory) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $stateParams.id;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false,
    'settingsEdited': false
  };

  // Load game and points instance
  gamesFactory.getInstanceById($stateParams.id, 'points', $stateParams.idPoints).then(function (response) {
    $scope.game = response.game;
    $scope.points = response.inst;

  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  };

  $scope.openEditInstanceModal = function () {
    // Edit points instance
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_points_instance_edit.html',
      controller: EditPointsInstanceModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        game: function () {
          return $scope.game;
        },
        instance: function () {
          return $scope.points;
        }
      }
    });

    modalInstance.result.then(function () {
      // Show 'settings successfully edited' alert
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
    // Delete points instance
    // Before points removal, tell the user that linked leaderboard will be deleted too
    var leaderboards = gamesFactory.pointsDeleteCheck($scope.game, $scope.points);

    if (leaderboards.length != 0) {
      // There are linked leaderboards
      var modalInstance = $modal.open({
        templateUrl: 'templates/modals/modal_delete_leaderboards_confirm.html',
        controller: DeleteLeaderboardsConfirmModalInstanceCtrl,
        backdrop: "static",
        resolve: {
          game: function () {
            return $scope.game;
          },
          leaderboards: function () {
            return leaderboards;
          }
        }
      });

      modalInstance.result.then(function () {
        var secondModalInstance = $modal.open({
          templateUrl: 'templates/modals/modal_delete_confirm.html',
          controller: DeleteInstanceConfirmModalInstanceCtrl,
          backdrop: "static",
          resolve: {
            game: function () {
              return $scope.game;
            },
            instance: function () {
              return $scope.points;
            },
            instanceType: function () {
              return 'points';
            }
          }
        });
      });
    } else {
      // There are no linked leaderboards
      var secondModalInstance = $modal.open({
        templateUrl: 'templates/modals/modal_delete_confirm.html',
        controller: DeleteInstanceConfirmModalInstanceCtrl,
        backdrop: "static",
        resolve: {
          game: function () {
            return $scope.game;
          },
          instance: function () {
            return $scope.points;
          },
          instanceType: function () {
            return 'points';
          }
        }
      });
    }
  };

  $scope.deleteRule = function () {
    // Delete rule
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteRuleConfirmModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        argument: function () {
          return "TODO";
        }
      }
    });
  };

  // Edit rule
  $scope.openEditRuleModal = function () {
    // TODO: adjust to pass parameters
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_rule_edit.html',
      controller: EditRuleModalInstanceCtrl,
      backdrop: "static",
    });
  };

  // Add rule
  $scope.openAddRuleModal = function () {
    // TODO: adjust to pass parameters
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_rule_edit.html',
      controller: EditRuleModalInstanceCtrl,
      backdrop: "static",
    });
  };
}

// Badges collection instance controller (game_badges_collection.html)
function GameBadgesCollectionCtrl($scope, $rootScope, $stateParams, $modal, $window, gamesFactory) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $stateParams.id;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false,
    'settingsEdited': false
  };

  // Tab switching
  $scope.active = {
    'rules': false,
    'badges': false
  };

  // Read the tab param to select the right tab. If it isn't given, choose the dafualt tab
  var tab = $stateParams.tab;

  if (!!tab && (tab == 'rules' || tab == 'badges')) {
    // User choice (tab)
    $scope.active[tab] = true;
  } else {
    // Default choice = 'points'
    $scope.active.rules = true;
  }

  // Load game and badges collection instance
  gamesFactory.getInstanceById($stateParams.id, 'badges_collections', $stateParams.idBadgesCollection).then(function (response) {
    $scope.game = response.game;
    $scope.badges_collection = response.inst;
  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  };

  $scope.goToTab = function (tab) {
    $window.location.href = '#/game/' + $scope.game.id + '/badges_collections/' + $scope.badges_collection.id + '?tab=' + tab;
  };

  $scope.openEditInstanceModal = function () {
    // Edit badges collection instance
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_badges_collection_instance_edit.html',
      controller: EditBadgesCollectionInstanceModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        game: function () {
          return $scope.game;
        },
        instance: function () {
          return $scope.badges_collection;
        }
      }
    });

    modalInstance.result.then(function () {
      // Show 'settings successfully edited' alert
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
    // Delete badges collection instance
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteInstanceConfirmModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        game: function () {
          return $scope.game;
        },
        instance: function () {
          return $scope.badges_collection;
        },
        instanceType: function () {
          return 'badges_collections';
        }
      }
    });
  };

  $scope.openAddBadgeModal = function () {
    // Add badge
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_badges_edit.html',
      controller: EditBadgesModalInstanceCtrl,
      backdrop: "static",
    });
  };

  $scope.deleteRule = function () {
    // Delete rule
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteRuleConfirmModalInstanceCtrl,
      backdrop: "static",
      resolve: {
        argument: function () {
          return "TODO";
        }
      }
    });
  };

  // Edit rule
  $scope.openEditRuleModal = function () {
    // TODO: adjust to pass parameters
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_rule_edit.html',
      controller: EditRuleModalInstanceCtrl,
      backdrop: "static",
    });
  };

  // Add rule
  $scope.openAddRuleModal = function () {
    // TODO: adjust to pass parameters
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_rule_edit.html',
      controller: EditRuleModalInstanceCtrl,
      backdrop: "static",
    });
  };
}


// Actions controller (actions.html)
function ActionsCtrl($scope, $rootScope, $stateParams, $modal, gamesFactory) {

  $rootScope.currentNav = 'actions';
  $rootScope.currentGameId = $stateParams.id;

  // Error alerts object
  $scope.alerts = {
    'success': '',
    'error': ''
  };

  $scope.closeAlert = function (alertName) {
	    $scope.alerts[alertName] = '';
	  }
  
  //Add action
  $scope.openAddActionModal = function () {
	  var modalInstance = $modal.open({
		  templateUrl: 'templates/modals/modal_action_edit.html',
	      controller: EditActionModalInstanceCtrl,
	      backdrop: "static",
	      resolve: {
	      game: function () {
	          return $scope.game;
	        },
	      action: function () {
	    	  return $scope.actionName;
	      }
	      }
	    });
  };
  
  $scope.deleteAction = function (action) {
	    // Delete a game
	    var modalInstance = $modal.open({
	      templateUrl: 'templates/modals/modal_delete_confirm.html',
	      controller: DeleteActionConfirmModalInstanceCtrl,
	      backdrop: "static",
	      resolve: {
	        game: function () {
	          return $scope.game;
	        },
	        argument: function() {
	        	return action;
	        }
	      }
	    });
	  };
  
  
  
  // Load game
  gamesFactory.getGameById($stateParams.id).then(function (game) {
    $scope.game = game;
  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  }

  $scope.choose = function () {
    $scope.path = "OK";
  };

  $scope.uploadImport = function () {
    $scope.dataImported = {};
  };

  $scope.clear = function () {
    $scope.dataImported = undefined;
  };

  $scope.confirm = function () {};
}

// rules.html
function RulesCtrl($scope, $rootScope, $stateParams, $modal, gamesFactory) {
	 $rootScope.currentNav = 'rules';
	  $rootScope.currentGameId = $stateParams.id;

	  // Error alerts object
	  $scope.alerts = {
	    'success': '',
	    'error': ''
	  };

	  $scope.closeAlert = function (alertName) {
		    $scope.alerts[alertName] = '';
		  }
	  
	  //Add action
	  $scope.openAddRuleModal = function () {
		  var modalInstance = $modal.open({
			  templateUrl: 'templates/modals/modal_rule_edit.html',
		      controller: EditRuleModalInstanceCtrl,
		      backdrop: "static",
		      resolve: {
		      game: function () {
		          return $scope.game;
		      },
		      rule: function() {
		      }
		    }
		  });
	  };
	  
	  
	  //Add action
	  $scope.editRule = function (rule) {
		  var modalInstance = $modal.open({
			  templateUrl: 'templates/modals/modal_rule_edit.html',
		      controller: EditRuleModalInstanceCtrl,
		      backdrop: "static",
		      resolve: {
		      game: function () {
		          return $scope.game;
		      },
		      rule: function() {
		    	  return rule;
		      }
		    }
		  });
	  };
	  
	  $scope.deleteRule = function (rule) {
		    // Delete a game
		    var modalInstance = $modal.open({
		      templateUrl: 'templates/modals/modal_delete_confirm.html',
		      controller: DeleteRuleModalInstanceCtrl,
		      backdrop: "static",
		      resolve: {
		        game: function () {
		          return $scope.game;
		        },
		        rule: function () {
		        	return rule;
		        }
		      }
		    });
		  };
	  
	  
	  
	  // Load game
	  gamesFactory.getGameById($stateParams.id).then(function (game) {
	    $scope.game = game;
	  }, function () {
	    // Show error alert
	    $scope.alerts.loadGameError = true;
	  });

	  $scope.closeAlert = function (alertName) {
	    $scope.alerts[alertName] = false;
	  }

	  $scope.choose = function () {
	    $scope.path = "OK";
	  };

	  $scope.uploadImport = function () {
	    $scope.dataImported = {};
	  };

	  $scope.clear = function () {
	    $scope.dataImported = undefined;
	  };

	  $scope.confirm = function () {};
}

// tasks.html
function TasksCtrl($scope, $rootScope, $stateParams, $modal, gamesFactory) {
	  $rootScope.currentNav = 'tasks';
	  $rootScope.currentGameId = $stateParams.id;

	  // Error alerts object
	  $scope.alerts = {
	    'success': '',
	    'error': ''
	  };
	  
	  var convertTask = function(task) {
		  // convert in taskDto
			 if(task.schedule) {
				 task.cronExpression = task.schedule.cronExpression;
				 task.schedule = undefined;
			 }
		return task;
	  }
	  
	  $scope.openAddTaskModal = function () {
		  var modalInstance = $modal.open({
		      templateUrl: 'templates/modals/modal_task_edit.html',
		      controller: EditTaskModalInstanceCtrl,
		      backdrop: "static",
		      resolve: {
		        game: function () {
		          return $scope.game;
		        },
		        task: function () {
		        	return;
		        }
		      }
		    });
	  };
	  
	  $scope.editTask = function (task) {
		  var modalInstance = $modal.open({
		      templateUrl: 'templates/modals/modal_task_edit.html',
		      controller: EditTaskModalInstanceCtrl,
		      backdrop: "static",
		      resolve: {
		        game: function () {
		          return $scope.game;
		        },
		        task: function () {
		        	return convertTask(task);
		        }
		      }
		    });
	  };
	  
	  $scope.deleteTask = function (task) {
		    // Delete a game
		    var modalInstance = $modal.open({
		      templateUrl: 'templates/modals/modal_delete_confirm.html',
		      controller: DeleteTaskModalInstanceCtrl,
		      backdrop: "static",
		      resolve: {
		        game: function () {
		          return $scope.game;
		        },
		        task: function () {
		        	return convertTask(task);
		        }
		      }
		    });
		  };
	  
	  
	  
	  
	// Load game
	  gamesFactory.getGameById($stateParams.id).then(function (game) {
	    $scope.game = game;
	  }, function () {
	    // Show error alert
	    //$scope.alerts.loadGameError = true;
	  });

}

function MonitorCtrl($scope, $rootScope, $stateParams, $modal, gamesFactory,$state) {
	$rootScope.currentGameId = $stateParams.id;
	$scope.currentPage = 1;
	$scope.items4Page = 10;
 	
	  if($state.current.data) {
		  $rootScope.page = $state.current.data.page;
	  }
	  
	if($rootScope.monitorFilter) {
		$scope.playerIdFilter = $rootScope.monitorFilter; 
	}
	
	
	 // Load games
	  gamesFactory.getGameById($stateParams.id).then(function (game) {
	    $scope.game = game;
	  }, function () {
	    // Show error alert
		  $scope.err = 'msg_generic_error';
	  });
	
	 var enrichData = function(data) {
		 data.forEach(function(p) {
			var badges = p.state['BadgeCollectionConcept'] ? p.state['BadgeCollectionConcept'] : [];
			var score = p.state['PointConcept'] ? p.state['PointConcept'] : [];
			p.totalBadges = 0;
			p.totalScore = 0;
			badges.forEach(function(b) {
				p.totalBadges += b.badgeEarned.length;
			});
			
			score.forEach(function(s) {
				p.totalScore += s.score;
			});
		 });
		 
		 return data;
	 }
	 
	
	 
	gamesFactory.getPlayersState($rootScope.currentGameId,$scope.playerIdFilter,$scope.currentPage, $scope.items4Page).then(function(data){
		data.content = enrichData(data.content);
		$scope.playerStates = data;
		$scope.totalItems = data.totalElements;
	}, function(msg){
		$scope.err = msg;
	});
	
	$scope.expand = false;
	
	$scope.filter = function() {
		$rootScope.monitorFilter = $scope.playerIdFilter;
		gamesFactory.getPlayersState($rootScope.currentGameId,$scope.playerIdFilter,$scope.currentPage, $scope.items4Page).then(function(data){
			data.content = enrichData(data.content);
			$scope.playerStates = data;
			$scope.totalItems = data.totalElements;
		}, function(msg){
			$scope.err = msg;
		});
	}
	$scope.openDetails = function(idx) {
		if(idx != $scope.expandIdx) {
			$scope.expandIdx = idx;
		} else {
			$scope.expandIdx = -1000;
		}
	};
	
	$scope.update = function() {
		$scope.expandIdx = -1000;
		gamesFactory.getPlayersState($rootScope.currentGameId,$scope.playerIdFilter,$scope.currentPage, $scope.items4Page).then(function(data){
			data.content = enrichData(data.content);
			$scope.playerStates = data;
			$scope.totalItems = data.totalElements;
		}, function(msg){
			$scope.err = msg;
		});
	};
	
}