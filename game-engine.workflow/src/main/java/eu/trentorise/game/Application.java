package eu.trentorise.game;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import eu.trentorise.game.services.Workflow;

@ComponentScan("eu.trentorise.game")
@Configuration
public class Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(Application.class);
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
		data.put("bikeDistance", 1.93);
		data.put("walkDistance", 7.100);
		data.put("busDistance", 1.00);
		data.put("bikesharing", true);
		data.put("park", "MANIFATTURA");
		wf.apply("save_itinerary", "1", data);
	}

}
