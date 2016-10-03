package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.ChallengeDataDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.utils.Converter;

@RestController
public class PlayerController {

	@Autowired
	private Converter converter;

	@Autowired
	private PlayerService playerSrv;

	@RequestMapping(method = RequestMethod.POST, value = "/data/game/{gameId}/player/{playerId}/challenges")
	public void assignChallenge(@RequestBody ChallengeDataDTO challengeData,
			@PathVariable String gameId, @PathVariable String playerId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		playerSrv.assignChallenge(gameId, playerId,
				challengeData.getModelName(), challengeData.getInstanceName(),
				challengeData.getData(), challengeData.getStart(),
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

	@RequestMapping(method = RequestMethod.POST, value = "/data/game/{gameId}/player/{playerId}")
	public void createPlayer(@PathVariable String gameId,
			@RequestBody PlayerStateDTO player) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		// check if player already exists
		if (playerSrv.loadState(gameId, player.getPlayerId(), false) != null) {
			throw new IllegalArgumentException(String.format(
					"Player %s already exists in game %s",
					player.getPlayerId(), gameId));
		}

		player.setGameId(gameId);
		PlayerState p = converter.convertPlayerState(player);
		playerSrv.saveState(p);
	}

	// Read a player
	// GET /data/game/{id}/player/{playerId}
	// ­ Return everything: playerId, alias, customData, state (concept fields),
	// teams, challenges

	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/player/{playerId}")
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

		return converter.convertPlayerState(playerSrv.loadState(gameId,
				playerId, true));
	}

	// Update a player
	// PUT /data/game/{id}/player/{playerId}
	// ­ Do not update the concept fields
	// ­ Error if the player ID does not exist
	// ­ If alias not present, do not update it; if customdata not present do
	// not update it.
	@RequestMapping(method = RequestMethod.PUT, value = "/data/game/{gameId}/player/{playerId}")
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

		throw new UnsupportedOperationException(
				"Operation actually not supported");
	}

	// Delete a player
	// DELETE /data/game/{id}/player/{playerId}

	@RequestMapping(method = RequestMethod.DELETE, value = "/data/game/{gameId}/player/{playerId}")
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

	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/player/{playerId}/teams")
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

	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/player/{playerId}/challenges")
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

		throw new UnsupportedOperationException(
				"Operation actually not supported");
	}

	// Read user game state
	// GET /data/game/{id}/player/{playerId}/state
	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/player/{playerId}/state")
	public PlayerStateDTO readState(@PathVariable String gameId,
			@PathVariable String playerId) {
		return readPlayer(gameId, playerId);
	}

	// Read user custom data
	// GET /data/game/{id}/player/{playerId}/custom
	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/player/{playerId}/custom")
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

		throw new UnsupportedOperationException(
				"Operation actually not supported");

	}

	// Query player information
	// POST /data/game/{id}/player/search
	// Read player data using more complex queries
	//
	// Query structure:
	// ­ sort​ definition: list of object of type (optional, no sort by default)
	// ­ {element: element, field: field, order: 1/­1}
	// ­ filter​ definition: list of objects of type
	// ­ {element: element, condition: object (in mongo syntax)}
	// ­ projection​ definition: list of object of type (optional, all elements
	// by default)
	// ­ {element: element, include:[fields] (optional, all by default),
	// exclude: [fields]
	// (optional, none by default)}
	// ­ start​ : number (optional, defaults to 0)
	// ­ count​ : number (optional, defaults to retrieve all)
	// Player data elements:
	// ­ custom
	// ­ state
	// ­ challenges

	@RequestMapping(method = RequestMethod.POST, value = "/data/game/{gameId}/player/search")
	public PlayerStateDTO search(@PathVariable String gameId,
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

		throw new UnsupportedOperationException(
				"Operation actually not supported");

	}

}
