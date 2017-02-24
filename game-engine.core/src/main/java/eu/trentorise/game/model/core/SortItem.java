package eu.trentorise.game.model.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SortItem {

	public static enum Direction {
		ASC, DESC;
	}

	private String field;
	private Direction direction;

	@JsonCreator
	public SortItem(@JsonProperty("field") String field,
			@JsonProperty("direction") Direction direction) {
		this.field = field;
		this.direction = direction;
	}

	public String getField() {
		return field;
	}

	public Direction getDirection() {
		return direction;
	}

}
