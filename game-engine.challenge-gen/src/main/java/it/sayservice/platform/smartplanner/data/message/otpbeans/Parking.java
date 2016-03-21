package it.sayservice.platform.smartplanner.data.message.otpbeans;

import java.util.Map;
import java.util.TreeMap;

public class Parking {
	private String name;
	private String description;
	private int slotsTotal;
	private int slotsAvailable;
	private double[] position;
	private boolean monitored;
	
	private Map<String, Object> extra;

	public Parking() {
		extra = new TreeMap<String, Object>();
	}
	
	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSlotsTotal() {
		return slotsTotal;
	}

	public void setSlotsTotal(int slotsTotal) {
		this.slotsTotal = slotsTotal;
	}

	public int getSlotsAvailable() {
		return slotsAvailable;
	}

	public void setSlotsAvailable(int slotsAvailable) {
		this.slotsAvailable = slotsAvailable;
	}

	public double[] getPosition() {
		return position;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public boolean isMonitored() {
		return monitored;
	}

	public void setMonitored(boolean monitored) {
		this.monitored = monitored;
	}
}
