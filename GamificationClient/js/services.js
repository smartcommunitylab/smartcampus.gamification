app.factory('initFactory',
  function ($http, $q, $timeout) {

    var getGames = function () {
      var deferred = $q.defer();

      $http.get('data/games.json').success(function (data) {
        deferred.resolve(data);
      }).error(function () {
        deferred.reject();
      });

      return deferred.promise;
    }

    return {
      'getGames': getGames
    };
  }
);
