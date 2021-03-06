/**
 * Copyright 2018-2019 SmartCommunity Lab(FBK-ICT).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
 * Gamification Engine API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package it.smartcommunitylab.basic.api;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.auth.HttpBasicAuth;
import it.smartcommunitylab.model.Notification;

/**
 * API tests for NotificationControllerApi
 */
 @Ignore
public class NotificationControllerApiTest {

	private final NotificationControllerApi api = new NotificationControllerApi();
	private ApiClient apiClient;
	private final String userName = "long-rovereto";
	private final String password = "rov";
	private String baseUrl = "http://localhost:6060/gamification";
	private String gameId = "5b7a885149c95d50c5f9d442";
	private String playerId = "8";

	@Before
	public void init() {
		apiClient = new ApiClient(baseUrl);

		// Configure OAuth2 access token for authorization: oauth2
		// OAuth oauth2 = (OAuth) apiClient.getAuthentication("oauth2");
		// oauth2.setAccessToken("YOUR_ACCESS_TOKEN");

		// Configure basic auth.
		HttpBasicAuth basic = (HttpBasicAuth) apiClient.getAuthentication("basic");
		basic.setUsername(userName);
		basic.setPassword(password);

		api.setApiClient(apiClient);
	}

	/**
	 * Get game notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Test
	public void readNotificationUsingGET2Test() throws ApiException, IllegalArgumentException, ClassNotFoundException,
			JsonParseException, JsonMappingException, IOException {
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "15";
		List<Notification> response = api.readNotificationUsingGET2(gameId, fromTs, toTs, includeTypes, excludeTypes,
				page, size);

		System.out.println(response.size());

		// TODO: test validations
	}

	/**
	 * Get player notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 * @throws IOException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 */
	@Test
	public void readPlayerNotificationGroupedUsingGETTest() throws ApiException, JsonParseException, JsonMappingException, IOException, IllegalArgumentException, ClassNotFoundException {
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "15";
		Map<String, Collection<Notification>> response = api.readPlayerNotificationGroupedUsingGET(gameId, playerId,
				fromTs, toTs, includeTypes, excludeTypes, page, size);

		System.out.println(response.size());
	}

	/**
	 * Get player notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws IllegalArgumentException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@Test
	public void readPlayerNotificationUsingGETTest() throws ApiException, JsonParseException, JsonMappingException,
			IllegalArgumentException, ClassNotFoundException, IOException {
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "15";
		List<Notification> response = api.readPlayerNotificationUsingGET(gameId, playerId, fromTs, toTs, includeTypes,
				excludeTypes, page, size);

		System.out.println(response.size());
	}

	/**
	 * Get team notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws IllegalArgumentException 
	 * @throws JsonMappingException 
	 * @throws JsonParseException 
	 */
	@Test
	public void readTeamNotificationUsingGETTest() throws ApiException, JsonParseException, JsonMappingException, IllegalArgumentException, ClassNotFoundException, IOException {
		String teamId = "testTeam";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "15";
		List<Notification> response = api.readTeamNotificationUsingGET(gameId, playerId, fromTs, toTs, includeTypes,
				excludeTypes, page, size);

		System.out.println(response.size());
	}

}
