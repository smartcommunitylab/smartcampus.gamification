package eu.trentorise.game.event.service;

import eu.trentorise.game.event.model.StartEvent;
import eu.trentorise.game.response.SuccessResponse;

/**
 *
 * @author Luca Piras
 */
public interface IStartEventManager {
    
    public SuccessResponse runEvent(StartEvent event);
}