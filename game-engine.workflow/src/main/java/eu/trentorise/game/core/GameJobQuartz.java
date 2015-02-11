package eu.trentorise.game.core;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class GameJobQuartz extends QuartzJobBean {

	private final Logger logger = LoggerFactory.getLogger(GameJobQuartz.class);

	private String gameId;
	private String taskName;

	public GameJobQuartz() {
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		try {
			GameContext gameCtx = (GameContext) arg0.getScheduler()
					.getContext().get(gameId);
			GameTask task = (GameTask) arg0.getScheduler().getContext()
					.get(taskName);
			task.execute(gameCtx);
		} catch (SchedulerException e) {
			logger.error("Error getting gameContext in game task execution");
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
