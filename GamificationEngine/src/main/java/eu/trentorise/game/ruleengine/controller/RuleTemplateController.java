package eu.trentorise.game.ruleengine.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.ruleengine.container.IRuleTemplateContainer;
import eu.trentorise.game.ruleengine.container.RuleTemplateContainer;
import eu.trentorise.game.ruleengine.request.RuleTemplateRequest;
import eu.trentorise.game.ruleengine.response.RuleTemplateResponse;
import eu.trentorise.game.ruleengine.service.IRuleTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author Luca Piras
 */
@Controller("ruleTemplateController")
@RequestMapping(IGameConstants.SERVICE_RULEENGINE_TEMPLATERULE_PATH)
public class RuleTemplateController {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    @RequestMapping(method = RequestMethod.POST, value = "/getRuleTemplates" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody RuleTemplateResponse getRuleTemplates(@RequestBody RuleTemplateRequest request) throws Exception {
        //TODO: this service will provide a list of ruleTemplates related to the
        //gamificationPlugin specified in the request
        IRuleTemplateContainer container = new RuleTemplateContainer();
        container.setRuleTemplate(request.getRuleTemplate());
        
        return manager.getRuleTemplates(container);
    }
    
    @Qualifier("mockRuleTemplateManager")
    @Autowired
    protected IRuleTemplateManager manager;
}