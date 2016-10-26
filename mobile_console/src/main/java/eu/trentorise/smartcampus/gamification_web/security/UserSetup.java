package eu.trentorise.smartcampus.gamification_web.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import eu.trentorise.smartcampus.gamification_web.models.ConsoleUserData;

@Component
public class UserSetup {

	@Value("classpath:/user-conf.yml")
	private Resource resource;
	
	@PostConstruct
	public void init() throws IOException {
		Yaml yaml = new Yaml(new Constructor(UserSetup.class));
		UserSetup data = (UserSetup) yaml.load(resource.getInputStream());
		this.users = data.users;
	}
	
	private List<ConsoleUserData> users;
	private Map<String,ConsoleUserData> usersMap;
	
	/**
	 * @return the users
	 */
	public List<ConsoleUserData> getUsers() {
		return users;
	}

	/**
	 * @param users the users to set
	 */
	public void setUsers(List<ConsoleUserData> users) {
		this.users = users;
	}
	
	@Override
	public String toString() {
		return "UserSetup [users=" + users + "]";
	}
	
	public ConsoleUserData findUserByUsername(String username) {
		if (usersMap == null) {
			usersMap = new HashMap<String, ConsoleUserData>();
			for (ConsoleUserData user : users) {
				usersMap.put(user.getUsername(), user);
			}
		}
		return usersMap.get(username);
	}

}
