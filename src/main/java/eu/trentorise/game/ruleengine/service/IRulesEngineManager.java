package eu.trentorise.game.ruleengine.service;

import eu.trentorise.game.plugin.GamificationPluginIdentifier;

/**
 *
 * @author Luca Piras
 */
public interface IRulesEngineManager {
    
    public void runEngine(GamificationPluginIdentifier gamificationApproachId);
}