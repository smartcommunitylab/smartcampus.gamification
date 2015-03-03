package eu.trentorise.game.api.rest;

import java.util.List;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;

@RestController("/console")
public class ConsoleController {

	@RequestMapping(method = RequestMethod.POST, value = "/game")
	public Game saveGame(Game game) {
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}")
	public Game readGame(@PathVariable String gameId) {
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game")
	public List<Game> readGames() {
		return null;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/game/point")
	public void addPoint(PointConcept point) {

	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/point/{pointId}")
	public PointConcept readPoint(@PathVariable String pointId) {
		return null;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/point")
	public List<PointConcept> readPoints() {
		return null;
	}

}
