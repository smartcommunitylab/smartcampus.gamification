# DomainGameControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addActionUsingPOST**](DomainGameControllerApi.md#addActionUsingPOST) | **POST** /api/{domain}/model/game/{gameId}/action/{actionId} | Add action
[**deleteActionUsingDELETE**](DomainGameControllerApi.md#deleteActionUsingDELETE) | **DELETE** /api/{domain}/model/game/{gameId}/action/{actionId} | Delete action
[**deleteGameUsingDELETE1**](DomainGameControllerApi.md#deleteGameUsingDELETE1) | **DELETE** /api/{domain}/model/game/{gameId} | Delete game
[**deleteLevelUsingDELETE**](DomainGameControllerApi.md#deleteLevelUsingDELETE) | **DELETE** /api/{domain}/model/game/{gameId}/level/{levelName} | Delete a level
[**editActionUsingPUT**](DomainGameControllerApi.md#editActionUsingPUT) | **PUT** /api/{domain}/model/game/{domain}/action/{actionId} | Edit action
[**readActionUsingGET**](DomainGameControllerApi.md#readActionUsingGET) | **GET** /api/{domain}/model/game/{gameId}/action/{actionId} | Get action
[**readAllActionUsingGET**](DomainGameControllerApi.md#readAllActionUsingGET) | **GET** /api/{domain}/model/game/{gameId}/action | Get actions
[**readGameUsingGET1**](DomainGameControllerApi.md#readGameUsingGET1) | **GET** /api/{domain}/model/game/{gameId} | Read game definition
[**readGamesUsingGET1**](DomainGameControllerApi.md#readGamesUsingGET1) | **GET** /api/{domain}/model/game | Get games
[**saveGameUsingPOST2**](DomainGameControllerApi.md#saveGameUsingPOST2) | **POST** /api/{domain}/model/game | Save a game
[**saveLevelUsingPOST**](DomainGameControllerApi.md#saveLevelUsingPOST) | **POST** /api/{domain}/model/game/{gameId}/level | Save a level
[**startGameUsingPUT**](DomainGameControllerApi.md#startGameUsingPUT) | **PUT** /api/{domain}/model/game/{gameId}/start | Start game
[**stopGameUsingPUT**](DomainGameControllerApi.md#stopGameUsingPUT) | **PUT** /api/{domain}/model/game/{gameId}/stop | Stop a game


<a name="addActionUsingPOST"></a>
# **addActionUsingPOST**
> addActionUsingPOST(domain, gameId)

Add action

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.addActionUsingPOST(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#addActionUsingPOST");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteActionUsingDELETE"></a>
# **deleteActionUsingDELETE**
> deleteActionUsingDELETE(domain, gameId, actionId)

Delete action

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String actionId = "actionId_example"; // String | actionId
try {
    apiInstance.deleteActionUsingDELETE(domain, gameId, actionId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#deleteActionUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **actionId** | **String**| actionId |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteGameUsingDELETE1"></a>
# **deleteGameUsingDELETE1**
> deleteGameUsingDELETE1(domain, gameId)

Delete game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.deleteGameUsingDELETE1(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#deleteGameUsingDELETE1");
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

[oauth2](../README.md#oauth2)

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
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String gameId = "gameId_example"; // String | gameId
String levelName = "levelName_example"; // String | levelName
try {
    Boolean result = apiInstance.deleteLevelUsingDELETE(gameId, levelName);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#deleteLevelUsingDELETE");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editActionUsingPUT"></a>
# **editActionUsingPUT**
> editActionUsingPUT(domain, gameId)

Edit action

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.editActionUsingPUT(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#editActionUsingPUT");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readActionUsingGET"></a>
# **readActionUsingGET**
> readActionUsingGET(domain, gameId)

Get action

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.readActionUsingGET(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#readActionUsingGET");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readAllActionUsingGET"></a>
# **readAllActionUsingGET**
> List&lt;String&gt; readAllActionUsingGET(domain, gameId)

Get actions

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<String> result = apiInstance.readAllActionUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#readAllActionUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

**List&lt;String&gt;**

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGameUsingGET1"></a>
# **readGameUsingGET1**
> GameDTO readGameUsingGET1(domain, gameId)

Read game definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    GameDTO result = apiInstance.readGameUsingGET1(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#readGameUsingGET1");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGamesUsingGET1"></a>
# **readGamesUsingGET1**
> List&lt;GameDTO&gt; readGamesUsingGET1(domain)

Get games

Get all the game definitions of a user

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
try {
    List<GameDTO> result = apiInstance.readGamesUsingGET1(domain);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#readGamesUsingGET1");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="saveGameUsingPOST2"></a>
# **saveGameUsingPOST2**
> GameDTO saveGameUsingPOST2(domain, game)

Save a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
GameDTO game = new GameDTO(); // GameDTO | game
try {
    GameDTO result = apiInstance.saveGameUsingPOST2(domain, game);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#saveGameUsingPOST2");
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

[oauth2](../README.md#oauth2)

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
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String gameId = "gameId_example"; // String | gameId
LevelDTO level = new LevelDTO(); // LevelDTO | level
try {
    LevelDTO result = apiInstance.saveLevelUsingPOST(gameId, level);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#saveLevelUsingPOST");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="startGameUsingPUT"></a>
# **startGameUsingPUT**
> startGameUsingPUT(domain, gameId)

Start game

The game is able to accept action executions

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.startGameUsingPUT(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#startGameUsingPUT");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="stopGameUsingPUT"></a>
# **stopGameUsingPUT**
> stopGameUsingPUT(domain, gameId)

Stop a game

The game will not accept action execution anymore

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainGameControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainGameControllerApi apiInstance = new DomainGameControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.stopGameUsingPUT(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainGameControllerApi#stopGameUsingPUT");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

