function MainCtrl($scope, $rootScope) {
  $rootScope.games = [];
}

function LoginCtrl($scope, $rootScope) {
  $rootScope.currentNav = 'login';
}

function HomeCtrl($scope, $rootScope, $window, $modal, gamesFactory, utilsFactory) {
  $rootScope.currentNav = 'home';
  $rootScope.currentGameId = 1;

  // LOADS GAMES
  gamesFactory.getGames().then();

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false
  };

  gamesFactory.getGames().then(function () {}, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  }

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

function getNewPointsId(game) {
  var higher = -1;

  angular.forEach(game.instances.points, function (points) {
    if (points.id > higher) {
      higher = points.id;
    }
  });

  return higher + 1;
}

function getNewBadgesCollectionId(game) {
  var higher = -1;

  angular.forEach(game.instances.badges_collections, function (badges_collection) {
    if (badges_collection.id > higher) {
      higher = badges_collection.id;
    }
  });

  return higher + 1;
}

function getNewLeaderboardId(game) {
  var higher = -1;

  angular.forEach(game.instances.leaderboards, function (leaderboard) {
    if (leaderboard.id > higher) {
      higher = leaderboard.id;
    }
  });

  return higher + 1;
}

function GameCtrl($scope, $rootScope, $window, $routeParams, $modal, gamesFactory, utilsFactory) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $routeParams.id;

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

  $scope.game = {};

  gamesFactory.getGameById($routeParams.id).then(function (game) {
    $scope.game = game;
  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  }

  $scope.setSelectedInstance = function (type) {
    $scope.selectedInstance = type;
  }

  $scope.countActive = function (game, type) {
    return utilsFactory.countActive(game, type);
  };

  $scope.getLength = function (game, type) {
    return utilsFactory.getLength(game, type);
  };

  $scope.goto = function (path) {
    $window.location.href = path;
  };

  $scope.openEditModal = function (id) {
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
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.openAddInstanceModal = function () {
    switch ($scope.selectedInstance) {
    case 'points':
      $scope.openAddPointsInstanceModal();
      break;
    case 'badges_collections':
      $scope.openAddBadgesCollectionsInstanceModal();
      break;
    case 'leaderboards':
      $scope.openAddLeaderboardsInstanceModal();
      break;
    }
  };

  $scope.openAddPointsInstanceModal = function () {
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

  $scope.openAddLeaderboardsInstanceModal = function () {
    if ($scope.game.instances.points.length == 0) {
      // Show error alert
      $scope.alerts.cantCreateLeaderboards = true;
    } else {
      var modalInstance = $modal.open({
        templateUrl: 'templates/modals/modal_leaderboard_instance_edit.html',
        controller: EditLeaderboardInstanceModalInstanceCtrl,
        resolve: {
          game: function () {
            return $scope.game;
          },
          instance: function () {
            return {};
          },
          gamePoints: function () {
            return $scope.game.instances.points;
          }
        }
      });
    }
  };

  $scope.deleteGame = function () {
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
    if (!points.is_active) {
      var leaderboards = gamesFactory.pointsDeactivationCheck($scope.game, points);

      if (leaderboards.length != 0) {
        var modalInstance = $modal.open({
          templateUrl: 'templates/modals/modal_deactive_leaderboards_confirm.html',
          controller: DeactiveLeaderboardsConfirmModalInstanceCtrl,
          resolve: {
            leaderboards: function () {
              return leaderboards;
            }
          }
        });

        modalInstance.result.then(function () {}, function () {
          points.is_active = true;
        });
      }
    }
  };

  $scope.leaderboardActivationCheck = function (leaderboard) {
    if (leaderboard.is_active) {
      gamesFactory.leaderboardActivationCheck($scope.game, leaderboard).then(function () {}, function (points) {
        var modalInstance = $modal.open({
          templateUrl: 'templates/modals/modal_active_points_confirm.html',
          controller: ActivePointsConfirmModalInstanceCtrl,
          resolve: {
            points: function () {
              return points;
            }
          }
        });

        modalInstance.result.then(function () {}, function () {
          leaderboard.is_active = false;
        });
      });
    }
  };
}

function GamePointsCtrl($scope, $rootScope, $routeParams, $modal, $window, gamesFactory) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $routeParams.id;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false,
    'settingsEdited': false
  };

  // Backup variables for settings editing
  $scope.editPoints = {
    'name': '',
    'typology': ''
  };

  $scope.activeTab = 'edit';
  $scope.tabActive = {
    'edit': true,
    'rules': false
  };

  gamesFactory.getInstanceById($routeParams.id, 'points', $routeParams.idPoints).then(function (response) {
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
      // Show success alert
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
    var leaderboards = gamesFactory.pointsDeactivationCheck($scope.game, $scope.points);

    if (leaderboards.length != 0) {
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
}

function GameBadgesCollectionCtrl($scope, $rootScope, $routeParams, $modal, $window, gamesFactory) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $routeParams.id;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false,
    'settingsEdited': false
  };

  gamesFactory.getInstanceById($routeParams.id, 'badges_collections', $routeParams.idBadgesCollection).then(function (response) {
    $scope.game = response.game;
    $scope.badges_collection = response.inst;
  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  };

  $scope.openEditInstanceModal = function () {
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
      // Show success alert
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
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
}

function GameLeaderboardCtrl($scope, $rootScope, $routeParams, $modal, $window, gamesFactory) {
  $rootScope.currentNav = 'configure';
  $rootScope.currentGameId = $routeParams.id;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false,
    'settingsEdited': false
  };

  gamesFactory.getInstanceById($routeParams.id, 'leaderboards', $routeParams.idLeaderboard).then(function (response) {
    $scope.game = response.game;
    $scope.leaderboard = response.inst;
  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  };

  $scope.openEditInstanceModal = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_leaderboard_instance_edit.html',
      controller: EditLeaderboardInstanceModalInstanceCtrl,
      resolve: {
        game: function () {
          return $scope.game;
        },
        instance: function () {
          return $scope.leaderboard;
        },
        gamePoints: function () {
          return $scope.game.instances.points;
        }
      }
    });

    modalInstance.result.then(function () {
      // Show success alert
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteInstanceConfirmModalInstanceCtrl,
      resolve: {
        game: function () {
          return $scope.game;
        },
        instance: function () {
          return $scope.leaderboard;
        },
        instanceType: function () {
          return 'leaderboards';
        }
      }
    });
  };
}

function ActionsCtrl($scope, $rootScope, $routeParams, gamesFactory) {
  $rootScope.currentNav = 'actions';
  $rootScope.currentGameId = $routeParams.id;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false
  };

  gamesFactory.getGameById($routeParams.id).then(function (game) {
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
