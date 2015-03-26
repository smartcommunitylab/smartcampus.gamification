package eu.trentorise.game.managers;

import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.GameEngine;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.services.Workflow;

@Component
public class GameWorkflow implements Workflow {

	private final Logger logger = org.slf4j.LoggerFactory
			.getLogger(GameWorkflow.class);

	@Autowired
	protected GameEngine gameEngine;

	@Autowired
	protected PlayerService playerSrv;

	@Autowired
	protected GameService gameSrv;

	protected void workflowExec(String gameId, String actionId, String userId,
			Map<String, Object> data) {
		logger.info("gameId:{}, actionId: {}, playerId: {}, data: {}", gameId,
				actionId, userId, data);

		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g == null || g.getActions() == null
				|| !g.getActions().contains(actionId)) {
			throw new IllegalArgumentException(String.format(
					"game %s not exist or action %s not belong to it", gameId,
					actionId));
		}

		PlayerState playerState = playerSrv.loadState(userId, gameId);

		PlayerState newState = gameEngine.execute(gameId, playerState,
				actionId, data);

		boolean result = playerSrv.saveState(newState);

		logger.info("Process terminated: {}", result);
	}

	public void apply(String gameId, String actionId, String userId,
			Map<String, Object> data) {
		workflowExec(gameId, actionId, userId, data);

	}
}
