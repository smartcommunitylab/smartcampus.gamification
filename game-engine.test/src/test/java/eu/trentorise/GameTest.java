/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.managers.GameManager;
import eu.trentorise.game.managers.QueueGameWorkflow;
import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.FSRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class, MongoConfig.class }, loader = AnnotationConfigContextLoader.class)
public abstract class GameTest {

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

	public void savePlayerState(String gameId, String playerId,
			List<GameConcept> concepts) {
		PlayerState player = new PlayerState();
		player.setGameId(gameId);
		player.setPlayerId(playerId);
		player.setState(new HashSet<GameConcept>(concepts));
		mongo.save(new StatePersistence(player));
	}

	public void defineGameHelper(String gameId, List<String> actions,
			List<GameConcept> concepts) {
		Game g = new Game();
		g.setId(gameId);
		g.setName(gameId);
		g.setActions(new HashSet<String>(actions));

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
			throw new IllegalArgumentException(String.format(
					"please create game {} before call addGameTask", gameId));
		}
		tasks.add(gt);
	}

	public void loadClasspathRules(String gameId, List<String> rulesPath) {
		for (String path : rulesPath) {
			gameManager.addRule(new ClasspathRule(gameId, path));
		}
	}

	public void loadFilesystemRules(String gameId, List<String> rulesPath) {
		for (String path : rulesPath) {
			gameManager.addRule(new FSRule(gameId, path));
		}
	}

	private void runEngine() {
		List<ExecData> execList = new ArrayList<GameTest.ExecData>();
		defineExecData(execList);
		for (ExecData ex : execList) {
			workflow.apply(ex.gameId, ex.getActionId(), ex.getPlayerId(),
					ex.getData(), null);
		}

		// launch Task sequentially

		for (GameTask task : tasks) {
			task.execute((GameContext) provider.getApplicationContext()
					.getBean("gameCtx", gameId, task));
		}

	}

	public void assertionBadge(String gameId, List<String> values,
			String playerId, String conceptName) {
		List<PlayerState> states = playerSrv.loadStates(gameId);
		StateAnalyzer analyzer = new StateAnalyzer(states);
		List<String> badgesEarned = analyzer.getBadges(
				analyzer.findPlayer(playerId), conceptName);
		Assert.assertTrue(
				String.format(
						"Failure badgecollection concept %s for  player %s",
						conceptName, playerId),
				new HashSet<String>(values).containsAll(badgesEarned)
						&& values.size() == badgesEarned.size());
	}

	public void assertionPoint(String gameId, Double score, String playerId,
			String conceptName) {
		List<PlayerState> states = playerSrv.loadStates(gameId);
		StateAnalyzer analyzer = new StateAnalyzer(states);
		Assert.assertTrue(String.format(
				"Failure point concept %s for  player %s", conceptName,
				playerId), score.equals(analyzer.getScore(
				analyzer.findPlayer(playerId), conceptName)));
	}

	protected class ExecData {
		private String gameId;
		private String actionId;
		private String playerId;
		private Map<String, Object> data;

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

		public ExecData(String actionId, String playerId,
				Map<String, Object> data) {
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
				if (gc instanceof BadgeCollectionConcept
						&& gc.getName().equals(name)) {
					return ((BadgeCollectionConcept) gc).getBadgeEarned();
				}
			}

			return Collections.<String> emptyList();
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
