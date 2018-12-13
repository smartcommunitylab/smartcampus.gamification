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


package it.smartcommunitylab.api;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

import it.smartcommunitylab.ApiException;
import it.smartcommunitylab.model.PointConcept;

/**
 * API tests for PointConceptControllerApi
 */
@Ignore
public class PointConceptControllerApiTest {

    private final PointConceptControllerApi api = new PointConceptControllerApi();

    
    /**
     * Add point
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void addPointUsingPOST1Test() throws ApiException {
        String gameId = null;
        PointConcept point = null;
        PointConcept response = api.addPointUsingPOST1(gameId, point);

        // TODO: test validations
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
     */
    @Test
    public void readPointUsingGETTest() throws ApiException {
        String gameId = null;
        String pointId = null;
        PointConcept response = api.readPointUsingGET(gameId, pointId);

        // TODO: test validations
    }
    
    /**
     * Get points
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void readPointsUsingGET1Test() throws ApiException {
        String gameId = null;
        List<PointConcept> response = api.readPointsUsingGET1(gameId);

        // TODO: test validations
    }
    
    /**
     * Edit point
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void updatePointUsingPUTTest() throws ApiException {
        String gameId = null;
        PointConcept point = null;
        api.updatePointUsingPUT(gameId, point);

        // TODO: test validations
    }
    
}