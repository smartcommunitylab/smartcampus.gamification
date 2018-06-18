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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.game.api.rest.AuthorizationInterceptor;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.AuthUser;
import eu.trentorise.game.sec.UsersProvider;
import eu.trentorise.game.service.IdentityLookupService;
import eu.trentorise.game.service.SpringSecurityIdentityLookup;

@Configuration
@EnableWebSecurity
@Profile({"sec"})
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);

    @Autowired
    private UsersProvider usersProvider;

    @Bean
    public IdentityLookupService identityLookup() {
        return new SpringSecurityIdentityLookup();
    }

    @Bean
    public HandlerInterceptor authInterceptor() {
        return new AuthorizationInterceptor();
    }

    @Bean
    public HandlerInterceptor tokenInterceptor() {
        // dummie interceptor
        return new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
                    Object handler) throws Exception {
                return true;
            }

            @Override
            public void postHandle(HttpServletRequest request, HttpServletResponse response,
                    Object handler, ModelAndView modelAndView) throws Exception {

            }

            @Override
            public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                    Object handler, Exception ex) throws Exception {

            }
        };
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

        for (AuthUser user : usersProvider.getUsers()) {
            auth.inMemoryAuthentication().withUser(user.getUsername()).password(user.getPassword())
                    .roles(user.getRole());
            LogHub.info(null, logger, "Loaded auth user {}", user.getUsername());
        }
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // application never creates an http session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().antMatchers("/console/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/gengine/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/consoleweb/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/model/**").access("hasRole('ROLE_ADMIN')").and().httpBasic();

        // disable csrf permits POST http call to DomainConsoleController
        // without using csrf token
        http.csrf().disable();

    }
}
