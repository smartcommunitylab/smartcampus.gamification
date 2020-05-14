'use strict';

levelModule
	.factory('levelService', function ($http, $q) {
		/*
		 * WARNING: this is a refactor to break gamesFactory service into modular services
		 * look at gamesFactory.setUrl function used by main.js from platform profile
		 */
		const url = "..";
		
		const saveLevel = function (gameId, level) {
			var deferred = $q.defer();
			$http.post(url + `/model/game/${gameId}/level`, level).success(function (data, status, headers, config) {
				deferred.resolve(data);
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});
			return deferred.promise;
		};
		
		const deleteLevel = function (gameId, level) {
			var deferred = $q.defer();

			$http.delete(url + `/model/game/${gameId}/level/${level.name}`).success(function (data, status, headers, config) {
				deferred.resolve();
			}).error(function (data, status, headers, config) {
				deferred.reject('msg_generic_error');
			});
			return deferred.promise;
		};
		
		return {
			saveLevel,
			deleteLevel
		};	
});