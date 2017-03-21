package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.LogHub;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.managers.ClassificationFactory;
import eu.trentorise.game.managers.ClassificationFactory.ClassificationBuilder;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.PointConcept.Period;
import eu.trentorise.game.model.PointConcept.PeriodInstance;
import eu.trentorise.game.model.core.ClassificationPosition;
import eu.trentorise.game.model.core.ClassificationType;
import eu.trentorise.game.model.core.TimeInterval;

public class IncrementalClassificationTask extends ClassificationTask {

	private final Logger logger = LoggerFactory.getLogger(IncrementalClassificationTask.class);

	private String pointConceptName;
	private String periodName;
	private long periodLength;
	private long startupPeriodInstance;
	private int startupInstanceIndex;

	private static final String EXECUTION_NUMBER_PARAM = "executionNumber";
	private static final String LAST_INSTANCE_DATE_EXECUTED_PARAM = "lastInstanceDateExec";
	private static final String LAST_INSTANCE_INDEX_EXECUTED_PARAM = "lastIndexExec";

	public IncrementalClassificationTask(PointConcept pc, String periodName, String classificationName) {
		super();
		updatePointConceptData(pc, periodName, null);
		super.setClassificationName(classificationName);
		super.setName(classificationName);
	}

	public IncrementalClassificationTask(PointConcept pc, String periodName, String classificationName,
			TimeInterval delay) {
		super();
		updatePointConceptData(pc, periodName, delay);
		super.setClassificationName(classificationName);
		super.setName(classificationName);
	}

	public IncrementalClassificationTask() {
		super();
	}

	public final void updatePointConceptData(PointConcept pc, String periodName, TimeInterval delay) {
		if (pc != null) {
			Period period = pc.getPeriod(periodName);
			PeriodInstance periodInstance = pc.getPeriodCurrentInstance(periodName);
			pointConceptName = pc.getName();
			this.periodName = periodName;
			startupPeriodInstance = periodInstance.getStart();
			periodLength = period.getPeriod();
			startupInstanceIndex = periodInstance.getIndex();
			TaskSchedule schedule = new TaskSchedule();
			schedule.setStart(new Date(periodInstance.getEnd()));
			schedule.setPeriod(periodLength);
			schedule.setDelay(delay);
			super.setSchedule(schedule);
		}
	}

	@Override
	protected String getScoreType() {
		return pointConceptName;
	}

	/*
	 * TO REFACTOR..not used..override of execute method to manage
	 * incremantalMetainfo (non-Javadoc)
	 * 
	 * @see
	 * eu.trentorise.game.task.ClassificationTask#createClassificationObject
	 * (eu.trentorise.game.core.GameContext, double, java.lang.String, int)
	 */
	@Override
	protected Classification createClassificationObject(GameContext ctx, double score, String scoreType, int position) {
		return null;
	}

	/*
	 * Not good to override execute method of parent class
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * eu.trentorise.game.task.ClassificationTask#execute(eu.trentorise.game
	 * .core.GameContext)
	 */
	@Override
	public void execute(GameContext ctx) {
		if (ctx == null) {
			logger.warn("gameContext null");
			LogHub.warn(null, logger, "gameContext null");
			return;
		}

		// logger.debug("Execute of task {}", getClassificationName());
		LogHub.debug(ctx.getGameRefId(), logger, "Execute of task {}", getClassificationName());

		IncrementalClassificationTask execTask = (IncrementalClassificationTask) ctx.getTask();
		long periodLength = execTask.getPeriodLength();
		long startInstanceDate = execTask.getStartupPeriodInstance();
		int instanceIndex = execTask.getStartupInstanceIndex();
		String pointConceptName = execTask.getPointConceptName();
		String periodName = execTask.getPeriodName();

		// read all game players
		List<String> players = ctx.readPlayers();
		// load players status

		List<PlayerState> states = new ArrayList<PlayerState>();
		for (String p : players) {
			states.add(ctx.readStatus(p));
		}

		int position = 1, nextPosition = 1, index;
		Double lastScore = null;
		boolean sameScore = false;

		Map<String, Object> taskData = (Map<String, Object>) ctx.readTaskData();
		if (taskData == null) {
			taskData = new HashMap<String, Object>();
		}

		int executionNumber = 0;

		if (taskData.containsKey(EXECUTION_NUMBER_PARAM)) {
			executionNumber = (int) taskData.get(EXECUTION_NUMBER_PARAM);
		}
		executionNumber++;

		if (taskData.containsKey(LAST_INSTANCE_DATE_EXECUTED_PARAM)) {
			startInstanceDate = ((Date) taskData.get(LAST_INSTANCE_DATE_EXECUTED_PARAM)).getTime();
		}

		if (taskData.containsKey(LAST_INSTANCE_INDEX_EXECUTED_PARAM)) {
			instanceIndex = (int) taskData.get(LAST_INSTANCE_INDEX_EXECUTED_PARAM);
			instanceIndex++;
		}

		// to not consider timezone (and DST issue)
		DateTime endDate = new LocalDateTime(startInstanceDate)
				.withPeriodAdded(new org.joda.time.Period(periodLength), 1).toDateTime();

		// logger.info(String.format(
		// "run task \"%s\" of group %s on instance index: %s, instance date:
		// %s, pointConcept: %s, periodName: %s",
		// ctx.getTask().getName(), ctx.getGameRefId(), instanceIndex, new
		// Date(startInstanceDate).toString(),
		// pointConceptName, periodName));
		LogHub.info(ctx.getGameRefId(), logger,
				"run task \"{}\" of group {} on instance index: {}, instance date: {}, pointConcept: {}, periodName: {}",
				ctx.getTask().getName(), ctx.getGameRefId(), instanceIndex, new Date(startInstanceDate).toString(),
				pointConceptName, periodName);
		// logger.info(String.format("run task \"%s\" of group %s: periodLength:
		// %s, executionNumber: %s",
		// ctx.getTask().getName(), ctx.getGameRefId(), periodLength,
		// executionNumber));
		LogHub.info(ctx.getGameRefId(), logger, "run task \"{}\" of group {}: periodLength: {}, executionNumber: {}",
				ctx.getTask().getName(), ctx.getGameRefId(), periodLength, executionNumber);
		ClassificationBuilder builder = ClassificationFactory.createIncrementalClassification(states, pointConceptName,
				periodName, instanceIndex);

		List<ClassificationPosition> classification = builder.getClassificationBoard().getBoard();

		// debug logging
		if (logger.isDebugEnabled()) {
			for (ClassificationPosition entry : classification) {
				// logger.debug("{}: player {} score {}",
				// getClassificationName(), entry.getPlayerId(),
				// entry.getScore());
				LogHub.debug(ctx.getGameRefId(), logger, "{}: player {} score {}", getClassificationName(),
						entry.getPlayerId(), entry.getScore());
			}
		}

		for (ClassificationPosition item : classification) {

			sameScore = lastScore != null && lastScore == item.getScore();
			index = nextPosition - 1;

			if (index >= getItemsToNotificate() && !sameScore) {
				break;
			}

			if (!sameScore) {
				position = nextPosition;
			}
			lastScore = item.getScore();
			nextPosition++;

			Classification c = new Classification(getClassificationName(), position, getScoreType(),
					ClassificationType.INCREMENTAL, instanceIndex, startInstanceDate, endDate.getMillis(),
					executionNumber);
			c.setScore(lastScore);

			List<Object> factObjs = new ArrayList<Object>();
			factObjs.add(c);
			ctx.sendAction(getExecutionActions().get(0), item.getPlayerId(), null, factObjs);

		}

		taskData.put(EXECUTION_NUMBER_PARAM, executionNumber);
		taskData.put(LAST_INSTANCE_DATE_EXECUTED_PARAM, endDate.toDate());
		taskData.put(LAST_INSTANCE_INDEX_EXECUTED_PARAM, instanceIndex);
		ctx.writeTaskData(taskData);
	}

	public String getPointConceptName() {
		return pointConceptName;
	}

	public void setPointConceptName(String pointConceptName) {
		this.pointConceptName = pointConceptName;
	}

	public String getPeriodName() {
		return periodName;
	}

	public void setPeriodName(String periodName) {
		this.periodName = periodName;
	}

	/*
	 * not used because class override method execute (non-Javadoc)
	 * 
	 * @see
	 * eu.trentorise.game.task.ClassificationTask#createBuilder(java.util.List)
	 */
	@Override
	protected ClassificationBuilder createBuilder(List<PlayerState> states) {
		return null;
	}

	public long getStartupPeriodInstance() {
		return startupPeriodInstance;
	}

	public void setStartupPeriodInstance(long startupPeriodInstance) {
		this.startupPeriodInstance = startupPeriodInstance;
	}

	public long getPeriodLength() {
		return periodLength;
	}

	public void setPeriodLength(long periodLength) {
		this.periodLength = periodLength;
	}

	public int getStartupInstanceIndex() {
		return startupInstanceIndex;
	}

	public void setStartupInstanceIndex(int startupInstanceIndex) {
		this.startupInstanceIndex = startupInstanceIndex;
	}

}
