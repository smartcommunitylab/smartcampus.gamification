package eu.trentorise.game.api.rest;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import eu.trentorise.game.managers.PermissionManager;

@Component
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	PermissionManager permissionManager;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		@SuppressWarnings("unchecked")
		Map<String, String> pathVariables = (Map<String, String>) request
				.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

		String gameId = (String) pathVariables.get("gameId");
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		boolean isOk = permissionManager.checkGamePermission(user, gameId);
		if (!isOk) {
			response.sendError(HttpServletResponse.SC_FORBIDDEN,
					"access not permitted");
		}
		return isOk;
	}
}