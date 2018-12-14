package it.smartcommunitylab.api.wiremock.test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;

import com.github.tomakehurst.wiremock.junit.WireMockRule;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.api.PlayerControllerApi;
import it.smartcommunitylab.model.ChallengeConcept;
import it.smartcommunitylab.model.Inventory;
import it.smartcommunitylab.model.ItemChoice;
import it.smartcommunitylab.model.PlayerStateDTO;

public class PlayerControllerTest {

	private static final int PORT = 8089;
	@Rule
	public WireMockRule wireMockRule = new WireMockRule(PORT);

	@Test
	public void readPlayerState() throws IOException, ApiException {

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String gameId = "mockGameId";
		String playerId = "mockPlayerId";

		String response = "{\"gameId\":\"mockGameId\"}";

		stubFor(get(urlEqualTo("/data/game/" + gameId + "/player/" + playerId))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(response)));

		PlayerStateDTO result = api.readPlayerUsingGET(gameId, playerId);

		assertEquals(result.getGameId(), gameId);
	}

	@Test
	public void acceptChallengeUsingPOSTTest() throws ApiException {

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String challengeName = "mockChallenge";
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";

		String response = "{\"completed\": true,\"dateCompleted\":\"2018-12-13T07:29:19.523Z\",\"end\":\"2018-12-13T07:29:19.523Z\",\"fields\": {},\"forced\":true,\"id\":\"string\",\"modelName\":\"string\",\"name\":\"string\",\"origin\":\"string\",\"priority\": 0,\"start\":\"2018-12-13T07:29:19.523Z\",\"state\": \"PROPOSED\", \"stateDate\":{}}";

		stubFor(post(
				urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/challenges/" + challengeName + "/accept"))
						.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(response)));

		ChallengeConcept challengeConcept = api.acceptChallengeUsingPOST(gameId, playerId, challengeName);

		assertEquals(challengeConcept.getState(), "PROPOSED");

	}

	/**
	 * Activate a choice
	 *
	 * 
	 *
	 * @throws ApiException
	 *             if the Api call fails
	 */
	@Test
	public void activateChoiceUsingPOSTTest() throws ApiException {
		ItemChoice choice = new ItemChoice();
		choice.setName("mockChoice");
		choice.setType(ItemChoice.TypeEnum.CHALLENGE_MODEL);
		String gameId = "mockGameId";
		String playerId = "mockPlayerId";

		ApiClient client = new ApiClient();
		client.setBasePath("http://localhost:" + PORT);
		Configuration.setDefaultApiClient(client);

		PlayerControllerApi api = new PlayerControllerApi();

		String response = "{\"challengeActivationActions\": 0,\"challengeChoices\": [{\"modelName\": \"string\",\"state\": \"AVAILABLE\"}]}";

		stubFor(post(urlEqualTo("/data/game/" + gameId + "/player/" + playerId + "/inventory/activate"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json").withBody(response)));

		Inventory inventory = api.activateChoiceUsingPOST(gameId, playerId, choice);

		assertEquals(inventory.getChallengeChoices().get(0).getState().getValue(), "AVAILABLE");
	}

	//
	// /**
	// * Assign challenge
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void assignChallengeUsingPOSTTest() throws ApiException {
	// ChallengeAssignmentDTO challengeData = null;
	// api.assignChallengeUsingPOST(challengeData, gameId, playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * assignGroupChallenge
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void assignGroupChallengeUsingPOSTTest() throws ApiException {
	// GroupChallengeDTO challengeData = null;
	// api.assignGroupChallengeUsingPOST(challengeData, gameId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Add another player to challenge block list
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void blockPlayerUsingPOSTTest() throws ApiException {
	// String otherPlayerId = null;
	// PlayerBlackList response = api.blockPlayerUsingPOST(gameId, playerId,
	// otherPlayerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * cancelInvitation
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void cancelInvitationUsingPOSTTest() throws ApiException {
	// String challengeName = null;
	// api.cancelInvitationUsingPOST(gameId, playerId, challengeName);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Create player
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void createPlayerUsingPOST1Test() throws ApiException {
	// PlayerStateDTO player = null;
	// api.createPlayerUsingPOST1(gameId, player);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Delete player state
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void deletePlayerUsingDELETE1Test() throws ApiException {
	// api.deletePlayerUsingDELETE1(gameId, playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get player challenges
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void getPlayerChallengeUsingGETTest() throws ApiException {
	// List<ChallengeConcept> response = api.getPlayerChallengeUsingGET(gameId,
	// playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * inviteIntoAChallenge
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void inviteIntoAChallengeUsingPOSTTest() throws ApiException {
	// ChallengeInvitation invitation = null;
	// ChallengeInvitation response =
	// api.inviteIntoAChallengeUsingPOST(invitation, gameId, playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get player inventory
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void readInventoryUsingGETTest() throws ApiException {
	// Inventory response = api.readInventoryUsingGET(gameId, playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get player levels
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void readLevelsUsingGETTest() throws ApiException {
	// List<PlayerLevel> response = api.readLevelsUsingGET(gameId, playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get player black list of other players
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void readPlayerBlackListUsingGETTest() throws ApiException {
	// PlayerBlackList response = api.readPlayerBlackListUsingGET(gameId,
	// playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get player state
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void readPlayerUsingGETTest() throws ApiException {
	// PlayerStateDTO response = api.readPlayerUsingGET(gameId, playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get player state
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void readStateUsingGETTest() throws ApiException {
	// PlayerStateDTO response = api.readStateUsingGET(gameId, playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get availabe challengers for the player
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void readSystemPlayerStateUsingGETTest() throws ApiException {
	// List<String> response = api.readSystemPlayerStateUsingGET(gameId,
	// playerId, conceptName);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Get player teams
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void readTeamsByMemberUsingGET1Test() throws ApiException {
	// List<TeamDTO> response = api.readTeamsByMemberUsingGET1(gameId,
	// playerId);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * refuseInvitation
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void refuseInvitationUsingPOSTTest() throws ApiException {
	// String gameId = null;
	// String playerId = null;
	// String challengeName = null;
	// api.refuseInvitationUsingPOST(gameId, playerId, challengeName);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Search player states
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void searchByQueryUsingPOSTTest() throws ApiException {
	// String gameId = null;
	// WrapperQuery query = null;
	// String page = null;
	// String size = null;
	// PagePlayerStateDTO response = api.searchByQueryUsingPOST(gameId, query,
	// page, size);
	//
	// // TODO: test validations
	// }
	//
	// /**
	// * Unblock another player from challenge block list
	// *
	// *
	// *
	// * @throws ApiException
	// * if the Api call fails
	// */
	// @Test
	// public void unBlockPlayerUsingPOSTTest() throws ApiException {
	// String gameId = null;
	// String playerId = null;
	// String otherPlayerId = null;
	// PlayerBlackList response = api.unBlockPlayerUsingPOST(gameId, playerId,
	// otherPlayerId);
	//
	// // TODO: test validations
	// }

}