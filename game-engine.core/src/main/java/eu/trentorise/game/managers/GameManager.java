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

package eu.trentorise.game.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

import javax.annotation.PostConstruct;

import org.apache.log4j.LogManager;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.math.Quantiles;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.StatsLogger;
import eu.trentorise.game.managers.drools.KieContainerFactory;
import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.ChallengeModel;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameStatistics;
import eu.trentorise.game.model.GroupChallenge;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.PlayerLevel;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.PointConcept.PeriodInstance;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.FSRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.Rule;
import eu.trentorise.game.notification.ChallengeFailedNotication;
import eu.trentorise.game.repo.ChallengeModelRepo;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GameRepo;
import eu.trentorise.game.repo.GenericObjectPersistence;
import eu.trentorise.game.repo.RuleRepo;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.TaskService;
import eu.trentorise.game.services.Workflow;
import eu.trentorise.game.task.AutoChallengeChoiceTask;

@Component
public class GameManager implements GameService {

    private final Logger logger = LoggerFactory.getLogger(GameManager.class);

    public static final String INTERNAL_ACTION_PREFIX = "scogei_";

    private static final long ONE_SECOND_IN_MILLIS = 1000;

    @Autowired
    private TaskService taskSrv;

    @Autowired
    private GameRepo gameRepo;

    @Autowired
    private RuleRepo ruleRepo;

    @Autowired
    private ChallengeModelRepo challengeModelRepo;

    @Autowired
    private KieContainerFactory kieContainerFactory;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private ChallengeManager challengeSrv;

    @Autowired
    private NotificationManager notificationSrv;

    @Autowired
    private Workflow workflow;
    
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostConstruct
    private void startup() {

        for (Game game : loadGames(true)) {
            startupTasks(game.getId());
        }

    }

    public String getGameIdByAction(String actionId) {
        GamePersistence game = gameRepo.findByActions(actionId);
        return game != null ? game.getId() : null;
    }

    public void startupTasks(String gameId) {
        Game game = loadGameDefinitionById(gameId);
        if (game != null) {
            for (GameTask task : game.getTasks()) {
                    taskSrv.createTask(task, gameId);
            }
        }
    }

    public Game saveGameDefinition(Game game) {
        GamePersistence pers = null;

        if (game == null) {
            throw new IllegalArgumentException("game cannot be null");
        }

        boolean isChallengeChoiceTaskExistent = false;
        if (game.getId() != null) {
            pers = gameRepo.findOne(game.getId());
            if (pers != null) {

                isChallengeChoiceTaskExistent = pers.getTasks() != null
                        && 
                        pers.getTasks().stream().anyMatch(task -> task.getType()
                                .equals(AutoChallengeChoiceTask.class.getCanonicalName()));

                pers.setName(game.getName());
                pers.setActions(new HashSet<String>());

                // add all external actions
                if (game.getActions() != null) {
                    for (String a : game.getActions()) {
                        if (!a.startsWith(INTERNAL_ACTION_PREFIX)) {
                            pers.getActions().add(a);
                        }

                    }
                }
                pers.setExpiration(game.getExpiration());
                pers.setTerminated(game.isTerminated());
                pers.setRules(game.getRules());
                pers.setDomain(game.getDomain());

                if (game.getConcepts() != null) {
                    Set<GenericObjectPersistence> concepts =
                            new HashSet<GenericObjectPersistence>();
                    for (GameConcept c : game.getConcepts()) {
                        concepts.add(new GenericObjectPersistence(c));
                    }
                    pers.setConcepts(concepts);
                } else {
                    pers.setConcepts(null);
                }

                if (game.getTasks() != null) {
                    Set<GenericObjectPersistence> tasks = new HashSet<GenericObjectPersistence>();
                    for (GameTask t : game.getTasks()) {
                        tasks.add(new GenericObjectPersistence(t));
                        // set internal actions
                        pers.getActions().addAll(t.retrieveActions());
                    }
                    pers.setTasks(tasks);
                } else {
                    pers.setTasks(null);
                }

                pers.getLevels().clear();
                pers.getLevels().addAll(game.getLevels());
                pers.setSettings(game.getSettings());
                
            } else {
                pers = new GamePersistence(game);
            }
        } else {
            pers = new GamePersistence(game);
        }

        pers = gameRepo.save(pers);

        // check if to update the tasks AutoChallengeChoice
        createOrUpdateChallengeChoiceTask(game, isChallengeChoiceTaskExistent);
        return pers.toGame();
    }

    private void createOrUpdateChallengeChoiceTask(Game game, final boolean isTaskExistent) {
        if (game.getTasks() != null) {
            game.getTasks().stream()
                    .filter(task -> task.getClass() == AutoChallengeChoiceTask.class)
                    .forEach(task -> {
                        if (isTaskExistent) {
                            taskSrv.updateTask(task, game.getId());
                        } else {
                            taskSrv.createTask(task, game.getId());
                        }
                    });
        }
    }

    public Game loadGameDefinitionById(String gameId) {
        GamePersistence gp = gameRepo.findOne(gameId);
        return gp == null ? null : gp.toGame();
    }

    public List<Game> loadGames(boolean onlyActive) {
        return convert(gameRepo.findByTerminated(!onlyActive));
    }

    public List<Game> loadAllGames() {
        return convert(gameRepo.findAll());
    }

    public String addRule(Rule rule) {
        String ruleUrl = null;
        boolean isEdit = false;
        if (rule != null) {
            StopWatch stopWatch = LogManager.getLogger(StopWatch.DEFAULT_LOGGER_NAME)
                    .getAppender("perf-file") != null ? new Log4JStopWatch() : null;
            if (stopWatch != null) {
                stopWatch.start("insert rule");
            }
            Game game = loadGameDefinitionById(rule.getGameId());
            if (game != null) {
                if (rule instanceof ClasspathRule) {
                    ClasspathRule r = (ClasspathRule) rule;
                    if (!(r.getUrl().startsWith(ClasspathRule.URL_PROTOCOL))) {
                        ruleUrl = ClasspathRule.URL_PROTOCOL + r.getUrl();
                    }
                }

                if (rule instanceof FSRule) {
                    FSRule r = (FSRule) rule;
                    if (!(r.getUrl().startsWith(FSRule.URL_PROTOCOL))) {
                        ruleUrl = FSRule.URL_PROTOCOL + r.getUrl();
                    }
                }

                if (rule instanceof DBRule) {
                    boolean alreadyExist = false;
                    DBRule r = (DBRule) rule;
                    if (r.getId() != null) {
                        r.setId(r.getId().replace(DBRule.URL_PROTOCOL, ""));
                        isEdit = true;
                    } else {
                        alreadyExist =
                                ruleRepo.findByGameIdAndName(rule.getGameId(), r.getName()) != null;
                    }

                    if (!alreadyExist) {
                        rule = ruleRepo.save(r);
                        ruleUrl = DBRule.URL_PROTOCOL + r.getId();
                    }
                }

                if (isEdit || ruleUrl != null && !game.getRules().contains(ruleUrl)) {
                    game.getRules().add(ruleUrl);
                    saveGameDefinition(game);
                    kieContainerFactory.purgeContainer(rule.getGameId());
                } else {
                    throw new IllegalArgumentException(
                            "the rule already exist for game " + rule.getGameId());
                }
                if (stopWatch != null) {
                    stopWatch.stop("insert rule", "inserted rule for game " + rule.getGameId());
                }
            } else {
                LogHub.error(rule.getGameId(), logger, "Game {} not found", rule.getGameId());
            }
        }
        return ruleUrl;
    }

    public Rule loadRule(String gameId, String url) {
        Rule rule = null;
        if (url != null) {
            if (url.startsWith(DBRule.URL_PROTOCOL)) {
                url = url.substring(DBRule.URL_PROTOCOL.length());
                return ruleRepo.findOne(url);
            } else if (url.startsWith(ClasspathRule.URL_PROTOCOL)) {
                url = url.substring(ClasspathRule.URL_PROTOCOL.length());
                if (Thread.currentThread().getContextClassLoader().getResource(url) != null) {
                    return new ClasspathRule(gameId, url);
                }

            } else if (url.startsWith(FSRule.URL_PROTOCOL)) {
                url = url.substring(FSRule.URL_PROTOCOL.length());
                if (new File(url).exists()) {
                    return new FSRule(gameId, url);
                }
            }
        }
        return rule;
    }

    @Scheduled(cron = "0 0 1 * * *")
    public void taskDestroyer() {
        LogHub.info(null, logger, "task destroyer invocation");
        long deadline = System.currentTimeMillis();

        List<Game> games = loadGames(true);
        for (Game game : games) {
            if (game.getExpiration() > 0 && game.getExpiration() < deadline) {
                for (GameTask task : game.getTasks()) {
                    if (taskSrv.destroyTask(task, game.getId())) {
                        LogHub.info(game.getId(), logger, "Destroy task - {} - of game {}",
                                task.getName(), game.getId());
                    }
                }
                game.setTerminated(true);
                saveGameDefinition(game);
            }
        }
    }


    @Scheduled(cron = "0 0 1 * * *")
    public void conditionCheckPerformanceGroupChallengesTask() {
        LogHub.info(null, logger,
                "Condition checker for best performance group challenges in action");
        long startOperation = System.currentTimeMillis();
        List<Game> activeGames = loadGames(true);
        activeGames.forEach(game -> {
            String gameId = game.getId();
            List<GroupChallenge> completedChallenges =
                    challengeSrv.completedPerformanceGroupChallenges(gameId);

            completedChallenges.forEach(challenge -> {
                List<String> winners = challengeSrv.conditionCheck(challenge);

                challenge.updateState(ChallengeState.COMPLETED, challenge.getEnd());
                challengeSrv.save(challenge);
                challengeSrv.sendChallengeNotification(challenge);
                challengeSrv.logStatsEvents(game, challenge);
                // action rewards
                // fix: executionTime should be challenge end time minus some time to be sure that
                // reward will be assigned to the correct period
                winners.stream().forEach(w -> {
                    workflow.apply(gameId, INTERNAL_ACTION_PREFIX + "reward", w,
                            challenge.getEnd().getTime() - ONE_SECOND_IN_MILLIS, null,
                            Arrays.asList(challenge.getReward()));
                });
            });

        });
        LogHub.info(null, logger, String.format("End best performance challenge action in %s ms",
                (System.currentTimeMillis() - startOperation)));
    }

    @Scheduled(cron = "0 0 4 1/1 * ?")
    public void challengeFailureTask() {
        LogHub.info(null, logger, "Challenge failure checker in action");
        final int pageSize = 50;
        long start = System.currentTimeMillis();
        Date now = new Date();
        List<Game> activeGames = loadGames(true);
        PageRequest firstPage = new PageRequest(0, pageSize);

        activeGames.forEach(game -> {
            final String gameId = game.getId();
            Page<String> pagedPlayerIds = playerSrv.readPlayers(gameId, firstPage);
            updateChallengeState(pagedPlayerIds, game, now);
            for (int pageIdx = 1; pageIdx < pagedPlayerIds.getTotalPages(); pageIdx++) {
                PageRequest pageRequest = new PageRequest(pageIdx, pageSize);
                pagedPlayerIds = playerSrv.readPlayers(gameId, pageRequest);
                updateChallengeState(pagedPlayerIds, game, now);
            }
            List<GroupChallenge> groupChallengesToFail =
                    challengeSrv.groupChallengeToFail(gameId, now);
            groupChallengesToFail.forEach(groupChallenge -> {
                groupChallenge.updateState(ChallengeState.FAILED, groupChallenge.getEnd());
                groupChallenge = challengeSrv.save(groupChallenge);
                challengeSrv.sendChallengeNotification(groupChallenge);
                challengeSrv.logStatsEvents(game, groupChallenge);
            });
        });


        long end = System.currentTimeMillis();
        LogHub.info(null, logger,
                String.format("Challenge failure check finished in %s ms ", (end - start)));
    }

    private void updateChallengeState(Page<String> pagedPlayerIds, Game game, Date deadline) {
        String executionId = UUID.randomUUID().toString();
        final String gameId = game.getId();
        pagedPlayerIds.forEach(id -> {
            PlayerState player = playerSrv.loadState(gameId, id, false, false);
            player.challenges().forEach(challenge -> {
                if (challenge.getEnd() != null && challenge.getEnd().before(deadline)
                        && challenge.persistedState() != ChallengeState.COMPLETED
                        && challenge.persistedState() != ChallengeState.FAILED) {
                    challenge.updateState(ChallengeState.FAILED, challenge.getEnd());
                    StatsLogger.logChallengeFailed(game.getDomain(), gameId, player.getPlayerId(),
                            executionId, challenge.getEnd().getTime(), deadline.getTime(),
                            challenge.getName());
                    ChallengeFailedNotication notification = new ChallengeFailedNotication();
                    notification.setChallengeName(challenge.getName());
                    notification.setGameId(game.getId());
                    notification.setPlayerId(player.getPlayerId());
                    notificationSrv.notificate(notification);
                }
            });
            playerSrv.saveState(player);
        });
    }

    public Game loadGameDefinitionByAction(String actionId) {
        GamePersistence gp = gameRepo.findByActions(actionId);
        return gp != null ? gp.toGame() : null;
    }

    @Override
    public void addConceptInstance(String gameId, GameConcept gc) {
        Game g = loadGameDefinitionById(gameId);
        if (g != null) {
            if (g.getConcepts() == null) {
                g.setConcepts(new HashSet<GameConcept>());
            }
            g.getConcepts().add(gc);
            saveGameDefinition(g);
        }

    }

    @Override
    public Set<GameConcept> readConceptInstances(String gameId) {
        Game g = loadGameDefinitionById(gameId);
        if (g != null) {
            return g.getConcepts() != null ? g.getConcepts() : Collections.<GameConcept>emptySet();
        } else {
            return Collections.<GameConcept>emptySet();
        }
    }

    @Override
    public boolean deleteRule(String gameId, String url) {
        Game g = loadGameDefinitionById(gameId);
        boolean res = false;
        if (g != null && url != null && url.indexOf(DBRule.URL_PROTOCOL) != -1) {
            String id = url.substring(5);
            ruleRepo.delete(id);
            res = g.getRules().remove(url);
            saveGameDefinition(g);
            kieContainerFactory.purgeContainer(gameId);
        }

        return res;
    }

    @Override
    public boolean deleteGame(String gameId) {
        boolean res = false;
        if (gameId != null) {
            gameRepo.delete(gameId);
            res = true;
        }
        return res;
    }

    @Override
    public List<Game> loadGameByOwner(String user) {
        List<Game> result = new ArrayList<Game>();
        if (user != null) {
            result = convert(gameRepo.findByOwner(user));
        }
        return result;

    }

    @Override
    public ChallengeModel saveChallengeModel(String gameId, ChallengeModel model) {
        model.setGameId(gameId);
        return challengeModelRepo.save(model);
    }

    @Override
    public boolean deleteChallengeModel(String gameId, String modelId) {
        challengeModelRepo.delete(modelId);
        return true;
    }

    @Override
    public Set<ChallengeModel> readChallengeModels(String gameId) {
        return challengeModelRepo.findByGameId(gameId);
    }

    @Override
    public ChallengeModel readChallengeModel(String gameId, String modelId) {
        return challengeModelRepo.findByGameIdAndId(gameId, modelId);
    }

    @Override
    public List<Game> loadGameByOwner(String domain, String user) {
        return convert(gameRepo.findByDomainAndOwner(domain, user));
    }

    @Override
    public List<Game> loadGameByDomain(String domain) {
        List<Game> games = new ArrayList<>();
        if (domain != null) {
            games = convert(gameRepo.findByDomain(domain));
        }
        return games;
    }

    private List<Game> convert(Iterable<GamePersistence> gamesPersistence) {
        List<Game> games = null;
        if (gamesPersistence != null) {
            games = new ArrayList<>();
            for (GamePersistence gamePersistence : gamesPersistence) {
                games.add(gamePersistence.toGame());
            }
        }

        return games;
    }

    @Override
    public Game upsertLevel(String gameId, Level level) {
        Game game = loadGameDefinitionById(gameId);
        if (game != null) {

            if (!isPointConceptDefinedInGame(game, level.getPointConceptName())) {
                throw new IllegalArgumentException(String.format("level %s not defined in game %s",
                        level.getPointConceptName(), gameId));
            }

            Level toEdit = getLevelFromGame(game, level.getName());
            if (toEdit != null) { // update the level
                int levelIdx = game.getLevels().indexOf(toEdit);
                game.getLevels().remove(levelIdx);
                game.getLevels().add(levelIdx, level);
            } else { // new level
            if (game.getLevels().stream().anyMatch(
                    lev -> lev.getPointConceptName().equals(level.getPointConceptName()))) {
                throw new IllegalArgumentException(String.format(
                        "multiple levels bound to pointConcept %s", level.getPointConceptName()));
            }
                game.getLevels().add(level);
            }
            saveGameDefinition(game);
            if (toEdit != null) {
                LogHub.info(gameId, logger, String.format("updated level %s", toEdit.getName()));
            } else {
                LogHub.info(gameId, logger, String.format("saved level %s", level.getName()));
            }
        } else {
            throw new IllegalArgumentException(String.format("game %s not exist", gameId));
        }

        return game;
    }

    private boolean isPointConceptDefinedInGame(Game game, String pointConceptName) {
            return game != null &&game.getConcepts().stream().anyMatch(gc -> {
               return gc instanceof PointConcept && gc.getName().equals(pointConceptName); 
        });
    }

    @Override
    public Game deleteLevel(String gameId, String levelName) {
        Game game = loadGameDefinitionById(gameId);
        if (game != null) {
            game.getLevels().removeIf(level -> level.getName().equals(levelName));
            game = saveGameDefinition(game);
            LogHub.info(gameId, logger, String.format("deleted level %s", levelName));
        } else {
            throw new IllegalArgumentException(String.format("game %s not exist", gameId));
        }
        return game;
    }

    @Override
    public Level addLevelThreshold(String gameId, String levelName, Threshold threshold) {
        Game game = loadGameDefinitionById(gameId);
        if (game != null) {
            Level level = getLevelFromGame(game, levelName);
            if (!level.getThresholds().contains(threshold)) {
                level.getThresholds().add(threshold);
                game = saveGameDefinition(game);
                LogHub.info(gameId, logger, String.format("added threshold %s to level %s",
                        threshold.getName(), level.getName()));
            } else {
                throw new IllegalArgumentException(
                        String.format("threshold %s already present", threshold.getName()));
            }
            return level;
        } else {
            throw new IllegalArgumentException(String.format("game %s not exist", gameId));
        }
    }

    @Override
    public Level deleteLevelThreshold(String gameId, String levelName, String thresholdName) {
        Game game = loadGameDefinitionById(gameId);
        if (game != null) {
            Level level = getLevelFromGame(game, levelName);
            level.getThresholds().removeIf(thres -> thres.getName().equals(thresholdName));
            saveGameDefinition(game);
            LogHub.info(gameId, logger, String.format("deleted threshold %s to level %s",
                    thresholdName, level.getName()));
            return level;
        } else {
            throw new IllegalArgumentException(String.format("game %s not exist", gameId));
        }
    }

    private Level getLevelFromGame(Game game, String levelName) {
        if (game != null) {
            for (Level level : game.getLevels()) {
                if (level.getName().equals(levelName)) {
                    return level;
                }
            }
        }
        return null;
    }

    @Override
    public Level updateLevelThreshold(String gameId, String levelName, String thresholdName,
            double thresholdValue) {
        Game game = loadGameDefinitionById(gameId);
        if (game != null) {
            Level level = getLevelFromGame(game, levelName);
            if (level != null) {
                for (Threshold threshold : level.getThresholds()) {
                    if (threshold.getName().equals(thresholdName)) {
                        threshold.updateValue(thresholdValue);
                        saveGameDefinition(game);
                    }
                }
                return level;
            }
            return null;
        } else {
            throw new IllegalArgumentException(String.format("game %s not exist", gameId));
        }

    }

    @Override
    public List<PlayerLevel> calculateLevels(String gameId, PlayerState playerState) {
        Game game = loadGameDefinitionById(gameId);
        if (game != null) {
            List<PlayerLevel> playerLevels = new ArrayList<>();
            if (playerState != null) {
                List<Level> levelsDefinition = game.getLevels();
                levelsDefinition.stream().forEach(definition -> {
                    PointConcept pointConceptValue = (PointConcept) playerState.getState().stream()
                            .filter(concept -> concept instanceof PointConcept
                                    && concept.getName().equals(definition.getPointConceptName()))
                            .findFirst().orElse(null);
                    final double actualValue =
                            pointConceptValue != null ? pointConceptValue.getScore() : 0d;
                    try {
                        playerLevels.add(new PlayerLevel(definition, actualValue));
                    } catch (NoSuchElementException e) {
                        // do nothing: no playerLevel for actualValue using given definition
                    }
                });
            }
            return playerLevels;

        } else {
            throw new IllegalArgumentException(String.format("game %s not exist", gameId));
        }
    }
    
    
    @Scheduled(cron = "0 0 1 * * *")
	public void taskGameStats() {
		/**
		 * For every activeGame take settings -> statistics ( array of
		 * PointConcept name and periodName)
		 * 
		 * For every PointConcept in statistic calculate 10 quantiles, average
		 * and variance
		 * 
		 * Run this in a scheduled task every night
		 * 
		 * Save in a collection:
		 * 
		 * gameId, pointconceptName periodName, start date period, end date
		 * period, periodIndex -> Statistic { 10quantiles[], average, }
		 * 
		 * Example
		 * 
		 * game1, Walk_Km, weekly, period1Start, period1End, 1 {data}
		 * 
		 * game1, Walk_Km, weekly, period2Start, period2End, 2 {data}
		 * 
		 * game1, Bus_Km, weekly, period1Start, period1End, 1 {data}
		 * 
		 * 
		 */

        LogHub.info(null, logger, "task game statistics");

		Calendar cal = Calendar.getInstance();
		long moment = cal.getTimeInMillis();

		// 1 read active games.
		for (Game activeG : loadGames(true)) {
			// 1.1 read settings about statistics.
			if (activeG.getSettings() != null && !activeG.getSettings().getStatisticsConfig().isEmpty()) {
                final Set<String> scoreNames = activeG.getSettings().getStatisticsConfig().keySet();
                for (String pointConceptName : scoreNames) {
					String periodName = activeG.getSettings().getStatisticsConfig().get(pointConceptName);
					// 1.2 arrange statistics data array.
					PeriodInstance periodInstance = ClassificationUtils.retrieveWindow(activeG, periodName,
							pointConceptName, moment, -1);

					if (periodInstance != null) {
						String key = ClassificationUtils.generateKey(periodInstance);
						// 1.3 query player states and prepare data array.
						Query query = new Query();
						Criteria criteria = new Criteria("gameId").is(activeG.getId());
						query.addCriteria(criteria);
						query.fields().include("concepts.PointConcept." + pointConceptName + ".obj.periods."
								+ periodName + ".instances." + key + ".score");

						List<StatePersistence> pStates = mongoTemplate.find(query, StatePersistence.class);

						double[] data = new double[pStates.size()];
						for (int i = 0; i < pStates.size(); i++) {
							data[i] = pStates.get(i).getIncrementalScore(pointConceptName, periodName, key);
						}

						// average.
						double average = Arrays.stream(data).average().getAsDouble();
						// variance.
						double variance = ClassificationUtils.calculateVariance(data);
						// 10 quantiles.
						Map<Integer, Double> q = Quantiles.scale(10).indexes(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
								.compute(data);

						Query qGameStats = new Query();
						Criteria cGameStats = new Criteria("gameId").is(activeG.getId()).and("pointConceptName")
								.is(pointConceptName).and("periodName").is(periodName).and("periodIndex").is(key);
						qGameStats.addCriteria(cGameStats);

						Update update = new Update();
						update.set("gameId", activeG.getId());
						update.set("pointConceptName", pointConceptName);
						update.set("periodName", periodName);
						update.set("periodIndex", key);
						update.set("startDate", periodInstance.getStart());
						update.set("endDate", periodInstance.getEnd());
						update.set("average", average);
						update.set("variance", variance);
						update.set("quantiles", q);
						update.set("lastUpdated", moment);

						FindAndModifyOptions options = new FindAndModifyOptions();
						options.upsert(true);
						options.returnNew(true);

                        mongoTemplate.findAndModify(qGameStats, update, options,
								GameStatistics.class);
					}
				}
                LogHub.info(activeG.getId(), logger,
                        String.format("Calculated statistics on scores %s", scoreNames));
			}
		}
		
	}

	@Override
	public List<GameStatistics> loadGameStats(String gameId, String pointConceptName, String periodName, Long timestamp,
			String periodIndex, Pageable pageable) {
		
		Query q = new Query();
		Criteria c = new Criteria("gameId").is(gameId);
		
		if (pointConceptName != null && !pointConceptName.isEmpty()) {
			c.and("pointConceptName").is(pointConceptName);
		}
		if (periodName != null && !periodName.isEmpty()) {
			c.and("periodName").is(periodName);
		}
		if (periodIndex != null && !periodIndex.isEmpty()) {
			c.and("periodIndex").is(periodIndex);
		}
		if (timestamp != null) {
			c.and("startDate").lte(timestamp).and("endDate").gte(timestamp);
		}
		
		q.addCriteria(c);
		
		if (pageable != null) {
			q.with(pageable);
		}
		
		return mongoTemplate.find(q, GameStatistics.class);
		
	}
	
	


}
