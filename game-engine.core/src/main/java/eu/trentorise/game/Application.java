/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
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

package eu.trentorise.game;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import eu.trentorise.game.config.AppConfig;
import eu.trentorise.game.config.MongoConfig;
import eu.trentorise.game.services.Workflow;

public class Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig.class);
		ctx.register(MongoConfig.class);
		ctx.refresh();
		Workflow gameWorkflow = ctx.getBean("workflow", Workflow.class);

		/**
		 * input map known fields
		 * 
		 * bikeDistance walkDistance busDistance trainDistance carDistance ->
		 * double , distances must be expressed in km
		 * 
		 * bikeSharing -> boolean
		 * 
		 * park -> park id
		 * 
		 * sustainable -> boolean
		 * 
		 * p+r -> boolean
		 * 
		 * 
		 * 
		 */

		Map<String, Object> data = new HashMap<String, Object>();
		// data.put("bikeDistance", 8.43);
		// data.put("walkDistance", 3.100);
		// data.put("carDistance", 0.00);
		// data.put("busDistance", 0.00);
		// data.put("bikesharing", true);
		// data.put("sustainable", true);
		// data.put("p+r", true);
		// data.put("park", "MANIFATTURA");
		gameWorkflow.apply("demo-game", "save_itinerary", "1", data, null);
	}

}
