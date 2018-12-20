# DomainChallengeModelControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**deleteChallengeModelsUsingDELETE**](DomainChallengeModelControllerApi.md#deleteChallengeModelsUsingDELETE) | **DELETE** /api/{domain}/model/game/{domain}/challenge/{modelId} | Delete challenge model
[**readChallengeModelsUsingGET**](DomainChallengeModelControllerApi.md#readChallengeModelsUsingGET) | **GET** /api/{domain}/model/game/{gameId}/challenge | Get challenge models
[**saveGameUsingPOST**](DomainChallengeModelControllerApi.md#saveGameUsingPOST) | **POST** /api/{domain}/model/game/{gameId}/challenge | Add challenge model


<a name="deleteChallengeModelsUsingDELETE"></a>
# **deleteChallengeModelsUsingDELETE**
> deleteChallengeModelsUsingDELETE(domain, gameId, modelId)

Delete challenge model

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainChallengeModelControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainChallengeModelControllerApi apiInstance = new DomainChallengeModelControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String modelId = "modelId_example"; // String | modelId
try {
    apiInstance.deleteChallengeModelsUsingDELETE(domain, gameId, modelId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainChallengeModelControllerApi#deleteChallengeModelsUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **modelId** | **String**| modelId |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readChallengeModelsUsingGET"></a>
# **readChallengeModelsUsingGET**
> List&lt;ChallengeModel&gt; readChallengeModelsUsingGET(domain, gameId)

Get challenge models

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainChallengeModelControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainChallengeModelControllerApi apiInstance = new DomainChallengeModelControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<ChallengeModel> result = apiInstance.readChallengeModelsUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainChallengeModelControllerApi#readChallengeModelsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;ChallengeModel&gt;**](ChallengeModel.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="saveGameUsingPOST"></a>
# **saveGameUsingPOST**
> ChallengeModel saveGameUsingPOST(challengeModel, domain, gameId)

Add challenge model

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainChallengeModelControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainChallengeModelControllerApi apiInstance = new DomainChallengeModelControllerApi();
ChallengeModel challengeModel = new ChallengeModel(); // ChallengeModel | challengeModel
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    ChallengeModel result = apiInstance.saveGameUsingPOST(challengeModel, domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainChallengeModelControllerApi#saveGameUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **challengeModel** | [**ChallengeModel**](ChallengeModel.md)| challengeModel |
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**ChallengeModel**](ChallengeModel.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

