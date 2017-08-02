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

package eu.trentorise.game.core;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import eu.trentorise.game.model.core.GameTask;

public class GameJobQuartz extends QuartzJobBean {

	private final Logger logger = LoggerFactory.getLogger(GameJobQuartz.class);

	private String gameId;
	private String taskName;

	public GameJobQuartz() {
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		try {
			GameContext gameCtx = (GameContext) arg0.getScheduler().getContext().get(gameId + ":" + taskName);
			GameTask task = (GameTask) arg0.getScheduler().getContext().get(taskName);
			task.execute(gameCtx);
		} catch (SchedulerException e) {
			LogHub.error(gameId, logger, "Error getting gameContext in game task execution");
		}
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

}
