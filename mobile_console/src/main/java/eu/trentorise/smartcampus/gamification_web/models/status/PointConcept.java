package eu.trentorise.smartcampus.gamification_web.models.status;

public class PointConcept {

	private String name;
	private int score;
	
	public String getName() {
		return name;
	}

	public int getScore() {
		return score;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public PointConcept() {
		super();
	}

	public PointConcept(String name, int score) {
		super();
		this.name = name;
		this.score = score;
	}
	
}
