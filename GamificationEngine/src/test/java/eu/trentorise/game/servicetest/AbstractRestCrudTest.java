package eu.trentorise.game.servicetest;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Luca Piras
 * @param <T>
 * @param <RC>
 * @param <RT>
 */
public abstract class AbstractRestCrudTest<T, RC, RT> extends SkipServiceTestHelper {
    
    protected String baseRelativeUrl;
    
    protected IRestCrudTestManager<T> manager;
    protected Comparator comparator;

    public AbstractRestCrudTest(String testClassName, String baseRelativeUrl,
                                IRestCrudTestManager<T> manager, Comparator comparator) {
        
        super(testClassName);
        
        this.baseRelativeUrl = baseRelativeUrl;
        this.manager = manager;
        this.comparator = comparator;
    }
    
    
    //CREATE
    public void testCreateElement(String testName) throws Exception {
        T requestElement = manager.createElement();
        this.manageElementToCreate(requestElement);
        this.executeTestCreateElement(testName, requestElement, 
                                      HttpStatus.CREATED);
    }
    
    protected abstract T manageElementToCreate(T element);
    
    protected void executeTestCreateElement(String testName, T requestElement,
                                            HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        String testMsg = this.makeTestMsg(testName);
        
        helper.executeTest(testMsg, this.baseRelativeUrl, HttpMethod.POST, 
                           Void.class, jsonRequest, expectedStatus);
    }
    
    
    //READ COLLECTION
    public void testReadCollection(String testName, 
                                   Class<RC> responseClass) throws Exception {
        
        Collection<T> expectedElements = manager.createElements();
        this.executeTestReadCollection(testName, responseClass, 
                                       expectedElements);
    }
    
    protected void executeTestReadCollection(String testName, 
                                             Class<RC> responseClass,
                                             Collection<T> expectedElements) throws Exception {
        
        List<T> expectedElementsList = (List<T>) expectedElements;
        
        RestTemplateJsonServiceTestHelper<RC> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        String testMsg = this.makeTestMsg(testName);
        
        RC response = helper.executeTest(testMsg, this.baseRelativeUrl,
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
    public void testReadElementById(String testName, Class<RT> responseClass) throws Exception {
        T expectedElement = manager.createElement();
        this.executeTestReadElementById(testName, responseClass,
                                        expectedElement, HttpStatus.OK);
        
        this.manageNegativeElementToReadById(expectedElement);
        this.executeTestReadElementById(testName, responseClass,
                                        expectedElement, HttpStatus.NOT_FOUND);
    }
    
    protected abstract T manageNegativeElementToReadById(T element);
    
    protected void executeTestReadElementById(String testName,
                                              Class<RT> responseClass,
                                              T expectedElement,
                                              HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<RT> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        String testMsg = this.makeTestMsg(testName);
        
        RT response = helper.executeTest(testMsg,
                                         this.makeSingleRelativeUrl(expectedElement),
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
    public void testUpdateElement(String testName) throws Exception {
        T requestElement = manager.createElement();
        this.managePositiveElementToUpdate(requestElement);
        this.executeTestUpdateElement(testName, requestElement, 
                                      HttpStatus.NO_CONTENT);
        
        this.manageNegativeElementToUpdate(requestElement);
        this.executeTestUpdateElement(testName, requestElement, 
                                      HttpStatus.NOT_FOUND);
    }
    
    protected abstract T managePositiveElementToUpdate(T element);
    protected abstract T manageNegativeElementToUpdate(T element);
    
    protected void executeTestUpdateElement(String testName, T requestElement,
                                            HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        String testMsg = this.makeTestMsg(testName);
        
        String jsonRequest = mapper.writeValueAsString(requestElement);
        System.out.println(jsonRequest);
        
        helper.executeTest(testMsg,
                           this.makeSingleRelativeUrl(requestElement),
                           HttpMethod.PUT,
                           Void.class, 
                           jsonRequest,
                           expectedStatus);
    }
    
    
    //DELETE
    public void testDeleteElement(String testName) throws Exception {
        T requestElement = manager.createElement();
        this.executeTestDeleteElement(testName, requestElement, 
                                      HttpStatus.NO_CONTENT);
        
        this.manageNegativeElementToDelete(requestElement);
        this.executeTestDeleteElement(testName, requestElement, 
                                      HttpStatus.NOT_FOUND);
    }
    
    protected abstract T manageNegativeElementToDelete(T element);
    
    protected void executeTestDeleteElement(String testName, T requestElement,
                                            HttpStatus expectedStatus) throws Exception {
        
        RestTemplateJsonServiceTestHelper<Void> helper = new RestTemplateJsonServiceTestHelper<>(true);
        
        String testMsg = this.makeTestMsg(testName);
        
        helper.executeTest(testMsg,
                           this.makeSingleRelativeUrl(requestElement),
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
    
    protected String makeSingleRelativeUrl(T expectedElement) {
        StringBuilder sb = new StringBuilder(this.baseRelativeUrl);
        sb.append("/").append(this.makeSinglePartRelativeUrl(expectedElement));
        return sb.toString();
    }
}