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
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.basic.api.GameControllerApi;
import it.smartcommunitylab.model.GameDTO;
import it.smartcommunitylab.model.GameStatistics;
import it.smartcommunitylab.model.LevelDTO;

public class GameControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	/**
	 * Delete a level
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void deleteLevelUsingDELETETest() throws ApiException {
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		GameControllerApi api = new GameControllerApi();

		String gameId = "mockGameId";
		String levelName = "mockLevelName";

		String mResponse = "true";

		stubFor(delete(urlEqualTo("/model/game/" + gameId + "/level/" + levelName))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		Boolean response = api.deleteLevelUsingDELETE(gameId, levelName);

		assertEquals(response, true);

	}

	/**
	 * Get actions
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readAllActionUsingGETTest() throws ApiException {
		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		GameControllerApi api = new GameControllerApi();

		String gameId = "mockGameId";

		String mResponse = "[\"mockAction\"]";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/action"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<String> response = api.readAllActionUsingGET(gameId);

		assertEquals(response.size(), 1);

	}

	/**
	 * Get game statistics
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readGameStatisticsUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String pointConceptName = "mockLeaves";
		String periodName = "mockWeekly";
		Long timestamp = 1542495600000L;
		String periodIndex = null;
		Integer page = 1;
		Integer size = 15;

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		GameControllerApi api = new GameControllerApi();

		String mResponse = "[{\"id\": \"5bf6c4753b1e5f8a3e7e7946\",\"gameId\": \"" + gameId
				+ "\",\"pointConceptName\": \"" + pointConceptName
				+ " \",\"periodName\": \"weekly\",\"startDate\": 1542409200000,\"endDate\": 1543014000000,\"periodIndex\": \"2018-11-17T00:00:00\",\"average\": 0,\"variance\": 0,\"quantiles\": {    \"0\": 0,    \"1\": 0,    \"2\": 0,    \"3\": 0,    \"4\": 0,    \"5\": 0,    \"6\": 0,    \"7\": 0,    \"8\": 0,    \"9\": 0},\"lastUpdated\": 1542983400002 }]";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/statistics?pointConceptName=" + pointConceptName
				+ "&periodName=" + periodName + "&timestamp=" + timestamp + "&page=" + page + "&size=" + size))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<GameStatistics> response = api.readGameStatisticsUsingGET(gameId, pointConceptName, periodName, timestamp,
				periodIndex, page, size);

		assertEquals(gameId, response.get(0).getGameId());
	}

	/**
	 * Read game definition
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readGameUsingGET1Test() throws ApiException {
		String gameId = "mockGameId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		GameControllerApi api = new GameControllerApi();

		String mResponse = "{   \"id\": \"" + gameId
				+ "\",   \"name\": \"play and go 2018\",   \"owner\": \"long-rovereto\",   \"domain\": \"game\",   \"actions\": [     \"poi_reached\",     \"end_survey_complete\",     \"checkin\",     \"checkin_notte_dei_ricercatori\",     \"last_survey_complete\",     \"app_sent_recommandation\",     \"start_survey_complete\",     \"save_itinerary\",     \"checkin_new_user_Trento_Fiera\"   ],   \"rules\": [     {       \"id\": \"db://5b7a885149c95d50c5f9d449\",       \"content\": null,       \"name\": \"finalClassificationBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44d\",       \"content\": null,       \"name\": \"survey\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d450\",       \"content\": null,       \"name\": \"challenge_repetitiveBehaviour\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d443\",       \"content\": null,       \"name\": \"weekClassificationBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44c\",       \"content\": null,       \"name\": \"challenge_percentageIncrement\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44b\",       \"content\": null,       \"name\": \"challenge_absoluteIncrement\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44e\",       \"content\": null,       \"name\": \"challenge_checkin\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44f\",       \"content\": null,       \"name\": \"challenge_nextBadge\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44a\",       \"content\": null,       \"name\": \"constants\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d446\",       \"content\": null,       \"name\": \"mode-counters\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d445\",       \"content\": null,       \"name\": \"poiPoints\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d447\",       \"content\": null,       \"name\": \"specialBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d448\",       \"content\": null,       \"name\": \"greenPoints\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d444\",       \"content\": null,       \"name\": \"greenBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d451\",       \"content\": null,       \"name\": \"pippo\"     }   ],   \"levels\": [     {       \"name\": \"Green Warrior\",       \"thresholds\": [         {           \"name\": \"Green Starter\",           \"value\": 0,           \"config\": null         },         {           \"name\": \"Green Follower\",           \"value\": 200,           \"config\": null         },         {           \"name\": \"Green Lover\",           \"value\": 500,           \"config\": null         },         {           \"name\": \"Green Influencer\",           \"value\": 900,           \"config\": null         },         {           \"name\": \"Green Soldier\",           \"value\": 1500,           \"config\": null         },         {           \"name\": \"Green Master\",           \"value\": 2200,           \"config\": null         },         {           \"name\": \"Green Ambassador\",           \"value\": 3500,           \"config\": null         },         {           \"name\": \"Green Warrior\",           \"value\": 5000,           \"config\": null         },         {           \"name\": \"Green Veteran\",           \"value\": 10000,           \"config\": null         },         {           \"name\": \"Green Guru\",           \"value\": 20000,           \"config\": null         },         {           \"name\": \"Green God\",           \"value\": 40000,           \"config\": null         }       ],       \"pointConcept\": \"green leaves\"     }   ],   \"expiration\": 0,   \"terminated\": false,   \"classificationTask\": [     {       \"type\": \"incremental\",       \"name\": \"week classification green\",       \"gameId\": \"5b7a885149c95d50c5f9d442\",       \"itemsToNotificate\": 3,       \"itemType\": \"green leaves\",       \"classificationName\": \"week classification green\",       \"periodName\": \"weekly\",       \"delayValue\": 3,       \"delayUnit\": \"DAY\"     },     {       \"type\": \"general\",       \"name\": \"global classification green\",       \"gameId\": null,       \"itemsToNotificate\": 1,       \"itemType\": \"green leaves\",       \"classificationName\": \"global classification green\",       \"cronExpression\": \"\"    }   ],   \"pointConcept\": [     {       \"id\": \"1\",       \"name\": \"Bus_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"green leaves\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"PandR_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Transit_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"BikeSharing_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bike_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"BikeSharing_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Car_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Walk_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Train_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Walk_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Recommendations\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bus_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Car_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Train_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"NoCar_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bike_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"ZeroImpact_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     }   ],   \"badgeCollectionConcept\": [     {       \"id\": \"1\",       \"name\": \"public transport aficionado\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"sustainable life\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"recommendations\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"bike aficionado\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"leaderboard top 3\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"park and ride pioneer\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"bike sharing pioneer\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"green leaves\",       \"badgeEarned\": []     }   ],   \"challengeChoiceConfig\": {     \"deadline\": null } }";

		stubFor(get(urlEqualTo("/model/game/" + gameId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		GameDTO response = api.readGameUsingGET1(gameId);

		assertEquals(gameId, response.getId());

	}

	/**
	 * Get games
	 *
	 * Get all the game definitions of a user
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readGamesUsingGET1Test() throws ApiException {

		String gameId = "mockGameId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		GameControllerApi api = new GameControllerApi();

		String mResponse = "[{   \"id\": \"" + gameId
				+ "\",   \"name\": \"play and go 2018\",   \"owner\": \"long-rovereto\",   \"domain\": \"game\",   \"actions\": [     \"poi_reached\",     \"end_survey_complete\",     \"checkin\",     \"checkin_notte_dei_ricercatori\",     \"last_survey_complete\",     \"app_sent_recommandation\",     \"start_survey_complete\",     \"save_itinerary\",     \"checkin_new_user_Trento_Fiera\"   ],   \"rules\": [     {       \"id\": \"db://5b7a885149c95d50c5f9d449\",       \"content\": null,       \"name\": \"finalClassificationBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44d\",       \"content\": null,       \"name\": \"survey\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d450\",       \"content\": null,       \"name\": \"challenge_repetitiveBehaviour\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d443\",       \"content\": null,       \"name\": \"weekClassificationBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44c\",       \"content\": null,       \"name\": \"challenge_percentageIncrement\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44b\",       \"content\": null,       \"name\": \"challenge_absoluteIncrement\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44e\",       \"content\": null,       \"name\": \"challenge_checkin\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44f\",       \"content\": null,       \"name\": \"challenge_nextBadge\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44a\",       \"content\": null,       \"name\": \"constants\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d446\",       \"content\": null,       \"name\": \"mode-counters\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d445\",       \"content\": null,       \"name\": \"poiPoints\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d447\",       \"content\": null,       \"name\": \"specialBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d448\",       \"content\": null,       \"name\": \"greenPoints\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d444\",       \"content\": null,       \"name\": \"greenBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d451\",       \"content\": null,       \"name\": \"pippo\"     }   ],   \"levels\": [     {       \"name\": \"Green Warrior\",       \"thresholds\": [         {           \"name\": \"Green Starter\",           \"value\": 0,           \"config\": null         },         {           \"name\": \"Green Follower\",           \"value\": 200,           \"config\": null         },         {           \"name\": \"Green Lover\",           \"value\": 500,           \"config\": null         },         {           \"name\": \"Green Influencer\",           \"value\": 900,           \"config\": null         },         {           \"name\": \"Green Soldier\",           \"value\": 1500,           \"config\": null         },         {           \"name\": \"Green Master\",           \"value\": 2200,           \"config\": null         },         {           \"name\": \"Green Ambassador\",           \"value\": 3500,           \"config\": null         },         {           \"name\": \"Green Warrior\",           \"value\": 5000,           \"config\": null         },         {           \"name\": \"Green Veteran\",           \"value\": 10000,           \"config\": null         },         {           \"name\": \"Green Guru\",           \"value\": 20000,           \"config\": null         },         {           \"name\": \"Green God\",           \"value\": 40000,           \"config\": null         }       ],       \"pointConcept\": \"green leaves\"     }   ],   \"expiration\": 0,   \"terminated\": false,   \"classificationTask\": [     {       \"type\": \"incremental\",       \"name\": \"week classification green\",       \"gameId\": \"5b7a885149c95d50c5f9d442\",       \"itemsToNotificate\": 3,       \"itemType\": \"green leaves\",       \"classificationName\": \"week classification green\",       \"periodName\": \"weekly\",       \"delayValue\": 3,       \"delayUnit\": \"DAY\"     },     {       \"type\": \"general\",       \"name\": \"global classification green\",       \"gameId\": null,       \"itemsToNotificate\": 1,       \"itemType\": \"green leaves\",       \"classificationName\": \"global classification green\",       \"cronExpression\": \"\"    }   ],   \"pointConcept\": [     {       \"id\": \"1\",       \"name\": \"Bus_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"green leaves\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"PandR_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Transit_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"BikeSharing_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bike_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"BikeSharing_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Car_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Walk_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Train_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Walk_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Recommendations\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bus_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Car_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Train_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"NoCar_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bike_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"ZeroImpact_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     }   ],   \"badgeCollectionConcept\": [     {       \"id\": \"1\",       \"name\": \"public transport aficionado\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"sustainable life\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"recommendations\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"bike aficionado\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"leaderboard top 3\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"park and ride pioneer\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"bike sharing pioneer\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"green leaves\",       \"badgeEarned\": []     }   ],   \"challengeChoiceConfig\": {     \"deadline\": null } }]";

		stubFor(get(urlEqualTo("/model/game"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<GameDTO> response = api.readGamesUsingGET1();

		assertEquals(gameId, response.get(0).getId());
	}

	/**
	 * Save a game
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void saveGameUsingPOST2Test() throws ApiException {
		String gameId = "mockGameId";
		GameDTO game = new GameDTO();
		game.setId(gameId);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		GameControllerApi api = new GameControllerApi();

		String mResponse = "{   \"id\": \"" + gameId
				+ "\",   \"name\": \"play and go 2018\",   \"owner\": \"long-rovereto\",   \"domain\": \"game\",   \"actions\": [     \"poi_reached\",     \"end_survey_complete\",     \"checkin\",     \"checkin_notte_dei_ricercatori\",     \"last_survey_complete\",     \"app_sent_recommandation\",     \"start_survey_complete\",     \"save_itinerary\",     \"checkin_new_user_Trento_Fiera\"   ],   \"rules\": [     {       \"id\": \"db://5b7a885149c95d50c5f9d449\",       \"content\": null,       \"name\": \"finalClassificationBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44d\",       \"content\": null,       \"name\": \"survey\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d450\",       \"content\": null,       \"name\": \"challenge_repetitiveBehaviour\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d443\",       \"content\": null,       \"name\": \"weekClassificationBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44c\",       \"content\": null,       \"name\": \"challenge_percentageIncrement\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44b\",       \"content\": null,       \"name\": \"challenge_absoluteIncrement\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44e\",       \"content\": null,       \"name\": \"challenge_checkin\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44f\",       \"content\": null,       \"name\": \"challenge_nextBadge\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d44a\",       \"content\": null,       \"name\": \"constants\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d446\",       \"content\": null,       \"name\": \"mode-counters\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d445\",       \"content\": null,       \"name\": \"poiPoints\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d447\",       \"content\": null,       \"name\": \"specialBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d448\",       \"content\": null,       \"name\": \"greenPoints\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d444\",       \"content\": null,       \"name\": \"greenBadges\"     },     {       \"id\": \"db://5b7a885149c95d50c5f9d451\",       \"content\": null,       \"name\": \"pippo\"     }   ],   \"levels\": [     {       \"name\": \"Green Warrior\",       \"thresholds\": [         {           \"name\": \"Green Starter\",           \"value\": 0,           \"config\": null         },         {           \"name\": \"Green Follower\",           \"value\": 200,           \"config\": null         },         {           \"name\": \"Green Lover\",           \"value\": 500,           \"config\": null         },         {           \"name\": \"Green Influencer\",           \"value\": 900,           \"config\": null         },         {           \"name\": \"Green Soldier\",           \"value\": 1500,           \"config\": null         },         {           \"name\": \"Green Master\",           \"value\": 2200,           \"config\": null         },         {           \"name\": \"Green Ambassador\",           \"value\": 3500,           \"config\": null         },         {           \"name\": \"Green Warrior\",           \"value\": 5000,           \"config\": null         },         {           \"name\": \"Green Veteran\",           \"value\": 10000,           \"config\": null         },         {           \"name\": \"Green Guru\",           \"value\": 20000,           \"config\": null         },         {           \"name\": \"Green God\",           \"value\": 40000,           \"config\": null         }       ],       \"pointConcept\": \"green leaves\"     }   ],   \"expiration\": 0,   \"terminated\": false,   \"classificationTask\": [     {       \"type\": \"incremental\",       \"name\": \"week classification green\",       \"gameId\": \"5b7a885149c95d50c5f9d442\",       \"itemsToNotificate\": 3,       \"itemType\": \"green leaves\",       \"classificationName\": \"week classification green\",       \"periodName\": \"weekly\",       \"delayValue\": 3,       \"delayUnit\": \"DAY\"     },     {       \"type\": \"general\",       \"name\": \"global classification green\",       \"gameId\": null,       \"itemsToNotificate\": 1,       \"itemType\": \"green leaves\",       \"classificationName\": \"global classification green\",       \"cronExpression\": \"\"    }   ],   \"pointConcept\": [     {       \"id\": \"1\",       \"name\": \"Bus_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"green leaves\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"PandR_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Transit_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"BikeSharing_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bike_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"BikeSharing_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Car_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Walk_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Train_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Walk_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Recommendations\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bus_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Car_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Train_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"NoCar_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"Bike_Km\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     },     {       \"id\": \"1\",       \"name\": \"ZeroImpact_Trips\",       \"score\": 0,       \"periods\": {         \"weekly\": {           \"start\": null,           \"period\": 604800000,           \"identifier\": \"weekly\",           \"capacity\": 0,           \"instances\": {}         },         \"daily\": {           \"start\": null,           \"period\": 86400000,           \"identifier\": \"daily\",           \"capacity\": 7,           \"instances\": {}         }       }     }   ],   \"badgeCollectionConcept\": [     {       \"id\": \"1\",       \"name\": \"public transport aficionado\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"sustainable life\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"recommendations\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"bike aficionado\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"leaderboard top 3\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"park and ride pioneer\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"bike sharing pioneer\",       \"badgeEarned\": []     },     {       \"id\": \"1\",       \"name\": \"green leaves\",       \"badgeEarned\": []     }   ],   \"challengeChoiceConfig\": {     \"deadline\": null } }";

		stubFor(post(urlEqualTo("/model/game")).withRequestBody(equalToJson("{\"id\":\"" + gameId + "\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		GameDTO response = api.saveGameUsingPOST2(game);

		assertEquals(gameId, response.getId());

	}

	/**
	 * Save a level
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void saveLevelUsingPOSTTest() throws ApiException {
		String gameId = "mockGameId";
		String levelName = "mockLevel";
		LevelDTO level = new LevelDTO();
		level.setName(levelName);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		GameControllerApi api = new GameControllerApi();

		String mResponse = "{   \"name\": \"" + levelName
				+ "\",   \"pointConcept\": \"string\",   \"thresholds\": [     {       \"config\": {         \"availableModels\": [           \"string\"         ],         \"choices\": 0       },       \"name\": \"string\",       \"value\": 0     }   ] }";

		stubFor(post(urlEqualTo("/model/game/" + gameId + "/level"))
				.withRequestBody(equalToJson("{\"name\":\"" + levelName + "\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		LevelDTO response = api.saveLevelUsingPOST(gameId, level);

		assertEquals(levelName, response.getName());

	}

}
