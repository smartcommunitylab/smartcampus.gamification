function LoginCtrl($scope) {
  // TODO
}

function HomeCtrl($scope, $modal, $window, initFactory) {
  $scope.games = null;

  initFactory.getGames().then(function (games) {
    $scope.games = games;
  }, function () {
    alert('errore');
  });

  $scope.openGameModal = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/addgamemodal.html',
      controller: AddGameModalInstanceCtrl
    });

    modalInstance.result.then(function (game) {
      //    TODO: Add new game!
      //    Soluzione provvisoria...
      $scope.games.push(game);
    });
  };

  $scope.openEditModal = function (id) {

    var gameEdited;

    angular.forEach($scope.games, function (game) {
      if (game.id == id) {
        gameEdited = game;
      }
    });

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

  $scope.goto = function(path) {
    $window.location.href = path;
  }
}

function AddGameModalInstanceCtrl($scope, $modalInstance) {
  $scope.game = {};

  $scope.ok = function () {
    var game = $scope.game;

    if (game.name != null) {
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

    if (newGameName != oldGameName && newGameName != null) {
      $modalInstance.close(newGameName);
    }
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function GameCtrl($scope, $routeParams) {
  $scope.game = {};
  $scope.game.id = $routeParams.id;
}
