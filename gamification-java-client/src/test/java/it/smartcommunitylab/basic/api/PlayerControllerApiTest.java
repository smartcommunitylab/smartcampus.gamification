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

/*
 * Gamification Engine API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package it.smartcommunitylab.basic.api;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.auth.HttpBasicAuth;
import it.smartcommunitylab.model.ChallengeAssignmentDTO;
import it.smartcommunitylab.model.ChallengeConcept;
import it.smartcommunitylab.model.ChallengeInvitation;
import it.smartcommunitylab.model.GroupChallengeDTO;
import it.smartcommunitylab.model.Inventory;
import it.smartcommunitylab.model.ItemChoice;
import it.smartcommunitylab.model.PagePlayerStateDTO;
import it.smartcommunitylab.model.PlayerBlackList;
import it.smartcommunitylab.model.PlayerStateDTO;
import it.smartcommunitylab.model.TeamDTO;
import it.smartcommunitylab.model.WrapperQuery;
import it.smartcommunitylab.model.ext.GameConcept;
import it.smartcommunitylab.model.ext.PlayerLevel;
import it.smartcommunitylab.model.ext.PointConcept;

/**
 * API tests for PlayerControllerApi
 */
@Ignore
public class PlayerControllerApiTest {

    private final PlayerControllerApi api = new PlayerControllerApi();
    private ApiClient apiClient;
    private final String userName = "long-rovereto";
    private final String password = "rov";
    private String baseUrl = "http://localhost:6060/gamification";
    private String gameId = "57ac710fd4c6ac7872b0e7a1";
    private String playerId = "24153";
    private String conceptName = "green leaves";

    @Before
    public void init() {
    	 apiClient = new ApiClient(baseUrl);
    	
    	 // Configure OAuth2 access token for authorization: oauth2
    	 // OAuth oauth2 = (OAuth) apiClient.getAuthentication("oauth2");
    	 // oauth2.setAccessToken("YOUR_ACCESS_TOKEN");
    	 
    	 // Configure basic auth. 
    	 HttpBasicAuth basic = (HttpBasicAuth) apiClient.getAuthentication("basic");
    	 basic.setUsername(userName);
    	 basic.setPassword(password);

    	 api.setApiClient(apiClient);
    }
    
    /**
     * Accept challenge
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void acceptChallengeUsingPOSTTest() throws ApiException {
        String challengeName = null;
        ChallengeConcept response = api.acceptChallengeUsingPOST(gameId, playerId, challengeName);

        // TODO: test validations
    }
    
    /**
     * acceptInvitation
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void acceptInvitationUsingPOSTTest() throws ApiException {
        String challengeName = null;
        api.acceptInvitationUsingPOST(gameId, playerId, challengeName);

        // TODO: test validations
    }
    
    /**
     * Activate a choice
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void activateChoiceUsingPOSTTest() throws ApiException {
        ItemChoice choice = null;
        Inventory response = api.activateChoiceUsingPOST(gameId, playerId, choice);

        // TODO: test validations
    }
    
    /**
     * Assign challenge
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void assignChallengeUsingPOSTTest() throws ApiException {
        ChallengeAssignmentDTO challengeData = null;
        api.assignChallengeUsingPOST(challengeData, gameId, playerId);

        // TODO: test validations
    }
    
    /**
     * assignGroupChallenge
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void assignGroupChallengeUsingPOSTTest() throws ApiException {
        GroupChallengeDTO challengeData = null;
        api.assignGroupChallengeUsingPOST(challengeData, gameId);

        // TODO: test validations
    }
    
    /**
     * Add another player to challenge block list
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void blockPlayerUsingPOSTTest() throws ApiException {
        String otherPlayerId = null;
        PlayerBlackList response = api.blockPlayerUsingPOST(gameId, playerId, otherPlayerId);

        // TODO: test validations
    }
    
    /**
     * cancelInvitation
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void cancelInvitationUsingPOSTTest() throws ApiException {
        String challengeName = null;
        api.cancelInvitationUsingPOST(gameId, playerId, challengeName);

        // TODO: test validations
    }
    
    /**
     * Create player
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void createPlayerUsingPOST1Test() throws ApiException {
        PlayerStateDTO player = null;
        api.createPlayerUsingPOST1(gameId, player);

        // TODO: test validations
    }
    
    /**
     * Delete player state
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deletePlayerUsingDELETE1Test() throws ApiException {
        api.deletePlayerUsingDELETE1(gameId, playerId);

        // TODO: test validations
    }
    
    /**
     * Get player challenges
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void getPlayerChallengeUsingGETTest() throws ApiException {
        List<ChallengeConcept> response = api.getPlayerChallengeUsingGET(gameId, playerId);

        System.out.println(response.size());
    }
    
    /**
     * inviteIntoAChallenge
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void inviteIntoAChallengeUsingPOSTTest() throws ApiException {
        ChallengeInvitation invitation = null;
        ChallengeInvitation response = api.inviteIntoAChallengeUsingPOST(invitation, gameId, playerId);

        // TODO: test validations
    }
    
    /**
     * Get player inventory
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readInventoryUsingGETTest() throws ApiException {
        Inventory response = api.readInventoryUsingGET(gameId, playerId);

        // TODO: test validations
    }
    
    /**
     * Get player levels
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readLevelsUsingGETTest() throws ApiException {
        List<PlayerLevel> response = api.readLevelsUsingGET(gameId, playerId);

        // TODO: test validations
    }
    
    /**
     * Get player black list of other players
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readPlayerBlackListUsingGETTest() throws ApiException {
        PlayerBlackList response = api.readPlayerBlackListUsingGET(gameId, playerId);

        // TODO: test validations
    }
    
    /**
     * Get player state
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readPlayerUsingGETTest() throws ApiException {
        PlayerStateDTO response = api.readPlayerUsingGET(gameId, playerId);
        System.out.println(response.getPlayerId());

        // TODO: test validations
    }
    
    /**
     * Get player state
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @Test
    public void readStateUsingGETTest() throws ApiException, JsonParseException, JsonMappingException, IOException {
        PlayerStateDTO response = api.readStateUsingGET(gameId, playerId);
        System.out.println(response.getGameId());
       
        Set<GameConcept> scores = response.getState().get("PointConcept");
        scores.stream().filter(score -> "green leaves".equals(score.getName())).findFirst()
        .map(concept -> (PointConcept) concept)
        .ifPresent(score -> System.out.println(score.getScore()));
       
    }
    
    /**
     * Get availabe challengers for the player
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readSystemPlayerStateUsingGETTest() throws ApiException {
        List<String> response = api.readSystemPlayerStateUsingGET(gameId, playerId, conceptName);

        // TODO: test validations
    }
    
    /**
     * Get player teams
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readTeamsByMemberUsingGET1Test() throws ApiException {
        List<TeamDTO> response = api.readTeamsByMemberUsingGET1(gameId, playerId);

        // TODO: test validations
    }
    
    /**
     * refuseInvitation
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void refuseInvitationUsingPOSTTest() throws ApiException {
        String gameId = null;
        String playerId = null;
        String challengeName = null;
        api.refuseInvitationUsingPOST(gameId, playerId, challengeName);

        // TODO: test validations
    }
    
    /**
     * Search player states
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void searchByQueryUsingPOSTTest() throws ApiException {
        String gameId = null;
        WrapperQuery query = null;
        String page = null;
        String size = null;
        PagePlayerStateDTO response = api.searchByQueryUsingPOST(gameId, query, page, size);

        // TODO: test validations
    }
    
    /**
     * Unblock another player from challenge block list
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void unBlockPlayerUsingPOSTTest() throws ApiException {
        String gameId = null;
        String playerId = null;
        String otherPlayerId = null;
        PlayerBlackList response = api.unBlockPlayerUsingPOST(gameId, playerId, otherPlayerId);

        // TODO: test validations
    }
    
}