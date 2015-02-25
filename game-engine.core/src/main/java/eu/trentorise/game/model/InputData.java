package eu.trentorise.game.model;

import java.util.Map;

public class InputData {
	private Map<String, Object> data;

	public InputData(Map<String, Object> data) {
		this.data = data;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
