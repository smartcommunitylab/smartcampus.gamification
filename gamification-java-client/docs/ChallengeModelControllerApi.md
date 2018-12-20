# ChallengeModelControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteChallengeModelsUsingDELETE**](ChallengeModelControllerApi.md#deleteChallengeModelsUsingDELETE) | **DELETE** /model/game/{gameId}/challenge/{modelId} | Delete challenge model
[**readChallengeModelsUsingGET**](ChallengeModelControllerApi.md#readChallengeModelsUsingGET) | **GET** /model/game/{gameId}/challenge | Get challenge models
[**saveGameUsingPOST**](ChallengeModelControllerApi.md#saveGameUsingPOST) | **POST** /model/game/{gameId}/challenge | Add challenge model


<a name="deleteChallengeModelsUsingDELETE"></a>
# **deleteChallengeModelsUsingDELETE**
> deleteChallengeModelsUsingDELETE(gameId, modelId)

Delete challenge model

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.ChallengeModelControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ChallengeModelControllerApi apiInstance = new ChallengeModelControllerApi();
String gameId = "gameId_example"; // String | gameId
String modelId = "modelId_example"; // String | modelId
try {
    apiInstance.deleteChallengeModelsUsingDELETE(gameId, modelId);
} catch (ApiException e) {
    System.err.println("Exception when calling ChallengeModelControllerApi#deleteChallengeModelsUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **modelId** | **String**| modelId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readChallengeModelsUsingGET"></a>
# **readChallengeModelsUsingGET**
> List&lt;ChallengeModel&gt; readChallengeModelsUsingGET(gameId)

Get challenge models

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.ChallengeModelControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ChallengeModelControllerApi apiInstance = new ChallengeModelControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<ChallengeModel> result = apiInstance.readChallengeModelsUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ChallengeModelControllerApi#readChallengeModelsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;ChallengeModel&gt;**](ChallengeModel.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="saveGameUsingPOST"></a>
# **saveGameUsingPOST**
> ChallengeModel saveGameUsingPOST(challengeModel, gameId)

Add challenge model

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.ChallengeModelControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ChallengeModelControllerApi apiInstance = new ChallengeModelControllerApi();
ChallengeModel challengeModel = new ChallengeModel(); // ChallengeModel | challengeModel
String gameId = "gameId_example"; // String | gameId
try {
    ChallengeModel result = apiInstance.saveGameUsingPOST(challengeModel, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ChallengeModelControllerApi#saveGameUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challengeModel** | [**ChallengeModel**](ChallengeModel.md)| challengeModel |
 **gameId** | **String**| gameId |

### Return type

[**ChallengeModel**](ChallengeModel.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

