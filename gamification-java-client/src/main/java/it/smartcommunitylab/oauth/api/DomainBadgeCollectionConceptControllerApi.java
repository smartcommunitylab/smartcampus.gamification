/**
 * Copyright 2018-2019 SmartCommunity Lab(FBK-ICT).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

/*
 * Gamification Engine API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package it.smartcommunitylab.oauth.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.reflect.TypeToken;

import it.smartcommunitylab.ApiCallback;
import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.ApiResponse;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.Pair;
import it.smartcommunitylab.ProgressRequestBody;
import it.smartcommunitylab.ProgressResponseBody;
import it.smartcommunitylab.model.BadgeCollectionConcept;

public class DomainBadgeCollectionConceptControllerApi {
    private ApiClient apiClient;

    public DomainBadgeCollectionConceptControllerApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DomainBadgeCollectionConceptControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for addBadgeUsingPOST
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param badge badge (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addBadgeUsingPOSTCall(String domain, String gameId, BadgeCollectionConcept badge, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = badge;

        // create path and map variables
        String localVarPath = "/api/{domain}/model/game/{gameId}/badges"
            .replaceAll("\\{" + "domain" + "\\}", apiClient.escapeString(domain.toString()))
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call addBadgeUsingPOSTValidateBeforeCall(String domain, String gameId, BadgeCollectionConcept badge, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'domain' is set
        if (domain == null) {
            throw new ApiException("Missing the required parameter 'domain' when calling addBadgeUsingPOST(Async)");
        }
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling addBadgeUsingPOST(Async)");
        }
        
        // verify the required parameter 'badge' is set
        if (badge == null) {
            throw new ApiException("Missing the required parameter 'badge' when calling addBadgeUsingPOST(Async)");
        }
        

        com.squareup.okhttp.Call call = addBadgeUsingPOSTCall(domain, gameId, badge, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Add a badge collection
     * Add a badge collection to the game definition
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param badge badge (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void addBadgeUsingPOST(String domain, String gameId, BadgeCollectionConcept badge) throws ApiException {
        addBadgeUsingPOSTWithHttpInfo(domain, gameId, badge);
    }

    /**
     * Add a badge collection
     * Add a badge collection to the game definition
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param badge badge (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> addBadgeUsingPOSTWithHttpInfo(String domain, String gameId, BadgeCollectionConcept badge) throws ApiException {
        com.squareup.okhttp.Call call = addBadgeUsingPOSTValidateBeforeCall(domain, gameId, badge, null, null);
        return apiClient.execute(call);
    }

    /**
     * Add a badge collection (asynchronously)
     * Add a badge collection to the game definition
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param badge badge (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addBadgeUsingPOSTAsync(String domain, String gameId, BadgeCollectionConcept badge, final ApiCallback<Void> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = addBadgeUsingPOSTValidateBeforeCall(domain, gameId, badge, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
    /**
     * Build call for deleteBadgeCollectionUsingDELETE
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deleteBadgeCollectionUsingDELETECall(String domain, String gameId, String collectionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/{domain}/model/game/{gameId}/badges/{collectionId}"
            .replaceAll("\\{" + "domain" + "\\}", apiClient.escapeString(domain.toString()))
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()))
            .replaceAll("\\{" + "collectionId" + "\\}", apiClient.escapeString(collectionId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call deleteBadgeCollectionUsingDELETEValidateBeforeCall(String domain, String gameId, String collectionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'domain' is set
        if (domain == null) {
            throw new ApiException("Missing the required parameter 'domain' when calling deleteBadgeCollectionUsingDELETE(Async)");
        }
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling deleteBadgeCollectionUsingDELETE(Async)");
        }
        
        // verify the required parameter 'collectionId' is set
        if (collectionId == null) {
            throw new ApiException("Missing the required parameter 'collectionId' when calling deleteBadgeCollectionUsingDELETE(Async)");
        }
        

        com.squareup.okhttp.Call call = deleteBadgeCollectionUsingDELETECall(domain, gameId, collectionId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Delete a badge collection
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void deleteBadgeCollectionUsingDELETE(String domain, String gameId, String collectionId) throws ApiException {
        deleteBadgeCollectionUsingDELETEWithHttpInfo(domain, gameId, collectionId);
    }

    /**
     * Delete a badge collection
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> deleteBadgeCollectionUsingDELETEWithHttpInfo(String domain, String gameId, String collectionId) throws ApiException {
        com.squareup.okhttp.Call call = deleteBadgeCollectionUsingDELETEValidateBeforeCall(domain, gameId, collectionId, null, null);
        return apiClient.execute(call);
    }

    /**
     * Delete a badge collection (asynchronously)
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deleteBadgeCollectionUsingDELETEAsync(String domain, String gameId, String collectionId, final ApiCallback<Void> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = deleteBadgeCollectionUsingDELETEValidateBeforeCall(domain, gameId, collectionId, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
    /**
     * Build call for readBadgeCollectionUsingGET
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call readBadgeCollectionUsingGETCall(String domain, String gameId, String collectionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/{domain}/model/game/{gameId}/badges/{collectionId}"
            .replaceAll("\\{" + "domain" + "\\}", apiClient.escapeString(domain.toString()))
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()))
            .replaceAll("\\{" + "collectionId" + "\\}", apiClient.escapeString(collectionId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call readBadgeCollectionUsingGETValidateBeforeCall(String domain, String gameId, String collectionId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'domain' is set
        if (domain == null) {
            throw new ApiException("Missing the required parameter 'domain' when calling readBadgeCollectionUsingGET(Async)");
        }
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling readBadgeCollectionUsingGET(Async)");
        }
        
        // verify the required parameter 'collectionId' is set
        if (collectionId == null) {
            throw new ApiException("Missing the required parameter 'collectionId' when calling readBadgeCollectionUsingGET(Async)");
        }
        

        com.squareup.okhttp.Call call = readBadgeCollectionUsingGETCall(domain, gameId, collectionId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get a badge collection
     * Get the definition of a badge collection in a game
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @return BadgeCollectionConcept
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public BadgeCollectionConcept readBadgeCollectionUsingGET(String domain, String gameId, String collectionId) throws ApiException {
        ApiResponse<BadgeCollectionConcept> resp = readBadgeCollectionUsingGETWithHttpInfo(domain, gameId, collectionId);
        return resp.getData();
    }

    /**
     * Get a badge collection
     * Get the definition of a badge collection in a game
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @return ApiResponse&lt;BadgeCollectionConcept&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<BadgeCollectionConcept> readBadgeCollectionUsingGETWithHttpInfo(String domain, String gameId, String collectionId) throws ApiException {
        com.squareup.okhttp.Call call = readBadgeCollectionUsingGETValidateBeforeCall(domain, gameId, collectionId, null, null);
        Type localVarReturnType = new TypeToken<BadgeCollectionConcept>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get a badge collection (asynchronously)
     * Get the definition of a badge collection in a game
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param collectionId collectionId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call readBadgeCollectionUsingGETAsync(String domain, String gameId, String collectionId, final ApiCallback<BadgeCollectionConcept> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = readBadgeCollectionUsingGETValidateBeforeCall(domain, gameId, collectionId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<BadgeCollectionConcept>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for readBadgeCollectionsUsingGET
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call readBadgeCollectionsUsingGETCall(String domain, String gameId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/{domain}/model/game/{gameId}/badges"
            .replaceAll("\\{" + "domain" + "\\}", apiClient.escapeString(domain.toString()))
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call readBadgeCollectionsUsingGETValidateBeforeCall(String domain, String gameId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'domain' is set
        if (domain == null) {
            throw new ApiException("Missing the required parameter 'domain' when calling readBadgeCollectionsUsingGET(Async)");
        }
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling readBadgeCollectionsUsingGET(Async)");
        }
        

        com.squareup.okhttp.Call call = readBadgeCollectionsUsingGETCall(domain, gameId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get the badge collections
     * Get badge collections in a game
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @return List&lt;BadgeCollectionConcept&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public List<BadgeCollectionConcept> readBadgeCollectionsUsingGET(String domain, String gameId) throws ApiException {
        ApiResponse<List<BadgeCollectionConcept>> resp = readBadgeCollectionsUsingGETWithHttpInfo(domain, gameId);
        return resp.getData();
    }

    /**
     * Get the badge collections
     * Get badge collections in a game
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @return ApiResponse&lt;List&lt;BadgeCollectionConcept&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<List<BadgeCollectionConcept>> readBadgeCollectionsUsingGETWithHttpInfo(String domain, String gameId) throws ApiException {
        com.squareup.okhttp.Call call = readBadgeCollectionsUsingGETValidateBeforeCall(domain, gameId, null, null);
        Type localVarReturnType = new TypeToken<List<BadgeCollectionConcept>>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Get the badge collections (asynchronously)
     * Get badge collections in a game
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call readBadgeCollectionsUsingGETAsync(String domain, String gameId, final ApiCallback<List<BadgeCollectionConcept>> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = readBadgeCollectionsUsingGETValidateBeforeCall(domain, gameId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<BadgeCollectionConcept>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updateBadgeCollectionUsingPUT
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updateBadgeCollectionUsingPUTCall(String domain, String gameId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/api/{domain}/model/game/{gameId}/badges/{collectionId}"
            .replaceAll("\\{" + "domain" + "\\}", apiClient.escapeString(domain.toString()))
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()));

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();

        Map<String, String> localVarHeaderParams = new HashMap<String, String>();

        Map<String, Object> localVarFormParams = new HashMap<String, Object>();

        final String[] localVarAccepts = {
            "application/json"
        };
        final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) localVarHeaderParams.put("Accept", localVarAccept);

        final String[] localVarContentTypes = {
            "application/json"
        };
        final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        if(progressListener != null) {
            apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
                @Override
                public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
                    com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                    return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                    .build();
                }
            });
        }

        String[] localVarAuthNames = new String[] { "oauth2" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updateBadgeCollectionUsingPUTValidateBeforeCall(String domain, String gameId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'domain' is set
        if (domain == null) {
            throw new ApiException("Missing the required parameter 'domain' when calling updateBadgeCollectionUsingPUT(Async)");
        }
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling updateBadgeCollectionUsingPUT(Async)");
        }
        

        com.squareup.okhttp.Call call = updateBadgeCollectionUsingPUTCall(domain, gameId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Update a badge collection
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void updateBadgeCollectionUsingPUT(String domain, String gameId) throws ApiException {
        updateBadgeCollectionUsingPUTWithHttpInfo(domain, gameId);
    }

    /**
     * Update a badge collection
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> updateBadgeCollectionUsingPUTWithHttpInfo(String domain, String gameId) throws ApiException {
        com.squareup.okhttp.Call call = updateBadgeCollectionUsingPUTValidateBeforeCall(domain, gameId, null, null);
        return apiClient.execute(call);
    }

    /**
     * Update a badge collection (asynchronously)
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updateBadgeCollectionUsingPUTAsync(String domain, String gameId, final ApiCallback<Void> callback) throws ApiException {

        ProgressResponseBody.ProgressListener progressListener = null;
        ProgressRequestBody.ProgressRequestListener progressRequestListener = null;

        if (callback != null) {
            progressListener = new ProgressResponseBody.ProgressListener() {
                @Override
                public void update(long bytesRead, long contentLength, boolean done) {
                    callback.onDownloadProgress(bytesRead, contentLength, done);
                }
            };

            progressRequestListener = new ProgressRequestBody.ProgressRequestListener() {
                @Override
                public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
                    callback.onUploadProgress(bytesWritten, contentLength, done);
                }
            };
        }

        com.squareup.okhttp.Call call = updateBadgeCollectionUsingPUTValidateBeforeCall(domain, gameId, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
}
