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

package eu.trentorise.game.service;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class DefaultIdentityLookup implements IdentityLookupService {

    public static final String DEFAULT_USER = "sco_master";
    public static final String DEFAULT_ROLE = "ADMIN";

    private static Authentication defaultUser = new UsernamePasswordAuthenticationToken(
            DEFAULT_USER, "password", Arrays.asList(new SimpleGrantedAuthority(DEFAULT_ROLE)));

    @Override
    public String getName() {
        return DEFAULT_USER;
    }

    @Override
    public Authentication getAuthentication() {
        return defaultUser;
    }

    @Override
    public String getRole() {
        return getAuthentication().getAuthorities().stream().findFirst().get().getAuthority();
    }


}
