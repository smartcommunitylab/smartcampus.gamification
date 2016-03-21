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



public enum AlertType {

	// TODO: CUSTOM
		DELAY, ACCIDENT, PARKING, STRIKE, CUSTOM, ROAD;
	
		public static AlertType getAlertType (String type) {
			
			if (type.equalsIgnoreCase("delay"))
				return AlertType.DELAY;
			else if (type.equalsIgnoreCase("accident"))
				return AlertType.ACCIDENT;
			else if (type.equalsIgnoreCase("parking"))
				return AlertType.PARKING;
			else if (type.equalsIgnoreCase("strike"))
				return AlertType.STRIKE;
			else if (type.equalsIgnoreCase("road"))
				return AlertType.ROAD;
			
			return AlertType.CUSTOM;
			}
		
}
