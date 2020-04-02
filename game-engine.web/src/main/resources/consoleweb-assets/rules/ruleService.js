ruleModule.factory('ruleService', function ($http, $q) {
	/*
	 * WARNING: this is a refactor to break gamesFactory service into modular services
	 * look at gamesFactory.setUrl function used by main.js from platform profile
	 */
	const url = "..";
	
	const create = function (game, rule) {
		var deferred = $q.defer();
		const ruleForServer = {
				id : rule.id,
				name : rule.name,
				content : rule.content
		};
		$http.post(url + `/console/game/${game.id}/rule/db`, ruleForServer).success(function (data, status, headers, config) {
			deferred.resolve(data);
		}).error(function (data, status, headers, config) {
			deferred.reject('msg_generic_error');
		});
		return deferred.promise;
	}

	const remove = function (game, ruleId) {
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

	const validate = function (ruleContent) {
		var deferred = $q.defer();
		$http.post(url + `/console/rule/validate`, ruleContent).success(function (data, status, headers, config) {
			deferred.resolve(data);
		}).error(function (data, status, headers, config) {
			deferred.reject('msg_generic_error');
		});
		return deferred.promise;
	}

	const read = function (game, ruleId) {
		var deferred = $q.defer();
		ruleId = ruleId.slice(ruleId.indexOf("://") + 3);
		$http.get(url + `/console/game/${game.id}/rule/db/${ruleId}`).success(function (data, status, headers, config) {
			deferred.resolve(data);
		}).error(function (data, status, headers, config) {
			deferred.reject('msg_rule_error');
		});
		return deferred.promise;
	}
	
	return {
		create,
		remove,
		validate,
		read
	};
});