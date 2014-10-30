package eu.trentorise.game.managers;

import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;

@Component
public class TaskManager {

	private final Logger logger = LoggerFactory.getLogger(TaskManager.class);
	@Autowired
	ThreadPoolTaskScheduler scheduler;

	public void createTask(GameTask task, GameContext ctx) {
		CronTrigger trigger = new CronTrigger(task.getSchedule()
				.getCronExpression());

		ScheduledFuture schedulefuture = scheduler.schedule(new TaskRun(task,
				ctx), trigger);

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
			// gameTask.execute(gameCtx);
		}

	}
}
