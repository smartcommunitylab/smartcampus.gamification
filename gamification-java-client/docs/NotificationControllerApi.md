# NotificationControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**readNotificationUsingGET2**](NotificationControllerApi.md#readNotificationUsingGET2) | **GET** /notification/game/{gameId} | Get game notifications
[**readPlayerNotificationGroupedUsingGET**](NotificationControllerApi.md#readPlayerNotificationGroupedUsingGET) | **GET** /notification/game/{gameId}/player/{playerId}/grouped | Get player notifications
[**readPlayerNotificationUsingGET**](NotificationControllerApi.md#readPlayerNotificationUsingGET) | **GET** /notification/game/{gameId}/player/{playerId} | Get player notifications
[**readTeamNotificationUsingGET**](NotificationControllerApi.md#readTeamNotificationUsingGET) | **GET** /notification/game/{gameId}/team/{teamId} | Get team notifications


<a name="readNotificationUsingGET2"></a>
# **readNotificationUsingGET2**
> List&lt;Notification&gt; readNotificationUsingGET2(gameId, fromTs, toTs, includeTypes, excludeTypes, page, size)

Get game notifications

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.NotificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

NotificationControllerApi apiInstance = new NotificationControllerApi();
String gameId = "gameId_example"; // String | gameId
Long fromTs = -1L; // Long | fromTs
Long toTs = -1L; // Long | toTs
List<String> includeTypes = Arrays.asList("includeTypes_example"); // List<String> | includeTypes
List<String> excludeTypes = Arrays.asList("excludeTypes_example"); // List<String> | excludeTypes
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    List<Notification> result = apiInstance.readNotificationUsingGET2(gameId, fromTs, toTs, includeTypes, excludeTypes, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NotificationControllerApi#readNotificationUsingGET2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **fromTs** | **Long**| fromTs | [optional] [default to -1]
 **toTs** | **Long**| toTs | [optional] [default to -1]
 **includeTypes** | [**List&lt;String&gt;**](String.md)| includeTypes | [optional]
 **excludeTypes** | [**List&lt;String&gt;**](String.md)| excludeTypes | [optional]
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**List&lt;Notification&gt;**](Notification.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerNotificationGroupedUsingGET"></a>
# **readPlayerNotificationGroupedUsingGET**
> Map&lt;String, CollectionNotification&gt; readPlayerNotificationGroupedUsingGET(gameId, playerId, fromTs, toTs, includeTypes, excludeTypes, page, size)

Get player notifications

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.NotificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

NotificationControllerApi apiInstance = new NotificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Long fromTs = -1L; // Long | fromTs
Long toTs = -1L; // Long | toTs
List<String> includeTypes = Arrays.asList("includeTypes_example"); // List<String> | includeTypes
List<String> excludeTypes = Arrays.asList("excludeTypes_example"); // List<String> | excludeTypes
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    Map<String, CollectionNotification> result = apiInstance.readPlayerNotificationGroupedUsingGET(gameId, playerId, fromTs, toTs, includeTypes, excludeTypes, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NotificationControllerApi#readPlayerNotificationGroupedUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **fromTs** | **Long**| fromTs | [optional] [default to -1]
 **toTs** | **Long**| toTs | [optional] [default to -1]
 **includeTypes** | [**List&lt;String&gt;**](String.md)| includeTypes | [optional]
 **excludeTypes** | [**List&lt;String&gt;**](String.md)| excludeTypes | [optional]
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**Map&lt;String, CollectionNotification&gt;**](CollectionNotification.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerNotificationUsingGET"></a>
# **readPlayerNotificationUsingGET**
> List&lt;Notification&gt; readPlayerNotificationUsingGET(gameId, playerId, fromTs, toTs, includeTypes, excludeTypes, page, size)

Get player notifications

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.NotificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

NotificationControllerApi apiInstance = new NotificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Long fromTs = -1L; // Long | fromTs
Long toTs = -1L; // Long | toTs
List<String> includeTypes = Arrays.asList("includeTypes_example"); // List<String> | includeTypes
List<String> excludeTypes = Arrays.asList("excludeTypes_example"); // List<String> | excludeTypes
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    List<Notification> result = apiInstance.readPlayerNotificationUsingGET(gameId, playerId, fromTs, toTs, includeTypes, excludeTypes, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NotificationControllerApi#readPlayerNotificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId |
 **fromTs** | **Long**| fromTs | [optional] [default to -1]
 **toTs** | **Long**| toTs | [optional] [default to -1]
 **includeTypes** | [**List&lt;String&gt;**](String.md)| includeTypes | [optional]
 **excludeTypes** | [**List&lt;String&gt;**](String.md)| excludeTypes | [optional]
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**List&lt;Notification&gt;**](Notification.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamNotificationUsingGET"></a>
# **readTeamNotificationUsingGET**
> List&lt;Notification&gt; readTeamNotificationUsingGET(gameId, teamId, fromTs, toTs, includeTypes, excludeTypes, page, size)

Get team notifications

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.NotificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

NotificationControllerApi apiInstance = new NotificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
Long fromTs = -1L; // Long | fromTs
Long toTs = -1L; // Long | toTs
List<String> includeTypes = Arrays.asList("includeTypes_example"); // List<String> | includeTypes
List<String> excludeTypes = Arrays.asList("excludeTypes_example"); // List<String> | excludeTypes
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    List<Notification> result = apiInstance.readTeamNotificationUsingGET(gameId, teamId, fromTs, toTs, includeTypes, excludeTypes, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling NotificationControllerApi#readTeamNotificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **fromTs** | **Long**| fromTs | [optional] [default to -1]
 **toTs** | **Long**| toTs | [optional] [default to -1]
 **includeTypes** | [**List&lt;String&gt;**](String.md)| includeTypes | [optional]
 **excludeTypes** | [**List&lt;String&gt;**](String.md)| excludeTypes | [optional]
 **page** | **String**| Results page you want to retrieve  | [optional]
 **size** | **String**| Number of records per page. | [optional]

### Return type

[**List&lt;Notification&gt;**](Notification.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

