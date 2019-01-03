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

package it.smartcommunitylab.basic.api.wiremock.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.basic.api.NotificationControllerApi;
import it.smartcommunitylab.model.CollectionNotification;
import it.smartcommunitylab.model.Notification;

@Ignore
public class NotificationControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

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
	public void readNotificationUsingGET2Test() throws ApiException, IllegalArgumentException, ClassNotFoundException, JsonParseException, JsonMappingException, IOException {
		String gameId = "mockGameId";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "[{ \"gameId\": \"" + gameId + "\"}]";

		stubFor(get(urlEqualTo("/notification/game/" + gameId + "?page=" + page + "&size=" + size))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<Notification> response = api.readNotificationUsingGET2(gameId, fromTs, toTs, includeTypes, excludeTypes,
				page, size);

		assertEquals(gameId, response.get(0).getGameId());
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
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "{ \"mockGameId\": { \"empty\": \"test\" } }";

		stubFor(get(urlEqualTo(
				"/notification/game/" + gameId + "/player/" + playerId + "/grouped?page=" + page + "&size=" + size))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		Map<String, Collection<Notification>> response = api.readPlayerNotificationGroupedUsingGET(gameId, playerId,
				fromTs, toTs, includeTypes, excludeTypes, page, size);

		assertEquals(false, response.get(gameId).isEmpty());

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
	public void readPlayerNotificationUsingGETTest() throws ApiException, JsonParseException, JsonMappingException, IllegalArgumentException, ClassNotFoundException, IOException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "[{ \"gameId\": \"" + gameId + "\"}]";

		stubFor(get(
				urlEqualTo("/notification/game/" + gameId + "/player/" + playerId + "?page=" + page + "&size=" + size))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<Notification> response = api.readPlayerNotificationUsingGET(gameId, playerId, fromTs, toTs, includeTypes,
				excludeTypes, page, size);

		assertEquals(gameId, response.get(0).getGameId());

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
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		String teamId = "mockTeam";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "[{ \"gameId\": \"" + gameId + "\", \"playerId\": \"" + playerId + "\"}]";

		stubFor(get(urlEqualTo("/notification/game/" + gameId + "/team/" + teamId + "?page=" + page + "&size=" + size))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<Notification> response = api.readTeamNotificationUsingGET(gameId, teamId, fromTs, toTs, includeTypes,
				excludeTypes, page, size);

		assertEquals(playerId, response.get(0).getPlayerId());

	}

}
