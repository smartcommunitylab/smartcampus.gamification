app.factory('gamesFactory',
  function ($rootScope, $http, $q, $timeout) {

    // Get games
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

    // Get game by id
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
      }, function () {
        deferred.reject();
      });

      return deferred.promise;
    };

    // Get game by name
    var getGameByName = function (name) {
      var found = false;
      angular.forEach($rootScope.games, function (g) {
        if (!found && g.name === name) {
          found = true;
        }
      });
      return found;
    };

    // Gets an instance (points / basdges_collection / leaderboard) by its id
    var getInstanceById = function (gameId, instanceType, instanceId) {
      var deferred = $q.defer();

      var inst = {};
      getGameById(gameId).then(function (game) {
        var found = false;
        angular.forEach(game.instances[instanceType], function (i) {
          if (!found && i.id == instanceId) {
            inst = i;
            found = true;
          }
        });

        if (!!inst) {
          deferred.resolve({
            'game': game,
            'inst': inst
          });
        } else {
          deferred.reject();
        }

      }, function () {
        deferred.reject();
      });

      return deferred.promise;
    };

    var existsInstanceByName = function (game, instanceName, instanceType) {
      var found = false;
      angular.forEach(game.instances[instanceType], function (i) {
        if (!found && i.name === instanceName) {
          found = true;
        }
      });
      return found;
    };

    var getInstanceByName = function (game, instanceName, instanceType) {
      var found = false;
      var obj = {};
      angular.forEach(game.instances[instanceType], function (i) {
        if (!found && i.name === instanceName) {
          found = true;
          obj = i;
        }
      });
      return obj;
    };

    // Add or edit game
    var editGame = function (game, name) {
      var deferred = $q.defer();

      if (!name) {
        deferred.reject('msg_game_name_error');
      } else if (!game.id) {
        // New game
        if (!!getGameByName(name)) {
          // Game with same name alredy exists
          deferred.reject('msg_game_name_exists_error');
        } else {
          // Create new game
          var id = -1;
          angular.forEach($rootScope.games, function (g) {
            if (g.id > id) {
              id = g.id;
            }
          });

          game = {
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
      } else if (!!getGameByName(name)) {
        // User has entered the same name
        deferred.reject('msg_same_name_error');
      } else {
        // Edit game
        game.name = name;
        deferred.resolve();
      }

      return deferred.promise;
    };

    // Add or edit instance
    var editInstance = function (game, instance, instanceType, instanceProperties) {
      var deferred = $q.defer();

      if (!instanceProperties.name) {
        deferred.reject('msg_instance_name_error');
      } else if (!instance.id) {
        // New instance
        if (!!existsInstanceByName(game, instanceProperties.name, instanceType)) {
          // Instance with same name alredy exists
          deferred.reject('msg_instance_name_exists_error');
        } else {
          // Create new instance
          var id = -1;
          angular.forEach(game.instances[instanceType], function (i) {
            if (i.id > id) {
              id = i.id;
            }
          });

          // Choose instance object structure
          if (instanceType == 'points') {
            instance = {
              'id': id + 1,
              'name': instanceProperties.name,
              'typology': instanceProperties.typology,
              'is_active': true
            };
          } else if (instanceType == 'badges_collections') {
            instance = {
              'id': id + 1,
              'name': instanceProperties.name,
              'badges': [],
              'is_active': true
            };
          } else {
            // instanceType = 'leaderboards'
            instance = {
              'id': id + 1,
              'name': instanceProperties.name,
              'points_dependency': instanceProperties.points_dependency,
              'update_rate': instanceProperties.update_rate,
              'is_active': true
            };
          }

          game.instances[instanceType].push(instance);
          deferred.resolve(instance);
        }
      } else if (!!existsInstanceByName(game, instanceProperties.name, instanceType) && instance.name != instanceProperties.name) {
        // Instance with same name alredy exists
        deferred.reject('msg_instance_name_exists_error');
      } else {
        // Edit instance

        // Choose other instance properties to be modified
        if (instanceType == 'points') {
          if (instance.name == instanceProperties.name && instance.typology == instanceProperties.typology) {
            deferred.reject('msg_instance_unchanged_error');
          }

          instance.name = instanceProperties.name;
          instance.typology = instanceProperties.typology;
        } else if (instanceType == 'badges_cellections') {
          if (instance.name == instanceProperties.name) {
            deferred.reject('msg_instance_unchanged_error');
          }

          instance.name = instanceProperties.name;
        } else {
          // instanceType = 'leaderboards'
          if (instance.name == instanceProperties.name && instance.points_dependency == instanceProperties.points_dependency && instance.update_rate == instanceProperties.update_rate) {
            deferred.reject('msg_instance_unchanged_error');
          }

          instance.name = instanceProperties.name;
          instance.points_dependency = instanceProperties.points_dependency;
          instance.update_rate = instanceProperties.update_rate;
        }

        deferred.resolve();
      }

      return deferred.promise;
    };

    // Delete game
    var deleteGame = function (game) {
      var deferred = $q.defer();

      angular.forEach($rootScope.games, function (g, index) {
        if (g.id == game.id) {
          $rootScope.games.splice(index, 1);
          deferred.resolve();
        }
      });

      return deferred.promise;
    };

    // Delete instance
    var deleteInstance = function (game, instance, instanceType) {
      var deferred = $q.defer();

      angular.forEach(game.instances[instanceType], function (i, index) {
        if (i.id == instance.id) {
          game.instances[instanceType].splice(index, 1);
          deferred.resolve();
        }
      });

      return deferred.promise;
    };

    var leaderboardActivationCheck = function (game, leaderboard) {
      var deferred = $q.defer();
      var points = getInstanceByName(game, leaderboard.points_dependency, 'points');

      if (points.is_active) {
        deferred.resolve();
      } else {
        deferred.reject(points);
      }

      return deferred.promise;
    };

    var pointsDeactivationCheck = function (game, points) {
      var leaderboards = [];
      angular.forEach(game.instances.leaderboards, function (leaderboard) {
        if (leaderboard.points_dependency == points.name && leaderboard.is_active) {
          leaderboards.push(leaderboard);
        }
      });

      return leaderboards;
    };

    var pointsDeleteCheck = function (game, points) {
      var leaderboards = [];
      angular.forEach(game.instances.leaderboards, function (leaderboard) {
        if (leaderboard.points_dependency == points.name) {
          leaderboards.push(leaderboard);
        }
      });

      return leaderboards;
    };

    var deactiveLeaderboards = function (leaderboards) {
      angular.forEach(leaderboards, function (leaderboard) {
        leaderboard.is_active = false;
      });
    };

    var deleteLeaderboards = function (game, leaderboards) {
      // Using pure JS 'for' instead of 'angular.forEach' due to index trouble in splice operations
      for (var i = 0; i < game.instances.leaderboards.length; i++) {
        for (var j = 0; j < leaderboards.length; j++) {
          if (game.instances.leaderboards[i].id == leaderboards[j].id)
            game.instances.leaderboards.splice(i, 1);
        }
      }
    };

    return {
      'getGames': getGames,
      'getGameById': getGameById,
      'getInstanceById': getInstanceById,
      'editGame': editGame,
      'editInstance': editInstance,
      'deleteGame': deleteGame,
      'deleteInstance': deleteInstance,
      'leaderboardActivationCheck': leaderboardActivationCheck,
      'pointsDeactivationCheck': pointsDeactivationCheck,
      'pointsDeleteCheck': pointsDeleteCheck,
      'deactiveLeaderboards': deactiveLeaderboards,
      'deleteLeaderboards': deleteLeaderboards
    };
  });

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
