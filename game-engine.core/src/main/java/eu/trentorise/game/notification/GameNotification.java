package eu.trentorise.game.notification;

import java.util.Map;

import eu.trentorise.game.model.core.Notification;

public class GameNotification extends Notification {

	private String pointConceptName;
	private Double score;
	private String actionId;
	private Double delta;
	private Map<String, Object> dataPayLoad;

	public String getPointConceptName() {
		return pointConceptName;
	}

	public void setPointConceptName(String pointConceptName) {
		this.pointConceptName = pointConceptName;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public Map<String, Object> getDataPayLoad() {
		return dataPayLoad;
	}

	public void setDataPayLoad(Map<String, Object> dataPayLoad) {
		this.dataPayLoad = dataPayLoad;
	}

	public Double getDelta() {
		return delta;
	}

	public void setDelta(Double delta) {
		this.delta = delta;
	}
	
	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, pointConceptName=%s, actionId=%s, score=%s, delta=%s, dataPayLoad=%s]",
				getGameId(), getPlayerId(), pointConceptName, actionId, score, delta, dataPayLoad);
	}

}
