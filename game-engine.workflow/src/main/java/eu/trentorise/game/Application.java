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
		Workflow wf = ctx.getBean(Workflow.class);

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
		 * 
		 * 
		 */

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bikeDistance", 4.43);
		data.put("walkDistance", 7.100);
		data.put("busDistance", 1.00);
		data.put("bikesharing", true);
		data.put("park", "MANIFATTURA");
		// data.put("park", "QUERCIA");
		wf.apply("save_itinerary", "2", data);
	}

}
