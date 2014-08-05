package eu.trentorise.game.profile.game.controller;

import eu.trentorise.game.controller.IGameConstants;
import eu.trentorise.game.profile.game.model.Game;
import eu.trentorise.game.response.GameCollectionResponse;
import eu.trentorise.game.response.NewGameResponse;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriComponentsBuilder;


/**
 *
 * @author Luca Piras
 */
@Controller("gameProfileController")
public class GameProfileController extends AbstractCrudRestController<Game, Object, Game> {
    
    //TODO: IMPORTANT!!! define validators for all the services exposed by the
    //controllers
    
    public GameProfileController() {
        super(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH,
              LoggerFactory.getLogger(GameProfileController.class.getName()));
    }
    
    
    //CREATE
    @RequestMapping(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_PATH, method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Void> createGame(@RequestBody Game game,
                                           UriComponentsBuilder builder) {
        
        return super.createResource(game, builder);
    }
    
    
    //READ
    @RequestMapping(value=IGameConstants.SERVICE_GAME_PROFILE_GAMES_PATH, method = RequestMethod.GET)
    public @ResponseBody GameCollectionResponse readGames() {
        Collection<Game> results = super.readResources(null);
                                                
        GameCollectionResponse response = new GameCollectionResponse();
        response.setGames(results);
        
        return response;
    }
    
    @RequestMapping(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH, method = RequestMethod.GET)
    public @ResponseBody NewGameResponse readGameById(@PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId) {
        
        Game element = new Game();
        element.setId(gameId);
        
        Game result = super.readResourceById(element);
        
        NewGameResponse response = new NewGameResponse();
        response.setGame(result);
        
        return response;
    }
    
    
    //UPDATE
    @RequestMapping(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH, method = RequestMethod.PUT)
    public @ResponseBody ResponseEntity<Void> updateGame(@PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId, 
                                                         @RequestBody Game game,
                                                         UriComponentsBuilder builder) {
        
        game.setId(gameId);
        
        return super.updateResource(game, builder);
    }
    
    
    //DELETE
    @RequestMapping(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH, method = RequestMethod.DELETE)
    public @ResponseBody ResponseEntity<Void> deleteGame(@PathVariable(value = IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM) Integer gameId) {
        
        Game element = new Game();
        element.setId(gameId);
        
        return super.deleteResource(element);
    }
    
    
    @Override
    protected Map<String, Object> populateUriVariables(Game containerWithIds,
                                                       Game result, 
                                                       Map<String, Object> uriVariables) {
        
        uriVariables.put(IGameConstants.SERVICE_GAME_PROFILE_GAMES_SINGLE_PATH_PARAM, result.getId());
        
        return uriVariables;
    }
    

    @Qualifier("mockGameProfileManager")
    @Autowired
    public void setManager(IRestCrudManager<Game, Object, Game> manager) {
        this.manager = manager;
    }

    @Qualifier("restCrudHelper")
    @Autowired
    public void setRestCrudHelper(RestCrudHelper<Game, Object, Game> restCrudHelper) {
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
}