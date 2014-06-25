function MainCtrl($scope, $rootScope) {
  $rootScope.games = [];
}

function LoginCtrl($scope, $rootScope) {
  $rootScope.currentNav = 'login';
}

function HomeCtrl($scope, $rootScope, $window, $modal, gamesFactory, utilsFactory) {
  $rootScope.currentNav = 'home';
  $rootScope.currentGameId = 1;

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
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_game_add.html',
      controller: AddGameModalInstanceCtrl
    });

    modalInstance.result.then(function (game) {
      game.id = getNewGameId($rootScope.games);
      $rootScope.games.push(game);
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

function AddGameModalInstanceCtrl($scope, $modalInstance) {
  var game = $scope.game = {
    'id': 0,
    'name': '',
    'instances': {
      'points': [],
      'badges_collections': [],
      'leaderboards': []
    }
  };

  $scope.ok = function () {
    if (!!game.name) {
      $modalInstance.close(game);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function EditGameModalInstanceCtrl($scope, $modalInstance, oldGameName) {
  $scope.game = {};
  $scope.game.newGameName = oldGameName;

  $scope.ok = function () {
    var newGameName = $scope.game.newGameName;

    if (newGameName != oldGameName && !!newGameName) {
      $modalInstance.close(newGameName);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function getNewGameId(games) {
  var higher = -1;

  angular.forEach(games, function (game) {
    if (game.id > higher) {
      higher = game.id;
    }
  });

  return higher + 1;
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
    'loadGameError': false
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
        oldGameName: function () {
          return $scope.game.name;
        }
      }
    });

    modalInstance.result.then(function (newGameName) {
      //    TODO: Change game name!
      //    Soluzione provvisoria...
      $scope.game.name = newGameName;
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
      templateUrl: 'templates/modals/modal_points_instance_add.html',
      controller: AddPointsInstanceModalInstanceCtrl
    });

    modalInstance.result.then(function (newPointsInstance) {
      newPointsInstance.points_id = getNewPointsId($scope.game);
      $scope.game.instances.points.push(newPointsInstance);
    });
  };

  $scope.openAddBadgesCollectionsInstanceModal = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_badges_collection_instance_add.html',
      controller: AddBadgesCollectionInstanceModalInstanceCtrl
    });

    modalInstance.result.then(function (newBadgesCollectionInstance) {
      newBadgesCollectionInstance.badges_collection_id = getNewBadgesCollectionId($scope.game);
      $scope.game.instances.badges_collections.push(newBadgesCollectionInstance);
    });
  };

  $scope.openAddLeaderboardsInstanceModal = function () {
    if ($scope.game.instances.points.length == 0) {
      // Show error alert
      $scope.alerts.cantCreateLeaderboards = true;
    } else {
      var modalInstance = $modal.open({
        templateUrl: 'templates/modals/modal_leaderboard_instance_add.html',
        controller: AddLeaderboardInstanceModalInstanceCtrl,
        resolve: {
          gamePoints: function () {
            return $scope.game.instances.points;
          }
        }
      });

      modalInstance.result.then(function (newLeaderboardInstance) {
        newLeaderboardInstance.leaderboard_id = getNewLeaderboardId($scope.game);
        $scope.game.instances.leaderboards.push(newLeaderboardInstance);
      });
    }
  };

  $scope.deleteGame = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteConfirmCtrl,
      resolve: {
        argument: function () {
          return $scope.game.name;
        }
      }
    });

    modalInstance.result.then(function () {
      angular.forEach($rootScope.games, function (game, index) {
        if (game.id == $scope.game.id)
          $rootScope.games.splice(index, 1);
      });

      $window.location.href = '#/home';
    });
  }
}

function DeleteConfirmCtrl($scope, $modalInstance, argument) {
  $scope.argument = argument;

  $scope.delete = function () {
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  }
}

function AddPointsInstanceModalInstanceCtrl($scope, $modalInstance) {
  $scope.newPointsInstance = {
    'points_id': 0,
    'name': '',
    'typology': 'Skill points',
    'is_active': true
  };

  $scope.dropdown = {
    isOpen: false
  };

  $scope.toggleDropdown = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.dropdown.isOpen = !$scope.dropdown.isOpen;

  }

  $scope.setTypology = function (type, $event) {
    $scope.newPointsInstance.typology = type;
    $scope.toggleDropdown($event);
  };



  $scope.save = function () {
    if (!!$scope.newPointsInstance.name) {
      $modalInstance.close($scope.newPointsInstance);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function AddBadgesCollectionInstanceModalInstanceCtrl($scope, $modalInstance) {
  $scope.newBadgesCollectionInstance = {
    'badges_collection_id': 0,
    'name': '',
    'badges': [],
    'is_active': true
  };

  $scope.save = function () {
    if (!!$scope.newBadgesCollectionInstance.name) {
      $modalInstance.close($scope.newBadgesCollectionInstance);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function AddLeaderboardInstanceModalInstanceCtrl($scope, $window, $modalInstance, gamePoints) {
  $scope.newLeaderboardInstance = {
    'leaderboard_id': 0,
    'name': '',
    'points_dependency': gamePoints[0].name,
    'update_rate': 'Daily',
    'is_active': true
  };

  $scope.gamePoints = gamePoints;

  $scope.dropdownPointsDependency = {
    isOpen: false
  };

  $scope.toggleDropdownPointsDependency = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.dropdownPointsDependency.isOpen = !$scope.dropdownPointsDependency.isOpen;

  }

  $scope.setPointsDependency = function (pointsDependency, $event) {
    $scope.newLeaderboardInstance.points_dependency = pointsDependency;
    $scope.toggleDropdownPointsDependency($event);
  };

  $scope.dropdownUpdateRate = {
    isOpen: false
  };

  $scope.toggleDropdownUpdateRate = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.dropdownUpdateRate.isOpen = !$scope.dropdownUpdateRate.isOpen;

  }

  $scope.setUpdateRate = function (updateRate, $event) {
    $scope.newLeaderboardInstance.update_rate = updateRate;
    $scope.toggleDropdownUpdateRate($event);
  };

  $scope.save = function () {
    if (!!$scope.newLeaderboardInstance.name) {
      $modalInstance.close($scope.newLeaderboardInstance);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
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
        oldPointsName: function () {
          return $scope.points.name;
        },
        oldPointsTypology: function () {
          return $scope.points.typology;
        }
      }
    });

    modalInstance.result.then(function (newPointsInstance) {
      $scope.points = newPointsInstance;
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteConfirmCtrl,
      resolve: {
        argument: function () {
          return $scope.points.name;
        }
      }
    });

    modalInstance.result.then(function () {
      angular.forEach($scope.game.instances.points, function (points, index) {
        if (points.id == $scope.points.id)
          $scope.game.instances.points.splice(index, 1);
      });

      $window.location.href = '#/game/' + $scope.game.id;
    });
  };
}

function EditPointsInstanceModalInstanceCtrl($scope, $modalInstance, oldPointsName, oldPointsTypology) {
  $scope.points = {};
  $scope.points.name = oldPointsName;
  $scope.points.typology = oldPointsTypology;

  $scope.dropdown = {
    isOpen: false
  };

  $scope.toggleDropdown = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.dropdown.isOpen = !$scope.dropdown.isOpen;

  }

  $scope.setTypology = function (type, $event) {
    $scope.points.typology = type;
    $scope.toggleDropdown($event);
  };

  $scope.save = function () {
    var newPointsName = $scope.points.name;
    var newPointsTypology = $scope.points.typology;

    if ((newPointsName != oldPointsName || newPointsTypology != oldPointsTypology) && !!newPointsName) {
      $modalInstance.close($scope.points);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
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
        oldBadgesCollectionName: function () {
          return $scope.badges_collection.name;
        }
      }
    });

    modalInstance.result.then(function (newBadgesCollectionInstance) {
      $scope.badges_collection = newBadgesCollectionInstance;
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteConfirmCtrl,
      resolve: {
        argument: function () {
          return $scope.badges_collection.name;
        }
      }
    });

    modalInstance.result.then(function () {
      angular.forEach($scope.game.instances.badges_collections, function (badges_collection, index) {
        if (badges_collection.id == $scope.badges_collection.id)
          $scope.game.instances.badges_collections.splice(index, 1);
      });

      $window.location.href = '#/game/' + $scope.game.id;
    });
  };
}

function EditBadgesCollectionInstanceModalInstanceCtrl($scope, $modalInstance, oldBadgesCollectionName) {
  $scope.badges_collection = {};
  $scope.badges_collection.name = oldBadgesCollectionName;

  $scope.save = function () {
    var newBadgesCollectionName = $scope.badges_collection.name;

    if (newBadgesCollectionName != oldBadgesCollectionName && !!newBadgesCollectionName) {
      $modalInstance.close($scope.badges_collection);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
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
        oldLeaderboardName: function () {
          return $scope.leaderboard.name;
        },
        oldLeaderboardPointsDependency: function () {
          return $scope.leaderboard.points_dependency;
        },
        oldLeaderboardUpdateRate: function () {
          return $scope.leaderboard.update_rate;
        },
        gamePoints: function () {
          return $scope.game.instances.points;
        }
      }
    });

    modalInstance.result.then(function (newLeaderboardInstance) {
      $scope.leaderboard = newLeaderboardInstance;
      $scope.alerts.settingsEdited = true;
    });
  };

  $scope.deleteInstance = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_delete_confirm.html',
      controller: DeleteConfirmCtrl,
      resolve: {
        argument: function () {
          return $scope.leaderboard.name;
        }
      }
    });

    modalInstance.result.then(function () {
      angular.forEach($scope.game.instances.leaderboards, function (leaderboard, index) {
        if (leaderboard.id == $scope.leaderboard.id)
          $scope.game.instances.leaderboards.splice(index, 1);
      });

      $window.location.href = '#/game/' + $scope.game.id;
    });
  };
}

function EditLeaderboardInstanceModalInstanceCtrl($scope, $modalInstance, oldLeaderboardName, oldLeaderboardPointsDependency, oldLeaderboardUpdateRate, gamePoints) {

  $scope.leaderboard = {};
  $scope.leaderboard.name = oldLeaderboardName;
  $scope.leaderboard.points_dependency = oldLeaderboardPointsDependency;
  $scope.leaderboard.update_rate = oldLeaderboardUpdateRate;

  $scope.gamePoints = gamePoints;

  $scope.dropdownPointsDependency = {
    isOpen: false
  };

  $scope.toggleDropdownPointsDependency = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.dropdownPointsDependency.isOpen = !$scope.dropdownPointsDependency.isOpen;

  }

  $scope.setPointsDependency = function (pointsDependency, $event) {
    $scope.leaderboard.points_dependency = pointsDependency;
    $scope.toggleDropdownPointsDependency($event);
  };

  $scope.dropdownUpdateRate = {
    isOpen: false
  };

  $scope.toggleDropdownUpdateRate = function ($event) {
    $event.preventDefault();
    $event.stopPropagation();
    $scope.dropdownUpdateRate.isOpen = !$scope.dropdownUpdateRate.isOpen;

  }

  $scope.setUpdateRate = function (updateRate, $event) {
    $scope.leaderboard.update_rate = updateRate;
    $scope.toggleDropdownUpdateRate($event);
  };

  $scope.save = function () {
    var newLeaderboardName = $scope.leaderboard.name;
    var newLeaderboardPointsDependency = $scope.leaderboard.points_dependency;
    var newLeaderboardUpdateRate = $scope.leaderboard.update_rate;

    if ((newLeaderboardName != oldLeaderboardName || newLeaderboardPointsDependency != oldLeaderboardPointsDependency || newLeaderboardUpdateRate != oldLeaderboardUpdateRate) && !!newLeaderboardName) {
      $modalInstance.close($scope.leaderboard);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
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
