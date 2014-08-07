package eu.trentorise.game.plugin.badgecollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.request.BadgeCollectionPluginRequest;
import eu.trentorise.game.plugin.badgecollection.request.BadgeRequest;
import eu.trentorise.game.plugin.badgecollection.response.BadgeListResponse;
import eu.trentorise.game.plugin.badgecollection.service.MockBadgeCollectionPluginManager;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.response.SettingCustomizedPluginResponse;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
public class BadgeCollectionPluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;

    /**
     * Test of setCustomizedGamificationPlugin method, of class BadgeCollectionPluginController.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetCustomizedGamificationPlugin() throws Exception {
        RestTemplateJsonServiceTestHelper<SettingCustomizedPluginResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        MockPluginManager mock = new MockPluginManager();
        ObjectMapper mapper = new ObjectMapper();
        
        Game game = new Game();
        game.setId(MockGameProfileManager.MOCK_GAME_ID);
        
        BadgeCollectionPluginRequest request = new BadgeCollectionPluginRequest();
        request.setGame(game);
        BadgeCollectionPlugin plugin = mock.createEcologicalBadgesPlugin();
        plugin.setId(null);
        request.setPlugin(plugin);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        SettingCustomizedPluginResponse response = helper.executeTest("testBadgeCollectionSetCustomizedGamificationPlugin", 
                                                                           BASE_RELATIVE_URL + "/setCustomizedGamificationPlugin" + FINAL_PART_RELATIVE_URL,
                                                                           HttpMethod.POST,
                                                                           SettingCustomizedPluginResponse.class, 
                                                                           jsonRequest);
        
        if (null != response) {
            assertTrue(response.isSuccess());
            
            CustomizedPlugin customizedPlugin = response.getCustomizedPlugin();
            
            assertNotNull(customizedPlugin.getId());
            assertEquals(customizedPlugin.getPlugin().getId(), plugin.getPlugin().getId());
            assertEquals(customizedPlugin.getName(), plugin.getName());
            assertEquals(customizedPlugin.getVersion(), plugin.getVersion());
            assertEquals(customizedPlugin.getDescription(), plugin.getDescription());
        }
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
                                                        BASE_RELATIVE_URL + "/getBadges" + FINAL_PART_RELATIVE_URL,
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