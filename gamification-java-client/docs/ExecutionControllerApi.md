# ExecutionControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**executeActionUsingPOST**](ExecutionControllerApi.md#executeActionUsingPOST) | **POST** /exec/game/{gameId}/action/{actionId} | Execute an action


<a name="executeActionUsingPOST"></a>
# **executeActionUsingPOST**
> executeActionUsingPOST(gameId, actionId, data)

Execute an action

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ExecutionControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ExecutionControllerApi apiInstance = new ExecutionControllerApi();
String gameId = "gameId_example"; // String | gameId
String actionId = "actionId_example"; // String | actionId
ExecutionDataDTO data = new ExecutionDataDTO(); // ExecutionDataDTO | data
try {
    apiInstance.executeActionUsingPOST(gameId, actionId, data);
} catch (ApiException e) {
    System.err.println("Exception when calling ExecutionControllerApi#executeActionUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **actionId** | **String**| actionId |
 **data** | [**ExecutionDataDTO**](ExecutionDataDTO.md)| data |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

