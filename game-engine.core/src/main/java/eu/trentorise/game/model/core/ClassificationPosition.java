package eu.trentorise.game.model.core;

public class ClassificationPosition {
	private double score;
	private String playerId;

	public ClassificationPosition(double score, String playerId) {
		this.score = score;
		this.playerId = playerId;
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

}
