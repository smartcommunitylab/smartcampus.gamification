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

import it.sayservice.platform.smartplanner.data.message.SimpleLeg;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Result of a recurrent journey planning<br/>
 * <br/>
 * For an example, see this json representation of an instance of this class:<br/>
 * <br/>
 * {"parameters":{"time":"2:48pm","routeType":"fastest","transportTypes":["TRANSIT"],"from":{"name":null,"lat":"46.062005","lon":"11.129169","stopId":null,"stopCode":null},"to":{"name":null,"lat":"46.068854","lon":"11.151184","stopId":null,"stopCode":null},"fromDate":1347487200000,"interval":7200000,"resultsNumber":1,"toDate":1356130800000,"recurrence":[1,4]},"monitorLegs":{"12_5":true,"12_7":true},"legs":[{"from":"dei Mille  \"Villa Igea\"","to":"S.Francesco  Porta Nuova","transport":{"type":"BUS","agencyId":"12","routeId":"7","tripId":"07R-Festivo_018"}},{"from":"S.Francesco  Porta Nuova","to":"POVO  Valoni","transport":{"type":"BUS","agencyId":"12","routeId":"5","tripId":"05A-Festivo_010"}}]}
 */
public class RecurrentJourney implements Serializable {

	private static final long serialVersionUID = -5833810427596973852L;
	
	/**
	 * Parameters used by the planner to generate this instance
	 */
	private RecurrentJourneyParameters parameters;
	
	/**
	 * List of simple legs returned by the planner
	 */
	private List<SimpleLeg> legs;
	
	private Map<String, Boolean> monitorLegs;
	
	
	public RecurrentJourneyParameters getParameters() {
		return parameters;
	}
	public void setParameters(RecurrentJourneyParameters parameters) {
		this.parameters = parameters;
	}
	public List<SimpleLeg> getLegs() {
		return legs;
	}
	public void setLegs(List<SimpleLeg> legs) {
		this.legs = legs;
	}
	public Map<String, Boolean> getMonitorLegs() {
		return monitorLegs;
	}
	public void setMonitorLegs(Map<String, Boolean> monitorLegs) {
		this.monitorLegs = monitorLegs;
	}	
	
	
}
