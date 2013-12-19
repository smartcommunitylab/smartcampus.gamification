package eu.trentorise.game.event.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.event.controller.service.IStartEventManager;
import eu.trentorise.game.event.model.StartEvent;
import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.response.SuccessResponse;
import eu.trentorise.game.rule.model.Action;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @author Luca Piras
 */
@Controller("startEventController")
@RequestMapping(IGameConstants.SERVICE_EVENTS_START_EVENT)
public class StartEventController {
    
    @RequestMapping(method = RequestMethod.GET, value = "/runEvent" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody SuccessResponse runEvent(@RequestParam(value = "actionPerformerUsername", required = true) String actionPerformerUsername,
                                                       @RequestParam(value = "actionId", required = true) Integer actionId) {
        
        //TODO: substitute it with the retrieve from the db of the user, in 
        //another layer
        Player player = new Player();
        player.setUsername(actionPerformerUsername);
        
        Action action = new Action();
        action.setId(actionId);
        
        StartEvent event = new StartEvent();
        event.setPlayer(player);
        event.setAction(action);
        
        return manager.runEvent(event);
    }
    
    @Qualifier("mockStartEventManager")
    @Autowired
    protected IStartEventManager manager;
}