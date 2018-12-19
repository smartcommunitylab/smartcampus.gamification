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
import it.smartcommunitylab.api.PointConceptControllerApi;
import it.smartcommunitylab.model.PointConcept;

public class PointConceptControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	/**
	 * Add point
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void addPointUsingPOST1Test() throws ApiException {
		PointConcept point = new PointConcept();
		String conceptName = "mockLeaves";
		point.setName(conceptName);
		String gameId = "mockGameId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PointConceptControllerApi api = new PointConceptControllerApi(client);

		String mResponse = "{   \"id\": \"mockLeaves\",   \"name\": \"" + conceptName
				+ "\",   \"periods\": {},   \"score\": 0 }";

		stubFor(post(urlEqualTo("/model/game/" + gameId + "/point"))
				.withRequestBody(equalToJson("{\"name\": \"" + conceptName + "\"}"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		PointConcept response = api.addPointUsingPOST1(gameId, point);

		assertEquals(conceptName, response.getName());

	}

	/**
	 * Get point
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readPointUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String pointId = "mockLeaves";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PointConceptControllerApi api = new PointConceptControllerApi(client);

		String mResponse = "{   \"id\": \"" + pointId + "\",   \"name\": \"" + pointId
				+ "\",   \"periods\": {},   \"score\": 0 }";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/point/" + pointId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		PointConcept response = api.readPointUsingGET(gameId, pointId);

		assertEquals(pointId, response.getId());

	}

	/**
	 * Get points
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readPointsUsingGET1Test() throws ApiException {
		String gameId = "mockGameId";
		String pointId = "mockLeaves";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PointConceptControllerApi api = new PointConceptControllerApi(client);

		String mResponse = "[{   \"id\": \"" + pointId + "\",   \"name\": \"" + pointId
				+ "\",   \"periods\": {},   \"score\": 0 }]";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/point"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<PointConcept> response = api.readPointsUsingGET1(gameId);

		assertEquals(pointId, response.get(0).getId());

	}

}
