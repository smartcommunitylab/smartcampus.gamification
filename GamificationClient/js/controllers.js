function LoginCtrl($scope, $rootScope) {
  // TODO
  $rootScope.currentNav = 'login';
}

function HomeCtrl($scope, $rootScope, $window, $modal, initFactory) {
  $rootScope.games = null;
  $rootScope.currentNav = 'home';
  $rootScope.currentGameId = 1;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false
  };

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
  }, function () {
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
      //    TODO: Add new game!
      //    Soluzione provvisoria...
      game.id = getNewGameId($rootScope.games);
      $rootScope.games.push(game);
    });
  };

  $scope.openEditModal = function (id) {

    var gameEdited = getGameById(id, $rootScope.games);

    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_game_edit.html',
      controller: EditGameModalInstanceCtrl,
      resolve: {
        oldGameName: function () {
          return gameEdited.name;
        }
      }
    });

    modalInstance.result.then(function (newGameName) {
      //    TODO: Change game name!
      //    Soluzione provvisoria...
      gameEdited.name = newGameName;
    });
  };

  $scope.countActive = function (game, type) {
    return countActive(game, type);
  };

  $scope.getLength = function (game, type) {
    return getLength(game, type);
  };

  $scope.goto = function (path) {
    $window.location.href = path;
  };
}

function countActive(game, type) {
  var count = 0;
  if (!!game) {
    angular.forEach(game.instances[type], function (value) {
      if (value.is_active) {
        count++;
      }
    });
  }
  return count;
}

function getLength(game, type) {
  var len = 0;

  if (!!game) {
    len = game.instances[type].length;
  }

  return len;
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

function getGameById(id, games) {
  var found = false;
  var obj = null;

  angular.forEach(games, function (game) {
    if (!found) {
      if (game.id == id) {
        obj = game;
        found = true;
      }
    }
  });

  return obj;
}

function getPointsById(id, game) {
  var found = false;
  var obj = null;

  angular.forEach(game.instances.points, function (points) {
    if (!found) {
      if (points.points_id == id) {
        obj = points;
        found = true;
      }
    }
  });

  return obj;
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
    if (points.points_id > higher) {
      higher = points.points_id;
    }
  });

  return higher + 1;
}

function getNewBadgesCollectionId(game) {
  var higher = -1;

  angular.forEach(game.instances.badges_collections, function (badges_collection) {
    if (badges_collection.badges_collection_id > higher) {
      higher = badges_collection.badges_collection_id;
    }
  });

  return higher + 1;
}

function getNewLeaderboardId(game) {
  var higher = -1;

  angular.forEach(game.instances.leaderboards, function (leaderboard) {
    if (leaderboard.leaderboard_id > higher) {
      higher = leaderboard.leaderboard_id;
    }
  });

  return higher + 1;
}

function GameCtrl($scope, $rootScope, $window, $routeParams, $modal, initFactory) {
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


  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
    $scope.game = getGameById($routeParams.id, $rootScope.games);
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
    return countActive(game, type);
  };

  $scope.getLength = function (game, type) {
    return getLength(game, type);
  };

  $scope.goto = function (path) {
    $window.location.href = path;
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

function GamePointsCtrl($scope, $rootScope, $routeParams, initFactory) {
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

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;

    $scope.game = getGameById($routeParams.id, games);
    $scope.points = getPointsById($routeParams.idPoints, $scope.game);

    $scope.editPoints.name = $scope.points.name;
    $scope.editPoints.typology = $scope.points.typology;
  }, function () {
    // Show error alert
    $scope.alerts.loadGameError = true;
  });

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = false;
  };

  $scope.changeTab = function (tab) {
    angular.forEach($scope.tabs, function (tab) {
      tab = false;
    });

    $scope.tabActive[tab] = true;
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
    $scope.editPoints.typology = type;
    $scope.toggleDropdown($event);
  };

  $scope.save = function () {
    if (!!$scope.editPoints.name) {
      $scope.points.name = $scope.editPoints.name;
      $scope.alerts.settingsEdited = true;
    }
  }
}

function ActionsCtrl($scope, $rootScope, $routeParams, initFactory) {
  $rootScope.currentNav = 'actions';
  $rootScope.currentGameId = $routeParams.id;

  // Error alerts object
  $scope.alerts = {
    'loadGameError': false
  };

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
    $scope.game = getGameById($routeParams.id, $rootScope.games);
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
