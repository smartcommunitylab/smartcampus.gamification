package eu.trentorise.game.api.rest.platform;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.ChallengeDataDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.bean.WrapperQuery;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.utils.Converter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Profile({"platform"})
public class DomainPlayerController {

    @Autowired
    private Converter converter;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private GameService gameSrv;

    @RequestMapping(method = RequestMethod.POST,
            value = "/data/{domain}/game/{gameId}/player/{playerId}/challenges",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Assign challenge")
    public void assignChallenge(@RequestBody ChallengeDataDTO challengeData,
            @PathVariable String gameId,
            @PathVariable String playerId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }
        playerSrv.assignChallenge(gameId, playerId, challengeData.getModelName(),
                challengeData.getInstanceName(), challengeData.getData(), challengeData.getStart(),
                challengeData.getEnd());
    }

    // Create a player
    // POST /data/game/{id}/player/{playerId}
    // ­ Error if the player ID already exists
    // ­ In body specify alias (optional, e.g., email, nick or something else),
    // customData
    // (optional)
    // ­ No implicit fields (e.g., game) in input
    // ­ No concept fields in input

    @RequestMapping(method = RequestMethod.POST,
            value = "/data/{domain}/game/{gameId}/player/{playerId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Create player")
    public void createPlayer(@PathVariable String gameId,
            @RequestBody PlayerStateDTO player) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        // check if player already exists
        if (playerSrv.loadState(gameId, player.getPlayerId(), false) != null) {
            throw new IllegalArgumentException(String.format("Player %s already exists in game %s",
                    player.getPlayerId(), gameId));
        }

        Game game = gameSrv.loadGameDefinitionById(gameId);

        player.setGameId(gameId);
        PlayerState p = converter.convertPlayerState(player);
        playerSrv.saveState(p);
        StatsLogger.logUserCreation(game.getDomain(), gameId, player.getPlayerId(),
                UUID.randomUUID().toString(), System.currentTimeMillis());
    }

    // Read a player
    // GET /data/game/{id}/player/{playerId}
    // ­ Return everything: playerId, alias, customData, state (concept fields),
    // teams, challenges

    @RequestMapping(method = RequestMethod.GET,
            value = "/data/{domain}/game/{gameId}/player/{playerId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get player state")
    public PlayerStateDTO readPlayer(@PathVariable String gameId,
            @PathVariable String playerId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        return converter.convertPlayerState(playerSrv.loadState(gameId, playerId, true));
    }

    // Update a player
    // PUT /data/game/{id}/player/{playerId}
    // ­ Do not update the concept fields
    // ­ Error if the player ID does not exist
    // ­ If alias not present, do not update it; if customdata not present do
    // not update it.
    @RequestMapping(method = RequestMethod.PUT,
            value = "/data/{domain}/game/{gameId}/player/{playerId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Edit player state")
    public void updatePlayer(@PathVariable String gameId,
            @PathVariable String playerId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Delete a player
    // DELETE /data/game/{id}/player/{playerId}

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/data/{domain}/game/{gameId}/player/{playerId}",
            produces = {"application/json"})
    @ApiOperation(value = "Delete player state")
    public void deletePlayer(@PathVariable String gameId,
            @PathVariable String playerId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }
        playerSrv.deleteState(gameId, playerId);
    }

    // Read player’s teams
    // GET /data/game/{id}/player/{playerId}/teams

    @RequestMapping(method = RequestMethod.GET,
            value = "/data/{domain}/game/{gameId}/player/{playerId}/teams",
            produces = {"application/json"})
    @ApiOperation(value = "Get player teams")
    public List<TeamDTO> readTeamsByMember(@PathVariable String gameId,
            @PathVariable String playerId) {

        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        List<TeamState> result = playerSrv.readTeams(gameId, playerId);
        List<TeamDTO> converted = new ArrayList<>();
        for (TeamState r : result) {
            converted.add(converter.convertTeam(r));
        }
        return converted;
    }

    // Read user challenges
    // GET /data/game/{id}/player/{playerId}/challenges

    @RequestMapping(method = RequestMethod.GET,
            value = "/data/{domain}/game/{gameId}/player/{playerId}/challenges",
            produces = {"application/json"})
    @ApiOperation(value = "Get player challenges")
    public void getPlayerChallenge(@PathVariable String gameId,
            @PathVariable String playerId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        throw new UnsupportedOperationException("Operation actually not supported");
    }

    // Read user game state
    // GET /data/game/{id}/player/{playerId}/state
    @RequestMapping(method = RequestMethod.GET,
            value = "/data/{domain}/game/{gameId}/player/{playerId}/state",
            produces = {"application/json"})
    @ApiOperation(value = "Get player state")
    public PlayerStateDTO readState(@PathVariable String gameId,
            @PathVariable String playerId) {
        return readPlayer(gameId, playerId);
    }

    // Read user custom data
    // GET /data/game/{id}/player/{playerId}/custom
    @RequestMapping(method = RequestMethod.GET,
            value = "/data/{domain}/game/{gameId}/player/{playerId}/custom",
            produces = {"application/json"})
    @ApiOperation(value = "Get player custom data")
    public PlayerStateDTO readCustomData(@PathVariable String gameId,
            @PathVariable String playerId) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        try {
            playerId = URLDecoder.decode(playerId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("playerId is not UTF-8 encoded");
        }

        throw new UnsupportedOperationException("Operation actually not supported");

    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/data/{domain}/game/{gameId}/player/search", consumes = {"application/json"},
            produces = {"application/json"})
    @ApiOperation(value = "Search player states")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query",
                    value = "Results page you want to retrieve "),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query",
                    value = "Number of records per page."),})
    public Page<PlayerStateDTO> searchByQuery(
            @PathVariable String gameId, @RequestBody WrapperQuery query,
            @ApiIgnore Pageable pageable) {
        try {
            gameId = URLDecoder.decode(gameId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("gameId is not UTF-8 encoded");
        }

        Page<PlayerState> page = null;
        if (query.getComplexQuery() != null) {
            page = playerSrv.search(gameId, query.getComplexQuery(), pageable);
        } else {
            page = playerSrv.search(gameId, query.getRawQuery(), pageable);
        }

        List<PlayerStateDTO> resList = new ArrayList<PlayerStateDTO>();

        for (PlayerState ps : page) {
            resList.add(converter.convertPlayerState(ps));
        }

        PageImpl<PlayerStateDTO> res =
                new PageImpl<PlayerStateDTO>(resList, pageable, page.getTotalElements());

        return res;

    }

}
