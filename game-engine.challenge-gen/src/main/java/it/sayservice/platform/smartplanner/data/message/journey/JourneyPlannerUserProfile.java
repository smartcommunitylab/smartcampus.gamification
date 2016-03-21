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

import it.sayservice.platform.smartplanner.data.message.RType;
import it.sayservice.platform.smartplanner.data.message.TType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class JourneyPlannerUserProfile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2854169081506828870L;
	private String name;
	private String surname;
	
	private int greenPoints;
	private List<TType> transportPreferences;
	private RType routePreferences;
	
	public JourneyPlannerUserProfile() {
		transportPreferences = new ArrayList<TType>();
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public int getGreenPoints() {
		return greenPoints;
	}
	public void setGreenPoints(int greenPoints) {
		this.greenPoints = greenPoints;
	}
	public List<TType> getTransportPreferences() {
		return transportPreferences;
	}
	public void setTransportPreferences(List<TType> transportPreferences) {
		this.transportPreferences = transportPreferences;
	}
	public RType getRoutePreferences() {
		return routePreferences;
	}
	public void setRoutePreferences(RType routePreferences) {
		this.routePreferences = routePreferences;
	}
	
	
	
}
