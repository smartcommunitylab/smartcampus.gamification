package it.sayservice.platform.smartplanner.data.message.otpbeans;

import java.util.List;

public class ExtendedCompressedTransitTimeTable extends CompressedTransitTimeTable {

	private List<Integer> invisibles;
	private List<String> line;
	private List<String> frequency;
	private List<String> routesIds;

	private String shortDescription;
	private String longDescription;
	private String validity;
	private String schedule;

	public List<Integer> getInvisibles() {
		return invisibles;
	}

	public void setInvisibles(List<Integer> invisibles) {
		this.invisibles = invisibles;
	}

	public List<String> getLine() {
		return line;
	}

	public void setLine(List<String> line) {
		this.line = line;
	}

	public List<String> getFrequency() {
		return frequency;
	}

	public void setFrequency(List<String> frequency) {
		this.frequency = frequency;
	}

	public List<String> getRoutesIds() {
		return routesIds;
	}

	public void setRoutesIds(List<String> routesIds) {
		this.routesIds = routesIds;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public void setLongDescription(String longDescription) {
		this.longDescription = longDescription;
	}

	public String getValidity() {
		return validity;
	}

	public void setValidity(String validity) {
		this.validity = validity;
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule;
	}
}
