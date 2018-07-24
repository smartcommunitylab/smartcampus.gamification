package eu.trentorise.game.api.rest.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.LevelDTO;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.service.IdentityLookupService;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.utils.Converter;
import io.swagger.annotations.ApiOperation;

@RestController
@Profile("platform")
public class DomainGameController  {

    @Autowired
    private GameService gameSrv;

    @Autowired
    private Converter converter;

    @Autowired
    private IdentityLookupService identityLookup;

    /**
     * ************************************************************************* GAME API
     * ************************************************************************
     */

    // Create a game
    // POST /model/game
    // TODO: ­ Remove from body implicit elements (e.g., owner) , The content
    // elements are optional (except name)

    @RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/model/game",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Save a game")
    public GameDTO saveGame(@PathVariable String domain, @RequestBody GameDTO game) {
        // set creator
        String user = identityLookup.getName();
        game.setOwner(user);
        game.setDomain(domain);
        Game res = gameSrv.saveGameDefinition(converter.convertGame(game));
        return converter.convertGame(res);
    }

    //
    // Read a game
    // GET /model/game/{id}

    @RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/model/game/{gameId}",
            produces = {"application/json"})
    @ApiOperation(value = "Read game definition")
    public GameDTO readGame(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        Game g = gameSrv.loadGameDefinitionById(gameId);
        return g == null ? null : converter.convertGame(g);
    }

    // Read all games of the user
    // GET /model/game

    @RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/model/game",
            produces = {"application/json"})
    @ApiOperation(value = "Get games", notes = "Get all the game definitions of a user")
    public List<GameDTO> readGames(@PathVariable String domain) {
        String user = identityLookup.getName();
        List<GameDTO> r = new ArrayList<GameDTO>();
        for (Game g : gameSrv.loadGameByOwner(user)) {
            r.add(converter.convertGame(g));
        }
        return r;
    }

    // Delete a game
    // DELETE /model/game/{id}

    @RequestMapping(method = RequestMethod.DELETE, value = "/api/{domain}/model/game/{gameId}",
            produces = {"application/json"})
    @ApiOperation(value = "Delete game")
    public void deleteGame(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        gameSrv.deleteGame(gameId);
    }

    // Start a game
    // PUT /model/game/{id}/start

    @RequestMapping(method = RequestMethod.PUT, value = "/api/{domain}/model/game/{gameId}/start",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Start game", notes = "The game is able to accept action executions")
    public void startGame(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }
        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Stop a game
    // PUT /model/game/{id}/stop

    @RequestMapping(method = RequestMethod.PUT, value = "/api/{domain}/model/game/{gameId}/stop",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Stop a game",
            notes = "The game will not accept action execution anymore")
    public void stopGame(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        throw new UnsupportedOperationException("Operation actually not supported");
    }

    /**
     * ************************************************************************* ACTION API
     * ************************************************************************
     */

    // Create action concept
    // POST /model/game/{id}/action/{actionId}
    // ­ Action should be unique. Error if exists
    // ­ Consider other fields: name, description

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/model/game/{gameId}/action/{actionId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Add action")
    public void addAction(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }
        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Modify an action
    // PUT /model/game/{id}/action/{actionId}

    @RequestMapping(method = RequestMethod.PUT,
            value = "/api/{domain}/model/game/{domain}/action/{actionId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Edit action")
    public void editAction(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }
        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Read all actions
    // GET /model/game/{id}/action

    @RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/model/game/{gameId}/action",
            produces = {"application/json"})
    @ApiOperation(value = "Get actions")
    public Set<String> readAllAction(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }
        Game g = gameSrv.loadGameDefinitionById(gameId);
        return g != null ? g.getActions() : Collections.<String>emptySet();

    }

    // Read an action
    // GET /model/game/{id}/action/{actionId}

    @RequestMapping(method = RequestMethod.GET,
            value = "/api/{domain}/model/game/{gameId}/action/{actionId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get action")
    public void readAction(@PathVariable String domain, @PathVariable String gameId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }
        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Delete an action
    // DELETE /model/game/{id}/action/{actionId}
    @RequestMapping(method = RequestMethod.DELETE,
            value = "/api/{domain}/model/game/{gameId}/action/{actionId}",
            produces = {"application/json"})
    @ApiOperation(value = "Delete action")
    public void deleteAction(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String actionId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            actionId = URLDecoder.decode(actionId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("actionId is not UTF-8 encoded");
        }
        Game g = gameSrv.loadGameDefinitionById(gameId);

        if (g != null) {
            g.getActions().remove(actionId);
            gameSrv.saveGameDefinition(g);
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/model/game/{gameId}/level",
            produces = {"application/json"})
    @ApiOperation(value = "Save a level")
    public LevelDTO saveLevel(@PathVariable String gameId, @RequestBody LevelDTO level) {
        Game game = gameSrv.upsertLevel(gameId, converter.convert(level));

        Level saved = game.getLevels().stream().filter(lev -> lev.getName().equals(level.getName()))
                .findFirst().orElse(null);
        return converter.convert(saved);

    }

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/api/{domain}/model/game/{gameId}/level/{levelName}",
            produces = {"application/json"})
    @ApiOperation(value = "Delete a level")
    public boolean deleteLevel(@PathVariable String gameId, @PathVariable String levelName) {
        gameSrv.deleteLevel(gameId, levelName);
        return true;


    }

}
