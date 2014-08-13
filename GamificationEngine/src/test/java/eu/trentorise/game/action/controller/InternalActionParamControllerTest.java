package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.model.Action;
import eu.trentorise.game.action.model.InternalAction;
import eu.trentorise.game.action.model.Param;
import eu.trentorise.game.action.response.ParamCollectionResponse;
import eu.trentorise.game.action.response.ParamResponse;
import eu.trentorise.game.action.service.MockInternalActionParamManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import eu.trentorise.utils.web.IUrlMaker;
import eu.trentorise.utils.web.UrlMaker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class InternalActionParamControllerTest extends AbstractRestCrudTest<Param, 
                                                                            InternalAction, 
                                                                            Param,
                                                                            ParamCollectionResponse,
                                                                            ParamResponse> {
    
    protected static final MockInternalActionParamManager mockInternalActionParamManager = MockInternalActionParamManager.createInstance();
    
    
    public InternalActionParamControllerTest() {
        super("InternalActionParamControllerTest", 
              IGameConstants.SERVICE_INTERNALACTIONS_PARAMS_PATH,
              mockInternalActionParamManager,
              mockInternalActionParamManager.getComparator());
    }
    
    
    //CREATE
    /*@Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateParam", null, 
                                makeBaseRelativeUrlExpanded(mockApplicationManager.createViaggiaRovereto()));
    }*/
    
    
    @Override
    protected Param manageElementToCreate(Param element) {
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        Param element = mockInternalActionParamManager.createElement(null);
        InternalAction action = (InternalAction) element.getAction();
        
        super.testReadCollection("testReadParams", 
                                 null, 
                                 ParamCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(action));
    }
    
    @Override
    protected List<Param> retrieveCollection(ParamCollectionResponse response) {
        return (List<Param>) response.getParams();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        Param element = mockInternalActionParamManager.createElement(null);
        Action action = element.getAction();
        
        super.testReadElementById("testReadParamById", null, 
                                  ParamResponse.class,
                                  makeBaseRelativeUrlExpanded((InternalAction) action));
    }

    @Override
    protected Param manageNegativeElementToReadById(Param element) {
        return this.setNegativeName(element);
    }
    
    @Override
    protected Param retrieveSingleElement(ParamResponse response) {
        return response.getParam();
    }
    
    
    //UPDATE
    /*@Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateParam", null,
                                makeBaseRelativeUrlExpanded(mockApplicationManager.createViaggiaRovereto()));
    }*/

    @Override
    protected Param managePositiveElementToUpdate(Param element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected Param manageNegativeElementToUpdate(Param element) {
        return this.setNegativeName(element);
    }
    
    
    //DELETE
    /*@Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteParam", null,
                                makeBaseRelativeUrlExpanded(mockApplicationManager.createViaggiaRovereto()));
    }*/
    
    @Override
    protected Param manageNegativeElementToDelete(Param element) {
        return this.setNegativeName(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(Param element) {
        return element.getName();
    }

    
    protected Param setNegativeName(Param element) {
        element.setName(element.getName() + "WrongName");
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(InternalAction element) {
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_INTERNALACTIONS_SINGLE_PATH_PARAM, element.getId());
        
        IUrlMaker urlMaker = new UrlMaker();
        return urlMaker.makeUrl(this.baseRelativeUrl, uriVariables);
    }
}