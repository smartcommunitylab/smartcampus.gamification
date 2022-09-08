# DomainPlayerControllerApi

All URIs are relative to *https://localhost/gamification*

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
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

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

[oauth2](../README.md#oauth2)

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
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="assignChallengeUsingPOST"></a>
# **assignChallengeUsingPOST**
> assignChallengeUsingPOST(domain, challengeData, gameId, playerId)

Assign challenge

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
it.smartcommunitylab.model.ext.ChallengeAssignmentDTO challengeData = new it.smartcommunitylab.model.ext.ChallengeAssignmentDTO(); // it.smartcommunitylab.model.ext.ChallengeAssignmentDTO | challengeData
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.assignChallengeUsingPOST(domain, challengeData, gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#assignChallengeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **challengeData** | [**it.smartcommunitylab.model.ext.ChallengeAssignmentDTO**](it.smartcommunitylab.model.ext.ChallengeAssignmentDTO.md)| challengeData |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createPlayerUsingPOST1"></a>
# **createPlayerUsingPOST1**
> createPlayerUsingPOST1(domain, gameId, player)

Create player

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
PlayerStateDTO player = new PlayerStateDTO(); // PlayerStateDTO | player
try {
    apiInstance.createPlayerUsingPOST1(domain, gameId, player);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#createPlayerUsingPOST1");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deletePlayerUsingDELETE1"></a>
# **deletePlayerUsingDELETE1**
> deletePlayerUsingDELETE1(domain, gameId, playerId)

Delete player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.deletePlayerUsingDELETE1(domain, gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#deletePlayerUsingDELETE1");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getPlayerChallengeUsingGET"></a>
# **getPlayerChallengeUsingGET**
> List&lt;ChallengeConcept&gt; getPlayerChallengeUsingGET(domain, gameId, playerId)

Get player challenges

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<ChallengeConcept> result = apiInstance.getPlayerChallengeUsingGET(domain, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#getPlayerChallengeUsingGET");
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

[**List&lt;ChallengeConcept&gt;**](ChallengeConcept.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readCustomDataUsingGET"></a>
# **readCustomDataUsingGET**
> PlayerStateDTO readCustomDataUsingGET(domain, gameId, playerId)

Get player custom data

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readCustomDataUsingGET(domain, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readCustomDataUsingGET");
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

[**PlayerStateDTO**](PlayerStateDTO.md)

### Authorization

[oauth2](../README.md#oauth2)

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
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readLevelsUsingGET"></a>
# **readLevelsUsingGET**
> List&lt;it.smartcommunitylab.model.ext.PlayerLevel&gt; readLevelsUsingGET(domain, gameId, playerId)

Get player levels

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<it.smartcommunitylab.model.ext.PlayerLevel> result = apiInstance.readLevelsUsingGET(domain, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readLevelsUsingGET");
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

[**List&lt;it.smartcommunitylab.model.ext.PlayerLevel&gt;**](it.smartcommunitylab.model.ext.PlayerLevel.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerUsingGET"></a>
# **readPlayerUsingGET**
> PlayerStateDTO readPlayerUsingGET(domain, gameId, playerId, readChallenges, points, badges)

Get player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Boolean readChallenges = true; // Boolean | readChallenges
List<String> points = Arrays.asList("points_example"); // List<String> | 
List<String> badges = Arrays.asList("badges_example"); // List<String> | 
try {
    PlayerStateDTO result = apiInstance.readPlayerUsingGET(domain, gameId, playerId, readChallenges, points, badges);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readPlayerUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **readChallenges** | **Boolean**| readChallenges | [optional]
 **points** | [**List&lt;String&gt;**](String.md)|  | [optional]
 **badges** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**PlayerStateDTO**](PlayerStateDTO.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readStateUsingGET"></a>
# **readStateUsingGET**
> PlayerStateDTO readStateUsingGET(domain, gameId, playerId, points, badges)

Get player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
List<String> points = Arrays.asList("points_example"); // List<String> | 
List<String> badges = Arrays.asList("badges_example"); // List<String> | 
try {
    PlayerStateDTO result = apiInstance.readStateUsingGET(domain, gameId, playerId, points, badges);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readStateUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **points** | [**List&lt;String&gt;**](String.md)|  | [optional]
 **badges** | [**List&lt;String&gt;**](String.md)|  | [optional]

### Return type

[**PlayerStateDTO**](PlayerStateDTO.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamsByMemberUsingGET1"></a>
# **readTeamsByMemberUsingGET1**
> List&lt;it.smartcommunitylab.model.ext.TeamDTO&gt; readTeamsByMemberUsingGET1(domain, gameId, playerId)

Get player teams

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    List<it.smartcommunitylab.model.ext.TeamDTO> result = apiInstance.readTeamsByMemberUsingGET1(domain, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#readTeamsByMemberUsingGET1");
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

[**List&lt;it.smartcommunitylab.model.ext.TeamDTO&gt;**](it.smartcommunitylab.model.ext.TeamDTO.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="searchByQueryUsingPOST"></a>
# **searchByQueryUsingPOST**
> PagePlayerStateDTO searchByQueryUsingPOST(domain, gameId, query, page, size)

Search player states

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
WrapperQuery query = new WrapperQuery(); // WrapperQuery | query
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    PagePlayerStateDTO result = apiInstance.searchByQueryUsingPOST(domain, gameId, query, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#searchByQueryUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **query** | [**WrapperQuery**](WrapperQuery.md)| query |
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**PagePlayerStateDTO**](PagePlayerStateDTO.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updatePlayerUsingPUT"></a>
# **updatePlayerUsingPUT**
> updatePlayerUsingPUT(domain, gameId, playerId)

Edit player state

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPlayerControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPlayerControllerApi apiInstance = new DomainPlayerControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    apiInstance.updatePlayerUsingPUT(domain, gameId, playerId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPlayerControllerApi#updatePlayerUsingPUT");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

