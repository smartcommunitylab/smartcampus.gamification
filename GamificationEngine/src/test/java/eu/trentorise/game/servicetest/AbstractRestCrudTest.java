package eu.trentorise.game.servicetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <CC>
 * @param <C>
 * @param <RC>
 * @param <RT>
 */
public abstract class AbstractRestCrudTest<T, CC, C, RC, RT> extends SkipServiceTestHelper {
    
    protected String baseRelativeUrl;
    
    protected IRestCrudTestManager<T, CC, C> manager;
    protected Comparator comparator;

    public AbstractRestCrudTest(String testClassName, String baseRelativeUrl,
                                IRestCrudTestManager<T, CC, C> manager, Comparator comparator) {
        
        super(testClassName);
        
        this.baseRelativeUrl = baseRelativeUrl;
        this.manager = manager;
        this.comparator = comparator;
    }
    
    
    //CREATE
    public void testCreateElement(String testName, 
                                  C containerWithIds,
                                  String baseRelativeUrlExpanded) throws Exception {
        
        T requestElement = manager.createElement(containerWithIds);
        this.manageElementToCreate(requestElement);
        this.executeTestCreateElement(testName, requestElement, 
                                      HttpStatus.CREATED, 
                                      baseRelativeUrlExpanded);
    }
    
    protected abstract T manageElementToCreate(T element);
    
    protected void executeTestCreateElement(String testName, T requestElement,
                                            HttpStatus expectedStatus,
                                            String baseRelativeUrlExpanded) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        String testMsg = this.makeTestMsg(testName);
        
        helper.executeTest(testMsg, this.chooseBaseRelativeUrl(baseRelativeUrlExpanded), HttpMethod.POST, 
                           Void.class, jsonRequest, expectedStatus);
    }
    
    
    //READ COLLECTION
    public void testReadCollection(String testName, CC containerWithIds, 
                                   Class<RC> responseClass,
                                   String baseRelativeUrlExpanded) throws Exception {
        
        Collection<T> expectedElements = manager.createElements(containerWithIds);
        this.executeTestReadCollection(testName, responseClass, 
                                       expectedElements, 
                                       baseRelativeUrlExpanded);
    }
    
    protected void executeTestReadCollection(String testName, 
                                             Class<RC> responseClass,
                                             Collection<T> expectedElements,
                                             String baseRelativeUrlExpanded) throws Exception {
        
        List<T> expectedElementsList = (List<T>) expectedElements;
        
        RestTemplateJsonServiceTestHelper<RC> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        String testMsg = this.makeTestMsg(testName);
        
        RC response = helper.executeTest(testMsg, 
                                         this.chooseBaseRelativeUrl(baseRelativeUrlExpanded),
                                         HttpMethod.GET, responseClass, null);
        
        if (null != response) {
            List<T> responseElements = this.retrieveCollection(response);
            
            assertNotNull(responseElements);
            assertEquals(responseElements.size(), expectedElementsList.size());
            
            for (int i = 0; i < responseElements.size(); i++) {
                T responseElement = responseElements.get(i);
                T expectedElement = expectedElementsList.get(i);
                
                assertEquals(0, this.comparator.compare(responseElement, 
                                                        expectedElement));
            }
        }
    }
    
    protected abstract List<T> retrieveCollection(RC response);
    
    
    //READ SINGLE ELEMENT
    public void testReadElementById(String testName, C containerWithIds, 
                                    Class<RT> responseClass,
                                    String baseRelativeUrlExpanded) throws Exception {
        
        T expectedElement = manager.createElement(containerWithIds);
        this.executeTestReadElementById(testName,
                                        responseClass, expectedElement,
                                        HttpStatus.OK,
                                        baseRelativeUrlExpanded);
        
        this.manageNegativeElementToReadById(expectedElement);
        this.executeTestReadElementById(testName,
                                        responseClass, expectedElement,
                                        HttpStatus.NOT_FOUND,
                                        baseRelativeUrlExpanded);
    }
    
    protected abstract T manageNegativeElementToReadById(T element);
    
    protected void executeTestReadElementById(String testName,
                                              Class<RT> responseClass,
                                              T expectedElement,
                                              HttpStatus expectedStatus,
                                              String baseRelativeUrlExpanded) throws Exception {
        
        RestTemplateJsonServiceTestHelper<RT> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        String testMsg = this.makeTestMsg(testName);
        
        RT response = helper.executeTest(testMsg,
                                         this.makeSingleRelativeUrl(baseRelativeUrlExpanded, expectedElement),
                                         HttpMethod.GET,
                                         responseClass, 
                                         null, 
                                         expectedStatus);
        
        if (null != response && 0 == expectedStatus.compareTo(HttpStatus.OK)) {
            T responseElement = this.retrieveSingleElement(response);
            
            assertNotNull(responseElement);
            assertEquals(0, this.comparator.compare(expectedElement, responseElement));
        }
    }
    
    protected abstract T retrieveSingleElement(RT response);
    
    
    //UPDATE
    public void testUpdateElement(String testName, 
                                  C containerWithIds,
                                  String baseRelativeUrlExpanded) throws Exception {
        
        T requestElement = manager.createElement(containerWithIds);
        this.managePositiveElementToUpdate(requestElement);
        this.executeTestUpdateElement(testName, requestElement, 
                                      HttpStatus.NO_CONTENT, 
                                      baseRelativeUrlExpanded);
        
        this.manageNegativeElementToUpdate(requestElement);
        this.executeTestUpdateElement(testName, requestElement, 
                                      HttpStatus.NOT_FOUND, 
                                      baseRelativeUrlExpanded);
    }
    
    protected abstract T managePositiveElementToUpdate(T element);
    protected abstract T manageNegativeElementToUpdate(T element);
    
    protected void executeTestUpdateElement(String testName, T requestElement,
                                            HttpStatus expectedStatus,
                                            String baseRelativeUrlExpanded) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String testMsg = this.makeTestMsg(testName);
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest(testMsg,
                           this.makeSingleRelativeUrl(baseRelativeUrlExpanded, requestElement),
                           HttpMethod.PUT,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
    
    
    //DELETE
    public void testDeleteElement(String testName, 
                                  C containerWithIds,
                                  String baseRelativeUrlExpanded) throws Exception {
        
        T requestElement = manager.createElement(containerWithIds);
        this.executeTestDeleteElement(testName, requestElement,
                                      HttpStatus.NO_CONTENT, 
                                      baseRelativeUrlExpanded);
        
        this.manageNegativeElementToDelete(requestElement);
        this.executeTestDeleteElement(testName, requestElement, 
                                      HttpStatus.NOT_FOUND,
                                      baseRelativeUrlExpanded);
    }
    
    protected abstract T manageNegativeElementToDelete(T element);
    
    protected void executeTestDeleteElement(String testName, T requestElement,
                                            HttpStatus expectedStatus,
                                            String baseRelativeUrlExpanded) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        String testMsg = this.makeTestMsg(testName);
        
        helper.executeTest(testMsg,
                           this.makeSingleRelativeUrl(baseRelativeUrlExpanded, requestElement),
                           HttpMethod.DELETE,
                           Void.class, 
                           null,
                           expectedStatus);
    }
    
    
    protected abstract String makeSinglePartRelativeUrl(T element);
    
    
    //TOOLS
    protected String makeTestMsg(String testName) {
        StringBuilder sb = new StringBuilder(this.testClassName);
        sb.append(" - ").append(testName);
        return sb.toString();
    }
    
    protected String makeSingleRelativeUrl(String baseRelativeUrlExpanded, 
                                           T expectedElement) {
        
        StringBuilder sb = new StringBuilder(this.chooseBaseRelativeUrl(baseRelativeUrlExpanded));
        sb.append("/").append(this.makeSinglePartRelativeUrl(expectedElement));
        return sb.toString();
    }
    
    protected String expandUrl(String url, Map<String, Object> uriVariables) {
        if (!uriVariables.isEmpty()) {
            UriComponentsBuilder builder = UriComponentsBuilder.fromPath(url);
            url = builder.buildAndExpand(uriVariables).toString();
        }
        
        return url;
    }

    protected String chooseBaseRelativeUrl(String baseRelativeUrlExpanded) {
        String result = this.baseRelativeUrl;
        
        if (null != baseRelativeUrlExpanded) {
            result = baseRelativeUrlExpanded;
        }
        
        return result;
    }
}