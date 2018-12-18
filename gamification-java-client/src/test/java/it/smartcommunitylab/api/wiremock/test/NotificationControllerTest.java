package it.smartcommunitylab.api.wiremock.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.api.NotificationControllerApi;
import it.smartcommunitylab.model.CollectionNotification;
import it.smartcommunitylab.model.Notification;

public class NotificationControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	/**
	 * Get game notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readNotificationUsingGET2Test() throws ApiException {
		String gameId = "mockGameId";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "[{ \"gameId\": \"" + gameId + "\"}]";

		stubFor(get(urlEqualTo("/notification/game/" + gameId + "?page=" + page + "&size=" + size))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<Notification> response = api.readNotificationUsingGET2(gameId, fromTs, toTs, includeTypes, excludeTypes,
				page, size);

		assertEquals(gameId, response.get(0).getGameId());
	}

	/**
	 * Get player notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readPlayerNotificationGroupedUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "{ \"mockGameId\": { \"empty\": \"test\" } }";

		stubFor(get(urlEqualTo(
				"/notification/game/" + gameId + "/player/" + playerId + "/grouped?page=" + page + "&size=" + size))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		Map<String, CollectionNotification> response = api.readPlayerNotificationGroupedUsingGET(gameId, playerId,
				fromTs, toTs, includeTypes, excludeTypes, page, size);

		assertEquals(false, response.get(gameId).isEmpty());

	}

	/**
	 * Get player notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readPlayerNotificationUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "[{ \"gameId\": \"" + gameId + "\"}]";

		stubFor(get(
				urlEqualTo("/notification/game/" + gameId + "/player/" + playerId + "?page=" + page + "&size=" + size))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<Notification> response = api.readPlayerNotificationUsingGET(gameId, playerId, fromTs, toTs, includeTypes,
				excludeTypes, page, size);

		assertEquals(gameId, response.get(0).getGameId());

	}

	/**
	 * Get team notifications
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void readTeamNotificationUsingGETTest() throws ApiException {
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";
		String teamId = "mockTeam";
		Long fromTs = null;
		Long toTs = null;
		List<String> includeTypes = null;
		List<String> excludeTypes = null;
		String page = "1";
		String size = "10";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		NotificationControllerApi api = new NotificationControllerApi(client);

		String mResponse = "[{ \"gameId\": \"" + gameId + "\", \"playerId\": \"" + playerId + "\"}]";

		stubFor(get(urlEqualTo("/notification/game/" + gameId + "/team/" + teamId + "?page=" + page + "&size=" + size))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(mResponse)));

		List<Notification> response = api.readTeamNotificationUsingGET(gameId, teamId, fromTs, toTs, includeTypes,
				excludeTypes, page, size);

		assertEquals(playerId, response.get(0).getPlayerId());

	}

}
