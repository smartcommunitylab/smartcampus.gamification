package it.smartcommunitylab.api.wiremock.test;

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

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.api.ChallengeModelControllerApi;
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
