package eu.trentorise.game.model;

import java.util.Map;

public class PlayerState {
	private Map<String, Object> state;

	public Map<String, Object> getState() {
		return state;
	}

	public void setState(Map<String, Object> state) {
		this.state = state;
	}

}
