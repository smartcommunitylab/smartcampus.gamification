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

package it.sayservice.platform.smartplanner.data.message.alerts;

import it.sayservice.platform.smartplanner.data.message.StopId;


public class AlertParking extends Alert {

	// TODO: poi
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1090808895270541790L;

	/** agencyId(BIKE-STATION,CAR-STATION), id(PLACE ID) **/
	private StopId place;
	
	/** number of parking posts. **/
	private int placesAvailable;
	
	/** number of available rental vehicles **/
	private int noOfvehicles;

	public StopId getPlace() {
		return place;
	}

	public void setPlace(StopId place) {
		this.place = place;
	}

	public int getPlacesAvailable() {
		return placesAvailable;
	}

	public void setPlacesAvailable(int placesAvailable) {
		this.placesAvailable = placesAvailable;
	}

	public int getNoOfvehicles() {
		return noOfvehicles;
	}

	public void setNoOfvehicles(int noOfvehicles) {
		this.noOfvehicles = noOfvehicles;
	}

}
