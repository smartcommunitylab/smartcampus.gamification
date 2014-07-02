function EditGameModalInstanceCtrl($scope, $modalInstance, game, gamesFactory) {
  $scope.newGame = {};
  $scope.newGame.name = game.name;

  // Error alerts object
  $scope.alerts = {
    'editGameError': ''
  };

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = '';
  }

  $scope.ok = function () {
    gamesFactory.editGame(game, $scope.newGame.name).then(
      function () {
        $modalInstance.close();
      },
      function (message) {
        // Show error alert
        $scope.alerts.editGameError = message;
      }
    );
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function DeleteGameConfirmModalInstanceCtrl($scope, $modalInstance, $window, game, gamesFactory) {
  $scope.argument = game.name;

  $scope.delete = function () {
    gamesFactory.deleteGame(game).then(function () {
      // Temporary redirect
      $window.location.href = '#/home';

      $modalInstance.close();
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  }
}

function DeleteInstanceConfirmModalInstanceCtrl($scope, $modalInstance, $window, game, instance, instanceType, gamesFactory) {
  $scope.argument = instance.name;

  $scope.delete = function () {
    gamesFactory.deleteInstance(game, instance, instanceType).then(function () {
      // Temporary redirect
      $window.location.href = '#/game/' + game.id;

      $modalInstance.close();
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  }
}

function EditPointsInstanceModalInstanceCtrl($scope, $modalInstance, game, instance, gamesFactory) {
  $scope.points = {};
  $scope.points.name = instance.name;
  $scope.points.typology = instance.typology || 'Skill points';

  // Error alerts object
  $scope.alerts = {
    'editInstanceError': ''
  };

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = '';
  }

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
    gamesFactory.editInstance(game, instance, 'points', $scope.points).then(function () {
      $modalInstance.close();
    }, function (message) {
      // Show error alert
      $scope.alerts.editInstanceError = message;
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function EditBadgesCollectionInstanceModalInstanceCtrl($scope, $modalInstance, game, instance, gamesFactory) {
  $scope.badges_collection = {};
  $scope.badges_collection.name = instance.name;

  // Error alerts object
  $scope.alerts = {
    'editInstanceError': ''
  };

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = '';
  }

  $scope.save = function () {
    gamesFactory.editInstance(game, instance, 'badges_collections', $scope.badges_collection).then(function () {
      $modalInstance.close();
    }, function (message) {
      // Show error alert
      $scope.alerts.editInstanceError = message;
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function EditLeaderboardInstanceModalInstanceCtrl($scope, $modalInstance, game, instance, gamePoints, gamesFactory) {

  $scope.leaderboard = {};
  $scope.leaderboard.name = instance.name;
  $scope.leaderboard.points_dependency = instance.points_dependency || game.instances.points[0].name;
  $scope.leaderboard.update_rate = instance.update_rate || 'Weekly';

  $scope.gamePoints = gamePoints;

  // Error alerts object
  $scope.alerts = {
    'editInstanceError': ''
  };

  $scope.closeAlert = function (alertName) {
    $scope.alerts[alertName] = '';
  }

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
    gamesFactory.editInstance(game, instance, 'leaderboards', $scope.leaderboard).then(function () {
      $modalInstance.close();
    }, function (message) {
      // Show error alert
      $scope.alerts.editInstanceError = message;
    });
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function ActivePointsConfirmModalInstanceCtrl($scope, $modalInstance, points) {
  $scope.argument = points.name;

  $scope.active = function () {
    points.is_active = true;
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function DeactiveLeaderboardsConfirmModalInstanceCtrl($scope, $modalInstance, leaderboards, gamesFactory) {
  $scope.leaderboards = leaderboards;

  $scope.deactive = function () {
    gamesFactory.deactiveLeaderboards(leaderboards);
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

function DeleteLeaderboardsConfirmModalInstanceCtrl($scope, $modalInstance, game, leaderboards, gamesFactory) {
  $scope.leaderboards = leaderboards;

  $scope.delete = function () {
    gamesFactory.deleteLeaderboards(game, leaderboards);
    $modalInstance.close();
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}
