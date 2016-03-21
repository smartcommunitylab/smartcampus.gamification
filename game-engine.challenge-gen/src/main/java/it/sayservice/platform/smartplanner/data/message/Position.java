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

import java.io.Serializable;

/**
 * A position, possibly associated to a stop.
 */
public class Position implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5144042125421161884L;
	
	/**
	 * Place name
	 */
	private String name;
	
	/**
	 * Stop id
	 */
	private StopId stopId;
	
	/**
	 * Stop code
	 */
	private String stopCode;
	
	/**
	 * Longitude
	 */
	private String lon;
	
	/**
	 * Latitude
	 */
	private String lat;
//	private Geometery geometery;

	public Position(String latlon) {
		String split[] = latlon.split(",");
		lat = split[0];
		lon = split[1];
	}
	
	public Position(String name, StopId stopId, String stopCode, String lon,
			String lat) {
		super();
		this.name = name;
		this.stopId = stopId;
		this.stopCode = stopCode;
		this.lon = lon;
		this.lat = lat;
//		this.geometery = geometery;
	}

	public Position() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StopId getStopId() {
		return stopId;
	}

	public void setStopId(StopId stopId) {
		this.stopId = stopId;
	}

	public String getStopCode() {
		return stopCode;
	}

	public void setStopCode(String stopCode) {
		this.stopCode = stopCode;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String toLatLon() {
		return lat + "," + lon;
	}
	
	@Override
	public String toString() {
		return "Position [name=" + name + ", stopId=" + stopId + ", stopCode="
				+ stopCode + ", lon=" + lon + ", lat=" + lat + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lat == null) ? 0 : lat.hashCode());
		result = prime * result + ((lon == null) ? 0 : lon.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((stopCode == null) ? 0 : stopCode.hashCode());
		result = prime * result + ((stopId == null) ? 0 : stopId.hashCode());
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
		Position other = (Position) obj;
		if (lat == null) {
			if (other.lat != null)
				return false;
		} else if (!lat.equals(other.lat))
			return false;
		if (lon == null) {
			if (other.lon != null)
				return false;
		} else if (!lon.equals(other.lon))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (stopCode == null) {
			if (other.stopCode != null)
				return false;
		} else if (!stopCode.equals(other.stopCode))
			return false;
		if (stopId == null) {
			if (other.stopId != null)
				return false;
		} else if (!stopId.equals(other.stopId))
			return false;
		return true;
	}
	
}
