# DomainTeamControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addTeamMemberUsingPUT**](DomainTeamControllerApi.md#addTeamMemberUsingPUT) | **PUT** /api/{domain}/data/game/{gameId}/team/{teamId}/members/{playerId} | Add team member
[**createTeamUsingPOST1**](DomainTeamControllerApi.md#createTeamUsingPOST1) | **POST** /api/{domain}/data/game/{gameId}/team/{teamId} | Create team
[**deleteTeamUsingDELETE1**](DomainTeamControllerApi.md#deleteTeamUsingDELETE1) | **DELETE** /api/{domain}/data/game/{gameId}/team/{teamId} | Delte team
[**readTeamMembersUsingGET**](DomainTeamControllerApi.md#readTeamMembersUsingGET) | **GET** /api/{domain}/data/game/{gameId}/team/{teamId}/members | Get team members
[**removeTeamMemberUsingDELETE**](DomainTeamControllerApi.md#removeTeamMemberUsingDELETE) | **DELETE** /api/{domain}/data/game/{gameId}/team/{teamId}/members/{playerId} | Delete team member
[**updateTeamMembersUsingPUT**](DomainTeamControllerApi.md#updateTeamMembersUsingPUT) | **PUT** /api/{domain}/data/game/{gameId}/team/{teamId}/members | Edit team


<a name="addTeamMemberUsingPUT"></a>
# **addTeamMemberUsingPUT**
> addTeamMemberUsingPUT(domain, gameId, teamId, playerId, members)

Add team member

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainTeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainTeamControllerApi apiInstance = new DomainTeamControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
String playerId = "playerId_example"; // String | playerId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.addTeamMemberUsingPUT(domain, gameId, teamId, playerId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainTeamControllerApi#addTeamMemberUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **playerId** | **String**| playerId |
 **members** | **List&lt;String&gt;**| members |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createTeamUsingPOST1"></a>
# **createTeamUsingPOST1**
> createTeamUsingPOST1(domain, gameId, team)

Create team

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainTeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainTeamControllerApi apiInstance = new DomainTeamControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
TeamDTO team = new TeamDTO(); // TeamDTO | team
try {
    apiInstance.createTeamUsingPOST1(domain, gameId, team);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainTeamControllerApi#createTeamUsingPOST1");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteTeamUsingDELETE1"></a>
# **deleteTeamUsingDELETE1**
> deleteTeamUsingDELETE1(domain, gameId, teamId)

Delte team

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainTeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainTeamControllerApi apiInstance = new DomainTeamControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
try {
    apiInstance.deleteTeamUsingDELETE1(domain, gameId, teamId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainTeamControllerApi#deleteTeamUsingDELETE1");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamMembersUsingGET"></a>
# **readTeamMembersUsingGET**
> Collectionstring readTeamMembersUsingGET(domain, gameId, teamId)

Get team members

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainTeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainTeamControllerApi apiInstance = new DomainTeamControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
try {
    Collectionstring result = apiInstance.readTeamMembersUsingGET(domain, gameId, teamId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainTeamControllerApi#readTeamMembersUsingGET");
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

[**Collectionstring**](Collectionstring.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="removeTeamMemberUsingDELETE"></a>
# **removeTeamMemberUsingDELETE**
> removeTeamMemberUsingDELETE(domain, gameId, teamId, playerId, members)

Delete team member

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainTeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainTeamControllerApi apiInstance = new DomainTeamControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
String playerId = "playerId_example"; // String | playerId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.removeTeamMemberUsingDELETE(domain, gameId, teamId, playerId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainTeamControllerApi#removeTeamMemberUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **playerId** | **String**| playerId |
 **members** | **List&lt;String&gt;**| members |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateTeamMembersUsingPUT"></a>
# **updateTeamMembersUsingPUT**
> updateTeamMembersUsingPUT(domain, gameId, teamId, members)

Edit team

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainTeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainTeamControllerApi apiInstance = new DomainTeamControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.updateTeamMembersUsingPUT(domain, gameId, teamId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainTeamControllerApi#updateTeamMembersUsingPUT");
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

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

