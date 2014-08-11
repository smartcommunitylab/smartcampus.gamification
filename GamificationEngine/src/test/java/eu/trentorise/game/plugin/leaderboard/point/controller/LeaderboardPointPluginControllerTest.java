package eu.trentorise.game.plugin.leaderboard.point.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.leaderboard.point.model.LeaderboardPointPlugin;
import eu.trentorise.game.plugin.leaderboard.point.response.LeaderboardPointPluginCollectionResponse;
import eu.trentorise.game.plugin.leaderboard.point.response.LeaderboardPointPluginResponse;
import eu.trentorise.game.plugin.leaderboard.point.service.MockLeaderboardPointPluginManager;
import eu.trentorise.game.servicetest.AbstractRestCrudTest;
import java.util.List;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class LeaderboardPointPluginControllerTest extends AbstractRestCrudTest<LeaderboardPointPlugin,
                                                                               Object, 
                                                                               LeaderboardPointPlugin,
                                                                               LeaderboardPointPluginCollectionResponse,
                                                                               LeaderboardPointPluginResponse> {
    
    protected static final MockLeaderboardPointPluginManager mockLeaderboardPointPluginManager = MockLeaderboardPointPluginManager.createInstance();
    
    
    public LeaderboardPointPluginControllerTest() {
        super("LeaderboardPointPluginControllerTest", 
              IGameConstants.SERVICE_PLUGINS_LEADERBOARDPOINT_PATH,
              mockLeaderboardPointPluginManager,
              mockLeaderboardPointPluginManager.getComparator());
    }
    
    
    //CREATE
    @Test
    public void testCreateElement() throws Exception {
        super.testCreateElement("testCreateLeaderboardPointPlugin", null, null);
    }
    
    @Override
    protected LeaderboardPointPlugin manageElementToCreate(LeaderboardPointPlugin element) {
        element.setId(null);
        return element;
    }
    
    
    //READ COLLECTION
    @Test
    public void testReadCollection() throws Exception {
        super.testReadCollection("testReadLeaderboardPointPlugins", null, 
                                 LeaderboardPointPluginCollectionResponse.class,
                                 null);
    }
    
    @Override
    protected List<LeaderboardPointPlugin> retrieveCollection(LeaderboardPointPluginCollectionResponse response) {
        return (List<LeaderboardPointPlugin>) response.getLeaderboardPointPlugins();
    }
    
    
    //READ SINGLE ELEMENT
    @Test
    public void testReadElementById() throws Exception {
        super.testReadElementById("testReadLeaderboardPointPluginById", null,
                                  LeaderboardPointPluginResponse.class, null);
    }

    @Override
    protected LeaderboardPointPlugin manageNegativeElementToReadById(LeaderboardPointPlugin element) {
        return this.setNegativeId(element);
    }
    
    @Override
    protected LeaderboardPointPlugin retrieveSingleElement(LeaderboardPointPluginResponse response) {
        return response.getLeaderboardPointPlugin();
    }
    
    
    //UPDATE
    @Test
    public void testUpdateElement() throws Exception {
        super.testUpdateElement("testUpdateLeaderboardPointPlugin", null, null);
    }

    @Override
    protected LeaderboardPointPlugin managePositiveElementToUpdate(LeaderboardPointPlugin element) {
        element.setName(element.getName() + "Modified");
        
        return element;
    }

    @Override
    protected LeaderboardPointPlugin manageNegativeElementToUpdate(LeaderboardPointPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //DELETE
    @Test
    public void testDeleteElement() throws Exception {
        super.testDeleteElement("testDeleteLeaderboardPointPlugin", null, null);
    }
    
    @Override
    protected LeaderboardPointPlugin manageNegativeElementToDelete(LeaderboardPointPlugin element) {
        return this.setNegativeId(element);
    }
    
    
    //TOOLS
    @Override
    protected String makeSinglePartRelativeUrl(LeaderboardPointPlugin element) {
        return element.getId().toString();
    }

    
    protected LeaderboardPointPlugin setNegativeId(LeaderboardPointPlugin element) {
        element.setId(-1);
        
        return element;
    }
}