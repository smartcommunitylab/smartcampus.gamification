package eu.trentorise.game.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class ProfileController {

	@Autowired
	Environment environment;

	@RequestMapping(method = RequestMethod.GET)
	public String[] getCurrentActiveProfiles() {
		return environment.getActiveProfiles();
	}
}
