package eu.trentorise.game.api.rest;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import eu.trentorise.game.platform.PlatformRolesClient;

public class AACAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger =
            LoggerFactory.getLogger(AACAuthenticationInterceptor.class);

    @Autowired
    private PlatformRolesClient platformRoles;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws IOException {


        String token = extractToken(request);
        String domain = extractDomain(request);

        boolean passRequest = true;
        if (token != null) {
            try {
                List<String> roles = platformRoles.getRolesByToken(token);

                if (!roles.contains(domain)) {
                    logger.warn("User has not privileges to operate with domain {}", domain);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, String
                            .format("User has not privileges to operate with domain %s", domain));
                    passRequest = false;
                } else {
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(null, null,
                                authorities));
                }
            } catch (SecurityException | IllegalArgumentException e) {
                logger.warn("Exception retrieving user roles", e.getMessage());
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "token is not valid");
                logger.warn("Token could be invalid");
                passRequest = false;
            }

        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "request should be authenticated");
            logger.warn("Request without token");
            passRequest = false;
        }

        return passRequest;
    }

    private String extractDomain(HttpServletRequest request) {
        return "test";
    }

    private String extractToken(HttpServletRequest request) {
        String authHeaderValue = request.getHeader("Authorization");
        if (authHeaderValue != null) {
            return authHeaderValue.replaceFirst("Bearer ", "");
        }

        return null;
    }

}
