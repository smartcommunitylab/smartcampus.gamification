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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.smartcommunitylab.ApiCallback;
import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.ApiResponse;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.Pair;
import it.smartcommunitylab.ProgressRequestBody;
import it.smartcommunitylab.ProgressResponseBody;
import it.smartcommunitylab.model.ExecutionDataDTO;

public class DomainExecutionControllerApi {
    private ApiClient apiClient;

    public DomainExecutionControllerApi() {
        this(Configuration.getDefaultApiClient());
    }

    public DomainExecutionControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for executeActionUsingPOST
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param actionId actionId (required)
     * @param data data (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call executeActionUsingPOSTCall(String domain, String gameId, String actionId, ExecutionDataDTO data, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = data;

        // create path and map variables
        String localVarPath = "/api/{domain}/exec/game/{gameId}/action/{actionId}"
            .replaceAll("\\{" + "domain" + "\\}", apiClient.escapeString(domain.toString()))
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()))
            .replaceAll("\\{" + "actionId" + "\\}", apiClient.escapeString(actionId.toString()));

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
    private com.squareup.okhttp.Call executeActionUsingPOSTValidateBeforeCall(String domain, String gameId, String actionId, ExecutionDataDTO data, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'domain' is set
        if (domain == null) {
            throw new ApiException("Missing the required parameter 'domain' when calling executeActionUsingPOST(Async)");
        }
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling executeActionUsingPOST(Async)");
        }
        
        // verify the required parameter 'actionId' is set
        if (actionId == null) {
            throw new ApiException("Missing the required parameter 'actionId' when calling executeActionUsingPOST(Async)");
        }
        
        // verify the required parameter 'data' is set
        if (data == null) {
            throw new ApiException("Missing the required parameter 'data' when calling executeActionUsingPOST(Async)");
        }
        

        com.squareup.okhttp.Call call = executeActionUsingPOSTCall(domain, gameId, actionId, data, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Execute an action
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param actionId actionId (required)
     * @param data data (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void executeActionUsingPOST(String domain, String gameId, String actionId, ExecutionDataDTO data) throws ApiException {
        executeActionUsingPOSTWithHttpInfo(domain, gameId, actionId, data);
    }

    /**
     * Execute an action
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param actionId actionId (required)
     * @param data data (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> executeActionUsingPOSTWithHttpInfo(String domain, String gameId, String actionId, ExecutionDataDTO data) throws ApiException {
        com.squareup.okhttp.Call call = executeActionUsingPOSTValidateBeforeCall(domain, gameId, actionId, data, null, null);
        return apiClient.execute(call);
    }

    /**
     * Execute an action (asynchronously)
     * 
     * @param domain domain (required)
     * @param gameId gameId (required)
     * @param actionId actionId (required)
     * @param data data (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call executeActionUsingPOSTAsync(String domain, String gameId, String actionId, ExecutionDataDTO data, final ApiCallback<Void> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = executeActionUsingPOSTValidateBeforeCall(domain, gameId, actionId, data, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
}
