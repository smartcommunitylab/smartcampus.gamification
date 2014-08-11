package eu.trentorise.game.plugin.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.point.response.PointPluginCollectionResponse;
import eu.trentorise.game.plugin.point.response.PointPluginResponse;
import eu.trentorise.game.plugin.point.service.MockPointPluginManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class PointPluginControllerTest extends AbstractRestCrudTest<PointPlugin,
                                                                    Object, 
                                                                    PointPlugin,
                                                                    PointPluginCollectionResponse,
                                                                    PointPluginResponse> {
    
    protected static final MockPointPluginManager mockPointPluginManager = MockPointPluginManager.createInstance();
    
    
    public PointPluginControllerTest() {
        super("PointPluginControllerTest", 
              IGameConstants.SERVICE_PLUGINS_POINT_PATH,
              mockPointPluginManager,
              mockPointPluginManager.getComparator());
    }
    
    
    //CREATE
    @Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreatePointPlugin", null, null);
    }
    
    @Override
    protected PointPlugin manageElementToCreate(PointPlugin element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadPointPlugins", null, 
                                 PointPluginCollectionResponse.class, null);
    }
    
    @Override
    protected List<PointPlugin> retrieveCollection(PointPluginCollectionResponse response) {
        return (List<PointPlugin>) response.getPointPlugins();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadPointPluginById", null,
                                  PointPluginResponse.class, null);
    }

    @Override
    protected PointPlugin manageNegativeElementToReadById(PointPlugin element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected PointPlugin retrieveSingleElement(PointPluginResponse response) {
        return response.getPointPlugin();
    }
    
    
    //UPDATE
    @Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdatePointPlugin", null, null);
    }

    @Override
    protected PointPlugin managePositiveElementToUpdate(PointPlugin element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected PointPlugin manageNegativeElementToUpdate(PointPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeletePointPlugin", null, null);
    }
    
    @Override
    protected PointPlugin manageNegativeElementToDelete(PointPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(PointPlugin element) {
        return element.getId().toString();
    }

    
    protected PointPlugin setNegativeId(PointPlugin element) {
        element.setId(-1);
        
        return element;
    }
}