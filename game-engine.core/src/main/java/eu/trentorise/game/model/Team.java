package eu.trentorise.game.model;

import java.util.HashMap;
import java.util.Map;

public class Team {
	private String playerId;
	private Map<String, Object> inputData;

	public Team(String playerId) {
		this.playerId = playerId;
		this.inputData = new HashMap<String, Object>();
	}

	public Team(String playerId, Map<String, Object> inputData) {
		this.playerId = playerId;
		this.inputData = inputData != null ? inputData
				: new HashMap<String, Object>();
	}

	public Map<String, Object> getInputData() {
		return inputData;
	}

	public void setInputData(Map<String, Object> inputData) {
		this.inputData = inputData;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	@Override
	public String toString() {
		return String.format("%s(playerId=%s, inputData=%s)", getClass()
				.getSimpleName(), playerId, inputData);
	}
}
