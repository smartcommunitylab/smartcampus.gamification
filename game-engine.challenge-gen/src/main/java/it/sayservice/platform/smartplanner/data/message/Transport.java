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
 * A transport
 */
public class Transport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3477444815169512553L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((agencyId == null) ? 0 : agencyId.hashCode());
		result = prime * result + ((routeId == null) ? 0 : routeId.hashCode());
		result = prime * result + ((tripId == null) ? 0 : tripId.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Transport other = (Transport) obj;
		if (agencyId == null) {
			if (other.agencyId != null)
				return false;
		} else if (!agencyId.equals(other.agencyId))
			return false;
		if (routeId == null) {
			if (other.routeId != null)
				return false;
		} else if (!routeId.equals(other.routeId))
			return false;
		if (routeShortName == null) {
			if (other.routeShortName != null)
				return false;
		} else if (!routeShortName.equals(other.routeShortName))
			return false;		
		if (tripId == null) {
			if (other.tripId != null)
				return false;
		} else if (!tripId.equals(other.tripId))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public Transport(TType type, String agencyId, String routeId, String routeShortName, String tripId) {
		super();
		this.type = type;
		this.agencyId = agencyId;
		this.routeId = routeId;
		this.routeShortName = routeShortName;
		this.tripId = tripId;
	}

	// for compatibility only
	public Transport(TType type, String agencyId, String routeId, String tripId) {
		super();
		this.type = type;
		this.agencyId = agencyId;
		this.routeId = routeId;
		this.routeShortName = routeId;
		this.tripId = tripId;
	}	
	
	public Transport() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * Transport type
	 */
	private TType type;
	
	/**
	 * Id of the agency
	 */
	private String agencyId;
	
	/**
	 * Id of the route
	 */
	private String routeId;
	
	/**
	 * Short name of the route
	 */
	private String routeShortName;	
	
	/**
	 * Id of the trip
	 */
	private String tripId;

	public TType getType() {
		return type;
	}

	public void setType(TType type) {
		this.type = type;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getRouteShortName() {
		return routeShortName;
	}

	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	public String getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}

	@Override
	public String toString() {
		return "Transport [type=" + type + ", agencyId=" + agencyId
				+ ", routeId=" + routeId + ",routeShortName=" + routeShortName + ", tripId=" + tripId + "]";
	}


}
