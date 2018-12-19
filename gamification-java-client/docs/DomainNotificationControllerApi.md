# DomainNotificationControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**readNotificationUsingGET2**](DomainNotificationControllerApi.md#readNotificationUsingGET2) | **GET** /api/{domain}/notification/game/{gameId} | Get game notifications
[**readPlayerNotificationUsingGET**](DomainNotificationControllerApi.md#readPlayerNotificationUsingGET) | **GET** /api/{domain}/notification/game/{gameId}/player/{playerId} | Get player notifications
[**readTeamNotificationUsingGET**](DomainNotificationControllerApi.md#readTeamNotificationUsingGET) | **GET** /api/{domain}/notification/game/{gameId}/team/{teamId} | Get team notifications


<a name="readNotificationUsingGET2"></a>
# **readNotificationUsingGET2**
> List&lt;Notification&gt; readNotificationUsingGET2(domain, gameId, fromTs, toTs, includeTypes, excludeTypes, page, size)

Get game notifications

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainNotificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainNotificationControllerApi apiInstance = new DomainNotificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
Long fromTs = -1L; // Long | fromTs
Long toTs = -1L; // Long | toTs
List<String> includeTypes = Arrays.asList("includeTypes_example"); // List<String> | includeTypes
List<String> excludeTypes = Arrays.asList("excludeTypes_example"); // List<String> | excludeTypes
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    List<Notification> result = apiInstance.readNotificationUsingGET2(domain, gameId, fromTs, toTs, includeTypes, excludeTypes, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainNotificationControllerApi#readNotificationUsingGET2");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPlayerNotificationUsingGET"></a>
# **readPlayerNotificationUsingGET**
> List&lt;Notification&gt; readPlayerNotificationUsingGET(domain, gameId, playerId, fromTs, toTs, includeTypes, excludeTypes, page, size)

Get player notifications

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainNotificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainNotificationControllerApi apiInstance = new DomainNotificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
Long fromTs = -1L; // Long | fromTs
Long toTs = -1L; // Long | toTs
List<String> includeTypes = Arrays.asList("includeTypes_example"); // List<String> | includeTypes
List<String> excludeTypes = Arrays.asList("excludeTypes_example"); // List<String> | excludeTypes
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    List<Notification> result = apiInstance.readPlayerNotificationUsingGET(domain, gameId, playerId, fromTs, toTs, includeTypes, excludeTypes, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainNotificationControllerApi#readPlayerNotificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamNotificationUsingGET"></a>
# **readTeamNotificationUsingGET**
> List&lt;Notification&gt; readTeamNotificationUsingGET(domain, gameId, teamId, fromTs, toTs, includeTypes, excludeTypes, page, size)

Get team notifications

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainNotificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainNotificationControllerApi apiInstance = new DomainNotificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
Long fromTs = -1L; // Long | fromTs
Long toTs = -1L; // Long | toTs
List<String> includeTypes = Arrays.asList("includeTypes_example"); // List<String> | includeTypes
List<String> excludeTypes = Arrays.asList("excludeTypes_example"); // List<String> | excludeTypes
String page = "page_example"; // String | Results page you want to retrieve 
String size = "size_example"; // String | Number of records per page.
try {
    List<Notification> result = apiInstance.readTeamNotificationUsingGET(domain, gameId, teamId, fromTs, toTs, includeTypes, excludeTypes, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainNotificationControllerApi#readTeamNotificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

