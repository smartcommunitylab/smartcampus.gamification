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

package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.bean.ActionDTO;
import eu.trentorise.game.bean.ClassificationDTO;
import eu.trentorise.game.bean.GameDTO;
import eu.trentorise.game.bean.GeneralClassificationDTO;
import eu.trentorise.game.bean.GetListResponse;
import eu.trentorise.game.bean.GetOneResponse;
import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.bean.LevelDTO;
import eu.trentorise.game.bean.PointConceptDTO;
import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.bean.TaskDTO;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.model.core.TimeUnit;
import eu.trentorise.game.repo.PlayerRepo;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.service.IdentityLookupService;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.task.GeneralClassificationTask;
import eu.trentorise.game.task.IncrementalClassificationTask;
import eu.trentorise.game.utils.Converter;
import eu.trentorise.game.utils.JsonDB;
import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping(value = "/console-ui")
@Profile({ "sec", "no-sec" })
public class InterfaceManagerController {

	@Autowired
	private GameService gameSrv;

	@Autowired
	private PlayerRepo playerRepo;

	@Autowired
	private TaskService taskSrv;

	@Autowired
	private Converter converter;

	@Autowired
	private IdentityLookupService identityLookup;

	@Autowired
	private JsonDB jsonDB;

	private ObjectMapper objectMapper = new ObjectMapper();

	@RequestMapping(method = RequestMethod.GET, value = "/game", produces = { "application/json" })
	public GetListResponse readGames(Pageable pageable) {
		String user = identityLookup.getName();
		List<GameDTO> r = new ArrayList<GameDTO>();
		for (Game g : gameSrv.loadGameByOwner(user)) {
			r.add(converter.convertGame(g));
		}

		int totalpages = r.size() / pageable.getPageSize();
		int max = pageable.getPageNumber() >= totalpages ? r.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<GameDTO> page = new PageImpl<GameDTO>(r.subList(min, max), pageable, r.size());

		return r == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/game/{gameId}", produces = { "application/json" })
	public GetOneResponse readGame(@PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		return g == null ? null : new GetOneResponse(converter.convertGame(g));
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/game/{gameId}", produces = { "application/json" })
	public GetListResponse deleteGame(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		gameSrv.deleteGame(gameId);
		String user = identityLookup.getName();
		List<GameDTO> r = new ArrayList<GameDTO>();
		for (Game g : gameSrv.loadGameByOwner(user)) {
			r.add(converter.convertGame(g));
		}

		int totalpages = r.size() / pageable.getPageSize();
		int max = pageable.getPageNumber() >= totalpages ? r.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<GameDTO> page = new PageImpl<GameDTO>(r.subList(min, max), pageable, r.size());

		return r == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/game", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public GetOneResponse createGame(@RequestParam("data") MultipartFile data) throws Exception {
		HashMap<String, Object> mapInFile = (HashMap<String, Object>) objectMapper.readValue(data.getBytes(),
				Map.class);
		String gameId = jsonDB.importDB(mapInFile);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		return g == null ? null : new GetOneResponse(converter.convertGame(g));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/game", consumes = { "application/json" }, produces = {
			"application/json" })
	public GetOneResponse editGame(@RequestBody GameDTO game) {
		// set creator
		String user = identityLookup.getName();
		game.setOwner(user);
		Game g = gameSrv.saveGameDefinition(converter.convertGame(game));
		return g == null ? null : new GetOneResponse(converter.convertGame(g));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/actions/{gameId}", produces = { "application/json" })
	public GetListResponse readActionList(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);

		List<ActionDTO> actions = new ArrayList<ActionDTO>();
		for (String action : g.getActions()) {
			actions.add(new ActionDTO(action));
		}

		int totalpages = actions.size() / pageable.getPageSize();

		int max = pageable.getPageNumber() >= totalpages ? actions.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<ActionDTO> page = new PageImpl<ActionDTO>(actions.subList(min, max), pageable, actions.size());

		return g == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/actions/{gameId}/{actions}", produces = {
			"application/json" })
	public GetListResponse deleteAction(@PathVariable String gameId, @PathVariable List<String> actions) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);

		g.getActions().removeAll(actions);

		Game res = gameSrv.saveGameDefinition(g);
		List<ActionDTO> actionUpdated = new ArrayList<ActionDTO>();
		for (String actionU : res.getActions()) {
			actionUpdated.add(new ActionDTO(actionU));
		}

		return g == null ? null : new GetListResponse(actionUpdated.size(), actionUpdated);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/actions/{gameId}/{actionName}", produces = {
			"application/json" })
	public GetOneResponse saveAction(@PathVariable String gameId, @PathVariable String actionName) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (!g.getActions().contains(actionName)) {
			g.getActions().add(actionName);
		} else {
			throw new IllegalArgumentException(
					String.format("Action %s already exists in game %s", actionName, gameId));
		}
		Game res = gameSrv.saveGameDefinition(g);
		List<ActionDTO> actions = new ArrayList<ActionDTO>();
		for (String action : res.getActions()) {
			actions.add(new ActionDTO(action));
		}
		return g == null ? null : new GetOneResponse(new ActionDTO(actionName));
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pointconcepts/{gameId}", produces = { "application/json" })
	public GetListResponse readPointConceptList(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);

		List<PointConceptDTO> pcs = new ArrayList<PointConceptDTO>();
		int i = 0;
		for (GameConcept gc : g.getConcepts()) {
			if (gc instanceof PointConcept) {
				pcs.add(new PointConceptDTO(String.valueOf(i), (PointConcept) gc));
			}
			i++;
		}

		int totalpages = pcs.size() / pageable.getPageSize();
		int max = pageable.getPageNumber() >= totalpages ? pcs.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<PointConceptDTO> page = new PageImpl<PointConceptDTO>(pcs.subList(min, max), pageable, pcs.size());

		return g == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pointconcepts/{gameId}/{ids}", produces = {
			"application/json" })
	public GetListResponse readPointConceptById(@PathVariable String gameId, @PathVariable List<String> ids) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);

		List<PointConceptDTO> pcs = new ArrayList<PointConceptDTO>();
		int i = 0;
		for (GameConcept gc : g.getConcepts()) {
			if (gc instanceof PointConcept) {
				pcs.add(new PointConceptDTO(String.valueOf(i), (PointConcept) gc));
			}
			i++;
		}

		List<PointConceptDTO> result = pcs.stream().filter(pc -> ids.contains(pc.getId())).collect(Collectors.toList());

		return g == null ? null : new GetListResponse(result.size(), result);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tasks/{gameId}", produces = { "application/json" })
	public GetListResponse readTask(@PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		GameDTO game = converter.convertGame(g);

		List<TaskDTO> tasks = new ArrayList<TaskDTO>();

		for (ClassificationDTO c : game.getClassificationTask()) {

			if (c instanceof GeneralClassificationDTO) {
				GeneralClassificationTask general = converter.convertClassificationTask((GeneralClassificationDTO) c);
				tasks.add(new TaskDTO(general.getName(), general));
			} else if (c instanceof IncrementalClassificationDTO) {
				IncrementalClassificationTask incremental = converter
						.convertClassificationTask((IncrementalClassificationDTO) c);
				tasks.add(new TaskDTO(incremental.getName(), incremental));
			}

		}
		return g == null ? null : new GetListResponse(tasks.size(), tasks);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/tasks/{gameId}/general", consumes = {
			"application/json" }, produces = { "application/json" })
	public GetOneResponse createGeneralTask(@PathVariable String gameId, @RequestBody GeneralClassificationDTO task) {
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
			return g == null ? null : new GetOneResponse(task);
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/tasks/{gameId}/general", consumes = {
			"application/json" }, produces = { "application/json" })
	public GetOneResponse editGeneralTask(@PathVariable String gameId, @RequestBody GeneralClassificationDTO task) {
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
			task.setId(task.getName()); // must have id inside data.
			return g == null ? null : new GetOneResponse(task);

		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.POST, value = "/tasks/{gameId}/incremental", consumes = {
			"application/json" }, produces = { "application/json" })
	public GetOneResponse createIncrementalTask(@PathVariable String gameId,
			@RequestBody IncrementalClassificationDTO task) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				IncrementalClassificationTask incClassification = converter.convertClassificationTask(task);
				incClassification.setName(task.getName());
				if (g.getTasks().contains(incClassification)) {
					throw new IllegalArgumentException("task name already exist");
				} else {
					g.getTasks().add(incClassification);
					gameSrv.saveGameDefinition(g);
					taskSrv.createTask(incClassification, gameId);
				}
				task.setGameId(gameId);
			}
			return g == null ? null : new GetOneResponse(task);
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/tasks/{gameId}/incremental", consumes = {
			"application/json" }, produces = { "application/json" })
	public GetOneResponse updateIncrementalTask(@PathVariable String gameId,
			@RequestBody IncrementalClassificationDTO task) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof IncrementalClassificationTask && gt.getName().equals(task.getName())) {

						IncrementalClassificationTask ct = (IncrementalClassificationTask) gt;
						ct.setItemsToNotificate(task.getItemsToNotificate());
						ct.setClassificationName(task.getClassificationName());
						if (StringUtils.isNotBlank(task.getDelayUnit())) {
							ct.getSchedule().setDelay(
									new TimeInterval(task.getDelayValue(), TimeUnit.valueOf(task.getDelayUnit())));
						} else {
							ct.getSchedule().setDelay(null);
						}
						// if itemType or periodName changes update schedule
						// data
						if (!ct.getPeriodName().equals(task.getPeriodName())
								|| !ct.getPointConceptName().equals(task.getItemType())) {
							// found pointConcept
							for (GameConcept gc : g.getConcepts()) {
								if (gc instanceof PointConcept && gc.getName().equals(task.getItemType())) {
									ct.updatePointConceptData((PointConcept) gc, task.getPeriodName(), null);
									break;
								}
							}
						}
						taskSrv.updateTask(gt, gameId);
					}
				}
				gameSrv.saveGameDefinition(g);
			}
			task.setId(task.getName()); // must have id inside data.
			return g == null ? null : new GetOneResponse(task);
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/tasks/{gameId}/{tasks}", produces = { "application/json" })
	public GetListResponse deleteTask(@PathVariable String gameId, @PathVariable List<String> tasks) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		List<GameTask> purgeTask = new ArrayList<GameTask>();

		for (String classificationId : tasks) {
			classificationId = decodePathVariable(classificationId);
			if (g != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof GeneralClassificationTask && gt.getName().equals(classificationId)) {
						purgeTask.add((GeneralClassificationTask) gt);
					}
				}
			} else {
				throw new IllegalArgumentException("game not exist");
			}
		}

		for (String classificationId : tasks) {
			classificationId = decodePathVariable(classificationId);
			if (g != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof IncrementalClassificationTask && gt.getName().equals(classificationId)) {
						purgeTask.add((IncrementalClassificationTask) gt);
					}
				}
			} else {
				throw new IllegalArgumentException("game not exist");
			}
		}

		g.getTasks().removeAll(purgeTask);
		gameSrv.saveGameDefinition(g);
		for (GameTask pT : purgeTask) {
			taskSrv.destroyTask(pT, gameId);
		}

		Game gUpdate = gameSrv.loadGameDefinitionById(gameId);
		GameDTO game = converter.convertGame(gUpdate);

		List<TaskDTO> taskUpdated = new ArrayList<TaskDTO>();

		for (ClassificationDTO c : game.getClassificationTask()) {

			if (c instanceof GeneralClassificationDTO) {
				GeneralClassificationTask general = converter.convertClassificationTask((GeneralClassificationDTO) c);
				taskUpdated.add(new TaskDTO(general.getName(), general));
			} else if (c instanceof IncrementalClassificationDTO) {
				IncrementalClassificationTask incremental = converter
						.convertClassificationTask((IncrementalClassificationDTO) c);
				taskUpdated.add(new TaskDTO(incremental.getName(), incremental));
			}

		}

		return g == null ? null : new GetListResponse(taskUpdated.size(), taskUpdated);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/tasks/{gameId}/{id}", produces = { "application/json" })
	public GetOneResponse readTask(@PathVariable String gameId, @PathVariable String id) {
		Game g = gameSrv.loadGameDefinitionById(gameId);
		String classificationId = decodePathVariable(id);
		GameDTO game = converter.convertGame(g);

		TaskDTO task = new TaskDTO();

		for (ClassificationDTO c : game.getClassificationTask()) {

			if (c instanceof GeneralClassificationDTO) {
				GeneralClassificationTask general = converter.convertClassificationTask((GeneralClassificationDTO) c);
				GeneralClassificationDTO generalObj = converter.convertClassificationTask(general);
				if (generalObj.getName().equalsIgnoreCase(classificationId)) {
					task = new TaskDTO(generalObj.getName(), generalObj);
					break;
				}
			} else if (c instanceof IncrementalClassificationDTO) {
				IncrementalClassificationTask incremental = converter
						.convertClassificationTask((IncrementalClassificationDTO) c);
				IncrementalClassificationDTO incrementalObj = converter.convertClassificationTask(gameId, incremental);
				if (incrementalObj.getName().equalsIgnoreCase(classificationId)) {
					task = new TaskDTO(incrementalObj.getName(), incrementalObj);
					break;
				}

			}

		}

		return g == null ? null : new GetOneResponse(task);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/badges/{gameId}", produces = { "application/json" })
	public GetListResponse readBadgeist(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Set<GameConcept> concepts = gameSrv.readConceptInstances(gameId);
		List<BadgeCollectionConcept> badgeColl = new ArrayList<BadgeCollectionConcept>();
		if (concepts != null) {
			for (GameConcept gc : concepts) {
				if (gc instanceof BadgeCollectionConcept) {
					gc.setId(gc.getName());
					badgeColl.add((BadgeCollectionConcept) gc);
				}
			}
		}

		int totalpages = badgeColl.size() / pageable.getPageSize();

		int max = pageable.getPageNumber() >= totalpages ? badgeColl.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<BadgeCollectionConcept> page = new PageImpl<BadgeCollectionConcept>(badgeColl.subList(min, max), pageable,
				badgeColl.size());

		return badgeColl == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/badges/{gameId}/{badges}", produces = {
			"application/json" })
	public GetListResponse deleteBadge(@PathVariable String gameId, @PathVariable List<String> badges) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		GameDTO gameDTO = converter.convertGame(g);
		List<BadgeCollectionConcept> purgeBadges = new ArrayList<BadgeCollectionConcept>();

		for (String badgeName : badges) {
			BadgeCollectionConcept temp = new BadgeCollectionConcept(badgeName);
			if (gameDTO.getBadgeCollectionConcept().contains(temp)) {
				purgeBadges.add(temp);
			}
		}

		gameDTO.getBadgeCollectionConcept().removeAll(purgeBadges);
		g = converter.convertGame(gameDTO);
		gameSrv.saveGameDefinition(g);
		gameDTO = converter.convertGame(g);

		return g == null ? null
				: new GetListResponse(gameDTO.getBadgeCollectionConcept().size(), gameDTO.getBadgeCollectionConcept());
	}

	@RequestMapping(method = RequestMethod.POST, value = "/badges/{gameId}", consumes = {
			"application/json" }, produces = { "application/json" })
	public GetOneResponse saveBadge(@PathVariable String gameId, @RequestBody BadgeCollectionConcept badge) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		GameDTO gameDTO = converter.convertGame(g);
		if (!gameDTO.getBadgeCollectionConcept().contains(badge)) {
			gameDTO.getBadgeCollectionConcept().add(badge);
		} else {
			throw new IllegalArgumentException("badge already exist");
		}
		gameSrv.saveGameDefinition(converter.convertGame(gameDTO));
		return gameId == null ? null : new GetOneResponse(badge);
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rules/{gameId}", produces = { "application/json" })
	public GetListResponse readRules(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Game game = gameSrv.loadGameDefinitionById(gameId);
		List<RuleDTO> rules = new ArrayList<RuleDTO>();
		if (game.getRules() != null) {
			for (String ruleUrl : game.getRules()) {
				RuleDTO r = new RuleDTO();
				r.setId(ruleUrl.substring(ruleUrl.indexOf("://") + 3));
				Rule rule = gameSrv.loadRule(game.getId(), ruleUrl);
				if (rule != null) {
					r.setName(rule.getName());
				}
				rules.add(r);
			}
		}

		int totalpages = rules.size() / pageable.getPageSize();

		int max = pageable.getPageNumber() >= totalpages ? rules.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<RuleDTO> page = new PageImpl<RuleDTO>(rules.subList(min, max), pageable, rules.size());

		return rules == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/rules/{gameId}/{ruleUrl}", produces = { "application/json" })
	public GetOneResponse readDbRule(@PathVariable String gameId, @PathVariable String ruleUrl) {
		gameId = decodePathVariable(gameId);
		ruleUrl = DBRule.URL_PROTOCOL + ruleUrl;
		DBRule r = (DBRule) gameSrv.loadRule(gameId, ruleUrl);
		RuleDTO res = new RuleDTO();
		res.setId(r.getId());
		res.setName(r.getName());
		res.setContent(r.getContent());
		return res == null ? null : new GetOneResponse(res);
	}

	@RequestMapping(method = RequestMethod.POST, value = "/rules/{gameId}", consumes = {
			"application/json" }, produces = { "application/json" })
	public GetOneResponse addRule(@PathVariable String gameId, @RequestBody RuleDTO rule) {
		gameId = decodePathVariable(gameId);
		DBRule r = new DBRule(gameId, rule.getContent());
		r.setName(rule.getName());
		r.setId(rule.getId());
		String ruleUrl = gameSrv.addRule(r);
		rule.setId(ruleUrl);
		RuleDTO res = new RuleDTO();
		res.setId(r.getId());
		res.setName(r.getName());
		res.setContent(r.getContent());
		return r == null ? null : new GetOneResponse(res);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/rules/{gameId}", consumes = {
			"application/json" }, produces = { "application/json" })
	public GetOneResponse updateRule(@PathVariable String gameId, @RequestBody RuleDTO rule) {
		gameId = decodePathVariable(gameId);
		DBRule r = new DBRule(gameId, rule.getContent());
		r.setName(rule.getName());
		r.setId(rule.getId());
		String ruleUrl = gameSrv.addRule(r);
		rule.setId(ruleUrl);
		RuleDTO res = new RuleDTO();
		res.setId(r.getId());
		res.setName(r.getName());
		res.setContent(r.getContent());
		return r == null ? null : new GetOneResponse(res);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/rules/{gameId}/{ruleUrls}", produces = {
			"application/json" })
	public GetListResponse deleteDbRule(@PathVariable String gameId, @PathVariable List<String> ruleUrls) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		GameDTO gameDTO = converter.convertGame(g);

		for (String rr : ruleUrls) {
			String ruleUrl = DBRule.URL_PROTOCOL + rr;
			gameSrv.deleteRule(gameId, ruleUrl);
		}

		return g == null ? null : new GetListResponse(gameDTO.getRules().size(), gameDTO.getRules());

	}

	@RequestMapping(method = RequestMethod.GET, value = "/levels/{gameId}", produces = { "application/json" })
	public GetListResponse readLevels(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Game game = gameSrv.loadGameDefinitionById(gameId);
		List<LevelDTO> levels = new ArrayList<LevelDTO>();
		if (game.getLevels() != null) {
			for (Level level : game.getLevels()) {
				LevelDTO l = converter.convert(level);
				levels.add(l);
			}
		}

		int totalpages = levels.size() / pageable.getPageSize();

		int max = pageable.getPageNumber() >= totalpages ? levels.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<LevelDTO> page = new PageImpl<LevelDTO>(levels.subList(min, max), pageable, levels.size());

		return levels == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/levels/{gameId}/{levelName}", produces = {
			"application/json" })
	public GetOneResponse readLevel(@PathVariable String gameId, @PathVariable String levelName) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		Level saved = g.getLevels().stream().filter(lev -> lev.getName().equals(levelName)).findFirst().orElse(null);
		return g == null ? null : new GetOneResponse(converter.convert(saved));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/levels/{gameId}", consumes = {
			"application/json" }, produces = { "application/json" })
	@Operation(summary = "Save a level")
	public GetOneResponse addLevel(@PathVariable String gameId, @RequestBody LevelDTO level) {
		Game game = gameSrv.loadGameDefinitionById(gameId);
		if (game.getLevels().stream().anyMatch(lev -> lev.getName().equals(level.getName()))) {
			throw new IllegalArgumentException(
					String.format("Level %s already exists in game %s", level.getName(), gameId));
		}
		game = gameSrv.upsertLevel(gameId, converter.convert(level));
		Level saved = game.getLevels().stream().filter(lev -> lev.getName().equals(level.getName())).findFirst()
				.orElse(null);
		return new GetOneResponse(converter.convert(saved));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/levels/{gameId}", produces = { "application/json" })
	@Operation(summary = "Update a level")
	public GetOneResponse updateLevel(@PathVariable String gameId, @RequestBody LevelDTO level) {
		Game game = gameSrv.upsertLevel(gameId, converter.convert(level));
		Level saved = game.getLevels().stream().filter(lev -> lev.getName().equals(level.getName())).findFirst()
				.orElse(null);
		return new GetOneResponse(converter.convert(saved));
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/levels/{gameId}/{levelName}", produces = {
			"application/json" })
	@Operation(summary = "Delete a level")
	public GetListResponse deleteLevel(@PathVariable String gameId, @PathVariable String levelName) {
		gameSrv.deleteLevel(gameId, levelName);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		GameDTO gameDTO = converter.convertGame(g);
		return g == null ? null : new GetListResponse(gameDTO.getLevels().size(), gameDTO.getLevels());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/challengemodels/{gameId}", produces = { "application/json" })
	@Operation(summary = "Read challenge models")
	public GetListResponse readChallengeModels(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		List<ChallengeModel> challengeModelList = new ArrayList<ChallengeModel>();
		challengeModelList.addAll(gameSrv.readChallengeModels(gameId));
		for (String groupChallengeName : GroupChallenge.MODELS) {
			ChallengeModel tmp = new ChallengeModel();
			tmp.setName(groupChallengeName);
			tmp.setGameId(gameId);
			challengeModelList.add(tmp);
		}

		int totalpages = challengeModelList.size() / pageable.getPageSize();

		int max = pageable.getPageNumber() >= totalpages ? challengeModelList.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();

		Page<ChallengeModel> page = new PageImpl<ChallengeModel>(challengeModelList.subList(min, max), pageable,
				challengeModelList.size());

		return challengeModelList == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/challengemodels/{gameId}/{ids}", produces = {
			"application/json" })
	public GetListResponse readChallengeModelsByIds(@PathVariable String gameId, @PathVariable List<String> ids) {
		gameId = decodePathVariable(gameId);
		List<ChallengeModel> challengeModelList = new ArrayList<ChallengeModel>();
		challengeModelList.addAll(gameSrv.readChallengeModels(gameId));
		for (String groupChallengeName : GroupChallenge.MODELS) {
			ChallengeModel tmp = new ChallengeModel();
			tmp.setName(groupChallengeName);
			tmp.setGameId(gameId);
			challengeModelList.add(tmp);
		}
		List<ChallengeModel> result = challengeModelList.stream().filter(ch -> ids.contains(ch.getName()))
				.collect(Collectors.toList());

		return gameId == null ? null : new GetListResponse(result.size(), result);
	}

//	@RequestMapping(method = RequestMethod.GET, value = "/challengemodels/{gameId}/{name}", produces = {
//			"application/json" })
//	public GetOneResponse singleChallengeModel(@PathVariable String gameId, @PathVariable String name) {
//		gameId = decodePathVariable(gameId);
//		Game g = gameSrv.loadGameDefinitionById(gameId);
//		ChallengeModel saved = gameSrv.readChallengeModels(gameId).stream()
//				.filter(chModel -> chModel.getName().equals(name)).findFirst().orElse(null);
//		return g == null ? null : new GetOneResponse(saved);
//	}

	@GetMapping(value = "/monitor/{gameId}")
	public GetListResponse readPlayerStates(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Page<StatePersistence> states = playerRepo.findByGameId(gameId, pageable);
		return states == null ? null : new GetListResponse(states.getSize(), states.getContent());
	}

}
