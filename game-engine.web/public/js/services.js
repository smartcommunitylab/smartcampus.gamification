app.factory('gamesFactory',
  function ($rootScope, $http, $q, $timeout) {
    // Games data operations factory

    // Get games
    var getGames = function () {
      var deferred = $q.defer();

      // If games haven't been already loaded
      if (!$rootScope.games || $rootScope.games.length === 0) {
        // Load games
        $http.get('/console/game').success(function (data) {
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

    // Get game by ID
    var getGameById = function (id) {
      var deferred = $q.defer();

      var game = {};

      // Load games
      getGames().then(function () {
        var found = false;
        angular.forEach($rootScope.games, function (g) {
          if (!found && g.id == id) {
            game = g;
            found = true;
          }
        });

        // If i've found the requested game
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

    // Get an instance (points / basdges_collection / leaderboard) by its ID
    var getInstanceById = function (gameId, instanceType, instanceId) {
      var deferred = $q.defer();

      var inst = {};

      // Load game
      getGameById(gameId).then(function (game) {
        var found = false;
        if(game.concepts) {
        angular.forEach(game.concepts, function (i) {
          if (!found && i.id == instanceId) {
            inst = i;
            found = true;
          }
        });
        }
        // If i've found the requested instance
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

    // Boolean. Returns whether exists or not an instance by its name
    var existsInstanceByName = function (game, instanceName, instanceType) {
      var found = false;
      var a = [];
      if(instanceType === 'points') {
    		  a = game.pointConcept;
      }
      
      if(instanceType === 'badge_collections') {
    		  a = game.badgeCollectionConcept;
      }
      angular.forEach(a, function (i) {
        if (!found && i.name === instanceName) {
          found = true;
        }
      });
      return found;
    };

    // Get an instance (points / basdges_collection / leaderboard) by its name
    var getInstanceByName = function (game, instanceName, instanceType) {
      var found = false;
      var obj = {};
      if(game.concepts) {
      angular.forEach(game.concepts, function (i) {
        if (!found && i.name === instanceName) {
          found = true;
          obj = i;
        }
      });
      return obj;
    }
    };

    
    var getPoints = function(gameId) {
    	var deferred = $q.defer();
    
    	$http.get('console/game/'+gameId+"/point").
    	success(function(data, status, headers, config) {
    		deferred.resolve(data);
        }).
        error(function(data, status, headers, config) {
        	deferred.reject();
        });
        
        return deferred.promise;
    }
    
    var addPoint = function(game,pc) {
    	$http.post('console/game/'+game.id+"/point", pc).
    	success(function(data, status, headers, config) {
    		
        }).
        error(function(data, status, headers, config) {
        });
    };
    
    var getBadges = function(gameId) {
    	var deferred = $q.defer();
    
    	$http.get('console/game/'+gameId+"/badgecoll").
    	success(function(data, status, headers, config) {
    		deferred.resolve(data);
        }).
        error(function(data, status, headers, config) {
        	deferred.reject();
        });
        
        return deferred.promise;
    }
    
    var addBadge = function(game,badge) {
    	$http.post('console/game/'+game.id+"/badgecoll", badge).
    	success(function(data, status, headers, config) {
    		
        }).
        error(function(data, status, headers, config) {
        	
        });
    };
    
    var saveGame = function(game) {
    	var deferred = $q.defer();
    	
    	$http.post('console/game', game).
    	success(function(data, status, headers, config) {
    		deferred.resolve(data);
        }).
        error(function(data, status, headers, config) {
        	deferred.resolve('Error');
        });
    	
    	 return deferred.promise;
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
        game = {};
        game.name = name;

        $http.post('console/game', game).
        success(function(data, status, headers, config) {
        	  $rootScope.games.push(data);
              deferred.resolve(data);
        }).
        error(function(data, status, headers, config) {
        	 deferred.reject('msg_game_name_error');
        });
        
      
        }
      } else if (!!getGameByName(name)) {
        // User has entered the same name
        deferred.reject('msg_same_name_error');
      } else {
        // Edit game
        game.name = name;
        $http.post('console/game', game).
        success(function(data, status, headers, config) {
        }).
        error(function(data, status, headers, config) {
        	 deferred.reject('msg_game_name_error');
        });
        
        deferred.resolve(game);
      }

      return deferred.promise;
    };

    // Add or edit instance
    var editInstance = function (game, instance, instanceType, instanceProperties) {
      var deferred = $q.defer();

      if (!instanceProperties.name) {
        deferred.reject('msg_instance_name_error');
      } else if (instance.id == null) {
        // New instance
        if (!!existsInstanceByName(game, instanceProperties.name, instanceType)) {
          // Instance with same name alredy exists
          deferred.reject('msg_instance_name_exists_error');
        } else {
          // Create new instance
          var id = 1;
          angular.forEach(game.concepts, function (i) {
            if (i.id > id) {
              id = i.id;
            }
            id++;
          });

          var url = '';
         
          instance = {
        		  'id': id,
          		  'name': instanceProperties.name
          };
          // Choose instance object structure
          if (instanceType == 'points') {
        	  game.pointConcept.push(instance);
          } else if (instanceType == 'badges_collections') {
        	  game.badgeCollectionConcept.push(instance);
          }
          
          $http.post('console/game', game).success(function(data, status, headers, config) {
        	  deferred.resolve(data);
          }).error(function(data, status, headers, config){
        	  deferred.reject('msg_instance_name_error');
          });
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

      angular.forEach(game.concepts[instanceType], function (i, index) {
        if (i.id == instance.id) {
          game.concepts[instanceType].splice(index, 1);
          deferred.resolve();
        }
      });

      return deferred.promise;
    };

    // Check if leaderboard points dependency is actived befoer allow leaderboard activation
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

    // Check if there are any ACTIVE leaderboards linked to the given points instance, and then return them
    var pointsDeactivationCheck = function (game, points) {
      var leaderboards = [];
      angular.forEach(game.concepts.leaderboards, function (leaderboard) {
        if (leaderboard.points_dependency == points.name && leaderboard.is_active) {
          leaderboards.push(leaderboard);
        }
      });

      return leaderboards;
    };

    // Check if there are any leaderboards linked to the given points instance, and then return them
    var pointsDeleteCheck = function (game, points) {
      var leaderboards = [];
      angular.forEach(game.concepts.leaderboards, function (leaderboard) {
        if (leaderboard.points_dependency == points.name) {
          leaderboards.push(leaderboard);
        }
      });

      return leaderboards;
    };

    // Deactivate the given leaderboards
    var deactivateLeaderboards = function (leaderboards) {
      angular.forEach(leaderboards, function (leaderboard) {
        leaderboard.is_active = false;
      });
    };

    // Delete the given leaderboards
    var deleteLeaderboards = function (game, leaderboards) {
      // Using pure JS 'for' instead of 'angular.forEach' due to index trouble in splice operations
      for (var i = 0; i < game.concepts.leaderboards.length; i++) {
        for (var j = 0; j < leaderboards.length; j++) {
          if (game.concepts.leaderboards[i].id == leaderboards[j].id)
            game.concepts.leaderboards.splice(i, 1);
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
      'deactivateLeaderboards': deactivateLeaderboards,
      'deleteLeaderboards': deleteLeaderboards,
      'saveGame' : saveGame,
      'addPoint' : addPoint,
      'addBadge' : addBadge,
      'getPoints' : getPoints,
      'getBadges' : getBadges,
    };
  }
);

app.factory('utilsFactory',
  function () {
    // Utils factory

    // Count active instances
    var countActive = function (game, type) {
      var count = 0;
      if (!!game && !!game.concepts && !!game.concepts[type]) {
        angular.forEach(game.concepts[type], function (value) {
          if (value.is_active) {
            count++;
          }
        });
      }
      return count;
    };

    // Get given instances lenght
    var getLength = function (game, type) {
      var len = 0;
      if (!!game && !!game.concepts && !!game.concepts[type]) {
        len = game.concepts[type].length;
      }
      return len;
    };

    return {
      'getLength': getLength,
      'countActive': countActive
    };
  }
);
