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

import java.util.List;

import org.apache.commons.lang.StringUtils;

import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.managers.ClassificationFactory;
import eu.trentorise.game.managers.ClassificationFactory.ClassificationBuilder;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.core.ClassificationType;

public class GeneralClassificationTask extends ClassificationTask {

	private String itemType;

	public GeneralClassificationTask(TaskSchedule schedule, String itemType, String classificationName) {
		super(classificationName, schedule);
		if (StringUtils.isBlank(itemType)) {
			throw new IllegalArgumentException("itemType cannot be null or empty");
		}
		this.itemType = itemType;
	}

	public GeneralClassificationTask(TaskSchedule schedule, int itemsToNotificate, String itemType,
			String classificationName) {
		super(itemsToNotificate, classificationName, schedule);
		if (StringUtils.isBlank(itemType)) {
			throw new IllegalArgumentException("itemType cannot be null or empty");
		}
		this.itemType = itemType;
	}

	public GeneralClassificationTask() {
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	// @Override
	// protected double retrieveScore(PlayerState state) {
	// for (GameConcept gc : state.getState()) {
	// if (gc instanceof PointConcept
	// && ((PointConcept) gc).getName().equals(itemType)) {
	// return ((PointConcept) gc).getScore();
	// }
	// }
	//
	// return 0d;
	// }

	@Override
	protected String getScoreType() {
		return itemType;
	}

	@Override
	protected ClassificationBuilder createBuilder(List<PlayerState> states) {
		return ClassificationFactory.createGeneralClassification(states, getScoreType());
	}

	@Override
	protected Classification createClassificationObject(GameContext ctx, double score, String scoreType, int position) {
		Classification c = new Classification(getClassificationName(), position, scoreType, ClassificationType.GENERAL);
		c.setScore(score);
		return c;
	}
}
