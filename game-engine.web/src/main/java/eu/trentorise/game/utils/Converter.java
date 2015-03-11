package eu.trentorise.game.utils;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Rule;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.ClassificationTask;

@Component
public class Converter {

	@Autowired
	private GameService gameSrv;

	public GameDTO convertGame(Game game) {
		GameDTO gDTO = null;
		if (game != null) {
			gDTO = new GameDTO();
			gDTO.setId(game.getId());
			gDTO.setActions(game.getActions());
			gDTO.setExpiration(game.getExpiration());
			gDTO.setName(game.getName());
			gDTO.setOwner(game.getOwner());

			if (game.getRules() != null) {
				gDTO.setRules(new HashSet<RuleDTO>());
				for (String ruleUrl : game.getRules()) {
					RuleDTO r = new RuleDTO();
					r.setId(ruleUrl);
					Rule rule = gameSrv.loadRule(game.getId(), ruleUrl);
					if (rule != null) {
						r.setName(rule.getName());
					}
					gDTO.getRules().add(r);
				}
			}

			gDTO.setTerminated(game.isTerminated());

			if (game.getConcepts() != null) {
				gDTO.setPointConcept(new HashSet<PointConcept>());
				gDTO.setBadgeCollectionConcept(new HashSet<BadgeCollectionConcept>());
				for (GameConcept gc : game.getConcepts()) {
					if (gc instanceof PointConcept) {
						gDTO.getPointConcept().add((PointConcept) gc);
					}

					if (gc instanceof BadgeCollectionConcept) {
						gDTO.getBadgeCollectionConcept().add(
								(BadgeCollectionConcept) gc);
					}
				}
			}

			if (game.getTasks() != null) {
				gDTO.setClassificationTask(new HashSet<ClassificationTask>());
				for (GameTask gt : game.getTasks()) {
					gDTO.getClassificationTask().add((ClassificationTask) gt);
				}
			}
		}
		return gDTO;
	}

	public Game convertGame(GameDTO game) {
		Game g = new Game();
		g.setId(game.getId());
		g.setActions(game.getActions());
		g.setExpiration(game.getExpiration());
		g.setName(game.getName());
		g.setOwner(game.getOwner());
		if (game.getRules() != null) {
			g.setRules(new HashSet<String>());
			for (RuleDTO r : game.getRules()) {
				g.getRules().add(r.getId());
			}
		}
		g.setTerminated(game.isTerminated());

		if (game.getPointConcept() != null) {
			g.setConcepts(new HashSet<GameConcept>());
			g.getConcepts().addAll(game.getPointConcept());
		}

		if (game.getBadgeCollectionConcept() != null) {
			if (g.getConcepts() == null) {
				g.setConcepts(new HashSet<GameConcept>());
			}
			g.getConcepts().addAll(game.getBadgeCollectionConcept());

		}

		if (game.getClassificationTask() != null) {
			g.setTasks(new HashSet<GameTask>());
			g.getTasks().addAll(game.getClassificationTask());
		}

		return g;
	}

}
