package it.sayservice.platform.smartplanner.data.message.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CacheUpdateResponse {

	private List<String> added;
	private List<String> removed;
	private long version;
	private Map<String, CompressedCalendar> calendars;

	public CacheUpdateResponse() {
		added = new ArrayList<String>();
		removed = new ArrayList<String>();
		calendars = new TreeMap<String, CompressedCalendar>();
	}

	public List<String> getAdded() {
		return added;
	}

	public void setAdded(List<String> added) {
		this.added = added;
	}

	public List<String> getRemoved() {
		return removed;
	}

	public void setRemoved(List<String> removed) {
		this.removed = removed;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public Map<String, CompressedCalendar> getCalendars() {
		return calendars;
	}

	public void setCalendars(Map<String, CompressedCalendar> calendar) {
		this.calendars = calendar;
	}

	@Override
	public String toString() {
		return "V" + version + ": " + calendars + "," + added + "," + removed;
	}
	
}
