package eu.trentorise.game.event.service;

import eu.trentorise.game.event.model.StartEvent;
import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.SuccessResponse;
import eu.trentorise.game.ruleengine.service.IRulesEngineManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockStartEventManager")
public class MockStartEventManager extends MockResponder implements IStartEventManager {

    @Override
    public SuccessResponse runEvent(StartEvent event) {
        
        //TODO: manage the value of the gamification approach id
        rulesEngineManager.runEngine(GamificationPluginIdentifier.POINT_PLUGIN);
        
        return this.getPositiveResponse();
    }
    
    @Qualifier("pointRulesEngineManager")
    @Autowired
    protected IRulesEngineManager rulesEngineManager;
}