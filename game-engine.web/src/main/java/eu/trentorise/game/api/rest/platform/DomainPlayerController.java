package eu.trentorise.game.api.rest.platform;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.ChallengeAssignmentDTO;
import eu.trentorise.game.bean.GroupChallengeDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.bean.WrapperQuery;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.managers.ChallengeManager;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeInvitation;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.Inventory;
import eu.trentorise.game.model.Inventory.ItemChoice;
import eu.trentorise.game.model.PlayerBlackList;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.ChallengeAssignment;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.utils.Converter;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Profile({ "platform" })
public class DomainPlayerController {

    private static final Logger logger = LoggerFactory.getLogger(DomainPlayerController.class);

	@Autowired
	private Converter converter;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private GameService gameSrv;

    @Autowired
    private ChallengeManager challengeSrv;

	@RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/challenges", consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "Assign challenge")
	public void assignChallenge(@PathVariable String domain, @RequestBody ChallengeAssignmentDTO challengeData,
			@PathVariable String gameId, @PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		ChallengeAssignment assignment = converter.convert(challengeData);
		playerSrv.assignChallenge(gameId, playerId, assignment);
	}

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/data/game/{gameId}/group-challenges",
            consumes = {"application/json"}, produces = {"application/json"})
    public void assignGroupChallenge(@PathVariable String domain,
            @RequestBody GroupChallengeDTO challengeData,
            @PathVariable String gameId) {

        gameId = decodePathVariable(gameId);
        GroupChallenge assignment = converter.convert(challengeData);
        challengeSrv.save(assignment);
    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/invitation",
            consumes = {"application/json"}, produces = {"application/json"})
    public ChallengeInvitation inviteIntoAChallenge(@PathVariable String domain,
            @RequestBody ChallengeInvitation invitation,
            @PathVariable String gameId, @PathVariable String playerId) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);

        GroupChallenge pendingChallenge = challengeSrv.inviteToChallenge(invitation);
        if (invitation != null) {
            invitation.setChallengeName(pendingChallenge.getInstanceName());
        }
        return invitation;
    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/invitation/accept/{challengeName}",
            consumes = {"application/json"}, produces = {"application/json"})
    public void acceptInvitation(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String playerId,
            @PathVariable String challengeName) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        challengeName = decodePathVariable(challengeName);

        challengeSrv.acceptInvitation(gameId, playerId, challengeName);

    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/invitation/refuse/{challengeName}",
            consumes = {"application/json"}, produces = {"application/json"})
    public void refuseInvitation(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String playerId,
            @PathVariable String challengeName) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        challengeName = decodePathVariable(challengeName);

        challengeSrv.refuseInvitation(gameId, playerId, challengeName);

    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/invitation/cancel/{challengeName}",
            consumes = {"application/json"}, produces = {"application/json"})
    public void cancelInvitation(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String playerId,
            @PathVariable String challengeName) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        challengeName = decodePathVariable(challengeName);

        challengeSrv.cancelInvitation(gameId, playerId, challengeName);

    }

	@RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/challenges/{challengeName}/accept")
	@ApiOperation(value = "Accept challenge")
	public ChallengeConcept acceptChallenge(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId, @PathVariable String challengeName) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		return playerSrv.acceptChallenge(gameId, playerId, challengeName);

	}

	// Create a player
	// POST /data/game/{id}/player/{playerId}
	// ­ Error if the player ID already exists
	// ­ In body specify alias (optional, e.g., email, nick or something else),
	// customData
	// (optional)
	// ­ No implicit fields (e.g., game) in input
	// ­ No concept fields in input

	@RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/data/game/{gameId}/player/{playerId}", consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "Create player")
	public void createPlayer(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody PlayerStateDTO player) {
		gameId = decodePathVariable(gameId);
		// check if player already exists
		if (playerSrv.loadState(gameId, player.getPlayerId(), false, false) != null) {
			throw new IllegalArgumentException(
					String.format("Player %s already exists in game %s", player.getPlayerId(), gameId));
		}

		Game game = gameSrv.loadGameDefinitionById(gameId);

		player.setGameId(gameId);
		PlayerState p = converter.convertPlayerState(player);
		playerSrv.saveState(p);
		StatsLogger.logUserCreation(game.getDomain(), gameId, player.getPlayerId(), UUID.randomUUID().toString(),
				System.currentTimeMillis());
	}

	// Read a player
	// GET /data/game/{id}/player/{playerId}
	// ­ Return everything: playerId, alias, customData, state (concept fields),
	// teams, challenges

	@RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/player/{playerId}", produces = {
			"application/json" })
	@ApiOperation(value = "Get player state")
	public PlayerStateDTO readPlayer(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		return converter.convertPlayerState(playerSrv.loadState(gameId, playerId, true, true));
	}

	// Update a player
	// PUT /data/game/{id}/player/{playerId}
	// ­ Do not update the concept fields
	// ­ Error if the player ID does not exist
	// ­ If alias not present, do not update it; if customdata not present do
	// not update it.
	@RequestMapping(method = RequestMethod.PUT, value = "/api/{domain}/data/game/{gameId}/player/{playerId}", consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "Edit player state")
	public void updatePlayer(@PathVariable String domain, @PathVariable String gameId, @PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		throw new UnsupportedOperationException("Operation actually not supported");
	}

	// Delete a player
	// DELETE /data/game/{id}/player/{playerId}

	@RequestMapping(method = RequestMethod.DELETE, value = "/api/{domain}/data/game/{gameId}/player/{playerId}", produces = {
			"application/json" })
	@ApiOperation(value = "Delete player state")
	public void deletePlayer(@PathVariable String domain, @PathVariable String gameId, @PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		playerSrv.deleteState(gameId, playerId);
	}

	// Read player’s teams
	// GET /data/game/{id}/player/{playerId}/teams

	@RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/teams", produces = {
			"application/json" })
	@ApiOperation(value = "Get player teams")
	public List<TeamDTO> readTeamsByMember(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		List<TeamState> result = playerSrv.readTeams(gameId, playerId);
		List<TeamDTO> converted = new ArrayList<>();
		for (TeamState r : result) {
			converted.add(converter.convertTeam(r));
		}
		return converted;
	}

	// Read user challenges
	// GET /data/game/{id}/player/{playerId}/challenges

	@RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/challenges", produces = {
			"application/json" })
	@ApiOperation(value = "Get player challenges")
	public List<ChallengeConcept> getPlayerChallenge(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);

		PlayerState state = playerSrv.loadState(gameId, playerId, true, false);
		return state.challenges();
	}

	// Read user game state
	// GET /data/game/{id}/player/{playerId}/state
	@RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/state", produces = {
			"application/json" })
	@ApiOperation(value = "Get player state")
	public PlayerStateDTO readState(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId) {
		return readPlayer(domain, gameId, playerId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/levels", produces = {
			"application/json" })
	@ApiOperation(value = "Get player levels")
	public List<PlayerLevel> readLevels(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId) {
		PlayerState state = playerSrv.loadState(gameId, playerId, false, false);
		if (state != null) {
			return state.getLevels();
		} else {
			return Collections.emptyList();
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/inventory", produces = {
			"application/json" })
	@ApiOperation(value = "Get player inventory")
	public Inventory readInventory(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId) {
		PlayerState state = playerSrv.loadState(gameId, playerId, false, false);
		if (state != null) {
			return state.getInventory();
		} else {
			throw new IllegalArgumentException(
					String.format("state for player %s in game %s doesn't exist", playerId, gameId));
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/inventory/activate", produces = {
			"application/json" })
	@ApiOperation(value = "Activate a choice")
	public Inventory activateChoice(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId, @RequestBody ItemChoice choice) {
        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        return playerSrv.choiceActivation(gameId, playerId, choice);
	}

	// Read user custom data
	// GET /data/game/{id}/player/{playerId}/custom
	@RequestMapping(method = RequestMethod.GET, value = "/api/{domain}/data/game/{gameId}/player/{playerId}/custom", produces = {
			"application/json" })
	@ApiOperation(value = "Get player custom data")
	public PlayerStateDTO readCustomData(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		throw new UnsupportedOperationException("Operation actually not supported");

	}

	@RequestMapping(method = RequestMethod.POST, value = "/api/{domain}/data/game/{gameId}/player/search", consumes = {
			"application/json" }, produces = { "application/json" })
	@ApiOperation(value = "Search player states")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve "),
			@ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page."), })
	public Page<PlayerStateDTO> searchByQuery(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody WrapperQuery query, @ApiIgnore Pageable pageable) {
		gameId = decodePathVariable(gameId);
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

		PageImpl<PlayerStateDTO> res = new PageImpl<PlayerStateDTO>(resList, pageable, page.getTotalElements());

		return res;
	}

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/block/{otherPlayerId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Add another player to challenge block list")
    public PlayerBlackList blockPlayer(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String playerId,
            @PathVariable String otherPlayerId) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        otherPlayerId = decodePathVariable(otherPlayerId);

        LogHub.info(gameId, logger,
                String.format("add player %s to black list of player %s", otherPlayerId, playerId));

        return playerSrv.blockPlayer(gameId, playerId, otherPlayerId);

    }

    @RequestMapping(method = RequestMethod.POST,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/unblock/{otherPlayerId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Unblock another player from challenge block list")
    public PlayerBlackList unBlockPlayer(@PathVariable String domain, @PathVariable String gameId,
            @PathVariable String playerId,
            @PathVariable String otherPlayerId) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);
        otherPlayerId = decodePathVariable(otherPlayerId);

        LogHub.info(gameId, logger, String.format("remove player %s from black list of player %s",
                otherPlayerId, playerId));

        return playerSrv.unblockPlayer(gameId, playerId, otherPlayerId);

    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/blacklist",
            produces = {"application/json"})
    @ApiOperation(value = "Get player black list of other players")
    public PlayerBlackList readPlayerBlackList(@PathVariable String domain,
            @PathVariable String gameId,
            @PathVariable String playerId) {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);

        return playerSrv.readBlackList(gameId, playerId);

    }

    @RequestMapping(method = RequestMethod.GET,
            value = "/api/{domain}/data/game/{gameId}/player/{playerId}/challengers",
            produces = {"application/json"})
    @ApiOperation(value = "Get availabe challengers for the player")
    public List<String> readSystemPlayerState(@PathVariable String domain,
            @PathVariable String gameId,
            @PathVariable String playerId, @RequestParam(required = false) String conceptName)
            throws Exception {

        gameId = decodePathVariable(gameId);
        playerId = decodePathVariable(playerId);

        if (conceptName != null) {
            conceptName = decodePathVariable(conceptName);
        }

        return playerSrv.readSystemPlayerState(gameId, playerId, conceptName);

    }

}
