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

/**
 * Parameters to plan a single journey.<br/>
 * <br/>
 * For an example, see this json representation of an instance of this class:<br/>
 * <br/>
 * { "from" : { "lat" : 46.062005, "lon" : 11.129169} , "to" : { "lat" : 46.068854, "lon" : 11.151184} ,  "date" : "12/13/2012", "departureTime" : "1:25pm", "transportTypes" : [ "TRANSIT"], "routeType" : "fastest", "resultsNumber" : 1 }
 */
public class SingleJourney implements Serializable {

	private static final long serialVersionUID = -2518737295064515663L;
	
	/**
	 * Start location
	 */
	private Position from;
	
	/**
	 * End location
	 */
	private Position to;
	
	/**
	 * Date. Format is mm/dd/yyyy
	 */
	private String date;
	
	/**
	 * Departure time. Format is hh:mm, 12 hours am/pm
	 */
	private String departureTime;
	
	/**
	 * Transport types
	 */
	private TType[] transportTypes;
	
	/**
	 * Route type
	 */
	private RType routeType;
	
	/**
	 * Results number. Currently not used (forced at 3 for TRANSIT transport types, and to 1 otherwise.
	 */
	private int resultsNumber = 1;
	
	
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
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getDepartureTime() {
		return departureTime;
	}
	public void setDepartureTime(String departureTime) {
		this.departureTime = departureTime;
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
