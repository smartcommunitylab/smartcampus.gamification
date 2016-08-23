package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.service.IdentityLookupService;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.utils.Converter;

@RestController
public class GameController {

	@Autowired
	private GameService gameSrv;

	@Autowired
	private Converter converter;

	@Autowired
	private IdentityLookupService identityLookup;

	/**
	 * *************************************************************************
	 * GAME API
	 * ************************************************************************
	 * */

	// Create a game
	// POST /model/game
	// TODO: ­ Remove from body implicit elements (e.g., owner) , The content
	// elements are optional (except name)

	@RequestMapping(method = RequestMethod.POST, value = "/model/game")
	public GameDTO saveGame(@RequestBody GameDTO game) {
		// set creator
		String user = identityLookup.getName();
		game.setOwner(user);
		Game res = gameSrv.saveGameDefinition(converter.convertGame(game));
		return converter.convertGame(res);
	}

	//
	// Read a game
	// GET /model/game/{id}

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}")
	public GameDTO readGame(@PathVariable String gameId) {

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

	@RequestMapping(method = RequestMethod.GET, value = "/model/game")
	public List<GameDTO> readGames() {
		String user = identityLookup.getName();
		List<GameDTO> r = new ArrayList<GameDTO>();
		for (Game g : gameSrv.loadGameByOwner(user)) {
			r.add(converter.convertGame(g));
		}
		return r;
	}

	// Delete a game
	// DELETE /model/game/{id}

	@RequestMapping(method = RequestMethod.DELETE, value = "/model/game/{gameId}")
	public void deleteGame(@PathVariable String gameId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		gameSrv.deleteGame(gameId);
	}

	// Start a game
	// PUT /model/game/{id}/start

	@RequestMapping(method = RequestMethod.PUT, value = "/model/game/{gameId}/start")
	public void startGame(@PathVariable String gameId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		throw new UnsupportedOperationException(
				"Operation actually not supported");
	}

	// Stop a game
	// PUT /model/game/{id}/stop

	@RequestMapping(method = RequestMethod.PUT, value = "/model/game/{gameId}/stop")
	public void stopGame(@PathVariable String gameId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		throw new UnsupportedOperationException(
				"Operation actually not supported");
	}

	/**
	 * *************************************************************************
	 * ACTION API
	 * ************************************************************************
	 * */

	// Create action concept
	// POST /model/game/{id}/action/{actionId}
	// ­ Action should be unique. Error if exists
	// ­ Consider other fields: name, description

	@RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/action/{actionId}")
	public void addAction(@PathVariable String gameId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		throw new UnsupportedOperationException(
				"Operation actually not supported");
	}

	// Modify an action
	// PUT /model/game/{id}/action/{actionId}

	@RequestMapping(method = RequestMethod.PUT, value = "/model/game/{gameId}/action/{actionId}")
	public void editAction(@PathVariable String gameId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		throw new UnsupportedOperationException(
				"Operation actually not supported");
	}

	// Read all actions
	// GET /model/game/{id}/action

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/action")
	public Set<String> readAllAction(@PathVariable String gameId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		Game g = gameSrv.loadGameDefinitionById(gameId);
		return g != null ? g.getActions() : Collections.<String> emptySet();

	}

	// Read an action
	// GET /model/game/{id}/action/{actionId}

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/action/{actionId}")
	public void readAction(@PathVariable String gameId) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		throw new UnsupportedOperationException(
				"Operation actually not supported");
	}

	// Delete an action
	// DELETE /model/game/{id}/action/{actionId}
	@RequestMapping(method = RequestMethod.DELETE, value = "/model/game/{gameId}/action/{actionId}")
	public void deleteAction(@PathVariable String gameId,
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

}
