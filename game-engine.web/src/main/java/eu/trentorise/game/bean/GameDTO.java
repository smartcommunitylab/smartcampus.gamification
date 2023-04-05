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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.model.Settings;

public class GameDTO {
	private String id;
	private String name;
	private String owner;
	private String domain;

	private Set<String> actions = new HashSet<>();
	private Set<RuleDTO> rules = new HashSet<>();
	private List<LevelDTO> levels = new ArrayList<>();

	private long expiration;
	private boolean terminated;

	private Set<ClassificationDTO> classificationTask = new HashSet<>();
	private Set<PointConcept> pointConcept = new HashSet<>();
	private Set<BadgeCollectionConcept> badgeCollectionConcept = new HashSet<>();

	private ChallengeChoiceConfig challengeChoiceConfig;

	private Settings settings = new Settings();

	private String notifyPCName;

	public static class ChallengeChoiceConfig {
		private Date deadline;
		private String cronExpression;

		public Date getDeadline() {
			return deadline;
		}

		public void setDeadline(Date deadline) {
			this.deadline = deadline;
		}

		public String getCronExpression() {
			return cronExpression;
		}

		public void setCronExpression(String cronExpression) {
			this.cronExpression = cronExpression;
		}

	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

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

	public Set<ClassificationDTO> getClassificationTask() {
		return classificationTask;
	}

	public void setClassificationTask(Set<ClassificationDTO> classificationTask) {
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

	public void setBadgeCollectionConcept(Set<BadgeCollectionConcept> badgeCollectionConcept) {
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

	public List<LevelDTO> getLevels() {
		return levels;
	}

	public void setLevels(List<LevelDTO> levels) {
		this.levels = levels;
	}

	public ChallengeChoiceConfig getChallengeChoiceConfig() {
		return challengeChoiceConfig;
	}

	public void setChallengeChoiceConfig(ChallengeChoiceConfig challengeChoiceConfig) {
		this.challengeChoiceConfig = challengeChoiceConfig;
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	public String getNotifyPCName() {
		return notifyPCName;
	}

	public void setNotifyPCName(String notifyPCName) {
		this.notifyPCName = notifyPCName;
	}

}
