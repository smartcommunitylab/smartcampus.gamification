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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameJobQuartz;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GameRepo;

@Component
public class QuartzTaskManager extends TaskDataManager {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private GameRepo gameRepo;

	@Autowired
	private AppContextProvider provider;

	private final Logger logger = LoggerFactory
			.getLogger(QuartzTaskManager.class);

	private static final int DAY = 24 * 60 * 60 * 1000;

	private void init() {
		try {
			List<Game> result = new ArrayList<Game>();
			for (GamePersistence gp : gameRepo.findByTerminated(false)) {
				result.add(gp.toGame());
			}
			for (Game g : result) {
				for (GameTask gt : g.getTasks()) {
					scheduler.getContext().put(g.getId() + ":" + gt.getName(),
							createGameCtx(g.getId(), gt));
					logger.debug("Added gameCtx of game {} to scheduler ctx",
							g.getId() + ":" + g.getName());
					scheduler.getContext().put(gt.getName(), gt);
					logger.debug("Added {} task to scheduler ctx", gt.getName());
				}
			}
			logger.debug("Init scheduler ctx");
			scheduler.start();
			logger.debug("Scheduler started");
		} catch (SchedulerException e) {
			logger.error("Scheduler not started: {}", e.getMessage());
		}
	}

	private GameContext createGameCtx(String gameId, GameTask task) {
		return (GameContext) provider.getApplicationContext().getBean(
				"gameCtx", gameId, task);
	}

	@PreDestroy
	@SuppressWarnings("unused")
	private void shutdown() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error("Scheduler shutdown problem: {}", e.getMessage());
		}
	}

	public void createTask(GameTask task, String gameId) {
		try {

			// start the scheduler
			// init in postcontruct not possible cause circolar reference of
			// gameCtx
			if (!scheduler.isStarted()) {
				init();
			}

			GameContext ctx = createGameCtx(gameId, task);
			// check scheduler context data
			if (!scheduler.getContext().containsKey(
					ctx.getGameRefId() + ":" + task.getName())) {
				scheduler.getContext().put(
						ctx.getGameRefId() + ":" + task.getName(), ctx);
				logger.debug("Added gameCtx {} to scheduler ctx",
						ctx.getGameRefId() + ":" + task.getName());
			}
			if (!scheduler.getContext().containsKey(task.getName())) {
				scheduler.getContext().put(task.getName(), task);
				logger.debug("Added {} task to scheduler ctx", task.getName());
			}

			// schedule task
			if (!scheduler.checkExists(new JobKey(task.getName(), ctx
					.getGameRefId()))
					&& !scheduler.checkExists(new TriggerKey(task.getName(),
							ctx.getGameRefId()))) {
				JobDetailFactoryBean jobFactory = new JobDetailFactoryBean();
				jobFactory.setJobClass(GameJobQuartz.class);
				Map<String, Object> jobdata = new HashMap<String, Object>();
				jobdata.put("taskName", task.getName());
				jobdata.put("gameId", ctx.getGameRefId());
				jobFactory.setName(task.getName());
				jobFactory.setGroup(ctx.getGameRefId());
				jobFactory.setJobDataAsMap(jobdata);
				jobFactory.afterPropertiesSet();
				JobDetail job = jobFactory.getObject();

				Trigger trigger = null;
				if (task.getSchedule().getCronExpression() != null) {
					CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
					String cronExpression = task.getSchedule()
							.getCronExpression();
					// fix for version 2.2.1 of CronTrigger
					triggerFactory
							.setCronExpression(fixCronExpression(cronExpression));
					triggerFactory.setName(task.getName());
					triggerFactory.setGroup(ctx.getGameRefId());
					triggerFactory.setJobDetail(job);
					triggerFactory.afterPropertiesSet();
					trigger = triggerFactory.getObject();
				} else {
					CalendarIntervalTriggerImpl calTrigger = new CalendarIntervalTriggerImpl();
					calTrigger.setName(task.getName());
					calTrigger.setGroup(ctx.getGameRefId());
					calTrigger.setStartTime(task.getSchedule().getStart());
					Repeat repeat = extractRepeat((int) task.getSchedule()
							.getPeriod());
					calTrigger.setRepeatInterval(repeat.getInterval());
					calTrigger.setRepeatIntervalUnit(repeat.getUnit());
					trigger = calTrigger;

				}
				scheduler.scheduleJob(job, trigger);
				logger.info("Created and started job task {} in group {}",
						task.getName(), ctx.getGameRefId());
			} else {
				logger.info("Job task {} in group {} already exists",
						task.getName(), ctx.getGameRefId());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private Repeat extractRepeat(int period) {
		final int MILLIS_IN_MINUTE = 60000;
		final int MILLIS_IN_HOUR = 3600000;
		final int MILLIS_IN_DAY = 86400000;
		int result = period / MILLIS_IN_DAY;
		IntervalUnit unit = null;
		if (result * MILLIS_IN_DAY == period) {
			unit = IntervalUnit.DAY;
		} else {
			result = period / MILLIS_IN_HOUR;
			if (result * MILLIS_IN_HOUR == period) {
				unit = IntervalUnit.HOUR;
			} else {
				result = period / MILLIS_IN_MINUTE;
				if (result * MILLIS_IN_MINUTE == period) {
					unit = IntervalUnit.MINUTE;
				} else {
					throw new IllegalArgumentException(
							"period must representing valid minutes, hours or days value");
				}
			}
		}
		logger.debug("extract repeat every {} unit {}", result, unit.toString());
		return new Repeat(result, unit);

	}

	private class Repeat {
		private int interval;
		private IntervalUnit unit;

		public Repeat(int interval, IntervalUnit unit) {
			this.interval = interval;
			this.unit = unit;
		}

		public int getInterval() {
			return interval;
		}

		public IntervalUnit getUnit() {
			return unit;
		}

	}

	/**
	 * fix actual cronTrigger limitation (v 2.2.1) to support some type of
	 * cronExpression
	 * 
	 * from CronTrigger documentation: Support for specifying both a day-of-week
	 * and a day-of-month value is not complete (you'll need to use the '?'
	 * character in one of these fields).
	 * 
	 * @param cronExp
	 * @return
	 */
	private String fixCronExpression(String cronExp) {
		String cron = null;
		if (cronExp != null) {
			String[] tokens = cronExp.split(" ");
			int dayOfMonthPosition = 3;
			int dayOfWeekPosition = 5;
			if (!"?".equals(tokens[dayOfMonthPosition])) {
				tokens[dayOfWeekPosition] = "?";
			}
			cron = StringUtils.join(tokens, " ");

			logger.info("fix cron expression for Quartz 2.2.1 issue: {}", cron);
		}
		return cron;
	}

	public boolean destroyTask(GameTask task, String gameId) {
		boolean operationResult = false;
		try {
			if (scheduler.isStarted()) {

				operationResult = scheduler.deleteJob(new JobKey(
						task.getName(), gameId));
				if (operationResult) {
					logger.info("task {} destroyed", task.getName());
					deleteData(gameId, task.getName());
					logger.info("data of task {} deleted", task.getName());
				}
				return operationResult;
			}
		} catch (SchedulerException e) {
			logger.error("Scheduler exception removing task");
		}
		return operationResult;
	}

	@Override
	public void updateTask(GameTask task, String gameId) {
		JobDetail job;
		try {
			job = scheduler.getJobDetail(new JobKey(task.getName(), gameId));
			if (job != null) {
				Trigger trigger = null;
				if (task.getSchedule().getCronExpression() != null) {
					CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
					String cronExpression = task.getSchedule()
							.getCronExpression();
					// fix for version 2.2.1 of CronTrigger
					triggerFactory
							.setCronExpression(fixCronExpression(cronExpression));
					triggerFactory.setName(task.getName());
					triggerFactory.setGroup(gameId);
					triggerFactory.setJobDetail(job);
					triggerFactory.afterPropertiesSet();
					trigger = triggerFactory.getObject();
				} else {
					CalendarIntervalTriggerImpl calTrigger = new CalendarIntervalTriggerImpl();
					calTrigger.setName(task.getName());
					calTrigger.setGroup(gameId);
					calTrigger.setStartTime(task.getSchedule().getStart());
					Repeat repeat = extractRepeat((int) task.getSchedule()
							.getPeriod());
					calTrigger.setRepeatInterval(repeat.getInterval());
					calTrigger.setRepeatIntervalUnit(repeat.getUnit());
					trigger = calTrigger;
				}
				scheduler.rescheduleJob(new TriggerKey(task.getName(), gameId),
						trigger);
				logger.info("task {} updated", task.getName());
			} else {
				logger.warn("job task {} not found, task not updated",
						task.getName());
			}
		} catch (SchedulerException e) {
			logger.error("SchedulerException: task {} not updated",
					task.getName());
		} catch (ParseException e) {
			logger.error("ParseException: task {} not updated", task.getName());
		}
	}
}
