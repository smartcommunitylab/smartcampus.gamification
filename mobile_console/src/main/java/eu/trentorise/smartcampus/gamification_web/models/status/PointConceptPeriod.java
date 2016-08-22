package eu.trentorise.smartcampus.gamification_web.models.status;

public class PointConceptPeriod {
	
	private int score;
	private long start;
	private long end;
	
	public int getScore() {
		return score;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void setStart(long start) {
		this.start = start;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	public PointConceptPeriod() {
		super();
	}

	public PointConceptPeriod(int score, long start, long end) {
		super();
		this.score = score;
		this.start = start;
		this.end = end;
	}

}
