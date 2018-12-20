package eu.trentorise.game.config;

import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import eu.trentorise.game.api.rest.AACAuthenticationInterceptor;
import eu.trentorise.game.platform.PlatformAuthorizationInterceptor;

@Configuration
@EnableOAuth2Client
@Profile("platform")
public class PlatformWebConfig extends WebMvcConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Bean
	public HandlerInterceptor platformTokenInterceptor() {
		return new AACAuthenticationInterceptor();
	}

	@Bean
	public HandlerInterceptor platformAuthInterceptor() {
		return new PlatformAuthorizationInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(platformTokenInterceptor()).addPathPatterns("/api/**");
		registry.addInterceptor(platformAuthInterceptor()).addPathPatterns("/api/**");

	}

	@Bean
	@ConfigurationProperties("security.oauth2.client")
	public AuthorizationCodeResourceDetails aac() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("security.oauth2.resource")
	public ResourceServerProperties aacResource() {
		return new ResourceServerProperties();
	}

	private Filter ssoFilter() {
		OAuth2ClientAuthenticationProcessingFilter aacFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/login/aac");
		OAuth2RestTemplate aacTemplate = new OAuth2RestTemplate(aac(), oauth2ClientContext);
		aacFilter.setRestTemplate(aacTemplate);
		UserInfoTokenServices tokenServices = new UserInfoTokenServices(aacResource().getUserInfoUri(),
				aac().getClientId());
		tokenServices.setRestTemplate(aacTemplate);
		tokenServices.setPrincipalExtractor(new PrincipalExtractor() {

			@SuppressWarnings("unchecked")
			@Override
			public Object extractPrincipal(Map<String, Object> map) {
				return map;
			}

		});

		aacFilter.setTokenServices(tokenServices);
		return aacFilter;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/gengine/**", "/console/**", "/model/**", "/data/**", "/exec/**", "/notification/**")
				.fullyAuthenticated().and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
	}

}
