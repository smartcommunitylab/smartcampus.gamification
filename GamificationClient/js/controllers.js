function LoginCtrl($scope, $rootScope) {
  // TODO
  $rootScope.currentNav = 'login';
}

function HomeCtrl($scope, $rootScope, $modal, $window, initFactory) {
  $rootScope.games = null;
  $rootScope.currentNav = 'home';

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
  }, function () {
    alert('Errore nel caricamento dei giochi da file');
  });

  $scope.openGameModal = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/addgamemodal.html',
      controller: AddGameModalInstanceCtrl
    });

    modalInstance.result.then(function (game) {
      //    TODO: Add new game!
      //    Soluzione provvisoria...
      $rootScope.games.push(game);
    });
  };

  $scope.openEditModal = function (id) {

    var gameEdited = getGameById(id, $rootScope.games);

    var modalInstance = $modal.open({
      templateUrl: 'templates/editgamemodal.html',
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
    var count = 0;
    angular.forEach(game.instances[type], function (value) {
      if (value.is_active) {
        count++;
      }
    });
    return count;
  };

  $scope.getLength = function (game, type) {
    return game.instances[type].length;
  };

  $scope.goto = function (path) {
    $window.location.href = path;
  }
}

function AddGameModalInstanceCtrl($scope, $modalInstance) {
  $scope.game = {};

  $scope.ok = function () {
    var game = $scope.game;

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

function GameCtrl($scope, $rootScope, $routeParams, initFactory) {

  initFactory.getGames().then(function (games) {
    $rootScope.games = games;
    $scope.game = getGameById($routeParams.id, $rootScope.games);
  }, function () {
    alert('Errore nel caricamento dei giochi da file');
  });

  $rootScope.currentNav = 'configure'

}
