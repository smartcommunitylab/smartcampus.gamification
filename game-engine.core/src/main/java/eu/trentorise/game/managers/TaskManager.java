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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;

@Component
public class TaskManager extends TaskDataManager {

	private final Logger logger = LoggerFactory.getLogger(TaskManager.class);

	@Autowired
	private ThreadPoolTaskScheduler scheduler;

	@Autowired
	private AppContextProvider provider;

	private GameContext createGameCtx(String gameId, GameTask task) {
		return (GameContext) provider.getApplicationContext().getBean(
				"gameCtx", gameId, task);
	}

	public void createTask(GameTask task, String gameId) {
		CronTrigger trigger = new CronTrigger(task.getSchedule()
				.getCronExpression());
		GameContext ctx = createGameCtx(gameId, task);
		scheduler.schedule(new TaskRun(task, ctx), trigger);

	}

	class TaskRun implements Runnable {

		private GameTask gameTask;
		private GameContext gameCtx;

		public TaskRun(GameTask gt, GameContext ctx) {
			gameTask = gt;
			gameCtx = ctx;
		}

		public void run() {
			logger.info("RUN TASK");
			gameTask.execute(gameCtx);
		}

	}

	public boolean destroyTask(GameTask task, String gameId) {
		throw new UnsupportedOperationException("method not implemented !!");
	}
}
