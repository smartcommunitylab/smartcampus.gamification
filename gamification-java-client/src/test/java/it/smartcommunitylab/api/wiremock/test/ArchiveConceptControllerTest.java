package it.smartcommunitylab.api.wiremock.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
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
import it.smartcommunitylab.api.ArchiveConceptControllerApi;
import it.smartcommunitylab.model.ArchivedConcept;

public class ArchiveConceptControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	@Test
	public void readArchivesForGameUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		ArchiveConceptControllerApi api = new ArchiveConceptControllerApi(client);

		String mResponse = "[   {     \"archivingDate\": \"2018-12-17T08:54:55.467Z\",     \"challenge\": {       \"completed\": true,       \"dateCompleted\": \"2018-12-17T08:54:55.467Z\",       \"end\": \"2018-12-17T08:54:55.467Z\",       \"fields\": {},       \"forced\": true,       \"id\": \"string\",       \"modelName\": \"string\",       \"name\": \"string\",       \"origin\": \"string\",       \"priority\": 0,       \"start\": \"2018-12-17T08:54:55.467Z\",       \"state\": \"PROPOSED\",       \"stateDate\": {}     },     \"gameId\": \""
				+ gameId
				+ "\",     \"groupChallenge\": {       \"attendees\": [         {           \"challengeScore\": 0,           \"playerId\": \"string\",           \"role\": \"PROPOSER\",           \"valuationTime\": \"2018-12-17T08:54:55.467Z\",           \"winner\": true         }       ],       \"challengeModel\": \"string\",       \"challengePointConcept\": {         \"name\": \"string\",         \"period\": \"string\"       },       \"challengeTarget\": 0,       \"end\": \"2018-12-17T08:54:55.467Z\",       \"gameId\": \"string\",       \"id\": \"string\",       \"instanceName\": \"string\",       \"origin\": \"string\",       \"priority\": 0,       \"reward\": {         \"bonusScore\": {},         \"calculationPointConcept\": {           \"name\": \"string\",           \"period\": \"string\"         },         \"percentage\": 0,         \"targetPointConcept\": {           \"name\": \"string\",           \"period\": \"string\"         },         \"threshold\": 0       },       \"start\": \"2018-12-17T08:54:55.467Z\",       \"state\": \"PROPOSED\",       \"stateDate\": {}     },     \"id\": \"string\",     \"playerId\": \"string\"   } ]";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/archive?playerId=" + playerId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<ArchivedConcept> response = api.readArchivesForGameUsingGET(gameId, playerId, null, null, null);

		assertEquals(gameId, response.get(0).getGameId());
		
	}
}
