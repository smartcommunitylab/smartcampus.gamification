package eu.trentorise.game.config;

import eu.trentorise.game.ruleengine.data.IFactsDAO;
import eu.trentorise.game.ruleengine.data.IRulesDAO;
import eu.trentorise.game.ruleengine.data.IRulesStreamDAO;
import eu.trentorise.game.ruleengine.data.ISpreadSheetDAO;
import eu.trentorise.game.ruleengine.data.MockSpreadSheetDAO;
import eu.trentorise.game.ruleengine.data.drools.DroolsRulesStreamDAO;
import eu.trentorise.game.ruleengine.data.drools.DroolsTemplateRulesDAO;
import eu.trentorise.game.ruleengine.service.IRuleEngineManager;
import eu.trentorise.game.ruleengine.service.RuleEngineManager;
import eu.trentorise.game.ruleengine.service.drools.DroolsKnowledgeBuilder;
import eu.trentorise.game.ruleengine.service.preparer.IRulesPreparerManager;
import eu.trentorise.game.ruleengine.service.preparer.RulesPreparerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Luca Piras
 */
@Configuration
public class GameConfig {
    
    protected static final String DROOLS_RULES_PATH = "/conf/drools/rules/";
    protected static final String DROOLS_RULES_ROV_GAME_PATH = DROOLS_RULES_PATH + "rov_green_game/";
    
    ///////IRulesEngineManager///////
    @Bean(name="badgeRulesEngineManager")
    public IRuleEngineManager rulesEngineManager() {
        RuleEngineManager manager = new RuleEngineManager();
        
        manager.setKnowledgeBuilder(new DroolsKnowledgeBuilder());
        manager.setFactsDAO(factsDAO);
        manager.setRulesPreparerManager(badgeRulesPreparerManager());
        manager.setAddNewRulePreparerManager(badgeAddNewRulePreparerManager());
        
        return manager;
    }
    
    @Bean(name="pointRulesEngineManager")
    public IRuleEngineManager pointRulesEngineManager() {
        RuleEngineManager manager = new RuleEngineManager();
        
        manager.setKnowledgeBuilder(new DroolsKnowledgeBuilder());
        manager.setFactsDAO(pointFactsDAO);
        manager.setRulesPreparerManager(pointRulesPreparerManager());
        manager.setAddNewRulePreparerManager(null);
        
        return manager;
    }
    
    @Bean(name="rovGameRulesEngineManager")
    public IRuleEngineManager rovGameRulesEngineManager() {
        RuleEngineManager manager = new RuleEngineManager();
        
        manager.setKnowledgeBuilder(new DroolsKnowledgeBuilder());
        manager.setFactsDAO(rovGameFactsDAO);
        manager.setRulesPreparerManager(rovGameRulesPreparerManager());
        manager.setAddNewRulePreparerManager(null);
        
        return manager;
    }
    
    ///////IRulesPreparerManager///////
    @Bean(name="pointRulesPreparerManager")
    public IRulesPreparerManager pointRulesPreparerManager() {
        RulesPreparerManager manager = new RulesPreparerManager();
        manager.setDao(pointDroolsTemplateRulesDAO());
        
        return manager;
    }
    
    @Bean(name="badgeRulesPreparerManager")
    public IRulesPreparerManager badgeRulesPreparerManager() {
        RulesPreparerManager manager = new RulesPreparerManager();
        manager.setDao(badgeDroolsTemplateRulesDAO());
        
        return manager;
    }
    
    @Bean(name="badgeAddNewRulePreparerManager")
    public IRulesPreparerManager badgeAddNewRulePreparerManager() {
        RulesPreparerManager manager = new RulesPreparerManager();
        manager.setDao(badgeAddNewRuleDroolsTemplateRulesDAO());
        
        return manager;
    }
    
    @Bean(name="rovGameRulesPreparerManager")
    public IRulesPreparerManager rovGameRulesPreparerManager() {
        RulesPreparerManager manager = new RulesPreparerManager();
        manager.setDao(rovGameDroolsTemplateRulesDAO());
        
        return manager;
    }
    
    
    ///////IRulesDAO///////
    @Bean(name="pointDroolsTemplateRulesDAO")
    public IRulesDAO pointDroolsTemplateRulesDAO() {
        DroolsTemplateRulesDAO dao = new DroolsTemplateRulesDAO();
        dao.setSpreadSheetDAO(pointMockSpreadSheetDAO());
        dao.setRulesStreamDAO(pointDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="badgeDroolsTemplateRulesDAO")
    public IRulesDAO badgeDroolsTemplateRulesDAO() {
        DroolsTemplateRulesDAO dao = new DroolsTemplateRulesDAO();
        dao.setSpreadSheetDAO(badgeMockSpreadSheetDAO());
        dao.setRulesStreamDAO(badgeDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="badgeAddNewRuleDroolsTemplateRulesDAO")
    public IRulesDAO badgeAddNewRuleDroolsTemplateRulesDAO() {
        DroolsTemplateRulesDAO dao = new DroolsTemplateRulesDAO();
        dao.setSpreadSheetDAO(badgeMockAddNewRuleSpreadSheetDAO());
        dao.setRulesStreamDAO(badgeDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="rovGameDroolsTemplateRulesDAO")
    public IRulesDAO rovGameDroolsTemplateRulesDAO() {
        DroolsTemplateRulesDAO dao = new DroolsTemplateRulesDAO();
        dao.setSpreadSheetDAO(rovGameMockSpreadSheetDAO());
        dao.setRulesStreamDAO(rovGameDroolsRulesStreamDAO());
        
        return dao;
    }
    
    
    ///////ISpreadSheetDAO///////
    @Bean(name="pointMockSpreadSheetDAO")
    public ISpreadSheetDAO pointMockSpreadSheetDAO() {
        MockSpreadSheetDAO dao = new MockSpreadSheetDAO();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\"1\",\"1\",\"10\"\n");
        sb.append("\"2\",\"2\",\"100\"\n");
        
        dao.setContent(sb.toString());
        
        return dao;
    }
    
    @Bean(name="badgeMockSpreadSheetDAO")
    public ISpreadSheetDAO badgeMockSpreadSheetDAO() {
        MockSpreadSheetDAO dao = new MockSpreadSheetDAO();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\"1\",\"10\",\"Basic Mayor\"\n");
        sb.append("\"2\",\"100\",\"Enhanced Mayor\"\n");
        
        dao.setContent(sb.toString());
        
        return dao;
    }
    
    @Bean(name="badgeMockAddNewRuleSpreadSheetDAO")
    public ISpreadSheetDAO badgeMockAddNewRuleSpreadSheetDAO() {
        MockSpreadSheetDAO dao = new MockSpreadSheetDAO();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\"3\",\"1000\",\"Advanced Mayor\"\n");
        
        dao.setContent(sb.toString());
        
        return dao;
    }
    
    @Bean(name="rovGameMockSpreadSheetDAO")
    public ISpreadSheetDAO rovGameMockSpreadSheetDAO() {
        MockSpreadSheetDAO dao = new MockSpreadSheetDAO();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\"1\",\"1\",\"bikeKM\",\"1\",\"2\"\n");
        sb.append("\"2\",\"1\",\"busKM\",\"1\",\"1\"\n");
        
        dao.setContent(sb.toString());
        
        return dao;
    }
    
    
    ///////IRulesStreamDAO///////
    @Bean(name="pointDroolsRulesStreamDAO")
    public IRulesStreamDAO pointDroolsRulesStreamDAO() {
        DroolsRulesStreamDAO dao = new DroolsRulesStreamDAO();
        
        dao.setResourcePath(DROOLS_RULES_PATH + "point.drt");
        
        return dao;
    }
    
    @Bean(name="badgeDroolsRulesStreamDAO")
    public IRulesStreamDAO badgeDroolsRulesStreamDAO() {
        DroolsRulesStreamDAO dao = new DroolsRulesStreamDAO();
        
        dao.setResourcePath(DROOLS_RULES_PATH + "basicBadge.drt");
        
        return dao;
    }
    
    @Bean(name="rovGameDroolsRulesStreamDAO")
    public IRulesStreamDAO rovGameDroolsRulesStreamDAO() {
        DroolsRulesStreamDAO dao = new DroolsRulesStreamDAO();
        
        dao.setResourcePath(DROOLS_RULES_ROV_GAME_PATH + "point.drt");
        
        return dao;
    }
    
    
    @Qualifier("mockFactsDAO")
    @Autowired
    protected IFactsDAO factsDAO;
    @Qualifier("mockPointFactsDAO")
    @Autowired
    protected IFactsDAO pointFactsDAO;
    
    @Qualifier("rovGameFactsDAO")
    @Autowired
    protected IFactsDAO rovGameFactsDAO;
}