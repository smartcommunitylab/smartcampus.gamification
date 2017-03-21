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
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.LogHub;

/**
 * A game workflow that manage sequential execution queue of gameEngine to fix
 * issue https://github.com/smartcampuslab/smartcampus.gamification/issues/1
 * 
 * @author mirko perillo
 * 
 */
@Component
public class QueueGameWorkflow extends GameWorkflow {

	private final Logger logger = org.slf4j.LoggerFactory.getLogger(QueueGameWorkflow.class);

	private static ExecutorService executor = Executors.newSingleThreadExecutor();

	@Override
	public void apply(String gameId, String actionId, String userId, Map<String, Object> data,
			List<Object> factObjects) {
		try {
			String executionId = UUID.randomUUID().toString();
			long executionMoment = System.currentTimeMillis();
			executor.execute(new Execution(gameId, actionId, userId, executionId, executionMoment, data, factObjects));
		} catch (Exception e) {
			// logger.error("Exception in game queue execution", e);
			LogHub.error(gameId, logger, "Exception in game queue execution", e);
		}

	}

	class Execution implements Runnable {

		private String gameId;
		private String actionId;
		private String userId;
		private String executionId;
		private long executionMoment;
		private Map<String, Object> data;
		private List<Object> factObjects;

		public Execution(String gameId, String actionId, String userId, String executionId, long executionMoment,
				Map<String, Object> data, List<Object> factObjects) {
			this.gameId = gameId;
			this.actionId = actionId;
			this.userId = userId;
			this.executionId = executionId;
			this.executionMoment = executionMoment;
			this.data = data;
			this.factObjects = factObjects;

		}

		public void run() {
			workflowExec(gameId, actionId, userId, executionId, executionMoment, data, factObjects);
		}

	}
}
