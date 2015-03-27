/**
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

package eu.trentorise.game.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import eu.trentorise.game.api.rest.AuthorizationInterceptor;

/*
 * extend WebMvcConfigurerAdapter and not use annotation @EnableMvc to permit
 * correct static resources publishing and restController functionalities
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

	/**
	 * If this mapping change, remember to align angular file app.js
	 * i18nextProvider if not angular internationalization will be broken
	 */
	private static final String CONSOLE_URL_MAPPING = "consoleweb";

	@Autowired
	AuthorizationInterceptor authInterceptor;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(
				String.format("/%s/**", CONSOLE_URL_MAPPING))
				.addResourceLocations("classpath:/consoleweb-assets/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(String.format("/%s/", CONSOLE_URL_MAPPING))
				.setViewName("forward:index.html");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authInterceptor).addPathPatterns(
				"/console/game/**");
	}

}
