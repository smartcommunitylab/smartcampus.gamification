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

import org.joda.time.Interval;
import org.kie.api.definition.type.PropertyReactive;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import eu.trentorise.game.model.core.GameConcept;

@PropertyReactive
public class PointConcept extends GameConcept {

	private Double score = 0.0;

	private Map<String, PeriodInternal> periods = new LinkedHashMap<String, PeriodInternal>();

	@JsonIgnore
	long executionMoment = System.currentTimeMillis();

	public PointConcept(String name, long moment) {
		super(name);
		executionMoment = moment;
	}

	public PointConcept(String name) {
		super(name);
	}

	@JsonCreator
	public PointConcept(Map<String, Object> jsonProps) {
		super(jsonProps);
		Object scoreField = jsonProps.get("score");
		// fix: in some case PointConcept JSON representation contains 0 value
		// in score field
		// and so it is cast to Integer
		if (scoreField != null) {
			if (scoreField instanceof Double) {
				score = (Double) scoreField;
			}
			if (scoreField instanceof Integer) {
				score = ((Integer) scoreField).doubleValue();
			}
		}
		Map<String, Object> temp = (Map<String, Object>) jsonProps
				.get("periods");
		if (temp != null) {
			Set<Entry<String, Object>> entries = temp.entrySet();
			for (Entry<String, Object> entry : entries) {
				periods.put(entry.getKey(), new PeriodInternal(
						(Map<String, Object>) entry.getValue()));
			}
		}
	}

	public Period getPeriod(String periodName) {
		return periods.get(periodName);
	}

	/*
	 * Actually I must have this methods to permit Jackson to correctly
	 * serialize the inner class
	 */
	public Map<String, PeriodInternal> getPeriods() {
		return periods;
	}

	/*
	 * Actually I must have this methods to permit Jackson to correctly
	 * serialize the inner class
	 */
	public void setPeriods(Map<String, PeriodInternal> periods) {
		this.periods = periods;
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
		for (PeriodInternal p : periods.values()) {
			p.increaseScore(score, executionMoment);
		}
	}

	public void addPeriod(String identifier, Date start, long period) {
		PeriodInternal p = new PeriodInternal(identifier, start, period);
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

	public PeriodInstance getPeriodCurrentInstance(int periodIndex) {
		return new ArrayList<>(periods.values()).get(periodIndex)
				.getCurrentInstance();
	}

	public PeriodInstance getPeriodCurrentInstance(String periodIdentifier) {
		return periods.containsKey(periodIdentifier) ? periods.get(
				periodIdentifier).getCurrentInstance() : null;
	}

	public Double getPeriodPreviousScore(String periodIdentifier) {
		return getPeriodScore(periodIdentifier, 1);
	}

	public PeriodInstance getPeriodPreviousInstance(String periodIdentifier) {
		return getPeriodInstance(periodIdentifier, 1);
	}

	public Double getPeriodScore(String periodIdentifier, long moment) {
		return periods.containsKey(periodIdentifier) ? periods.get(
				periodIdentifier).getScore(moment) : 0d;
	}

	public PeriodInstance getPeriodInstance(String periodIdentifier, long moment) {
		return periods.containsKey(periodIdentifier) ? periods.get(
				periodIdentifier).retrieveInstance(moment) : null;
	}

	public Double getPeriodScore(String periodIdentifier, int instanceIndex) {
		Double result = 0d;
		PeriodInternal p = periods.get(periodIdentifier);
		if (p != null) {
			LinkedList<PeriodInstanceImpl> instances = p.getInstances();
			try {
				result = instances.get(instances.size() - 1 - instanceIndex)
						.getScore();
			} catch (IndexOutOfBoundsException e) {
			}
		}

		return result;
	}

	public PeriodInstance getPeriodInstance(String periodIdentifier,
			int instanceIndex) {
		PeriodInstance result = null;
		PeriodInternal p = periods.get(periodIdentifier);
		if (p != null) {
			LinkedList<PeriodInstanceImpl> instances = p.getInstances();
			try {
				PeriodInstance current = getPeriodCurrentInstance(periodIdentifier);
				result = instances.get((current != null ? current.getIndex()
						: 0) - instanceIndex);
			} catch (IndexOutOfBoundsException e) {
			}
		}

		return result;
	}

	public interface Period {
		public Date getStart();

		public long getPeriod();

		public String getIdentifier();
	}

	public interface PeriodInstance {
		public Double getScore();

		public long getStart();

		public long getEnd();

		public int getIndex();
	}

	private class PeriodInternal implements Period {
		private Date start;
		private long period;
		private String identifier;
		private LinkedList<PeriodInstanceImpl> instances = new LinkedList<>();

		public PeriodInternal(String identifier, Date start, long period) {
			this.start = start;
			this.period = period;
			this.identifier = identifier;
		}

		public PeriodInternal(Map<String, Object> jsonProps) {
			start = new Date((long) jsonProps.get("start"));
			Object periodField = jsonProps.get("period");
			if (periodField != null) {
				if (periodField instanceof Long) {
					period = (Long) periodField;
				}
				if (periodField instanceof Integer) {
					period = Integer.valueOf((Integer) periodField).longValue();
				}
			}
			identifier = (String) jsonProps.get("identifier");
			List<Map<String, Object>> tempInstances = (List<Map<String, Object>>) jsonProps
					.get("instances");
			if (tempInstances != null) {
				for (Map<String, Object> tempInstance : tempInstances) {
					instances.add(new PeriodInstanceImpl(tempInstance));
				}
			}

		}

		private PeriodInstanceImpl getCurrentInstance() {
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

		public Double getScore(long moment) {
			try {
				return retrieveInstance(moment).getScore();
			} catch (NullPointerException e) {
				return 0d;
			}
		}

		public Double increaseScore(Double value, long moment) {
			try {
				PeriodInstanceImpl instance = retrieveInstance(moment);
				return instance.increaseScore(value);
			} catch (IllegalArgumentException e) {
				return 0d;
			}
		}

		private PeriodInstanceImpl retrieveInstance(long moment) {
			if (moment < start.getTime()) {
				throw new IllegalArgumentException(
						"moment is previous than startDate of period");
			}
			PeriodInstanceImpl instance = null;
			long startInstance = -1;
			long endInstance = -1;
			if (instances.isEmpty() || instances.getLast().getEnd() < moment) {
				startInstance = instances.isEmpty() ? start.getTime()
						: instances.getLast().getEnd();
				endInstance = instances.isEmpty() ? startInstance + period
						: instances.getLast().getEnd() + period;
				instance = new PeriodInstanceImpl(startInstance, endInstance);
				instances.add(instance);
				instance.setIndex(instances.size() - 1);
				while (endInstance < moment) {
					startInstance = endInstance;
					endInstance = endInstance + period;
					instance = new PeriodInstanceImpl(startInstance,
							endInstance);
					instances.add(instance);
					instance.setIndex(instances.size() - 1);
				}
			} else {
				Interval periodInterval = null;
				for (Iterator<PeriodInstanceImpl> iter = instances
						.descendingIterator(); iter.hasNext();) {
					PeriodInstanceImpl instanceTemp = iter.next();
					periodInterval = new Interval(instanceTemp.getStart(),
							instanceTemp.getEnd());
					if (periodInterval.contains(moment)) {
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

		public LinkedList<PeriodInstanceImpl> getInstances() {
			return instances;
		}

		public void setInstances(LinkedList<PeriodInstanceImpl> instances) {
			this.instances = instances;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

		public String getIdentifier() {
			return identifier;
		}
	}

	private class PeriodInstanceImpl implements PeriodInstance {
		private Double score = 0d;
		private long start;
		private long end;
		private int index;

		public PeriodInstanceImpl(long start, long end) {
			this.start = start;
			this.end = end;
		}

		public PeriodInstanceImpl(Map<String, Object> jsonProps) {
			Object scoreField = jsonProps.get("score");
			Object startField = jsonProps.get("start");
			Object endField = jsonProps.get("end");
			Object indexField = jsonProps.get("index");
			if (scoreField != null) {
				if (scoreField instanceof Double) {
					score = (Double) scoreField;
				}

				if (scoreField instanceof Integer) {
					score = Integer.valueOf((Integer) scoreField).doubleValue();
				}
			}

			if (startField != null) {
				if (startField instanceof Long) {
					start = (Long) startField;
				}

				if (startField instanceof Integer) {
					start = Integer.valueOf((Integer) startField).longValue();
				}
			}

			if (endField != null) {
				if (endField instanceof Long) {
					end = (Long) endField;
				}

				if (endField instanceof Integer) {
					end = Integer.valueOf((Integer) endField).longValue();
				}
			}

			if (indexField != null) {
				if (indexField instanceof Integer) {
					index = Integer.valueOf((Integer) indexField);
				}
			}
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
			return String.format("[start: %s, end: %s, score: %s]",
					formatter.format(new Date(start)),
					formatter.format(new Date(end)), score);
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}

	/*
	 * ExecutionMoment should be a private immutable field. Actually I need
	 * public getter/setter to expose the field to some tests with temporary
	 * constraints...TO IMPROVE
	 */
	public long getExecutionMoment() {
		return executionMoment;
	}

	/*
	 * ExecutionMoment should be a private immutable field. Actually I need
	 * public getter/setter to expose the field to some tests with temporary
	 * constraints...TO IMPROVE
	 */
	public void setExecutionMoment(long executionMoment) {
		this.executionMoment = executionMoment;
	}

}
