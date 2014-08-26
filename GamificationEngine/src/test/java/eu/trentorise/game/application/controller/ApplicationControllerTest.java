package eu.trentorise.game.application.controller;

import eu.trentorise.game.action.model.Application;
import eu.trentorise.game.application.response.ApplicationCollectionResponse;
import eu.trentorise.game.application.response.ApplicationResponse;
import eu.trentorise.game.application.service.MockApplicationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.List;
import org.junit.Test;


/**
 *
 * @author Luca Piras
 */
public class ApplicationControllerTest extends AbstractRestCrudTest<Application, 
                                                                    Object,
                                                                    Application,
                                                                    ApplicationCollectionResponse,
                                                                    ApplicationResponse> {
    
    protected static final MockApplicationManager mockApplicationManager = MockApplicationManager.createInstance();
    
    
    public ApplicationControllerTest() {
        super("ApplicationControllerTest", 
              IGameConstants.SERVICE_APPLICATIONS_PATH,
              mockApplicationManager,
              mockApplicationManager.getComparator());
    }
    
    
    @Test
    public void testApplication() throws Exception {
        super.testCreateElement("testCreateApplication", null, null);
        
        super.testReadCollection("testReadApplications", null, 
                                 ApplicationCollectionResponse.class, null);
        
        super.testReadElementById("testReadApplicationById", null, 
                                  ApplicationResponse.class, null);
        
        super.testUpdateElement("testUpdateApplication", null, null);
        
        //super.testDeleteElement("testDeleteApplication", null, null);
    }
    
    //CREATE
    /*@Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateApplication", null, null);
    }*/
    
    @Override
    protected Application manageElementToCreate(Application element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    /*@Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadApplications", null, 
                                 ApplicationCollectionResponse.class, null);
    }*/
    
    @Override
    protected List<Application> retrieveCollection(ApplicationCollectionResponse response) {
        return (List<Application>) response.getApplications();
    }
    
    
    //READ SINGLE ELEMENT
    /*@Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadApplicationById", null, 
                                  ApplicationResponse.class, null);
    }*/

    @Override
    protected Application manageNegativeElementToReadById(Application element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected Application retrieveSingleElement(ApplicationResponse response) {
        return response.getApplication();
    }
    
    
    //UPDATE
    /*@Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateApplication", null, null);
    }*/

    @Override
    protected Application managePositiveElementToUpdate(Application element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected Application manageNegativeElementToUpdate(Application element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    /*@Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteApplication", null, null);
    }*/
    
    @Override
    protected Application manageNegativeElementToDelete(Application element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(Application element) {
        return element.getId().toString();
    }

    
    protected Application setNegativeId(Application element) {
        element.setId(-1);
        
        return element;
    }
}