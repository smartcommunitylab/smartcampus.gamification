# ConsoleControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addBadgeUsingPOST1**](ConsoleControllerApi.md#addBadgeUsingPOST1) | **POST** /console/game/{gameId}/badgecoll | addBadge
[**addClassificationTaskUsingPOST1**](ConsoleControllerApi.md#addClassificationTaskUsingPOST1) | **POST** /console/game/{gameId}/task | addClassificationTask
[**addPointUsingPOST**](ConsoleControllerApi.md#addPointUsingPOST) | **POST** /console/game/{gameId}/point | addPoint
[**addRuleUsingPOST**](ConsoleControllerApi.md#addRuleUsingPOST) | **POST** /console/game/{gameId}/rule/db | addRule
[**createPlayerUsingPOST**](ConsoleControllerApi.md#createPlayerUsingPOST) | **POST** /console/game/{gameId}/player | createPlayer
[**createTeamUsingPOST**](ConsoleControllerApi.md#createTeamUsingPOST) | **POST** /console/game/{gameId}/team | createTeam
[**deleteClassificationTaskUsingPOST**](ConsoleControllerApi.md#deleteClassificationTaskUsingPOST) | **POST** /console/game/{gameId}/task/del | deleteClassificationTask
[**deleteDbRuleUsingDELETE**](ConsoleControllerApi.md#deleteDbRuleUsingDELETE) | **DELETE** /console/game/{gameId}/rule/db/{ruleUrl} | deleteDbRule
[**deleteGameUsingDELETE**](ConsoleControllerApi.md#deleteGameUsingDELETE) | **DELETE** /console/game/{gameId} | deleteGame
[**deletePlayerUsingDELETE**](ConsoleControllerApi.md#deletePlayerUsingDELETE) | **DELETE** /console/game/{gameId}/player/{playerId} | deletePlayer
[**deleteTeamUsingDELETE**](ConsoleControllerApi.md#deleteTeamUsingDELETE) | **DELETE** /console/game/{gameId}/team/{teamId} | deleteTeam
[**editClassificationTaskUsingPUT1**](ConsoleControllerApi.md#editClassificationTaskUsingPUT1) | **PUT** /console/game/{gameId}/task | editClassificationTask
[**readBadgeCollectionsUsingGET1**](ConsoleControllerApi.md#readBadgeCollectionsUsingGET1) | **GET** /console/game/{gameId}/badgecoll | readBadgeCollections
[**readDbRuleUsingGET**](ConsoleControllerApi.md#readDbRuleUsingGET) | **GET** /console/game/{gameId}/rule/db/{ruleUrl} | readDbRule
[**readGameUsingGET**](ConsoleControllerApi.md#readGameUsingGET) | **GET** /console/game/{gameId} | readGame
[**readGamesUsingGET**](ConsoleControllerApi.md#readGamesUsingGET) | **GET** /console/game | readGames
[**readPointsUsingGET**](ConsoleControllerApi.md#readPointsUsingGET) | **GET** /console/game/{gameId}/point | readPoints
[**readTeamsByMemberUsingGET**](ConsoleControllerApi.md#readTeamsByMemberUsingGET) | **GET** /console/game/{gameId}/player/{playerId}/teams | readTeamsByMember
[**saveGameUsingPOST1**](ConsoleControllerApi.md#saveGameUsingPOST1) | **POST** /console/game | saveGame
[**updateCustomDataUsingPUT**](ConsoleControllerApi.md#updateCustomDataUsingPUT) | **PUT** /console/game/{gameId}/player/{playerId} | updateCustomData
[**updateTeamMembersUsingPOST**](ConsoleControllerApi.md#updateTeamMembersUsingPOST) | **POST** /console/game/{gameId}/team/{teamId}/members | updateTeamMembers
[**validateRuleUsingPOST**](ConsoleControllerApi.md#validateRuleUsingPOST) | **POST** /console/rule/validate | validateRule


<a name="addBadgeUsingPOST1"></a>
# **addBadgeUsingPOST1**
> addBadgeUsingPOST1(gameId, badge)

addBadge

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
BadgeCollectionConcept badge = new BadgeCollectionConcept(); // BadgeCollectionConcept | badge
try {
    apiInstance.addBadgeUsingPOST1(gameId, badge);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#addBadgeUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **badge** | [**BadgeCollectionConcept**](BadgeCollectionConcept.md)| badge |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="addClassificationTaskUsingPOST1"></a>
# **addClassificationTaskUsingPOST1**
> GeneralClassificationDTO addClassificationTaskUsingPOST1(gameId, task)

addClassificationTask

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    GeneralClassificationDTO result = apiInstance.addClassificationTaskUsingPOST1(gameId, task);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#addClassificationTaskUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

[**GeneralClassificationDTO**](GeneralClassificationDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="addPointUsingPOST"></a>
# **addPointUsingPOST**
> addPointUsingPOST(gameId, point)

addPoint

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
PointConcept point = new PointConcept(); // PointConcept | point
try {
    apiInstance.addPointUsingPOST(gameId, point);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#addPointUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **point** | [**PointConcept**](PointConcept.md)| point |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="addRuleUsingPOST"></a>
# **addRuleUsingPOST**
> RuleDTO addRuleUsingPOST(gameId, rule)

addRule

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
RuleDTO rule = new RuleDTO(); // RuleDTO | rule
try {
    RuleDTO result = apiInstance.addRuleUsingPOST(gameId, rule);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#addRuleUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **rule** | [**RuleDTO**](RuleDTO.md)| rule |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createPlayerUsingPOST"></a>
# **createPlayerUsingPOST**
> createPlayerUsingPOST(gameId, player)

createPlayer

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
PlayerStateDTO player = new PlayerStateDTO(); // PlayerStateDTO | player
try {
    apiInstance.createPlayerUsingPOST(gameId, player);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#createPlayerUsingPOST");
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

<a name="createTeamUsingPOST"></a>
# **createTeamUsingPOST**
> createTeamUsingPOST(gameId, team)

createTeam

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
TeamDTO team = new TeamDTO(); // TeamDTO | team
try {
    apiInstance.createTeamUsingPOST(gameId, team);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#createTeamUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **team** | [**TeamDTO**](TeamDTO.md)| team |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteClassificationTaskUsingPOST"></a>
# **deleteClassificationTaskUsingPOST**
> deleteClassificationTaskUsingPOST(gameId, task)

deleteClassificationTask

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    apiInstance.deleteClassificationTaskUsingPOST(gameId, task);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#deleteClassificationTaskUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDbRuleUsingDELETE"></a>
# **deleteDbRuleUsingDELETE**
> Boolean deleteDbRuleUsingDELETE(gameId, ruleUrl)

deleteDbRule

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
String ruleUrl = "ruleUrl_example"; // String | ruleUrl
try {
    Boolean result = apiInstance.deleteDbRuleUsingDELETE(gameId, ruleUrl);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#deleteDbRuleUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **ruleUrl** | **String**| ruleUrl |

### Return type

**Boolean**

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteGameUsingDELETE"></a>
# **deleteGameUsingDELETE**
> deleteGameUsingDELETE(gameId)

deleteGame

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.deleteGameUsingDELETE(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#deleteGameUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deletePlayerUsingDELETE"></a>
# **deletePlayerUsingDELETE**
> deletePlayerUsingDELETE(gameId, playerId)

deletePlayer

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.deletePlayerUsingDELETE(gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#deletePlayerUsingDELETE");
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

<a name="deleteTeamUsingDELETE"></a>
# **deleteTeamUsingDELETE**
> deleteTeamUsingDELETE(gameId, teamId)

deleteTeam

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
try {
    apiInstance.deleteTeamUsingDELETE(gameId, teamId);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#deleteTeamUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editClassificationTaskUsingPUT1"></a>
# **editClassificationTaskUsingPUT1**
> editClassificationTaskUsingPUT1(gameId, task)

editClassificationTask

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    apiInstance.editClassificationTaskUsingPUT1(gameId, task);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#editClassificationTaskUsingPUT1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readBadgeCollectionsUsingGET1"></a>
# **readBadgeCollectionsUsingGET1**
> List&lt;BadgeCollectionConcept&gt; readBadgeCollectionsUsingGET1(gameId)

readBadgeCollections

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<BadgeCollectionConcept> result = apiInstance.readBadgeCollectionsUsingGET1(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#readBadgeCollectionsUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;BadgeCollectionConcept&gt;**](BadgeCollectionConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readDbRuleUsingGET"></a>
# **readDbRuleUsingGET**
> RuleDTO readDbRuleUsingGET(gameId, ruleUrl)

readDbRule

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
String ruleUrl = "ruleUrl_example"; // String | ruleUrl
try {
    RuleDTO result = apiInstance.readDbRuleUsingGET(gameId, ruleUrl);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#readDbRuleUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **ruleUrl** | **String**| ruleUrl |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGameUsingGET"></a>
# **readGameUsingGET**
> GameDTO readGameUsingGET(gameId)

readGame

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    GameDTO result = apiInstance.readGameUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#readGameUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**GameDTO**](GameDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGamesUsingGET"></a>
# **readGamesUsingGET**
> List&lt;GameDTO&gt; readGamesUsingGET()

readGames

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
try {
    List<GameDTO> result = apiInstance.readGamesUsingGET();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#readGamesUsingGET");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

[**List&lt;GameDTO&gt;**](GameDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPointsUsingGET"></a>
# **readPointsUsingGET**
> List&lt;PointConcept&gt; readPointsUsingGET(gameId)

readPoints

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<PointConcept> result = apiInstance.readPointsUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#readPointsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;PointConcept&gt;**](PointConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamsByMemberUsingGET"></a>
# **readTeamsByMemberUsingGET**
> List&lt;TeamDTO&gt; readTeamsByMemberUsingGET(gameId, playerId)

readTeamsByMember

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<TeamDTO> result = apiInstance.readTeamsByMemberUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#readTeamsByMemberUsingGET");
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

<a name="saveGameUsingPOST1"></a>
# **saveGameUsingPOST1**
> GameDTO saveGameUsingPOST1(game)

saveGame

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
GameDTO game = new GameDTO(); // GameDTO | game
try {
    GameDTO result = apiInstance.saveGameUsingPOST1(game);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#saveGameUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **game** | [**GameDTO**](GameDTO.md)| game |

### Return type

[**GameDTO**](GameDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateCustomDataUsingPUT"></a>
# **updateCustomDataUsingPUT**
> PlayerStateDTO updateCustomDataUsingPUT(gameId, playerId, customData)

updateCustomData

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Object customData = null; // Object | customData
try {
    PlayerStateDTO result = apiInstance.updateCustomDataUsingPUT(gameId, playerId, customData);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#updateCustomDataUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **customData** | **Object**| customData |

### Return type

[**PlayerStateDTO**](PlayerStateDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateTeamMembersUsingPOST"></a>
# **updateTeamMembersUsingPOST**
> updateTeamMembersUsingPOST(gameId, teamId, members)

updateTeamMembers

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.updateTeamMembersUsingPOST(gameId, teamId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#updateTeamMembersUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **members** | **List&lt;String&gt;**| members |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="validateRuleUsingPOST"></a>
# **validateRuleUsingPOST**
> List&lt;String&gt; validateRuleUsingPOST(ruleContent)

validateRule

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ConsoleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ConsoleControllerApi apiInstance = new ConsoleControllerApi();
String ruleContent = "ruleContent_example"; // String | ruleContent
try {
    List<String> result = apiInstance.validateRuleUsingPOST(ruleContent);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ConsoleControllerApi#validateRuleUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **ruleContent** | **String**| ruleContent |

### Return type

**List&lt;String&gt;**

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

