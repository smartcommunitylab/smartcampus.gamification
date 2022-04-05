package eu.trentorise.game.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Component
@Controller
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	
	@Autowired
	@Value("${oauth.serverUrl:}")
	private String oauthServerUrl;
	
	@Value("${server.singeLogoutUrl:}")
	private String singeLogoutUrl;

	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {

		String target = singeLogoutUrl.trim();
		if(target != null && !target.isEmpty()) {
			String URL = oauthServerUrl + "/logout?target=" + target;
			response.sendRedirect(URL);
		}
		response.setStatus(HttpStatus.OK.value());
	}
	
	@RequestMapping(value = "/singleLogout")
	public ModelAndView clearSession(HttpServletRequest request) {
		// clear session.
		request.getSession().invalidate();
		return new ModelAndView("redirect:" + "/login/");

	}

}
