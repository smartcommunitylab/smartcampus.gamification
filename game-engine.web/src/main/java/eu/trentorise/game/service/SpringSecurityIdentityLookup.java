package eu.trentorise.game.service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringSecurityIdentityLookup implements IdentityLookupService {

	@Override
	public String getName() {
		return SecurityContextHolder.getContext().getAuthentication().getName();
	}

}
