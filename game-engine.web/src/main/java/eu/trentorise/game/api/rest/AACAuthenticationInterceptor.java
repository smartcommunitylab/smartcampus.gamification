package eu.trentorise.game.api.rest;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import it.smartcommunitylab.aac.AACException;
import it.smartcommunitylab.aac.AACProfileService;
import it.smartcommunitylab.aac.AACService;
import it.smartcommunitylab.aac.model.AccountProfile;

@Component
@Profile("platform")
public class AACAuthenticationInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger =
            LoggerFactory.getLogger(AACAuthenticationInterceptor.class);

    @Autowired
    private AACProfileService aacProfileService;

    @Autowired
    private AACService aacService;

    @Value("${aac.gamification.scopes}")
    private String gamificationScopes;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws IOException {


        String token = extractToken(request);
        boolean passRequest = true;
        if (token != null) {
            AccountProfile profile = null;
            String email = null;
            try {
                profile = aacProfileService.findAccountProfile(token);
                email = profile.getAttribute("google", "email");
            } catch (SecurityException | AACException e) {
                logger.warn("Exception in findAccountProfile of aacProfileService", e.getMessage());
            }

            if (!hasGamificationScope(token)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN,
                        "not the right security scope");
                logger.warn("Token has not scope: {}", gamificationScopes);
                passRequest = false;
            } else {
                SecurityContextHolder.getContext()
                        .setAuthentication(new UsernamePasswordAuthenticationToken(email, null));
                // aggiungere i roles
            }
        } else {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "request should be authenticated");
            logger.warn("Request without token");
            passRequest = false;
        }

        return passRequest;
    }

    private String extractToken(HttpServletRequest request) {
        String authHeaderValue = request.getHeader("Authorization");
        if (authHeaderValue != null) {
            return authHeaderValue.replaceFirst("Bearer ", "");
        }

        return null;
    }

    private boolean hasGamificationScope(String token) {
        try {
                return aacService.isTokenApplicable(token, gamificationScopes);
        } catch (AACException e) {
            logger.error("AACService exception: ", e);
            return false;
        }
    }

}
