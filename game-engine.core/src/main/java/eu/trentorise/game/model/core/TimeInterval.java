package eu.trentorise.game.model.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TimeInterval {
	private int value;
	private TimeUnit unit;

	@JsonCreator
	public TimeInterval(@JsonProperty("value") int value,
			@JsonProperty("unit") TimeUnit unit) {
		this.value = value;
		this.unit = unit;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public TimeUnit getUnit() {
		return unit;
	}

	public void setUnit(TimeUnit unit) {
		this.unit = unit;
	}

}
