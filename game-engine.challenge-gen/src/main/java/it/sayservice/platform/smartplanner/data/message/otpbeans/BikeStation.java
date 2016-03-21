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

package it.sayservice.platform.smartplanner.data.message.otpbeans;

import it.sayservice.platform.smartplanner.data.message.StopId;

import java.util.Arrays;


public class BikeStation {
	
	private String id;
	private StopId stationId;
	private String type;
	private String fullName;
	private double[] position;
	private int availableSharingVehicles;
	private int posts;

	/**
	 * @param stationId
	 * @param type
	 * @param agencyId
	 * @param position
	 * @param number
	 */
	public BikeStation(StopId stationId, String type,
			double x, double y, int noAvialableVehicles, int post) {
		super();
		this.id = stationId.getId();
		this.stationId = stationId;
		this.type = type;
		this.position = new double[] {x, y};
		this.availableSharingVehicles = noAvialableVehicles;
		this.posts = post;
		this.fullName = x + "," + y;
	}

	public BikeStation() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public double[] getPosition() {
		return position;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public StopId getStationId() {
		return stationId;
	}

	public void setStationId(StopId stationId) {
		this.stationId = stationId;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public int getAvailableSharingVehicles() {
		return availableSharingVehicles;
	}

	public void setAvailableSharingVehicles(int availableSharingVehicles) {
		this.availableSharingVehicles = availableSharingVehicles;
	}

	public int getPosts() {
		return posts;
	}

	public void setPosts(int posts) {
		this.posts = posts;
	}

	@Override
	public String toString() {
		return "BikeStation [id=" + id + ", stationId=" + stationId + ", type="
				+ type + ", fullName=" + fullName + ", position="
				+ Arrays.toString(position) + ", availableSharingVehicles="
				+ availableSharingVehicles + ", posts=" + posts + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		BikeStation other = (BikeStation) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	

}
