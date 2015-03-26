package eu.trentorise.game.managers;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

/**
 * A game workflow that manage sequential execution queue of gameEngine to fix
 * issue https://github.com/smartcampuslab/smartcampus.gamification/issues/1
 * 
 * @author mirko perillo
 * 
 */
@Component
public class QueueGameWorkflow extends GameWorkflow {

	private final Logger logger = org.slf4j.LoggerFactory
			.getLogger(QueueGameWorkflow.class);

	private static ExecutorService executor = Executors
			.newSingleThreadExecutor();

	@Override
	public void apply(String gameId, String actionId, String userId,
			Map<String, Object> data) {
		try {
			executor.execute(new Execution(gameId, actionId, userId, data));
		} catch (Exception e) {
			logger.error("Exception in game queue execution", e);
		}

	}

	class Execution implements Runnable {

		private String gameId;
		private String actionId;
		private String userId;
		private Map<String, Object> data;

		public Execution(String gameId, String actionId, String userId,
				Map<String, Object> data) {
			this.gameId = gameId;
			this.actionId = actionId;
			this.userId = userId;
			this.data = data;
		}

		public void run() {

			workflowExec(gameId, actionId, userId, data);
			// logger.info("actionId: {}, playerId: {}, data: {}", actionId,
			// userId, data);
			// String gameId = gameSrv.getGameIdByAction(actionId);
			//
			// PlayerState playerState = playerSrv.loadState(userId, gameId);
			//
			// PlayerState newState = gameEngine.execute(gameId, playerState,
			// actionId, data);
			//
			// boolean result = playerSrv.saveState(newState);
			//
			// logger.info("Process terminated: {}", result);
		}

	}
}
