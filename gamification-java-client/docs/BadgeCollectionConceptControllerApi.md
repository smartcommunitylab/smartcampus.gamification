# BadgeCollectionConceptControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addBadgeUsingPOST**](BadgeCollectionConceptControllerApi.md#addBadgeUsingPOST) | **POST** /model/game/{gameId}/badges | Add a badge collection
[**deleteBadgeCollectionUsingDELETE**](BadgeCollectionConceptControllerApi.md#deleteBadgeCollectionUsingDELETE) | **DELETE** /model/game/{gameId}/badges/{collectionId} | Delete a badge collection
[**readBadgeCollectionUsingGET**](BadgeCollectionConceptControllerApi.md#readBadgeCollectionUsingGET) | **GET** /model/game/{gameId}/badges/{collectionId} | Get a badge collection
[**readBadgeCollectionsUsingGET**](BadgeCollectionConceptControllerApi.md#readBadgeCollectionsUsingGET) | **GET** /model/game/{gameId}/badges | Get the badge collections
[**updateBadgeCollectionUsingPUT**](BadgeCollectionConceptControllerApi.md#updateBadgeCollectionUsingPUT) | **PUT** /model/game/{gameId}/badges/{collectionId} | Update a badge collection


<a name="addBadgeUsingPOST"></a>
# **addBadgeUsingPOST**
> addBadgeUsingPOST(gameId, badge)

Add a badge collection

Add a badge collection to the game definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.BadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

BadgeCollectionConceptControllerApi apiInstance = new BadgeCollectionConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
BadgeCollectionConcept badge = new BadgeCollectionConcept(); // BadgeCollectionConcept | badge
try {
    apiInstance.addBadgeUsingPOST(gameId, badge);
} catch (ApiException e) {
    System.err.println("Exception when calling BadgeCollectionConceptControllerApi#addBadgeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **badge** | [**BadgeCollectionConcept**](BadgeCollectionConcept.md)| badge |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteBadgeCollectionUsingDELETE"></a>
# **deleteBadgeCollectionUsingDELETE**
> deleteBadgeCollectionUsingDELETE(gameId, collectionId)

Delete a badge collection

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.BadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

BadgeCollectionConceptControllerApi apiInstance = new BadgeCollectionConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
String collectionId = "collectionId_example"; // String | collectionId
try {
    apiInstance.deleteBadgeCollectionUsingDELETE(gameId, collectionId);
} catch (ApiException e) {
    System.err.println("Exception when calling BadgeCollectionConceptControllerApi#deleteBadgeCollectionUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **collectionId** | **String**| collectionId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readBadgeCollectionUsingGET"></a>
# **readBadgeCollectionUsingGET**
> BadgeCollectionConcept readBadgeCollectionUsingGET(gameId, collectionId)

Get a badge collection

Get the definition of a badge collection in a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.BadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

BadgeCollectionConceptControllerApi apiInstance = new BadgeCollectionConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
String collectionId = "collectionId_example"; // String | collectionId
try {
    BadgeCollectionConcept result = apiInstance.readBadgeCollectionUsingGET(gameId, collectionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BadgeCollectionConceptControllerApi#readBadgeCollectionUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **collectionId** | **String**| collectionId |

### Return type

[**BadgeCollectionConcept**](BadgeCollectionConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readBadgeCollectionsUsingGET"></a>
# **readBadgeCollectionsUsingGET**
> List&lt;BadgeCollectionConcept&gt; readBadgeCollectionsUsingGET(gameId)

Get the badge collections

Get badge collections in a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.BadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

BadgeCollectionConceptControllerApi apiInstance = new BadgeCollectionConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<BadgeCollectionConcept> result = apiInstance.readBadgeCollectionsUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling BadgeCollectionConceptControllerApi#readBadgeCollectionsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;BadgeCollectionConcept&gt;**](BadgeCollectionConcept.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateBadgeCollectionUsingPUT"></a>
# **updateBadgeCollectionUsingPUT**
> updateBadgeCollectionUsingPUT(gameId)

Update a badge collection

### Example
```java
// Import classes:
//import it.smartcommunitylab.basic.ApiClient;
//import it.smartcommunitylab.basic.ApiException;
//import it.smartcommunitylab.basic.Configuration;
//import it.smartcommunitylab.basic.auth.*;
//import it.smartcommunitylab.basic.api.BadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

BadgeCollectionConceptControllerApi apiInstance = new BadgeCollectionConceptControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.updateBadgeCollectionUsingPUT(gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling BadgeCollectionConceptControllerApi#updateBadgeCollectionUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

null (empty response body)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

