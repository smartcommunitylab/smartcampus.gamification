package eu.trentorise.game.api.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.GameService;

@RestController
@RequestMapping(value = "/console")
public class ConsoleController {

	@Autowired
	GameService gameSrv;

	@RequestMapping(method = RequestMethod.POST, value = "/game")
	public GameDTO saveGame(@RequestBody GameDTO game) {
		Game res = gameSrv.saveGameDefinition(game.toGame());
		return new GameDTO(res);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}")
	public GameDTO readGame(@PathVariable String gameId) {
		Game g = gameSrv.loadGameDefinitionById(gameId);
		return g == null ? null : new GameDTO(g);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game")
	public List<GameDTO> readGames() {
		List<GameDTO> r = new ArrayList<GameDTO>();
		for (Game g : gameSrv.loadAllGames()) {
			r.add(new GameDTO(g));
		}
		return r;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/game/{gameId}/point")
	public void addPoint(@PathVariable String gameId,
			@RequestBody PointConcept point) {
		gameSrv.addConceptInstance(gameId, point);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}/point")
	public List<PointConcept> readPoints(@PathVariable String gameId) {
		Set<GameConcept> concepts = gameSrv.readConceptInstances(gameId);
		List<PointConcept> points = new ArrayList<PointConcept>();
		if (concepts != null) {
			for (GameConcept gc : concepts) {
				if (gc instanceof PointConcept) {
					points.add((PointConcept) gc);
				}
			}
		}

		return points;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/game/{gameId}/badgecoll")
	public void addBadge(@PathVariable String gameId,
			@RequestBody BadgeCollectionConcept badge) {
		gameSrv.addConceptInstance(gameId, badge);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}/badgecoll")
	public List<BadgeCollectionConcept> readBadgeCollections(
			@PathVariable String gameId) {
		Set<GameConcept> concepts = gameSrv.readConceptInstances(gameId);
		List<BadgeCollectionConcept> badgeColl = new ArrayList<BadgeCollectionConcept>();
		if (concepts != null) {
			for (GameConcept gc : concepts) {
				if (gc instanceof BadgeCollectionConcept) {
					badgeColl.add((BadgeCollectionConcept) gc);
				}
			}
		}

		return badgeColl;
	}

}
