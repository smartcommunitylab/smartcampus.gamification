package eu.trentorise.game.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;

public class ChallengeReportCSVStub {
	private String id;
	private String name;
	private String modelName;
	private String playerId;
	private String gameId;
	private Date start;
	private Date end;
	private Map<ChallengeState, Date> stateDate = new HashMap<>();
	private boolean hide;
	private String challengeType;

	public ChallengeReportCSVStub() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}

	public Map<ChallengeState, Date> getStateDate() {
		return stateDate;
	}

	public void setStateDate(Map<ChallengeState, Date> stateDate) {
		this.stateDate = stateDate;
	}

	public boolean isHide() {
		return hide;
	}

	public void setHide(boolean hide) {
		this.hide = hide;
	}

	public String getChallengeType() {
		return challengeType;
	}

	public void setChallengeType(String challengeType) {
		this.challengeType = challengeType;
	}

}
