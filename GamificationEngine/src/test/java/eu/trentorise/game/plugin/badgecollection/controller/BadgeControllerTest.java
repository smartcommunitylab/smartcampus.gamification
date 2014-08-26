package eu.trentorise.game.plugin.badgecollection.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeResponse;
import eu.trentorise.game.plugin.badgecollection.service.MockBadgeManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class BadgeControllerTest extends AbstractRestCrudTest<Badge, 
                                                              BadgeCollectionPlugin,
                                                              Badge,
                                                              BadgeCollectionResponse,
                                                              BadgeResponse> {
    
    protected static final MockBadgeManager mockBadgeManager = MockBadgeManager.createInstance();
    protected static final MockPluginManager mockPluginManager = MockPluginManager.createInstance();
    
    public BadgeControllerTest() {
        super("BadgeControllerTest", 
              IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH,
              mockBadgeManager,
              mockBadgeManager.getComparator());
    }
    
    
    //CREATE
    @Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateBadge", null, 
                                makeBaseRelativeUrlExpanded(null));
    }
    
    @Override
    protected Badge manageElementToCreate(Badge element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadBadges", 
                                 mockPluginManager.createUsageBadgesPlugin(), 
                                 BadgeCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(IGameConstants.SEQUENCE_INITIAL_VALUE));
        super.testReadCollection("testReadBadges", 
                                 mockPluginManager.createHealthBadgesPlugin(), 
                                 BadgeCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(IGameConstants.SEQUENCE_INITIAL_VALUE + 1));
        super.testReadCollection("testReadBadges", 
                                 mockPluginManager.createEcologicalBadgesPlugin(), 
                                 BadgeCollectionResponse.class,
                                 makeBaseRelativeUrlExpanded(IGameConstants.SEQUENCE_INITIAL_VALUE + 2));
    }
    
    @Override
    protected List<Badge> retrieveCollection(BadgeCollectionResponse response) {
        return (List<Badge>) response.getBadges();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadBadgeById", null, 
                                  BadgeResponse.class,
                                  makeBaseRelativeUrlExpanded(IGameConstants.SEQUENCE_INITIAL_VALUE));
    }

    @Override
    protected Badge manageNegativeElementToReadById(Badge element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected Badge retrieveSingleElement(BadgeResponse response) {
        return response.getBadge();
    }
    
    
    //UPDATE
    @Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateBadge", null,
                                makeBaseRelativeUrlExpanded(IGameConstants.SEQUENCE_INITIAL_VALUE));
    }

    @Override
    protected Badge managePositiveElementToUpdate(Badge element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected Badge manageNegativeElementToUpdate(Badge element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteBadge", null,
                                makeBaseRelativeUrlExpanded(IGameConstants.SEQUENCE_INITIAL_VALUE));
    }
    
    @Override
    protected Badge manageNegativeElementToDelete(Badge element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(Badge element) {
        return element.getId().toString();
    }

    
    protected Badge setNegativeId(Badge element) {
        element.setId(-1);
        
        return element;
    }
    
    protected String makeBaseRelativeUrlExpanded(Integer cusPlugId) {
        if (null == cusPlugId) {
            cusPlugId = 0;
        }
        
        Map<String, Object> uriVariables = new HashMap<>();
        
        uriVariables.put(IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_SINGLE_PATH_PARAM, cusPlugId);
        
        return super.expandUrl(this.baseRelativeUrl, uriVariables);
    }
    
    
    /*<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
      <html xmlns="http://www.w3.org/1999/xhtml">
        <head>

        </head>
            <body>
                <div class="container">
                        <form action="http://localhost:8080/gamificationengine/game/services/plugins/badgeCollection/badge/setBadge.service" method="post" enctype="multipart/form-data">
                                <input type="text" name="badgeCollectionId" value="2" size="50" />
                                <input type="text" name="badgeCollectionPluginId" value="1" size="50" />		    
                                <p>Select a file : <input type="file" name="badgeCollectionBadgeFile" size="50" /></p>

                            <input type="submit" value="Upload" />
                        </form>
                </div>
            </body>
        </head>
    */
    /*public void testSetBadge() throws Exception {
        HttpMultipartPostCallerServiceTestHelper helper = new HttpMultipartPostCallerServiceTestHelper(true);
        ObjectMapper mapper = new ObjectMapper();
        MockPluginManager mock = new MockPluginManager();
        MockBadgeCollectionPluginManager badgeMock = new MockBadgeCollectionPluginManager();
        badgeMock.setManager(mock);
        
        Badge expectedBadge = badgeMock.createGreenHeroNovice();
        
        List<BasicNameValuePair> nameValuePairs = new ArrayList<>();

        nameValuePairs.add(new BasicNameValuePair(IGameConstants.PARAM_BADGECOLLECTION_ID, 
                                                  expectedBadge.getBadgeCollection().getId() + ""));
        nameValuePairs.add(new BasicNameValuePair(IGameConstants.PARAM_BADGECOLLECTION_PLUGIN_ID, 
                                                  expectedBadge.getBadgeCollection().getPlugin().getId() + ""));
        
        String stringResponse = helper.executeTest("testSetBadge", 
                                                   BASE_RELATIVE_URL + "/setBadge" + FINAL_PART_RELATIVE_URL,
                                                   nameValuePairs,
                                                   null);
        
        if (null != stringResponse) {
            System.out.println(stringResponse);
            BadgeResponse response = mapper.readValue(stringResponse, 
                                                      BadgeResponse.class);
            
            assertNotNull(response);
            assertTrue(response.isSuccess());
            
            Badge badgeResponse = response.getBadge();
            assertNotNull(badgeResponse);
            assertEquals(badgeResponse.getId(), expectedBadge.getId());
            assertEquals(badgeResponse.getBadgeCollection().getId(), expectedBadge.getBadgeCollection().getId());
            assertEquals(badgeResponse.getBadgeCollection().getPlugin().getId(), expectedBadge.getBadgeCollection().getPlugin().getId());
        }
    }*/
}