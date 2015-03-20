package eu.trentorise.game.api.rest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.bean.TaskDTO;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.ClassificationTask;
import eu.trentorise.game.utils.Converter;

@RestController
@RequestMapping(value = "/console")
public class ConsoleController {

	@Autowired
	GameService gameSrv;

	@Autowired
	Converter converter;

	@RequestMapping(method = RequestMethod.POST, value = "/game")
	public GameDTO saveGame(@RequestBody GameDTO game) {
		// set creator
		String user = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		game.setOwner(user);
		Game res = gameSrv.saveGameDefinition(converter.convertGame(game));
		return converter.convertGame(res);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}")
	public GameDTO readGame(@PathVariable String gameId) {
		Game g = gameSrv.loadGameDefinitionById(gameId);
		return g == null ? null : converter.convertGame(g);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/game/{gameId}")
	public void deleteGame(@PathVariable String gameId) {
		gameSrv.deleteGame(gameId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game")
	public List<GameDTO> readGames() {

		List<GameDTO> r = new ArrayList<GameDTO>();
		for (Game g : gameSrv.loadAllGames()) {
			r.add(converter.convertGame(g));
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

	@RequestMapping(method = RequestMethod.POST, value = "/game/{gameId}/rule/db")
	public RuleDTO addRule(@PathVariable String gameId,
			@RequestBody RuleDTO rule) {
		DBRule r = new DBRule(gameId, rule.getContent());
		r.setName(rule.getName());
		r.setId(rule.getId());
		String ruleUrl = gameSrv.addRule(r);
		rule.setId(ruleUrl);
		return rule;

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/game/{gameId}/rule/db/{ruleUrl}")
	public boolean deleteDbRule(@PathVariable String gameId,
			@PathVariable String ruleUrl) {
		ruleUrl = "db://" + ruleUrl;
		return gameSrv.deleteRule(gameId, ruleUrl);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}/rule/db/{ruleUrl}")
	public RuleDTO readDbRule(@PathVariable String gameId,
			@PathVariable String ruleUrl) {
		ruleUrl = "db://" + ruleUrl;
		DBRule r = (DBRule) gameSrv.loadRule(gameId, ruleUrl);
		RuleDTO res = new RuleDTO();
		res.setId(r.getId());
		res.setName(r.getName());
		res.setContent(r.getContent());
		return res;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/game/{gameId}/task")
	public TaskDTO addClassificationTask(@PathVariable String gameId,
			@RequestBody TaskDTO task) {
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() == null) {
				g.setTasks(new HashSet<GameTask>());
			}
			ClassificationTask t = converter.convertClassificationTask(task);
			t.setName(task.getName());
			if (g.getTasks().contains(t)) {
				throw new IllegalArgumentException("task name already exist");
			} else {
				g.getTasks().add(t);
				gameSrv.saveGameDefinition(g);
			}
			task.setGameId(gameId);
			return task;
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/game/{gameId}/task/del")
	public void deleteClassificationTask(@PathVariable String gameId,
			@RequestBody TaskDTO task) {
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				ClassificationTask t = converter
						.convertClassificationTask(task);
				t.setName(task.getName());
				g.getTasks().remove(t);
				gameSrv.saveGameDefinition(g);
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/game/{gameId}/task")
	public void editClassificationTask(@PathVariable String gameId,
			@RequestBody TaskDTO task) {
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof ClassificationTask
							&& gt.getName().equals(task.getName())) {
						ClassificationTask t = converter
								.convertClassificationTask(task);
						ClassificationTask ct = (ClassificationTask) gt;
						ct.setItemsToNotificate(t.getItemsToNotificate());
						ct.setClassificationName(t.getClassificationName());
						ct.setItemType(t.getItemType());
						ct.setSchedule(t.getSchedule());
					}
				}
				gameSrv.saveGameDefinition(g);
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}
}
