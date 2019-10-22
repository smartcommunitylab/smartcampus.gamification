package eu.trentorise.game.model.core;

import java.util.Date;
import java.util.Map;

public class ChallengeAssignment {
    private String modelName;
    private String instanceName;
    private Map<String, Object> data;
    private String challengeType;
    private String origin;
    private Date start;
    private Date end;
    private int priority = 0;
    private boolean hide = false;

    public ChallengeAssignment(String modelName,
            String instanceName, Map<String, Object> data, String challengeType, Date start,
            Date end) {
        this.modelName = modelName;
        this.instanceName = instanceName;
        this.data = data;
        this.challengeType = challengeType;
        this.start = start;
        this.end = end;
    }

    public ChallengeAssignment() {

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

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }
}