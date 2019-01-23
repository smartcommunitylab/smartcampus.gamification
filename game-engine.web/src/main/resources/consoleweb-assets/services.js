/*
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

angular.module('gamificationEngine.services', [])
	.factory('gamesFactory', function ($rootScope, $http, $q, $timeout) {
		// Games data operations factory
		//var url = "https://dev.smartcommunitylab.it/gamification";
		var url = "..";

		var setUrl = function (urlPrefix) {
			url = urlPrefix;
		}

		// Get games
		var getGames = function () {
			
			var deferred = $q.defer();

			// If games haven't been already loaded
			if (!$rootScope.games || $rootScope.games.length === 0) {
				// Load games
				$http.get(url + `/console/game`).success(function (data) {
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
		
		var getGamesByDomain = function () {
			
			var deferred = $q.defer();

			// If games haven't been already loaded
			if (!$rootScope.games || $rootScope.games.length === 0) {
				// Load games
				$http.get(url + `/console/game-by-domain`).success(function (data) {
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

		// Boolean. Returns whether exists or not an instance by its name
		var existsInstanceByName = function (game, instanceName, instanceType) {
			var found = false;
			var a = [];
			if (instanceType === 'points') {
				a = game.pointConcept;
			}

			if (instanceType === 'badges_collections') {
				a = game.badgeCollectionConcept;
			}
			angular.forEach(a, function (i) {
				if (!found && i.name === instanceName) {
					found = true;
				}
			});
			return found;
		};

		var getPoints = function (gameId) {
			var deferred = $q.defer();

			$http.get(url + `/console/game/${gameId}/point`).
			success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).
			error(function (data, status, headers, config) {
				deferred.reject();
			});

			return deferred.promise;
		}

		var getBadges = function (gameId) {
			var deferred = $q.defer();

			$http.get(url + `/console/game/${gameId}/badgecoll`).
			success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).
			error(function (data, status, headers, config) {
				deferred.reject();
			});

			return deferred.promise;
		}

		var addTask = function (game, task) {
			task.type = 'general';
			// ^\s*($|#|\w+\s*=|(\?|\*|(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?(?:,(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?)*)\s+(\?|\*|(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?(?:,(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?)*)\s+(\?|\*|(?:[01]?\d|2[0-3])(?:(?:-|\/|\,)(?:[01]?\d|2[0-3]))?(?:,(?:[01]?\d|2[0-3])(?:(?:-|\/|\,)(?:[01]?\d|2[0-3]))?)*)\s+(\?|\*|(?:0?[1-9]|[12]\d|3[01])(?:(?:-|\/|\,)(?:0?[1-9]|[12]\d|3[01]))?(?:,(?:0?[1-9]|[12]\d|3[01])(?:(?:-|\/|\,)(?:0?[1-9]|[12]\d|3[01]))?)*)\s+(\?|\*|(?:[1-9]|1[012])(?:(?:-|\/|\,)(?:[1-9]|1[012]))?(?:L|W)?(?:,(?:[1-9]|1[012])(?:(?:-|\/|\,)(?:[1-9]|1[012]))?(?:L|W)?)*|\?|\*|(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?(?:,(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?)*)\s+(\?|\*|(?:[0-6])(?:(?:-|\/|\,|#)(?:[0-6]))?(?:L)?(?:,(?:[0-6])(?:(?:-|\/|\,|#)(?:[0-6]))?(?:L)?)*|\?|\*|(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?(?:,(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?)*)(|\s)+(\?|\*|(?:|\d{4})(?:(?:-|\/|\,)(?:|\d{4}))?(?:,(?:|\d{4})(?:(?:-|\/|\,)(?:|\d{4}))?)*))$
			// reg exp for cron validation
			var deferred = $q.defer();
			$http.post(url + `/console/game/${game.id}/task`, task).
			success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).
			error(function (data, status, headers, config) {
				deferred.reject('msg_task_error');
			});

			return deferred.promise;
		}

		var editTask = function (game, task) {
			// ^\s*($|#|\w+\s*=|(\?|\*|(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?(?:,(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?)*)\s+(\?|\*|(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?(?:,(?:[0-5]?\d)(?:(?:-|\/|\,)(?:[0-5]?\d))?)*)\s+(\?|\*|(?:[01]?\d|2[0-3])(?:(?:-|\/|\,)(?:[01]?\d|2[0-3]))?(?:,(?:[01]?\d|2[0-3])(?:(?:-|\/|\,)(?:[01]?\d|2[0-3]))?)*)\s+(\?|\*|(?:0?[1-9]|[12]\d|3[01])(?:(?:-|\/|\,)(?:0?[1-9]|[12]\d|3[01]))?(?:,(?:0?[1-9]|[12]\d|3[01])(?:(?:-|\/|\,)(?:0?[1-9]|[12]\d|3[01]))?)*)\s+(\?|\*|(?:[1-9]|1[012])(?:(?:-|\/|\,)(?:[1-9]|1[012]))?(?:L|W)?(?:,(?:[1-9]|1[012])(?:(?:-|\/|\,)(?:[1-9]|1[012]))?(?:L|W)?)*|\?|\*|(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?(?:,(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC)(?:(?:-)(?:JAN|FEB|MAR|APR|MAY|JUN|JUL|AUG|SEP|OCT|NOV|DEC))?)*)\s+(\?|\*|(?:[0-6])(?:(?:-|\/|\,|#)(?:[0-6]))?(?:L)?(?:,(?:[0-6])(?:(?:-|\/|\,|#)(?:[0-6]))?(?:L)?)*|\?|\*|(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?(?:,(?:MON|TUE|WED|THU|FRI|SAT|SUN)(?:(?:-)(?:MON|TUE|WED|THU|FRI|SAT|SUN))?)*)(|\s)+(\?|\*|(?:|\d{4})(?:(?:-|\/|\,)(?:|\d{4}))?(?:,(?:|\d{4})(?:(?:-|\/|\,)(?:|\d{4}))?)*))$
			// reg exp for cron validation
			var deferred = $q.defer();
			$http.put(url + `/console/game/${game.id}/task`, task).
			success(function (data, status, headers, config) {
				deferred.resolve();
			}).
			error(function (data, status, headers, config) {
				deferred.reject('msg_task_error');
			});

			return deferred.promise;
		}

		var deleteTask = function (game, task) {
			var deferred = $q.defer();
			$http.post(url + `/console/game/${game.id}/task/del`, task).
			success(function (data, status, headers, config) {
				deferred.resolve();
			}).
			error(function (data, status, headers, config) {
				deferred.reject('msg_task_error');
			});

			return deferred.promise;
		}
		
		
		var addIncrementalClassification = function(game, classification) {
			var deferred = $q.defer();
			$http.post(url + `/model/game/${game.id}/incclassification`, classification).
			success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).
			error(function (data, status, headers, config) {
				deferred.reject('msg_task_error');
			});

			return deferred.promise;
		}
		
		var deleteIncrementalClassification = function (game, classification) {
			var deferred = $q.defer();
			$http.delete(url + `/model/game/${game.id}/incclassification/${classification.name}`).
			success(function (data, status, headers, config) {
				deferred.resolve();
			}).
			error(function (data, status, headers, config) {
				deferred.reject('msg_task_error');
			});

			return deferred.promise;
		}
		
		var editIncrementalClassification = function (game, classification) {
			var deferred = $q.defer();
			$http.put(url + `/model/game/${game.id}/incclassification/${classification.name}`, classification).
			success(function (data, status, headers, config) {
				deferred.resolve();
			}).
			error(function (data, status, headers, config) {
				deferred.reject('msg_task_error');
			});

			return deferred.promise;
		}
		
		

		var saveGame = function (game) {
			var deferred = $q.defer();

			$http.post(url + `/console/game`, game).
			success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).
			error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		};

		// Add or edit game
		var editGame = function (game, fields) {
			var deferred = $q.defer();

				if (!game.id) {
					game = {};
				}
				game.name = fields.name.trim();
				game.expiration = fields.expiration;
				game.domain = fields.domain ? fields.domain.trim() : fields.domain;

				$http.post(url + `/console/game`, game).
				success(function (data, status, headers, config) {
					if (!game.id) {
						$rootScope.games.unshift(data);
					}
					deferred.resolve(data);
				}).
				error(function (data, status, headers, config) {
					deferred.reject('msg_generic_error');
				});
			return deferred.promise;
		};

		// Add or edit instance
		var editInstance = function (game, instanceType, instanceProperties) {
			var deferred = $q.defer();

			// Create new instance
			var id = 1;
			angular.forEach(game.concepts, function (i) {
				if (i.id > id) {
					id = i.id;
				}
				id++;
			});


			var instance = {
					'id': id,
					'name': instanceProperties.name
			};

			var tmpGame = angular.copy(game);

			// Choose instance object structure
			if (instanceType == 'points') {
				let periods = {};
				let dayMillis = 24 * 3600 * 1000;
				angular.forEach(instanceProperties.periods,function(value,key){
					periods[value.name] = {start: value.start.getTime(),period: value.period * dayMillis, identifier :value.name, capacity: value.capacity};
				});
				instance.periods = periods;
				tmpGame.pointConcept.unshift(instance);
			} else if (instanceType == 'badges_collections') {
				tmpGame.badgeCollectionConcept.unshift(instance);
			}

			$http.post(url + `/console/game`, tmpGame).success(function (data, status, headers, config) {

				deferred.resolve(instance);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		};

		// Delete game
		var deleteGame = function (game) {
			var deferred = $q.defer();

			$http.delete(url + `/console/game/${game.id}`).success(function (data, status, headers, config) {
				angular.forEach($rootScope.games, function (g, index) {
					if (g.id == game.id) {
						$rootScope.games.splice(index, 1);
						deferred.resolve();
					}
				});
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_delete_error');
			});

			return deferred.promise;
		};

		var addRule = function (game, rule) {
			var deferred = $q.defer();

			$http.post(url + `/console/game/${game.id}/rule/db`, rule).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});
			return deferred.promise;
		}

		var deleteRule = function (game, ruleId) {
			var deferred = $q.defer();
			var rule = {};
			ruleId = ruleId.slice(ruleId.indexOf("://") + 3);
			$http.delete(url + `/console/game/${game.id}/rule/db/${ruleId}`).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_delete_error');
			});
			return deferred.promise;
		}

		var validateRule = function (ruleContent) {
			var deferred = $q.defer();
			$http.post(url + `/console/rule/validate`, ruleContent).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});
			return deferred.promise;
		}

		var getRule = function (game, ruleId) {
			var deferred = $q.defer();
			ruleId = ruleId.slice(ruleId.indexOf("://") + 3);
			$http.get(url + `/console/game/${game.id}/rule/db/${ruleId}`).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_rule_error');
			});
			return deferred.promise;
		}

		var getPlayersState = function (gameId, playerFilter, pageRequest, pageSize) {
			var deferred = $q.defer();
			$http.get(url + `/gengine/state/${gameId}`, {
				params: {
					page: pageRequest,
					size: pageSize,
					playerFilter: playerFilter
				}
			}).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		}
		
		var playersSearch = function (gameId, playerFilter, pageRequest, pageSize) {
			var deferred = $q.defer();
			var query = {};
			query.rawQuery = {};
			query.rawQuery.query = {};
			query.rawQuery.query = {'playerId' : '/' +playerFilter+'/'}
			$http.post(url + `/gamification/data/game/${gameId}/player/search`,query, {
				params: {
					page: pageRequest,
					size: pageSize,
				}
			}).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		}
		
		var saveChallengeModel = function(gameId, model) {
			var deferred = $q.defer();
			model.variables = [];
			angular.copy(model.fields , model.variables);
			delete model.fields;
			$http.post(url + `/model/game/${gameId}/challenge`, model).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		}
		
		var readChallengeModels = function(gameId) {
			var deferred = $q.defer();
			$http.get(url + `/model/game/${gameId}/challenge`).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		}
		
		var deleteChallengeModel = function(gameId,modelId) {
			var deferred = $q.defer();
			$http.delete(url + `/model/game/${gameId}/challenge/${modelId}`).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		}

		
		const saveLevel = function (gameId, level) {
			var deferred = $q.defer();

			$http.post(url + `/model/game/${gameId}/level`, level).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});
			return deferred.promise;
		}
		
		const deleteLevel = function (gameId, level) {
			var deferred = $q.defer();

			$http.delete(url + `/model/game/${gameId}/level/${level.name}`).success(function (data, status, headers, config) {
				deferred.resolve();
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});
			return deferred.promise;
		}

		// Get game by name
		var userProfile = function () {
			var deferred = $q.defer();
			$http.get(`../userProfile/`).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});

			return deferred.promise;
		};
		
		return {
			getGamesByDomain,
			'getGames': getGames,
			'getGameById': getGameById,
			'getGameByName': getGameByName,
			'editGame': editGame,
			'existsInstanceByName': existsInstanceByName,
			'editInstance': editInstance,
			'deleteGame': deleteGame,
			'saveGame': saveGame,
			'getPoints': getPoints,
			'getBadges': getBadges,
			'addRule': addRule,
			'deleteRule': deleteRule,
			'getRule': getRule,
			'addTask': addTask,
			'deleteTask': deleteTask,
			'editTask': editTask,
			'validateRule': validateRule,
			'getPlayersState': getPlayersState,
			'saveChallengeModel' : saveChallengeModel,
			'readChallengeModels' : readChallengeModels,
			'deleteChallengeModel' : deleteChallengeModel,
			'addIncrementalClassification' : addIncrementalClassification,
			'deleteIncrementalClassification' : deleteIncrementalClassification,
			'editIncrementalClassification' : editIncrementalClassification,
			'saveLevel' : saveLevel,
			'deleteLevel': deleteLevel,
			'userProfile': userProfile,
			'setUrl': setUrl,
		};
	})
	.factory('utilsFactory', function () {
		// Utils factory
		// Get given instances lenght
		var getLength = function (game, type) {
			var len = 0;
			if (!!game) {
				if (type === 'points') {
					len = game.pointConcept.length;
				}
				if (type === 'badges_collections') {
					len = game.badgeCollectionConcept.length;
				}

			}
			return len;
		};

		return {
			'getLength': getLength,
		};
	});
