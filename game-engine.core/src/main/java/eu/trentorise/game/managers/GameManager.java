package eu.trentorise.game.managers;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.model.ClasspathRule;
import eu.trentorise.game.model.DBRule;
import eu.trentorise.game.model.FSRule;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.Rule;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GameRepo;
import eu.trentorise.game.repo.GenericObjectPersistence;
import eu.trentorise.game.repo.RuleRepo;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.TaskService;

@Component
public class GameManager implements GameService {

	private final Logger logger = LoggerFactory.getLogger(GameManager.class);

	@Autowired
	TaskService taskSrv;

	@Autowired
	AppContextProvider provider;

	@Autowired
	GameRepo gameRepo;

	@Autowired
	RuleRepo ruleRepo;

	@PostConstruct
	@SuppressWarnings("unused")
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
				taskSrv.createTask(
						task,
						(GameContext) provider.getApplicationContext().getBean(
								"gameCtx", gameId, task));
			}
		}

	}

	public Game saveGameDefinition(Game game) {
		GamePersistence pers = null;
		if (game.getId() != null) {
			pers = gameRepo.findOne(game.getId());
			if (pers != null) {
				pers.setActions(game.getActions());
				pers.setExpiration(game.getExpiration());
				pers.setTerminated(game.isTerminated());
				pers.setRules(game.getRules());

				if (game.getConcepts() != null) {
					Set<GenericObjectPersistence> concepts = new HashSet<GenericObjectPersistence>();
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
					}
					pers.setTasks(tasks);
				} else {
					pers.setTasks(null);
				}
			} else {
				pers = new GamePersistence(game);
			}
		} else {
			pers = new GamePersistence(game);
		}
		pers = gameRepo.save(pers);
		return pers.toGame();
	}

	public Game loadGameDefinitionById(String gameId) {
		GamePersistence gp = gameRepo.findOne(gameId);
		return gp == null ? null : gp.toGame();
	}

	public List<Game> loadGames(boolean onlyActive) {
		List<Game> result = new ArrayList<Game>();
		for (GamePersistence gp : gameRepo.findByTerminated(!onlyActive)) {
			result.add(gp.toGame());
		}
		return result;
	}

	public List<Game> loadAllGames() {
		List<Game> result = new ArrayList<Game>();
		for (GamePersistence gp : gameRepo.findAll()) {
			result.add(gp.toGame());
		}
		return result;
	}

	public String addRule(Rule rule) {
		String ruleUrl = null;
		if (rule != null) {
			Game game = loadGameDefinitionById(rule.getGameId());
			if (game != null) {
				if (rule instanceof ClasspathRule) {
					ruleUrl = "classpath://" + ((ClasspathRule) rule).getUrl();
				}

				if (rule instanceof FSRule) {
					ruleUrl = "file://" + ((FSRule) rule).getUrl();
				}

				if (rule instanceof DBRule) {
					if (((DBRule) rule).getId() != null) {
						((DBRule) rule).setId(((DBRule) rule).getId().replace(
								"db://", ""));
					}
					rule = ruleRepo.save((DBRule) rule);
					ruleUrl = "db://" + ((DBRule) rule).getId();
				}

				game.getRules().add(ruleUrl);
				saveGameDefinition(game);
			} else {
				logger.error("Game {} not found", rule.getGameId());
			}
		}
		return ruleUrl;
	}

	public Rule loadRule(String gameId, String url) {
		Rule rule = null;
		if (url != null) {
			if (url.startsWith("db://")) {
				url = url.substring("db://".length());
				return ruleRepo.findOne(url);
			} else if (url.startsWith("classpath://")) {
				url = url.substring("classpath://".length());
				if (Thread.currentThread().getContextClassLoader()
						.getResource(url) != null) {
					return new ClasspathRule(gameId, url);
				}

			} else if (url.startsWith("file://")) {
				url = url.substring("file://".length());
				if (new File(url).exists()) {
					return new FSRule(gameId, url);
				}
			}
		}
		return rule;
	}

	@Scheduled(cron = "0 0 1 * * *")
	public void taskDestroyer() {
		logger.info("task destroyer invocation");
		long deadline = System.currentTimeMillis();

		List<Game> games = loadGames(true);
		for (Game game : games) {
			if (game.getExpiration() > 0 && game.getExpiration() < deadline) {
				for (GameTask task : game.getTasks()) {
					if (taskSrv.destroyTask(task, game.getId())) {
						logger.info("Destroy task - {} - of game {}",
								task.getName(), game.getId());
					}
				}
				game.setTerminated(true);
				saveGameDefinition(game);
			}
		}

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
		}

		saveGameDefinition(g);
	}

	@Override
	public Set<GameConcept> readConceptInstances(String gameId) {
		Game g = loadGameDefinitionById(gameId);
		if (g != null) {
			return g.getConcepts() != null ? g.getConcepts() : Collections
					.<GameConcept> emptySet();
		} else {
			return Collections.<GameConcept> emptySet();
		}
	}

	@Override
	public boolean deleteRule(String gameId, String url) {
		Game g = loadGameDefinitionById(gameId);
		boolean res = false;
		if (g != null && url != null && url.indexOf("db://") != -1) {
			String id = url.substring(5);
			ruleRepo.delete(id);
			res = g.getRules().remove(url);
			saveGameDefinition(g);
		}

		return res;
	}
}
