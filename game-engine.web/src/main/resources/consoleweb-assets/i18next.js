// Load and set up the translation library i18next
angular.module('jm.i18next')
	.config(function ($i18nextProvider) {
		'use strict';
		$i18nextProvider.options = {
			useCookie: false,
			useLocalStorage: false,
			fallbackLng: 'en',
			resGetPath: 'locales/__lng__/__ns__.json',
			ns: {
				namespaces: ['messages', 'labels'],
				defaultNs: 'labels'
			}
		};
	});
