package eu.trentorise.game.managers;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameJobQuartz;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.services.TaskService;

@Component("quartzTaskManager")
public class QuartzTaskManager implements TaskService {

	@Autowired
	Scheduler scheduler;

	private final Logger logger = LoggerFactory
			.getLogger(QuartzTaskManager.class);

	public void createTask(GameTask task, GameContext ctx) {
		try {
			JobDetailFactoryBean jobFactory = new JobDetailFactoryBean();
			jobFactory.setJobClass(GameJobQuartz.class);
			Map<String, Object> jobdata = new HashMap<String, Object>();
			jobdata.put("task", task);
			jobdata.put("gameCtx", ctx);
			jobFactory.setJobDataAsMap(jobdata);
			jobFactory.setName("job_" + System.currentTimeMillis());
			jobFactory.afterPropertiesSet();
			JobDetail job = jobFactory.getObject();

			CronTriggerFactoryBean triggerFactory = new CronTriggerFactoryBean();
			String cronExpression = task.getSchedule().getCronExpression();
			// fix for version 2.2.1 of CronTrigger
			triggerFactory.setCronExpression(fixCronExpression(cronExpression));
			triggerFactory.setName("trigger_" + System.currentTimeMillis());
			triggerFactory.setJobDetail(job);
			triggerFactory.afterPropertiesSet();
			Trigger trigger = triggerFactory.getObject();
			scheduler.scheduleJob(job, trigger);
			scheduler.start();
		} catch (Exception e) {
			logger.error(e.getMessage());
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
		String[] tokens = cronExp.split(" ");
		int dayOfMonthPosition = 3;
		int dayOfWeekPosition = 5;
		if ("*".equals(tokens[dayOfMonthPosition])
				&& "*".equals(tokens[dayOfWeekPosition])) {
			tokens[dayOfWeekPosition] = "?";
		}

		return StringUtils.join(tokens, " ");
	}

}
