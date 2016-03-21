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
 * A route of a transit.</br>
 * </br>
 * For an example, see this json representation of an instance of this class:<br/>
 * {"id":{"id":"01","agency":"12"},"routeShortName":"1","routeLongName":"Roncafort - P.Dante - Osp.S.Chiara- Roncafort"}
 * </br>
 */
public class Route {

	/**
	 * Id of the route
	 */
	private Id id;

	/**
	 * Route short name
	 */
	private String routeShortName;
	
	/**
	 * Route long name
	 */
	private String routeLongName;

	@Override
	public String toString() {
		if (getRouteLongName().length() > 0 && getRouteShortName().length() > 0) {
			return getRouteShortName() + " - " + getRouteLongName();
		}

		return super.toString();
	}

	public Id getId() {
		return id;
	}

	public void setId(Id id) {
		this.id = id;
	}

	public String getRouteShortName() {
		return routeShortName;
	}

	public void setRouteShortName(String routeShortName) {
		this.routeShortName = routeShortName;
	}

	public String getRouteLongName() {
		return routeLongName;
	}

	public void setRouteLongName(String routeLongName) {
		this.routeLongName = routeLongName;
	}

}
