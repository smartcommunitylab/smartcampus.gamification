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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import it.smartcommunitylab.ApiClient;
import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.auth.HttpBasicAuth;
import it.smartcommunitylab.model.ext.PointConcept;

/**
 * API tests for PointConceptControllerApi
 */
@Ignore
public class PointConceptControllerApiTest {

    private final PointConceptControllerApi api = new PointConceptControllerApi();
    private ApiClient apiClient;
    private final String userName = "long-rovereto";
    private final String password = "rov";
    private String baseUrl = "http://localhost:6060/gamification";
    private String gameId = "57ac710fd4c6ac7872b0e7a1";
    private String playerId = "24153";
    private String conceptName = "green leaves";

    @Before
    public void init() {
    	 apiClient = new ApiClient(baseUrl);
    	
    	 // Configure OAuth2 access token for authorization: oauth2
    	 // OAuth oauth2 = (OAuth) apiClient.getAuthentication("oauth2");
    	 // oauth2.setAccessToken("YOUR_ACCESS_TOKEN");
    	 
    	 // Configure basic auth. 
    	 HttpBasicAuth basic = (HttpBasicAuth) apiClient.getAuthentication("basic");
    	 basic.setUsername(userName);
    	 basic.setPassword(password);

    	 api.setApiClient(apiClient);
    }
    
    /**
     * Add point
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
//    @Test
    public void addPointUsingPOST1Test() throws ApiException {
        PointConcept point = new PointConcept("04012019");
        point.setName("testPoint");
        point.setScore(2.0);
        Map<String, PointConcept.PeriodInternal> periods = new HashMap<>();
		point.setPeriods(periods );
        PointConcept response = api.addPointUsingPOST1(gameId, point);

        System.out.println(response.getId());
    }
    
    /**
     * Delete point
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void deletePointUsingDELETETest() throws ApiException {
        String gameId = null;
        String pointId = null;
        api.deletePointUsingDELETE(gameId, pointId);

        // TODO: test validations
    }
    
    /**
     * Get point
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     * @throws IOException 
     * @throws IllegalArgumentException 
     * @throws ClassNotFoundException 
     */
    @Test
    public void readPointUsingGETTest() throws ApiException, IllegalArgumentException, IOException, ClassNotFoundException {
        String pointId = "1";
        PointConcept response = api.readPointUsingGET(gameId, pointId);

        System.out.println(response);
    }
    
    /**
     * Get points
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     * @throws IOException 
     * @throws ClassNotFoundException 
     * @throws IllegalArgumentException 
     * @throws JsonMappingException 
     * @throws JsonParseException 
     */
    @Test
    public void readPointsUsingGET1Test() throws ApiException, JsonParseException, JsonMappingException, IllegalArgumentException, ClassNotFoundException, IOException {
        List<PointConcept> response = api.readPointsUsingGET1(gameId);

        System.out.println(response.size());
    }
    
    /**
     * Edit point
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
//	@Test
	public void updatePointUsingPUTTest() throws ApiException {
		PointConcept point = new PointConcept("3f1addae-52ca-4c60-83cc-d08a36cc13f8");
		point.setName("testPoint");
		point.setScore(1.0);
		Map<String, PointConcept.PeriodInternal> periods = new HashMap<>();
		point.setPeriods(periods);
		api.updatePointUsingPUT(gameId, point);
		
	}
    
}
