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

package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.LocalDateTime;
import org.joda.time.Minutes;
import org.joda.time.ReadablePeriod;
import org.joda.time.Seconds;

import eu.trentorise.game.model.Level.Threshold;
import eu.trentorise.game.model.Settings.ChallengeSettings.ChallengeDisclosure;
import eu.trentorise.game.model.core.GameConcept;
import eu.trentorise.game.model.core.GameTask;
import eu.trentorise.game.model.core.TimeInterval;

public class Game {
	private String id;
	private String name;
	private String owner;
	private String domain;
	private Set<String> actions;
	private Set<GameTask> tasks;
	private Set<String> rules;
	private Set<GameConcept> concepts;
	private List<Level> levels = new ArrayList<>();
	private Settings settings = new Settings();

	public void setLevels(List<Level> levels) {
		this.levels = levels;
	}

	/**
	 * game expiration time. If game must live forever set a negative value
	 */
	private long expiration;

	private boolean terminated;

	public Game() {
	}

	public Game(String id) {
		this.id = id;
	}

    /**
     * calculated the disclosure date for challenges more closed to from Date
     * 
     * @param from date
     * @return the disclosure date or null if any challenge settings is defined for the game
     * 
     */
    public Date nextChallengeDisclosureDate(Date from) {
        ChallengeDisclosure disclosure = settings.getChallengeSettings().getDisclosure();
        if (disclosure.getFrequency() != null) {
            final Date disclosureStart = disclosure.getStartDate();
            final TimeInterval frequency = disclosure.getFrequency();

            LocalDateTime cursorDate = new LocalDateTime(disclosureStart);
            final DateTime fromDateTime = new DateTime(from);
            while (cursorDate.toDateTime().isBefore(fromDateTime)) {
                cursorDate = cursorDate.plus(periodFromFrequency(frequency));
            }
            return cursorDate.toDate();
        } else {
            return null;
        }
    }

    private ReadablePeriod periodFromFrequency(TimeInterval interval) {
        ReadablePeriod period = null;
        switch (interval.getUnit()) {
            case DAY:
                period = Days.days(interval.getValue());
                break;
            case HOUR:
                period = Hours.hours(interval.getValue());
                break;
            case MINUTE:
                period = Minutes.minutes(interval.getValue());
                break;
            case SEC:
                period = Seconds.seconds(interval.getValue());
                break;
            case MILLISEC:
                throw new IllegalArgumentException("millis not supported");
            default:
                break;
        }
        return period;
    }
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<GameTask> getTasks() {
		return tasks;
	}

	public void setTasks(Set<GameTask> tasks) {
		this.tasks = tasks;
	}

	public Set<String> getRules() {
		return rules;
	}

	public void setRules(Set<String> rules) {
		this.rules = rules;
	}

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	public boolean isTerminated() {
		terminated = terminated || expiration > 0 && expiration < System.currentTimeMillis();
		return terminated;
	}

	public void setTerminated(boolean terminated) {
		this.terminated = terminated;
	}

	public Set<GameConcept> getConcepts() {
		return concepts;
	}

	public void setConcepts(Set<GameConcept> concepts) {
		this.concepts = concepts;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public List<Level> getLevels() {
		return levels;
	}

	public List<Threshold> getLevelThresholds(String levelName) {
		return levels.stream().filter(level -> level.getName().equals(levelName)).map(level -> level.getThresholds())
				.flatMap(thresholds -> thresholds.stream()).collect(Collectors.toList());
	}

	public Settings getSettings() {
		return settings;
	}

	public void setSettings(Settings settings) {
		this.settings = settings;
	}

}
