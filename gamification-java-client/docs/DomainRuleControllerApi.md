# DomainRuleControllerApi

All URIs are relative to *https://localhost/gamification*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addRuleUsingPOST1**](DomainRuleControllerApi.md#addRuleUsingPOST1) | **POST** /api/{domain}/model/game/{gameId}/rule | Add rule
[**deleteDbRuleUsingDELETE1**](DomainRuleControllerApi.md#deleteDbRuleUsingDELETE1) | **DELETE** /api/{domain}/model/game/{gameId}/rule/{ruleId} | Delete rule
[**editRuleUsingPUT**](DomainRuleControllerApi.md#editRuleUsingPUT) | **PUT** /api/{domain}/model/game/{gameId}/rule/{ruleId} | Edit rule
[**readAllRulesUsingGET**](DomainRuleControllerApi.md#readAllRulesUsingGET) | **GET** /api/{domain}/model/game/{gameId}/rule | Get rules
[**readDbRuleUsingGET1**](DomainRuleControllerApi.md#readDbRuleUsingGET1) | **GET** /api/{domain}/model/game/{gameId}/rule/{ruleId} | Get rule
[**validateRuleUsingPOST1**](DomainRuleControllerApi.md#validateRuleUsingPOST1) | **POST** /api/{domain}/model/game/{gameId}/rule/validate | Validate rule


<a name="addRuleUsingPOST1"></a>
# **addRuleUsingPOST1**
> RuleDTO addRuleUsingPOST1(domain, gameId, rule)

Add rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainRuleControllerApi;


DomainRuleControllerApi apiInstance = new DomainRuleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
RuleDTO rule = new RuleDTO(); // RuleDTO | rule
try {
    RuleDTO result = apiInstance.addRuleUsingPOST1(domain, gameId, rule);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainRuleControllerApi#addRuleUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **rule** | [**RuleDTO**](RuleDTO.md)| rule |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="deleteDbRuleUsingDELETE1"></a>
# **deleteDbRuleUsingDELETE1**
> Boolean deleteDbRuleUsingDELETE1(domain, gameId, ruleId)

Delete rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainRuleControllerApi;


DomainRuleControllerApi apiInstance = new DomainRuleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String ruleId = "ruleId_example"; // String | ruleId
try {
    Boolean result = apiInstance.deleteDbRuleUsingDELETE1(domain, gameId, ruleId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainRuleControllerApi#deleteDbRuleUsingDELETE1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **ruleId** | **String**| ruleId |

### Return type

**Boolean**

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="editRuleUsingPUT"></a>
# **editRuleUsingPUT**
> RuleDTO editRuleUsingPUT(domain, gameId, rule)

Edit rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainRuleControllerApi;


DomainRuleControllerApi apiInstance = new DomainRuleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
RuleDTO rule = new RuleDTO(); // RuleDTO | rule
try {
    RuleDTO result = apiInstance.editRuleUsingPUT(domain, gameId, rule);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainRuleControllerApi#editRuleUsingPUT");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **rule** | [**RuleDTO**](RuleDTO.md)| rule |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readAllRulesUsingGET"></a>
# **readAllRulesUsingGET**
> List&lt;RuleDTO&gt; readAllRulesUsingGET(domain, gameId)

Get rules

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainRuleControllerApi;


DomainRuleControllerApi apiInstance = new DomainRuleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
try {
    List<RuleDTO> result = apiInstance.readAllRulesUsingGET(domain, gameId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainRuleControllerApi#readAllRulesUsingGET");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |

### Return type

[**List&lt;RuleDTO&gt;**](RuleDTO.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="readDbRuleUsingGET1"></a>
# **readDbRuleUsingGET1**
> RuleDTO readDbRuleUsingGET1(domain, gameId, ruleId)

Get rule

### Example
```java
// Import classes:
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainRuleControllerApi;


DomainRuleControllerApi apiInstance = new DomainRuleControllerApi();
String domain = "domain_example"; // String | domain
String gameId = "gameId_example"; // String | gameId
String ruleId = "ruleId_example"; // String | ruleId
try {
    RuleDTO result = apiInstance.readDbRuleUsingGET1(domain, gameId, ruleId);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainRuleControllerApi#readDbRuleUsingGET1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **domain** | **String**| domain |
 **gameId** | **String**| gameId |
 **ruleId** | **String**| ruleId |

### Return type

[**RuleDTO**](RuleDTO.md)

### Authorization

No authorization required

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
//import it.smartcommunitylab.ApiException;
//import it.smartcommunitylab.oauth.api.DomainRuleControllerApi;


DomainRuleControllerApi apiInstance = new DomainRuleControllerApi();
String gameId = "gameId_example"; // String | gameId
RuleValidateWrapper wrapper = new RuleValidateWrapper(); // RuleValidateWrapper | wrapper
try {
    List<String> result = apiInstance.validateRuleUsingPOST1(gameId, wrapper);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling DomainRuleControllerApi#validateRuleUsingPOST1");
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

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

