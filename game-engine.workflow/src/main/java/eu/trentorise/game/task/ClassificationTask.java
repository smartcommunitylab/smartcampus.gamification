package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private int itemsToNotificate;
	private String itemType;
	private String classificationName;

	private static final int DEFAULT_VALUE = 3;

	private static final String ACTION_CLASSIFICATION = "classification";
	private static final String K_POSITION = "classification_position";
	private static final String K_CLASSIFICATION_NAME = "classification_name";

	public ClassificationTask(TaskSchedule schedule, String itemType,
			String classificationName) {
		super(schedule);
		this.itemsToNotificate = DEFAULT_VALUE;
		this.itemType = itemType;
		this.classificationName = classificationName;
	}

	public ClassificationTask(TaskSchedule schedule, int itemsToNotificate,
			String itemType, String classificationName) {
		super(schedule);
		this.itemsToNotificate = itemsToNotificate;
		this.itemType = itemType;
		this.classificationName = classificationName;
	}

	@Override
	public void execute(GameContext ctx) {
		if (ctx == null) {
			return;
		}
		// read all game players
		List<String> players = ctx.readPlayers();
		// load players status

		List<PlayerState> states = new ArrayList<PlayerState>();
		for (String p : players) {
			states.add(ctx.readStatus(p));
		}
		// sort for green leaves
		Collections.sort(states,
				Collections.reverseOrder(new ClassificationSorter(itemType)));

		// debug logging
		if (logger.isDebugEnabled()) {
			for (PlayerState state : states) {
				PointConcept score = retrieveConcept(state, itemType);
				if (score != null) {
					logger.debug("{}: player {} score {}", classificationName,
							state.getPlayerId(), score.getScore());
				}
			}
		}

		// event on classification
		if (itemsToNotificate < states.size()) {
			states = states.subList(0, itemsToNotificate);
		}

		Map<String, Object> params = new HashMap<String, Object>();
		params.put(K_CLASSIFICATION_NAME, classificationName);
		for (int i = 0; i < itemsToNotificate; i++) {
			params.put(K_POSITION, i + 1);
			try {
				ctx.sendAction(ACTION_CLASSIFICATION, states.get(i)
						.getPlayerId(), params);
			} catch (IndexOutOfBoundsException e) {
				break;
			}
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

	private class ClassificationSorter implements Comparator<PlayerState> {

		private String pointType;

		public ClassificationSorter(String pointType) {
			this.pointType = pointType;
		}

		public int compare(PlayerState o1, PlayerState o2) {
			PointConcept pc1 = retrieveConcept(o1, pointType);
			PointConcept pc2 = retrieveConcept(o2, pointType);
			if (pc1 == null && pc2 != null) {
				return -1;
			}

			if (pc1 != null && pc2 == null) {
				return 1;
			}

			if (pc1 == null && pc2 == null) {
				return 0;
			}

			return pc1.getScore().compareTo(pc2.getScore());

		}

	}
}
