# DomainExecutionControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**executeActionUsingPOST**](DomainExecutionControllerApi.md#executeActionUsingPOST) | **POST** /api/{domain}/exec/game/{gameId}/action/{actionId} | Execute an action


<a name="executeActionUsingPOST"></a>
# **executeActionUsingPOST**
> executeActionUsingPOST(domain, gameId, actionId, data)

Execute an action

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainExecutionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainExecutionControllerApi apiInstance = new DomainExecutionControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String actionId = "actionId_example"; // String | actionId
ExecutionDataDTO data = new ExecutionDataDTO(); // ExecutionDataDTO | data
try {
    apiInstance.executeActionUsingPOST(domain, gameId, actionId, data);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainExecutionControllerApi#executeActionUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **actionId** | **String**| actionId |
 **data** | [**ExecutionDataDTO**](ExecutionDataDTO.md)| data |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

