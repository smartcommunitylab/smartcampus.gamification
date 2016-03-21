package eu.trentorise.game.challenges.util;

import java.util.Map;

public class JourneyData {

    private String userId;
    private Map<String, Object> data;

    public String getUserId() {
	return userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    public Map<String, Object> getData() {
	return data;
    }

    public void setData(Map<String, Object> data) {
	this.data = data;
    }

}
