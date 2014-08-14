package eu.trentorise.game.plugin.badgecollection.service;

import eu.trentorise.game.plugin.badgecollection.comparator.BadgeCollectionKeyComparator;
import eu.trentorise.game.plugin.badgecollection.comparator.BadgeKeyComparator;
import eu.trentorise.game.plugin.badgecollection.model.Badge;
import eu.trentorise.game.plugin.badgecollection.model.BadgeCollectionPlugin;
import eu.trentorise.game.plugin.model.CustomizedPlugin;
import eu.trentorise.game.plugin.service.MockGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
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


@Service("mockBadgeCollectionPluginBadgeManager")
public class MockBadgeManager implements IRestCrudManager<Badge, BadgeCollectionPlugin, Badge>,
                                         IRestCrudTestManager<Badge, BadgeCollectionPlugin, Badge> {

    public static final String BADGE_FILE_NAME_BRONZE = "bronze_badge.png";
    public static final String BADGE_FILE_NAME_SILVER = "silver_badge.png";
    public static final String BADGE_FILE_NAME_GOLD = "gold_badge.png";
    
    public static final String BADGE_FILE_NAME_GREEN_HERO_NOVICE = "green-hero-novice.png";
    public static final String BADGE_FILE_NAME_GREEN_HERO_1 = "green_hero_1.png";
    public static final String BADGE_FILE_NAME_GREEN_HERO_2 = "green_hero_2.png";
    
    
    public static MockBadgeManager createInstance() {
        MockBadgeManager mock = new MockBadgeManager();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockBadgeCollectionPluginManager = MockBadgeCollectionPluginManager.createInstance();
        mock.mockGameCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        
        mock.comparator = new BadgeKeyComparator();
        mock.badgeCollectionPluginComparator = new BadgeCollectionKeyComparator();
        Comparator<CustomizedPlugin> customizedPluginComparator = mock.mockBadgeCollectionPluginManager.getComparator();
        ((BadgeCollectionKeyComparator) mock.badgeCollectionPluginComparator).setCustomizedGamificationPluginComparator(customizedPluginComparator);
        ((BadgeKeyComparator) mock.comparator).setBadgeCollectionPluginComparator(mock.badgeCollectionPluginComparator);
        
        return mock;
    }
    
    @Override
    public Badge createSingleElement(Badge containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to create a
        //new one
        
        return this.createElement(containerWithForeignIds);
    }

    @Override
    public Collection<Badge> readCollection(BadgeCollectionPlugin containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: vai nella tabella Badge e recupera tutti i
        //customizedPlugins per il plugin indicato
        return this.createElements(containerWithIds);
    }

    @Override
    public Badge readSingleElement(Badge containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        Badge returnValue = null;
        
        Badge expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public Badge updateSingleElement(Badge containerWithForeignIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to update a
        //that one
        
        Badge returnValue = null;
        
        Badge expectedElement = this.createElement(containerWithForeignIds);
        if (0 == comparator.compare(containerWithForeignIds, expectedElement)) {
            returnValue = containerWithForeignIds;
        }
        
        return returnValue;
    }

    @Override
    public Badge deleteSingleElement(Badge containerWithIds) throws Exception {
        //TODO: return null or throw Exception if it is not possible to delete
        //that one or if it is not present
        
        Badge returnValue = null;
        
        Badge expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }
    
    
    @Override
    public Badge createElement(Badge containerWithIds) throws Exception {
        return this.createGreenHeroNovice();
    }
    
    @Override
    public Collection createElements(BadgeCollectionPlugin containerWithIds) throws Exception {
        List<Badge> list = new ArrayList<>();
        
        if (0 == badgeCollectionPluginComparator.compare(containerWithIds, mockPluginManager.createUsageBadgesPlugin())) {
            list = this.createUsageBadgesList();
        } else if (0 == badgeCollectionPluginComparator.compare(containerWithIds, mockPluginManager.createHealthBadgesPlugin())) {
            list = this.createHealthBadgesList();
        } else if (0 == badgeCollectionPluginComparator.compare(containerWithIds, mockPluginManager.createEcologicalBadgesPlugin())) {
            list = this.createEcologicalBadgesList();
        }
        
        return list;
    }
    
    public List<Badge> createUsageBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = mockPluginManager.createUsageBadgesPlugin();
        
        list.add(this.createBadge(badgeCollection, 0, BADGE_FILE_NAME_BRONZE));
        list.add(this.createBadge(badgeCollection, 1, BADGE_FILE_NAME_SILVER));
        list.add(this.createBadge(badgeCollection, 2, BADGE_FILE_NAME_GOLD));
        
        return list;
    }
    
    public Badge createGreenHeroNovice() throws Exception {
        return this.createBadge(mockPluginManager.createEcologicalBadgesPlugin(), 0,
                                BADGE_FILE_NAME_GREEN_HERO_NOVICE);
    }
    
    public Badge createGreenHeroOne() throws Exception {
        return this.createBadge(mockPluginManager.createEcologicalBadgesPlugin(), 1,
                                BADGE_FILE_NAME_GREEN_HERO_1);
    }
    
    public List<Badge> createHealthBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = mockPluginManager.createHealthBadgesPlugin();
        
        list.add(this.createBadge(badgeCollection, 0, BADGE_FILE_NAME_BRONZE));
        list.add(this.createBadge(badgeCollection, 1, BADGE_FILE_NAME_SILVER));
        list.add(this.createBadge(badgeCollection, 2, BADGE_FILE_NAME_GOLD));
        
        return list;
    }
    
    public List<Badge> createEcologicalBadgesList() throws Exception {
        List<Badge> list = new ArrayList<>();
        
        BadgeCollectionPlugin badgeCollection = mockPluginManager.createEcologicalBadgesPlugin();
        
        list.add(this.createGreenHeroNovice());
        list.add(this.createGreenHeroOne());
        list.add(this.createBadge(badgeCollection, 2, BADGE_FILE_NAME_GREEN_HERO_2));
        
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
        image.setFileContent("//TODO: implement this part");
        image.setUrl(new URL(this.makeUrl(badgeCollection.getPlugin().getId(), 
                                          badgeCollection.getId(), 
                                          badgeFileName)));

        badge.setImage(image);

        return badge;
    }

    
    public MockPluginManager getMockPluginManager() {
        return mockPluginManager;
    }
    
    public Comparator<Badge> getComparator() {
        return comparator;
    }
    
    public void setManager(MockPluginManager manager) {
        this.mockPluginManager = manager;
    }
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    @Qualifier("mockBadgeCollectionPluginManager")
    @Autowired
    protected MockBadgeCollectionPluginManager mockBadgeCollectionPluginManager;
    
    @Qualifier("mockGameCustomizedPluginManager")
    @Autowired
    protected MockGameCustomizedPluginManager mockGameCustomizedPluginManager;
    
    @Qualifier("badgeCollectionKeyComparator")
    @Autowired
    protected Comparator<BadgeCollectionPlugin> badgeCollectionPluginComparator;
    
    @Qualifier("BadgeKeyComparator")
    @Autowired
    protected Comparator<Badge> comparator;
}