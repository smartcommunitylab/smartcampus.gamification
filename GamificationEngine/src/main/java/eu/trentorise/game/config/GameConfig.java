package eu.trentorise.game.config;

import eu.trentorise.game.ruleengine.data.IFactsDAO;
import eu.trentorise.game.ruleengine.data.IRulesDAO;
import eu.trentorise.game.ruleengine.data.IRulesStreamDAO;
import eu.trentorise.game.ruleengine.data.ISpreadSheetDAO;
import eu.trentorise.game.ruleengine.data.MockSpreadSheetDAO;
import eu.trentorise.game.ruleengine.data.drools.DroolsFileRulesDAO;
import eu.trentorise.game.ruleengine.data.drools.DroolsRuleTemplateDAO;
import eu.trentorise.game.ruleengine.data.drools.DroolsRulesStreamDAO;
import eu.trentorise.game.ruleengine.service.IRuleEngineManager;
import eu.trentorise.game.ruleengine.service.RuleEngineManager;
import eu.trentorise.game.ruleengine.service.drools.DroolsKnowledgeBuilder;
import eu.trentorise.game.ruleengine.service.preparer.IRulesPreparerManager;
import eu.trentorise.game.ruleengine.service.preparer.RulesPreparerManager;
import java.util.ArrayList;
import java.util.List;
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
        
        List<IRulesDAO> daos = new ArrayList<>();
        daos.add(pointDroolsRuleTemplateDAO());
        
        manager.setDaos(daos);
        
        return manager;
    }
    
    @Bean(name="badgeRulesPreparerManager")
    public IRulesPreparerManager badgeRulesPreparerManager() {
        RulesPreparerManager manager = new RulesPreparerManager();
        
        List<IRulesDAO> daos = new ArrayList<>();
        daos.add(badgeDroolsRuleTemplateDAO());
        
        manager.setDaos(daos);
        
        return manager;
    }
    
    @Bean(name="badgeAddNewRulePreparerManager")
    public IRulesPreparerManager badgeAddNewRulePreparerManager() {
        RulesPreparerManager manager = new RulesPreparerManager();
        
        List<IRulesDAO> daos = new ArrayList<>();
        daos.add(badgeAddNewRuleDroolsRuleTemplateDAO());
        
        manager.setDaos(daos);
        
        return manager;
    }
    
    @Bean(name="rovGameRulesPreparerManager")
    public IRulesPreparerManager rovGameRulesPreparerManager() {
        RulesPreparerManager manager = new RulesPreparerManager();
        
        List<IRulesDAO> daos = new ArrayList<>();
        //daos.add(rovGameBadgeCollectionPluginDroolsRuleDAO());
        daos.add(rovGamePointPluginDroolsRuleDAO());
        
        manager.setDaos(daos);
        
        return manager;
    }
    
    
    ///////IRulesDAO///////
    @Bean(name="pointDroolsRuleTemplateDAO")
    public IRulesDAO pointDroolsRuleTemplateDAO() {
        DroolsRuleTemplateDAO dao = new DroolsRuleTemplateDAO();
        dao.setSpreadSheetDAO(pointMockSpreadSheetDAO());
        dao.setRulesStreamDAO(pointDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="badgeDroolsRuleTemplateDAO")
    public IRulesDAO badgeDroolsRuleTemplateDAO() {
        DroolsRuleTemplateDAO dao = new DroolsRuleTemplateDAO();
        dao.setSpreadSheetDAO(badgeMockSpreadSheetDAO());
        dao.setRulesStreamDAO(badgeDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="badgeAddNewRuleDroolsRuleTemplateDAO")
    public IRulesDAO badgeAddNewRuleDroolsRuleTemplateDAO() {
        DroolsRuleTemplateDAO dao = new DroolsRuleTemplateDAO();
        dao.setSpreadSheetDAO(badgeMockAddNewRuleSpreadSheetDAO());
        dao.setRulesStreamDAO(badgeDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="rovGamePointPluginDroolsRuleTemplateDAO")
    public IRulesDAO rovGamePointPluginDroolsRuleTemplateDAO() {
        DroolsRuleTemplateDAO dao = new DroolsRuleTemplateDAO();
        dao.setSpreadSheetDAO(rovGamePointPluginMockSpreadSheetDAO());
        dao.setRulesStreamDAO(rovGamePointPluginDroolsRuleTemplatesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="rovGamePointPluginDroolsRuleDAO")
    public IRulesDAO rovGamePointPluginDroolsRuleDAO() {
        DroolsFileRulesDAO dao = new DroolsFileRulesDAO();
        dao.setRulesStreamDAO(rovGamePointPluginDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="rovGameBadgeCollectionPluginDroolsRuleTemplateDAO")
    public IRulesDAO rovGameBadgeCollectionPluginDroolsRuleTemplateDAO() {
        DroolsRuleTemplateDAO dao = new DroolsRuleTemplateDAO();
        dao.setSpreadSheetDAO(rovGameBadgeCollectionPluginMockSpreadSheetDAO());
        dao.setRulesStreamDAO(rovGameBadgeCollectionPluginDroolsRulesStreamDAO());
        
        return dao;
    }
    
    @Bean(name="rovGameBadgeCollectionPluginDroolsRuleDAO")
    public IRulesDAO rovGameBadgeCollectionPluginDroolsRuleDAO() {
        DroolsFileRulesDAO dao = new DroolsFileRulesDAO();
        dao.setRulesStreamDAO(rovGameBadgeCollectionPluginDroolsRulesStreamDAO());
        
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
    
    @Bean(name="rovGamePointPluginMockSpreadSheetDAO")
    public ISpreadSheetDAO rovGamePointPluginMockSpreadSheetDAO() {
        MockSpreadSheetDAO dao = new MockSpreadSheetDAO();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\"1\",\"saveItinerary\",\"bikeKM\",\"greenLeaves\",\"2\"\n");
        sb.append("\"2\",\"saveItinerary\",\"busKM\",\"greenLeaves\",\"1\"\n");
        
        dao.setContent(sb.toString());
        
        return dao;
    }
    
    @Bean(name="rovGameBadgeCollectionPluginMockSpreadSheetDAO")
    public ISpreadSheetDAO rovGameBadgeCollectionPluginMockSpreadSheetDAO() {
        MockSpreadSheetDAO dao = new MockSpreadSheetDAO();
        
        StringBuilder sb = new StringBuilder();
        sb.append("\"1\",\"1\",\"20\",\"2\"\n");
        sb.append("\"2\",\"1\",\"50\",\"3\"\n");
        
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
    
    @Bean(name="rovGamePointPluginDroolsRuleTemplatesStreamDAO")
    public IRulesStreamDAO rovGamePointPluginDroolsRuleTemplatesStreamDAO() {
        DroolsRulesStreamDAO dao = new DroolsRulesStreamDAO();
        
        dao.setResourcePath(DROOLS_RULES_ROV_GAME_PATH + "pointTemplates.drt");
        
        return dao;
    }
    
    @Bean(name="rovGamePointPluginDroolsRulesStreamDAO")
    public IRulesStreamDAO rovGamePointPluginDroolsRulesStreamDAO() {
        DroolsRulesStreamDAO dao = new DroolsRulesStreamDAO();
        
        dao.setResourcePath(DROOLS_RULES_ROV_GAME_PATH + "pointRules.drl");
        
        return dao;
    }
    
    @Bean(name="rovGameBadgeCollectionPluginDroolsRuleTemplatesStreamDAO")
    public IRulesStreamDAO rovGameBadgeCollectionPluginDroolsRuleTemplatesStreamDAO() {
        DroolsRulesStreamDAO dao = new DroolsRulesStreamDAO();
        
        dao.setResourcePath(DROOLS_RULES_ROV_GAME_PATH + "badgecollectionTemplates.drt");
        
        return dao;
    }
    
    @Bean(name="rovGameBadgeCollectionPluginDroolsRulesStreamDAO")
    public IRulesStreamDAO rovGameBadgeCollectionPluginDroolsRulesStreamDAO() {
        DroolsRulesStreamDAO dao = new DroolsRulesStreamDAO();
        
        dao.setResourcePath(DROOLS_RULES_ROV_GAME_PATH + "badgecollectionRules.drl");
        
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