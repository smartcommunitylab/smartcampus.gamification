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
