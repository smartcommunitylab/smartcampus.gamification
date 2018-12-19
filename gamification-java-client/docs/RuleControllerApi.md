# RuleControllerApi

All URIs are relative to *https://dev.smartcommunitylab.it/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addRuleUsingPOST1**](RuleControllerApi.md#addRuleUsingPOST1) | **POST** /model/game/{gameId}/rule | Add rule
[**deleteDbRuleUsingDELETE1**](RuleControllerApi.md#deleteDbRuleUsingDELETE1) | **DELETE** /model/game/{gameId}/rule/{ruleId} | Delete rule
[**editRuleUsingPUT**](RuleControllerApi.md#editRuleUsingPUT) | **PUT** /model/game/{gameId}/rule/{ruleId} | Edit rule
[**readAllRulesUsingGET**](RuleControllerApi.md#readAllRulesUsingGET) | **GET** /model/game/{gameId}/rule | Get rules
[**readDbRuleUsingGET1**](RuleControllerApi.md#readDbRuleUsingGET1) | **GET** /model/game/{gameId}/rule/{ruleId} | Get rule
[**validateRuleUsingPOST1**](RuleControllerApi.md#validateRuleUsingPOST1) | **POST** /model/game/{gameId}/rule/validate | Validate rule


<a name="addRuleUsingPOST1"></a>
# **addRuleUsingPOST1**
> RuleDTO addRuleUsingPOST1(gameId, rule)

Add rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.RuleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

RuleControllerApi apiInstance = new RuleControllerApi();
String gameId = "gameId_example"; // String | gameId
RuleDTO rule = new RuleDTO(); // RuleDTO | rule
try {
    RuleDTO result = apiInstance.addRuleUsingPOST1(gameId, rule);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RuleControllerApi#addRuleUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **rule** | [**RuleDTO**](RuleDTO.md)| rule |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDbRuleUsingDELETE1"></a>
# **deleteDbRuleUsingDELETE1**
> Boolean deleteDbRuleUsingDELETE1(gameId, ruleId)

Delete rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.RuleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

RuleControllerApi apiInstance = new RuleControllerApi();
String gameId = "gameId_example"; // String | gameId
String ruleId = "ruleId_example"; // String | ruleId
try {
    Boolean result = apiInstance.deleteDbRuleUsingDELETE1(gameId, ruleId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RuleControllerApi#deleteDbRuleUsingDELETE1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **ruleId** | **String**| ruleId |

### Return type

**Boolean**

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editRuleUsingPUT"></a>
# **editRuleUsingPUT**
> RuleDTO editRuleUsingPUT(gameId, ruleId, rule)

Edit rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.RuleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

RuleControllerApi apiInstance = new RuleControllerApi();
String gameId = "gameId_example"; // String | gameId
String ruleId = "ruleId_example"; // String | ruleId
RuleDTO rule = new RuleDTO(); // RuleDTO | rule
try {
    RuleDTO result = apiInstance.editRuleUsingPUT(gameId, ruleId, rule);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RuleControllerApi#editRuleUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **ruleId** | **String**| ruleId |
 **rule** | [**RuleDTO**](RuleDTO.md)| rule |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readAllRulesUsingGET"></a>
# **readAllRulesUsingGET**
> List&lt;RuleDTO&gt; readAllRulesUsingGET(gameId)

Get rules

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.RuleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

RuleControllerApi apiInstance = new RuleControllerApi();
String gameId = "gameId_example"; // String | gameId
try {
    List<RuleDTO> result = apiInstance.readAllRulesUsingGET(gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RuleControllerApi#readAllRulesUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |

### Return type

[**List&lt;RuleDTO&gt;**](RuleDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readDbRuleUsingGET1"></a>
# **readDbRuleUsingGET1**
> RuleDTO readDbRuleUsingGET1(gameId, ruleId)

Get rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.RuleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

RuleControllerApi apiInstance = new RuleControllerApi();
String gameId = "gameId_example"; // String | gameId
String ruleId = "ruleId_example"; // String | ruleId
try {
    RuleDTO result = apiInstance.readDbRuleUsingGET1(gameId, ruleId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RuleControllerApi#readDbRuleUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **ruleId** | **String**| ruleId |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="validateRuleUsingPOST1"></a>
# **validateRuleUsingPOST1**
> List&lt;String&gt; validateRuleUsingPOST1(gameId, wrapper)

Validate rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiClient;
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.Configuration;
//import it.smartcommunitylab.auth.*;
//import it.smartcommunitylab.basic.api.RuleControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basic
HttpBasicAuth basic = (HttpBasicAuth) defaultClient.getAuthentication("basic");
basic.setUsername("YOUR USERNAME");
basic.setPassword("YOUR PASSWORD");

RuleControllerApi apiInstance = new RuleControllerApi();
String gameId = "gameId_example"; // String | gameId
RuleValidateWrapper wrapper = new RuleValidateWrapper(); // RuleValidateWrapper | wrapper
try {
    List<String> result = apiInstance.validateRuleUsingPOST1(gameId, wrapper);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling RuleControllerApi#validateRuleUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gameId** | **String**| gameId |
 **wrapper** | [**RuleValidateWrapper**](RuleValidateWrapper.md)| wrapper |

### Return type

**List&lt;String&gt;**

### Authorization

[basic](../README.md#basic)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

