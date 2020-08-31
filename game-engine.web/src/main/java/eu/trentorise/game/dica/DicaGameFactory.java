package eu.trentorise.game.dica;

import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Level;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.DBRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.GeneralClassificationTask;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class DicaGameFactory {
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(DicaGameFactory.class);

    @Autowired
    private GameService gameSrv;

    private static final String DICA_PACKAGE = "eu/trentorise/game/sample";

    private static final String DICA_GAME = "dica-game";
    private static final String GAME_NAME = "dica-game";
    private static final String GAME_OWNER = "sco_master";
    private static final String DICA_DOMAIN = "dica-domain";

    public Game createGame(String domain, String gameId, String gameName, String gameOwner) {

        if (domain == null) {
            domain = DICA_DOMAIN;
        }
        if (gameName == null) {
            gameName = GAME_NAME;
        }

        if (gameOwner == null) {
            gameOwner = GAME_OWNER;
        }
        List<Game> g = gameSrv.loadGameByOwner(gameOwner);
        boolean gameFound = false;
        for (Game game : g) {
            if (gameFound = game.getName().equals(gameName)) {
                break;
            }
        }
        if (gameFound) {
            LogHub.info(gameId, logger, "sample {} already loaded for user {}", gameName,
                    gameOwner);
            return null;
        } else {
            LogHub.info(gameId, logger, "sample {} not loaded for user {}..start loading", gameName,
                    gameOwner);
            Game game = new Game(gameId);
            game.setName(gameName);
            game.setOwner(gameOwner);
            game.setDomain(domain);

            game.setActions(new HashSet<>(Arrays.asList("user_register", "user_login", "ask", "answer", "best_answer")));

            game.setConcepts(new HashSet<>(this.buildGameConcepts()));

            // add tasks
            game.setTasks(new HashSet<>());
            game.getTasks().addAll(this.buildGameTasks());

            game.getLevels().addAll(buildLevels());

            game = gameSrv.saveGameDefinition(game);
            gameId = game != null ? game.getId() : null;

            // add rules
            try {

                this.buildGameRules(gameId);

                LogHub.info(gameId, logger, "{} saved", gameName);
                return game;
            } catch (IOException e) {
                logger.error("Error loading {} rules", gameName);
                return null;
            }
        }
    }

    private List<String> pointNames() {
        List<String> pointNames = new ArrayList<>();
        pointNames.add("chua-phan-mon");

        pointNames.add("lich-su");

        pointNames.add("toan-hoc");

        pointNames.add("van");

        pointNames.add("sinh-hoc");

        pointNames.add("vat-ly");

        pointNames.add("on-thi-khoa-hoc-xa-hoi");

        pointNames.add("cong-nghe");

        pointNames.add("on-thi-khoa-hoc-tu-nhien");

        pointNames.add("dia-ly");

        pointNames.add("tieng-viet");

        pointNames.add("hoa-hoc");

        pointNames.add("tieng-anh");

        pointNames.add("giao-duc-cong-dan");

        pointNames.add("tin-hoc");

        pointNames.add("ngu-van");

        pointNames.add("am-nhac");

        pointNames.add("my-thuat");

        pointNames.add("khoa-hoc");

        pointNames.add("total");

        return pointNames;
    }

    private List<GameConcept> buildGameConcepts() {
        List<GameConcept> gameConcepts = new ArrayList<>();
        List<String> pointNames = this.pointNames();
        for (String pointName: pointNames) {
            PointConcept pointConcept = new PointConcept(pointName);
            Map<String, Long> periodConfig = new HashMap<>();
            final long MILLIS_IN_DAY = 86400000;
            periodConfig.put("daily", 1 * MILLIS_IN_DAY);
            periodConfig.put("weekly", 7 * MILLIS_IN_DAY);
            periodConfig.put("monthly", 30 * MILLIS_IN_DAY);
            periodConfig.put("quarterly", 90 * MILLIS_IN_DAY);
            periodConfig.put("annually", 365 * MILLIS_IN_DAY);
            for(Map.Entry<String, Long> entryPeriod : periodConfig.entrySet()){
                pointConcept.addPeriod(entryPeriod.getKey(), new Date(), entryPeriod.getValue());
            }
            gameConcepts.add(pointConcept);
        }
        BadgeCollectionConcept badgeConcept = new BadgeCollectionConcept("totalBadge");
        gameConcepts.add(badgeConcept);
        return gameConcepts;
    }

    private List<GameTask> buildGameTasks() {
        List<GameTask> gameTasks = new ArrayList<>();
        List<String> pointNames = this.pointNames();
        Map<String, String> scheduleConfig = new HashMap<>();
        scheduleConfig.put("daily", "0 0 0 * * *");
        scheduleConfig.put("weekly", "0 0 0 * * 0");
        scheduleConfig.put("monthly", "0 0 0 1 * *");
        scheduleConfig.put("quarterly", "0 0 0 1 */3 *");
        scheduleConfig.put("yearly", "0 0 0 1 1 *");
        for (String pointName: pointNames) {
            for(Map.Entry<String, String> entry: scheduleConfig.entrySet()) {
                TaskSchedule taskSchedule = new TaskSchedule();
                taskSchedule.setCronExpression(entry.getValue());

                GeneralClassificationTask task = new GeneralClassificationTask(taskSchedule, 3,
                        pointName, String.format("%s %s", entry.getKey(), pointName));
                gameTasks.add(task);
            }

        }
        return gameTasks;
    }

    private void buildGameRules(String gameId) throws IOException{

        String c = IOUtils.toString(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(DICA_PACKAGE + "/" + DICA_GAME + "/constants"));
        DBRule rule = new DBRule(gameId, c);
        rule.setName("constants");

        gameSrv.addRule(rule);


        c = IOUtils.toString(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(DICA_PACKAGE + "/" + DICA_GAME + "/behaviorPoints.drl"));
        rule = new DBRule(gameId, c);
        rule.setName("behaviorPoints");

        gameSrv.addRule(rule);

        c = IOUtils.toString(Thread.currentThread().getContextClassLoader()
                .getResourceAsStream(DICA_PACKAGE + "/" + DICA_GAME + "/threadPoints.drl"));
        rule = new DBRule(gameId, c);
        rule.setName("threadPoints");

        gameSrv.addRule(rule);
        List<String> badgeNames = totalDicaBadges().stream().map(badge -> badge.name).collect(Collectors.toList());
        for(String badgeName: badgeNames) {
            String subRuleFile = String.format("/badges/%s.drl", badgeName);
            c = IOUtils.toString(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(DICA_PACKAGE + "/" + DICA_GAME + subRuleFile));
            rule = new DBRule(gameId, c);
            rule.setName(badgeName);

            gameSrv.addRule(rule);
        }

    }

    private List<TotalDicaBadge> totalDicaBadges() {
        return Arrays.asList(
                new TotalDicaBadge("001-rabbit", 0L),
                new TotalDicaBadge("002-turtle", 50L),
                new TotalDicaBadge("003-parrot", 100L),
                new TotalDicaBadge("004-penguin", 150L),
                new TotalDicaBadge("005-ostrich", 200L),
                new TotalDicaBadge("006-owl", 300L),
                new TotalDicaBadge("007-hedgehog", 400L),
                new TotalDicaBadge("008-goat", 500L),
                new TotalDicaBadge("009-horse", 700L),
                new TotalDicaBadge("010-giraffe", 900L),
                new TotalDicaBadge("011-deer", 1100L),
                new TotalDicaBadge("012-kangaroo", 1300L),
                new TotalDicaBadge("013-bear", 1600L),
                new TotalDicaBadge("014-bull", 2000L),
                new TotalDicaBadge("015-boar", 2500L),
                new TotalDicaBadge("016-gorilla", 3000L),
                new TotalDicaBadge("017-bear", 4000L),
                new TotalDicaBadge("018-rhinoceros", 6000L),
                new TotalDicaBadge("019-elephant", 10000L),
                new TotalDicaBadge("020-lion", 15000L)

        );
    }

    private List<Level> buildLevels() {
        List<Level> levelsDefinition = new ArrayList<>();
        Level explorer = new Level("totalExplorer", "total");
        for(TotalDicaBadge badge : totalDicaBadges()){
            explorer.getThresholds().add(new Level.Threshold(badge.name, badge.point));
        }
        levelsDefinition.add(explorer);
        return levelsDefinition;
    }

    public static class TotalDicaBadge{
        public String name;
        public Long point;

        public TotalDicaBadge(String name, Long point) {
            this.name = name;
            this.point = point;
        }
    }
}
