package eu.trentorise.game.api.rest;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.GeneralClassificationDTO;
import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.core.ResourceNotFoundException;
import eu.trentorise.game.managers.ClassificationFactory;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ClassificationBoard;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.model.core.TimeUnit;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.task.GeneralClassificationTask;
import eu.trentorise.game.task.IncrementalClassificationTask;
import eu.trentorise.game.utils.Converter;

@RestController
public class ClassificationController {

	@Autowired
	private GameService gameSrv;

	@Autowired
	private TaskService taskSrv;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private Converter converter;

	/*
	 * GENERAL CLASSIFICATIONS
	 */

	// Create classification
	// POST /model/game/{id}/classification

	@RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/classification")
	public GeneralClassificationDTO addClassificationTask(
			@PathVariable String gameId,
			@RequestBody GeneralClassificationDTO task) {

		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() == null) {
				g.setTasks(new HashSet<GameTask>());
			}
			GeneralClassificationTask t = converter
					.convertClassificationTask(task);
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

	// Update classification
	// PUT /model/game/{id}/classification/{classificationId}

	@RequestMapping(method = RequestMethod.PUT, value = "/model/game/{gameId}/classification/{classificationId}")
	public void editClassificationTask(@PathVariable String gameId,
			@PathVariable String classificationId,
			@RequestBody GeneralClassificationDTO task) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		try {
			classificationId = URLDecoder.decode(classificationId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"classificationId is not UTF-8 encoded");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof GeneralClassificationTask
							&& gt.getName().equals(task.getName())) {
						GeneralClassificationTask t = converter
								.convertClassificationTask(task);
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

	// Read all classifications
	// GET /model/game/{id}/classification

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/classification")
	public List<GeneralClassificationDTO> readAllGeneralClassifications(
			@PathVariable String gameId) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		List<GeneralClassificationDTO> result = new ArrayList<>();
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask c : g.getTasks()) {
					if (c instanceof GeneralClassificationTask) {
						result.add(converter
								.convertClassificationTask((GeneralClassificationTask) c));
					}
				}
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}

		return result;
	}

	// Read a classification
	// GET /model/game/{id}/classification/{classificationId}

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/classification/{classificationId}")
	public GeneralClassificationDTO readGeneralClassification(
			@PathVariable String gameId, @PathVariable String classificationId) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		try {
			classificationId = URLDecoder.decode(classificationId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"classificationId is not UTF-8 encoded");
		}

		List<GeneralClassificationDTO> result = readAllGeneralClassifications(gameId);
		for (GeneralClassificationDTO r : result) {
			if (r.getName().equals(classificationId)) {
				return r;
			}
		}

		throw new ResourceNotFoundException(String.format(
				"classificationId %s not found in game %s", classificationId,
				gameId));
	}

	// Delete a classification
	// DELETE /model/game/{id}/classification/{classificationId}

	@RequestMapping(method = RequestMethod.DELETE, value = "/model/game/{gameId}/task/{classificationId}")
	public void deleteClassificationTask(@PathVariable String gameId,
			@PathVariable String classificationId) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		try {
			classificationId = URLDecoder.decode(classificationId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"classificationId is not UTF-8 encoded");
		}
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				GeneralClassificationTask t = new GeneralClassificationTask();
				t.setName(classificationId); // consider classification name as
												// ID
				g.getTasks().remove(t);
				gameSrv.saveGameDefinition(g);
				taskSrv.destroyTask(t, gameId);
			}
		} else {
			throw new IllegalArgumentException("game not exist");
		}
	}

	/*
	 * INCREMENTAL CLASSIFICATIONS
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/model/game/{gameId}/incclassification")
	public IncrementalClassificationDTO create(@PathVariable String gameId,
			@RequestBody IncrementalClassificationDTO classification) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			classification.setGameId(gameId);
			if (g.getTasks() != null) {
				IncrementalClassificationTask incClassification = converter
						.convertClassificationTask(classification);
				incClassification.setName(classification.getName());
				if (g.getTasks().contains(incClassification)) {
					throw new IllegalArgumentException(
							"task name already exist");
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

	@RequestMapping(method = RequestMethod.PUT, value = "/model/game/{gameId}/incclassification/{classificationId}")
	public void updateIncrementalClassification(@PathVariable String gameId,
			@PathVariable String classificationId,
			@RequestBody IncrementalClassificationDTO classification) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			classification.setGameId(gameId);
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof IncrementalClassificationTask
							&& gt.getName().equals(classificationId)) {

						IncrementalClassificationTask ct = (IncrementalClassificationTask) gt;
						ct.setItemsToNotificate(classification
								.getItemsToNotificate());
						ct.setClassificationName(classification
								.getClassificationName());
						if (StringUtils.isNotBlank(classification
								.getDelayUnit())) {
							ct.getSchedule().setDelay(
									new TimeInterval(classification
											.getDelayValue(), TimeUnit
											.valueOf(classification
													.getDelayUnit())));
						} else {
							ct.getSchedule().setDelay(null);
						}
						// if itemType or periodName changes update schedule
						// data
						if (!ct.getPeriodName().equals(
								classification.getPeriodName())
								|| !ct.getPointConceptName().equals(
										classification.getItemType())) {
							// found pointConcept
							for (GameConcept gc : g.getConcepts()) {
								if (gc instanceof PointConcept
										&& gc.getName().equals(
												classification.getItemType())) {
									ct.updatePointConceptData(
											(PointConcept) gc,
											classification.getPeriodName(),
											null);
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

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/incclassification")
	public List<IncrementalClassificationDTO> readAll(
			@PathVariable String gameId) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		List<IncrementalClassificationDTO> result = new ArrayList<>();
		if (g != null) {
			for (GameTask gt : g.getTasks()) {
				if (gt instanceof IncrementalClassificationTask) {
					result.add(converter.convertClassificationTask(gameId,
							(IncrementalClassificationTask) gt));
				}
			}
		}

		return result;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/model/game/{gameId}/incclassification/{classificationId}")
	public IncrementalClassificationDTO read(@PathVariable String gameId,
			@PathVariable String classificationId) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}
		Game g = gameSrv.loadGameDefinitionById(gameId);
		IncrementalClassificationDTO result = null;
		if (g != null) {
			for (GameTask gt : g.getTasks()) {
				if (gt instanceof IncrementalClassificationTask
						&& gt.getName().equals(classificationId)) {
					result = converter.convertClassificationTask(gameId,
							(IncrementalClassificationTask) gt);
				}
			}
		}
		return result;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/model/game/{gameId}/incclassification/{classificationId}")
	public void delete(@PathVariable String gameId,
			@PathVariable String classificationId) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof IncrementalClassificationTask
							&& gt.getName().equals(classificationId)) {
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

	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/incclassification/{classificationId}")
	public ClassificationBoard getIncrementalClassification(
			@PathVariable String gameId, @PathVariable String classificationId,
			@RequestParam(defaultValue = "-1") long timestamp,
			@RequestParam(defaultValue = "-1") int periodInstanceIndex) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		try {
			classificationId = URLDecoder.decode(classificationId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"classificationId is not UTF-8 encoded");
		}

		if (timestamp != -1 && periodInstanceIndex != -1) {
			throw new IllegalArgumentException(
					"Not use both timestamp and periodIndex parameters in the same request");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		ClassificationBoard result = null;
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof IncrementalClassificationTask
							&& gt.getName().equals(classificationId)) {
						IncrementalClassificationTask classificationDefinition = (IncrementalClassificationTask) gt;
						if (timestamp > -1) {
							result = ClassificationFactory
									.createIncrementalClassification(
											playerSrv.loadStates(gameId),
											classificationDefinition
													.getPointConceptName(),
											classificationDefinition
													.getPeriodName(),
											new Date(timestamp))
									.getClassificationBoard();
						} else if (periodInstanceIndex > -1) {
							result = ClassificationFactory
									.createIncrementalClassification(
											playerSrv.loadStates(gameId),
											classificationDefinition
													.getPointConceptName(),
											classificationDefinition
													.getPeriodName(),
											periodInstanceIndex)
									.getClassificationBoard();
						} else {
							result = ClassificationFactory
									.createIncrementalClassification(
											playerSrv.loadStates(gameId),
											classificationDefinition
													.getPointConceptName(),
											classificationDefinition
													.getPeriodName())
									.getClassificationBoard();
						}

					}
				}
			}
		} else {
			throw new IllegalArgumentException(String.format(
					"game %s not exist", gameId));
		}

		if (result == null) {
			throw new IllegalArgumentException(String.format(
					"classification %s not exist in game %s", classificationId,
					gameId));
		} else {
			return result;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/data/game/{gameId}/classification/{classificationId}")
	public ClassificationBoard getGeneralClassification(
			@PathVariable String gameId, @PathVariable String classificationId) {
		try {
			gameId = URLDecoder.decode(gameId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("gameId is not UTF-8 encoded");
		}

		try {
			classificationId = URLDecoder.decode(classificationId, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException(
					"classificationId is not UTF-8 encoded");
		}

		Game g = gameSrv.loadGameDefinitionById(gameId);
		ClassificationBoard result = null;
		if (g != null) {
			if (g.getTasks() != null) {
				for (GameTask gt : g.getTasks()) {
					if (gt instanceof GeneralClassificationTask
							&& gt.getName().equals(classificationId)) {
						GeneralClassificationTask classificationDefinition = (GeneralClassificationTask) gt;

						result = ClassificationFactory
								.createGeneralClassification(
										playerSrv.loadStates(gameId),
										classificationDefinition.getItemType())
								.getClassificationBoard();

					}
				}
			}
		} else {
			throw new IllegalArgumentException(String.format(
					"game %s not exist", gameId));
		}

		if (result == null) {
			throw new IllegalArgumentException(String.format(
					"classification %s not exist in game %s", classificationId,
					gameId));
		} else {
			return result;
		}
	}
}
