function LoginCtrl($scope) {

}

function HomeCtrl($scope, $modal) {
  $scope.openGameModal = function () {

    var modalInstance = $modal.open({
      templateUrl: 'templates/gamemodal.html',
      controller: GameModalInstanceCtrl
    });

    modalInstance.result.then(function (game) {
      /* TODO: Add new game! */
    });
  };
}

function GameModalInstanceCtrl($scope, $modalInstance) {
  $scope.game = {};

  $scope.ok = function () {
    $modalInstance.close($scope.game);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}
