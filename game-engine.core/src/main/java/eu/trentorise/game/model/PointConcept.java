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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.kie.api.definition.type.PropertyReactive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.trentorise.game.model.core.GameConcept;

@PropertyReactive
public class PointConcept extends GameConcept {

	private Double score = 0.0;

	private Map<String, Period> periods = new LinkedHashMap<String, Period>();

	long executionMoment = System.currentTimeMillis();

	public PointConcept(String name, long moment) {
		super(name);
		executionMoment = moment;
	}

	public PointConcept(String name) {
		super(name);
	}

	public PointConcept() {

	}

	@JsonCreator
	public PointConcept(Map<String, Object> jsonProps) {
		super(jsonProps);
		score = (Double) jsonProps.get("score");
		Map<String, Object> temp = (Map<String, Object>) jsonProps
				.get("periods");
		Set<Entry<String, Object>> entries = temp.entrySet();
		for (Entry<String, Object> entry : entries) {
			periods.put(entry.getKey(),
					new Period((Map<String, Object>) entry.getValue()));
		}
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		increasePeriodicPoints(score - this.score);
		this.score = score;
	}

	public Double increment(Double score) {
		increasePeriodicPoints(score);
		this.score += score;
		return this.score;
	}

	private void increasePeriodicPoints(Double score) {
		for (Period p : periods.values()) {
			p.increaseScore(score, executionMoment);
		}
	}

	public void addPeriod(String identifier, Date start, long period) {
		Period p = new Period(identifier, start, period);
		if (!periods.containsKey(identifier)) {
			periods.put(identifier, p);
		}
	}

	public void deletePeriod(String identifier) {
		periods.remove(identifier);
	}

	public Double getPeriodCurrentScore(int periodIndex) {
		return new ArrayList<>(periods.values()).get(periodIndex)
				.getCurrentScore();
	}

	public Double getPeriodCurrentScore(String periodIdentifier) {
		return periods.containsKey(periodIdentifier) ? periods.get(
				periodIdentifier).getCurrentScore() : 0d;
	}

	// getPreviousValue
	// getNextValue

	private class Period {
		private Date start;
		private long period;
		private String identifier;
		private LinkedList<PeriodInstance> instances = new LinkedList<>();

		public Period(String identifier, Date start, long period) {
			this.start = start;
			this.period = period;
			this.identifier = identifier;
		}

		public Period(Map<String, Object> jsonProps) {
			start = new Date((long) jsonProps.get("start"));
			period = (Long) jsonProps.get("period");
			identifier = (String) jsonProps.get("identifier");
			List<Map<String, Object>> tempInstances = (List<Map<String, Object>>) jsonProps
					.get("instances");
			for (Map<String, Object> tempInstance : tempInstances) {
				instances.add(new PeriodInstance(tempInstance));
			}

		}

		public String getIdentifier() {
			return identifier;
		}

		private PeriodInstance getCurrentInstance() {
			return retrieveInstance(executionMoment);
		}

		@JsonIgnore
		public Double getCurrentScore() {
			try {
				return getCurrentInstance().getScore();
			} catch (IllegalArgumentException e) {
				return 0d;
			}
		}

		public Double increaseScore(Double value, long moment) {
			try {
				PeriodInstance instance = retrieveInstance(moment);
				return instance.increaseScore(value);
			} catch (IllegalArgumentException e) {
				return 0d;
			}
		}

		private PeriodInstance retrieveInstance(long moment) {
			if (moment < start.getTime()) {
				throw new IllegalArgumentException(
						"moment is previous than startDate of period");
			}
			PeriodInstance instance = null;
			long startInstance = -1;
			long endInstance = -1;
			if (instances.isEmpty() || instances.getLast().getEnd() < moment) {
				startInstance = instances.isEmpty() ? start.getTime()
						: instances.getLast().getEnd() + 1;
				endInstance = instances.isEmpty() ? startInstance + period
						: instances.getLast().getEnd() + period;
				instance = new PeriodInstance(startInstance, endInstance);
				instances.add(instance);

				while (endInstance < moment) {
					startInstance = endInstance + 1;
					endInstance = endInstance + period;
					instance = new PeriodInstance(startInstance, endInstance);
					instances.add(instance);
				}
			} else {

				for (Iterator<PeriodInstance> iter = instances
						.descendingIterator(); iter.hasNext();) {
					PeriodInstance instanceTemp = iter.next();
					if (moment > instanceTemp.getStart()
							&& moment < instanceTemp.getEnd()) {
						instance = instanceTemp;
						break;
					}
				}
			}

			return instance;
		}

		public Date getStart() {
			return start;
		}

		public void setStart(Date start) {
			this.start = start;
		}

		public long getPeriod() {
			return period;
		}

		public void setPeriod(long period) {
			this.period = period;
		}

		public LinkedList<PeriodInstance> getInstances() {
			return instances;
		}

		public void setInstances(LinkedList<PeriodInstance> instances) {
			this.instances = instances;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}
	}

	private class PeriodInstance {
		private Double score = 0d;
		private long start;
		private long end;

		public PeriodInstance(long start, long end) {
			this.start = start;
			this.end = end;
		}

		public PeriodInstance(Map<String, Object> jsonProps) {
			score = (Double) jsonProps.get("score");
			start = (Long) jsonProps.get("start");
			end = (Long) jsonProps.get("end");
		}

		public Double increaseScore(Double value) {
			score = score + value;
			return score;
		}

		public Double getScore() {
			return score;
		}

		public void setScore(Double score) {
			this.score = score;
		}

		public long getStart() {
			return start;
		}

		public void setStart(long start) {
			this.start = start;
		}

		public long getEnd() {
			return end;
		}

		public void setEnd(long end) {
			this.end = end;
		}

		@Override
		public String toString() {
			DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			return String.format("[start: %s, end: %s, score: %s",
					formatter.format(new Date(start)),
					formatter.format(new Date(end)), score);
		}
	}

	public Map<String, Period> getPeriods() {
		return periods;
	}

	public void setPeriods(Map<String, Period> periods) {
		this.periods = periods;
	}
}
