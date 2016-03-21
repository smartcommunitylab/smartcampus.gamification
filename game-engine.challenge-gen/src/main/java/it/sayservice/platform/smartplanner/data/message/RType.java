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

/**
 * Route type enum
 */
public enum RType {
	fastest,
	healthy,
	leastWalking,
	leastChanges,
	greenest,
	safest;
	
	public static RType getRoute(String route) {
		if (route.equalsIgnoreCase("fastest")) {
			return RType.fastest;
		} else if (route.equalsIgnoreCase("healty")) {
			return RType.healthy;
		} else if (route.equalsIgnoreCase("leastWalking")) {
			return RType.leastWalking;
		} else if (route.equalsIgnoreCase("leastChanges")) {
			return RType.leastChanges;
		} else if (route.equalsIgnoreCase("greenest")) {
			return RType.greenest;
		} else if (route.equalsIgnoreCase("saftest")) {
			return RType.safest;
		}
		return RType.fastest;
	}
}
