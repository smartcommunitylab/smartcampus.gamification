package eu.trentorise.game.task;

public class Classification {
	private String name;
	private int position;
	private String scoreType;

	public Classification(String name, int position, String scoreType) {
		this.name = name;
		this.position = position;
		this.scoreType = scoreType;
	}

	public Classification() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getScoreType() {
		return scoreType;
	}

	public void setScoreType(String scoreType) {
		this.scoreType = scoreType;
	}

	@Override
	public String toString() {
		return String.format("{name: %s, scoreType: %s, position: %s}", name,
				scoreType, position);
	}
}
