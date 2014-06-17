package eu.trentorise.game.event.service;

import eu.trentorise.game.event.model.StartEvent;
import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import eu.trentorise.game.response.MockResponder;
import eu.trentorise.game.response.GameResponse;
import eu.trentorise.game.ruleengine.service.IRulesEngineManager;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


@Service("mockStartEventManager")
public class MockStartEventManager extends MockResponder implements IStartEventManager {

    private static Logger logger = LoggerFactory.getLogger(MockStartEventManager.class.getName());
    
    @Override
    public GameResponse runEvent(StartEvent event) {
        
        List elements = new ArrayList();
        elements.add(event);
        try {
            //TODO: manage the value of the gamification approach id
            rulesEngineManager.runEngine(elements,
                    GamificationPluginIdentifier.POINT_PLUGIN);
        } catch (Exception ex) {
            logger.debug("************************** EXCEPTION *******************************" + ex);
        }
        
        return this.getPositiveResponse();
    }
    
    @Qualifier("pointRulesEngineManager")
    @Autowired
    protected IRulesEngineManager rulesEngineManager;
}