package eu.trentorise.game.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfiguration;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import eu.trentorise.game.platform.PlatformRolesClient;

@Configuration
//@EnableOAuth2Client
@EnableOAuth2Sso
@Profile("platform")
public class PlatformWebConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	OAuth2ClientContext oauth2ClientContext;

	@Autowired
	private PlatformRolesClient platformRoles;

	@Autowired
	@Value("${rememberMe.key}")
	private String rememberMeKey;

	@Bean
	@ConfigurationProperties("security.oauth2.client")
	public AuthorizationCodeResourceDetails aac() {
		return new AuthorizationCodeResourceDetails();
	}

	@Bean
	@ConfigurationProperties("security.oauth2.resource")
	@Primary
	public ResourceServerProperties aacResource() {
		return new ResourceServerProperties();
	}

	private Filter ssoFilter() {
		OAuth2ClientAuthenticationProcessingFilter aacFilter = new OAuth2ClientAuthenticationProcessingFilter(
				"/login/aac");
		OAuth2RestTemplate aacTemplate = new OAuth2RestTemplate(aac(), oauth2ClientContext);
		aacFilter.setRestTemplate(aacTemplate);
		AacUserInfoTokenServices tokenServices = new AacUserInfoTokenServices(aacResource().getUserInfoUri(),
				aac().getClientId());
		tokenServices.setRestTemplate(aacTemplate);
		tokenServices.setPlatformRoleClient(platformRoles);
		tokenServices.setPrincipalExtractor(new PrincipalExtractor() {

			@Override
			public Object extractPrincipal(Map<String, Object> map) {
				return (String) map.get("name");
			}

		});

		aacFilter.setTokenServices(tokenServices);
		return aacFilter;
	}

	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/gengine/**", "/consoleweb/**", "/model/**", "/data/**", "/exec/**", "/notification/**", "/userProfile/**", "/api/**/console/**")
				.fullyAuthenticated().and().exceptionHandling()
				.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login/aac")).and()
				.addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
	}

	@Bean
	protected ResourceServerConfiguration apiResources() {
		ResourceServerConfiguration resource = new ResourceServerConfiguration() {
			public void setConfigurers(List<ResourceServerConfigurer> configurers) {
				super.setConfigurers(configurers);
			}
		};
		resource.setConfigurers(Arrays.<ResourceServerConfigurer> asList(new ResourceServerConfigurerAdapter() {
			public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
				resources.resourceId(null);
				OAuth2RestTemplate aacTemplate = new OAuth2RestTemplate(aac(), oauth2ClientContext);
				AacUserInfoTokenServices tokenServices = new AacUserInfoTokenServices(aacResource().getUserInfoUri(),
						aac().getClientId());
				tokenServices.setRestTemplate(aacTemplate);
				tokenServices.setPlatformRoleClient(platformRoles);
				tokenServices.setPrincipalExtractor(new PrincipalExtractor() {

					@Override
					public Object extractPrincipal(Map<String, Object> map) {
						return (String) map.get("name");
					}

				});
				resources.tokenServices(tokenServices);
			}

			public void configure(HttpSecurity http) throws Exception {
				http.antMatcher("/*api/**").authorizeRequests().antMatchers(HttpMethod.OPTIONS, "/*api/**").permitAll()
						.antMatchers("/api/**").fullyAuthenticated().and().csrf().disable();
			}
		}));
		resource.setOrder(Ordered.LOWEST_PRECEDENCE);
		return resource;
	}

}
