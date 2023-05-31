package eu.trentorise.game.notification;

import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.model.core.Notification;

public class GameNotification extends Notification {

	private String actionId;
	private Map<String, Object> dataPayLoad;
	private Map<String, Double> scoreMap = new HashMap<>();
	private Map<String, Double> deltaMap = new HashMap<>();

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
	
	public Map<String, Double> getScoreMap() {
		return scoreMap;
	}

	public void setScoreMap(Map<String, Double> scoreMap) {
		this.scoreMap = scoreMap;
	}

	public Map<String, Double> getDeltaMap() {
		return deltaMap;
	}

	public void setDeltaMap(Map<String, Double> deltaMap) {
		this.deltaMap = deltaMap;
	}

	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, executionMoment=%s, actionId=%s, scoreMap=%s, deltaMap=%s, dataPayLoad=%s]",
				getGameId(), getPlayerId(), getTimestamp(), actionId, scoreMap, deltaMap, dataPayLoad);
	}

}
