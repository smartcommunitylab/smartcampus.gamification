package eu.trentorise.game.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.inMemoryAuthentication().withUser("gameAdmin")
				.password("password").roles("ADMIN");
		auth.inMemoryAuthentication().withUser("user").password("password")
				.roles("USER");

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/console/**")
				.access("hasRole('ROLE_ADMIN')").antMatchers("/consoleweb/**")
				.access("hasRole('ROLE_ADMIN')").and().httpBasic();

		// disable csrf permits POST http call to ConsoleController
		// without using csrf token
		http.csrf().disable();

	}
}
