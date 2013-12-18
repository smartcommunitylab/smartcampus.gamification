package eu.trentorise.game.controller;

import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.response.SuccessResponse;
import eu.trentorise.game.service.IPlayerManager;
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
@Controller("playerController")
@RequestMapping(IGameConstants.SERVICE_GAME_PLAYER)
public class PlayerController {

    @RequestMapping(method = RequestMethod.GET, value = "/add" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody SuccessResponse add(@RequestParam(value = "username", required = true) String username,
                                             @RequestParam(value = "initialPoints", required = false) Integer initialPoints) {
        
        //TODO: manage the case when the username is yet used
        Player player = new Player();
        
        player.setUsername(username);
        player.setPoints(initialPoints);
        
        return manager.add(player);
    }
    
    @Qualifier("mockPlayerManager")
    @Autowired
    protected IPlayerManager manager;
}