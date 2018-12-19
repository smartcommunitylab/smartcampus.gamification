# ArchiveConceptControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**readArchivesForGameUsingGET**](ArchiveConceptControllerApi.md#readArchivesForGameUsingGET) | **GET** /data/game/{gameId}/archive | Read archive concepts for a game with optional filter parameters


<a name="readArchivesForGameUsingGET"></a>
# **readArchivesForGameUsingGET**
> List&lt;ArchivedConcept&gt; readArchivesForGameUsingGET(gameId, playerId, state, from, to)

Read archive concepts for a game with optional filter parameters

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.ArchiveConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ArchiveConceptControllerApi apiInstance = new ArchiveConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
String playerId = "playerId_example"; // String | playerId
String state = "state_example"; // String | state
Long from = 789L; // Long | from
Long to = 789L; // Long | to
try {
    List<ArchivedConcept> result = apiInstance.readArchivesForGameUsingGET(gameId, playerId, state, from, to);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ArchiveConceptControllerApi#readArchivesForGameUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **playerId** | **String**| playerId | [optional]
 **state** | **String**| state | [optional]
 **from** | **Long**| from | [optional]
 **to** | **Long**| to | [optional]

### Return type

[**List&lt;ArchivedConcept&gt;**](ArchivedConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

