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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PreDestroy;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
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
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.TimeInterval;
import eu.trentorise.game.repo.GamePersistence;
import eu.trentorise.game.repo.GameRepo;
import eu.trentorise.game.services.PlayerService;
import eu.trentorise.game.task.AutoChallengeChoiceTask;

@Component
public class QuartzTaskManager extends TaskDataManager {

	@Autowired
	private Scheduler scheduler;

	@Autowired
	private GameRepo gameRepo;

	@Autowired
	private AppContextProvider provider;

    @Autowired
    private PlayerService playerSrv;

	private final Logger logger = LoggerFactory.getLogger(QuartzTaskManager.class);

	private void init() {
		try {
			List<Game> result = new ArrayList<Game>();
			for (GamePersistence gp : gameRepo.findByTerminated(false)) {
				result.add(gp.toGame());
			}
			for (Game g : result) {
				for (GameTask gt : g.getTasks()) {
					scheduler.getContext().put(g.getId() + ":" + gt.getName(), createGameCtx(g.getId(), gt));
					LogHub.debug(g.getId(), logger, "Added gameCtx of game {} to scheduler ctx",
							g.getId() + ":" + g.getName());
					scheduler.getContext().put(gt.getName(), gt);
					LogHub.debug(g.getId(), logger, "Added {} task to scheduler ctx", gt.getName());
				}
			}
			LogHub.debug(null, logger, "Init scheduler ctx");
			scheduler.start();
			LogHub.debug(null, logger, "Scheduler started");
		} catch (SchedulerException e) {
			LogHub.error(null, logger, "Scheduler not started: {}", e.getMessage());
		}
	}

	private GameContext createGameCtx(String gameId, GameTask task) {
		return (GameContext) provider.getApplicationContext().getBean("gameCtx", gameId, task);
	}

	@PreDestroy
	private void shutdown() {
		try {
			scheduler.shutdown();
		} catch (SchedulerException e) {
			logger.error("Scheduler shutdown problem: {}", e.getMessage());
			LogHub.error(null, logger, "Scheduler shutdown problem: {}", e.getMessage());
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
			if (!scheduler.getContext().containsKey(ctx.getGameRefId() + ":" + task.getName())) {
				scheduler.getContext().put(ctx.getGameRefId() + ":" + task.getName(), ctx);
				LogHub.debug(gameId, logger, "Added gameCtx {} to scheduler ctx",
						ctx.getGameRefId() + ":" + task.getName());
			}

            if (!scheduler.getContext().containsKey(ctx.getGameRefId() + ":playerSrv")
                    && task.getClass() == AutoChallengeChoiceTask.class) {
                scheduler.getContext().put(ctx.getGameRefId() + ":playerSrv", playerSrv);
                LogHub.debug(gameId, logger, "Added playerSrv {} to scheduler ctx",
                        ctx.getGameRefId() + ":playerSrv");
            }
			if (!scheduler.getContext().containsKey(task.getName())) {
                scheduler.getContext().put(task.getName(), task);
				LogHub.debug(gameId, logger, "Added {} task to scheduler ctx", task.getName());
			}

			// schedule task
			if (!scheduler.checkExists(new JobKey(task.getName(), ctx.getGameRefId()))
					&& !scheduler.checkExists(new TriggerKey(task.getName(), ctx.getGameRefId()))) {
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

				Trigger trigger = createTrigger(task, gameId, job);
				scheduler.scheduleJob(job, trigger);
				LogHub.info(gameId, logger, "Created and started job task {} in group {}", task.getName(),
						ctx.getGameRefId());
			} else {
				LogHub.info(gameId, logger, "Job task {} in group {} already exists", task.getName(),
						ctx.getGameRefId());
			}

		} catch (Exception e) {
			LogHub.error(gameId, logger, "Exception creating tasks {}", e.getMessage());

		}
	}

	private Trigger createTrigger(GameTask task, String gameId, JobDetail job) {
		if (task.getSchedule().getCronExpression() != null) {
			CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
			String cronExpression = task.getSchedule().getCronExpression();
			// fix for version 2.2.1 of CronTrigger
			String cron = fixCronExpression(cronExpression);
			triggerFactory.setCronExpression(cron);
			LogHub.info(null, logger, "fix cron expression for Quartz 2.2.1 issue: {}", cron);
			triggerFactory.setName(task.getName());
			triggerFactory.setGroup(gameId);
			triggerFactory.setJobDetail(job);
			try {
				triggerFactory.afterPropertiesSet();
			} catch (ParseException e) {
				LogHub.error(gameId, logger, "Error creating task trigger", e);
				return null;
			}
			return triggerFactory.getObject();
		} else {
			CalendarIntervalTriggerImpl calTrigger = new CalendarIntervalTriggerImpl();
			calTrigger.setName(task.getName());
			calTrigger.setGroup(gameId);
			LogHub.info(gameId, logger, "Scheduled startTime of task {} group {}: {} ", task.getName(), gameId,
					task.getSchedule().getStart());
			Date calculatedStart = calculateStartDate(task.getSchedule().getStart(), task.getSchedule().getPeriod());
			LogHub.info(gameId, logger, "Set start task {} group {} on next triggerDate: {}", task.getName(), gameId,
					calculatedStart);
			int delayMillis = getDelayInMillis(task.getSchedule().getDelay());
			calTrigger.setStartTime(new DateTime(calculatedStart).plusMillis(delayMillis).toDate());
			if (delayMillis != 0) {
				LogHub.info(gameId, logger, "Delay setted: {} millis, recalculated triggerDate: {}", delayMillis,
						calTrigger.getStartTime());
			}
			Repeat repeat = extractRepeat((int) task.getSchedule().getPeriod());
			LogHub.debug(gameId, logger, "extract repeat every {} unit {}", repeat.getInterval(),
					repeat.getUnit().toString());
            calTrigger.setRepeatInterval(repeat.getInterval());
            calTrigger.setRepeatIntervalUnit(repeat.getUnit());
            return calTrigger;

		}

	}

	private Date calculateStartDate(Date initialStart, long period) {
        if (period <= 0) {
            return initialStart;
        }
		LocalDateTime start = new LocalDateTime(initialStart);
		while (start.toDateTime().isBeforeNow()) {
			start = start.plusMillis((int) period);
		}

		return start.toDate();
	}

	private int getDelayInMillis(TimeInterval delay) {
		int value = 0;
		if (delay != null) {
			switch (delay.getUnit()) {
			case DAY:
				value = delay.getValue() * 24 * 3600000;
				break;
			case HOUR:
				value = delay.getValue() * 3600000;
				break;
			case MINUTE:
				value = delay.getValue() * 60000;
				break;
			case SEC:
				value = delay.getValue() * 1000;
				break;
			case MILLISEC:
				value = delay.getValue();
				break;
			default:
				break;
			}
		}

		return value;
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
					throw new IllegalArgumentException("period must representing valid minutes, hours or days value");
				}
			}
		}
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

			// logger.info("fix cron expression for Quartz 2.2.1 issue: {}",
			// cron);

		}
		return cron;
	}

	public boolean destroyTask(GameTask task, String gameId) {
		boolean operationResult = false;
		try {
			if (scheduler.isStarted()) {

				operationResult = scheduler.deleteJob(new JobKey(task.getName(), gameId));
				if (operationResult) {
					LogHub.info(gameId, logger, "task {} destroyed", task.getName());
					deleteData(gameId, task.getName());
					LogHub.info(gameId, logger, "data of task {} deleted", task.getName());
				}
				return operationResult;
			}
		} catch (SchedulerException e) {
			LogHub.error(gameId, logger, "Scheduler exception removing task {}", task.getName());
		}
		return operationResult;
	}

	@Override
	public void updateTask(GameTask task, String gameId) {
		JobDetail job;
		try {
			job = scheduler.getJobDetail(new JobKey(task.getName(), gameId));
			if (job != null) {
				Trigger trigger = createTrigger(task, gameId, job);
				scheduler.rescheduleJob(new TriggerKey(task.getName(), gameId), trigger);
				LogHub.info(gameId, logger, "task {} updated", task.getName());
			} else {
				LogHub.warn(gameId, logger, "job task {} not found, task not updated", task.getName());
			}
		} catch (SchedulerException e) {
			LogHub.error(gameId, logger, "SchedulerException: task {} not updated", task.getName());
		}
	}
}
