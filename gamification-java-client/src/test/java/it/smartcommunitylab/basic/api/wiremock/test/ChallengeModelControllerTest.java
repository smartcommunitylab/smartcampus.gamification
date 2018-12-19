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

import java.util.List;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.basic.ApiClient;
import it.smartcommunitylab.basic.ApiException;
import it.smartcommunitylab.basic.Configuration;
import it.smartcommunitylab.basic.api.ChallengeModelControllerApi;
import it.smartcommunitylab.model.ChallengeModel;

public class ChallengeModelControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	/**
	 * Get challenge models
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readChallengeModelsUsingGETTest() throws ApiException {
		String gameId = "mockGameId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ChallengeModelControllerApi api = new ChallengeModelControllerApi(client);

		String mResponse = "[   {     \"gameId\": \"" + gameId
				+ "\",     \"id\": \"string\",     \"name\": \"string\",     \"variables\": [       \"string\"     ]   } ]";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/challenge"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<ChallengeModel> response = api.readChallengeModelsUsingGET(gameId);

		assertEquals(gameId, response.get(0).getGameId());
	}

	/**
	 * Add challenge model
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void saveGameUsingPOSTTest() throws ApiException {
		String gameId = "mockGameId";
		ChallengeModel challengeModel = new ChallengeModel();
		challengeModel.setGameId(gameId);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ChallengeModelControllerApi api = new ChallengeModelControllerApi(client);

		String mResponse = "{ \"gameId\": \"" + gameId
				+ "\",     \"id\": \"string\",     \"name\": \"string\",     \"variables\": [       \"string\"     ]   }";

		stubFor(post(urlEqualTo("/model/game/" + gameId + "/challenge"))
				.withRequestBody(equalToJson("{\"gameId\": \"mockGameId\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		ChallengeModel response = api.saveGameUsingPOST(challengeModel, gameId);

		assertEquals(gameId, response.getGameId());

	}

}
