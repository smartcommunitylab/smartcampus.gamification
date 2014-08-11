package eu.trentorise.game.plugin.badgecollection.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionPluginCollectionResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionPluginResponse;
import eu.trentorise.game.plugin.badgecollection.service.MockBadgeCollectionPluginManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionPluginControllerTest extends AbstractRestCrudTest<BadgeCollectionPlugin,
                                                                              Object, 
                                                                              BadgeCollectionPlugin,
                                                                              BadgeCollectionPluginCollectionResponse,
                                                                              BadgeCollectionPluginResponse> {
    
    protected static final MockBadgeCollectionPluginManager mockBadgeCollectionPluginManager = MockBadgeCollectionPluginManager.createInstance();
    
    public BadgeCollectionPluginControllerTest() {
        super("BadgeCollectionPluginControllerTest", 
              IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_PATH,
              mockBadgeCollectionPluginManager,
              mockBadgeCollectionPluginManager.getComparator());
    }
    
    
    //CREATE
    @Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateBadgeCollectionPlugin", null, null);
    }
    
    @Override
    protected BadgeCollectionPlugin manageElementToCreate(BadgeCollectionPlugin element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadBadgeCollectionPlugins", null, 
                                 BadgeCollectionPluginCollectionResponse.class,
                                 null);
    }
    
    @Override
    protected List<BadgeCollectionPlugin> retrieveCollection(BadgeCollectionPluginCollectionResponse response) {
        return (List<BadgeCollectionPlugin>) response.getBadgeCollectionPlugins();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadBadgeCollectionPluginById", null, 
                                  BadgeCollectionPluginResponse.class,
                                  null);
    }

    @Override
    protected BadgeCollectionPlugin manageNegativeElementToReadById(BadgeCollectionPlugin element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected BadgeCollectionPlugin retrieveSingleElement(BadgeCollectionPluginResponse response) {
        return response.getBadgeCollectionPlugin();
    }
    
    
    //UPDATE
    @Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateBadgeCollectionPlugin", null, null);
    }

    @Override
    protected BadgeCollectionPlugin managePositiveElementToUpdate(BadgeCollectionPlugin element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected BadgeCollectionPlugin manageNegativeElementToUpdate(BadgeCollectionPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteBadgeCollectionPlugin", null, null);
    }
    
    @Override
    protected BadgeCollectionPlugin manageNegativeElementToDelete(BadgeCollectionPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(BadgeCollectionPlugin element) {
        return element.getId().toString();
    }

    
    protected BadgeCollectionPlugin setNegativeId(BadgeCollectionPlugin element) {
        element.setId(-1);
        
        return element;
    }
}