package eu.trentorise.game.plugin.leaderboard.point.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;
import eu.trentorise.game.plugin.leaderboard.point.request.LeaderboardPointPluginRequest;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.response.SettingCustomizedPluginResponse;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.servicetest.RestTemplateJsonServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import static org.junit.Assert.*;
import org.junit.Test;
import org.springframework.http.HttpMethod;

/**
 *
 * @author Luca Piras
 */
public class LeadearboardPointPluginControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;

    /**
     * Test of setCustomizedGamificationPlugin method, of class LeaderboardPointPluginController.
     * @throws java.lang.Exception
     */
    @Test
    public void testSetCustomizedGamificationPlugin() throws Exception {
        RestTemplateJsonServiceTestHelper<SettingCustomizedPluginResponse> helper = new RestTemplateJsonServiceTestHelper<>(true);
        MockPluginManager mock = new MockPluginManager();
        ObjectMapper mapper = new ObjectMapper();
        
        Game game = new Game();
        game.setId(MockGameProfileManager.MOCK_GAME_ID);
        
        LeaderboardPointPluginRequest request = new LeaderboardPointPluginRequest();
        request.setGame(game);
        LeaderboardPointPlugin plugin = mock.createGreenWeeklyLeadearboardPlugin();
        plugin.setId(null);
        request.setPlugin(plugin);
        
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        
        SettingCustomizedPluginResponse response = helper.executeTest("testLeaderboardPointSetCustomizedGamificationPlugin", 
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
}