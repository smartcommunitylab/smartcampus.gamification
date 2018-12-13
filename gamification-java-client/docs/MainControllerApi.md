# MainControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**executeActionUsingPOST1**](MainControllerApi.md#executeActionUsingPOST1) | **POST** /gengine/execute | Execute an action
[**readNotificationUsingGET**](MainControllerApi.md#readNotificationUsingGET) | **GET** /gengine/notification/{gameId}/{playerId} | Get player notifications
[**readNotificationUsingGET1**](MainControllerApi.md#readNotificationUsingGET1) | **GET** /gengine/notification/{gameId} | Get notifications
[**readPlayerStateUsingGET**](MainControllerApi.md#readPlayerStateUsingGET) | **GET** /gengine/state/{gameId}/{playerId} | Get player state
[**readPlayerStateUsingGET1**](MainControllerApi.md#readPlayerStateUsingGET1) | **GET** /gengine/state/{gameId} | Get player states


<a name="executeActionUsingPOST1"></a>
# **executeActionUsingPOST1**
> executeActionUsingPOST1(data)

Execute an action

Execute an action in a game

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.MainControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

MainControllerApi apiInstance = new MainControllerApi();
ExecutionDataDTO data = new ExecutionDataDTO(); // ExecutionDataDTO | data
try {
    apiInstance.executeActionUsingPOST1(data);
} catch (ApiException e) {
    System.err.println("Exception when calling MainControllerApi#executeActionUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **data** | [**ExecutionDataDTO**](ExecutionDataDTO.md)| data |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readNotificationUsingGET"></a>
# **readNotificationUsingGET**
> List&lt;Notification&gt; readNotificationUsingGET(gameId, playerId, timestamp)

Get player notifications

Get the player notifications of a game

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.MainControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

MainControllerApi apiInstance = new MainControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Long timestamp = 789L; // Long | timestamp
try {
    List<Notification> result = apiInstance.readNotificationUsingGET(gameId, playerId, timestamp);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MainControllerApi#readNotificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **timestamp** | **Long**| timestamp | [optional]

### Return type

[**List&lt;Notification&gt;**](Notification.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readNotificationUsingGET1"></a>
# **readNotificationUsingGET1**
> List&lt;Notification&gt; readNotificationUsingGET1(gameId, timestamp)

Get notifications

Get the notifications of a game

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.MainControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

MainControllerApi apiInstance = new MainControllerApi();
String gameId = "gameId_example"; // String | gameId
Long timestamp = 789L; // Long | timestamp
try {
    List<Notification> result = apiInstance.readNotificationUsingGET1(gameId, timestamp);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MainControllerApi#readNotificationUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **timestamp** | **Long**| timestamp | [optional]

### Return type

[**List&lt;Notification&gt;**](Notification.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerStateUsingGET"></a>
# **readPlayerStateUsingGET**
> PlayerStateDTO readPlayerStateUsingGET(gameId, playerId)

Get player state

Get the state of a player in a game

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.MainControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

MainControllerApi apiInstance = new MainControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readPlayerStateUsingGET(gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MainControllerApi#readPlayerStateUsingGET");
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

<a name="readPlayerStateUsingGET1"></a>
# **readPlayerStateUsingGET1**
> PagePlayerStateDTO readPlayerStateUsingGET1(gameId, playerFilter, page, size)

Get player states

Get the state of players in a game filter by optional player name

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.MainControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

MainControllerApi apiInstance = new MainControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerFilter = "playerFilter_example"; // String | playerFilter
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    PagePlayerStateDTO result = apiInstance.readPlayerStateUsingGET1(gameId, playerFilter, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling MainControllerApi#readPlayerStateUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerFilter** | **String**| playerFilter | [optional]
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**PagePlayerStateDTO**](PagePlayerStateDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

