# DomainMainControllerApi

All URIs are relative to *https://localhost:6060/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**executeActionUsingPOST1**](DomainMainControllerApi.md#executeActionUsingPOST1) | **POST** /api/{domain}/gengine/execute | Execute an action
[**readNotificationUsingGET**](DomainMainControllerApi.md#readNotificationUsingGET) | **GET** /api/{domain}/gengine/notification/{gameId}/{playerId} | Get player notifications
[**readNotificationUsingGET1**](DomainMainControllerApi.md#readNotificationUsingGET1) | **GET** /api/{domain}/gengine/notification/{gameId} | Get notifications
[**readPlayerStateUsingGET**](DomainMainControllerApi.md#readPlayerStateUsingGET) | **GET** /api/{domain}/gengine/state/{gameId}/{playerId} | Get player state
[**readPlayerStateUsingGET1**](DomainMainControllerApi.md#readPlayerStateUsingGET1) | **GET** /api/{domain}/gengine/state/{gameId} | Get player states


<a name="executeActionUsingPOST1"></a>
# **executeActionUsingPOST1**
> executeActionUsingPOST1(domain, data)

Execute an action

Execute an action in a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainMainControllerApi;


DomainMainControllerApi apiInstance = new DomainMainControllerApi();
String domain = "domain_example"; // String | domain
ExecutionDataDTO data = new ExecutionDataDTO(); // ExecutionDataDTO | data
try {
    apiInstance.executeActionUsingPOST1(domain, data);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainMainControllerApi#executeActionUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **data** | [**ExecutionDataDTO**](ExecutionDataDTO.md)| data |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readNotificationUsingGET"></a>
# **readNotificationUsingGET**
> List&lt;Notification&gt; readNotificationUsingGET(domain, gameId, playerId, timestamp)

Get player notifications

Get the player notifications of a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainMainControllerApi;


DomainMainControllerApi apiInstance = new DomainMainControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Long timestamp = 789L; // Long | timestamp
try {
    List<Notification> result = apiInstance.readNotificationUsingGET(domain, gameId, playerId, timestamp);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainMainControllerApi#readNotificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **timestamp** | **Long**| timestamp | [optional]

### Return type

[**List&lt;Notification&gt;**](Notification.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readNotificationUsingGET1"></a>
# **readNotificationUsingGET1**
> List&lt;Notification&gt; readNotificationUsingGET1(domain, gameId, timestamp)

Get notifications

Get the notifications of a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainMainControllerApi;


DomainMainControllerApi apiInstance = new DomainMainControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
Long timestamp = 789L; // Long | timestamp
try {
    List<Notification> result = apiInstance.readNotificationUsingGET1(domain, gameId, timestamp);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainMainControllerApi#readNotificationUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **timestamp** | **Long**| timestamp | [optional]

### Return type

[**List&lt;Notification&gt;**](Notification.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerStateUsingGET"></a>
# **readPlayerStateUsingGET**
> PlayerStateDTO readPlayerStateUsingGET(domain, gameId, playerId)

Get player state

Get the state of a player in a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainMainControllerApi;


DomainMainControllerApi apiInstance = new DomainMainControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
try {
    PlayerStateDTO result = apiInstance.readPlayerStateUsingGET(domain, gameId, playerId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainMainControllerApi#readPlayerStateUsingGET");
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

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerStateUsingGET1"></a>
# **readPlayerStateUsingGET1**
> PagePlayerStateDTO readPlayerStateUsingGET1(domain, gameId, playerFilter, page, size)

Get player states

Get the state of players in a game filter by optional player name

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainMainControllerApi;


DomainMainControllerApi apiInstance = new DomainMainControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerFilter = "playerFilter_example"; // String | playerFilter
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    PagePlayerStateDTO result = apiInstance.readPlayerStateUsingGET1(domain, gameId, playerFilter, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainMainControllerApi#readPlayerStateUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **playerFilter** | **String**| playerFilter | [optional]
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**PagePlayerStateDTO**](PagePlayerStateDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

