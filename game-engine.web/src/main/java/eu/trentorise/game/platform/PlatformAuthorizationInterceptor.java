package eu.trentorise.game.platform;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import eu.trentorise.game.managers.PermissionManager;
import eu.trentorise.game.service.IdentityLookupService;

public class PlatformAuthorizationInterceptor implements HandlerInterceptor {

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private IdentityLookupService identityLookup;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        @SuppressWarnings("unchecked")
        Map<String, String> pathVariables = (Map<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        String gameId = (String) pathVariables.get("gameId");
        String domain = identityLookup.getDomain();
        boolean isOk = permissionManager.checkDomain(domain, gameId);
        if (!isOk) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "access not permitted");
        }
        return isOk;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
            Object handler, Exception ex) throws Exception {

    }

}
