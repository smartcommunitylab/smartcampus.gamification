package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.managers.ClassificationFactory.ClassificationBuilder;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.ClassificationPosition;
import eu.trentorise.game.model.core.GameTask;

public abstract class ClassificationTask extends GameTask {

	private final Logger logger = LoggerFactory
			.getLogger(ClassificationTask.class);

	private static final int DEFAULT_VALUE = 3;

	private static final String ACTION_CLASSIFICATION = "classification";

	private Integer itemsToNotificate = DEFAULT_VALUE;
	private String classificationName;

	public ClassificationTask(String classificationName, TaskSchedule schedule) {
		super(classificationName, schedule);
		this.classificationName = classificationName;

	}

	public ClassificationTask(Integer itemsToNotificate,
			String classificationName, TaskSchedule schedule) {
		super(classificationName, schedule);
		this.classificationName = classificationName;
		this.itemsToNotificate = itemsToNotificate;
	}

	@JsonCreator
	public ClassificationTask() {
		super();
	}

	@Override
	public void execute(GameContext ctx) {
		if (ctx == null) {
			logger.warn("gameContext null");
			return;
		}

		logger.debug("Execute of task {}", classificationName);

		// read all game players
		List<String> players = ctx.readPlayers();
		// load players status

		List<PlayerState> states = new ArrayList<PlayerState>();
		for (String p : players) {
			states.add(ctx.readStatus(p));
		}

		ClassificationBuilder builder = createBuilder(states);

		List<ClassificationPosition> classification = builder
				.getClassificationBoard().getBoard();
		// debug logging
		if (logger.isDebugEnabled()) {
			for (ClassificationPosition position : classification) {
				logger.debug("{}: player {} score {}", classificationName,
						position.getPlayerId(), position.getScore());

			}
		}

		int position = 1, nextPosition = 1, index;
		Double lastScore = null;
		boolean sameScore = false;
		for (ClassificationPosition item : classification) {

			sameScore = lastScore != null && lastScore == item.getScore();
			index = nextPosition - 1;

			if (index >= itemsToNotificate && !sameScore) {
				break;
			}

			// Classification c = new Classification();

			// c.setName(classificationName);
			// c.setScoreType(getScoreType());
			// if (sameScore) {
			// c.setPosition(position);
			// } else {
			// c.setPosition(nextPosition);
			// position = nextPosition;
			// }

			if (!sameScore) {
				position = nextPosition;
			}
			lastScore = item.getScore();
			nextPosition++;

			Classification c = createClassificationObject(ctx, item.getScore(),
					getScoreType(), position);

			List<Object> factObjs = new ArrayList<Object>();
			factObjs.add(c);
			ctx.sendAction(ACTION_CLASSIFICATION, item.getPlayerId(), null,
					factObjs);
		}

		// ClassificationList classification = new ClassificationList(states);
		//
		// // debug logging
		// if (logger.isDebugEnabled()) {
		// for (ClassificationItem item : classification.getClassification()) {
		// logger.debug("{}: player {} score {}", classificationName,
		// item.getPlayerId(), item.getScore());
		//
		// }
		// }
		//
		// int position = 1, nextPosition = 1, index;
		// Double lastScore = null;
		// boolean sameScore = false;
		// for (ClassificationItem item : classification) {
		//
		// sameScore = lastScore != null && lastScore == item.getScore();
		// index = nextPosition - 1;
		//
		// if (index >= itemsToNotificate && !sameScore) {
		// break;
		// }
		//
		// Classification c = new Classification();
		//
		// c.setName(classificationName);
		// c.setScoreType(getScoreType());
		// if (sameScore) {
		// c.setPosition(position);
		// } else {
		// c.setPosition(nextPosition);
		// position = nextPosition;
		// }
		// lastScore = item.getScore();
		// nextPosition++;
		//
		// List<Object> factObjs = new ArrayList<Object>();
		// factObjs.add(c);
		// ctx.sendAction(ACTION_CLASSIFICATION, item.getPlayerId(), null,
		// factObjs);
		// }
	}

	@Override
	@JsonIgnore
	public List<String> getExecutionActions() {
		return Arrays.asList(ACTION_CLASSIFICATION);
	}

	// private class ClassificationItem {
	// private double score;
	// private String playerId;
	//
	// public ClassificationItem(double score, String playerId) {
	// this.score = score;
	// this.playerId = playerId;
	// }
	//
	// public double getScore() {
	// return score;
	// }
	//
	// public void setScore(double score) {
	// this.score = score;
	// }
	//
	// public String getPlayerId() {
	// return playerId;
	// }
	//
	// public void setPlayerId(String playerId) {
	// this.playerId = playerId;
	// }
	//
	// }

	protected abstract ClassificationBuilder createBuilder(
			List<PlayerState> states);

	// protected abstract double retrieveScore(PlayerState state);

	protected abstract String getScoreType();

	protected abstract Classification createClassificationObject(
			GameContext ctx, double score, String scoreType, int position);

	/*
	 * protected PointConcept retrieveConcept(PlayerState p, String pointType) {
	 * for (GameConcept gc : p.getState()) { if (gc instanceof PointConcept &&
	 * ((PointConcept) gc).getName().equals(pointType)) { return (PointConcept)
	 * gc; } }
	 * 
	 * return null; }
	 */

	// private class ClassificationList implements Iterable<ClassificationItem>
	// {
	// private List<ClassificationItem> classification;
	//
	// public ClassificationList(List<PlayerState> states) {
	// init(states);
	// }
	//
	// private void init(List<PlayerState> states) {
	// classification = new ArrayList<ClassificationItem>();
	// for (PlayerState state : states) {
	// classification.add(new ClassificationItem(retrieveScore(state),
	// state.getPlayerId()));
	// }
	//
	// Collections.sort(classification,
	// Collections.reverseOrder(new ClassificationSorter()));
	//
	// }
	//
	// private class ClassificationSorter implements
	// Comparator<ClassificationItem> {
	//
	// public ClassificationSorter() {
	// }
	//
	// public int compare(ClassificationItem o1, ClassificationItem o2) {
	// return Double.compare(o1.getScore(), o2.getScore());
	// }
	//
	// }
	//
	// public List<ClassificationItem> getClassification() {
	// return classification;
	// }
	//
	// public void setClassification(List<ClassificationItem> classification) {
	// this.classification = classification;
	// }
	//
	// public Iterator<ClassificationItem> iterator() {
	// return classification.iterator();
	// }
	// }

	public Integer getItemsToNotificate() {
		return itemsToNotificate;
	}

	public void setItemsToNotificate(Integer itemsToNotificate) {
		this.itemsToNotificate = itemsToNotificate;
	}

	public String getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}

}
