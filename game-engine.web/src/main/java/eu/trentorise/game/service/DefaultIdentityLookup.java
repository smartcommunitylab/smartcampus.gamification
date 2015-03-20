package eu.trentorise.game.service;

import org.springframework.stereotype.Component;

@Component
public class DefaultIdentityLookup implements IdentityLookupService {

	private static final String DEFAULT_USER = "sco_master";

	@Override
	public String getName() {
		return DEFAULT_USER;
	}

}
