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

import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.task.IncrementalClassificationTask;
import eu.trentorise.game.utils.Converter;

@RestController
public class ClassificationController {

	@Autowired
	private GameService gameSrv;

	@Autowired
	private TaskService taskSrv;

	@Autowired
	private Converter converter;

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
	public void update(@PathVariable String gameId,
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
											classification.getPeriodName());
									break;
								}
							}
							taskSrv.updateTask(gt, gameId);
						}
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
					result.add(converter
							.convertClassificationTask((IncrementalClassificationTask) gt));
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
					result = converter
							.convertClassificationTask((IncrementalClassificationTask) gt);
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
}
