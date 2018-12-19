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
import static com.github.tomakehurst.wiremock.client.WireMock.put;
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
import it.smartcommunitylab.basic.api.RuleControllerApi;
import it.smartcommunitylab.model.RuleDTO;
import it.smartcommunitylab.model.RuleValidateWrapper;

public class RuleControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	@Test
	public void addRuleUsingPOST1Test() throws ApiException {
		String gameId = "mockGameId";
		RuleDTO rule = new RuleDTO();
		String ruleName = "mockRule";
		rule.setName(ruleName);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		RuleControllerApi api = new RuleControllerApi(client);

		String mResponse = "{   \"content\": \"string\",   \"id\": \"string\",   \"name\": \"" + ruleName + "\" }";

		stubFor(post(urlEqualTo("/model/game/" + gameId + "/rule"))
				.withRequestBody(equalToJson("{\"name\": \"" + ruleName + "\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		RuleDTO response = api.addRuleUsingPOST1(gameId, rule);

		assertEquals(ruleName, response.getName());

	}

	/**
	 * Delete rule
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void deleteDbRuleUsingDELETE1Test() throws ApiException {
		String gameId = "mockGameId";
		String ruleId = "mockRuleId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		RuleControllerApi api = new RuleControllerApi(client);

		String mResponse = "true";

		stubFor(delete(urlEqualTo("/model/game/" + gameId + "/rule/" + ruleId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		Boolean response = api.deleteDbRuleUsingDELETE1(gameId, ruleId);

		assertEquals(true, response);

	}

	/**
	 * Edit rule
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void editRuleUsingPUTTest() throws ApiException {
		String gameId = "mockGameId";
		String ruleId = "mockRuleId";
		RuleDTO rule = new RuleDTO();
		String ruleName = "mockRuleName";
		rule.setId(ruleId);
		rule.setName(ruleName);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		RuleControllerApi api = new RuleControllerApi(client);

		String mResponse = "{   \"content\": \"string\",   \"id\": \"string\",   \"name\": \"" + ruleName + "\" }";

		stubFor(put(urlEqualTo("/model/game/" + gameId + "/rule/" + ruleId))
				.withRequestBody(equalToJson("{\"id\": \"" + ruleId + "\", \"name\": \"" + ruleName + "\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		RuleDTO response = api.editRuleUsingPUT(gameId, ruleId, rule);

		assertEquals(ruleName, response.getName());

	}

	/**
	 * Get rules
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readAllRulesUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String ruleName = "mockRuleName";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		RuleControllerApi api = new RuleControllerApi(client);

		String mResponse = "[{   \"content\": \"string\",   \"id\": \"string\",   \"name\": \"" + ruleName + "\" }]";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/rule"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<RuleDTO> response = api.readAllRulesUsingGET(gameId);

		assertEquals(1, response.size());

	}

	/**
	 * Get rule
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readDbRuleUsingGET1Test() throws ApiException {
		String gameId = "mockGameId";
		String ruleId = "mockRuleId";
		String ruleName = "mockRuleName";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		RuleControllerApi api = new RuleControllerApi(client);

		String mResponse = "{   \"content\": \"string\",   \"id\": \"" + ruleId + "\",   \"name\": \"" + ruleName
				+ "\" }";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/rule/" + ruleId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		RuleDTO response = api.readDbRuleUsingGET1(gameId, ruleId);

		assertEquals(ruleId, response.getId());
		
	}

	/**
	 * Validate rule
	 *
	 *
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void validateRuleUsingPOST1Test() throws ApiException {
		String gameId = "mockGameId";
		String ruleId = "mockRuleId";
		
		RuleValidateWrapper wrapper = new RuleValidateWrapper();
		wrapper.setRule(ruleId);

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		RuleControllerApi api = new RuleControllerApi(client);

		String mResponse = "[\"valid\"]";

		stubFor(post(urlEqualTo("/model/game/" + gameId + "/rule/validate"))
				.withRequestBody(equalToJson("{\"rule\": \"" + ruleId + "\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<String> response = api.validateRuleUsingPOST1(gameId, wrapper);

		assertEquals(true, response.contains("valid"));

	}

}
