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


package it.smartcommunitylab.basic.api;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Response;

import it.smartcommunitylab.ApiCallback;
import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.ApiResponse;
import it.smartcommunitylab.Configuration;
import it.smartcommunitylab.Pair;
import it.smartcommunitylab.ProgressRequestBody;
import it.smartcommunitylab.ProgressResponseBody;
import it.smartcommunitylab.model.ext.PointConcept;
import it.smartcommunitylab.model.ext.PointConceptControllerUtils;

public class PointConceptControllerApi {
    private ApiClient apiClient;
    ObjectMapper mapper = new ObjectMapper();
    PointConceptControllerUtils pointConceptControllerUtils = new PointConceptControllerUtils();

    public PointConceptControllerApi() {
        this(Configuration.getDefaultApiClient());
    }

    public PointConceptControllerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Build call for addPointUsingPOST1
     * @param gameId gameId (required)
     * @param point point (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call addPointUsingPOST1Call(String gameId, PointConcept point, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = point;

        // create path and map variables
        String localVarPath = "/model/game/{gameId}/point"
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

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call addPointUsingPOST1ValidateBeforeCall(String gameId, PointConcept point, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling addPointUsingPOST1(Async)");
        }
        
        // verify the required parameter 'point' is set
        if (point == null) {
            throw new ApiException("Missing the required parameter 'point' when calling addPointUsingPOST1(Async)");
        }
        

        com.squareup.okhttp.Call call = addPointUsingPOST1Call(gameId, point, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Add point
     * 
     * @param gameId gameId (required)
     * @param point point (required)
     * @return PointConcept
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public PointConcept addPointUsingPOST1(String gameId, PointConcept point) throws ApiException {
        ApiResponse<PointConcept> resp = addPointUsingPOST1WithHttpInfo(gameId, point);
        return resp.getData();
    }

    /**
     * Add point
     * 
     * @param gameId gameId (required)
     * @param point point (required)
     * @return ApiResponse&lt;PointConcept&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<PointConcept> addPointUsingPOST1WithHttpInfo(String gameId, PointConcept point) throws ApiException {
        com.squareup.okhttp.Call call = addPointUsingPOST1ValidateBeforeCall(gameId, point, null, null);
        Type localVarReturnType = new TypeToken<PointConcept>(){}.getType();
        return apiClient.execute(call, localVarReturnType);
    }

    /**
     * Add point (asynchronously)
     * 
     * @param gameId gameId (required)
     * @param point point (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call addPointUsingPOST1Async(String gameId, PointConcept point, final ApiCallback<PointConcept> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = addPointUsingPOST1ValidateBeforeCall(gameId, point, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PointConcept>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for deletePointUsingDELETE
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call deletePointUsingDELETECall(String gameId, String pointId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/model/game/{gameId}/point/{pointId}"
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()))
            .replaceAll("\\{" + "pointId" + "\\}", apiClient.escapeString(pointId.toString()));

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

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "DELETE", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call deletePointUsingDELETEValidateBeforeCall(String gameId, String pointId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling deletePointUsingDELETE(Async)");
        }
        
        // verify the required parameter 'pointId' is set
        if (pointId == null) {
            throw new ApiException("Missing the required parameter 'pointId' when calling deletePointUsingDELETE(Async)");
        }
        

        com.squareup.okhttp.Call call = deletePointUsingDELETECall(gameId, pointId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Delete point
     * 
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void deletePointUsingDELETE(String gameId, String pointId) throws ApiException {
        deletePointUsingDELETEWithHttpInfo(gameId, pointId);
    }

    /**
     * Delete point
     * 
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> deletePointUsingDELETEWithHttpInfo(String gameId, String pointId) throws ApiException {
        com.squareup.okhttp.Call call = deletePointUsingDELETEValidateBeforeCall(gameId, pointId, null, null);
        return apiClient.execute(call);
    }

    /**
     * Delete point (asynchronously)
     * 
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call deletePointUsingDELETEAsync(String gameId, String pointId, final ApiCallback<Void> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = deletePointUsingDELETEValidateBeforeCall(gameId, pointId, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
    /**
     * Build call for readPointUsingGET
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call readPointUsingGETCall(String gameId, String pointId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/model/game/{gameId}/point/{pointId}"
            .replaceAll("\\{" + "gameId" + "\\}", apiClient.escapeString(gameId.toString()))
            .replaceAll("\\{" + "pointId" + "\\}", apiClient.escapeString(pointId.toString()));

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

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call readPointUsingGETValidateBeforeCall(String gameId, String pointId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling readPointUsingGET(Async)");
        }
        
        // verify the required parameter 'pointId' is set
        if (pointId == null) {
            throw new ApiException("Missing the required parameter 'pointId' when calling readPointUsingGET(Async)");
        }
        

        com.squareup.okhttp.Call call = readPointUsingGETCall(gameId, pointId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get point
     * 
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @return PointConcept
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @throws IOException 
     * @throws IllegalArgumentException 
     * @throws ClassNotFoundException 
     */
	public PointConcept readPointUsingGET(String gameId, String pointId)
			throws ApiException, IllegalArgumentException, IOException, ClassNotFoundException {
		Response response = readPointUsingGETWithHttpInfo(gameId, pointId);
		Map myObject = mapper.readValue(response.body().byteStream(), Map.class);
		return pointConceptControllerUtils.convertToSpecificType(myObject);

	}

    /**
     * Get point
     * 
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @return ApiResponse&lt;PointConcept&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response readPointUsingGETWithHttpInfo(String gameId, String pointId) throws ApiException {
        com.squareup.okhttp.Call call = readPointUsingGETValidateBeforeCall(gameId, pointId, null, null);
        Type localVarReturnType = new TypeToken<PointConcept>(){}.getType();
        return apiClient.executeSimple(call, localVarReturnType);
    }

    /**
     * Get point (asynchronously)
     * 
     * @param gameId gameId (required)
     * @param pointId pointId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call readPointUsingGETAsync(String gameId, String pointId, final ApiCallback<PointConcept> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = readPointUsingGETValidateBeforeCall(gameId, pointId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<PointConcept>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for readPointsUsingGET1
     * @param gameId gameId (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call readPointsUsingGET1Call(String gameId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = null;

        // create path and map variables
        String localVarPath = "/model/game/{gameId}/point"
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

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "GET", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call readPointsUsingGET1ValidateBeforeCall(String gameId, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling readPointsUsingGET1(Async)");
        }
        

        com.squareup.okhttp.Call call = readPointsUsingGET1Call(gameId, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Get points
     * 
     * @param gameId gameId (required)
     * @return List&lt;PointConcept&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     * @throws ClassNotFoundException 
     * @throws IllegalArgumentException 
     * @throws IOException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    public List<PointConcept> readPointsUsingGET1(String gameId) throws ApiException, JsonParseException, JsonMappingException, IOException, IllegalArgumentException, ClassNotFoundException {
    	Response response = readPointsUsingGET1WithHttpInfo(gameId);
    	Map[] myObjects = mapper.readValue(response.body().byteStream(), Map[].class);
        return pointConceptControllerUtils.convertToSpecificType(myObjects);
    }

    /**
     * Get points
     * 
     * @param gameId gameId (required)
     * @return ApiResponse&lt;List&lt;PointConcept&gt;&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public Response readPointsUsingGET1WithHttpInfo(String gameId) throws ApiException {
        com.squareup.okhttp.Call call = readPointsUsingGET1ValidateBeforeCall(gameId, null, null);
        Type localVarReturnType = new TypeToken<List<PointConcept>>(){}.getType();
        return apiClient.executeSimple(call, localVarReturnType);
    }

    /**
     * Get points (asynchronously)
     * 
     * @param gameId gameId (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call readPointsUsingGET1Async(String gameId, final ApiCallback<List<PointConcept>> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = readPointsUsingGET1ValidateBeforeCall(gameId, progressListener, progressRequestListener);
        Type localVarReturnType = new TypeToken<List<PointConcept>>(){}.getType();
        apiClient.executeAsync(call, localVarReturnType, callback);
        return call;
    }
    /**
     * Build call for updatePointUsingPUT
     * @param gameId gameId (required)
     * @param point point (required)
     * @param progressListener Progress listener
     * @param progressRequestListener Progress request listener
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     */
    public com.squareup.okhttp.Call updatePointUsingPUTCall(String gameId, PointConcept point, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        Object localVarPostBody = point;

        // create path and map variables
        String localVarPath = "/model/game/{gameId}/point/{pointId}"
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

        String[] localVarAuthNames = new String[] { "basic" };
        return apiClient.buildCall(localVarPath, "PUT", localVarQueryParams, localVarCollectionQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
    }

    @SuppressWarnings("rawtypes")
    private com.squareup.okhttp.Call updatePointUsingPUTValidateBeforeCall(String gameId, PointConcept point, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
        
        // verify the required parameter 'gameId' is set
        if (gameId == null) {
            throw new ApiException("Missing the required parameter 'gameId' when calling updatePointUsingPUT(Async)");
        }
        
        // verify the required parameter 'point' is set
        if (point == null) {
            throw new ApiException("Missing the required parameter 'point' when calling updatePointUsingPUT(Async)");
        }
        

        com.squareup.okhttp.Call call = updatePointUsingPUTCall(gameId, point, progressListener, progressRequestListener);
        return call;

    }

    /**
     * Edit point
     * 
     * @param gameId gameId (required)
     * @param point point (required)
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public void updatePointUsingPUT(String gameId, PointConcept point) throws ApiException {
        updatePointUsingPUTWithHttpInfo(gameId, point);
    }

    /**
     * Edit point
     * 
     * @param gameId gameId (required)
     * @param point point (required)
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
     */
    public ApiResponse<Void> updatePointUsingPUTWithHttpInfo(String gameId, PointConcept point) throws ApiException {
        com.squareup.okhttp.Call call = updatePointUsingPUTValidateBeforeCall(gameId, point, null, null);
        return apiClient.execute(call);
    }

    /**
     * Edit point (asynchronously)
     * 
     * @param gameId gameId (required)
     * @param point point (required)
     * @param callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body object
     */
    public com.squareup.okhttp.Call updatePointUsingPUTAsync(String gameId, PointConcept point, final ApiCallback<Void> callback) throws ApiException {

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

        com.squareup.okhttp.Call call = updatePointUsingPUTValidateBeforeCall(gameId, point, progressListener, progressRequestListener);
        apiClient.executeAsync(call, callback);
        return call;
    }
}
