package eu.trentorise.game.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.model.AuthUser;

@RestController
@RequestMapping("/userProfile")
public class ProfileController {

	private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
	
   @RequestMapping(method = RequestMethod.GET, produces = { "application/json" })
	public AuthUser getUserProfile() {

		AuthUser user = completeUserDetails();

		return user;
	}

	private AuthUser completeUserDetails() {

		AuthUser user;
		if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()
				&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null) {

			user = new AuthUser();
			user.setUsername(SecurityContextHolder.getContext().getAuthentication().getName());
			for (GrantedAuthority auth : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
				user.getDomains().add(auth.getAuthority().toString());
			}
		} else {
			logger.info("Principal not found");
			throw new UnauthorizedUserException("Principal not found");
		}

		return user;

	}
	
}
