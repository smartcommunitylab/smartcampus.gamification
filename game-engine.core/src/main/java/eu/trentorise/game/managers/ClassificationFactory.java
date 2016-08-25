package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.managers.ClassificationFactory.ClassificationBuilder;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.ClassificationPosition;
import eu.trentorise.game.model.core.GameConcept;

public class ClassificationFactory {

	public interface ClassificationBuilder {
		public List<ClassificationPosition> getClassification();
	}

	public static ClassificationBuilder createGeneralClassification(
			List<PlayerState> states, String pointConceptName) {
		return new GeneralClassificationBuilder(states, pointConceptName);
	}

	public static ClassificationBuilder createIncrementalClassification(
			List<PlayerState> states, String pointConceptName, String periodName) {
		return new IncrementalClassificationBuilder(states, pointConceptName,
				periodName);
	}

	public static ClassificationBuilder createIncrementalClassification(
			List<PlayerState> states, String pointConceptName,
			String periodName, Date date) {
		return new IncrementalClassificationBuilder(states, pointConceptName,
				periodName, date.getTime());
	}

}

class GeneralClassificationBuilder extends AbstractClassificationBuilder {

	private static final Logger logger = LoggerFactory
			.getLogger(GeneralClassificationBuilder.class);

	private String pointConceptName;

	public GeneralClassificationBuilder(List<PlayerState> states,
			String pointConceptName) {
		super(states);

		this.pointConceptName = pointConceptName;
	}

	@Override
	protected double retrieveScore(PlayerState state, long moment) {
		for (GameConcept gc : state.getState()) {
			if (gc instanceof PointConcept
					&& ((PointConcept) gc).getName().equals(pointConceptName)) {
				return ((PointConcept) gc).getScore();
			}
		}
		logger.warn(String
				.format("PointConcept %s not found", pointConceptName));
		return 0d;
	}

}

class IncrementalClassificationBuilder extends AbstractClassificationBuilder {

	private static final Logger logger = LoggerFactory
			.getLogger(IncrementalClassificationBuilder.class);
	private String pointConceptName;
	private String periodName;

	public IncrementalClassificationBuilder(List<PlayerState> states,
			String pointConceptName, String periodName) {
		super(states);
		this.pointConceptName = pointConceptName;
		this.periodName = periodName;
	}

	public IncrementalClassificationBuilder(List<PlayerState> states,
			String pointConceptName, String periodName, long moment) {
		super(states, moment);
		this.pointConceptName = pointConceptName;
		this.periodName = periodName;
	}

	@Override
	protected double retrieveScore(PlayerState state, long moment) {
		for (GameConcept gc : state.getState()) {
			if (gc.getName().equals(pointConceptName)
					&& gc instanceof PointConcept) {
				PointConcept pc = (PointConcept) gc;
				if (moment > 0) {
					return pc.getPeriodScore(periodName, moment);
				} else {
					return pc.getPeriodCurrentScore(periodName);
				}
			}
		}
		logger.warn(String
				.format("PointConcept %s not found", pointConceptName));
		return 0d;
	}

}

abstract class AbstractClassificationBuilder implements ClassificationBuilder {

	private List<PlayerState> states;
	private long moment = -1;

	public AbstractClassificationBuilder(List<PlayerState> states) {
		this.states = states;
	}

	public AbstractClassificationBuilder(List<PlayerState> states, long moment) {
		this.states = states;
		if (moment > 0) {
			this.moment = moment;
		}
	}

	protected abstract double retrieveScore(PlayerState state, long moment);

	@Override
	public List<ClassificationPosition> getClassification() {
		return IteratorUtils.toList(new ClassificationBoard(states, moment)
				.iterator());
	}

	private class ClassificationBoard implements
			Iterable<ClassificationPosition> {
		private List<ClassificationPosition> classification;

		public ClassificationBoard(List<PlayerState> states, long moment) {
			init(states, moment);
		}

		private void init(List<PlayerState> states, long moment) {
			classification = new ArrayList<ClassificationPosition>();
			for (PlayerState state : states) {
				classification.add(new ClassificationPosition(retrieveScore(
						state, moment), state.getPlayerId()));
			}

			Collections.sort(classification,
					Collections.reverseOrder(new ClassificationSorter()));

		}

		private class ClassificationSorter implements
				Comparator<ClassificationPosition> {

			public ClassificationSorter() {
			}

			public int compare(ClassificationPosition o1,
					ClassificationPosition o2) {
				return Double.compare(o1.getScore(), o2.getScore());
			}

		}

		@Override
		public Iterator<ClassificationPosition> iterator() {
			return classification.iterator();
		}
	}
}