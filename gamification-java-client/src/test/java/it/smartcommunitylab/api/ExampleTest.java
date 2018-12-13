package it.smartcommunitylab.api;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.model.PlayerStateDTO;

public class ExampleTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	@Test
	public void exactUrlOnly() throws IOException, ApiException {

		it.smartcommunitylab.ApiClient client = new it.smartcommunitylab.ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		String gameId = "mockGameId";
		String playerId = "mockPlayerId";

		String response = "{\"gameId\":\"mockGameId\"}";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(response)));

		PlayerControllerApi api = new PlayerControllerApi();
		PlayerStateDTO result = api.readPlayerUsingGET(gameId, playerId);

		assertEquals(result.getGameId(), gameId);
	}
}