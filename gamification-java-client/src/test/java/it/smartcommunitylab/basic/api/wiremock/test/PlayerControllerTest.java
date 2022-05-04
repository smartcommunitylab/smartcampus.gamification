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
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.basic.api.PlayerControllerApi;
import it.smartcommunitylab.model.ChallengeConcept;
import it.smartcommunitylab.model.ChallengeInvitation;
import it.smartcommunitylab.model.ComplexSearchQuery;
import it.smartcommunitylab.model.Inventory;
import it.smartcommunitylab.model.ItemChoice;
import it.smartcommunitylab.model.PagePlayerStateDTO;
import it.smartcommunitylab.model.PlayerBlackList;
import it.smartcommunitylab.model.PlayerStateDTO;
import it.smartcommunitylab.model.RawSearchQuery;
import it.smartcommunitylab.model.WrapperQuery;
import it.smartcommunitylab.model.ext.PlayerLevel;
import it.smartcommunitylab.model.ext.TeamDTO;

public class PlayerControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	@Test
	public void readPlayerState() throws IOException, ApiException {

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
	
		String mResponse = "{\"gameId\":\"mockGameId\",\"playerId\":\"mockPlayerId\"}";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "?readChallenges=true"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		PlayerStateDTO result = api.readPlayerUsingGET(gameId, playerId, true, null, null);

		assertEquals(result.getGameId(), gameId);
	}

	@Test
	public void acceptChallengeUsingPOSTTest() throws ApiException {

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String challengeName = "mockChallenge";
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";

		String mResponse = "{\"completed\": true,\"dateCompleted\":\"1474129837683\",\"end\":\"1474129837683\",\"fields\": {},\"forced\":true,\"id\":\"string\",\"modelName\":\"string\",\"name\":\"string\",\"origin\":\"string\",\"priority\": 0,\"start\":\"1474129837683\",\"state\": \"PROPOSED\", \"stateDate\":{}}";

		stubFor(post(
				urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/challenges/" + challengeName + "/accept"))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		ChallengeConcept challengeConcept = api.acceptChallengeUsingPOST(gameId, playerId, challengeName);

		assertEquals(challengeConcept.getState().name(), "PROPOSED");

	}

	/**
	 * Activate a choice
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void activateChoiceUsingPOSTTest() throws ApiException {
		ItemChoice choice = new ItemChoice();
		choice.setName("mockChoice");
		choice.setType(ItemChoice.TypeEnum.CHALLENGE_MODEL);
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "{\"challengeActivationActions\": 0,\"challengeChoices\": [{\"modelName\": \"string\",\"state\": \"AVAILABLE\"}]}";

		stubFor(post(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/inventory/activate"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		Inventory inventory = api.activateChoiceUsingPOST(gameId, playerId, choice);

		assertEquals(inventory.getChallengeChoices().get(0).getState().getValue(), "AVAILABLE");
	}

	/**
	 * Add another player to challenge block list
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void blockPlayerUsingPOSTTest() throws ApiException {
		String otherPlayerId = "mockOtherPlayerId";
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "{ \"blockedPlayers\": [ \"mockOtherPlayerId\" ], \"gameId\": \"mockGameId\", \"id\": \"string\", \"playerId\": \"mockPlayerId\"}";

		stubFor(post(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/block/" + otherPlayerId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		PlayerBlackList result = api.blockPlayerUsingPOST(gameId, playerId, otherPlayerId);

		assertEquals(result.getBlockedPlayers().get(0), otherPlayerId);

	}

	/**
	 * Get player challenges
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void getPlayerChallengeUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "[{\"completed\": true,\"dateCompleted\":\"1474129837683\",\"end\":\"1474129837683\",\"fields\": {},\"forced\":true,\"id\":\"string\",\"modelName\":\"string\",\"name\":\"string\",\"origin\":\"string\",\"priority\": 0,\"start\":\"1474129837683\",\"state\": \"PROPOSED\", \"stateDate\":{}}]";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/challenges"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<ChallengeConcept> result = api.getPlayerChallengeUsingGET(gameId, playerId);

		assertEquals(result.get(0).getState().name(), "PROPOSED");

	}

	/**
	 * inviteIntoAChallenge
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void inviteIntoAChallengeUsingPOSTTest() throws ApiException {

		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		ChallengeInvitation invitation = new ChallengeInvitation();
		invitation.setGameId(gameId);
		invitation.setChallengeModelName("mockChallengeModel");

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "{ \"challengeEnd\": \"2018-12-14T07:50:43.324Z\",  \"challengeModelName\": \"mockChallengeModel\",  \"challengeName\": \"string\",  \"challengePointConcept\": {    \"name\": \"string\",    \"period\": \"string\"  },\"challengeStart\": \"2018-12-14T07:50:43.324Z\",\"challengeTarget\": 0,\"gameId\": \"string\",\"guests\": [	{      \"playerId\": \"string\"    }],\"proposer\": {\"playerId\": \"string\"},\"reward\": {\"bonusScore\": {},\"calculationPointConcept\": {\"name\": \"string\",\"period\": \"string\"},\"percentage\": 0,\"targetPointConcept\": {\"name\": \"string\",\"period\": \"string\"},\"threshold\": 0}}";

		stubFor(post(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/invitation"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		ChallengeInvitation result = api.inviteIntoAChallengeUsingPOST(invitation, gameId, playerId);

		assertEquals(result.getChallengeModelName(), "mockChallengeModel");

	}

	/**
	 * Get player inventory
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readInventoryUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "{\"challengeActivationActions\": 0,\"challengeChoices\": [{\"modelName\": \"string\",\"state\": \"AVAILABLE\"}]}";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/inventory"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		Inventory result = api.readInventoryUsingGET(gameId, playerId);

		assertEquals(result.getChallengeChoices().get(0).getState().getValue(), "AVAILABLE");

	}

	/**
	 * Get player levels
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readLevelsUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "[{\"endLevelScore\": 0,    \"levelIndex\": 0,    \"levelName\": \"string\",    \"levelValue\": \"string\",    \"pointConcept\": \"green leaves\",    \"startLevelScore\": 0,    \"toNextLevel\": 0  }]";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/levels"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<PlayerLevel> result = api.readLevelsUsingGET(gameId, playerId);

		assertEquals(result.get(0).getPointConcept(), "green leaves");

	}

	/**
	 * Get player black list of other players
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readPlayerBlackListUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		String otherPlayerId = "mockOtherPlayerId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "{ \"blockedPlayers\": [ \"mockOtherPlayerId\" ], \"gameId\": \"mockGameId\", \"id\": \"string\", \"playerId\": \"mockPlayerId\"}";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/blacklist"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		PlayerBlackList response = api.readPlayerBlackListUsingGET(gameId, playerId);

		assertEquals(response.getBlockedPlayers().get(0), otherPlayerId);

	}

	/**
	 * Get availabe challengers for the player
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readSystemPlayerStateUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		String conceptName = "mockConcept";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "[\"mockChallenger\"]";

		stubFor(get(
				urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/challengers?conceptName=" + conceptName))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<String> response = api.readSystemPlayerStateUsingGET(gameId, playerId, conceptName);

		assertEquals(response.contains("mockChallenger"), true);

	}

	/**
	 * Get player teams
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readTeamsByMemberUsingGET1Test() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "[{ \"customData\": {}, \"gameId\":" + gameId
				+ ", \"inventory\": {\"challengeActivationActions\": 0,\"challengeChoices\": [{\"modelName\": \"string\",\"state\": \"AVAILABLE\"}]},\"levels\": [{\"endLevelScore\": 0,\"levelIndex\": 0,\"levelName\": \"string\",\"levelValue\": \"string\",\"pointConcept\": \"string\",\"startLevelScore\": 0,\"toNextLevel\": 0}],\"members\": [\"string\"],\"name\": \"string\",\"playerId\": \"string\",\"state\": {}}]";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/teams"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<TeamDTO> response = api.readTeamsByMemberUsingGET1(gameId, playerId);

		assertEquals(response.get(0).getGameId(), gameId);
	}

	/**
	 * Search player states
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 * @throws JsonProcessingException
	 */
	@Test
	public void searchByQueryUsingPOSTTest() throws ApiException, JsonProcessingException {
		WrapperQuery query = new WrapperQuery();
		query.setComplexQuery(new ComplexSearchQuery());
		query.setRawQuery(new RawSearchQuery());

		String page = "1";
		String size = "10";
		String gameId = "mockGameId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "{\"content\": [{ \"customData\": {}, \"gameId\": \"mockGameId\", \"inventory\": { \"challengeActivationActions\": 0, \"challengeChoices\": [ { \"modelName\": \"string\", \"state\": \"AVAILABLE\" } ] }, \"levels\": [ { \"endLevelScore\": 0, \"levelIndex\": 0, \"levelName\": \"string\", \"levelValue\": \"string\", \"pointConcept\": \"string\", \"startLevelScore\": 0, \"toNextLevel\": 0 } ], \"playerId\": \"mockPlayerId\", \"state\": {} } ], \"first\": true, \"firstPage\": true, \"last\": true, \"lastPage\": true, \"number\": 0, \"numberOfElements\": 16, \"size\": 0, \"sort\": {}, \"totalElements\": 16, \"totalPages\": 2 }";

		stubFor(post(urlEqualTo("/data/game/" + gameId + "/player/search?page=1&size=10"))
				.withRequestBody(equalToJson(
						"{\"complexQuery\":{\"query\":{},\"sortItems\":[]},\"rawQuery\":{\"sortItems\":[]}}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		PagePlayerStateDTO response = api.searchByQueryUsingPOST(gameId, query, page, size);

		assertEquals(response.getContent().get(0).getGameId(), gameId);

	}

	/**
	 * Unblock another player from challenge block list
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void unBlockPlayerUsingPOSTTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		String otherPlayerId = "mockOtherPlayerId";
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String mResponse = "{ \"blockedPlayers\": [], \"gameId\": \"mockGameId\", \"id\": \"string\", \"playerId\": \"mockPlayerId\"}";

		stubFor(post(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/unblock/" + otherPlayerId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		PlayerBlackList response = api.unBlockPlayerUsingPOST(gameId, playerId, otherPlayerId);

		assertEquals(response.getBlockedPlayers().isEmpty(), true);

	}

}