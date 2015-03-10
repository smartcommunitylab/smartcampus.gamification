// Main controller (index.html)
function MainCtrl($scope, $rootScope) {
  $rootScope.games = [];
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
  gamesFactory.getGames().then(function () {}, function () {
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
      resolve: {
        game: function () {
          return {};
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
function GameCtrl($scope, $rootScope, $window, $stateParams, $modal, gamesFactory, utilsFactory) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $stateParams.id;

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
    'leaderboards': 'leaderboards'
  };

  // Tab switching
  $scope.active = {
    'points': false,
    'badges_collections': false,
    'leaderboards': false
  };

  // Read the tab param to select the right tab. If it isn't given, choose the dafualt tab
  var tab = $stateParams.tab;

  if (!!tab && (tab == 'points' || tab == 'badges_collections' || tab == 'leaderboards')) {
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
    $window.location.href = path;
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

  $scope.deleteGame = function () {
    // Delete a game
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteGameConfirmModalInstanceCtrl,
      resolve: {
        game: function () {
          return $scope.game;
        }
      }
    });
  };

  $scope.pointsDeactivationCheck = function (points) {
    // Before points deactivation, tell the user that linked leaderboard will be deactivated too
    if (points.is_active) {
      // Check for linked leaderboards
      var leaderboards = gamesFactory.pointsDeactivationCheck($scope.game, points);

      if (leaderboards.length != 0) {
        // There are some linked leaderboards
        var modalInstance = $modal.open({
          templateUrl: 'templates/modals/modal_deactivate_leaderboards_confirm.html',
          controller: DeactivateLeaderboardsConfirmModalInstanceCtrl,
          resolve: {
            leaderboards: function () {
              return leaderboards;
            }
          }
        });

        modalInstance.result.then(function () {
          points.is_active = !points.is_active;
        });
      } else {
        // There are no linked leaderboards
        points.is_active = !points.is_active;
      }
    } else {
      points.is_active = !points.is_active;
    }
  };

  $scope.leaderboardActivationCheck = function (leaderboard) {
    // Before leaderboard activation, check its dependency. Points dependency MUST be active too
    if (!leaderboard.is_active) {
      gamesFactory.leaderboardActivationCheck($scope.game, leaderboard).then(function () {
        // Points dependency is alredy active
        leaderboard.is_active = !leaderboard.is_active;
      }, function (points) {
        // Ask for dependency activation
        var modalInstance = $modal.open({
          templateUrl: 'templates/modals/modal_activate_points_confirm.html',
          controller: ActivatePointsConfirmModalInstanceCtrl,
          resolve: {
            points: function () {
              return points;
            }
          }
        });

        modalInstance.result.then(function () {
          leaderboard.is_active = !leaderboard.is_active;
        });
      });
    } else {
      leaderboard.is_active = !leaderboard.is_active;
    }
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
    });
  };

  // Add rule
  $scope.openAddRuleModal = function () {
    // TODO: adjust to pass parameters
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_rule_edit.html',
      controller: EditRuleModalInstanceCtrl,
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
      controller: EditBadgesModalInstanceCtrl
    });
  };

  $scope.deleteRule = function () {
    // Delete rule
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteRuleConfirmModalInstanceCtrl,
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
    });
  };

  // Add rule
  $scope.openAddRuleModal = function () {
    // TODO: adjust to pass parameters
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_rule_edit.html',
      controller: EditRuleModalInstanceCtrl,
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
