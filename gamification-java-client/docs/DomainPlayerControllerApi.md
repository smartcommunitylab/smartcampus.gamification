# DomainPlayerControllerApi

All URIs are relative to *https://localhost:6060/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**acceptChallengeUsingPOST**](DomainPlayerControllerApi.md#acceptChallengeUsingPOST) | **POST** /api/{domain}/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept | Accept challenge
[**activateChoiceUsingPOST**](DomainPlayerControllerApi.md#activateChoiceUsingPOST) | **POST** /api/{domain}/data/game/{gameId}/player/{playerId}/inventory/activate | Activate a choice
[**assignChallengeUsingPOST**](DomainPlayerControllerApi.md#assignChallengeUsingPOST) | **POST** /api/{domain}/data/game/{gameId}/player/{playerId}/challenges | Assign challenge
[**createPlayerUsingPOST1**](DomainPlayerControllerApi.md#createPlayerUsingPOST1) | **POST** /api/{domain}/data/game/{gameId}/player/{playerId} | Create player
[**deletePlayerUsingDELETE1**](DomainPlayerControllerApi.md#deletePlayerUsingDELETE1) | **DELETE** /api/{domain}/data/game/{gameId}/player/{playerId} | Delete player state
[**getPlayerChallengeUsingGET**](DomainPlayerControllerApi.md#getPlayerChallengeUsingGET) | **GET** /api/{domain}/data/game/{gameId}/player/{playerId}/challenges | Get player challenges
[**readCustomDataUsingGET**](DomainPlayerControllerApi.md#readCustomDataUsingGET) | **GET** /api/{domain}/data/game/{gameId}/player/{playerId}/custom | Get player custom data
[**readInventoryUsingGET**](DomainPlayerControllerApi.md#readInventoryUsingGET) | **GET** /api/{domain}/data/game/{gameId}/player/{playerId}/inventory | Get player inventory
[**readLevelsUsingGET**](DomainPlayerControllerApi.md#readLevelsUsingGET) | **GET** /api/{domain}/data/game/{gameId}/player/{playerId}/levels | Get player levels
[**readPlayerUsingGET**](DomainPlayerControllerApi.md#readPlayerUsingGET) | **GET** /api/{domain}/data/game/{gameId}/player/{playerId} | Get player state
[**readStateUsingGET**](DomainPlayerControllerApi.md#readStateUsingGET) | **GET** /api/{domain}/data/game/{gameId}/player/{playerId}/state | Get player state
[**readTeamsByMemberUsingGET1**](DomainPlayerControllerApi.md#readTeamsByMemberUsingGET1) | **GET** /api/{domain}/data/game/{gameId}/player/{playerId}/teams | Get player teams
[**searchByQueryUsingPOST**](DomainPlayerControllerApi.md#searchByQueryUsingPOST) | **POST** /api/{domain}/data/game/{gameId}/player/search | Search player states
[**updatePlayerUsingPUT**](DomainPlayerControllerApi.md#updatePlayerUsingPUT) | **PUT** /api/{domain}/data/game/{gameId}/player/{playerId} | Edit player state


<a name="acceptChallengeUsingPOST"></a>
# **acceptChallengeUsingPOST**
> ChallengeConcept acceptChallengeUsingPOST(domain, gameId, playerId, challengeName)

Accept challenge

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String challengeName = "challengeName_example"; // String | challengeName
try {
    ChallengeConcept result = apiInstance.acceptChallengeUsingPOST(domain, gameId, playerId, challengeName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#acceptChallengeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **challengeName** | **String**| challengeName |

### Return type

[**ChallengeConcept**](ChallengeConcept.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="activateChoiceUsingPOST"></a>
# **activateChoiceUsingPOST**
> Inventory activateChoiceUsingPOST(domain, gameId, playerId, choice)

Activate a choice

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
ItemChoice choice = new ItemChoice(); // ItemChoice | choice
try {
    Inventory result = apiInstance.activateChoiceUsingPOST(domain, gameId, playerId, choice);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#activateChoiceUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **choice** | [**ItemChoice**](ItemChoice.md)| choice |

### Return type

[**Inventory**](Inventory.md)

### Authorization

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
ChallengeAssignmentDTO challengeData = new ChallengeAssignmentDTO(); // ChallengeAssignmentDTO | challengeData
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.assignChallengeUsingPOST(challengeData, gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#assignChallengeUsingPOST");
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

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
PlayerStateDTO player = new PlayerStateDTO(); // PlayerStateDTO | player
try {
    apiInstance.createPlayerUsingPOST1(gameId, player);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#createPlayerUsingPOST1");
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

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.deletePlayerUsingDELETE1(gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#deletePlayerUsingDELETE1");
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

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getPlayerChallengeUsingGET"></a>
# **getPlayerChallengeUsingGET**
> getPlayerChallengeUsingGET(gameId, playerId)

Get player challenges

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.getPlayerChallengeUsingGET(gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#getPlayerChallengeUsingGET");
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

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readCustomDataUsingGET"></a>
# **readCustomDataUsingGET**
> PlayerStateDTO readCustomDataUsingGET(gameId, playerId)

Get player custom data

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readCustomDataUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readCustomDataUsingGET");
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

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readInventoryUsingGET"></a>
# **readInventoryUsingGET**
> Inventory readInventoryUsingGET(domain, gameId, playerId)

Get player inventory

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    Inventory result = apiInstance.readInventoryUsingGET(domain, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readInventoryUsingGET");
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

[**Inventory**](Inventory.md)

### Authorization

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<PlayerLevel> result = apiInstance.readLevelsUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readLevelsUsingGET");
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

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readPlayerUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readPlayerUsingGET");
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

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readStateUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readStateUsingGET");
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

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<TeamDTO> result = apiInstance.readTeamsByMemberUsingGET1(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readTeamsByMemberUsingGET1");
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

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
WrapperQuery query = new WrapperQuery(); // WrapperQuery | query
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    PagePlayerStateDTO result = apiInstance.searchByQueryUsingPOST(gameId, query, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#searchByQueryUsingPOST");
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

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updatePlayerUsingPUT"></a>
# **updatePlayerUsingPUT**
> updatePlayerUsingPUT(gameId, playerId)

Edit player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;


DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.updatePlayerUsingPUT(gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#updatePlayerUsingPUT");
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

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

