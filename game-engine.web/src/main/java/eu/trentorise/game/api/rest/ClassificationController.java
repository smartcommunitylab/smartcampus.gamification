package eu.trentorise.game.api.rest;

import static eu.trentorise.game.api.rest.ControllerUtils.decodePathVariable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.trentorise.game.bean.GeneralClassificationDTO;
import eu.trentorise.game.bean.IncrementalClassificationDTO;
import eu.trentorise.game.core.ResourceNotFoundException;
import eu.trentorise.game.managers.ClassificationUtils;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.PointConcept.PeriodInstance;
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
import io.swagger.annotations.ApiOperation;

@RestController
@Profile({"sec", "no-sec"})
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

    @RequestMapping(method = RequestMethod.POST,
            value = "/model/game/{gameId}/classification", consumes = {"application/json"},
            produces = {"application/json"})
    @ApiOperation(value = "Add general classification definition")
    public GeneralClassificationDTO addClassificationTask(@PathVariable String gameId,
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

    // Update classification
    // PUT /model/game/{id}/classification/{classificationId}

    @RequestMapping(method = RequestMethod.PUT,
            value = "/model/game/{gameId}/classification/{classificationId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Edit general classification definition")
    public void editClassificationTask(@PathVariable String gameId,
            @PathVariable String classificationId, @RequestBody GeneralClassificationDTO task) {
        gameId = decodePathVariable(gameId);
        classificationId = decodePathVariable(classificationId);

        Game g = gameSrv.loadGameDefinitionById(gameId);
        if (g != null) {
            if (g.getTasks() != null) {
                for (GameTask gt : g.getTasks()) {
                    if (gt instanceof GeneralClassificationTask
                            && gt.getName().equals(task.getName())) {
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

    // Read all classifications
    // GET /model/game/{id}/classification

    @RequestMapping(method = RequestMethod.GET,
            value = "/model/game/{gameId}/classification", produces = {"application/json"})
    @ApiOperation(value = "Get general classification definitions")
    public List<GeneralClassificationDTO> readAllGeneralClassifications(
            @PathVariable String gameId) {
        gameId = decodePathVariable(gameId);

        Game g = gameSrv.loadGameDefinitionById(gameId);
        List<GeneralClassificationDTO> result = new ArrayList<>();
        if (g != null) {
            if (g.getTasks() != null) {
                for (GameTask c : g.getTasks()) {
                    if (c instanceof GeneralClassificationTask) {
                        result.add(
                                converter.convertClassificationTask((GeneralClassificationTask) c));
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

    @RequestMapping(method = RequestMethod.GET,
            value = "/model/game/{gameId}/classification/{classificationId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get general classification definition")
    public GeneralClassificationDTO readGeneralClassification(
            @PathVariable String gameId, @PathVariable String classificationId) {
        gameId = decodePathVariable(gameId);
        classificationId = decodePathVariable(classificationId);

        List<GeneralClassificationDTO> result = readAllGeneralClassifications(gameId);
        for (GeneralClassificationDTO r : result) {
            if (r.getName().equals(classificationId)) {
                return r;
            }
        }

        throw new ResourceNotFoundException(String
                .format("classificationId %s not found in game %s", classificationId, gameId));
    }

    // Delete a classification
    // DELETE /model/game/{id}/classification/{classificationId}

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/model/game/{gameId}/task/{classificationId}",
            produces = {"application/json"})
    @ApiOperation(value = "Delete general classification definition")
    public void deleteClassificationTask(@PathVariable String gameId,
            @PathVariable String classificationId) {
        gameId = decodePathVariable(gameId);
        classificationId = decodePathVariable(classificationId);

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
    @RequestMapping(method = RequestMethod.POST,
            value = "/model/game/{gameId}/incclassification",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Add incremental classification definition")
    public IncrementalClassificationDTO createIncremental(
            @PathVariable String gameId, @RequestBody IncrementalClassificationDTO classification) {
        gameId = decodePathVariable(gameId);
        Game g = gameSrv.loadGameDefinitionById(gameId);
        if (g != null) {
            classification.setGameId(gameId);
            if (g.getTasks() != null) {
                IncrementalClassificationTask incClassification =
                        converter.convertClassificationTask(classification);
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

    @RequestMapping(method = RequestMethod.PUT,
            value = "/model/game/{gameId}/incclassification/{classificationId}",
            consumes = {"application/json"}, produces = {"application/json"})
    @ApiOperation(value = "Edit general classification definition")
    public void updateIncrementalClassification(
            @PathVariable String gameId, @PathVariable String classificationId,
            @RequestBody IncrementalClassificationDTO classification) {
        gameId = decodePathVariable(gameId);

        Game g = gameSrv.loadGameDefinitionById(gameId);
        if (g != null) {
            classification.setGameId(gameId);
            if (g.getTasks() != null) {
                for (GameTask gt : g.getTasks()) {
                    if (gt instanceof IncrementalClassificationTask
                            && gt.getName().equals(classificationId)) {

                        IncrementalClassificationTask ct = (IncrementalClassificationTask) gt;
                        ct.setItemsToNotificate(classification.getItemsToNotificate());
                        ct.setClassificationName(classification.getClassificationName());
                        if (StringUtils.isNotBlank(classification.getDelayUnit())) {
                            ct.getSchedule()
                                    .setDelay(new TimeInterval(classification.getDelayValue(),
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
                                if (gc instanceof PointConcept
                                        && gc.getName().equals(classification.getItemType())) {
                                    ct.updatePointConceptData((PointConcept) gc,
                                            classification.getPeriodName(), null);
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

    @RequestMapping(method = RequestMethod.GET,
            value = "/model/game/{gameId}/incclassification",
            produces = {"application/json"})
    @ApiOperation(value = "Get incremental classification defintions")
    public List<IncrementalClassificationDTO> readAllIncremental(
            @PathVariable String gameId) {
        gameId = decodePathVariable(gameId);

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

    @RequestMapping(method = RequestMethod.GET,
            value = "/model/game/{gameId}/incclassification/{classificationId}",
            produces = {"application/json"})
    @ApiOperation(value = "Get incremental classification defition")
    public IncrementalClassificationDTO readIncremental(
            @PathVariable String gameId, @PathVariable String classificationId) {
        gameId = decodePathVariable(gameId);

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

    @RequestMapping(method = RequestMethod.DELETE,
            value = "/model/game/{gameId}/incclassification/{classificationId}",
            produces = {"application/json"})
    @ApiOperation(value = "Delete incremental classification definition")
    public void deleteIncremental(@PathVariable String gameId,
            @PathVariable String classificationId) {
        gameId = decodePathVariable(gameId);

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

    /*
     * used requestParam (page and size) instead of Pageable to maintain compatibility with API
     * version < 2.2.0
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/data/game/{gameId}/incclassification/{classificationId}",
            produces = {"application/json"})
    @ApiOperation(value = "Read incremental classification board")
    public ClassificationBoard getIncrementalClassification(
            @PathVariable String gameId, @PathVariable String classificationId,
            @RequestParam(defaultValue = "-1") long timestamp,
            @RequestParam(defaultValue = "-1") int periodInstanceIndex,
            @RequestParam(required = false, defaultValue = "-1") int page,
            @RequestParam(required = false, defaultValue = "-1") int size) {

        PeriodInstance instance = null;
        gameId = decodePathVariable(gameId);
        classificationId = decodePathVariable(classificationId);

        if (timestamp != -1 && periodInstanceIndex != -1) {
            throw new IllegalArgumentException(
                    "Cannot use both timestamp and periodIndex parameters in the same request");
        }

        // put this to maintain same behavior of pageable config ( start page
        // from index 1)
        if (page == 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }

        Game g = gameSrv.loadGameDefinitionById(gameId);
        ClassificationBoard result = null;
        if (g != null) {
            if (g.getTasks() != null) {
                for (GameTask gt : g.getTasks()) {
                    if (gt instanceof IncrementalClassificationTask
                            && gt.getName().equals(classificationId)) {
                        IncrementalClassificationTask classificDef =
                                (IncrementalClassificationTask) gt;
                        if (timestamp > -1) {
                            /** identify window instance. **/
                            instance = ClassificationUtils.retrieveWindow(g,
                                    classificDef.getPeriodName(),
                                    classificDef.getPointConceptName(), timestamp, -1);

                        } else if (periodInstanceIndex > -1) {
                            /** identify window instance using period. **/
                            instance = ClassificationUtils.retrieveWindow(g,
                                    classificDef.getPeriodName(),
                                    classificDef.getPointConceptName(), -1, periodInstanceIndex);

                        } else {
                            /** retrieve current classification **/
                            instance = ClassificationUtils.retrieveWindow(g,
                                    classificDef.getPeriodName(),
                                    classificDef.getPointConceptName(), System.currentTimeMillis(),
                                    -1);
                        }

                        /** generate key. **/
                        if (instance != null) {

                            String key = ClassificationUtils.generateKey(instance);

                            /**
                             * search in player state for all the that matches classification + key.
                             **/

                            result = playerSrv.classifyPlayerStatesWithKey(timestamp,
                                    classificDef.getPointConceptName(),
                                    classificDef.getPeriodName(), key, g.getId(),
                                    createPageRequest(page, size));

                        }

                    }
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("game %s does not exist", gameId));
        }

        if (result == null) {
            throw new IllegalArgumentException(String.format(
                    "classification %s does not exist in game %s", classificationId, gameId));
        } else {
            return result;
        }
    }

    private PageRequest createPageRequest(int page, int size) {
        PageRequest pageRequest = null;
        if (page != -1 && size != -1) {
            // put page-1 to maintain same behavior of pageable config ( start
            // page
            // from index 1)
            pageRequest = new PageRequest(page - 1, size);
        }
        return pageRequest;
    }

    /*
     * used requestParam (page and size) instead of Pageable to maintain compatibility with API
     * version < 2.2.0
     */
    @RequestMapping(method = RequestMethod.GET,
            value = "/data/game/{gameId}/classification/{classificationId}",
            produces = {"application/json"})
    @ApiOperation(value = "Read general classification board")
    public ClassificationBoard getGeneralClassification(
            @PathVariable String gameId, @PathVariable String classificationId,
            @RequestParam(required = false, defaultValue = "-1") int page,
            @RequestParam(required = false, defaultValue = "-1") int size) {
        gameId = decodePathVariable(gameId);
        classificationId = decodePathVariable(classificationId);

        // put this to maintain same behavior of pageable config ( start page
        // from index 1)
        if (page == 0) {
            throw new IllegalArgumentException("Page index must not be less than zero!");
        }

        Game g = gameSrv.loadGameDefinitionById(gameId);
        ClassificationBoard result = null;
        if (g != null) {
            if (g.getTasks() != null) {
                for (GameTask gt : g.getTasks()) {
                    if (gt instanceof GeneralClassificationTask
                            && gt.getName().equals(classificationId)) {
                        GeneralClassificationTask classificationDefinition =
                                (GeneralClassificationTask) gt;
                        result = playerSrv.classifyAllPlayerStates(g,
                                classificationDefinition.getItemType(),
                                createPageRequest(page, size));

                    }
                }
            }
        } else {
            throw new IllegalArgumentException(String.format("game %s does not exist", gameId));
        }

        if (result == null) {
            throw new IllegalArgumentException(String.format(
                    "classification %s does not exist in game %s", classificationId, gameId));
        } else {
            return result;
        }
    }
}
