app.factory('gamesFactory',
  function ($rootScope, $http, $q, $timeout) {
    var getGames = function () {
      var deferred = $q.defer();

      if (!$rootScope.games || $rootScope.games.length == 0) {
        $http.get('data/games.json').success(function (data) {
          $rootScope.games = data;
          deferred.resolve();
        }).error(function () {
          deferred.reject();
        });
      } else {
        deferred.resolve();
      }
      return deferred.promise;
    }

    var getGameById = function (id) {
      var deferred = $q.defer();

      var game = {};
      getGames().then(function () {
        var found = false;
        angular.forEach($rootScope.games, function (g) {
          if (!found && g.id == id) {
            game = g;
            found = true;
          }
        });

        if (!!game) {
          deferred.resolve(game);
        } else {
          deferred.reject();
        }
      });

      return deferred.promise;
    }

    return {
      'getGames': getGames,
      'getGameById': getGameById
    };
  }
);

app.factory('utilsFactory',
  function () {
    var countActive = function (game, type) {
      var count = 0;
      if (!!game && !!game.instances) {
        angular.forEach(game.instances[type], function (value) {
          if (value.is_active) {
            count++;
          }
        });
      }
      return count;
    }

    var getLength = function (game, type) {
      var len = 0;
      if (!!game && !!game.instances) {
        len = game.instances[type].length;
      }
      return len;
    }

    return {
      'getLength': getLength,
      'countActive': countActive
    };
  }
);
