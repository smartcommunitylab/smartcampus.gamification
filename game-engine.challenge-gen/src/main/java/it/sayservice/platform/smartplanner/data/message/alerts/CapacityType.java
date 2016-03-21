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


public enum CapacityType {
	MEDIUM, FULL, DEFAULT;

	public static CapacityType getCapacityType(String type) {
		if (type.equalsIgnoreCase("MEDIUM"))
			return CapacityType.MEDIUM;
		else if (type.equalsIgnoreCase("FULL"))
			return CapacityType.FULL;
		return CapacityType.DEFAULT;
	}
}
