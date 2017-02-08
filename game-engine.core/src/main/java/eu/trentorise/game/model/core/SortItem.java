package eu.trentorise.game.model.core;

public class SortItem {

	public static enum Direction {
		ASC, DESC;
	}

	private String field;
	private Direction direction;

	public SortItem(String field, Direction direction) {
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
