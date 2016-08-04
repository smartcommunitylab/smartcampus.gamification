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

package eu.trentorise.game.bean;

import java.util.HashSet;
import java.util.Set;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.task.GeneralClassificationTask;

public class GameDTO {
	private String id;
	private String name;
	private String owner;
	private Set<String> actions = new HashSet<>();
	private Set<RuleDTO> rules = new HashSet<>();
	private long expiration;
	private boolean terminated;

	private Set<GeneralClassificationTask> classificationTask = new HashSet<>();
	private Set<PointConcept> pointConcept = new HashSet<>();
	private Set<BadgeCollectionConcept> badgeCollectionConcept = new HashSet<>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}

	public Set<RuleDTO> getRules() {
		return rules;
	}

	public void setRules(Set<RuleDTO> rules) {
		this.rules = rules;
	}

	public Set<GeneralClassificationTask> getClassificationTask() {
		return classificationTask;
	}

	public void setClassificationTask(Set<GeneralClassificationTask> classificationTask) {
		this.classificationTask = classificationTask;
	}

	public Set<PointConcept> getPointConcept() {
		return pointConcept;
	}

	public void setPointConcept(Set<PointConcept> pointConcept) {
		this.pointConcept = pointConcept;
	}

	public Set<BadgeCollectionConcept> getBadgeCollectionConcept() {
		return badgeCollectionConcept;
	}

	public void setBadgeCollectionConcept(
			Set<BadgeCollectionConcept> badgeCollectionConcept) {
		this.badgeCollectionConcept = badgeCollectionConcept;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public boolean isTerminated() {
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public GameDTO() {

	}

}
