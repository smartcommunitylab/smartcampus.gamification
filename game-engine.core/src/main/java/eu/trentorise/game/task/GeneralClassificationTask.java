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

package eu.trentorise.game.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;

public class ClassificationTask extends GameTask {

	private final Logger logger = LoggerFactory
			.getLogger(ClassificationTask.class);

	private Integer itemsToNotificate;
	private String itemType;
	private String classificationName;

	private static final int DEFAULT_VALUE = 3;

	private static final String ACTION_CLASSIFICATION = "classification";

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

		ClassificationList classification = new ClassificationList(states);

		// debug logging
		if (logger.isDebugEnabled()) {
			for (ClassificationItem item : classification.getClassification()) {
				logger.debug("{}: player {} score {}", classificationName,
						item.getPlayerId(), item.getScore());

			}
		}

		int position = 1, nextPosition = 1, index;
		Double lastScore = null;
		boolean sameScore = false;
		for (ClassificationItem item : classification) {

			sameScore = lastScore != null && lastScore == item.getScore();
			index = nextPosition - 1;

			if (index >= itemsToNotificate && !sameScore) {
				break;
			}

			Classification c = new Classification();

			c.setName(classificationName);
			c.setScoreType(itemType);
			if (sameScore) {
				c.setPosition(position);
			} else {
				c.setPosition(nextPosition);
				position = nextPosition;
			}
			lastScore = item.getScore();
			nextPosition++;

			List<Object> factObjs = new ArrayList<Object>();
			factObjs.add(c);
			ctx.sendAction(ACTION_CLASSIFICATION, item.getPlayerId(), null,
					factObjs);
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

	private class ClassificationList implements Iterable<ClassificationItem> {
		private List<ClassificationItem> classification;

		public ClassificationList(List<PlayerState> states) {
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

	@Override
	protected List<String> getExecutionActions() {
		return Arrays.asList(ACTION_CLASSIFICATION);
	}
}
