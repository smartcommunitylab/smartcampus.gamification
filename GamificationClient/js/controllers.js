function LoginCtrl($scope) {
  // TODO
}

function HomeCtrl($scope, $modal, initFactory) {
  $scope.games = null;

  initFactory.getGames().then(function (games) {
    $scope.games = games;
  }, function () {
    alert('errore');
  });

  $scope.openGameModal = function () {
    var modalInstance = $modal.open({
      templateUrl: 'templates/gamemodal.html',
      controller: GameModalInstanceCtrl
    });

    modalInstance.result.then(function (gameName) {
      /* TODO: Add new game! */
      $scope.games.push(gameName);
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

  $scope.getLength = function(game, type) {
    return game.instances[type].length;
  }
}

function GameModalInstanceCtrl($scope, $modalInstance) {
  $scope.game = {};

  $scope.ok = function () {
    var gameName = $scope.gameName;

    //    if (gameName != "" && gameName != undefined)
    $modalInstance.close(gameName);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}
