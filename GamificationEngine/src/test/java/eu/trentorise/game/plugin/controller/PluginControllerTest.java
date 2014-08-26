package eu.trentorise.game.plugin.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.response.PluginCollectionResponse;
import eu.trentorise.game.plugin.response.PluginResponse;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.List;
import org.junit.Test;


/**
 *
 * @author Luca Piras
 */
public class PluginControllerTest extends AbstractRestCrudTest<Plugin, 
                                                               Object,
                                                               Plugin,
                                                               PluginCollectionResponse,
                                                               PluginResponse> {
    
    protected static final MockPluginManager mockPluginManager = MockPluginManager.createInstance();
    
    
    public PluginControllerTest() {
        super("PluginControllerTest", 
              IGameConstants.SERVICE_PLUGINS_PATH,
              mockPluginManager,
              mockPluginManager.getComparator());
    }
    
    
    @Test
    public void testPlugin() throws Exception {
        super.testReadCollection("testReadPlugins", null, 
                                 PluginCollectionResponse.class, null);
        
        super.testReadElementById("testReadPluginById", null, 
                                  PluginResponse.class, null);
    }
    
    //CREATE
    /*@Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreatePlugin", null, null);
    }*/
    
    @Override
    protected Plugin manageElementToCreate(Plugin element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    /*@Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadPlugins", null, 
                                 PluginCollectionResponse.class, null);
    }*/
    
    @Override
    protected List<Plugin> retrieveCollection(PluginCollectionResponse response) {
        return (List<Plugin>) response.getPlugins();
    }
    
    
    //READ SINGLE ELEMENT
    /*@Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadPluginById", null, 
                                  PluginResponse.class, null);
    }*/

    @Override
    protected Plugin manageNegativeElementToReadById(Plugin element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected Plugin retrieveSingleElement(PluginResponse response) {
        return response.getPlugin();
    }
    
    
    //UPDATE
    /*@Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdatePlugin", null, null);
    }*/

    @Override
    protected Plugin managePositiveElementToUpdate(Plugin element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected Plugin manageNegativeElementToUpdate(Plugin element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    /*@Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeletePlugin", null, null);
    }*/
    
    @Override
    protected Plugin manageNegativeElementToDelete(Plugin element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(Plugin element) {
        return element.getId().toString();
    }

    
    protected Plugin setNegativeId(Plugin element) {
        element.setId(-1);
        
        return element;
    }
}