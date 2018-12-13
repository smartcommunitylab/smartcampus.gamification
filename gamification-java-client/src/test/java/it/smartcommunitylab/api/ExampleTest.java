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
import it.smartcommunitylab.auth.HttpBasicAuth;
import it.smartcommunitylab.model.PlayerStateDTO;

public class ExampleTest {

  private final String userName = "long-rovereto";
  private final String password = "rov";
  private static final int PORT = 8089;
  @Rule public WireMockRule wireMockRule = new WireMockRule(PORT);

  @Test
  public void exactUrlOnly() throws IOException, ApiException {
  
	it.smartcommunitylab.ApiClient client = new it.smartcommunitylab.ApiClient();
    client.setBasePath("http://localhost:" + PORT);
    HttpBasicAuth basic = (HttpBasicAuth) client.getAuthentication("basic");
	basic.setUsername(userName);
	basic.setPassword(password);
    Configuration.setDefaultApiClient(client);

    String gameId = "5b7a885149c95d50c5f9d442";
    String playerId = "8";
    
    String response = "{\"gameId\":\"5b7a885149c95d50c5f9d442\"}";
    
    stubFor(
        get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId))
            .willReturn(
                aResponse()
                    .withHeader("Content-Type", "application/json")
                    .withBody(response)));

//    CoreV1Api api = new CoreV1Api();
    PlayerControllerApi api = new PlayerControllerApi();
    PlayerStateDTO result = api.readPlayerUsingGET(gameId, playerId);
   
    assertEquals(result.getGameId(), gameId);
  }
}