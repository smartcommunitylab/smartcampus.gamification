package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.plugin.GamificationPluginIdentifier;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
public interface IRulesEngineManager {
    
    public void runEngine(Collection facts, GamificationPluginIdentifier gamificationApproachId);
}