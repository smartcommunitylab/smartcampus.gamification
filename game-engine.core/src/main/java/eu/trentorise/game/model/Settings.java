package eu.trentorise.game.model;

import java.util.HashMap;
import java.util.Map;

public class Settings {

	private Map<String, String> statisticsConfig = new HashMap<String, String>();

	public Map<String, String> getStatisticsConfig() {
		return statisticsConfig;
	}

	public void setStatisticsConfig(Map<String, String> statisticsConfig) {
		this.statisticsConfig = statisticsConfig;
	}

}