package eu.trentorise.game.event.service;

import eu.trentorise.game.event.model.StartEvent;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IStartEventManager {
    
    public GameResponse runEvent(StartEvent event);
}