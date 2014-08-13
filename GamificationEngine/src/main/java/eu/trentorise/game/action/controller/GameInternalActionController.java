package eu.trentorise.game.action.controller;

import eu.trentorise.game.action.model.GameInternalAction;
import eu.trentorise.game.action.model.InternalAction;
import eu.trentorise.game.action.response.GameInternalActionCollectionResponse;
import eu.trentorise.game.action.response.GameInternalActionResponse;
import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.utils.rest.RestExceptionHandler;
import eu.trentorise.utils.rest.crud.AbstractCrudRestController;
import eu.trentorise.utils.rest.crud.IRestCrudManager;
import eu.trentorise.utils.rest.crud.RestCrudHelper;
import eu.trentorise.utils.rest.crud.RestCrudResponseHelper;
import eu.trentorise.utils.rest.crud.RestCrudResultHelper;
import java.util.Collection;
import java.util.Map;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 *
 * @author Luca Piras
 */
@Controller("gameInternalActionController")
public class GameInternalActionController extends AbstractCrudRestController<GameInternalAction, GameInternalAction, GameInternalAction> {

    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public GameInternalActionController() {
        super(IGameConstants.SERVICE_GAME_INTERNALACTIONS_SINGLE_PATH, 
              LoggerFactory.getLogger(GameInternalActionController.class.getName()));
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_GAME_INTERNALACTIONS_PATH, method = RequestMethod.GET)
    public @ResponseBody GameInternalActionCollectionResponse readInternalActions(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId) {
        
        GameInternalAction element = this.makeGameInternalAction(gameId, null);
        
        Collection<GameInternalAction> results = super.readResources(element);
                                                
        GameInternalActionCollectionResponse response = new GameInternalActionCollectionResponse();
        response.setGameInternalActions(results);
        
        return response;
    }
    
    
    @RequestMapping(value = IGameConstants.SERVICE_GAME_INTERNALACTIONS_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody GameInternalActionResponse readInternalActionById(
                         @PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId,
                         @PathVariable(value = IGameConstants.SERVICE_INTERNALACTIONS_SINGLE_PATH_PARAM) Integer intActId) {
        
        GameInternalAction element = this.makeGameInternalAction(gameId, intActId);
        
        GameInternalAction result = super.readResourceById(element);
        
        GameInternalActionResponse response = new GameInternalActionResponse();
        response.setGameInternalAction(result);
        
        return response;
    }
    

    @Override
    protected Map<String, Object> populateUriVariables(GameInternalAction containerWithIds, GameInternalAction result, Map<String, Object> uriVariables) {
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, result.getGame().getId());
        uriVariables.put(IGameConstants.SERVICE_INTERNALACTIONS_SINGLE_PATH_PARAM, result.getInternalAction().getId());
        
        return uriVariables;
    }
    
    
    protected GameInternalAction makeGameInternalAction(Integer gameId, Integer intActId) {
        Game game = new Game();
        game.setId(gameId);

        InternalAction ia = new InternalAction();
        ia.setId(intActId);

        GameInternalAction element = new GameInternalAction();
        element.setGame(game);
        element.setInternalAction(ia);

        return element;
    }
        
    
    @Qualifier("mockGameInternalActionManager")
    @Autowired
    public void setManager(IRestCrudManager<GameInternalAction, GameInternalAction, GameInternalAction> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<GameInternalAction, GameInternalAction, GameInternalAction> restCrudHelper) {
        this.restCrudHelper = restCrudHelper;
    }

    @Qualifier("restCrudResultHelper")
    @Autowired
    public void setRestCrudResultHelper(RestCrudResultHelper restCrudResultHelper) {
        this.restCrudResultHelper = restCrudResultHelper;
    }
    
    @Qualifier("restCrudResponseHelper")
    @Autowired
    public void setRestCrudResponseHelper(RestCrudResponseHelper restCrudResponseHelper) {
        this.restCrudResponseHelper = restCrudResponseHelper;
    }
    
    @Qualifier("restExceptionHandler")
    @Autowired
    public void setRestExceptionHandler(RestExceptionHandler restExceptionHandler) {
        this.restExceptionHandler = restExceptionHandler;
    }
}