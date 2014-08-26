package eu.trentorise.game.action.service;

import eu.trentorise.game.action.comparator.GameInternalActionKeyComparator;
import eu.trentorise.game.action.comparator.InternalActionKeyComparator;
import eu.trentorise.game.action.model.GameInternalAction;
import eu.trentorise.game.action.model.InternalAction;
import eu.trentorise.game.plugin.point.model.PointPlugin;
import eu.trentorise.game.plugin.service.MockGameCustomizedPluginManager;
import eu.trentorise.game.plugin.service.MockPluginManager;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.profile.game.service.MockGameProfileManager;
import eu.trentorise.game.ruleengine.model.RuleTemplate;
import eu.trentorise.game.ruleengine.service.MockRuleTemplateManager;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.IRestCrudTestManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author Luca Piras
 */
@Service("mockGameInternalActionManager")
public class MockGameInternalActionManager implements IRestCrudManager<GameInternalAction, GameInternalAction, GameInternalAction>,
                                                      IRestCrudTestManager<GameInternalAction, GameInternalAction, GameInternalAction> {

    public static MockGameInternalActionManager createInstance() {
        MockGameInternalActionManager mock = new MockGameInternalActionManager();
        mock.mockActionManager = MockActionManager.createInstance();
        mock.mockGameProfileManager = MockGameProfileManager.createInstance();
        mock.mockPluginManager = MockPluginManager.createInstance();
        mock.mockRuleTemplateManager = MockRuleTemplateManager.createInstance();
        mock.mockGameCustomizedPluginManager = MockGameCustomizedPluginManager.createInstance();
        
        mock.internalActionKeyComparator = new InternalActionKeyComparator();
        ((InternalActionKeyComparator) mock.internalActionKeyComparator).setActionKeyComparator(mock.mockActionManager.getComparator());
        //useful for InternalActionKeyFkComparator
        //((InternalActionKeyFkComparator) mock.internalActionKeyComparator).setCustomizedPluginKeyComparator(mock.mockGameCustomizedPluginManager.getCustomizedPluginComparator());
        //((InternalActionKeyFkComparator) mock.internalActionKeyComparator).setRuleTemplateKeyComparator(mock.mockRuleTemplateManager.getComparator());
        
        mock.comparator = new GameInternalActionKeyComparator();
        ((GameInternalActionKeyComparator) mock.comparator).setGameKeyComparator(mock.mockGameProfileManager.getComparator());
        ((GameInternalActionKeyComparator) mock.comparator).setInternalActionKeyComparator(mock.internalActionKeyComparator);
        
        return mock;
    }
    
    
    @Override
    public GameInternalAction createSingleElement(GameInternalAction containerWithForeignIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public Collection<GameInternalAction> readCollection(GameInternalAction containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        //TODO: prendi il gameId e vai nella tabella GameRule, recupera tutte le
        //regole attivate del game e prendi i ruleTemplateId. Usa i 
        //ruleTemplateIds con la tabella InternalAction per recuperare tutte le
        //InternalActions dei RuleTemplateIds ottenuti e fare join con la 
        //tabella CustomizedPluginInternalAction, così facendo si hanno tutte le
        //InternalActions attivate per i CustomizedPlugins del gameId indicato.
        //Oppure realizzare una vista od una tabella apposita.
        return this.createElements(containerWithIds);
    }

    @Override
    public GameInternalAction readSingleElement(GameInternalAction containerWithIds) throws Exception {
        //TODO: return null or throw Exception if this activity it is not 
        //possible
        GameInternalAction returnValue = null;
        
        GameInternalAction expectedElement = this.createElement(containerWithIds);
        if (0 == comparator.compare(containerWithIds, expectedElement)) {
            returnValue = expectedElement;
        }
        
        return returnValue;
    }

    @Override
    public GameInternalAction updateSingleElement(GameInternalAction containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public GameInternalAction deleteSingleElement(GameInternalAction containerWithIds) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    @Override
    public GameInternalAction createElement(GameInternalAction containerWithIds) throws Exception {
        return this.createGameGreenLeavesUpdatingInternalAction();
    }
    
    @Override
    public Collection createElements(GameInternalAction containerWithIds) throws Exception {
        return this.createRoveretoGameInternalActions();
    }

    
    public GameInternalAction createGameGreenLeavesUpdatingInternalAction() throws Exception {
        Game game = mockGameProfileManager.createElement(null);
        
        PointPlugin customizedPlugin = mockPluginManager.createGreenLeavesPointPlugin();
        RuleTemplate ruleTemplate = mockRuleTemplateManager.createElement(null);
        InternalAction internalAction = mockActionManager.createInternalAction(customizedPlugin,
                                                                               ruleTemplate, 
                                                                               1,
                                                                               "GreenLeavesUpdating",
                                                                               "GreenLeavesUpdating description");
        
        GameInternalAction element = this.createGameInternalActions(game, internalAction);
        
        return element;
    }
    
    public List<GameInternalAction> createRoveretoGameInternalActions() throws Exception {
        List<GameInternalAction> list = new ArrayList<>();
        
        GameInternalAction element = this.createGameGreenLeavesUpdatingInternalAction();
        list.add(element);
        
        //////////////////
        Game game = element.getGame();
        PointPlugin customizedPlugin = mockPluginManager.createHeartsPointPlugin();
        RuleTemplate ruleTemplate = element.getInternalAction().getRuleTemplate();
        InternalAction internalAction = mockActionManager.createInternalAction(customizedPlugin,
                                                                ruleTemplate, 
                                                                2,
                                                                "HeartsPointsUpdating",
                                                                "HeartsPointsUpdating description");
        
        element = this.createGameInternalActions(game, internalAction);
        list.add(element);
        
        //////////////////
        customizedPlugin = mockPluginManager.createUsagePointsPointPlugin();
        internalAction = mockActionManager.createInternalAction(customizedPlugin,
                                                                ruleTemplate, 
                                                                3,
                                                                "UsagePointsUpdating",
                                                                "UsagePointsUpdating description");
        
        element = this.createGameInternalActions(game, internalAction);
        list.add(element);
        
        return list;
    }
    
    protected GameInternalAction createGameInternalActions(Game game, InternalAction internalAction) {
        GameInternalAction element = new GameInternalAction();
        element.setGame(game);
        element.setInternalAction(internalAction);
        return element;
    }
    
    
    public Comparator<GameInternalAction> getComparator() {
        return comparator;
    }

    public Comparator<InternalAction> getInternalActionKeyFkComparator() {
        return internalActionKeyComparator;
    }
    
    
    @Qualifier("mockActionManager")
    @Autowired
    protected MockActionManager mockActionManager;
    
    @Qualifier("mockGameProfileManager")
    @Autowired
    protected MockGameProfileManager mockGameProfileManager;
    
    @Qualifier("mockPluginManager")
    @Autowired
    protected MockPluginManager mockPluginManager;
    
    @Qualifier("mockRuleTemplateManager")
    @Autowired
    protected MockRuleTemplateManager mockRuleTemplateManager;
    
    @Qualifier("mockGameCustomizedPluginManager")
    @Autowired
    protected MockGameCustomizedPluginManager mockGameCustomizedPluginManager;
    
    
    @Qualifier("gameInternalActionKeyComparator")
    @Autowired
    protected Comparator<GameInternalAction> comparator;
    
    @Qualifier("internalActionKeyComparator")
    @Autowired
    protected Comparator<InternalAction> internalActionKeyComparator;
}