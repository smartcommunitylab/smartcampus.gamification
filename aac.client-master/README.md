# AAC API Client Library

AAC API Client library that wraps standard functionality and different specific services. The library is
intended for use in server-side Java applications.

See [AAC documentation](https://github.com/scc-digitalhub/AAC) for API details. 

To include the library as Maven dependency use the following definitions:

```
    <dependency>
      <groupId>it.smartcommunitylab</groupId>
      <artifactId>aac.client</artifactId>
      <version>3.1.1</version>
    </dependency>
    
    ...
    
  <repositories>
    <repository>
      <id>SmartCommunity</id>
      <url>http://repository.smartcommunitylab.it/content/groups/public</url>
    </repository>
    ...
  </repositories>
```

## 1. Common Methods

The ``AAACService`` class provides a set common OAuth2.0 methods. First, initialize an instance of
the class providing AAC endpoint, client ID, and client secret:

```java
AACService aacService = new AACService("https://aac.example.com/aac", "my-client-id", "my-client-secret");
```

### 1.1 Generate Client Token  

To generate client credentials flow token,

use the following code:

```java
TokenData token = aacService.generateClientToken(scope);
```

where,

- ``scope`` specific scopes to be used. If omitted, all the scopes associated to the client app will be engaged.

The response will represent the TokenData data structure containing

- ``scope`` associated scope
- ``access_token`` token value
- ``token_type`` evaluates to 'Bearer'
- ``expires_in`` duration of token in seconds


### 1.2 Authorization Code Flow redirect

to generate the redirect URL for the authorization code flow, use the following code:

```java
String url = aacService.generateAuthorizationURIForCodeFlow(redirectURL, authority, scope, state);
```

where,

- ``authority`` corresponds to the Identity Provider to be used. If omitted, a login screen with all enabled IdPs will be present.
- ``scope`` specific scopes to be used. If omitted, all the scopes associated to the client app will be engaged.
- ``state`` state validation parameter (may be null).

The authorization code flow should be enabled in the AAC console. Specifically, it is necessary to 
configure the redirect URL for the application, and the basic profile scopes (e.g., openid, profile, etc) 
in API Access / User Profile Service.

To exchange code for token, use   

```java
TokenData token = aacService.exchangeCodeForToken(code, redirectUri);
```

The response will represent the TokenData data structure containing

- ``scope`` associated scope
- ``access_token`` token value
- ``refresh_token`` refresh token value
- ``token_type`` evaluates to 'Bearer'
- ``id_token`` Open ID token (if applicable)
- ``expires_in`` duration of token in seconds

To refresh the token once expired, use the following code:

```java
TokenData token = aacService.refreshToken(refreshToken);
```

### 1.3 Password Grant Token

To generate user token with the password grant token (enabled for the Trusted clients only),
use the following code:

```java
TokenData token = aacService.generateUserToken(username, password, scope);
```


### 1.4 Token and User metadata

While the token i JWT format encodes all the necessary data, it is also possible to use API that provides
the detailed information about token and the user. Specifically,

```java
AACTokenIntrospection tokenData = aacService.tokenInfo(token); 
```

allows to obtain details about standard token claims and validate the token: if the token is invalid (expired, revoked, etc),
the method will throw an exception. 


To obtain user data
 
 
```java
Map<String, Object> usernData = aacService.userInfo(token); 
```
 
which provides a map of user claims (if the corresponding token is valid). 

## 2. Specific API classes

The library includes a set of classes to deal with the custom AAC APIs. In particular

- ``AACProfileService`` deals with the user profile and account information
- ``AACRoleService`` deals with the Role API
- ``AACAuthorizationService`` deals with the authorization module
- ``AACServiceDefinitionService`` deals with the API to create and manage custom resource services (scopes, claims definition)
- ``AACUserClaimService`` deals with the API to associate custom claims for the user.