package eu.trentorise.game.aaa.controller;

import eu.trentorise.game.aaa.request.GameAuthenticationRequest;
import eu.trentorise.game.aaa.service.IGameAuthenticationManager;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.response.GameResponse;
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
@Controller("gameAuthenticationController")
@RequestMapping(IGameConstants.SERVICE_GAME_AAA_PATH)
public class GameAuthenticationController {

    @RequestMapping(method = RequestMethod.POST, value = "/login" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody GameResponse authenticate(@RequestBody GameAuthenticationRequest co) {
        
        return manager.authenticate(null);
    }
    
    @Qualifier("mockGameAuthenticationManager")
    @Autowired
    protected IGameAuthenticationManager manager;
}