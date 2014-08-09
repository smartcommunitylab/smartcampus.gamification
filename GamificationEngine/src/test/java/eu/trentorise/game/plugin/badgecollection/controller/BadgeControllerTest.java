package eu.trentorise.game.plugin.badgecollection.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.response.BadgeResponse;
import eu.trentorise.game.plugin.badgecollection.service.MockBadgeCollectionPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.servicetest.HttpMultipartPostCallerServiceTestHelper;
import eu.trentorise.game.servicetest.SkipServiceTestHelper;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author Luca Piras
 */
public class BadgeControllerTest extends SkipServiceTestHelper {
    
    protected final static String BASE_RELATIVE_URL = IGameConstants.SERVICE_PLUGINS_BADGECOLLECTION_BADGE_PATH;
    protected final static String FINAL_PART_RELATIVE_URL = IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION;
    
    
    public BadgeControllerTest() {
        super("BadgeControllerTest");
    }
    
    
    //TODO: nel seguente test viene testato tutto tranne l'upload effettivo del
    //file; da implementare anche quel test. Col codice html di seguito, al
    //momento è stato possibile testare anche questo aspetto:
    //
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
    /**
     * Test of setBadge method, of class BadgeController.
     */
    @Test
    public void testSetBadge() throws Exception {
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
    }
}