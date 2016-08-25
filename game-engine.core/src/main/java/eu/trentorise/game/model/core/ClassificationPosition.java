package eu.trentorise.game.model.core;

public class ClassificationPosition {
	private double score;
	private String playerId;
	private int position;

	public ClassificationPosition(double score, String playerId) {
		this.score = score;
		this.playerId = playerId;
	}

	public ClassificationPosition(double score, String playerId, int position) {
		this.score = score;
		this.playerId = playerId;
		this.position = position;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public int getPosition() {
		return position;
	}
}
