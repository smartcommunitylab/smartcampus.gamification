# GameControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addActionUsingPOST**](GameControllerApi.md#addActionUsingPOST) | **POST** /model/game/{gameId}/action/{actionId} | Add action
[**deleteActionUsingDELETE**](GameControllerApi.md#deleteActionUsingDELETE) | **DELETE** /model/game/{gameId}/action/{actionId} | Delete action
[**deleteGameUsingDELETE1**](GameControllerApi.md#deleteGameUsingDELETE1) | **DELETE** /model/game/{gameId} | Delete game
[**deleteLevelUsingDELETE**](GameControllerApi.md#deleteLevelUsingDELETE) | **DELETE** /model/game/{gameId}/level/{levelName} | Delete a level
[**editActionUsingPUT**](GameControllerApi.md#editActionUsingPUT) | **PUT** /model/game/{domain}/action/{actionId} | Edit action
[**readActionUsingGET**](GameControllerApi.md#readActionUsingGET) | **GET** /model/game/{gameId}/action/{actionId} | Get action
[**readAllActionUsingGET**](GameControllerApi.md#readAllActionUsingGET) | **GET** /model/game/{gameId}/action | Get actions
[**readGameStatisticsUsingGET**](GameControllerApi.md#readGameStatisticsUsingGET) | **GET** /data/game/{gameId}/statistics | Get game statistics
[**readGameUsingGET1**](GameControllerApi.md#readGameUsingGET1) | **GET** /model/game/{gameId} | Read game definition
[**readGamesUsingGET1**](GameControllerApi.md#readGamesUsingGET1) | **GET** /model/game | Get games
[**saveGameUsingPOST2**](GameControllerApi.md#saveGameUsingPOST2) | **POST** /model/game | Save a game
[**saveLevelUsingPOST**](GameControllerApi.md#saveLevelUsingPOST) | **POST** /model/game/{gameId}/level | Save a level
[**startGameUsingPUT**](GameControllerApi.md#startGameUsingPUT) | **PUT** /model/game/{gameId}/start | Start game
[**stopGameUsingPUT**](GameControllerApi.md#stopGameUsingPUT) | **PUT** /model/game/{gameId}/stop | Stop a game


<a name="addActionUsingPOST"></a>
# **addActionUsingPOST**
> addActionUsingPOST(gameId)

Add action

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.addActionUsingPOST(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#addActionUsingPOST");
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

<a name="deleteActionUsingDELETE"></a>
# **deleteActionUsingDELETE**
> deleteActionUsingDELETE(gameId, actionId)

Delete action

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
String actionId = "actionId_example"; // String | actionId
try {
    apiInstance.deleteActionUsingDELETE(gameId, actionId);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#deleteActionUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **actionId** | **String**| actionId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteGameUsingDELETE1"></a>
# **deleteGameUsingDELETE1**
> deleteGameUsingDELETE1(gameId)

Delete game

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.deleteGameUsingDELETE1(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#deleteGameUsingDELETE1");
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

<a name="deleteLevelUsingDELETE"></a>
# **deleteLevelUsingDELETE**
> Boolean deleteLevelUsingDELETE(gameId, levelName)

Delete a level

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
String levelName = "levelName_example"; // String | levelName
try {
    Boolean result = apiInstance.deleteLevelUsingDELETE(gameId, levelName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#deleteLevelUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **levelName** | **String**| levelName |

### Return type

**Boolean**

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editActionUsingPUT"></a>
# **editActionUsingPUT**
> editActionUsingPUT(gameId)

Edit action

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.editActionUsingPUT(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#editActionUsingPUT");
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

<a name="readActionUsingGET"></a>
# **readActionUsingGET**
> readActionUsingGET(gameId)

Get action

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.readActionUsingGET(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#readActionUsingGET");
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

<a name="readAllActionUsingGET"></a>
# **readAllActionUsingGET**
> List&lt;String&gt; readAllActionUsingGET(gameId)

Get actions

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<String> result = apiInstance.readAllActionUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#readAllActionUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

**List&lt;String&gt;**

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGameStatisticsUsingGET"></a>
# **readGameStatisticsUsingGET**
> List&lt;GameStatistics&gt; readGameStatisticsUsingGET(gameId, pointConceptName, periodName, timestamp, periodIndex, page, size)

Get game statistics

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
String pointConceptName = "pointConceptName_example"; // String | pointConceptName
String periodName = "periodName_example"; // String | periodName
Long timestamp = 789L; // Long | timestamp
String periodIndex = "periodIndex_example"; // String | periodIndex
Integer page = -1; // Integer | page
Integer size = -1; // Integer | size
try {
    List<GameStatistics> result = apiInstance.readGameStatisticsUsingGET(gameId, pointConceptName, periodName, timestamp, periodIndex, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#readGameStatisticsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **pointConceptName** | **String**| pointConceptName | [optional]
 **periodName** | **String**| periodName | [optional]
 **timestamp** | **Long**| timestamp | [optional]
 **periodIndex** | **String**| periodIndex | [optional]
 **page** | **Integer**| page | [optional] [default to -1]
 **size** | **Integer**| size | [optional] [default to -1]

### Return type

[**List&lt;GameStatistics&gt;**](GameStatistics.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGameUsingGET1"></a>
# **readGameUsingGET1**
> GameDTO readGameUsingGET1(gameId)

Read game definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    GameDTO result = apiInstance.readGameUsingGET1(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#readGameUsingGET1");
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

<a name="readGamesUsingGET1"></a>
# **readGamesUsingGET1**
> List&lt;GameDTO&gt; readGamesUsingGET1()

Get games

Get all the game definitions of a user

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
try {
    List<GameDTO> result = apiInstance.readGamesUsingGET1();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#readGamesUsingGET1");
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

<a name="saveGameUsingPOST2"></a>
# **saveGameUsingPOST2**
> GameDTO saveGameUsingPOST2(game)

Save a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
GameDTO game = new GameDTO(); // GameDTO | game
try {
    GameDTO result = apiInstance.saveGameUsingPOST2(game);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#saveGameUsingPOST2");
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

<a name="saveLevelUsingPOST"></a>
# **saveLevelUsingPOST**
> LevelDTO saveLevelUsingPOST(gameId, level)

Save a level

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
LevelDTO level = new LevelDTO(); // LevelDTO | level
try {
    LevelDTO result = apiInstance.saveLevelUsingPOST(gameId, level);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#saveLevelUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **level** | [**LevelDTO**](LevelDTO.md)| level |

### Return type

[**LevelDTO**](LevelDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="startGameUsingPUT"></a>
# **startGameUsingPUT**
> startGameUsingPUT(gameId)

Start game

The game is able to accept action executions

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.startGameUsingPUT(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#startGameUsingPUT");
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

<a name="stopGameUsingPUT"></a>
# **stopGameUsingPUT**
> stopGameUsingPUT(gameId)

Stop a game

The game will not accept action execution anymore

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.GameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

GameControllerApi apiInstance = new GameControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.stopGameUsingPUT(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling GameControllerApi#stopGameUsingPUT");
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

