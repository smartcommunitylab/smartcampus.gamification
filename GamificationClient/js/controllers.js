function LoginCtrl($scope) {

}

function HomeCtrl($scope, $modal) {
  $scope.games = games;
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
}

function GameModalInstanceCtrl($scope, $modalInstance) {
  $scope.game = {};

  $scope.ok = function () {
    var gameName = $scope.gameName;

    if (gameName != "" && gameName != undefined)
      $modalInstance.close(gameName);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}

var games = [
  {
    'name': 'Game 1. Lorem ipsum dolor sit amet.',
    'points': 'Points: 1 active out of 3.',
    'badges': 'Badges collections: 3 active out of 7.',
    'leaderboards': 'Leaderboards: 0 active out of 2.'
    },
  {
    'name': 'Game 2',
    'points': 'Points: 1 active out of 3.',
    'badges': 'Badges collections: 3 active out of 7.',
    'leaderboards': 'Leaderboards: 0 active out of 2.'
    },
  {
    'name': 'Game 3',
    'points': 'Points: 1 active out of 3.',
    'badges': 'Badges collections: 3 active out of 7.',
    'leaderboards': 'Leaderboards: 0 active out of 2.'
    },
  {
    'name': 'Game 4',
    'points': 'Points: 1 active out of 3.',
    'badges': 'Badges collections: 3 active out of 7.',
    'leaderboards': 'Leaderboards: 0 active out of 2.'
    },
  {
    'name': 'Game 5',
    'points': 'Points: 1 active out of 3.',
    'badges': 'Badges collections: 3 active out of 7.',
    'leaderboards': 'Leaderboards: 0 active out of 2.'
    }
];
