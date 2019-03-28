/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.trentorise.game.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import eu.trentorise.game.platform.PlatformAuthorizationInterceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

/*
 * extend WebMvcConfigurerAdapter and not use annotation @EnableMvc to permit correct static
 * resources publishing and restController functionalities
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

	/**
	 * If this mapping change, remember to align angular file app.js
	 * i18nextProvider if not angular internationalization will be broken
	 */
	private static final String CONSOLE_URL_MAPPING = "consoleweb";
	private static final String CONSOLE_LOGIN_URL_MAPPING = "login";

    @Autowired
    private HandlerInterceptor authInterceptor;

    @Autowired
    private Environment env;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler(String.format("/%s/**", CONSOLE_URL_MAPPING))
				.addResourceLocations("classpath:/consoleweb-assets/");
		registry.addResourceHandler(String.format("/%s/**", CONSOLE_LOGIN_URL_MAPPING))
				.addResourceLocations("classpath:/consoleweb-assets/");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController(String.format("/%s/", CONSOLE_URL_MAPPING)).setViewName("forward:index.html");
		registry.addViewController(String.format("/%s/", CONSOLE_LOGIN_URL_MAPPING)).setViewName("forward:login.html");
	}

	 @Bean
     public HandlerInterceptor platformInterceptor() {
     return new PlatformAuthorizationInterceptor();
     }
	
	public void addInterceptors(InterceptorRegistry registry) {
        String[] paths = null;
        if (Arrays.stream(env.getActiveProfiles())
                .anyMatch(profile -> profile.equals("platform"))) {
            paths = new String[] {"/api/**"};
        } else {
            paths = new String[] {"/gengine/**", "/console/**", "/model/**", "/data/**", "/exec/**",
                    "/notification/**"};
        }
        registry.addInterceptor(authInterceptor).addPathPatterns(paths);
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableHandlerMethodArgumentResolver pageResolver =
                new PageableHandlerMethodArgumentResolver();
        pageResolver.setPrefix("");
        pageResolver.setOneIndexedParameters(true);

        argumentResolvers.add(pageResolver);
    }

    @Autowired
    public void configureJackson(ObjectMapper jackson2ObjectMapper) {
        jackson2ObjectMapper.registerModule(new Jdk8Module());
    }

}
