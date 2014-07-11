package eu.trentorise.game.profile.game.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.request.NewGameRequest;
import eu.trentorise.game.profile.game.container.INewGameContainer;
import eu.trentorise.game.profile.game.container.NewGameContainer;
import eu.trentorise.game.profile.game.response.NewGameResponse;
import eu.trentorise.game.profile.game.service.IGameProfileManager;
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
@Controller("gameProfileController")
@RequestMapping(IGameConstants.SERVICE_GAME_PROFILE_GAME_PATH)
public class GameProfileController {

    @RequestMapping(method = RequestMethod.POST, value = "/newGame" + IGameConstants.SERVICE_SEPARATOR_PLUS_EXTENSION)
    public @ResponseBody NewGameResponse newGame(@RequestBody NewGameRequest co) {
        INewGameContainer container = new NewGameContainer();
        container.setGame(co.getGame());
        
        return manager.newGame(container);
    }
    
    @Qualifier("mockGameProfileManager")
    @Autowired
    protected IGameProfileManager manager;
}