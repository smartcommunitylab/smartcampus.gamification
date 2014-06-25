app.factory('gamesFactory',
  function ($rootScope, $http, $q, $timeout) {

    // get games
    var getGames = function () {
      var deferred = $q.defer();

      if (!$rootScope.games || $rootScope.games.length === 0) {
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
    };

    // get game by id
    var getGameById = function (id) {
      var deferred = $q.defer();

      var game = {};
      getGames().then(function () {
        var found = false;
        angular.forEach($rootScope.games, function (g) {
          if (!found && g.id === id) {
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
    };

    // get game by name
    var getGameByName = function (name) {
      var found = false;
      angular.forEach($rootScope.games, function (g) {
        if (!found && g.name === name) {
          found = true;
        }
      });
      return found;
    };

    // add new game
    var addGame = function (name) {
      var deferred = $q.defer();

      if (!name || getGameByName(name)) {
        deferred.reject();
      } else {
        var id = -1;
        angular.forEach($rootScope.games, function (game) {
          if (game.id > id) {
            id = game.id;
          }
        });

        var game = {
          'id': id + 1,
          'name': name,
          'instances': {
            'points': [],
            'badges_collections': [],
            'leaderboards': []
          }
        };

        $rootScope.games.push(game);
        deferred.resolve(game);
      }

      return deferred.promise;
    }

    return {
      'getGames': getGames,
      'getGameById': getGameById,
      'getGameByName': getGameByName,
      'addGame': addGame
    };
  }
);

app.factory('utilsFactory',
  function () {
    var countActive = function (game, type) {
      var count = 0;
      if (!!game && !!game.instances && !!game.instances[type]) {
        angular.forEach(game.instances[type], function (value) {
          if (value.is_active) {
            count++;
          }
        });
      }
      return count;
    };

    var getLength = function (game, type) {
      var len = 0;
      if (!!game && !!game.instances && !!game.instances[type]) {
        len = game.instances[type].length;
      }
      return len;
    };

    return {
      'getLength': getLength,
      'countActive': countActive
    };
  }
);
