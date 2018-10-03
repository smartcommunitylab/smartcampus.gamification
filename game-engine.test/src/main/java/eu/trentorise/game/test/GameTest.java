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

package eu.trentorise.game.test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.managers.QueueGameWorkflow;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.TeamState;
import eu.trentorise.game.model.core.ClasspathRule;
import eu.trentorise.game.model.core.FSRule;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {AppConfig.class, MongoConfig.class},
        loader = AnnotationConfigContextLoader.class)
public abstract class GameTest {

    private static final Logger logger = LoggerFactory.getLogger(GameTest.class);

    private String gameId;
    private List<GameTask> tasks = new ArrayList<GameTask>();

    private static final long WAIT_EXEC = 15 * 1000;

    @Autowired
    private GameManager gameManager;

    @Autowired
    private PlayerService playerSrv;

    @Autowired
    private Workflow workflow;

    @Autowired
    private MongoTemplate mongo;

    @Autowired
    private AppContextProvider provider;

    @Before
    public final void cleanDB() {
        // clean mongo
        mongo.dropCollection(StatePersistence.class);
        mongo.dropCollection(GamePersistence.class);
        mongo.dropCollection(NotificationPersistence.class);
    }

    public abstract void initEnv();

    public abstract void defineGame();

    public abstract void defineExecData(List<ExecData> execList);

    public abstract void analyzeResult();

    @Test
    public void run() {
        defineGame();
        initEnv();
        runEngine();
        if (workflow instanceof QueueGameWorkflow) {
            try {
                Thread.sleep(WAIT_EXEC);
            } catch (InterruptedException e) {
                Assert.fail("sleep failure");
            }
        }
        analyzeResult();
    }

    public void savePlayerState(String gameId, String playerId, List<GameConcept> concepts) {
        savePlayerState(gameId, playerId, concepts, null);
    }

    public void savePlayerState(String gameId, String playerId, List<GameConcept> concepts,
            Map<String, Object> customData) {
        PlayerState player = new PlayerState(gameId, playerId);
        Set<GameConcept> state = new HashSet<>();
        if (concepts != null) {
            state.addAll(concepts);
        }
        player.setState(state);
        if (customData != null) {
            player.getCustomData().putAll(customData);
        }
        playerSrv.saveState(player);
    }

    public void saveTeam(String gameId, String teamId, String name, Set<String> members) {
        saveTeam(gameId, teamId, name, members, null, null);
    }

    public void saveTeam(String gameId, String teamId, String name, Set<String> members,
            List<GameConcept> concepts) {
        saveTeam(gameId, teamId, name, members, concepts, null);
    }

    public void saveTeam(String gameId, String teamId, String name, Set<String> members,
            List<GameConcept> concepts, Map<String, Object> customData) {
        TeamState team = new TeamState(gameId, teamId);
        team.setName(name);
        if (members != null) {
            team.setMembers(new ArrayList<>(members));
        }
        playerSrv.saveTeam(team);
        if (concepts != null || customData != null) {
            savePlayerState(gameId, teamId, concepts, customData);
        }
    }

    public void defineGameHelper(String domain, String gameId, List<String> actions,
            List<GameConcept> concepts) {
        Game g = new Game();
        g.setId(gameId);
        g.setName(gameId);
        g.setActions(new HashSet<String>(actions));
        g.setDomain(domain);

        g.setConcepts(new HashSet<GameConcept>());
        if (concepts != null) {
            for (GameConcept gc : concepts) {
                g.getConcepts().add(gc);
            }

        }
        gameManager.saveGameDefinition(g);
        this.gameId = gameId;
    }

    public void addGameTask(String gameId, GameTask gt) {
        Game g = gameManager.loadGameDefinitionById(gameId);
        if (g != null) {
            if (g.getTasks() == null) {
                g.setTasks(new HashSet<GameTask>());
            }
            g.getTasks().add(gt);
            gameManager.saveGameDefinition(g);
        } else {
            throw new IllegalArgumentException(
                    String.format("please create game %s before call addGameTask", gameId));
        }
        tasks.add(gt);
    }

    public void loadClasspathRules(String gameId, List<String> rulesPath) {
        for (String path : rulesPath) {
            gameManager.addRule(new ClasspathRule(gameId, path));
        }
    }

    public void loadClasspathRules(String gameId, String classpathFolder) throws IOException {
        if (gameId == null) {
            throw new IllegalArgumentException("gameId cannot be null");
        }
        if (classpathFolder == null) {
            throw new IllegalArgumentException("classpathFolder cannot be null");
        }

        // search for rule files
        String patternAntStyle = String.format("classpath:%s/**/*.drl", classpathFolder);
        PathMatchingResourcePatternResolver matcherResources =
                new PathMatchingResourcePatternResolver();
        Resource[] classpathRules = matcherResources.getResources(patternAntStyle);
        List<String> rulesPath = new ArrayList<>();
        for (Resource classpathRule : classpathRules) {
            rulesPath.add(relativeRulePath(classpathRule, classpathFolder));
        }
        loadClasspathRules(gameId, rulesPath);

        // search for constants file
        patternAntStyle = String.format("classpath:%s/**/constants", classpathFolder);
        classpathRules = matcherResources.getResources(patternAntStyle);
        rulesPath = new ArrayList<>();
        for (Resource classpathRule : classpathRules) {
            rulesPath.add(relativeRulePath(classpathRule, classpathFolder));
        }
        loadClasspathRules(gameId, rulesPath);
    }

    private String relativeRulePath(Resource resource, String classpathFolder) throws IOException {
        String path = null;
        if (resource != null) {
            path = classpathFolder;
            String uri = resource.getURI().getPath();
            String pattern = String.format(".*/%s(.*)%s", classpathFolder, resource.getFilename());
            Pattern subfolderPattern = Pattern.compile(pattern);
            Matcher m = subfolderPattern.matcher(uri);
            if (m.matches() && m.group(1) != null) {
                path += m.group(1);
            }
            path += resource.getFilename();
        }
        return path;
    }

    public void loadFilesystemRules(String gameId, List<String> rulesPath) {
        for (String path : rulesPath) {
            gameManager.addRule(new FSRule(gameId, path));
        }
    }


    public void loadFilesystemRules(String gameId, String folderPath, boolean exploreSubFolders) {
        loadFilesystemRules(gameId, folderPath, exploreSubFolders, null);
    }

    public void loadFilesystemRules(String gameId, String folderPath, boolean exploreSubFolders,
            List<RuleFilter> filters) {
        if (gameId == null) {
            throw new IllegalArgumentException("gameId cannot be null");
        }
        if (folderPath == null) {
            throw new IllegalArgumentException("folderPath cannot be null");
        }

        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folderPath is not path of a directory");
        }

        File[] rulesFile = folder.listFiles();
        List<String> rulesPaths = new ArrayList<>();
        for (File ruleFile : rulesFile) {
            String ruleFileName = ruleFile.getName();
            if (ruleFile.isFile() && validateOnFilters(filters, ruleFileName)) {
                rulesPaths.add(ruleFile.getAbsolutePath());
            } else if (ruleFile.isDirectory() && exploreSubFolders) {
                loadFilesystemRules(gameId, ruleFile.getAbsolutePath(), exploreSubFolders, filters);
            }
        }
        loadFilesystemRules(gameId, rulesPaths);
    }

    private boolean validateOnFilters(List<RuleFilter> filters, String ruleName) {
        if (filters == null) {
            return true;
        }
        boolean isValid = false;
        for (RuleFilter filter : filters) {
            isValid = filter.accept(ruleName);
        }
        return isValid;
    }

    public interface RuleFilter {
        boolean accept(String ruleName);
    }


    public static class IncludeRuleNameFilter implements RuleFilter {

        private String ruleToInclude;

        public IncludeRuleNameFilter(String ruleName) {
            ruleToInclude = ruleName;
        }

        @Override
        public boolean accept(String ruleName) {
            return ruleToInclude != null && ruleName != null && ruleToInclude.equals(ruleName);
        }
    }


    public static class ExcludeRuleNameFilter implements RuleFilter {

        private String ruleToExclude;

        public ExcludeRuleNameFilter(String ruleName) {
            ruleToExclude = ruleName;
        }

        @Override
        public boolean accept(String ruleName) {
            return ruleToExclude != null && ruleName != null && !ruleToExclude.equals(ruleName);
        }

    }

    private void runEngine() {
        List<ExecData> execList = new ArrayList<GameTest.ExecData>();
        defineExecData(execList);
        for (ExecData ex : execList) {
            workflow.apply(ex.gameId, ex.getActionId(), ex.getPlayerId(), ex.getData(),
                    ex.getFactObjects());
        }

        // launch Task sequentially

        for (GameTask task : tasks) {
            task.execute((GameContext) provider.getApplicationContext().getBean("gameCtx", gameId,
                    task));
        }

    }

    public void assertionBadge(String gameId, List<String> values, String playerId,
            String conceptName) {
        List<PlayerState> states = playerSrv.loadStates(gameId);
        StateAnalyzer analyzer = new StateAnalyzer(states);
        List<String> badgesEarned = analyzer.getBadges(analyzer.findPlayer(playerId), conceptName);
        Assert.assertEquals(new HashSet<String>(values), new HashSet<String>(badgesEarned));
    }

    public void assertionPoint(String gameId, Double score, String playerId, String conceptName) {
        List<PlayerState> states = playerSrv.loadStates(gameId);
        StateAnalyzer analyzer = new StateAnalyzer(states);
        Assert.assertEquals(
                String.format("Failure point concept %s for  player %s", conceptName, playerId),
                score.doubleValue(), analyzer.getScore(analyzer.findPlayer(playerId), conceptName),
                0);
    }

    public void assertionCustomData(String gameId, String playerId, String key, Object value) {
        List<PlayerState> states = playerSrv.loadStates(gameId);
        StateAnalyzer analyzer = new StateAnalyzer(states);
        PlayerState ps = analyzer.findPlayer(playerId);
        Assert.assertNotNull(String.format("player %s has not state in game %s", playerId, gameId),
                ps);
        Object dataValue = ps.getCustomData().get(key);
        Assert.assertNotNull(String.format("customData %s of player %s not exist in game %s", key,
                playerId, gameId), dataValue);
        Assert.assertEquals(String.format("customData %s of player %s", key, playerId), value,
                dataValue);
    }

    public void assertionAnyCustomData(String gameId, String playerId,
            Map<String, Object> customData) {
        List<PlayerState> states = playerSrv.loadStates(gameId);
        StateAnalyzer analyzer = new StateAnalyzer(states);
        PlayerState ps = analyzer.findPlayer(playerId);
        Assert.assertNotNull(String.format("player %s has not state in game %s", playerId, gameId),
                ps);
        for (Entry<String, Object> entry : customData.entrySet()) {
            Assert.assertThat(String.format("customData of player %s", playerId),
                    ps.getCustomData().entrySet(), CoreMatchers.hasItem((entry)));
        }
    }

    public void assertionSameCustomData(String gameId, String playerId,
            Map<String, Object> customData) {
        List<PlayerState> states = playerSrv.loadStates(gameId);
        StateAnalyzer analyzer = new StateAnalyzer(states);
        PlayerState ps = analyzer.findPlayer(playerId);
        Assert.assertNotNull(String.format("player %s has not state in game %s", playerId, gameId),
                ps);
        Assert.assertEquals(String.format("customData of player %s", playerId), customData,
                ps.getCustomData());
    }

    protected class ExecData {
        private String gameId;
        private String actionId;
        private String playerId;
        private Map<String, Object> data;
        private List<Object> factObjects;

        public String getActionId() {
            return actionId;
        }

        public void setActionId(String actionId) {
            this.actionId = actionId;
        }

        public String getPlayerId() {
            return playerId;
        }

        public void setPlayerId(String playerId) {
            this.playerId = playerId;
        }

        public Map<String, Object> getData() {
            return data;
        }

        public void setData(Map<String, Object> data) {
            this.data = data;
        }

        public ExecData(String gameId, String actionId, String playerId, Map<String, Object> data,
                List<Object> factObjects) {
            this.gameId = gameId;
            this.actionId = actionId;
            this.playerId = playerId;
            this.data = data;
            this.factObjects = factObjects;
        }

        public ExecData(String gameId, String actionId, String playerId, Map<String, Object> data) {
            this.gameId = gameId;
            this.actionId = actionId;
            this.playerId = playerId;
            this.data = data;
        }

        public String getGameId() {
            return gameId;
        }

        public void setGameId(String gameId) {
            this.gameId = gameId;
        }

        public List<Object> getFactObjects() {
            return factObjects;
        }

        public void setFactObjects(List<Object> factObjects) {
            this.factObjects = factObjects;
        }

    }

    protected class PointConceptBuilder {
        private String name;
        private Double score;

        public GameConcept build() {
            PointConcept gc = new PointConcept(name);
            gc.setScore(score);
            return gc;
        }

        public PointConceptBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public PointConceptBuilder setScore(Double score) {
            this.score = score;
            return this;
        }
    }

    protected class BadgeCollectionConceptBuilder {
        private String name;
        private List<String> badges = new ArrayList<String>();

        public BadgeCollectionConceptBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public BadgeCollectionConceptBuilder addBadge(String badge) {
            badges.add(badge);
            return this;
        }

        public BadgeCollectionConceptBuilder setBadges(List<String> badges) {
            this.badges = badges;
            return this;
        }

        public GameConcept build() {
            BadgeCollectionConcept gc = new BadgeCollectionConcept(name);
            gc.setBadgeEarned(badges);
            return gc;
        }
    }

    protected class StateAnalyzer {
        private List<PlayerState> s;

        public StateAnalyzer(List<PlayerState> s) {
            this.s = s;

        }

        public double getScore(PlayerState ps, String name) {
            for (GameConcept gc : ps.getState()) {
                if (gc instanceof PointConcept && gc.getName().equals(name)) {
                    return ((PointConcept) gc).getScore();
                }
            }

            return 0d;
        }

        public List<String> getBadges(PlayerState ps, String name) {
            for (GameConcept gc : ps.getState()) {
                if (gc instanceof BadgeCollectionConcept && gc.getName().equals(name)) {
                    return ((BadgeCollectionConcept) gc).getBadgeEarned();
                }
            }

            return Collections.<String>emptyList();
        }

        public PlayerState findPlayer(String playerId) {
            for (PlayerState ps : s) {
                if (ps.getPlayerId().equals(playerId)) {
                    return ps;
                }
            }

            return null;
        }
    }

}
