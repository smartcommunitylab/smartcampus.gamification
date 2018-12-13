# TeamControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addTeamMemberUsingPUT**](TeamControllerApi.md#addTeamMemberUsingPUT) | **PUT** /data/game/{gameId}/team/{teamId}/members/{playerId} | Add team member
[**createTeamUsingPOST1**](TeamControllerApi.md#createTeamUsingPOST1) | **POST** /data/game/{gameId}/team/{teamId} | Create team
[**deleteTeamUsingDELETE1**](TeamControllerApi.md#deleteTeamUsingDELETE1) | **DELETE** /data/game/{gameId}/team/{teamId} | Delte team
[**readTeamMembersUsingGET**](TeamControllerApi.md#readTeamMembersUsingGET) | **GET** /data/game/{gameId}/team/{teamId}/members | Get team members
[**removeTeamMemberUsingDELETE**](TeamControllerApi.md#removeTeamMemberUsingDELETE) | **DELETE** /data/game/{gameId}/team/{teamId}/members/{playerId} | Delete team member
[**updateTeamMembersUsingPUT**](TeamControllerApi.md#updateTeamMembersUsingPUT) | **PUT** /data/game/{gameId}/team/{teamId}/members | Edit team


<a name="addTeamMemberUsingPUT"></a>
# **addTeamMemberUsingPUT**
> addTeamMemberUsingPUT(gameId, teamId, playerId, members)

Add team member

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.TeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

TeamControllerApi apiInstance = new TeamControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
String playerId = "playerId_example"; // String | playerId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.addTeamMemberUsingPUT(gameId, teamId, playerId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling TeamControllerApi#addTeamMemberUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **playerId** | **String**| playerId |
 **members** | **List&lt;String&gt;**| members |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createTeamUsingPOST1"></a>
# **createTeamUsingPOST1**
> createTeamUsingPOST1(gameId, team)

Create team

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.TeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

TeamControllerApi apiInstance = new TeamControllerApi();
String gameId = "gameId_example"; // String | gameId
TeamDTO team = new TeamDTO(); // TeamDTO | team
try {
    apiInstance.createTeamUsingPOST1(gameId, team);
} catch (ApiException e) {
    System.err.println("Exception when calling TeamControllerApi#createTeamUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **team** | [**TeamDTO**](TeamDTO.md)| team |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteTeamUsingDELETE1"></a>
# **deleteTeamUsingDELETE1**
> deleteTeamUsingDELETE1(gameId, teamId)

Delte team

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.TeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

TeamControllerApi apiInstance = new TeamControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
try {
    apiInstance.deleteTeamUsingDELETE1(gameId, teamId);
} catch (ApiException e) {
    System.err.println("Exception when calling TeamControllerApi#deleteTeamUsingDELETE1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readTeamMembersUsingGET"></a>
# **readTeamMembersUsingGET**
> Collectionstring readTeamMembersUsingGET(gameId, teamId)

Get team members

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.TeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

TeamControllerApi apiInstance = new TeamControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
try {
    Collectionstring result = apiInstance.readTeamMembersUsingGET(gameId, teamId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling TeamControllerApi#readTeamMembersUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |

### Return type

[**Collectionstring**](Collectionstring.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="removeTeamMemberUsingDELETE"></a>
# **removeTeamMemberUsingDELETE**
> removeTeamMemberUsingDELETE(gameId, teamId, playerId, members)

Delete team member

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.TeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

TeamControllerApi apiInstance = new TeamControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
String playerId = "playerId_example"; // String | playerId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.removeTeamMemberUsingDELETE(gameId, teamId, playerId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling TeamControllerApi#removeTeamMemberUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **playerId** | **String**| playerId |
 **members** | **List&lt;String&gt;**| members |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateTeamMembersUsingPUT"></a>
# **updateTeamMembersUsingPUT**
> updateTeamMembersUsingPUT(gameId, teamId, members)

Edit team

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.TeamControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

TeamControllerApi apiInstance = new TeamControllerApi();
String gameId = "gameId_example"; // String | gameId
String teamId = "teamId_example"; // String | teamId
List<String> members = Arrays.asList(new List<String>()); // List<String> | members
try {
    apiInstance.updateTeamMembersUsingPUT(gameId, teamId, members);
} catch (ApiException e) {
    System.err.println("Exception when calling TeamControllerApi#updateTeamMembersUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **teamId** | **String**| teamId |
 **members** | **List&lt;String&gt;**| members |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

