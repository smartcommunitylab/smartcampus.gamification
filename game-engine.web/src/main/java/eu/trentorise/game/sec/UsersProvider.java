package eu.trentorise.game.sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import eu.trentorise.game.model.AuthUser;

@Component
public class UsersProvider {

	@Value("classpath:/users.yml")
	private Resource resource;

	private static final Logger logger = org.slf4j.LoggerFactory
			.getLogger(UsersProvider.class);

	@PostConstruct
	@SuppressWarnings("unused")
	private void init() {
		Yaml yaml = new Yaml(new Constructor(UsersProvider.class));
		try {
			UsersProvider data = (UsersProvider) yaml.load(resource
					.getInputStream());
			this.users = data.users;
		} catch (IOException e) {
			logger.error("exception loading auth users resource");
			users = new ArrayList<AuthUser>();
		}

	}

	private List<AuthUser> users;

	public List<AuthUser> getUsers() {
		return users;
	}

	public void setUsers(List<AuthUser> users) {
		this.users = users;
	}

}
