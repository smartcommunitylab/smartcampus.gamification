# PlayerControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**acceptChallengeUsingPOST**](PlayerControllerApi.md#acceptChallengeUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept | Accept challenge
[**acceptInvitationUsingPOST**](PlayerControllerApi.md#acceptInvitationUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation/accept/{challengeName} | acceptInvitation
[**activateChoiceUsingPOST**](PlayerControllerApi.md#activateChoiceUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/inventory/activate | Activate a choice
[**assignChallengeUsingPOST**](PlayerControllerApi.md#assignChallengeUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/challenges | Assign challenge
[**assignGroupChallengeUsingPOST**](PlayerControllerApi.md#assignGroupChallengeUsingPOST) | **POST** /data/game/{gameId}/group-challenges | assignGroupChallenge
[**blockPlayerUsingPOST**](PlayerControllerApi.md#blockPlayerUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/block/{otherPlayerId} | Add another player to challenge block list
[**cancelInvitationUsingPOST**](PlayerControllerApi.md#cancelInvitationUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation/cancel/{challengeName} | cancelInvitation
[**createPlayerUsingPOST1**](PlayerControllerApi.md#createPlayerUsingPOST1) | **POST** /data/game/{gameId}/player/{playerId} | Create player
[**deletePlayerUsingDELETE1**](PlayerControllerApi.md#deletePlayerUsingDELETE1) | **DELETE** /data/game/{gameId}/player/{playerId} | Delete player state
[**getPlayerChallengeUsingGET**](PlayerControllerApi.md#getPlayerChallengeUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/challenges | Get player challenges
[**inviteIntoAChallengeUsingPOST**](PlayerControllerApi.md#inviteIntoAChallengeUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation | inviteIntoAChallenge
[**readInventoryUsingGET**](PlayerControllerApi.md#readInventoryUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/inventory | Get player inventory
[**readLevelsUsingGET**](PlayerControllerApi.md#readLevelsUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/levels | Get player levels
[**readPlayerBlackListUsingGET**](PlayerControllerApi.md#readPlayerBlackListUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/blacklist | Get player black list of other players
[**readPlayerUsingGET**](PlayerControllerApi.md#readPlayerUsingGET) | **GET** /data/game/{gameId}/player/{playerId} | Get player state
[**readStateUsingGET**](PlayerControllerApi.md#readStateUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/state | Get player state
[**readSystemPlayerStateUsingGET**](PlayerControllerApi.md#readSystemPlayerStateUsingGET) | **GET** /data/game/{gameId}/player/{playerId}/challengers | Get availabe challengers for the player
[**readTeamsByMemberUsingGET1**](PlayerControllerApi.md#readTeamsByMemberUsingGET1) | **GET** /data/game/{gameId}/player/{playerId}/teams | Get player teams
[**refuseInvitationUsingPOST**](PlayerControllerApi.md#refuseInvitationUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/invitation/refuse/{challengeName} | refuseInvitation
[**searchByQueryUsingPOST**](PlayerControllerApi.md#searchByQueryUsingPOST) | **POST** /data/game/{gameId}/player/search | Search player states
[**unBlockPlayerUsingPOST**](PlayerControllerApi.md#unBlockPlayerUsingPOST) | **POST** /data/game/{gameId}/player/{playerId}/unblock/{otherPlayerId} | Unblock another player from challenge block list


<a name="acceptChallengeUsingPOST"></a>
# **acceptChallengeUsingPOST**
> ChallengeConcept acceptChallengeUsingPOST(gameId, playerId, challengeName)

Accept challenge

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String challengeName = "challengeName_example"; // String | challengeName
try {
    ChallengeConcept result = apiInstance.acceptChallengeUsingPOST(gameId, playerId, challengeName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#acceptChallengeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **challengeName** | **String**| challengeName |

### Return type

[**ChallengeConcept**](ChallengeConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="acceptInvitationUsingPOST"></a>
# **acceptInvitationUsingPOST**
> acceptInvitationUsingPOST(gameId, playerId, challengeName)

acceptInvitation

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String challengeName = "challengeName_example"; // String | challengeName
try {
    apiInstance.acceptInvitationUsingPOST(gameId, playerId, challengeName);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#acceptInvitationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **challengeName** | **String**| challengeName |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="activateChoiceUsingPOST"></a>
# **activateChoiceUsingPOST**
> Inventory activateChoiceUsingPOST(gameId, playerId, choice)

Activate a choice

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
ItemChoice choice = new ItemChoice(); // ItemChoice | choice
try {
    Inventory result = apiInstance.activateChoiceUsingPOST(gameId, playerId, choice);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#activateChoiceUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **choice** | [**ItemChoice**](ItemChoice.md)| choice |

### Return type

[**Inventory**](Inventory.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="assignChallengeUsingPOST"></a>
# **assignChallengeUsingPOST**
> assignChallengeUsingPOST(challengeData, gameId, playerId)

Assign challenge

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
ChallengeAssignmentDTO challengeData = new ChallengeAssignmentDTO(); // ChallengeAssignmentDTO | challengeData
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.assignChallengeUsingPOST(challengeData, gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#assignChallengeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challengeData** | [**ChallengeAssignmentDTO**](ChallengeAssignmentDTO.md)| challengeData |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="assignGroupChallengeUsingPOST"></a>
# **assignGroupChallengeUsingPOST**
> assignGroupChallengeUsingPOST(challengeData, gameId)

assignGroupChallenge

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
GroupChallengeDTO challengeData = new GroupChallengeDTO(); // GroupChallengeDTO | challengeData
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.assignGroupChallengeUsingPOST(challengeData, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#assignGroupChallengeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challengeData** | [**GroupChallengeDTO**](GroupChallengeDTO.md)| challengeData |
 **gameId** | **String**| gameId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="blockPlayerUsingPOST"></a>
# **blockPlayerUsingPOST**
> PlayerBlackList blockPlayerUsingPOST(gameId, playerId, otherPlayerId)

Add another player to challenge block list

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String otherPlayerId = "otherPlayerId_example"; // String | otherPlayerId
try {
    PlayerBlackList result = apiInstance.blockPlayerUsingPOST(gameId, playerId, otherPlayerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#blockPlayerUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **otherPlayerId** | **String**| otherPlayerId |

### Return type

[**PlayerBlackList**](PlayerBlackList.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="cancelInvitationUsingPOST"></a>
# **cancelInvitationUsingPOST**
> cancelInvitationUsingPOST(gameId, playerId, challengeName)

cancelInvitation

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String challengeName = "challengeName_example"; // String | challengeName
try {
    apiInstance.cancelInvitationUsingPOST(gameId, playerId, challengeName);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#cancelInvitationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **challengeName** | **String**| challengeName |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createPlayerUsingPOST1"></a>
# **createPlayerUsingPOST1**
> createPlayerUsingPOST1(gameId, player)

Create player

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
PlayerStateDTO player = new PlayerStateDTO(); // PlayerStateDTO | player
try {
    apiInstance.createPlayerUsingPOST1(gameId, player);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#createPlayerUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **player** | [**PlayerStateDTO**](PlayerStateDTO.md)| player |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deletePlayerUsingDELETE1"></a>
# **deletePlayerUsingDELETE1**
> deletePlayerUsingDELETE1(gameId, playerId)

Delete player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.deletePlayerUsingDELETE1(gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#deletePlayerUsingDELETE1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getPlayerChallengeUsingGET"></a>
# **getPlayerChallengeUsingGET**
> List&lt;ChallengeConcept&gt; getPlayerChallengeUsingGET(gameId, playerId)

Get player challenges

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<ChallengeConcept> result = apiInstance.getPlayerChallengeUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#getPlayerChallengeUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**List&lt;ChallengeConcept&gt;**](ChallengeConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="inviteIntoAChallengeUsingPOST"></a>
# **inviteIntoAChallengeUsingPOST**
> ChallengeInvitation inviteIntoAChallengeUsingPOST(invitation, gameId, playerId)

inviteIntoAChallenge

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
ChallengeInvitation invitation = new ChallengeInvitation(); // ChallengeInvitation | invitation
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    ChallengeInvitation result = apiInstance.inviteIntoAChallengeUsingPOST(invitation, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#inviteIntoAChallengeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **invitation** | [**ChallengeInvitation**](ChallengeInvitation.md)| invitation |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**ChallengeInvitation**](ChallengeInvitation.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readInventoryUsingGET"></a>
# **readInventoryUsingGET**
> Inventory readInventoryUsingGET(gameId, playerId)

Get player inventory

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    Inventory result = apiInstance.readInventoryUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#readInventoryUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**Inventory**](Inventory.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readLevelsUsingGET"></a>
# **readLevelsUsingGET**
> List&lt;PlayerLevel&gt; readLevelsUsingGET(gameId, playerId)

Get player levels

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<PlayerLevel> result = apiInstance.readLevelsUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#readLevelsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**List&lt;PlayerLevel&gt;**](PlayerLevel.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerBlackListUsingGET"></a>
# **readPlayerBlackListUsingGET**
> PlayerBlackList readPlayerBlackListUsingGET(gameId, playerId)

Get player black list of other players

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerBlackList result = apiInstance.readPlayerBlackListUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#readPlayerBlackListUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**PlayerBlackList**](PlayerBlackList.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerUsingGET"></a>
# **readPlayerUsingGET**
> PlayerStateDTO readPlayerUsingGET(gameId, playerId)

Get player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readPlayerUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#readPlayerUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**PlayerStateDTO**](PlayerStateDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readStateUsingGET"></a>
# **readStateUsingGET**
> PlayerStateDTO readStateUsingGET(gameId, playerId)

Get player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readStateUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#readStateUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**PlayerStateDTO**](PlayerStateDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readSystemPlayerStateUsingGET"></a>
# **readSystemPlayerStateUsingGET**
> List&lt;String&gt; readSystemPlayerStateUsingGET(gameId, playerId, conceptName)

Get availabe challengers for the player

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String conceptName = "conceptName_example"; // String | conceptName
try {
    List<String> result = apiInstance.readSystemPlayerStateUsingGET(gameId, playerId, conceptName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#readSystemPlayerStateUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **conceptName** | **String**| conceptName | [optional]

### Return type

**List&lt;String&gt;**

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamsByMemberUsingGET1"></a>
# **readTeamsByMemberUsingGET1**
> List&lt;TeamDTO&gt; readTeamsByMemberUsingGET1(gameId, playerId)

Get player teams

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<TeamDTO> result = apiInstance.readTeamsByMemberUsingGET1(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#readTeamsByMemberUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**List&lt;TeamDTO&gt;**](TeamDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="refuseInvitationUsingPOST"></a>
# **refuseInvitationUsingPOST**
> refuseInvitationUsingPOST(gameId, playerId, challengeName)

refuseInvitation

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String challengeName = "challengeName_example"; // String | challengeName
try {
    apiInstance.refuseInvitationUsingPOST(gameId, playerId, challengeName);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#refuseInvitationUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **challengeName** | **String**| challengeName |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="searchByQueryUsingPOST"></a>
# **searchByQueryUsingPOST**
> PagePlayerStateDTO searchByQueryUsingPOST(gameId, query, page, size)

Search player states

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
WrapperQuery query = new WrapperQuery(); // WrapperQuery | query
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    PagePlayerStateDTO result = apiInstance.searchByQueryUsingPOST(gameId, query, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#searchByQueryUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **query** | [**WrapperQuery**](WrapperQuery.md)| query |
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**PagePlayerStateDTO**](PagePlayerStateDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="unBlockPlayerUsingPOST"></a>
# **unBlockPlayerUsingPOST**
> PlayerBlackList unBlockPlayerUsingPOST(gameId, playerId, otherPlayerId)

Unblock another player from challenge block list

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.PlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PlayerControllerApi apiInstance = new PlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String otherPlayerId = "otherPlayerId_example"; // String | otherPlayerId
try {
    PlayerBlackList result = apiInstance.unBlockPlayerUsingPOST(gameId, playerId, otherPlayerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PlayerControllerApi#unBlockPlayerUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **otherPlayerId** | **String**| otherPlayerId |

### Return type

[**PlayerBlackList**](PlayerBlackList.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

