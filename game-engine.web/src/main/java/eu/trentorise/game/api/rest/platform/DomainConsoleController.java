/**
 * Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package eu.trentorise.game.api.rest.platform;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.GeneralClassificationDTO;
import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.bean.LevelDTO;
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.bean.TeamDTO;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.model.core.TimeUnit;
import eu.trentorise.game.service.IdentityLookupService;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.task.GeneralClassificationTask;
import eu.trentorise.game.task.IncrementalClassificationTask;
import eu.trentorise.game.utils.Converter;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/consoleapi/{domain}")
@Profile("platform")
public class DomainConsoleController {

	@Autowired
	private GameService gameSrv;

	@Autowired
	private TaskService taskSrv;

	@Autowired
	private GameEngine gameEngine;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private Converter converter;

	@Autowired
	private IdentityLookupService identityLookup;

	@RequestMapping(method = RequestMethod.POST, value = "/console/game", consumes = {
			"application/json" }, produces = { "application/json" })
	public GameDTO saveGame(@PathVariable String domain, @RequestBody GameDTO game) {
		// set creator
		String user = identityLookup.getName();
		game.setOwner(user);
		game.setDomain(domain);
		Game res = gameSrv.saveGameDefinition(converter.convertGame(game));
		return converter.convertGame(res);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/console/game/{gameId}", produces = { "application/json" })
	public GameDTO readGame(@PathVariable String domain, @PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		return g == null ? null : converter.convertGame(g);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/console/game/{gameId}", produces = { "application/json" })
	public void deleteGame(@PathVariable String domain, @PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
		gameSrv.deleteGame(gameId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/console/game", produces = { "application/json" })
	public List<GameDTO> readGames(@PathVariable String domain) {
		String user = identityLookup.getName();
		List<GameDTO> r = new ArrayList<GameDTO>();
		for (Game g : gameSrv.loadGameByOwner(domain, user)) {
			r.add(converter.convertGame(g));
		}
		return r;
	}

    @RequestMapping(method = RequestMethod.GET, value = "/console/game-by-domain",
            produces = {"application/json"})
    public List<GameDTO> readGamesByDomain(@PathVariable String domain) {
        List<GameDTO> r = new ArrayList<GameDTO>();
        for (Game g : gameSrv.loadGameByDomain(domain)) {
            r.add(converter.convertGame(g));
        }
        return r;
    }

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/point", consumes = {
			"application/json" }, produces = { "application/json" })
	public void addPoint(@PathVariable String domain, @PathVariable String gameId, @RequestBody PointConcept point) {
		gameId = decodePathVariable(gameId);
		gameSrv.addConceptInstance(gameId, point);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/console/game/{gameId}/point", produces = {
			"application/json" })
	public List<PointConcept> readPoints(@PathVariable String domain, @PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
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

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/badgecoll", consumes = {
			"application/json" }, produces = { "application/json" })
	public void addBadge(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody BadgeCollectionConcept badge) {
		gameId = decodePathVariable(gameId);
		gameSrv.addConceptInstance(gameId, badge);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/console/game/{gameId}/badgecoll", produces = { "application/json" })
	public List<BadgeCollectionConcept> readBadgeCollections(@PathVariable String domain, @PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
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

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/rule/db", consumes = {
			"application/json" }, produces = { "application/json" })
	public RuleDTO addRule(@PathVariable String domain, @PathVariable String gameId, @RequestBody RuleDTO rule) {
		gameId = decodePathVariable(gameId);
		DBRule r = new DBRule(gameId, rule.getContent());
		r.setName(rule.getName());
		r.setId(rule.getId());
		String ruleUrl = gameSrv.addRule(r);
		rule.setId(ruleUrl);
		return rule;

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/game/{gameId}/rule/db/{ruleUrl}", produces = {
			"application/json" })
	public boolean deleteDbRule(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String ruleUrl) {
		gameId = decodePathVariable(gameId);
		ruleUrl = DBRule.URL_PROTOCOL + ruleUrl;
		return gameSrv.deleteRule(gameId, ruleUrl);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/console/game/{gameId}/rule/db/{ruleUrl}", produces = {
			"application/json" })
	public RuleDTO readDbRule(@PathVariable String domain, @PathVariable String gameId, @PathVariable String ruleUrl) {
		gameId = decodePathVariable(gameId);
		ruleUrl = DBRule.URL_PROTOCOL + ruleUrl;
		DBRule r = (DBRule) gameSrv.loadRule(gameId, ruleUrl);
		RuleDTO res = new RuleDTO();
		res.setId(r.getId());
		res.setName(r.getName());
		res.setContent(r.getContent());
		return res;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/task", consumes = {
			"application/json" }, produces = { "application/json" })
	public GeneralClassificationDTO addClassificationTask(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody GeneralClassificationDTO task) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() == null) {
				g.setTasks(new HashSet<GameTask>());
			}
			GeneralClassificationTask t = converter.convertClassificationTask(task);
			t.setName(task.getName());
			if (g.getTasks().contains(t)) {
				throw new IllegalArgumentException("task name already exist");
			} else {
				g.getTasks().add(t);
				gameSrv.saveGameDefinition(g);
				taskSrv.createTask(t, gameId);
			}
			task.setGameId(gameId);
			return task;
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/task/del", consumes = {
			"application/json" }, produces = { "application/json" })
	public void deleteClassificationTask(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody GeneralClassificationDTO task) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				GeneralClassificationTask t = converter.convertClassificationTask(task);
				t.setName(task.getName());
				g.getTasks().remove(t);
				gameSrv.saveGameDefinition(g);
				taskSrv.destroyTask(t, gameId);
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/console/game/{gameId}/task", consumes = {
			"application/json" }, produces = { "application/json" })
	public void editClassificationTask(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody GeneralClassificationDTO task) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof GeneralClassificationTask && gt.getName().equals(task.getName())) {
						GeneralClassificationTask t = converter.convertClassificationTask(task);
						GeneralClassificationTask ct = (GeneralClassificationTask) gt;
						ct.setItemsToNotificate(t.getItemsToNotificate());
						ct.setClassificationName(t.getClassificationName());
						ct.setItemType(t.getItemType());
						ct.setSchedule(t.getSchedule());
						taskSrv.updateTask(gt, gameId);
					}
				}
				gameSrv.saveGameDefinition(g);
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/player", consumes = {
			"application/json" }, produces = { "application/json" })
	public void createPlayer(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody PlayerStateDTO player) {
		gameId = decodePathVariable(gameId);
		// check if player already exists
		if (playerSrv.loadState(gameId, player.getPlayerId(), false, false) != null) {
			throw new IllegalArgumentException(
					String.format("Player %s already exists in game %s", player.getPlayerId(), gameId));
		}

		player.setGameId(gameId);
		PlayerState p = converter.convertPlayerState(player);
		playerSrv.saveState(p);
		StatsLogger.logUserCreation(domain, gameId, player.getPlayerId(), UUID.randomUUID().toString(),
				System.currentTimeMillis());
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/console/game/{gameId}/player/{playerId}", produces = {
			"application/json" })
	public void deletePlayer(@PathVariable String domain, @PathVariable String gameId, @PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		playerSrv.deleteState(gameId, playerId);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/console/game/{gameId}/player/{playerId}", consumes = {
			"application/json" }, produces = { "application/json" })
	public PlayerStateDTO updateCustomData(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String playerId, @RequestBody Map<String, Object> customData) {

		PlayerState state = playerSrv.loadState(gameId, playerId, false, false);

		if (state == null) {
			throw new IllegalArgumentException(String.format("player %s doesn't exist in game %s", playerId, gameId));
		} else {
			state.getCustomData().putAll(customData);
			state = playerSrv.updateCustomData(gameId, playerId, state.getCustomData());
			return converter.convertPlayerState(state);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/team", consumes = {
			"application/json" }, produces = { "application/json" })
	public void createTeam(@PathVariable String domain, @PathVariable String gameId, @RequestBody TeamDTO team) {
		gameId = decodePathVariable(gameId);
		// check if player already exists
		if (playerSrv.readTeam(gameId, team.getPlayerId()) != null) {
			throw new IllegalArgumentException(
					String.format("Team %s already exists in game %s", team.getPlayerId(), gameId));
		}

		team.setGameId(gameId);
		TeamState t = converter.convertTeam(team);
		playerSrv.saveTeam(t);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/console/game/{gameId}/team/{teamId}", produces = {
			"application/json" })
	public void deleteTeam(@PathVariable String domain, @PathVariable String gameId, @PathVariable String teamId) {
		gameId = decodePathVariable(gameId);
		teamId = decodePathVariable(teamId);
		deletePlayer(domain, gameId, teamId);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/console/game/{gameId}/player/{playerId}/teams", produces = {
			"application/json" })
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

	@RequestMapping(method = RequestMethod.POST, value = "/console/game/{gameId}/team/{teamId}/members", consumes = {
			"application/json" }, produces = { "application/json" })
	public void updateTeamMembers(@PathVariable String domain, @PathVariable String gameId, @PathVariable String teamId,
			@RequestBody List<String> members) {

		gameId = decodePathVariable(gameId);
		teamId = decodePathVariable(teamId);
		TeamState team = playerSrv.readTeam(gameId, teamId);
		if (team != null) {
			team.setMembers(members);
			playerSrv.saveTeam(team);
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rule/validate", consumes = {
			"application/json" }, produces = { "application/json" })
	public List<String> validateRule(@PathVariable String domain, @RequestBody String ruleContent) {
		return gameEngine.validateRule(null, ruleContent);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/challenge", consumes = {
			"application/json" }, produces = { "application/json" })
	@Operation(summary = "Add challenge model")
	public ChallengeModel saveGame(@RequestBody ChallengeModel challengeModel, @PathVariable String domain,
			@PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
		return gameSrv.saveChallengeModel(gameId, challengeModel);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/challenge", produces = {
			"application/json" })
	@Operation(summary = "Get challenge models")
	public Set<ChallengeModel> readChallengeModels(@PathVariable String domain, @PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
		return gameSrv.readChallengeModels(gameId);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/model/game/{gameId}/challenge/{modelId}", produces = {
			"application/json" })
	@Operation(summary = "Delete challenge model")
	public void deleteChallengeModels(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String modelId) {
		gameId = decodePathVariable(gameId);
		gameSrv.deleteChallengeModel(gameId, modelId);
	}

	/*
	 * INCREMENTAL CLASSIFICATIONS
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/incclassification", consumes = {
			"application/json" }, produces = { "application/json" })
	@Operation(summary = "Add incremental classification definition")
	public IncrementalClassificationDTO createIncremental(@PathVariable String domain, @PathVariable String gameId,
			@RequestBody IncrementalClassificationDTO classification) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			classification.setGameId(gameId);
			if (g.getTasks() != null) {
				IncrementalClassificationTask incClassification = converter.convertClassificationTask(classification);
				incClassification.setName(classification.getName());
				if (g.getTasks().contains(incClassification)) {
					throw new IllegalArgumentException("task name already exist");
				} else {
					g.getTasks().add(incClassification);
					gameSrv.saveGameDefinition(g);
					taskSrv.createTask(incClassification, gameId);
				}
				classification.setGameId(gameId);
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}
		return classification;
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/model/game/{gameId}/incclassification/{classificationId}", consumes = {
			"application/json" }, produces = { "application/json" })
	@Operation(summary = "Edit general classification definition")
	public void updateIncrementalClassification(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String classificationId, @RequestBody IncrementalClassificationDTO classification) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			classification.setGameId(gameId);
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof IncrementalClassificationTask && gt.getName().equals(classificationId)) {

						IncrementalClassificationTask ct = (IncrementalClassificationTask) gt;
						ct.setItemsToNotificate(classification.getItemsToNotificate());
						ct.setClassificationName(classification.getClassificationName());
						if (StringUtils.isNotBlank(classification.getDelayUnit())) {
							ct.getSchedule().setDelay(new TimeInterval(classification.getDelayValue(),
									TimeUnit.valueOf(classification.getDelayUnit())));
						} else {
							ct.getSchedule().setDelay(null);
						}
						// if itemType or periodName changes update schedule
						// data
						if (!ct.getPeriodName().equals(classification.getPeriodName())
								|| !ct.getPointConceptName().equals(classification.getItemType())) {
							// found pointConcept
							for (GameConcept gc : g.getConcepts()) {
								if (gc instanceof PointConcept && gc.getName().equals(classification.getItemType())) {
									ct.updatePointConceptData((PointConcept) gc, classification.getPeriodName(), null);
									break;
								}
							}
						}
						taskSrv.updateTask(gt, gameId);
					}
				}
				gameSrv.saveGameDefinition(g);
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/model/game/{gameId}/incclassification/{classificationId}", produces = {
			"application/json" })
	@Operation(summary = "Delete incremental classification definition")
	public void deleteIncremental(@PathVariable String domain, @PathVariable String gameId,
			@PathVariable String classificationId) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof IncrementalClassificationTask && gt.getName().equals(classificationId)) {
						g.getTasks().remove((IncrementalClassificationTask) gt);
						gameSrv.saveGameDefinition(g);
						taskSrv.destroyTask(gt, gameId);
						break;
					}
				}
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/level", produces = {
			"application/json" })
	@Operation(summary = "Save a level")
	public LevelDTO saveLevel(@PathVariable String gameId, @RequestBody LevelDTO level) {
		Game game = gameSrv.upsertLevel(gameId, converter.convert(level));

		Level saved = game.getLevels().stream().filter(lev -> lev.getName().equals(level.getName())).findFirst()
				.orElse(null);
		return converter.convert(saved);

	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/model/game/{gameId}/level/{levelName}", produces = {
			"application/json" })
	@Operation(summary = "Delete a level")
	public boolean deleteLevel(@PathVariable String gameId, @PathVariable String levelName) {
		gameSrv.deleteLevel(gameId, levelName);
		return true;

	}

}
