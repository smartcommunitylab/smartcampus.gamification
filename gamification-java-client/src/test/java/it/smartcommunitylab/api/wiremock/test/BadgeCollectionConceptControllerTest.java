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
import it.smartcommunitylab.api.BadgeCollectionConceptControllerApi;
import it.smartcommunitylab.model.BadgeCollectionConcept;

public class BadgeCollectionConceptControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	@Test
	public void readArchivesForGameUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String collectionId = "badge";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		BadgeCollectionConceptControllerApi api = new BadgeCollectionConceptControllerApi(client);

		String mResponse = "{     \"badgeEarned\": [       \"string\"     ],     \"id\": \"" + collectionId
				+ "\", \"name\": \"string\"   }";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/badges/" + collectionId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		BadgeCollectionConcept response = api.readBadgeCollectionUsingGET(gameId, collectionId);

		assertEquals(collectionId, response.getId());

	}

	/**
	 * Get the badge collections
	 *
	 * Get badge collections in a game
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readBadgeCollectionsUsingGETTest() throws ApiException {

		String gameId = "mockGameId";
		String collectionId = "badge";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		BadgeCollectionConceptControllerApi api = new BadgeCollectionConceptControllerApi(client);

		String mResponse = "[{     \"badgeEarned\": [       \"string\"     ],     \"id\": \"" + collectionId
				+ "\", \"name\": \"string\"   }]";

		stubFor(get(urlEqualTo("/model/game/" + gameId + "/badges"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<BadgeCollectionConcept> response = api.readBadgeCollectionsUsingGET(gameId);

		assertEquals(collectionId, response.get(0).getId());

	}

}
