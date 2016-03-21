package it.sayservice.platform.smartplanner.data.message.otpbeans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * Information about stop times for a route at a particular stop.<br/>
 * <br/>
 * For an example, see this (limited to 5 times) json representation of an instance of this class:<br/>
 * <br/>
 * {"delays":{},"name":"Roncafort - P.Dante - Osp.S.Chiara- Roncafort","route":"1","times":[{"time":1363164000,"trip":{"id":"01-Feriale_015","agency":"12"}},{"time":1363165080,"trip":{"id":"01-Feriale_016","agency":"12"}},{"time":1363166160,"trip":{"id":"01-Feriale_017","agency":"12"}},{"time":1363167240,"trip":{"id":"01-Feriale_018","agency":"12"}},{"time":1363168320,"trip":{"id":"01-Feriale_019","agency":"12"}}]}
 */
public class LimitedTransitTimeTable {

	/**
	 * stop name
	 */
	private String name;
	
	/**
	 * route
	 */
	private String route;
	
	/**
	 * next stop times
	 */
	private List<StopTime> times;
	
	/**
	 * delays
	 */
	private Map<String, Integer> delays;
	
	public LimitedTransitTimeTable() {
		times = new ArrayList<StopTime>();
		delays = new TreeMap<String, Integer>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRoute() {
		return route;
	}

	public void setRoute(String route) {
		this.route = route;
	}

	public List<StopTime> getTimes() {
		return times;
	}

	public void setTimes(List<StopTime> times) {
		this.times = times;
	}

	public Map<String, Integer> getDelays() {
		return delays;
	}

	public void setDelays(Map<String, Integer> delays) {
		this.delays = delays;
	}

	@Override
	public String toString() {
		return route + ", " + name + ", " + times + ", " + delays;
	}
	
}
