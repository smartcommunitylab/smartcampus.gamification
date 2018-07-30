package eu.trentorise.game.model.core;

import java.util.Date;
import java.util.Map;

public class ChallengeAssignment {
    private String gameId;
    private String playerId;
    private String modelName;
    private String instanceName;
    private Map<String, Object> data;
    private String challengeType;
    private Date start;
    private Date end;

    public ChallengeAssignment(String gameId, String playerId, String modelName,
            String instanceName, Map<String, Object> data, String challengeType, Date start,
            Date end) {
        this.gameId = gameId;
        this.playerId = playerId;
        this.modelName = modelName;
        this.instanceName = instanceName;
        this.data = data;
        this.challengeType = challengeType;
        this.start = start;
        this.end = end;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String getChallengeType() {
        return challengeType;
    }

    public void setChallengeType(String challengeType) {
        this.challengeType = challengeType;
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
}