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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import eu.trentorise.game.bean.PlayerStateDTO;
import eu.trentorise.game.bean.PointConceptDTO;
import eu.trentorise.game.bean.RuleDTO;
import eu.trentorise.game.bean.TaskDTO;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.ResourceNotFoundException;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.model.core.TimeUnit;
import eu.trentorise.game.repo.ChallengeConceptPersistence;
import eu.trentorise.game.repo.ChallengeConceptRepo;
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
	private static final Logger logger = LoggerFactory.getLogger(InterfaceManagerController.class);

	@Autowired
	private GameService gameSrv;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private ChallengeConceptRepo challengeConceptRepo;

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
	public GetListResponse readGames(Pageable pageable, @RequestParam(required = false) String filter) {
		String user = identityLookup.getName();
		List<GameDTO> r = new ArrayList<GameDTO>();

		if (filter != null && !filter.isEmpty()) {
			for (Game g : gameSrv.loadGameByOwnerAndName(user, filter)) {
				r.add(converter.convertGame(g));
			}
		} else {
			for (Game g : gameSrv.loadGameByOwner(user)) {
				r.add(converter.convertGame(g));
			}
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
	public GetListResponse readPointConceptList(@PathVariable String gameId,
			@RequestParam(required = false) List<String> ids, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);

		List<PointConceptDTO> pcs = new ArrayList<PointConceptDTO>();
		for (GameConcept gc : g.getConcepts()) {
			if (gc instanceof PointConcept) {
				pcs.add(new PointConceptDTO(gc.getName(), (PointConcept) gc));
			}
		}

		// GetMany
		if (ids != null && !ids.isEmpty()) {
			pcs = pcs.stream().filter(pc -> ids.contains(pc.getId())).collect(Collectors.toList());
		}

		int totalpages = pcs.size() / pageable.getPageSize();
		int max = pageable.getPageNumber() >= totalpages ? pcs.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();
		Page<PointConceptDTO> page = new PageImpl<PointConceptDTO>(pcs.subList(min, max), pageable, pcs.size());
		return g == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/pointconcepts/{gameId}/{pointId}", produces = {
			"application/json" })
	@Operation(summary = "Get point")
	public GetOneResponse readPoint(@PathVariable String gameId, @PathVariable String pointId) {
		gameId = decodePathVariable(gameId);
		pointId = decodePathVariable(pointId);
		Set<GameConcept> concepts = gameSrv.readConceptInstances(gameId);
		PointConcept point = null;

		if (concepts != null) {
			for (GameConcept gc : concepts) {
				if (gc instanceof PointConcept && gc.getName().equals(pointId)) {
					point = (PointConcept) gc;
					point.setId(point.getName());
					break;
				}
			}
		}

		if (point == null) {
			throw new ResourceNotFoundException(String.format("pointId %s not exist in game %s", pointId, gameId));
		}

		return (new GetOneResponse(point));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/pointconcepts/{gameId}", consumes = {
			"application/json" }, produces = { "application/json" })
	@Operation(summary = "Add point")
	public GetOneResponse addPoint(@PathVariable String gameId, @RequestBody PointConcept point) {
		gameId = decodePathVariable(gameId);
		gameSrv.addConceptInstance(gameId, point);
		return gameId == null ? null : new GetOneResponse(point);
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/pointconcepts/{gameId}/{pointId}", produces = {
			"application/json" })
	@Operation(summary = "Delete point")
	public GetListResponse deletePoint(@PathVariable String gameId, @PathVariable List<String> pointId) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);

		if (g != null) {
			for (String pcName : pointId) {
				for (Iterator<GameConcept> iter = g.getConcepts().iterator(); iter.hasNext();) {
					GameConcept gc = iter.next();
					if (gc instanceof PointConcept && pcName.equals(gc.getName())) {
						iter.remove();
						break;
					}
				}
				gameSrv.saveGameDefinition(g);
			}
		}

		g = gameSrv.loadGameDefinitionById(gameId);
		List<PointConceptDTO> pcs = new ArrayList<PointConceptDTO>();

		for (GameConcept gc : g.getConcepts()) {
			if (gc instanceof PointConcept) {
				pcs.add(new PointConceptDTO(gc.getName(), (PointConcept) gc));
			}
		}

		return new GetListResponse(pcs.size(), pcs);
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
			task.setId(task.getName());
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

						// create if not exist 'schedule'
						if (ct.getSchedule() == null) {
							TaskSchedule schedule = new TaskSchedule();
							gt.setSchedule(schedule);									
						}
						
						if (StringUtils.isNotBlank(task.getDelayUnit())) {
							ct.getSchedule().setDelay(
									new TimeInterval(task.getDelayValue(), TimeUnit.valueOf(task.getDelayUnit())));
						} else {
							ct.getSchedule().setDelay(null);
						}

						if (StringUtils.isNotBlank(task.getPeriodName())) {
							if (ct.getPeriodName() == null || !ct.getPeriodName().equals(task.getPeriodName())
									|| !ct.getPointConceptName().equals(task.getItemType())) {
								// found pointConcept
								for (GameConcept gc : g.getConcepts()) {
									if (gc instanceof PointConcept && gc.getName().equals(task.getItemType())) {
										ct.updatePointConceptData((PointConcept) gc, task.getPeriodName(), null);
										break;
									}
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

	@RequestMapping(method = RequestMethod.DELETE, value = "/levels/{gameId}/{levels}", produces = {
			"application/json" })
	@Operation(summary = "Delete a level")
	public GetListResponse deleteLevel(@PathVariable String gameId, @PathVariable List<String> levels) {
		gameId = decodePathVariable(gameId);

		for (String levelName : levels) {
			gameSrv.deleteLevel(gameId, levelName);
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		GameDTO gameDTO = converter.convertGame(g);
		return g == null ? null : new GetListResponse(gameDTO.getLevels().size(), gameDTO.getLevels());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/challengemodels/{gameId}", produces = { "application/json" })
	@Operation(summary = "Read challenge models")
	public GetListResponse readChallengeModels(@PathVariable String gameId,
			@RequestParam(required = false) List<String> ids, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		List<ChallengeModel> challengeModelList = new ArrayList<ChallengeModel>();
		challengeModelList.addAll(gameSrv.readChallengeModels(gameId));

		for (String groupChallengeName : GroupChallenge.MODELS) {
			ChallengeModel tmp = new ChallengeModel();
			tmp.setName(groupChallengeName);
			tmp.setGameId(gameId);
			tmp.setId(groupChallengeName);
			challengeModelList.add(tmp);
		}

		// GetMany
		if (ids != null && !ids.isEmpty()) {
			challengeModelList = challengeModelList.stream().filter(ch -> ids.contains(ch.getName()))
					.collect(Collectors.toList());
		}

		int totalpages = challengeModelList.size() / pageable.getPageSize();
		int max = pageable.getPageNumber() >= totalpages ? challengeModelList.size()
				: pageable.getPageSize() * (pageable.getPageNumber() + 1);
		int min = pageable.getPageNumber() > totalpages ? max : pageable.getPageSize() * pageable.getPageNumber();
		Page<ChallengeModel> page = new PageImpl<ChallengeModel>(challengeModelList.subList(min, max), pageable,
				challengeModelList.size());
		return challengeModelList == null ? null : new GetListResponse(page.getSize(), page.getContent());
	}

	@RequestMapping(method = RequestMethod.GET, value = "/challengemodels/{gameId}/{id}", produces = {
			"application/json" })
	public GetOneResponse singleChallengeModel(@PathVariable String gameId, @PathVariable String id) {
		gameId = decodePathVariable(gameId);
		Game g = gameSrv.loadGameDefinitionById(gameId);
		return g == null ? null : new GetOneResponse(gameSrv.readChallengeModel(gameId, id));
	}

	@RequestMapping(method = RequestMethod.POST, value = "/challengemodels/{gameId}", consumes = {
			"application/json" }, produces = { "application/json" })
	@Operation(summary = "Add challenge model")
	public GetOneResponse createChallengeModel(@RequestBody ChallengeModel challengeModel,
			@PathVariable String gameId) {
		gameId = decodePathVariable(gameId);
		return new GetOneResponse(gameSrv.saveChallengeModel(gameId, challengeModel));
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/challengemodels/{gameId}", produces = { "application/json" })
	@Operation(summary = "Update a challenge model")
	public GetOneResponse updateLevel(@PathVariable String gameId, @RequestBody ChallengeModel challengeModel) {
		gameId = decodePathVariable(gameId);
		return new GetOneResponse(gameSrv.saveChallengeModel(gameId, challengeModel));
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/challengemodels/{gameId}/{modelIds}", produces = {
			"application/json" })
	@Operation(summary = "Delete challenge model")
	public GetListResponse deleteChallengeModels(@PathVariable String gameId, @PathVariable List<String> modelIds) {
		gameId = decodePathVariable(gameId);
		for (String modelId : modelIds) {
			gameSrv.deleteChallengeModel(gameId, modelId);
		}
		List<ChallengeModel> challengeModelList = new ArrayList<ChallengeModel>();
		challengeModelList.addAll(gameSrv.readChallengeModels(gameId));
		return challengeModelList == null ? null : new GetListResponse(challengeModelList.size(), challengeModelList);
	}

	@GetMapping(value = "/monitor/{gameId}")
	public GetListResponse readPlayerStates(@PathVariable String gameId, Pageable pageable) {
		gameId = decodePathVariable(gameId);
		Page<PlayerState> playerStates = playerSrv.loadStates(gameId, pageable, true, false);
		List<PlayerStateDTO> monitors = new ArrayList<PlayerStateDTO>();
		for (PlayerState ps : playerStates) {
			PlayerStateDTO temp = converter.convertPlayerState(ps);
			temp.setId(ps.getPlayerId());
			monitors.add(temp);
		}
		return monitors == null ? null : new GetListResponse(monitors.size(), monitors);
	}

	@GetMapping(value = "/monitor/{gameId}/{playerId}")
	public GetOneResponse readPlayerState(@PathVariable String gameId, @PathVariable String playerId) {
		gameId = decodePathVariable(gameId);
		playerId = decodePathVariable(playerId);
		PlayerStateDTO playerState = converter
				.convertPlayerState(playerSrv.loadState(gameId, playerId, true, true, false));
		playerState.setId(playerState.getPlayerId());
		return new GetOneResponse(playerState);
	}

	@DeleteMapping("/challenges/{gameId}/{playerId}/challenge/{instanceName}")
	public GetOneResponse deleteChallenge(@PathVariable String gameId, @PathVariable String playerId,
			@PathVariable String instanceName) {
		gameId = decodePathVariable(gameId);
		final String decodedPlayerId = decodePathVariable(playerId);
		final String decodedInstanceName = decodePathVariable(instanceName);
		PlayerState state = playerSrv.loadState(gameId, playerId, false, false);
		List<ChallengeConceptPersistence> listCcs = challengeConceptRepo.findByGameIdAndPlayerId(gameId, playerId);
		state.loadChallengeConcepts(listCcs);
		Optional<ChallengeConcept> removed = state.removeChallenge(decodedInstanceName);
		ChallengeConceptPersistence saved = challengeConceptRepo.findByGameIdAndPlayerIdAndName(gameId, playerId,
				instanceName);
		challengeConceptRepo.delete(saved);

		if (removed.isPresent()) {
			playerSrv.saveState(state);
			LogHub.info(gameId, logger, "removed challenge {} of player {}", instanceName, playerId);
			return new GetOneResponse(removed.get());
		}

		throw new IllegalArgumentException(String.format("challenge %s doesn't exist in state of player %s",
				decodedInstanceName, decodedPlayerId));
	}

}
