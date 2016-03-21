/*******************************************************************************
 * Copyright 2012-2013 Trento RISE
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
 ******************************************************************************/
package it.sayservice.platform.smartplanner.data.message;

import java.io.Serializable;

/**
 * Leg geometry (for graphical representation). Similar to {@link Leg}, but less detailed.
 */
public class SimpleLeg implements Serializable {

	private static final long serialVersionUID = -8082821649049055050L;
	
		/**
		 * Start location name
		 */
		private String from;
		
		/**
		 * End location name
		 */
		private String to;
		
		/**
		 * Transport
		 */
		private Transport transport;

		// TODO: alert list?
		
		public SimpleLeg() {
		}
		
		public SimpleLeg(String from, String to, Transport transport) {
			this.from = from;
			this.to = to;
			this.transport = transport;
		}		
		
		public SimpleLeg(Leg leg) {
			this.from = leg.getFrom().getName();
			this.to = leg.getTo().getName();
			this.transport = leg.getTransport();
		}
		
		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getTo() {
			return to;
		}

		public void setTo(String to) {
			this.to = to;
		}

		public Transport getTransport() {
			return transport;
		}

		public void setTransport(Transport transport) {
			this.transport = transport;
		}

		@Override
		public String toString() {
			return "Leg [from=" + from + ", to=" + to + ", " + transport + "]";
		}
		
		
}
