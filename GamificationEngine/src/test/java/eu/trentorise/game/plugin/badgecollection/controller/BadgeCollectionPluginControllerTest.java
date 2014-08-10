package eu.trentorise.game.plugin.badgecollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.request.BadgeRequest;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionPluginCollectionResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeCollectionPluginResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeListResponse;
import eu.trentorise.game.plugin.badgecollection.service.MockBadgeCollectionPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionPluginControllerTest extends AbstractRestCrudTest<BadgeCollectionPlugin, 
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
        super.testCreateElement("testCreateBadgeCollectionPlugin");
    }
    
    @Override
    protected BadgeCollectionPlugin manageElementToCreate(BadgeCollectionPlugin element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadBadgeCollectionPlugins", 
                                 BadgeCollectionPluginCollectionResponse.class);
    }
    
    @Override
    protected List<BadgeCollectionPlugin> retrieveCollection(BadgeCollectionPluginCollectionResponse response) {
        return (List<BadgeCollectionPlugin>) response.getBadgeCollectionPlugins();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadBadgeCollectionPluginById", 
                                  BadgeCollectionPluginResponse.class);
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
        super.testUpdateElement("testUpdateBadgeCollectionPlugin");
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
        super.testDeleteElement("testDeleteBadgeCollectionPlugin");
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
    
    
    /**
     * Test of getBadges method, of class BadgeCollectionPluginController.
     * @throws java.lang.Exception
     */
    @Test
    public void testGetBadges() throws Exception {
        MockPluginManager mock = new MockPluginManager();
        MockBadgeCollectionPluginManager badgeMock = new MockBadgeCollectionPluginManager();
        badgeMock.setManager(mock);
        
        BadgeCollectionPlugin plugin = mock.createUsageBadgesPlugin();
        List<Badge> expectedBadges = badgeMock.createUsageBadgesList();
        this.executeTest(plugin, expectedBadges);
        
        plugin = mock.createHealthBadgesPlugin();
        expectedBadges = badgeMock.createHealthBadgesList();
        this.executeTest(plugin, expectedBadges);
        
        plugin = mock.createEcologicalBadgesPlugin();
        expectedBadges = badgeMock.createEcologicalBadgesList();
        this.executeTest(plugin, expectedBadges);
    }
    
    protected void executeTest(BadgeCollectionPlugin plugin,
                               List<Badge> expectedBadges) throws Exception {
        
        RestTemplateJsonServiceTestHelper<BadgeListResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        ObjectMapper mapper = new ObjectMapper();
        
        BadgeRequest request = new BadgeRequest();
        request.setBadgeCollection(plugin);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        BadgeListResponse response = helper.executeTest("testGetBadges",
                                                        this.baseRelativeUrl + "/badge/getBadges" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION,
                                                        HttpMethod.POST,
                                                        BadgeListResponse.class, 
                                                        jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            List<Badge> responseBadges = response.getBadges();
            
            assertNotNull(responseBadges);
            assertEquals(responseBadges.size(), expectedBadges.size());
            
            for (int i = 0; i < responseBadges.size(); i++) {
                Badge responseBadge = responseBadges.get(i);
                Badge expectedBadge = expectedBadges.get(i);
                
                assertEquals(responseBadge.getId(), expectedBadge.getId());
                assertEquals(responseBadge.getBadgeCollection().getId(), 
                             expectedBadge.getBadgeCollection().getId());
                assertEquals(responseBadge.getBadgeCollection().getPlugin().getId(), 
                             expectedBadge.getBadgeCollection().getPlugin().getId());
            }
        }
    }
}