package eu.trentorise.game.model;

import java.util.HashMap;
import java.util.Map;

public class Transmission {

	private Map<String, Object> data;

	public Transmission(Map<String, Object> data) {
		setData(data);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}

	public String toString() {
		return String.format("%s(data=%s)", getClass()
				.getSimpleName(), getData());
	}
}
