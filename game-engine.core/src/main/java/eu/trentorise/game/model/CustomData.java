package eu.trentorise.game.model;

import java.util.HashMap;
import java.util.Map;

public class CustomData {
	private Map<String, Object> data;

	public CustomData() {
		data = new HashMap<String, Object>();
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

}
