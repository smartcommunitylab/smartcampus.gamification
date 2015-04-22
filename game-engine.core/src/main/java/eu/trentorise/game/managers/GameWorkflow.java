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

package eu.trentorise.game.managers;

import java.util.List;
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
			Map<String, Object> data, List<Object> factObjects) {
		logger.info(
				"gameId:{}, actionId: {}, playerId: {}, data: {}, factObjs: {}",
				gameId, actionId, userId, data, factObjects);

		Game g = gameSrv.loadGameDefinitionById(gameId);
		if (g == null || g.getActions() == null
				|| !g.getActions().contains(actionId)) {
			throw new IllegalArgumentException(String.format(
					"game %s not exist or action %s not belong to it", gameId,
					actionId));
		}

		PlayerState playerState = playerSrv.loadState(userId, gameId);

		PlayerState newState = gameEngine.execute(gameId, playerState,
				actionId, data, factObjects);

		boolean result = playerSrv.saveState(newState);

		logger.info("Process terminated: {}", result);
	}

	public void apply(String gameId, String actionId, String userId,
			Map<String, Object> data, List<Object> factObjects) {
		workflowExec(gameId, actionId, userId, data, factObjects);

	}
}
