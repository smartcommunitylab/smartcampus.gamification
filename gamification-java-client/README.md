# gamification-java-client

## Requirements

Building the API client library requires:
1. Java 1.7+
2. Maven

## Usage

### Maven users

Add this dependency to your project's POM:

```xml
<dependency>
  <groupId>it.smartcommunitylab.gamification</groupId>
  <artifactId>gamification-java-client</artifactId>
  <version>2.4.0</version>
  <scope>compile</scope>
</dependency>
```

### Others

At first generate the JAR by executing:

```shell
mvn clean package
```

Then manually install the following JARs:

* `target/gamification-java-client-2.4.0.jar`
* `target/lib/*.jar`

### Example

```java

import io.swagger.client.*;
import io.swagger.client.auth.*;
import io.swagger.client.model.*;
import io.swagger.client.api.ArchiveConceptControllerApi;

import java.io.File;
import java.util.*;

public class ArchiveConceptControllerApiExample {

    public static void main(String[] args) {
        ApiClient defaultClient = Configuration.getDefaultApiClient();
        
        // Configure HTTP basic authorization: basic
        HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
        basic.setUsername("YOUR USERNAME");
        basic.setPassword("YOUR PASSWORD");

        ArchiveConceptControllerApi apiInstance = new ArchiveConceptControllerApi();
        String gameId = "gameId_example"; // String | gameId
        String playerId = "playerId_example"; // String | playerId
        String state = "state_example"; // String | state
        Long from = 789L; // Long | from
        Long to = 789L; // Long | to
        try {
            List<ArchivedConcept> result = apiInstance.readArchivesForGameUsingGET(gameId, playerId, state, from, to);
            System.out.println(result);
        } catch (ApiException e) {
            System.err.println("Exception when calling ArchiveConceptControllerApi#readArchivesForGameUsingGET");
            e.printStackTrace();
        }
    }
}

```

## Getting Started

### Generate

#### Basic Auth
In order to add or modify API, it is required to edit the api-docs-basic.json file on project root before generating the sdk client stub running the following command from project root(gamification-java-client). 

```shell
java -jar lib/swagger-codegen-cli.jar generate \
-DhideGenerationTimestamp=true \
-i api-docs-basic.json -l java \
--api-package it.smartcommunitylab.basic.api \
--artifact-id gamification-java-client \
--model-package it.smartcommunitylab.model \
--invoker-package it.smartcommunitylab \
--import-mappings TeamDTO=it.smartcommunitylab.model.ext.TeamDTO \
--import-mappings PlayerLevel=it.smartcommunitylab.model.ext.PlayerLevel \
--import-mappings ChallengeAssignmentDTO=it.smartcommunitylab.model.ext.ChallengeAssignmentDTO \
--import-mappings GroupChallengeDTO=it.smartcommunitylab.model.ext.GroupChallengeDTO
```

#### OAuth
In order to add or modify API, it is required to edit the api-docs-oauth.json file on project root before generating the sdk client stub running the following command from project root(gamification-java-client). 

```shell
java -jar lib/swagger-codegen-cli.jar generate \
-DhideGenerationTimestamp=true \
-i api-docs-oauth.json -l java \
--api-package it.smartcommunitylab.oauth.api \
--artifact-id gamification-java-client \
--model-package it.smartcommunitylab.model \
--invoker-package it.smartcommunitylab \
--import-mappings TeamDTO=it.smartcommunitylab.model.ext.TeamDTO \
--import-mappings PlayerLevel=it.smartcommunitylab.model.ext.PlayerLevel \
--import-mappings ChallengeAssignmentDTO=it.smartcommunitylab.model.ext.ChallengeAssignmentDTO \ 
--import-mappings GroupChallengeDTO=it.smartcommunitylab.model.ext.GroupChallengeDTO
```

**WARNING**: at the moment to resolve a problem about polyphormism the code has been manually patched. So avoid to generate completely the client code to not miss the patches. Instead execute a punctual generation to maintain the control of code regeneration and patch the code if necessary To create a punctual generation use `-Dmodels=` (example: `-Dmodels=ChallengeAssignmentDTO`) or `-Dapis=` (example: `-Dapis=PlayerController`) options to create only models or APIs classes needed. Below the patched code to trace the workaround.

```
public class ApiClient {
    ...
    ...

    // FIXME PAY ATTENTION THIS FIELDS ARE MANUALLY INTRODUCED
    // TO PERMIT CORRECT INSTANTIATION OF STATE SUBCLASSES AS GAMECONCEPT
	public Response executeSimple(Call call, Type returnType) throws ApiException {
		try {
			Response response = call.execute();
			if (response.isSuccessful()) {
				if (returnType == null || response.code() == 204) {
					// returning null if the returnType is not defined,
					// or the status code is 204 (No Content)
					if (response.body() != null) {
						try {
							response.body().close();
						} catch (IOException e) {
							throw new ApiException(response.message(), e, response.code(),
									response.headers().toMultimap());
						}
					}
					return null;
				} else {
					return response;
				}
			} else {
				String respBody = null;
				if (response.body() != null) {
					try {
						respBody = response.body().string();
					} catch (IOException e) {
						throw new ApiException(response.message(), e, response.code(), response.headers().toMultimap());
					}
				}
				throw new ApiException(response.message(), response.code(), response.headers().toMultimap(), respBody);
			}
		} catch (IOException e) {
			throw new ApiException(e);
		}
	}

    ...
    ...

}
```
```
public class PlayerControllerApi {
    private ApiClient apiClient;

    // FIXME PAY ATTENTION THIS FIELDS ARE MANUALLY INTRODUCED
    // TO PERMIT CORRECT INSTANTIATION OF STATE SUBCLASSES AS GAMECONCEPT
    private ObjectMapper mapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private PlayerControllerUtils playerControllerUtils = new PlayerControllerUtils();

    ...
    ...
    ...
    // FIXME PAY ATTENTION THIS FIELDS ARE MANUALLY INTRODUCED
    // TO PERMIT CORRECT INSTANTIATION OF STATE SUBCLASSES AS GAMECONCEPT
    public PlayerStateDTO readStateUsingGET(String gameId, String playerId) throws ApiException, JsonParseException, JsonMappingException, IOException {
    	Response response = readStateUsingGETWithHttpInfo(gameId, playerId);
    	return playerControllerUtils.convertPlayerState(mapper.readValue(response.body().byteStream(), Map.class));        
    }
    
    // FIXME PAY ATTENTION THIS FIELDS ARE MANUALLY INTRODUCED
    // TO PERMIT CORRECT INSTANTIATION OF STATE SUBCLASSES AS GAMECONCEPT
    public Response readStateUsingGETWithHttpInfo(String gameId, String playerId) throws ApiException {
        com.squareup.okhttp.Call call = readStateUsingGETValidateBeforeCall(gameId, playerId, null, null);
        Type localVarReturnType = new TypeToken<PlayerStateDTO>(){}.getType();
        return apiClient.executeSimple(call, localVarReturnType);
    }

    ...
    ...
}
```

### Installation

#### Local installation
To install the API client library to your local Maven repository, simply execute:

```shell
mvn clean install
```

#### Remote deployment

To deploy it to a remote Maven repository instead, configure the settings of the repository and execute:

```shell
mvn clean install  deploy:deploy-file  \
-Dmaven.test.skip=true \
-Dpackaging=jar \
-DrepositoryId=SmartCommunity-releases \
-DpomFile=pom.xml \
-Durl=http://repository.smartcommunitylab.it/content/repositories/releases \
-Dfile=target/gamification-java-client-2.4.0.jar
```
## Documentation for API Endpoints

Class | Method | HTTP request | Description
------------ | ------------- | ------------- | -------------
*ArchiveConceptControllerApi* | [**readArchivesForGameUsingGET**](docs/ArchiveConceptControllerApi.md#readArchivesForGameUsingGET) | **GET** /data/game/{gameId}/archive | Read archive concepts for a game with optional filter parameters
*BadgeCollectionConceptControllerApi* | [**addBadgeUsingPOST**](docs/BadgeCollectionConceptControllerApi.md#addBadgeUsingPOST) | **POST** /model/game/{gameId}/badges | Add a badge collection
*BadgeCollectionConceptControllerApi* | [**deleteBadgeCollectionUsingDELETE**](docs/BadgeCollectionConceptControllerApi.md#deleteBadgeCollectionUsingDELETE) | **DELETE** /model/game/{gameId}/badges/{collectionId} | Delete a badge collection
*BadgeCollectionConceptControllerApi* | [**readBadgeCollectionUsingGET**](docs/BadgeCollectionConceptControllerApi.md#readBadgeCollectionUsingGET) | **GET** /model/game/{gameId}/badges/{collectionId} | Get a badge collection
*BadgeCollectionConceptControllerApi* | [**readBadgeCollectionsUsingGET**](docs/BadgeCollectionConceptControllerApi.md#readBadgeCollectionsUsingGET) | **GET** /model/game/{gameId}/badges | Get the badge collections
*BadgeCollectionConceptControllerApi* | [**updateBadgeCollectionUsingPUT**](docs/BadgeCollectionConceptControllerApi.md#updateBadgeCollectionUsingPUT) | **PUT** /model/game/{gameId}/badges/{collectionId} | Update a badge collection
*ChallengeModelControllerApi* | [**deleteChallengeModelsUsingDELETE**](docs/ChallengeModelControllerApi.md#deleteChallengeModelsUsingDELETE) | **DELETE** /model/game/{gameId}/challenge/{modelId} | Delete challenge model
*ChallengeModelControllerApi* | [**readChallengeModelsUsingGET**](docs/ChallengeModelControllerApi.md#readChallengeModelsUsingGET) | **GET** /model/game/{gameId}/challenge | Get challenge models
*ChallengeModelControllerApi* | [**saveGameUsingPOST**](docs/ChallengeModelControllerApi.md#saveGameUsingPOST) | **POST** /model/game/{gameId}/challenge | Add challenge model
*ClassificationControllerApi* | [**addClassificationTaskUsingPOST**](docs/ClassificationControllerApi.md#addClassificationTaskUsingPOST) | **POST** /model/game/{gameId}/classification | Add general classification definition
*ClassificationControllerApi* | [**createIncrementalUsingPOST**](docs/ClassificationControllerApi.md#createIncrementalUsingPOST) | **POST** /model/game/{gameId}/incclassification | Add incremental classification definition
*ClassificationControllerApi* | [**deleteClassificationTaskUsingDELETE**](docs/ClassificationControllerApi.md#deleteClassificationTaskUsingDELETE) | **DELETE** /model/game/{gameId}/task/{classificationId} | Delete general classification definition
*ClassificationControllerApi* | [**deleteIncrementalUsingDELETE**](docs/ClassificationControllerApi.md#deleteIncrementalUsingDELETE) | **DELETE** /model/game/{gameId}/incclassification/{classificationId} | Delete incremental classification definition
*ClassificationControllerApi* | [**editClassificationTaskUsingPUT**](docs/ClassificationControllerApi.md#editClassificationTaskUsingPUT) | **PUT** /model/game/{gameId}/classification/{classificationId} | Edit general classification definition
*ClassificationControllerApi* | [**getGeneralClassificationUsingGET**](docs/ClassificationControllerApi.md#getGeneralClassificationUsingGET) | **GET** /data/game/{gameId}/classification/{classificationId} | Read general classification board
*ClassificationControllerApi* | [**getIncrementalClassificationUsingGET**](docs/ClassificationControllerApi.md#getIncrementalClassificationUsingGET) | **GET** /data/game/{gameId}/incclassification/{classificationId} | Read incremental classification board
*ClassificationControllerApi* | [**readAllGeneralClassificationsUsingGET**](docs/ClassificationControllerApi.md#readAllGeneralClassificationsUsingGET) | **GET** /model/game/{gameId}/classification | Get general classification definitions
*ClassificationControllerApi* | [**readAllIncrementalUsingGET**](docs/ClassificationControllerApi.md#readAllIncrementalUsingGET) | **GET** /model/game/{gameId}/incclassification | Get incremental classification defintions
*ClassificationControllerApi* | [**readGeneralClassificationUsingGET**](docs/ClassificationControllerApi.md#readGeneralClassificationUsingGET) | **GET** /model/game/{gameId}/classification/{classificationId} | Get general classification definition
*ClassificationControllerApi* | [**readIncrementalUsingGET**](docs/ClassificationControllerApi.md#readIncrementalUsingGET) | **GET** /model/game/{gameId}/incclassification/{classificationId} | Get incremental classification defition
*ClassificationControllerApi* | [**updateIncrementalClassificationUsingPUT**](docs/ClassificationControllerApi.md#updateIncrementalClassificationUsingPUT) | **PUT** /model/game/{gameId}/incclassification/{classificationId} | Edit general classification definition
*ConsoleControllerApi* | [**addBadgeUsingPOST1**](docs/ConsoleControllerApi.md#addBadgeUsingPOST1) | **POST** /console/game/{gameId}/badgecoll | addBadge
*ConsoleControllerApi* | [**addClassificationTaskUsingPOST1**](docs/ConsoleControllerApi.md#addClassificationTaskUsingPOST1) | **POST** /console/game/{gameId}/task | addClassificationTask
*ConsoleControllerApi* | [**addPointUsingPOST**](docs/ConsoleControllerApi.md#addPointUsingPOST) | **POST** /console/game/{gameId}/point | addPoint
*ConsoleControllerApi* | [**addRuleUsingPOST**](docs/ConsoleControllerApi.md#addRuleUsingPOST) | **POST** /console/game/{gameId}/rule/db | addRule
*ConsoleControllerApi* | [**createPlayerUsingPOST**](docs/ConsoleControllerApi.md#createPlayerUsingPOST) | **POST** /console/game/{gameId}/player | createPlayer
*ConsoleControllerApi* | [**createTeamUsingPOST**](docs/ConsoleControllerApi.md#createTeamUsingPOST) | **POST** /console/game/{gameId}/team | createTeam
*ConsoleControllerApi* | [**deleteClassificationTaskUsingPOST**](docs/ConsoleControllerApi.md#deleteClassificationTaskUsingPOST) | **POST** /console/game/{gameId}/task/del | deleteClassificationTask
*ConsoleControllerApi* | [**deleteDbRuleUsingDELETE**](docs/ConsoleControllerApi.md#deleteDbRuleUsingDELETE) | **DELETE** /console/game/{gameId}/rule/db/{ruleUrl} | deleteDbRule
*ConsoleControllerApi* | [**deleteGameUsingDELETE**](docs/ConsoleControllerApi.md#deleteGameUsingDELETE) | **DELETE** /console/game/{gameId} | deleteGame
*ConsoleControllerApi* | [**deletePlayerUsingDELETE**](docs/ConsoleControllerApi.md#deletePlayerUsingDELETE) | **DELETE** /console/game/{gameId}/player/{playerId} | deletePlayer
*ConsoleControllerApi* | [**deleteTeamUsingDELETE**](docs/ConsoleControllerApi.md#deleteTeamUsingDELETE) | **DELETE** /console/game/{gameId}/team/{teamId} | deleteTeam
*ConsoleControllerApi* | [**editClassificationTaskUsingPUT1**](docs/ConsoleControllerApi.md#editClassificationTaskUsingPUT1) | **PUT** /console/game/{gameId}/task | editClassificationTask
*ConsoleControllerApi* | [**readBadgeCollectionsUsingGET1**](docs/ConsoleControllerApi.md#readBadgeCollectionsUsingGET1) | **GET** /console/game/{gameId}/badgecoll | readBadgeCollections
*ConsoleControllerApi* | [**readDbRuleUsingGET**](docs/ConsoleControllerApi.md#readDbRuleUsingGET) | **GET** /console/game/{gameId}/rule/db/{ruleUrl} | readDbRule
*ConsoleControllerApi* | [**readGameUsingGET**](docs/ConsoleControllerApi.md#readGameUsingGET) | **GET** /console/game/{gameId} | readGame
*ConsoleControllerApi* | [**readGamesUsingGET**](docs/ConsoleControllerApi.md#readGamesUsingGET) | **GET** /console/game | readGames
*ConsoleControllerApi* | [**readPointsUsingGET**](docs/ConsoleControllerApi.md#readPointsUsingGET) | **GET** /console/game/{gameId}/point | readPoints
*ConsoleControllerApi* | [**readTeamsByMemberUsingGET**](docs/ConsoleControllerApi.md#readTeamsByMemberUsingGET) | **GET** /console/game/{gameId}/player/{playerId}/teams | readTeamsByMember
*ConsoleControllerApi* | [**saveGameUsingPOST1**](docs/ConsoleControllerApi.md#saveGameUsingPOST1) | **POST** /console/game | saveGame
*ConsoleControllerApi* | [**updateCustomDataUsingPUT**](docs/ConsoleControllerApi.md#updateCustomDataUsingPUT) | **PUT** /console/game/{gameId}/player/{playerId} | updateCustomData
*ConsoleControllerApi* | [**updateTeamMembersUsingPOST**](docs/ConsoleControllerApi.md#updateTeamMembersUsingPOST) | **POST** /console/game/{gameId}/team/{teamId}/members | updateTeamMembers
*ConsoleControllerApi* | [**validateRuleUsingPOST**](docs/ConsoleControllerApi.md#validateRuleUsingPOST) | **POST** /console/rule/validate | validateRule
*ExecutionControllerApi* | [**executeActionUsingPOST**](docs/ExecutionControllerApi.md#executeActionUsingPOST) | **POST** /exec/game/{gameId}/action/{actionId} | Execute an action
*GameControllerApi* | [**addActionUsingPOST**](docs/GameControllerApi.md#addActionUsingPOST) | **POST** /model/game/{gameId}/action/{actionId} | Add action
*GameControllerApi* | [**deleteActionUsingDELETE**](docs/GameControllerApi.md#deleteActionUsingDELETE) | **DELETE** /model/game/{gameId}/action/{actionId} | Delete action
*GameControllerApi* | [**deleteGameUsingDELETE1**](docs/GameControllerApi.md#deleteGameUsingDELETE1) | **DELETE** /model/game/{gameId} | Delete game
*GameControllerApi* | [**deleteLevelUsingDELETE**](docs/GameControllerApi.md#deleteLevelUsingDELETE) | **DELETE** /model/game/{gameId}/level/{levelName} | Delete a level
*GameControllerApi* | [**editActionUsingPUT**](docs/GameControllerApi.md#editActionUsingPUT) | **PUT** /model/game/{domain}/action/{actionId} | Edit action
*GameControllerApi* | [**readActionUsingGET**](docs/GameControllerApi.md#readActionUsingGET) | **GET** /model/game/{gameId}/action/{actionId} | Get action
*GameControllerApi* | [**readAllActionUsingGET**](docs/GameControllerApi.md#readAllActionUsingGET) | **GET** /model/game/{gameId}/action | Get actions
*GameControllerApi* | [**readGameStatisticsUsingGET**](docs/GameControllerApi.md#readGameStatisticsUsingGET) | **GET** /data/game/{gameId}/statistics | Get game statistics
*GameControllerApi* | [**readGameUsingGET1**](docs/GameControllerApi.md#readGameUsingGET1) | **GET** /model/game/{gameId} | Read game definition
*GameControllerApi* | [**readGamesUsingGET1**](docs/GameControllerApi.md#readGamesUsingGET1) | **GET** /model/game | Get games
*GameControllerApi* | [**saveGameUsingPOST2**](docs/GameControllerApi.md#saveGameUsingPOST2) | **POST** /model/game | Save a game
*GameControllerApi* | [**saveLevelUsingPOST**](docs/GameControllerApi.md#saveLevelUsingPOST) | **POST** /model/game/{gameId}/level | Save a level
*GameControllerApi* | [**startGameUsingPUT**](docs/GameControllerApi.md#startGameUsingPUT) | **PUT** /model/game/{gameId}/start | Start game
*GameControllerApi* | [**stopGameUsingPUT**](docs/GameControllerApi.md#stopGameUsingPUT) | **PUT** /model/game/{gameId}/stop | Stop a game
*MainControllerApi* | [**executeActionUsingPOST1**](docs/MainControllerApi.md#executeActionUsingPOST1) | **POST** /gengine/execute | Execute an action
*MainControllerApi* | [**readNotificationUsingGET**](docs/MainControllerApi.md#readNotificationUsingGET) | **GET** /gengine/notification/{gameId}/{playerId} | Get player notifications
*MainControllerApi* | [**readNotificationUsingGET1**](docs/MainControllerApi.md#readNotificationUsingGET1) | **GET** /gengine/notification/{gameId} | Get notifications
*MainControllerApi* | [**readPlayerStateUsingGET**](docs/MainControllerApi.md#readPlayerStateUsingGET) | **GET** /gengine/state/{gameId}/{playerId} | Get player state
*MainControllerApi* | [**readPlayerStateUsingGET1**](docs/MainControllerApi.md#readPlayerStateUsingGET1) | **GET** /gengine/state/{gameId} | Get player states
*NotificationControllerApi* | [**readNotificationUsingGET2**](docs/NotificationControllerApi.md#readNotificationUsingGET2) | **GET** /notification/game/{gameId} | Get game notifications
*NotificationControllerApi* | [**readPlayerNotificationGroupedUsingGET**](docs/NotificationControllerApi.md#readPlayerNotificationGroupedUsingGET) | **GET** /notification/game/{gameId}/player/{playerId}/grouped | Get player notifications
*NotificationControllerApi* | [**readPlayerNotificationUsingGET**](docs/NotificationControllerApi.md#readPlayerNotificationUsingGET) | **GET** /notification/game/{gameId}/player/{playerId} | Get player notifications
*NotificationControllerApi* | [**readTeamNotificationUsingGET**](docs/NotificationControllerApi.md#readTeamNotificationUsingGET) | **GET** /notification/game/{gameId}/team/{teamId} | Get team notifications
*PlayerControllerApi* | [**acceptChallengeUsingPOST**](docs/PlayerControllerApi.md#acceptChallengeUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept | Accept challenge
*PlayerControllerApi* | [**acceptInvitationUsingPOST**](docs/PlayerControllerApi.md#acceptInvitationUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation/accept/{challengeName} | acceptInvitation
*PlayerControllerApi* | [**activateChoiceUsingPOST**](docs/PlayerControllerApi.md#activateChoiceUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/inventory/activate | Activate a choice
*PlayerControllerApi* | [**assignChallengeUsingPOST**](docs/PlayerControllerApi.md#assignChallengeUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/challenges | Assign challenge
*PlayerControllerApi* | [**assignGroupChallengeUsingPOST**](docs/PlayerControllerApi.md#assignGroupChallengeUsingPOST) | **POST** /data/game/{gameId}/group-challenges | assignGroupChallenge
*PlayerControllerApi* | [**blockPlayerUsingPOST**](docs/PlayerControllerApi.md#blockPlayerUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/block/{otherPlayerId} | Add another player to challenge block list
*PlayerControllerApi* | [**cancelInvitationUsingPOST**](docs/PlayerControllerApi.md#cancelInvitationUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation/cancel/{challengeName} | cancelInvitation
*PlayerControllerApi* | [**createPlayerUsingPOST1**](docs/PlayerControllerApi.md#createPlayerUsingPOST1) | **POST** /data/game/{gameId}/player/{playerId} | Create player
*PlayerControllerApi* | [**deletePlayerUsingDELETE1**](docs/PlayerControllerApi.md#deletePlayerUsingDELETE1) | **DELETE** /data/game/{gameId}/player/{playerId} | Delete player state
*PlayerControllerApi* | [**getPlayerChallengeUsingGET**](docs/PlayerControllerApi.md#getPlayerChallengeUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/challenges | Get player challenges
*PlayerControllerApi* | [**inviteIntoAChallengeUsingPOST**](docs/PlayerControllerApi.md#inviteIntoAChallengeUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation | inviteIntoAChallenge
*PlayerControllerApi* | [**readCustomDataUsingGET**](docs/PlayerControllerApi.md#readCustomDataUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/custom | Get player custom data
*PlayerControllerApi* | [**readInventoryUsingGET**](docs/PlayerControllerApi.md#readInventoryUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/inventory | Get player inventory
*PlayerControllerApi* | [**readLevelsUsingGET**](docs/PlayerControllerApi.md#readLevelsUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/levels | Get player levels
*PlayerControllerApi* | [**readPlayerBlackListUsingGET**](docs/PlayerControllerApi.md#readPlayerBlackListUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/blacklist | Get player black list of other players
*PlayerControllerApi* | [**readPlayerUsingGET**](docs/PlayerControllerApi.md#readPlayerUsingGET) | **GET** /data/game/{gameId}/player/{playerId} | Get player state
*PlayerControllerApi* | [**readStateUsingGET**](docs/PlayerControllerApi.md#readStateUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/state | Get player state
*PlayerControllerApi* | [**readSystemPlayerStateUsingGET**](docs/PlayerControllerApi.md#readSystemPlayerStateUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/challengers | Get availabe challengers for the player
*PlayerControllerApi* | [**readTeamsByMemberUsingGET1**](docs/PlayerControllerApi.md#readTeamsByMemberUsingGET1) | **GET** /data/game/{gameId}/player/{playerId}/teams | Get player teams
*PlayerControllerApi* | [**refuseInvitationUsingPOST**](docs/PlayerControllerApi.md#refuseInvitationUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation/refuse/{challengeName} | refuseInvitation
*PlayerControllerApi* | [**searchByQueryUsingPOST**](docs/PlayerControllerApi.md#searchByQueryUsingPOST) | **POST** /data/game/{gameId}/player/search | Search player states
*PlayerControllerApi* | [**unBlockPlayerUsingPOST**](docs/PlayerControllerApi.md#unBlockPlayerUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/unblock/{otherPlayerId} | Unblock another player from challenge block list
*PlayerControllerApi* | [**updatePlayerUsingPUT**](docs/PlayerControllerApi.md#updatePlayerUsingPUT) | **PUT** /data/game/{gameId}/player/{playerId} | Edit player state
*PointConceptControllerApi* | [**addPointUsingPOST1**](docs/PointConceptControllerApi.md#addPointUsingPOST1) | **POST** /model/game/{gameId}/point | Add point
*PointConceptControllerApi* | [**deletePointUsingDELETE**](docs/PointConceptControllerApi.md#deletePointUsingDELETE) | **DELETE** /model/game/{gameId}/point/{pointId} | Delete point
*PointConceptControllerApi* | [**readPointUsingGET**](docs/PointConceptControllerApi.md#readPointUsingGET) | **GET** /model/game/{gameId}/point/{pointId} | Get point
*PointConceptControllerApi* | [**readPointsUsingGET1**](docs/PointConceptControllerApi.md#readPointsUsingGET1) | **GET** /model/game/{gameId}/point | Get points
*PointConceptControllerApi* | [**updatePointUsingPUT**](docs/PointConceptControllerApi.md#updatePointUsingPUT) | **PUT** /model/game/{gameId}/point/{pointId} | Edit point
*RuleControllerApi* | [**addRuleUsingPOST1**](docs/RuleControllerApi.md#addRuleUsingPOST1) | **POST** /model/game/{gameId}/rule | Add rule
*RuleControllerApi* | [**deleteDbRuleUsingDELETE1**](docs/RuleControllerApi.md#deleteDbRuleUsingDELETE1) | **DELETE** /model/game/{gameId}/rule/{ruleId} | Delete rule
*RuleControllerApi* | [**editRuleUsingPUT**](docs/RuleControllerApi.md#editRuleUsingPUT) | **PUT** /model/game/{gameId}/rule/{ruleId} | Edit rule
*RuleControllerApi* | [**readAllRulesUsingGET**](docs/RuleControllerApi.md#readAllRulesUsingGET) | **GET** /model/game/{gameId}/rule | Get rules
*RuleControllerApi* | [**readDbRuleUsingGET1**](docs/RuleControllerApi.md#readDbRuleUsingGET1) | **GET** /model/game/{gameId}/rule/{ruleId} | Get rule
*RuleControllerApi* | [**validateRuleUsingPOST1**](docs/RuleControllerApi.md#validateRuleUsingPOST1) | **POST** /model/game/{gameId}/rule/validate | Validate rule
*TeamControllerApi* | [**addTeamMemberUsingPUT**](docs/TeamControllerApi.md#addTeamMemberUsingPUT) | **PUT** /data/game/{gameId}/team/{teamId}/members/{playerId} | Add team member
*TeamControllerApi* | [**createTeamUsingPOST1**](docs/TeamControllerApi.md#createTeamUsingPOST1) | **POST** /data/game/{gameId}/team/{teamId} | Create team
*TeamControllerApi* | [**deleteTeamUsingDELETE1**](docs/TeamControllerApi.md#deleteTeamUsingDELETE1) | **DELETE** /data/game/{gameId}/team/{teamId} | Delte team
*TeamControllerApi* | [**readTeamMembersUsingGET**](docs/TeamControllerApi.md#readTeamMembersUsingGET) | **GET** /data/game/{gameId}/team/{teamId}/members | Get team members
*TeamControllerApi* | [**removeTeamMemberUsingDELETE**](docs/TeamControllerApi.md#removeTeamMemberUsingDELETE) | **DELETE** /data/game/{gameId}/team/{teamId}/members/{playerId} | Delete team member
*TeamControllerApi* | [**updateTeamMembersUsingPUT**](docs/TeamControllerApi.md#updateTeamMembersUsingPUT) | **PUT** /data/game/{gameId}/team/{teamId}/members | Edit team


## Documentation for Models

 - [ArchivedConcept](docs/ArchivedConcept.md)
 - [Attendee](docs/Attendee.md)
 - [AttendeeDTO](docs/AttendeeDTO.md)
 - [BadgeCollectionConcept](docs/BadgeCollectionConcept.md)
 - [ChallengeAssignmentDTO](docs/ChallengeAssignmentDTO.md)
 - [ChallengeChoice](docs/ChallengeChoice.md)
 - [ChallengeChoiceConfig](docs/ChallengeChoiceConfig.md)
 - [ChallengeConcept](docs/ChallengeConcept.md)
 - [ChallengeInvitation](docs/ChallengeInvitation.md)
 - [ChallengeModel](docs/ChallengeModel.md)
 - [ClassificationBoard](docs/ClassificationBoard.md)
 - [ClassificationDTO](docs/ClassificationDTO.md)
 - [ClassificationPosition](docs/ClassificationPosition.md)
 - [CollectionNotification](docs/CollectionNotification.md)
 - [Collectionstring](docs/Collectionstring.md)
 - [ComplexSearchQuery](docs/ComplexSearchQuery.md)
 - [Config](docs/Config.md)
 - [ExecutionDataDTO](docs/ExecutionDataDTO.md)
 - [GameConcept](docs/GameConcept.md)
 - [GameDTO](docs/GameDTO.md)
 - [GameStatistics](docs/GameStatistics.md)
 - [GeneralClassificationDTO](docs/GeneralClassificationDTO.md)
 - [GroupChallenge](docs/GroupChallenge.md)
 - [GroupChallengeDTO](docs/GroupChallengeDTO.md)
 - [IncrementalClassificationDTO](docs/IncrementalClassificationDTO.md)
 - [Inventory](docs/Inventory.md)
 - [ItemChoice](docs/ItemChoice.md)
 - [LevelDTO](docs/LevelDTO.md)
 - [Notification](docs/Notification.md)
 - [PagePlayerStateDTO](docs/PagePlayerStateDTO.md)
 - [PeriodInstanceImpl](docs/PeriodInstanceImpl.md)
 - [PeriodInternal](docs/PeriodInternal.md)
 - [Player](docs/Player.md)
 - [PlayerBlackList](docs/PlayerBlackList.md)
 - [PlayerLevel](docs/PlayerLevel.md)
 - [PlayerStateDTO](docs/PlayerStateDTO.md)
 - [PointConcept](docs/PointConcept.md)
 - [PointConceptDTO](docs/PointConceptDTO.md)
 - [PointConceptRef](docs/PointConceptRef.md)
 - [Projection](docs/Projection.md)
 - [QueryElement](docs/QueryElement.md)
 - [RawSearchQuery](docs/RawSearchQuery.md)
 - [Reward](docs/Reward.md)
 - [RewardDTO](docs/RewardDTO.md)
 - [RuleDTO](docs/RuleDTO.md)
 - [RuleValidateWrapper](docs/RuleValidateWrapper.md)
 - [Settings](docs/Settings.md)
 - [Sort](docs/Sort.md)
 - [SortItem](docs/SortItem.md)
 - [StructuredElement](docs/StructuredElement.md)
 - [StructuredProjection](docs/StructuredProjection.md)
 - [StructuredSortItem](docs/StructuredSortItem.md)
 - [TeamDTO](docs/TeamDTO.md)
 - [ThresholdDTO](docs/ThresholdDTO.md)
 - [WrapperQuery](docs/WrapperQuery.md)
