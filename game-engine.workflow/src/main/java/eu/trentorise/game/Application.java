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
		 * bikeDistance walkDistance busDistance trainDistance
		 * 
		 */

		Map<String, Object> data = new HashMap<String, Object>();
		data.put("bikeDistance", 1.9);
		data.put("footDistance", 0.100);

		wf.apply("action1", "1", data);
	}

}
