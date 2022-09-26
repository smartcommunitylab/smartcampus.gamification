package eu.trentorise.game.notification;

import eu.trentorise.game.model.core.Notification;

public class ChallengeFailedNotication extends Notification {
	private String challengeName;
	private String model;
	private String pointConcept;
	private long start;
	private long end;

	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}
	
	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getPointConcept() {
		return pointConcept;
	}

	public void setPointConcept(String pointConcept) {
		this.pointConcept = pointConcept;
	}
	
	public long getStart() {
		return start;
	}

	public void setStart(long start) {
		this.start = start;
	}
	
	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, challengeName=%s, model=%s, pointConcept=%s, start=%s, end=%s]", getGameId(), getPlayerId(), challengeName, model, pointConcept, start, end);
	}

}
