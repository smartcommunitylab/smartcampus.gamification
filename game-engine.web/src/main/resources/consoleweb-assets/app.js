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


var app = angular.module('gamificationEngine', [
	'gamificationEngine.main',
	'gamificationEngine.actions',
	'gamificationEngine.game',
	'gamificationEngine.monitor',
	'gamificationEngine.home',
	'gamificationEngine.login',
	'gamificationEngine.rules',
	'gamificationEngine.tasks',
	'gamificationEngine.modals',
	'gamificationEngine.services',
	'ui.router',
	'ui.bootstrap',
	'jm.i18next',
	'toggle-switch',
	'ui.bootstrap.datetimepicker'
])

// Switch application views and states
.config(
	function ($stateProvider, $urlRouterProvider) {
		$stateProvider
			.state('home', {
				url: '/home',
				templateUrl: 'home/home.html',
				controller: 'HomeCtrl',
				data: {
					page: 'home'
				}
			})

		.state('game', {
			url: '/game/:id?tab',
			templateUrl: 'game/game.html',
			controller: 'GameCtrl',
			data: {
				page: 'game'
			}
		})

		.state('game-monitor', {
			url: '/game-monitor/:id',
			templateUrl: 'game-monitor/game-monitor.html',
			controller: 'MonitorCtrl',
			data: {
				page: 'monitor'
			}
		})

		$urlRouterProvider.otherwise("/home");
	}
);
