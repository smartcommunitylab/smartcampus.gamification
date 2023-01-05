package eu.trentorise.game.bean;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import eu.trentorise.game.model.Level.Config;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LevelDTO {
	private String id;
	private String name;

	@JsonProperty("pointConcept")
	private String pointConceptName;

	private List<ThresholdDTO> thresholds = new ArrayList<>();

	@JsonIgnoreProperties(ignoreUnknown = true)
	public static class ThresholdDTO {
		private String name;
		private double value;
		private Config config;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public double getValue() {
			return value;
		}

		public void setValue(double value) {
			this.value = value;
		}

		public Config getConfig() {
			return config;
		}

		public void setConfig(Config config) {
			this.config = config;
		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPointConceptName() {
		return pointConceptName;
	}

	public void setPointConceptName(String pointConceptName) {
		this.pointConceptName = pointConceptName;
	}

	public List<ThresholdDTO> getThresholds() {
		return thresholds;
	}

	public void setThresholds(List<ThresholdDTO> thresholds) {
		this.thresholds = thresholds;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
