package eu.trentorise.game.model;

import java.util.HashMap;
import java.util.Map;

public class UpdateTeams {

	private Map<String, Object> data = new HashMap<>();

	private String propagationAction;

	public UpdateTeams() {
	}

	public void addData(String k, Object v) {
		this.data.put(k, v);
	}

	public UpdateTeams(String propagationAction) {
		this.propagationAction = propagationAction;
	}

	public String getPropagationAction() {
		return propagationAction;
	}

	public void setPropagationAction(String propagationAction) {
		this.propagationAction = propagationAction;
	}

	public Map<String, Object> getData() {
		return this.data;
	}
}
