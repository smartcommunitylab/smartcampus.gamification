package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.core.GameContext;
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

	private final Logger logger = LoggerFactory
			.getLogger(IncrementalClassificationTask.class);

	private String pointConceptName;
	private String periodName;
	private long periodLength;
	private long startupPeriodInstance;
	private int startupInstanceIndex;

	private static final String EXECUTION_TIME_PARAM = "executionTime";
	private static final String START_INSTANCE_PARAM = "startInstance";
	private static final String START_INSTANCE_INDEX_PARAM = "startInstanceIndex";

	public IncrementalClassificationTask(PointConcept pc, String periodName,
			String classificationName) {
		super();
		updatePointConceptData(pc, periodName, null);
		super.setClassificationName(classificationName);
		super.setName(classificationName);
	}

	public IncrementalClassificationTask(PointConcept pc, String periodName,
			String classificationName, TimeInterval delay) {
		super();
		updatePointConceptData(pc, periodName, delay);
		super.setClassificationName(classificationName);
		super.setName(classificationName);
	}

	public IncrementalClassificationTask() {
		super();
		// logger.info("New Task incremental created");
	}

	public final void updatePointConceptData(PointConcept pc,
			String periodName, TimeInterval delay) {
		if (pc != null) {
			Period period = pc.getPeriod(periodName);
			PeriodInstance instance = pc.getPeriodCurrentInstance(periodName);
			pointConceptName = pc.getName();
			this.periodName = periodName;
			startupPeriodInstance = instance.getStart();
			periodLength = period.getPeriod();
			startupInstanceIndex = instance.getIndex();
			TaskSchedule schedule = new TaskSchedule();
			schedule.setStart(new Date(instance.getEnd()));
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
	protected Classification createClassificationObject(GameContext ctx,
			double score, String scoreType, int position) {
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
			return;
		}

		logger.debug("Execute of task {}", getClassificationName());

		IncrementalClassificationTask execTask = (IncrementalClassificationTask) ctx
				.getTask();
		long periodLength = execTask.getPeriodLength();
		long startupPeriodInstance = execTask.getStartupPeriodInstance();
		int startupInstanceIndex = execTask.getStartupInstanceIndex();
		String pointConceptName = execTask.getPointConceptName();
		String periodName = execTask.getPeriodName();

		// read all game players
		List<String> players = ctx.readPlayers();
		// load players status

		List<PlayerState> states = new ArrayList<PlayerState>();
		for (String p : players) {
			states.add(ctx.readStatus(p));
		}

		// ClassificationBuilder builder = createBuilder(states);

		int position = 1, nextPosition = 1, index;
		Double lastScore = null;
		boolean sameScore = false;

		Map<String, Object> taskData = (Map<String, Object>) ctx.readTaskData();
		if (taskData == null) {
			taskData = new HashMap<String, Object>();
		}

		int executionTime = 0;

		if (taskData.containsKey(EXECUTION_TIME_PARAM)) {
			executionTime = (int) taskData.get(EXECUTION_TIME_PARAM);
		}
		executionTime++;

		if (taskData.containsKey(START_INSTANCE_PARAM)) {
			startupPeriodInstance = (long) taskData.get(START_INSTANCE_PARAM);
		}

		if (taskData.containsKey(START_INSTANCE_INDEX_PARAM)) {
			startupInstanceIndex = (int) taskData
					.get(START_INSTANCE_INDEX_PARAM);
			startupInstanceIndex++;
		}

		long start = startupPeriodInstance + periodLength * (executionTime - 1);
		long end = start + periodLength;

		logger.info(String
				.format("run task %s of group %s: startupInstanceIndex (saved in game def or task data) %s",
						ctx.getTask().getName(), ctx.getGameRefId(),
						startupInstanceIndex));
		logger.info(String
				.format("run task %s of group %s: startupPeriodInstance (saved in game def or task data) %s",
						ctx.getTask().getName(), ctx.getGameRefId(), new Date(
								startupPeriodInstance).toString()));
		logger.info(String.format(
				"run task %s of group %s: period start task run reference %s",
				ctx.getTask().getName(), ctx.getGameRefId(),
				new Date(start).toString()));
		logger.info(String.format("run task %s of group %s: periodLength %s",
				ctx.getTask().getName(), ctx.getGameRefId(), periodLength));
		logger.info(String.format("run task %s of group %s: periodName %s", ctx
				.getTask().getName(), ctx.getGameRefId(), periodName));
		logger.info(String.format(
				"run task %s of group %s: pointConceptName %s", ctx.getTask()
						.getName(), ctx.getGameRefId(), pointConceptName));

		ClassificationBuilder builder = ClassificationFactory
				.createIncrementalClassification(states, pointConceptName,
						periodName, new Date(start));

		List<ClassificationPosition> classification = builder
				.getClassificationBoard().getBoard();

		// debug logging
		if (logger.isDebugEnabled()) {
			for (ClassificationPosition entry : classification) {
				logger.debug("{}: player {} score {}", getClassificationName(),
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

			Classification c = new Classification(getClassificationName(),
					position, getScoreType(), ClassificationType.INCREMENTAL,
					startupInstanceIndex, start, end, executionTime);
			c.setScore(lastScore);

			List<Object> factObjs = new ArrayList<Object>();
			factObjs.add(c);
			ctx.sendAction(getExecutionActions().get(0), item.getPlayerId(),
					null, factObjs);

		}

		taskData.put(EXECUTION_TIME_PARAM, executionTime);
		taskData.put(START_INSTANCE_PARAM, startupPeriodInstance);
		taskData.put(START_INSTANCE_INDEX_PARAM, startupInstanceIndex);
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
