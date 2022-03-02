/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.sec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.AuthUser;

@Component
public class UsersProvider {

	private Resource resource;

    private static final String DEFAULT_USERS_RESOURCE = "classpath:/users.yml";

    @Value("${security.usersFile:}")
    private String usersFilePath;

    @Autowired
    private AppContextProvider provider;

	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(UsersProvider.class);

	@PostConstruct
	private void init() {

        resource = getUsersResource();

		Yaml yaml = new Yaml(new Constructor(UsersProvider.class));
		try {
			UsersProvider data = (UsersProvider) yaml.load(resource.getInputStream());
			this.users = data.users;
		} catch (IOException e) {
			LogHub.error(null, logger, "exception loading auth users resource");
			users = new ArrayList<AuthUser>();
		}

	}

    private Resource getUsersResource() {
        Resource users = null;
        boolean noExternalFound = StringUtils.isBlank(usersFilePath);
        if (!noExternalFound) {
            users = provider.getApplicationContext().getResource("file:" + usersFilePath);
            noExternalFound = !users.exists();
            if (noExternalFound) {
                LogHub.info(null, logger, "external users file {} not exist", usersFilePath);
            }
        }

        if (noExternalFound) {
            LogHub.info(null, logger,
                    "not found any valid external users file...loading the default {}",
                    DEFAULT_USERS_RESOURCE);
            users = provider.getApplicationContext().getResource(DEFAULT_USERS_RESOURCE);
        } else {
            LogHub.info(null, logger, "loaded external users file {}", usersFilePath);
        }
        return users;
    }

	private List<AuthUser> users;

	public List<AuthUser> getUsers() {
		return users;
	}

	public void setUsers(List<AuthUser> users) {
		this.users = users;
	}

    public String getUsersFilePath() {
        return usersFilePath;
    }

    public void setUsersFilePath(String usersFilePath) {
        this.usersFilePath = usersFilePath;
    }

}
