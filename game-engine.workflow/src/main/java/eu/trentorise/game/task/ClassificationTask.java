package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.GameTask;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;

public class ClassificationTask extends GameTask {

	private int itemsToNotificate;
	private String itemType;

	private static final int DEFAULT_VALUE = 3;

	public ClassificationTask(TaskSchedule schedule, String itemType) {
		super(schedule);
		this.itemsToNotificate = DEFAULT_VALUE;
		this.itemType = itemType;
	}

	public ClassificationTask(TaskSchedule schedule, int itemsToNotificate,
			String itemType) {
		super(schedule);
		this.itemsToNotificate = itemsToNotificate;
		this.itemType = itemType;
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
		Collections.sort(states, new ClassificationSorter(itemType));

		// event on classification
		if (itemsToNotificate < states.size()) {
			states = states.subList(0, itemsToNotificate);
		}
		for (PlayerState p : states) {
			ctx.sendAction();
		}
	}

	private class ClassificationSorter implements Comparator<PlayerState> {

		private String pointType;

		public ClassificationSorter(String pointType) {
			this.pointType = pointType;
		}

		public int compare(PlayerState o1, PlayerState o2) {
			PointConcept pc1 = retrieveConcept(o1);
			PointConcept pc2 = retrieveConcept(o2);
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

		private PointConcept retrieveConcept(PlayerState p) {
			for (GameConcept gc : p.getState()) {
				if (gc instanceof PointConcept
						&& ((PointConcept) gc).getName().equals(pointType)) {
					return (PointConcept) gc;
				}
			}

			return null;
		}
	}
}
