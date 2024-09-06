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

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.basic.api.ClassificationControllerApi;
import it.smartcommunitylab.model.ClassificationBoard;
import it.smartcommunitylab.model.GeneralClassificationDTO;
import it.smartcommunitylab.model.IncrementalClassificationDTO;

@Ignore
public class ClassificationControllerTest {

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
	public void addClassificationTaskUsingPOSTTest() throws ApiException {
		String gameId = "mockGameId";
		GeneralClassificationDTO task = new GeneralClassificationDTO();
		task.setGameId(gameId);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "{     \"gameId\": \"" + gameId + "\"}";

		stubFor(post(urlEqualTo("/model/game/" + gameId + "/classification"))
				.withRequestBody(equalToJson("{\"gameId\": \"mockGameId\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		GeneralClassificationDTO response = api.addClassificationTaskUsingPOST(gameId, task);

		assertEquals(gameId, response.getGameId());
	}

	/**
	 * Add incremental classification definition
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void createIncrementalUsingPOSTTest() throws ApiException {
		String gameId = "mockGameId";
		IncrementalClassificationDTO classification = new IncrementalClassificationDTO();
		classification.setGameId(gameId);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "{     \"gameId\": \"" + gameId + "\"}";

		stubFor(post(urlEqualTo("/model/game/" + gameId + "/incclassification"))
				.withRequestBody(equalToJson("{\"gameId\": \"mockGameId\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		IncrementalClassificationDTO response = api.createIncrementalUsingPOST(gameId, classification);

		assertEquals(gameId, response.getGameId());

	}

	/**
	 * Read general classification board
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void getGeneralClassificationUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String classificationId = "general";
		String pointConceptName = "mockPointConcept";
		Integer page = 1;
		Integer size = 15;

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "{   \"board\": [     {       \"playerId\": \"string\",       \"score\": 0     }   ],   \"pointConceptName\": \""
				+ pointConceptName + "\",   \"type\": \"GENERAL\" }";

		stubFor(get(urlEqualTo(
				"/data/game/" + gameId + "/classification/" + classificationId + "?page=" + page + "&size=" + size))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		ClassificationBoard response = api.getGeneralClassificationUsingGET(gameId, classificationId, page, size);

		assertEquals(pointConceptName, response.getPointConceptName());

	}

	/**
	 * Read incremental classification board
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void getIncrementalClassificationUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String classificationId = "incremental";
		String pointConceptName = "mockPointConcept";
		Long timestamp = null;
		Integer periodInstanceIndex = null;
		Integer page = 1;
		Integer size = 15;

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "{   \"board\": [     {       \"playerId\": \"string\",       \"score\": 0     }   ],   \"pointConceptName\": \""
				+ pointConceptName + "\",   \"type\": \"GENERAL\" }";

		stubFor(get(urlEqualTo(
				"/data/game/" + gameId + "/incclassification/" + classificationId + "?page=" + page + "&size=" + size))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		ClassificationBoard response = api.getIncrementalClassificationUsingGET(gameId, classificationId, timestamp,
				periodInstanceIndex, page, size);

		assertEquals(pointConceptName, response.getPointConceptName());

	}

	/**
	 * Get general classification definitions
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readAllGeneralClassificationsUsingGETTest() throws ApiException {
		String gameId = "mockGameId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "[   {     \"classificationName\": \"general\",     \"cronExpression\": \"string\",     \"gameId\": \"string\",     \"itemType\": \"string\",     \"itemsToNotificate\": 0,     \"name\": \"string\",     \"type\": \"string\"   } ]";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/classification"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<GeneralClassificationDTO> response = api.readAllGeneralClassificationsUsingGET(gameId);

		assertEquals(1, response.size());

	}

	/**
	 * Get incremental classification defintions
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readAllIncrementalUsingGETTest() throws ApiException {
		String gameId = "mockGameId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "[   {     \"classificationName\": \"general\",     \"cronExpression\": \"string\",     \"gameId\": \"string\",     \"itemType\": \"string\",     \"itemsToNotificate\": 0,     \"name\": \"string\",     \"type\": \"string\"   } ]";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/incclassification"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<IncrementalClassificationDTO> response = api.readAllIncrementalUsingGET(gameId);

		assertEquals(1, response.size());

	}

	/**
	 * Get general classification definition
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readGeneralClassificationUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String classificationId = "general";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "{     \"gameId\": \"" + gameId + "\"}";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/classification/" + classificationId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		GeneralClassificationDTO response = api.readGeneralClassificationUsingGET(gameId, classificationId);

		assertEquals(gameId, response.getGameId());

	}

	/**
	 * Get incremental classification defition
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readIncrementalUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String classificationId = "general";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ClassificationControllerApi api = new ClassificationControllerApi(client);

		String mResponse = "{     \"gameId\": \"" + gameId + "\"}";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/incclassification/" + classificationId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		IncrementalClassificationDTO response = api.readIncrementalUsingGET(gameId, classificationId);

		assertEquals(gameId, response.getGameId());

	}

}
