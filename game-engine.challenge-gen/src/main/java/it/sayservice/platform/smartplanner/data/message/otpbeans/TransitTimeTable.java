package it.sayservice.platform.smartplanner.data.message.otpbeans;

import java.util.List;
import java.util.Map;

/**
 * Timetable for a route, consisting of:<br/>
 * <ul>
 * <li>a list of stop names</li>
 * <li>a list of stop id (same length of stop names)</li>
 * <li>trip times, grouped by day and route</li>
 * <li>delays, grouped by day and route (each trip has a single entry)</li>
 * <li>delays sources, grouped by day and route (each trip has a single entry)</li>
 * </ul>
 * <br/>
 * For an example, see this (simplified, real results are much longer) json representation of an instance of this class:<br/>
 * <br/>
 * {"stops":["RONCAFORT nord","GARDOLO  P.le Neufahrn"],"stopsId":["22500c","22055n"],"times":[[["09:46:00",""],["10:04:00",""]], [["09:48:00",""],["10:06:00",""]]],"delays":[[0,0],[0,0]]}
 */
public class TransitTimeTable {

	/**
	 * stop names
	 */
  private List<String> stops;
  
  /**
   * stop ids
   */
  private List<String> stopsId;
  
  /**
   * times (day -> trip -> times)
   */
  private List<List<List<String>>> times;
  
  /**
   * delays (day -> trip)
   */
  private List<List<Map<String, String>>> delays;
  
  /**
   * tripIds (day -> trip -> id)
   */
  private List<List<String>> tripIds;  
  
  
	public TransitTimeTable() {
	}
	
	public TransitTimeTable(TransitTimeTable other) {
		this.delays = other.delays;
		this.stops = other.stops;
		this.stopsId = other.stopsId;
		this.times = other.times;
		this.tripIds = other.tripIds;
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
  public List<List<List<String>>> getTimes() {
      return times;
  }
  public void setTimes(List<List<List<String>>> times) {
      this.times = times;
  }
  public List<List<Map<String, String>>> getDelays() {
      return delays;
  }
  public void setDelays(List<List<Map<String, String>>> delays) {
      this.delays = delays;
  }
	public List<List<String>> getTripIds() {
		return tripIds;
	}
	public void setTripIds(List<List<String>> tripIds) {
		this.tripIds = tripIds;
	}
	
	@Override
  public String toString() {
  	return stops + ", " + stopsId + ", " + tripIds + ", " + times + ", " + delays;
  }
  
	public String toCSV() {
		String s = "";
		for (int i = 0; i < stops.size(); i++) {
			s += stops.get(i) + ",";
			List<List<String>> day = times.get(0);
			for (List<String> trip: day) {
				s += trip.get(i) + ","; 
			}
			s += "\n";
		}
		
		return s;
	}
	

}
