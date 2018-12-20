# DomainPointConceptControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addPointUsingPOST1**](DomainPointConceptControllerApi.md#addPointUsingPOST1) | **POST** /api/{domain}/model/game/{gameId}/point | Add point
[**deletePointUsingDELETE**](DomainPointConceptControllerApi.md#deletePointUsingDELETE) | **DELETE** /api/{domain}/model/game/{gameId}/point/{pointId} | Delete point
[**readPointUsingGET**](DomainPointConceptControllerApi.md#readPointUsingGET) | **GET** /api/{domain}/model/game/{gameId}/point/{pointId} | Get point
[**readPointsUsingGET1**](DomainPointConceptControllerApi.md#readPointsUsingGET1) | **GET** /api/{domain}/model/game/{gameId}/point | Get points
[**updatePointUsingPUT**](DomainPointConceptControllerApi.md#updatePointUsingPUT) | **PUT** /api/{domain}/model/game/{gameId}/point/{pointId} | Edit point


<a name="addPointUsingPOST1"></a>
# **addPointUsingPOST1**
> PointConcept addPointUsingPOST1(domain, gameId, point)

Add point

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPointConceptControllerApi apiInstance = new DomainPointConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
PointConcept point = new PointConcept(); // PointConcept | point
try {
    PointConcept result = apiInstance.addPointUsingPOST1(domain, gameId, point);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPointConceptControllerApi#addPointUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **point** | [**PointConcept**](PointConcept.md)| point |

### Return type

[**PointConcept**](PointConcept.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deletePointUsingDELETE"></a>
# **deletePointUsingDELETE**
> deletePointUsingDELETE(domain, gameId, pointId)

Delete point

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPointConceptControllerApi apiInstance = new DomainPointConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String pointId = "pointId_example"; // String | pointId
try {
    apiInstance.deletePointUsingDELETE(domain, gameId, pointId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPointConceptControllerApi#deletePointUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **pointId** | **String**| pointId |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPointUsingGET"></a>
# **readPointUsingGET**
> PointConcept readPointUsingGET(domain, gameId, pointId)

Get point

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPointConceptControllerApi apiInstance = new DomainPointConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String pointId = "pointId_example"; // String | pointId
try {
    PointConcept result = apiInstance.readPointUsingGET(domain, gameId, pointId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPointConceptControllerApi#readPointUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **pointId** | **String**| pointId |

### Return type

[**PointConcept**](PointConcept.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPointsUsingGET1"></a>
# **readPointsUsingGET1**
> List&lt;PointConcept&gt; readPointsUsingGET1(domain, gameId)

Get points

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPointConceptControllerApi apiInstance = new DomainPointConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<PointConcept> result = apiInstance.readPointsUsingGET1(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPointConceptControllerApi#readPointsUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;PointConcept&gt;**](PointConcept.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updatePointUsingPUT"></a>
# **updatePointUsingPUT**
> updatePointUsingPUT(domain, gameId, point)

Edit point

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainPointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainPointConceptControllerApi apiInstance = new DomainPointConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
PointConcept point = new PointConcept(); // PointConcept | point
try {
    apiInstance.updatePointUsingPUT(domain, gameId, point);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainPointConceptControllerApi#updatePointUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **point** | [**PointConcept**](PointConcept.md)| point |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

