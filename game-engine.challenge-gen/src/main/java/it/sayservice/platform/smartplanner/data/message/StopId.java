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
import java.util.Map;

/**
 * Id of a stop
 */
public class StopId implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5663666850469168561L;
	
	/**
	 * Id of the agency
	 */
	private String agencyId;
	
	/**
	 * Id of the stop
	 */
	private String id;
	
	private Map<String,Object> extra;	
	
	/**
	 * @param agencyId
	 * @param id
	 */
	public StopId(String agencyId, String id) {
		super();
		this.agencyId = agencyId;
		this.id = id;
	}
	/**
	 * 
	 */
	public StopId() {
		// TODO Auto-generated constructor stub
	}
	public String getAgencyId() {
		return agencyId;
	}
	public void setAgencyId(String agencyId) {
		this.agencyId = agencyId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Map<String, Object> getExtra() {
		return extra;
	}
	public void setExtra(Map<String, Object> extra) {
		this.extra = extra;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "StopId [agencyId=" + agencyId + ", id=" + id + ", extra=" + extra + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((agencyId == null) ? 0 : agencyId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		StopId other = (StopId) obj;
		if (agencyId == null) {
			if (other.agencyId != null)
				return false;
		} else if (!agencyId.equals(other.agencyId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
}
