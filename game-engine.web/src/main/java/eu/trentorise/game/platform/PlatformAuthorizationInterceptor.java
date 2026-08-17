package eu.trentorise.game.platform;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.game.managers.PermissionManager;
import eu.trentorise.game.service.IdentityLookupService;

public class PlatformAuthorizationInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(PlatformAuthorizationInterceptor.class);

	@Autowired
	private PermissionManager permissionManager;

	@Autowired
	private IdentityLookupService identityLookup;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> pathVariables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		String gameId = (String) pathVariables.get("gameId");
		String domain = identityLookup.getDomain();
		
		// check domain against game.
		boolean isOk = permissionManager.checkDomain(domain, gameId);
		if (!isOk) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "access not permitted");
		}

		// check domain against security context authorities.
		String domainRequested = extractDomain(request);
		boolean passRequest = false;
		if (domainRequested != null) {
			try {
				for (GrantedAuthority auth : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
					if (auth.getAuthority().equalsIgnoreCase(domainRequested)) {
						passRequest = true;
						break;
					}
				}
				;
			} catch (SecurityException | IllegalArgumentException e) {
				passRequest = false;
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "access not permitted");
			}
		} else {
			passRequest = false;
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "access not permitted");
		}
		
		if (!passRequest) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN, "access not permitted");
		}

		return isOk && passRequest;
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	private String extractDomain(HttpServletRequest request) {
		String url = request.getRequestURI();
		url = url.substring(request.getContextPath().length());
		int lowerIndex = url.indexOf("/", 1) + 1;
		int upperIndex = url.indexOf("/", lowerIndex);
		String domain = url.substring(lowerIndex, upperIndex);
		logger.debug("Extracted domain from request: {}", request);
		return domain;
	}

}
