package it.sayservice.platform.smartplanner.data.message.otpbeans;

import java.util.Iterator;
import java.util.List;

public class CompressedTransitTimeTable {
	private List<String> stops;

	private List<String> stopsId;

	private List<String> tripIds;

	private String compressedTimes;
	
	private List<String> routesIds;

	public CompressedTransitTimeTable() {
		
	}
	
	public CompressedTransitTimeTable(TransitTimeTable tt) {
		this.stops = tt.getStops();
		this.stopsId = tt.getStopsId();
		if (tt.getTripIds() != null && tt.getTripIds().size() >= 1) {
			this.tripIds = tt.getTripIds().get(0);
		}
		List<List<List<String>>> times = tt.getTimes();
		compressedTimes = "";
		Iterator<List<List<String>>> it = times.iterator();
		while (it.hasNext()) {
			List<List<String>> day = it.next();
			String compressedDayTimes = "";
			for (List<String> trip : day) {
				for (String time : trip) {
					if (time.length() == 0) {
						compressedDayTimes += "|";
					} else {
						compressedDayTimes += time.replace(":", "");
					}
				}
			}
			compressedTimes += compressedDayTimes + (it.hasNext() ? "#" : "");
		}

	}

	public List<String> getStops() {
		return stops;
	}

	public void setStops(List<String> stops) {
		this.stops = stops;
	}

	public List<String> getStopsId() {
		return stopsId;
	}

	public void setStopsId(List<String> stopsId) {
		this.stopsId = stopsId;
	}

	public String getCompressedTimes() {
		return compressedTimes;
	}

	public void setCompressedTimes(String compressedTimes) {
		this.compressedTimes = compressedTimes;
	}

	public List<String> getTripIds() {
		return tripIds;
	}

	public void setTripIds(List<String> tripIds) {
		this.tripIds = tripIds;
	}

	public List<String> getRoutesIds() {
		return routesIds;
	}

	public void setRoutesIds(List<String> routeIds) {
		this.routesIds = routeIds;
	}

	@Override
	public String toString() {
		return stops + ", " + stopsId + ", " + tripIds + ", " + compressedTimes;
	}

}
