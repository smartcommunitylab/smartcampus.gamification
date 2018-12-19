# DomainClassificationControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addClassificationTaskUsingPOST**](DomainClassificationControllerApi.md#addClassificationTaskUsingPOST) | **POST** /api/{domain}/model/game/{gameId}/classification | Add general classification definition
[**createIncrementalUsingPOST**](DomainClassificationControllerApi.md#createIncrementalUsingPOST) | **POST** /api/{domain}/model/game/{gameId}/incclassification | Add incremental classification definition
[**deleteClassificationTaskUsingDELETE**](DomainClassificationControllerApi.md#deleteClassificationTaskUsingDELETE) | **DELETE** /api/{domain}/model/game/{gameId}/task/{classificationId} | Delete general classification definition
[**deleteIncrementalUsingDELETE**](DomainClassificationControllerApi.md#deleteIncrementalUsingDELETE) | **DELETE** /api/{domain}/model/game/{gameId}/incclassification/{classificationId} | Delete incremental classification definition
[**editClassificationTaskUsingPUT**](DomainClassificationControllerApi.md#editClassificationTaskUsingPUT) | **PUT** /api/{domain}/model/game/{gameId}/classification/{classificationId} | Edit general classification definition
[**getGeneralClassificationUsingGET**](DomainClassificationControllerApi.md#getGeneralClassificationUsingGET) | **GET** /api/{domain}/data/game/{gameId}/classification/{classificationId} | Read general classification board
[**getIncrementalClassificationUsingGET**](DomainClassificationControllerApi.md#getIncrementalClassificationUsingGET) | **GET** /api/{domain}/data/game/{gameId}/incclassification/{classificationId} | Read incremental classification board
[**readAllGeneralClassificationsUsingGET**](DomainClassificationControllerApi.md#readAllGeneralClassificationsUsingGET) | **GET** /api/{domain}/model/game/{gameId}/classification | Get general classification definitions
[**readAllIncrementalUsingGET**](DomainClassificationControllerApi.md#readAllIncrementalUsingGET) | **GET** /api/{domain}/model/game/{gameId}/incclassification | Get incremental classification defintions
[**readGeneralClassificationUsingGET**](DomainClassificationControllerApi.md#readGeneralClassificationUsingGET) | **GET** /api/{domain}/model/game/{gameId}/classification/{classificationId} | Get general classification definition
[**readIncrementalUsingGET**](DomainClassificationControllerApi.md#readIncrementalUsingGET) | **GET** /api/{domain}/model/game/{gameId}/incclassification/{classificationId} | Get incremental classification defition
[**updateIncrementalClassificationUsingPUT**](DomainClassificationControllerApi.md#updateIncrementalClassificationUsingPUT) | **PUT** /api/{domain}/model/game/{gameId}/incclassification/{classificationId} | Edit general classification definition


<a name="addClassificationTaskUsingPOST"></a>
# **addClassificationTaskUsingPOST**
> GeneralClassificationDTO addClassificationTaskUsingPOST(domain, gameId, task)

Add general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    GeneralClassificationDTO result = apiInstance.addClassificationTaskUsingPOST(domain, gameId, task);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#addClassificationTaskUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

[**GeneralClassificationDTO**](GeneralClassificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="createIncrementalUsingPOST"></a>
# **createIncrementalUsingPOST**
> IncrementalClassificationDTO createIncrementalUsingPOST(domain, gameId, classification)

Add incremental classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
IncrementalClassificationDTO classification = new IncrementalClassificationDTO(); // IncrementalClassificationDTO | classification
try {
    IncrementalClassificationDTO result = apiInstance.createIncrementalUsingPOST(domain, gameId, classification);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#createIncrementalUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classification** | [**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)| classification |

### Return type

[**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteClassificationTaskUsingDELETE"></a>
# **deleteClassificationTaskUsingDELETE**
> deleteClassificationTaskUsingDELETE(domain, gameId, classificationId)

Delete general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    apiInstance.deleteClassificationTaskUsingDELETE(domain, gameId, classificationId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#deleteClassificationTaskUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteIncrementalUsingDELETE"></a>
# **deleteIncrementalUsingDELETE**
> deleteIncrementalUsingDELETE(domain, gameId, classificationId)

Delete incremental classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    apiInstance.deleteIncrementalUsingDELETE(domain, gameId, classificationId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#deleteIncrementalUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editClassificationTaskUsingPUT"></a>
# **editClassificationTaskUsingPUT**
> editClassificationTaskUsingPUT(domain, gameId, classificationId, task)

Edit general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
GeneralClassificationDTO task = new GeneralClassificationDTO(); // GeneralClassificationDTO | task
try {
    apiInstance.editClassificationTaskUsingPUT(domain, gameId, classificationId, task);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#editClassificationTaskUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **task** | [**GeneralClassificationDTO**](GeneralClassificationDTO.md)| task |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getGeneralClassificationUsingGET"></a>
# **getGeneralClassificationUsingGET**
> ClassificationBoard getGeneralClassificationUsingGET(domain, gameId, classificationId, page, size)

Read general classification board

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
Integer page = -1; // Integer | page
Integer size = -1; // Integer | size
try {
    ClassificationBoard result = apiInstance.getGeneralClassificationUsingGET(domain, gameId, classificationId, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#getGeneralClassificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **page** | **Integer**| page | [optional] [default to -1]
 **size** | **Integer**| size | [optional] [default to -1]

### Return type

[**ClassificationBoard**](ClassificationBoard.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getIncrementalClassificationUsingGET"></a>
# **getIncrementalClassificationUsingGET**
> ClassificationBoard getIncrementalClassificationUsingGET(domain, gameId, classificationId, timestamp, periodInstanceIndex, page, size)

Read incremental classification board

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
Long timestamp = -1L; // Long | timestamp
Integer periodInstanceIndex = -1; // Integer | periodInstanceIndex
Integer page = -1; // Integer | page
Integer size = -1; // Integer | size
try {
    ClassificationBoard result = apiInstance.getIncrementalClassificationUsingGET(domain, gameId, classificationId, timestamp, periodInstanceIndex, page, size);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#getIncrementalClassificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **timestamp** | **Long**| timestamp | [optional] [default to -1]
 **periodInstanceIndex** | **Integer**| periodInstanceIndex | [optional] [default to -1]
 **page** | **Integer**| page | [optional] [default to -1]
 **size** | **Integer**| size | [optional] [default to -1]

### Return type

[**ClassificationBoard**](ClassificationBoard.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readAllGeneralClassificationsUsingGET"></a>
# **readAllGeneralClassificationsUsingGET**
> List&lt;GeneralClassificationDTO&gt; readAllGeneralClassificationsUsingGET(domain, gameId)

Get general classification definitions

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<GeneralClassificationDTO> result = apiInstance.readAllGeneralClassificationsUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#readAllGeneralClassificationsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;GeneralClassificationDTO&gt;**](GeneralClassificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readAllIncrementalUsingGET"></a>
# **readAllIncrementalUsingGET**
> List&lt;IncrementalClassificationDTO&gt; readAllIncrementalUsingGET(domain, gameId)

Get incremental classification defintions

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<IncrementalClassificationDTO> result = apiInstance.readAllIncrementalUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#readAllIncrementalUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;IncrementalClassificationDTO&gt;**](IncrementalClassificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readGeneralClassificationUsingGET"></a>
# **readGeneralClassificationUsingGET**
> GeneralClassificationDTO readGeneralClassificationUsingGET(domain, gameId, classificationId)

Get general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    GeneralClassificationDTO result = apiInstance.readGeneralClassificationUsingGET(domain, gameId, classificationId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#readGeneralClassificationUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

[**GeneralClassificationDTO**](GeneralClassificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readIncrementalUsingGET"></a>
# **readIncrementalUsingGET**
> IncrementalClassificationDTO readIncrementalUsingGET(domain, gameId, classificationId)

Get incremental classification defition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
try {
    IncrementalClassificationDTO result = apiInstance.readIncrementalUsingGET(domain, gameId, classificationId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#readIncrementalUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |

### Return type

[**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateIncrementalClassificationUsingPUT"></a>
# **updateIncrementalClassificationUsingPUT**
> updateIncrementalClassificationUsingPUT(domain, gameId, classificationId, classification)

Edit general classification definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainClassificationControllerApi;


DomainClassificationControllerApi apiInstance = new DomainClassificationControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String classificationId = "classificationId_example"; // String | classificationId
IncrementalClassificationDTO classification = new IncrementalClassificationDTO(); // IncrementalClassificationDTO | classification
try {
    apiInstance.updateIncrementalClassificationUsingPUT(domain, gameId, classificationId, classification);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainClassificationControllerApi#updateIncrementalClassificationUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **classificationId** | **String**| classificationId |
 **classification** | [**IncrementalClassificationDTO**](IncrementalClassificationDTO.md)| classification |

### Return type

null (empty response body)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

