# DomainConsoleControllerApi

All URIs are relative to *https://localhost:6060/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addBadgeUsingPOST1**](DomainConsoleControllerApi.md#addBadgeUsingPOST1) | **POST** /api/{domain}/console/game/{gameId}/badgecoll | addBadge
[**addClassificationTaskUsingPOST1**](DomainConsoleControllerApi.md#addClassificationTaskUsingPOST1) | **POST** /api/{domain}/console/game/{gameId}/task | addClassificationTask
[**addPointUsingPOST**](DomainConsoleControllerApi.md#addPointUsingPOST) | **POST** /api/{domain}/console/game/{gameId}/point | addPoint
[**addRuleUsingPOST**](DomainConsoleControllerApi.md#addRuleUsingPOST) | **POST** /api/{domain}/console/game/{gameId}/rule/db | addRule
[**createPlayerUsingPOST**](DomainConsoleControllerApi.md#createPlayerUsingPOST) | **POST** /api/{domain}/console/game/{gameId}/player | createPlayer
[**createTeamUsingPOST**](DomainConsoleControllerApi.md#createTeamUsingPOST) | **POST** /api/{domain}/console/game/{gameId}/team | createTeam
[**deleteClassificationTaskUsingPOST**](DomainConsoleControllerApi.md#deleteClassificationTaskUsingPOST) | **POST** /api/{domain}/console/game/{gameId}/task/del | deleteClassificationTask
[**deleteDbRuleUsingDELETE**](DomainConsoleControllerApi.md#deleteDbRuleUsingDELETE) | **DELETE** /api/{domain}/console/game/{gameId}/rule/db/{ruleUrl} | deleteDbRule
[**deleteGameUsingDELETE**](DomainConsoleControllerApi.md#deleteGameUsingDELETE) | **DELETE** /api/{domain}/console/game/{gameId} | deleteGame
[**deletePlayerUsingDELETE**](DomainConsoleControllerApi.md#deletePlayerUsingDELETE) | **DELETE** /api/{domain}/console/game/{gameId}/player/{playerId} | deletePlayer
[**deleteTeamUsingDELETE**](DomainConsoleControllerApi.md#deleteTeamUsingDELETE) | **DELETE** /api/{domain}/console/game/{gameId}/team/{teamId} | deleteTeam
[**editClassificationTaskUsingPUT1**](DomainConsoleControllerApi.md#editClassificationTaskUsingPUT1) | **PUT** /api/{domain}/console/game/{gameId}/task | editClassificationTask
[**readBadgeCollectionsUsingGET1**](DomainConsoleControllerApi.md#readBadgeCollectionsUsingGET1) | **GET** /api/{domain}/console/game/{gameId}/badgecoll | readBadgeCollections
[**readDbRuleUsingGET**](DomainConsoleControllerApi.md#readDbRuleUsingGET) | **GET** /api/{domain}/console/game/{gameId}/rule/db/{ruleUrl} | readDbRule
[**readGameUsingGET**](DomainConsoleControllerApi.md#readGameUsingGET) | **GET** /api/{domain}/console/game/{gameId} | readGame
[**readGamesUsingGET**](DomainConsoleControllerApi.md#readGamesUsingGET) | **GET** /api/{domain}/console/game | readGames
[**readPointsUsingGET**](DomainConsoleControllerApi.md#readPointsUsingGET) | **GET** /api/{domain}/console/game/{gameId}/point | readPoints
[**readTeamsByMemberUsingGET**](DomainConsoleControllerApi.md#readTeamsByMemberUsingGET) | **GET** /api/{domain}/console/game/{gameId}/player/{playerId}/teams | readTeamsByMember
[**saveGameUsingPOST1**](DomainConsoleControllerApi.md#saveGameUsingPOST1) | **POST** /api/{domain}/console/game | saveGame
[**updateCustomDataUsingPUT**](DomainConsoleControllerApi.md#updateCustomDataUsingPUT) | **PUT** /api/{domain}/console/game/{gameId}/player/{playerId} | updateCustomData
[**updateTeamMembersUsingPOST**](DomainConsoleControllerApi.md#updateTeamMembersUsingPOST) | **POST** /api/{domain}/console/game/{gameId}/team/{teamId}/members | updateTeamMembers
[**validateRuleUsingPOST**](DomainConsoleControllerApi.md#validateRuleUsingPOST) | **POST** /api/{domain}/console/rule/validate | validateRule


<a name="addBadgeUsingPOST1"></a>
# **addBadgeUsingPOST1**
> addBadgeUsingPOST1(domain, gameId, badge)

addBadge

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
BadgeCollectionConcept badge = new BadgeCollectionConcept(); // BadgeCollectionConcept | badge
try {
    apiInstance.addBadgeUsingPOST1(domain, gameId, badge);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#addBadgeUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **badge** | [**BadgeCollectionConcept**](BadgeCollectionConcept.md)| badge |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="addClassificationTaskUsingPOST1"></a>
# **addClassificationTaskUsingPOST1**
> GeneralClassificationDTO addClassificationTaskUsingPOST1(domain, gameId, task)

addClassificationTask

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    GeneralClassificationDTO result = apiInstance.addClassificationTaskUsingPOST1(domain, gameId, task);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#addClassificationTaskUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

[**GeneralClassificationDTO**](GeneralClassificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="addPointUsingPOST"></a>
# **addPointUsingPOST**
> addPointUsingPOST(domain, gameId, point)

addPoint

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
PointConcept point = new PointConcept(); // PointConcept | point
try {
    apiInstance.addPointUsingPOST(domain, gameId, point);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#addPointUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **point** | [**PointConcept**](PointConcept.md)| point |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="addRuleUsingPOST"></a>
# **addRuleUsingPOST**
> RuleDTO addRuleUsingPOST(domain, gameId, rule)

addRule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
RuleDTO rule = new RuleDTO(); // RuleDTO | rule
try {
    RuleDTO result = apiInstance.addRuleUsingPOST(domain, gameId, rule);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#addRuleUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **rule** | [**RuleDTO**](RuleDTO.md)| rule |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createPlayerUsingPOST"></a>
# **createPlayerUsingPOST**
> createPlayerUsingPOST(domain, gameId, player)

createPlayer

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
PlayerStateDTO player = new PlayerStateDTO(); // PlayerStateDTO | player
try {
    apiInstance.createPlayerUsingPOST(domain, gameId, player);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#createPlayerUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **player** | [**PlayerStateDTO**](PlayerStateDTO.md)| player |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createTeamUsingPOST"></a>
# **createTeamUsingPOST**
> createTeamUsingPOST(domain, gameId, team)

createTeam

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
TeamDTO team = new TeamDTO(); // TeamDTO | team
try {
    apiInstance.createTeamUsingPOST(domain, gameId, team);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#createTeamUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **team** | [**TeamDTO**](TeamDTO.md)| team |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteClassificationTaskUsingPOST"></a>
# **deleteClassificationTaskUsingPOST**
> deleteClassificationTaskUsingPOST(domain, gameId, task)

deleteClassificationTask

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    apiInstance.deleteClassificationTaskUsingPOST(domain, gameId, task);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#deleteClassificationTaskUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDbRuleUsingDELETE"></a>
# **deleteDbRuleUsingDELETE**
> Boolean deleteDbRuleUsingDELETE(domain, gameId, ruleUrl)

deleteDbRule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String ruleUrl = "ruleUrl_example"; // String | ruleUrl
try {
    Boolean result = apiInstance.deleteDbRuleUsingDELETE(domain, gameId, ruleUrl);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#deleteDbRuleUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **ruleUrl** | **String**| ruleUrl |

### Return type

**Boolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteGameUsingDELETE"></a>
# **deleteGameUsingDELETE**
> deleteGameUsingDELETE(domain, gameId)

deleteGame

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.deleteGameUsingDELETE(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#deleteGameUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deletePlayerUsingDELETE"></a>
# **deletePlayerUsingDELETE**
> deletePlayerUsingDELETE(domain, gameId, playerId)

deletePlayer

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.deletePlayerUsingDELETE(domain, gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#deletePlayerUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteTeamUsingDELETE"></a>
# **deleteTeamUsingDELETE**
> deleteTeamUsingDELETE(domain, gameId, teamId)

deleteTeam

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
try {
    apiInstance.deleteTeamUsingDELETE(domain, gameId, teamId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#deleteTeamUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editClassificationTaskUsingPUT1"></a>
# **editClassificationTaskUsingPUT1**
> editClassificationTaskUsingPUT1(domain, gameId, task)

editClassificationTask

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    apiInstance.editClassificationTaskUsingPUT1(domain, gameId, task);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#editClassificationTaskUsingPUT1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readBadgeCollectionsUsingGET1"></a>
# **readBadgeCollectionsUsingGET1**
> List&lt;BadgeCollectionConcept&gt; readBadgeCollectionsUsingGET1(domain, gameId)

readBadgeCollections

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<BadgeCollectionConcept> result = apiInstance.readBadgeCollectionsUsingGET1(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#readBadgeCollectionsUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;BadgeCollectionConcept&gt;**](BadgeCollectionConcept.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readDbRuleUsingGET"></a>
# **readDbRuleUsingGET**
> RuleDTO readDbRuleUsingGET(domain, gameId, ruleUrl)

readDbRule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String ruleUrl = "ruleUrl_example"; // String | ruleUrl
try {
    RuleDTO result = apiInstance.readDbRuleUsingGET(domain, gameId, ruleUrl);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#readDbRuleUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **ruleUrl** | **String**| ruleUrl |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGameUsingGET"></a>
# **readGameUsingGET**
> GameDTO readGameUsingGET(domain, gameId)

readGame

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    GameDTO result = apiInstance.readGameUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#readGameUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**GameDTO**](GameDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGamesUsingGET"></a>
# **readGamesUsingGET**
> List&lt;GameDTO&gt; readGamesUsingGET(domain)

readGames

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
try {
    List<GameDTO> result = apiInstance.readGamesUsingGET(domain);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#readGamesUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |

### Return type

[**List&lt;GameDTO&gt;**](GameDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPointsUsingGET"></a>
# **readPointsUsingGET**
> List&lt;PointConcept&gt; readPointsUsingGET(domain, gameId)

readPoints

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<PointConcept> result = apiInstance.readPointsUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#readPointsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;PointConcept&gt;**](PointConcept.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamsByMemberUsingGET"></a>
# **readTeamsByMemberUsingGET**
> List&lt;TeamDTO&gt; readTeamsByMemberUsingGET(domain, gameId, playerId)

readTeamsByMember

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<TeamDTO> result = apiInstance.readTeamsByMemberUsingGET(domain, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#readTeamsByMemberUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

[**List&lt;TeamDTO&gt;**](TeamDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="saveGameUsingPOST1"></a>
# **saveGameUsingPOST1**
> GameDTO saveGameUsingPOST1(domain, game)

saveGame

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
GameDTO game = new GameDTO(); // GameDTO | game
try {
    GameDTO result = apiInstance.saveGameUsingPOST1(domain, game);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#saveGameUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **game** | [**GameDTO**](GameDTO.md)| game |

### Return type

[**GameDTO**](GameDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateCustomDataUsingPUT"></a>
# **updateCustomDataUsingPUT**
> PlayerStateDTO updateCustomDataUsingPUT(domain, gameId, playerId, customData)

updateCustomData

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Object customData = null; // Object | customData
try {
    PlayerStateDTO result = apiInstance.updateCustomDataUsingPUT(domain, gameId, playerId, customData);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#updateCustomDataUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **customData** | **Object**| customData |

### Return type

[**PlayerStateDTO**](PlayerStateDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateTeamMembersUsingPOST"></a>
# **updateTeamMembersUsingPOST**
> updateTeamMembersUsingPOST(domain, gameId, teamId, members)

updateTeamMembers

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.updateTeamMembersUsingPOST(domain, gameId, teamId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#updateTeamMembersUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **members** | **List&lt;String&gt;**| members |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="validateRuleUsingPOST"></a>
# **validateRuleUsingPOST**
> List&lt;String&gt; validateRuleUsingPOST(domain, ruleContent)

validateRule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainConsoleControllerApi;


DomainConsoleControllerApi apiInstance = new DomainConsoleControllerApi();
String domain = "domain_example"; // String | domain
String ruleContent = "ruleContent_example"; // String | ruleContent
try {
    List<String> result = apiInstance.validateRuleUsingPOST(domain, ruleContent);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainConsoleControllerApi#validateRuleUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **ruleContent** | **String**| ruleContent |

### Return type

**List&lt;String&gt;**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

