package eu.trentorise.game.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.model.core.GameConcept;

public class ChallengeConcept extends GameConcept {
	private String modelName;
	private Map<String, Object> fields = new HashMap<String, Object>();
	private Date start;
	private Date end;

	// metadata fields
	private boolean completed = false;
	private Date dateCompleted;

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Date getDateCompleted() {
		return dateCompleted;
	}

	/**
	 * Helper method of challenge
	 * 
	 * @return true if challenge is completed
	 */
	public boolean completed() {
		completed = true;
		dateCompleted = new Date();
		return true;
	}

	public boolean isCompleted() {
		return completed;
	}

}
