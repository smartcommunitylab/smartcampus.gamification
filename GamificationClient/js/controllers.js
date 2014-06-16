function LoginCtrl($scope) {

}

function HomeCtrl($scope, $modal) {
  $scope.openAddGameModal = function () {

    var modalInstance = $modal.open({
      templateUrl: 'templates/addGameModal.html',
      controller: AddGameModalInstanceCtrl,
    });

    modalInstance.result.then(function (game) {
      /* TODO: Add new game! */
      $scope.game = game;
    });
  };
}

function AddGameModalInstanceCtrl($scope, $modalInstance) {
  $scope.ok = function () {
    $modalInstance.close($scope.game);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
}
