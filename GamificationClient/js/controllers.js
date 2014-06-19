function LoginCtrl($scope, $rootScope) {
  // TODO
  $rootScope.currentNav = 'login';
}

function HomeCtrl($scope, $rootScope, $modal, $window, initFactory) {
  $rootScope.games = null;
  $rootScope.currentNav = 'home';
  $rootScope.currentGameId = 1;

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
  }, function () {
    alert('Errore nel caricamento dei giochi da file');
  });

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
  }
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

function getNewGameId(games) {
  var higher = -1;
  angular.forEach(games, function (game) {
    if (game.id > higher)
      higher = game.id;
  });

  return higher + 1;
}

function getNewPointsId(game) {
  var higher = -1;
  angular.forEach(game.instances.points, function (points) {
    if (points.id > higher)
      higher = points.id;
  });

  return higher + 1;
}

function GameCtrl($scope, $rootScope, $routeParams, $modal, initFactory) {

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
    $scope.game = getGameById($routeParams.id, $rootScope.games);
  }, function () {
    alert('Errore nel caricamento dei giochi da file');
  });

  $rootScope.currentNav = 'configure'
  $rootScope.currentGameId = $routeParams.id;

  $scope.setSelectedInstance = function (type) {
    $scope.selectedInstance = type;
  }

  $scope.countActive = function (game, type) {
    return countActive(game, type);
  };

  $scope.getLength = function (game, type) {
    return getLength(game, type);
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
      newPointsInstance.id = getNewPointsId($scope.game);
      $scope.game.instances.points.push(newPointsInstance);
    });
  };

  $scope.openAddBadgesCollectionsInstanceModal = function () {
    /* TODO */
  };

  $scope.openAddLeaderboardsInstanceModal = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/modals/modal_points_instance_add.html',
      controller: AddPointsInstanceModalInstanceCtrl
    });

    modalInstance.result.then(function (newPointsInstance) {
      newPointsInstance.id = getNewPointsId($scope.game);
      $scope.game.instances.points.push(newPointsInstance);
    });
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

function ActionsCtrl($scope, $rootScope, $routeParams, initFactory) {

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
    $scope.game = getGameById($routeParams.id, $rootScope.games);
  }, function () {
    alert('Errore nel caricamento dei giochi da file');
  });

  $rootScope.currentNav = 'actions';
  $rootScope.currentGameId = $routeParams.id;

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
