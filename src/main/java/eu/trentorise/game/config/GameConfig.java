package eu.trentorise.game.config;

import eu.trentorise.game.ruleengine.data.ISpreadSheetDAO;
import eu.trentorise.game.ruleengine.data.drools.DroolsTemplateRulesDAO;
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
    
    @Bean(name="rulesPreparerManager")
    public RulesPreparerManager rulesPreparerManager() {
        RulesPreparerManager rulesPreparerManager = new RulesPreparerManager();
        rulesPreparerManager.setDao(droolsTemplateRulesDAO());
        
        return rulesPreparerManager;
    }
    
    @Bean(name="mockAddNewRulePreparerManager")
    public RulesPreparerManager mockAddNewRulePreparerManager() {
        RulesPreparerManager rulesPreparerManager = new RulesPreparerManager();
        rulesPreparerManager.setDao(addNewRuleDroolsTemplateRulesDAO());
        
        return rulesPreparerManager;
    }
    
    @Bean(name="droolsTemplateRulesDAO")
    public DroolsTemplateRulesDAO droolsTemplateRulesDAO() {
        DroolsTemplateRulesDAO droolsTemplateRulesDAO = new DroolsTemplateRulesDAO();
        droolsTemplateRulesDAO.setSpreadSheetDAO(mockSpreadSheetDAO);
        
        return droolsTemplateRulesDAO;
    }
    
    @Bean(name="addNewRuleDroolsTemplateRulesDAO")
    public DroolsTemplateRulesDAO addNewRuleDroolsTemplateRulesDAO() {
        DroolsTemplateRulesDAO droolsTemplateRulesDAO = new DroolsTemplateRulesDAO();
        droolsTemplateRulesDAO.setSpreadSheetDAO(mockAddNewRuleSpreadSheetDAO);
        
        return droolsTemplateRulesDAO;
    }
    
    
    @Qualifier("mockSpreadSheetDAO")
    @Autowired
    private ISpreadSheetDAO mockSpreadSheetDAO;
    @Qualifier("mockAddNewRuleSpreadSheetDAO")
    @Autowired
    private ISpreadSheetDAO mockAddNewRuleSpreadSheetDAO;
}