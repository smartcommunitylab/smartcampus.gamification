package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;

public class ClassificationTask extends GameTask {

	private final Logger logger = LoggerFactory
			.getLogger(ClassificationTask.class);

	private Integer itemsToNotificate;
	private String itemType;
	private String classificationName;

	private static final int DEFAULT_VALUE = 3;

	private static final String ACTION_CLASSIFICATION = "classification";
	private static final String K_POSITION = "classification_position";
	private static final String K_CLASSIFICATION_NAME = "classification_name";
	private static final String K_CLASSIFICATION_TYPE = "classification_point_type";

	public ClassificationTask(TaskSchedule schedule, String itemType,
			String classificationName) {
		super(classificationName, schedule);
		this.itemsToNotificate = DEFAULT_VALUE;
		this.itemType = itemType;
		this.classificationName = classificationName;
	}

	public ClassificationTask(TaskSchedule schedule, int itemsToNotificate,
			String itemType, String classificationName) {
		super(classificationName, schedule);
		this.itemsToNotificate = itemsToNotificate;
		this.itemType = itemType;
		this.classificationName = classificationName;
	}

	public ClassificationTask() {

	}

	@Override
	public void execute(GameContext ctx) {
		if (ctx == null) {
			logger.warn("gameContext null");
			return;
		}

		if (StringUtils.isBlank(classificationName)
				|| StringUtils.isBlank(itemType)) {
			throw new IllegalArgumentException(
					"classificationName and itemType cannot be null or empty");
		}
		// read all game players
		List<String> players = ctx.readPlayers();
		// load players status

		List<PlayerState> states = new ArrayList<PlayerState>();
		for (String p : players) {
			states.add(ctx.readStatus(p));
		}

		Classification classification = new Classification(states);

		// debug logging
		if (logger.isDebugEnabled()) {
			for (ClassificationItem item : classification.getClassification()) {
				logger.debug("{}: player {} score {}", classificationName,
						item.getPlayerId(), item.getScore());

			}
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(K_CLASSIFICATION_NAME, classificationName);
		params.put(K_CLASSIFICATION_TYPE, itemType);

		int position = 1, nextPosition = 1, index;
		Double lastScore = null;
		boolean sameScore = false;
		for (ClassificationItem item : classification) {

			sameScore = lastScore != null && lastScore == item.getScore();
			index = nextPosition - 1;

			if (index > itemsToNotificate && !sameScore) {
				break;
			}
			if (sameScore) {
				params.put(K_POSITION, position);
			} else {
				params.put(K_POSITION, nextPosition);
				position = nextPosition;
			}
			lastScore = item.getScore();
			nextPosition++;

			ctx.sendAction(ACTION_CLASSIFICATION, item.getPlayerId(), params);
		}

	}

	private PointConcept retrieveConcept(PlayerState p, String pointType) {
		for (GameConcept gc : p.getState()) {
			if (gc instanceof PointConcept
					&& ((PointConcept) gc).getName().equals(pointType)) {
				return (PointConcept) gc;
			}
		}

		return null;
	}

	private class Classification implements Iterable<ClassificationItem> {
		private List<ClassificationItem> classification;

		public Classification(List<PlayerState> states) {
			init(states);
		}

		private void init(List<PlayerState> states) {
			classification = new ArrayList<ClassificationItem>();
			for (PlayerState state : states) {
				classification.add(new ClassificationItem(retrieveConcept(
						state, itemType).getScore(), state.getPlayerId()));
			}

			Collections.sort(classification,
					Collections.reverseOrder(new ClassificationSorter()));

		}

		private class ClassificationSorter implements
				Comparator<ClassificationItem> {

			public ClassificationSorter() {
			}

			public int compare(ClassificationItem o1, ClassificationItem o2) {
				return Double.compare(o1.getScore(), o2.getScore());
			}

		}

		public List<ClassificationItem> getClassification() {
			return classification;
		}

		public void setClassification(List<ClassificationItem> classification) {
			this.classification = classification;
		}

		public Iterator<ClassificationItem> iterator() {
			return classification.iterator();
		}
	}

	private class ClassificationItem {
		private double score;
		private String playerId;

		public ClassificationItem(double score, String playerId) {
			this.score = score;
			this.playerId = playerId;
		}

		public double getScore() {
			return score;
		}

		public void setScore(double score) {
			this.score = score;
		}

		public String getPlayerId() {
			return playerId;
		}

		public void setPlayerId(String playerId) {
			this.playerId = playerId;
		}

	}

	public Integer getItemsToNotificate() {
		return itemsToNotificate;
	}

	public void setItemsToNotificate(Integer itemsToNotificate) {
		this.itemsToNotificate = itemsToNotificate;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public String getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}
}
