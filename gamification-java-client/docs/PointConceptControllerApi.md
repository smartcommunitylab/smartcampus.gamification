# PointConceptControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addPointUsingPOST1**](PointConceptControllerApi.md#addPointUsingPOST1) | **POST** /model/game/{gameId}/point | Add point
[**deletePointUsingDELETE**](PointConceptControllerApi.md#deletePointUsingDELETE) | **DELETE** /model/game/{gameId}/point/{pointId} | Delete point
[**readPointUsingGET**](PointConceptControllerApi.md#readPointUsingGET) | **GET** /model/game/{gameId}/point/{pointId} | Get point
[**readPointsUsingGET1**](PointConceptControllerApi.md#readPointsUsingGET1) | **GET** /model/game/{gameId}/point | Get points
[**updatePointUsingPUT**](PointConceptControllerApi.md#updatePointUsingPUT) | **PUT** /model/game/{gameId}/point/{pointId} | Edit point


<a name="addPointUsingPOST1"></a>
# **addPointUsingPOST1**
> PointConcept addPointUsingPOST1(gameId, point)

Add point

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.PointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PointConceptControllerApi apiInstance = new PointConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
PointConcept point = new PointConcept(); // PointConcept | point
try {
    PointConcept result = apiInstance.addPointUsingPOST1(gameId, point);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointConceptControllerApi#addPointUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **point** | [**PointConcept**](PointConcept.md)| point |

### Return type

[**PointConcept**](PointConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deletePointUsingDELETE"></a>
# **deletePointUsingDELETE**
> deletePointUsingDELETE(gameId, pointId)

Delete point

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.PointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PointConceptControllerApi apiInstance = new PointConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
String pointId = "pointId_example"; // String | pointId
try {
    apiInstance.deletePointUsingDELETE(gameId, pointId);
} catch (ApiException e) {
    System.err.println("Exception when calling PointConceptControllerApi#deletePointUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **pointId** | **String**| pointId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPointUsingGET"></a>
# **readPointUsingGET**
> PointConcept readPointUsingGET(gameId, pointId)

Get point

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.PointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PointConceptControllerApi apiInstance = new PointConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
String pointId = "pointId_example"; // String | pointId
try {
    PointConcept result = apiInstance.readPointUsingGET(gameId, pointId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointConceptControllerApi#readPointUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **pointId** | **String**| pointId |

### Return type

[**PointConcept**](PointConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readPointsUsingGET1"></a>
# **readPointsUsingGET1**
> List&lt;PointConcept&gt; readPointsUsingGET1(gameId)

Get points

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.PointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PointConceptControllerApi apiInstance = new PointConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<PointConcept> result = apiInstance.readPointsUsingGET1(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling PointConceptControllerApi#readPointsUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;PointConcept&gt;**](PointConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updatePointUsingPUT"></a>
# **updatePointUsingPUT**
> updatePointUsingPUT(gameId, point)

Edit point

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.PointConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

PointConceptControllerApi apiInstance = new PointConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
PointConcept point = new PointConcept(); // PointConcept | point
try {
    apiInstance.updatePointUsingPUT(gameId, point);
} catch (ApiException e) {
    System.err.println("Exception when calling PointConceptControllerApi#updatePointUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **point** | [**PointConcept**](PointConcept.md)| point |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

