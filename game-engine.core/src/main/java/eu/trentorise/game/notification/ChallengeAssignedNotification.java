package eu.trentorise.game.notification;

import java.util.Date;

import eu.trentorise.game.model.core.Notification;

public class ChallengeAssignedNotification extends Notification {

	private String challengeName;
	private Date startDate;
	private Date endDate;

	public String getChallengeName() {
		return challengeName;
	}

	public void setChallengeName(String challengeName) {
		this.challengeName = challengeName;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, challengeName=%s startDate=%s endDate=%s]", getGameId(),
				getPlayerId(), challengeName, startDate, endDate);
	}

}
