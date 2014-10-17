package eu.trentorise.game.model;

public class PointConcept extends GameConcept {

	public PointConcept(String name) {
		super(name);
	}

	public PointConcept() {

	}

	private Double score = 0.0;

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

}
