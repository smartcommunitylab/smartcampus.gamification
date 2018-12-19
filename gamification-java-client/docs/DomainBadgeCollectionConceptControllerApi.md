# DomainBadgeCollectionConceptControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addBadgeUsingPOST**](DomainBadgeCollectionConceptControllerApi.md#addBadgeUsingPOST) | **POST** /api/{domain}/model/game/{gameId}/badges | Add a badge collection
[**deleteBadgeCollectionUsingDELETE**](DomainBadgeCollectionConceptControllerApi.md#deleteBadgeCollectionUsingDELETE) | **DELETE** /api/{domain}/model/game/{gameId}/badges/{collectionId} | Delete a badge collection
[**readBadgeCollectionUsingGET**](DomainBadgeCollectionConceptControllerApi.md#readBadgeCollectionUsingGET) | **GET** /api/{domain}/model/game/{gameId}/badges/{collectionId} | Get a badge collection
[**readBadgeCollectionsUsingGET**](DomainBadgeCollectionConceptControllerApi.md#readBadgeCollectionsUsingGET) | **GET** /api/{domain}/model/game/{gameId}/badges | Get the badge collections
[**updateBadgeCollectionUsingPUT**](DomainBadgeCollectionConceptControllerApi.md#updateBadgeCollectionUsingPUT) | **PUT** /api/{domain}/model/game/{gameId}/badges/{collectionId} | Update a badge collection


<a name="addBadgeUsingPOST"></a>
# **addBadgeUsingPOST**
> addBadgeUsingPOST(domain, gameId, badge)

Add a badge collection

Add a badge collection to the game definition

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainBadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainBadgeCollectionConceptControllerApi apiInstance = new DomainBadgeCollectionConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
BadgeCollectionConcept badge = new BadgeCollectionConcept(); // BadgeCollectionConcept | badge
try {
    apiInstance.addBadgeUsingPOST(domain, gameId, badge);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainBadgeCollectionConceptControllerApi#addBadgeUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **badge** | [**BadgeCollectionConcept**](BadgeCollectionConcept.md)| badge |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteBadgeCollectionUsingDELETE"></a>
# **deleteBadgeCollectionUsingDELETE**
> deleteBadgeCollectionUsingDELETE(domain, gameId, collectionId)

Delete a badge collection

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainBadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainBadgeCollectionConceptControllerApi apiInstance = new DomainBadgeCollectionConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String collectionId = "collectionId_example"; // String | collectionId
try {
    apiInstance.deleteBadgeCollectionUsingDELETE(domain, gameId, collectionId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainBadgeCollectionConceptControllerApi#deleteBadgeCollectionUsingDELETE");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **collectionId** | **String**| collectionId |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readBadgeCollectionUsingGET"></a>
# **readBadgeCollectionUsingGET**
> BadgeCollectionConcept readBadgeCollectionUsingGET(domain, gameId, collectionId)

Get a badge collection

Get the definition of a badge collection in a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainBadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainBadgeCollectionConceptControllerApi apiInstance = new DomainBadgeCollectionConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String collectionId = "collectionId_example"; // String | collectionId
try {
    BadgeCollectionConcept result = apiInstance.readBadgeCollectionUsingGET(domain, gameId, collectionId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainBadgeCollectionConceptControllerApi#readBadgeCollectionUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **collectionId** | **String**| collectionId |

### Return type

[**BadgeCollectionConcept**](BadgeCollectionConcept.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readBadgeCollectionsUsingGET"></a>
# **readBadgeCollectionsUsingGET**
> List&lt;BadgeCollectionConcept&gt; readBadgeCollectionsUsingGET(domain, gameId)

Get the badge collections

Get badge collections in a game

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainBadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainBadgeCollectionConceptControllerApi apiInstance = new DomainBadgeCollectionConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<BadgeCollectionConcept> result = apiInstance.readBadgeCollectionsUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainBadgeCollectionConceptControllerApi#readBadgeCollectionsUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;BadgeCollectionConcept&gt;**](BadgeCollectionConcept.md)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="updateBadgeCollectionUsingPUT"></a>
# **updateBadgeCollectionUsingPUT**
> updateBadgeCollectionUsingPUT(domain, gameId)

Update a badge collection

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.oauth.api.DomainBadgeCollectionConceptControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure OAuth2 access token for authorization: oauth2
OAuth oauth2 = (OAuth) defaultClient.getAuthentication("oauth2");
oauth2.setAccessToken("YOUR ACCESS TOKEN");

DomainBadgeCollectionConceptControllerApi apiInstance = new DomainBadgeCollectionConceptControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    apiInstance.updateBadgeCollectionUsingPUT(domain, gameId);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainBadgeCollectionConceptControllerApi#updateBadgeCollectionUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

null (empty response body)

### Authorization

[oauth2](../README.md#oauth2)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

