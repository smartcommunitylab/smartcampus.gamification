package eu.trentorise.game.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import eu.trentorise.game.service.IdentityLookupService;
import eu.trentorise.game.service.SpringSecurityIdentityLookup;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:engine.web.properties")
@Profile({ "sec" })
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	Environment env;

	@Bean
	public IdentityLookupService identityLookup() {
		return new SpringSecurityIdentityLookup();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication()
				.withUser(env.getProperty("consoleweb.admin.username", "admin"))
				.password(env.getProperty("consoleweb.admin.password", "admin"))
				.roles("ADMIN");

		/*
		 * user for test scope only
		 */
		// auth.inMemoryAuthentication().withUser("user").password("password")
		// .roles("ADMIN");

		// demo user
		auth.inMemoryAuthentication().withUser("sco_master")
				.password("sco_master").roles("ADMIN");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/console/**")
				.access("hasRole('ROLE_ADMIN')").antMatchers("/gengine/**")
				.access("hasRole('ROLE_ADMIN')").antMatchers("/consoleweb/**")
				.access("hasRole('ROLE_ADMIN')").and().httpBasic();

		// disable csrf permits POST http call to ConsoleController
		// without using csrf token
		http.csrf().disable();

	}
}
