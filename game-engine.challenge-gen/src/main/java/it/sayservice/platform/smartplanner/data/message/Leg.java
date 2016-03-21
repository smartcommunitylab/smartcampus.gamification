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

package it.sayservice.platform.smartplanner.data.message;

import it.sayservice.platform.smartplanner.data.message.alerts.AlertAccident;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertDelay;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertParking;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertRoad;
import it.sayservice.platform.smartplanner.data.message.alerts.AlertStrike;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * Class representing a leg (a part of an itinerary)
 * 
 */
public class Leg implements Serializable {

	private static final long serialVersionUID = 1158583507390813968L;
	
	/**
	 * id
	 */
	private String legId;
	
	/**
	 * Start time
	 */
	private long startime;
	
	/**
	 * End time
	 */
	private long endtime;
	
	/**
	 * Duration
	 */
	private long duration;
	
	/***
	 * From position
	 */
	private Position from;
	
	/**
	 * To position
	 */
	private Position to;
	
	/**
	 * Transport
	 */
	private Transport transport;
	
	/**
	 * Leg geometry (for graphical representation)
	 */
	private LegGeometery legGeometery;
	
	
	private List<AlertStrike> alertStrikeList;
	private List<AlertDelay> alertDelayList;
	private List<AlertParking> alertParkingList;
	private List<AlertRoad> alertRoadList;
	private List<AlertAccident> alertAccidentList;

	private Map<String,Object> extra;
	
	/**
	 * Leg lenght
	 */
	private double length;
	
	public Leg(String legId, long startime, long endtime, long duration, double length,
			Position from, Position to, Transport transport,
			LegGeometery legGeometery, List<AlertDelay> alertDelayList,
			List<AlertStrike> alertStrikeList,
			List<AlertParking> alertParkingList, List<AlertRoad> alertRoadList, List<AlertAccident> alertAccidentList) {
		super();
		this.legId = legId;
		this.startime = startime;
		this.endtime = endtime;
		this.duration = duration;
		this.length = length;
		this.from = from;
		this.to = to;
		this.transport = transport;
		this.legGeometery = legGeometery;
		this.alertDelayList = alertDelayList;
		this.alertStrikeList = alertStrikeList;
		this.alertParkingList = alertParkingList;
		this.alertRoadList = alertRoadList;
		this.alertAccidentList = alertAccidentList;
	}

	public Leg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLegId() {
		return legId;
	}

	public void setLegId(String legId) {
		this.legId = legId;
	}

	public long getStartime() {
		return startime;
	}

	public void setStartime(long startime) {
		this.startime = startime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
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

	public Transport getTransport() {
		return transport;
	}

	public void setTransport(Transport tranport) {
		this.transport = tranport;
	}

	public LegGeometery getLegGeometery() {
		return legGeometery;
	}

	public void setLegGeometery(LegGeometery legGeometery) {
		this.legGeometery = legGeometery;
	}

	public List<AlertStrike> getAlertStrikeList() {
		return alertStrikeList;
	}

	public void setAlertStrikeList(List<AlertStrike> alertStrikeList) {
		this.alertStrikeList = alertStrikeList;
	}

	public List<AlertDelay> getAlertDelayList() {
		return alertDelayList;
	}

	public void setAlertDelayList(List<AlertDelay> alertDelayList) {
		this.alertDelayList = alertDelayList;
	}

	public List<AlertParking> getAlertParkingList() {
		return alertParkingList;
	}

	public void setAlertParkingList(List<AlertParking> alertParkingList) {
		this.alertParkingList = alertParkingList;
	}

	public List<AlertRoad> getAlertRoadList() {
		return alertRoadList;
	}

	public void setAlertRoadList(List<AlertRoad> alertRoadList) {
		this.alertRoadList = alertRoadList;
	}

	public List<AlertAccident> getAlertAccidentList() {
		return alertAccidentList;
	}

	public void setAlertAccidentList(List<AlertAccident> alertAccidentList) {
		this.alertAccidentList = alertAccidentList;
	}


	public Map<String, Object> getExtra() {
		return extra;
	}

	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "Leg [legId=" + legId + ", startime=" + startime + ", endtime="
				+ endtime + ", duration=" + duration + ", length=" + length + ", from=" + from
				+ ", to=" + to + ", transport=" + transport + ", legGeometery="
				+ legGeometery + ", alertStrikeList=" + alertStrikeList
				+ ", alertDelayList=" + alertDelayList + ", extra=" + extra +  "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((alertDelayList == null) ? 0 : alertDelayList.hashCode());
		result = prime
				* result
				+ ((alertParkingList == null) ? 0 : alertParkingList.hashCode());
		result = prime * result
				+ ((alertStrikeList == null) ? 0 : alertStrikeList.hashCode());
		result = prime * result + (int) (duration ^ (duration >>> 32));
		result = prime * result + (int) (endtime ^ (endtime >>> 32));
		result = prime * result + ((from == null) ? 0 : from.hashCode());
		result = prime * result
				+ ((legGeometery == null) ? 0 : legGeometery.hashCode());
		result = prime * result + ((legId == null) ? 0 : legId.hashCode());
		result = prime * result + (int) (startime ^ (startime >>> 32));
		result = prime * result + ((to == null) ? 0 : to.hashCode());
		result = prime * result
				+ ((transport == null) ? 0 : transport.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Leg other = (Leg) obj;
		if (alertDelayList == null) {
			if (other.alertDelayList != null)
				return false;
		} else if (!alertDelayList.equals(other.alertDelayList))
			return false;
		if (alertParkingList == null) {
			if (other.alertParkingList != null)
				return false;
		} else if (!alertParkingList.equals(other.alertParkingList))
			return false;
		if (alertStrikeList == null) {
			if (other.alertStrikeList != null)
				return false;
		} else if (!alertStrikeList.equals(other.alertStrikeList))
			return false;
		if (duration != other.duration)
			return false;
		if (endtime != other.endtime)
			return false;
		if (from == null) {
			if (other.from != null)
				return false;
		} else if (!from.equals(other.from))
			return false;
		if (legGeometery == null) {
			if (other.legGeometery != null)
				return false;
		} else if (!legGeometery.equals(other.legGeometery))
			return false;
		if (legId == null) {
			if (other.legId != null)
				return false;
		} else if (!legId.equals(other.legId))
			return false;
		if (startime != other.startime)
			return false;
		if (to == null) {
			if (other.to != null)
				return false;
		} else if (!to.equals(other.to))
			return false;
		if (transport == null) {
			if (other.transport != null)
				return false;
		} else if (!transport.equals(other.transport))
			return false;
		return true;
	}
}