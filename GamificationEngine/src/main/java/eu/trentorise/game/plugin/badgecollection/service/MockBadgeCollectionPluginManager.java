package eu.trentorise.game.plugin.badgecollection.service;

import eu.trentorise.game.plugin.badgecollection.container.IBadgeContainer;
import eu.trentorise.game.plugin.badgecollection.container.IBadgeSettingContainer;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.response.BadgeListResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeResponse;
import eu.trentorise.game.plugin.container.CustomizedPluginContainer;
import eu.trentorise.game.plugin.model.CustomizedGamificationPlugin;
import eu.trentorise.game.plugin.response.CustomizedGamificationPluginResponse;
import eu.trentorise.game.plugin.service.IGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockGamePluginManager;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.utils.web.WebFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockBadgeCollectionPluginManager")
public class MockBadgeCollectionPluginManager extends MockResponder implements IGameCustomizedPluginManager<BadgeCollectionPlugin>,
                                                                               IBadgeCollectionPluginManager,
                                                                               IBadgeManager {

    public static final String BADGE_FILE_NAME_BRONZE = "bronze_badge.png";
    public static final String BADGE_FILE_NAME_SILVER = "silver_badge.png";
    public static final String BADGE_FILE_NAME_GOLD = "gold_badge.png";
    
    public static final String BADGE_FILE_NAME_GREEN_HERO_NOVICE = "green-hero-novice.png";
    public static final String BADGE_FILE_NAME_GREEN_HERO_1 = "green_hero_1.png";
    public static final String BADGE_FILE_NAME_GREEN_HERO_2 = "green_hero_2.png";
    
    @Override
    public CustomizedGamificationPluginResponse setCustomizedGamificationPlugin(CustomizedPluginContainer<BadgeCollectionPlugin> container) {
        //TODO: create new customizedGamificationPlugin, obtain an id, then
        //create a customizedPluginGame using the new 
        //customizedGamificationPlugin with its id and the provided game
        CustomizedGamificationPlugin plugin = manager.createEcologicalBadgesPlugin();
        
        CustomizedGamificationPluginResponse response = new CustomizedGamificationPluginResponse();
        response.setCustomizedGamificationPlugin(plugin);
        
        return ((CustomizedGamificationPluginResponse) this.buildPositiveResponse(response));
    }
    
    @Override
    public BadgeListResponse getBadges(IBadgeContainer container) throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin collection = container.getBadgeCollection();
        if (0 == comparator.compare(collection, manager.createUsageBadgesPlugin())) {
            list = this.createUsageBadgesList();
        } else if (0 == comparator.compare(collection, manager.createHealthBadgesPlugin())) {
            list = this.createHealthBadgesList();
        } else if (0 == comparator.compare(collection, manager.createEcologicalBadgesPlugin())) {
            list = this.createEcologicalBadgesList();
        }
        
        return this.makeCustomizedResponse(list);
    }
    
    public List<Badge> createUsageBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = manager.createUsageBadgesPlugin();
        
        list.add(this.createBadge(badgeCollection, 0, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_BRONZE));
        list.add(this.createBadge(badgeCollection, 1, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_SILVER));
        list.add(this.createBadge(badgeCollection, 2, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_GOLD));
        
        return list;
    }
    
    @Override
    public BadgeResponse setBadge(IBadgeSettingContainer container) throws Exception {
        return this.makeBadgeResponse(this.createGreenHeroNovice());
    }
    
    public Badge createGreenHeroNovice() throws Exception {
        return this.createBadge(manager.createEcologicalBadgesPlugin(), 0,
                                MockBadgeCollectionPluginManager.BADGE_FILE_NAME_GREEN_HERO_NOVICE);
    }
    
    public List<Badge> createHealthBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = manager.createHealthBadgesPlugin();
        
        list.add(this.createBadge(badgeCollection, 0, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_BRONZE));
        list.add(this.createBadge(badgeCollection, 1, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_SILVER));
        list.add(this.createBadge(badgeCollection, 2, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_GOLD));
        
        return list;
    }
    
    public List<Badge> createEcologicalBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = manager.createEcologicalBadgesPlugin();
        
        list.add(this.createGreenHeroNovice());
        list.add(this.createBadge(badgeCollection, 1, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_GREEN_HERO_1));
        list.add(this.createBadge(badgeCollection, 2, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_GREEN_HERO_2));
        
        return list;
    }
    
    protected String makeUrl(Integer gamificationPluginId, Integer badgeCollectionId, String badgeFileName) {
        StringBuilder sb = new StringBuilder("http://localhost:8080/gamificationengine/img/plugins/");
        sb.append(gamificationPluginId).append("/");
        sb.append(badgeCollectionId).append("/badges/");
        sb.append(badgeFileName);
        
        return sb.toString();
    }
    
    protected Badge createBadge(BadgeCollectionPlugin badgeCollection, 
                                Integer badgeId, String badgeFileName) throws MalformedURLException {
        
        Badge badge = new Badge();
        badge.setId(badgeId);
        badge.setBadgeCollection(badgeCollection);

        WebFile image = new WebFile();
        image.setFileName(badgeFileName);
        image.setUrl(new URL(this.makeUrl(badgeCollection.getGamificationPlugin().getId(), 
                                          badgeCollection.getId(), 
                                          badgeFileName)));

        badge.setImage(image);

        return badge;
    }
    
    protected BadgeListResponse makeCustomizedResponse(List<Badge> list) {
        BadgeListResponse response = new BadgeListResponse();
        response.setBadges(list);
        
        return ((BadgeListResponse) this.buildPositiveResponse(response));
    }
    
    protected BadgeResponse makeBadgeResponse(Badge badge) {
        BadgeResponse response = new BadgeResponse();
        response.setBadge(badge);
        
        return ((BadgeResponse) this.buildPositiveResponse(response));
    }
    
    public void setManager(MockGamePluginManager manager) {
        this.manager = manager;
    }
    
    @Qualifier("mockGamePluginManager")
    @Autowired
    protected MockGamePluginManager manager;
    
    @Qualifier("badgeCollectionKeyComparator")
    @Autowired
    protected Comparator<BadgeCollectionPlugin> comparator;
}