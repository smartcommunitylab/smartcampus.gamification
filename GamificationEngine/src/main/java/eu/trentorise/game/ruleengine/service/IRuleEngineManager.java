package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.plugin.PluginIdentifier;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public interface IRuleEngineManager {
    
    public void runEngine(Collection facts, PluginIdentifier gamificationApproachId) throws Exception;
}