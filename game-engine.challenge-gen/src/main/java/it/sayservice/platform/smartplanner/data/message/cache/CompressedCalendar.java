package it.sayservice.platform.smartplanner.data.message.cache;

import java.util.Map;
import java.util.TreeMap;

public class CompressedCalendar {

	private Map<String, String> entries;
	private Map<String, String> mapping;
	
	public CompressedCalendar() {
		entries = new TreeMap<String, String>();
		mapping = new TreeMap<String, String>();
	}

	public Map<String, String> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, String> entries) {
		this.entries = entries;
	}

	public Map<String, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}
	
	@Override
	public String toString() {
		return mapping + ", " + entries;
	}	
	
}
