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

/**
 * 
 * A transport stop
 */
public class Stop {

	// for mongo storage: mongodb inverts lat/lon!
	private static final int LONGITUDE = 0;
	private static final int LATITUDE = 1;
	
	
	/**
	 * id of the stop
	 */
	private String id;
	
	/**
	 * name of the stop
	 */
	private String name;
	
	private double[] coordinates;

	public Stop() {
		coordinates = new double[2];
	}
	
	@Override
	public String toString() {
		if (getName().length() > 0)
			return getName();

		return super.toString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double[] getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(double[] coordinates) {
		this.coordinates = coordinates;
	}


	public double getLatitude() {
		return coordinates[LATITUDE];
	}

	public void setLatitude(double latitude) {
		coordinates[LATITUDE] = latitude;
	}

	public double getLongitude() {
		return coordinates[LONGITUDE];
	}

	public void setLongitude(double longitude) {
		coordinates[LONGITUDE] = longitude;
	}

}
