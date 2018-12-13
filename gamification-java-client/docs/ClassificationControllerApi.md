# ClassificationControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addClassificationTaskUsingPOST**](ClassificationControllerApi.md#addClassificationTaskUsingPOST) | **POST** /model/game/{gameId}/classification | Add general classification definition
[**createIncrementalUsingPOST**](ClassificationControllerApi.md#createIncrementalUsingPOST) | **POST** /model/game/{gameId}/incclassification | Add incremental classification definition
[**deleteClassificationTaskUsingDELETE**](ClassificationControllerApi.md#deleteClassificationTaskUsingDELETE) | **DELETE** /model/game/{gameId}/task/{classificationId} | Delete general classification definition
[**deleteIncrementalUsingDELETE**](ClassificationControllerApi.md#deleteIncrementalUsingDELETE) | **DELETE** /model/game/{gameId}/incclassification/{classificationId} | Delete incremental classification definition
[**editClassificationTaskUsingPUT**](ClassificationControllerApi.md#editClassificationTaskUsingPUT) | **PUT** /model/game/{gameId}/classification/{classificationId} | Edit general classification definition
[**getGeneralClassificationUsingGET**](ClassificationControllerApi.md#getGeneralClassificationUsingGET) | **GET** /data/game/{gameId}/classification/{classificationId} | Read general classification board
[**getIncrementalClassificationUsingGET**](ClassificationControllerApi.md#getIncrementalClassificationUsingGET) | **GET** /data/game/{gameId}/incclassification/{classificationId} | Read incremental classification board
[**readAllGeneralClassificationsUsingGET**](ClassificationControllerApi.md#readAllGeneralClassificationsUsingGET) | **GET** /model/game/{gameId}/classification | Get general classification definitions
[**readAllIncrementalUsingGET**](ClassificationControllerApi.md#readAllIncrementalUsingGET) | **GET** /model/game/{gameId}/incclassification | Get incremental classification defintions
[**readGeneralClassificationUsingGET**](ClassificationControllerApi.md#readGeneralClassificationUsingGET) | **GET** /model/game/{gameId}/classification/{classificationId} | Get general classification definition
[**readIncrementalUsingGET**](ClassificationControllerApi.md#readIncrementalUsingGET) | **GET** /model/game/{gameId}/incclassification/{classificationId} | Get incremental classification defition
[**updateIncrementalClassificationUsingPUT**](ClassificationControllerApi.md#updateIncrementalClassificationUsingPUT) | **PUT** /model/game/{gameId}/incclassification/{classificationId} | Edit general classification definition


<a name="addClassificationTaskUsingPOST"></a>
# **addClassificationTaskUsingPOST**
> GeneralClassificationDTO addClassificationTaskUsingPOST(gameId, task)

Add general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    GeneralClassificationDTO result = apiInstance.addClassificationTaskUsingPOST(gameId, task);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#addClassificationTaskUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

[**GeneralClassificationDTO**](GeneralClassificationDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createIncrementalUsingPOST"></a>
# **createIncrementalUsingPOST**
> IncrementalClassificationDTO createIncrementalUsingPOST(gameId, classification)

Add incremental classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
IncrementalClassificationDTO classification = new IncrementalClassificationDTO(); // IncrementalClassificationDTO | classification
try {
    IncrementalClassificationDTO result = apiInstance.createIncrementalUsingPOST(gameId, classification);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#createIncrementalUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classification** | [**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)| classification |

### Return type

[**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteClassificationTaskUsingDELETE"></a>
# **deleteClassificationTaskUsingDELETE**
> deleteClassificationTaskUsingDELETE(gameId, classificationId)

Delete general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    apiInstance.deleteClassificationTaskUsingDELETE(gameId, classificationId);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#deleteClassificationTaskUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteIncrementalUsingDELETE"></a>
# **deleteIncrementalUsingDELETE**
> deleteIncrementalUsingDELETE(gameId, classificationId)

Delete incremental classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    apiInstance.deleteIncrementalUsingDELETE(gameId, classificationId);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#deleteIncrementalUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editClassificationTaskUsingPUT"></a>
# **editClassificationTaskUsingPUT**
> editClassificationTaskUsingPUT(gameId, classificationId, task)

Edit general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    apiInstance.editClassificationTaskUsingPUT(gameId, classificationId, task);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#editClassificationTaskUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getGeneralClassificationUsingGET"></a>
# **getGeneralClassificationUsingGET**
> ClassificationBoard getGeneralClassificationUsingGET(gameId, classificationId, page, size)

Read general classification board

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
Integer page = -1; // Integer | page
Integer size = -1; // Integer | size
try {
    ClassificationBoard result = apiInstance.getGeneralClassificationUsingGET(gameId, classificationId, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#getGeneralClassificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **page** | **Integer**| page | [optional] [default to -1]
 **size** | **Integer**| size | [optional] [default to -1]

### Return type

[**ClassificationBoard**](ClassificationBoard.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getIncrementalClassificationUsingGET"></a>
# **getIncrementalClassificationUsingGET**
> ClassificationBoard getIncrementalClassificationUsingGET(gameId, classificationId, timestamp, periodInstanceIndex, page, size)

Read incremental classification board

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
Long timestamp = -1L; // Long | timestamp
Integer periodInstanceIndex = -1; // Integer | periodInstanceIndex
Integer page = -1; // Integer | page
Integer size = -1; // Integer | size
try {
    ClassificationBoard result = apiInstance.getIncrementalClassificationUsingGET(gameId, classificationId, timestamp, periodInstanceIndex, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#getIncrementalClassificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **timestamp** | **Long**| timestamp | [optional] [default to -1]
 **periodInstanceIndex** | **Integer**| periodInstanceIndex | [optional] [default to -1]
 **page** | **Integer**| page | [optional] [default to -1]
 **size** | **Integer**| size | [optional] [default to -1]

### Return type

[**ClassificationBoard**](ClassificationBoard.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readAllGeneralClassificationsUsingGET"></a>
# **readAllGeneralClassificationsUsingGET**
> List&lt;GeneralClassificationDTO&gt; readAllGeneralClassificationsUsingGET(gameId)

Get general classification definitions

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<GeneralClassificationDTO> result = apiInstance.readAllGeneralClassificationsUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#readAllGeneralClassificationsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;GeneralClassificationDTO&gt;**](GeneralClassificationDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readAllIncrementalUsingGET"></a>
# **readAllIncrementalUsingGET**
> List&lt;IncrementalClassificationDTO&gt; readAllIncrementalUsingGET(gameId)

Get incremental classification defintions

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<IncrementalClassificationDTO> result = apiInstance.readAllIncrementalUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#readAllIncrementalUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;IncrementalClassificationDTO&gt;**](IncrementalClassificationDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGeneralClassificationUsingGET"></a>
# **readGeneralClassificationUsingGET**
> GeneralClassificationDTO readGeneralClassificationUsingGET(gameId, classificationId)

Get general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    GeneralClassificationDTO result = apiInstance.readGeneralClassificationUsingGET(gameId, classificationId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#readGeneralClassificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

[**GeneralClassificationDTO**](GeneralClassificationDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readIncrementalUsingGET"></a>
# **readIncrementalUsingGET**
> IncrementalClassificationDTO readIncrementalUsingGET(gameId, classificationId)

Get incremental classification defition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    IncrementalClassificationDTO result = apiInstance.readIncrementalUsingGET(gameId, classificationId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#readIncrementalUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

[**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateIncrementalClassificationUsingPUT"></a>
# **updateIncrementalClassificationUsingPUT**
> updateIncrementalClassificationUsingPUT(gameId, classificationId, classification)

Edit general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.api.ClassificationControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

ClassificationControllerApi apiInstance = new ClassificationControllerApi();
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
IncrementalClassificationDTO classification = new IncrementalClassificationDTO(); // IncrementalClassificationDTO | classification
try {
    apiInstance.updateIncrementalClassificationUsingPUT(gameId, classificationId, classification);
} catch (ApiException e) {
    System.err.println("Exception when calling ClassificationControllerApi#updateIncrementalClassificationUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **classification** | [**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)| classification |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

