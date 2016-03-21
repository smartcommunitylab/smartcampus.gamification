package eu.trentorise.game.challenges.rest;

import java.util.Map;

public class ExecutionDataDTO {
    public String gameId;
    public String actionId;
    public String playerId;
    public Map<String, Object> data;

    public ExecutionDataDTO() {
    }

    public String getGameId() {
	return gameId;
    }

    public void setGameId(String gameId) {
	this.gameId = gameId;
    }

    public String getActionId() {
	return actionId;
    }

    public void setActionId(String actionId) {
	this.actionId = actionId;
    }

    public String getPlayerId() {
	return playerId;
    }

    public void setPlayerId(String playerId) {
	this.playerId = playerId;
    }

    public Map<String, Object> getData() {
	return data;
    }

    public void setData(Map<String, Object> data) {
	this.data = data;
    }

}