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
 * The time a trip passes by a stop
 */
public class StopTime implements Comparable<StopTime> {

    /**
     * Stop time, in milliseconds
     */
    private long time;

    /**
     * Id of the trip
     */
    private Id trip;

    @Override
    public String toString() {
	if (getTime() > 0) {
	    return Long.toString(getTime());
	}

	return super.toString();
    }

    public long getTime() {
	return time;
    }

    public void setTime(long time) {
	this.time = time;
    }

    public Id getTrip() {
	return trip;
    }

    public void setTrip(Id trip) {
	this.trip = trip;
    }

    public int compareTo(StopTime o) {
	return (int) (time - o.time);
    }

}
