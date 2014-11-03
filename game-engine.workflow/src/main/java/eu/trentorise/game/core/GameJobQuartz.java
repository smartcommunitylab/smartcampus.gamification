package eu.trentorise.game.core;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class GameJobQuartz extends QuartzJobBean {

	private final Logger logger = LoggerFactory.getLogger(GameJobQuartz.class);

	private GameTask task;
	private GameContext gameCtx;

	public GameJobQuartz(GameTask task, GameContext gameContext) {
		this.task = task;
		gameCtx = gameContext;
	}

	public GameJobQuartz() {
	}

	@Override
	protected void executeInternal(JobExecutionContext arg0)
			throws JobExecutionException {
		logger.info("RUN TASK");
		// task.execute(gameCtx);
	}

	public GameTask getTask() {
		return task;
	}

	public void setTask(GameTask task) {
		this.task = task;
	}

	public GameContext getGameCtx() {
		return gameCtx;
	}

	public void setGameCtx(GameContext gameCtx) {
		this.gameCtx = gameCtx;
	}

}
