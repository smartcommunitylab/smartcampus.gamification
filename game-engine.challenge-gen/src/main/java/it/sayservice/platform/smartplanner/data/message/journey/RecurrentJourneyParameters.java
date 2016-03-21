/**
 *    Copyright 2011-2013 SAYservice s.r.l.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package it.sayservice.platform.smartplanner.data.message.journey;

import it.sayservice.platform.smartplanner.data.message.Position;
import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;

import java.io.Serializable;
import java.util.List;

/**
 * 
 * Parameters needed to generate a recurrent journey.<br/>
 * <br/>
 * For an example, see this json representation of an instance of this class:<br/>
 * <br/>
 * { "recurrence" : [1,4], "from" :  "46.062005,11.129169", "to" : "46.068854,11.151184", "time" : "2:48pm", "interval" : 7200000, "transportTypes" : ["TRANSIT"], "routeType" : "fastest", "fromDate" : "1347487200000", "toDate" : "1356130800000" }
 * 
 */
public class RecurrentJourneyParameters implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 524169102342062982L;

	/**
	 * List of days of the week. See GregorianCalendar DAY_OF_WEEK for the allowed values
	 */
	private List<Integer> recurrence;
	
	/**
	 * Start place
	 */
	private Position from;
	
	/**
	 * End place
	 */
	private Position to;
	
	/**
	 * From date, in milliseconds
	 */
	private long fromDate;
	
	/**
	 * To date, in milliseconds
	 */
	private long toDate;
	
	/**
	 * Start time. Format is hh:mm, 12 hours am/pm
	 */
	private String time;
	
	/**
	 * Interval
	 */
	private long interval;
	
	/**
	 * Transport types
	 */
	private TType[] transportTypes;
	
	/**
	 * Route type
	 */
	private RType routeType;
	
	/**
	 * Results number
	 */
	private int resultsNumber = 1;

	public List<Integer> getRecurrence() {
		return recurrence;
	}
	public void setRecurrence(List<Integer> recurrence) {
		this.recurrence = recurrence;
	}
	public Position getFrom() {
		return from;
	}
	public void setFrom(Position from) {
		this.from = from;
	}
	public Position getTo() {
		return to;
	}
	public void setTo(Position to) {
		this.to = to;
	}
	public long getFromDate() {
		return fromDate;
	}
	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}
	public long getToDate() {
		return toDate;
	}
	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public long getInterval() {
		return interval;
	}
	public void setInterval(long interval) {
		this.interval = interval;
	}
	public TType[] getTransportTypes() {
		return transportTypes;
	}
	public void setTransportTypes(TType[] transportTypes) {
		this.transportTypes = transportTypes;
	}
	public RType getRouteType() {
		return routeType;
	}
	public void setRouteType(RType routeType) {
		this.routeType = routeType;
	}
	public int getResultsNumber() {
		return resultsNumber;
	}
	public void setResultsNumber(int resultsNumber) {
		this.resultsNumber = resultsNumber;
	}

}
