function LoginCtrl($scope) {

}

function HomeCtrl($scope, $modal) {
  $scope.games = games;
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

var games = [
  {
    'name': 'Game 1. Lorem ipsum dolor sit amet.',
    'description': 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fringilla justo sit amet aliquam congue. Ut malesuada ligula enim, ut ultricies tortor tempor bibendum. Donec id tempor eros. Aenean bibendum leo ut ipsum sollicitudin, et faucibus nulla sollicitudin. Pellentesque suscipit egestas varius. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fringilla justo sit amet aliquam congue. Ut malesuada ligula enim, ut ultricies tortor tempor bibendum. Donec id tempor eros. Aenean bibendum leo ut ipsum sollicitudin, et faucibus nulla sollicitudin. Pellentesque suscipit egestas varius. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fringilla justo sit amet aliquam congue. Ut malesuada ligula enim, ut ultricies tortor tempor bibendum. Donec id tempor eros. Aenean bibendum leo ut ipsum sollicitudin, et faucibus nulla sollicitudin. Pellentesque suscipit egestas varius.'
    },
  {
    'name': 'Game 2',
    'description': 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fringilla justo sit amet aliquam congue. Ut malesuada ligula enim, ut ultricies tortor tempor bibendum.'
    },
  {
    'name': 'Game 3',
    'description': 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fringilla justo sit amet aliquam congue. Ut malesuada ligula enim, ut ultricies tortor tempor bibendum. Donec id tempor eros. Aenean bibendum leo ut ipsum sollicitudin, et faucibus nulla sollicitudin. Pellentesque suscipit egestas varius.'
    },
  {
    'name': 'Game 4',
    'description': 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.'
    },
  {
    'name': 'Game 5',
    'description': 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur fringilla justo sit amet aliquam congue. Ut malesuada ligula enim, ut ultricies tortor tempor bibendum. Donec id tempor eros. Aenean bibendum leo ut ipsum sollicitudin, et faucibus nulla sollicitudin. Pellentesque suscipit egestas varius.'
    }
];
