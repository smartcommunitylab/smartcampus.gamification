package eu.trentorise.game.plugin.badgecollection.service;

import eu.trentorise.game.plugin.badgecollection.container.IBadgeContainer;
import eu.trentorise.game.plugin.badgecollection.container.IBadgeSettingContainer;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.badgecollection.response.BadgeListResponse;
import eu.trentorise.game.plugin.badgecollection.response.BadgeResponse;
import eu.trentorise.game.plugin.container.GameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.container.IGameCustomizedPluginCollectionContainer;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import eu.trentorise.game.plugin.model.Plugin;
import eu.trentorise.game.plugin.service.MockGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import eu.trentorise.utils.web.WebFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockBadgeCollectionPluginManager")
public class MockBadgeCollectionPluginManager extends MockResponder implements IBadgeCollectionPluginManager,
                                                                               IBadgeManager,
                                                                               IRestCrudManager<BadgeCollectionPlugin,
                                                                                                Object,
                                                                                                BadgeCollectionPlugin>,
                                                                               IRestCrudTestManager<BadgeCollectionPlugin> {

    public static final String BADGE_FILE_NAME_BRONZE = "bronze_badge.png";
    public static final String BADGE_FILE_NAME_SILVER = "silver_badge.png";
    public static final String BADGE_FILE_NAME_GOLD = "gold_badge.png";
    
    public static final String BADGE_FILE_NAME_GREEN_HERO_NOVICE = "green-hero-novice.png";
    public static final String BADGE_FILE_NAME_GREEN_HERO_1 = "green_hero_1.png";
    public static final String BADGE_FILE_NAME_GREEN_HERO_2 = "green_hero_2.png";
    
    
    public static MockBadgeCollectionPluginManager createInstance() {
        MockBadgeCollectionPluginManager mock = new MockBadgeCollectionPluginManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockGameCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        mock.comparator = mock.mockGameCustomizedPluginManager.getComparator();
        return mock;
    }
    
    @Override
    public BadgeCollectionPlugin createSingleElement(BadgeCollectionPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return this.createElement();
    }

    @Override
    public Collection<BadgeCollectionPlugin> readCollection(Object containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella BadgeCollectionPlugin e recupera tutti i
        //customizedPlugins per il plugin indicato
        return this.createElements();
    }

    @Override
    public BadgeCollectionPlugin readSingleElement(BadgeCollectionPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        BadgeCollectionPlugin returnValue = null;
        
        BadgeCollectionPlugin expectedElement = this.createElement();
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public BadgeCollectionPlugin updateSingleElement(BadgeCollectionPlugin containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        BadgeCollectionPlugin returnValue = null;
        
        BadgeCollectionPlugin expectedElement = this.createElement();
        if (0 == comparator.compare(containerWithForeignIds, expectedElement)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }

    @Override
    public BadgeCollectionPlugin deleteSingleElement(BadgeCollectionPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        BadgeCollectionPlugin returnValue = null;
        
        BadgeCollectionPlugin expectedElement = this.createElement();
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public BadgeListResponse getBadges(IBadgeContainer container) throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin collection = container.getBadgeCollection();
        if (0 == badgeCollectionPluginComparator.compare(collection, mockPluginManager.createUsageBadgesPlugin())) {
            list = this.createUsageBadgesList();
        } else if (0 == badgeCollectionPluginComparator.compare(collection, mockPluginManager.createHealthBadgesPlugin())) {
            list = this.createHealthBadgesList();
        } else if (0 == badgeCollectionPluginComparator.compare(collection, mockPluginManager.createEcologicalBadgesPlugin())) {
            list = this.createEcologicalBadgesList();
        }
        
        return this.makeCustomizedResponse(list);
    }
    
    
    @Override
    public BadgeCollectionPlugin createElement() {
        return mockPluginManager.createUsageBadgesPlugin();
    }

    @Override
    public Collection createElements() {
        Plugin plugin = mockPluginManager.createBadgeCollectionPlugin();
        GameCustomizedPlugin containerContent = mockGameCustomizedPluginManager.createContainerContent(MockGameProfileManager.MOCK_GAME_ID, plugin, null);
        
        IGameCustomizedPluginCollectionContainer container = new GameCustomizedPluginCollectionContainer();
        container.setGameCustomizedPlugin(containerContent);
        
        return mockPluginManager.createCustomizedPlugins(container);
    }
    
    public List<Badge> createUsageBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = mockPluginManager.createUsageBadgesPlugin();
        
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
        return this.createBadge(mockPluginManager.createEcologicalBadgesPlugin(), 0,
                                MockBadgeCollectionPluginManager.BADGE_FILE_NAME_GREEN_HERO_NOVICE);
    }
    
    public List<Badge> createHealthBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = mockPluginManager.createHealthBadgesPlugin();
        
        list.add(this.createBadge(badgeCollection, 0, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_BRONZE));
        list.add(this.createBadge(badgeCollection, 1, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_SILVER));
        list.add(this.createBadge(badgeCollection, 2, MockBadgeCollectionPluginManager.BADGE_FILE_NAME_GOLD));
        
        return list;
    }
    
    public List<Badge> createEcologicalBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = mockPluginManager.createEcologicalBadgesPlugin();
        
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
        image.setUrl(new URL(this.makeUrl(badgeCollection.getPlugin().getId(), 
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

    
    public Comparator<CustomizedPlugin> getComparator() {
        return comparator;
    }
    
    public void setManager(MockPluginManager manager) {
        this.mockPluginManager = manager;
    }
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    @Qualifier("mockGameCustomizedPluginManager")
    @Autowired
    protected MockGameCustomizedPluginManager mockGameCustomizedPluginManager;
    
    @Qualifier("customizedPluginKeyComparator")
    @Autowired
    protected Comparator<CustomizedPlugin> comparator;
    
    @Qualifier("badgeCollectionKeyComparator")
    @Autowired
    protected Comparator<BadgeCollectionPlugin> badgeCollectionPluginComparator;
}