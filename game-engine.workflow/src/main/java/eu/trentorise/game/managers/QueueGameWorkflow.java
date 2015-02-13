package eu.trentorise.game.managers;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

/**
 * A game workflow that manage sequential execution queue of gameEngine to fix
 * issue https://github.com/smartcampuslab/smartcampus.gamification/issues/1
 * 
 * @author mirko perillo
 * 
 */
@Component
public class QueueGameWorkflow implements Workflow {

	private final Logger logger = org.slf4j.LoggerFactory
			.getLogger(GameWorkflow.class);

	@Autowired
	private GameEngine gameEngine;

	@Autowired
	private PlayerService playerSrv;

	@Autowired
	private GameService gameSrv;

	private static ExecutorService executor = Executors
			.newSingleThreadExecutor();

	public void apply(String actionId, String userId, Map<String, Object> data) {
		try {
			executor.execute(new Execution(actionId, userId, data));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	class Execution implements Runnable {

		private String actionId;
		private String userId;
		private Map<String, Object> data;

		public Execution(String actionId, String userId,
				Map<String, Object> data) {
			this.actionId = actionId;
			this.userId = userId;
			this.data = data;
		}

		public void run() {

			logger.info("actionId: {}, playerId: {}, data: {}", actionId,
					userId, data);
			String gameId = gameSrv.getGameIdByAction(actionId);

			PlayerState playerState = playerSrv.loadState(userId, gameId);

			PlayerState newState = gameEngine.execute(gameId, playerState,
					actionId, data);

			boolean result = playerSrv.saveState(newState);

			logger.info("Process terminated: {}", result);
		}

	}
}
