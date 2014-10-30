package eu.trentorise.game.managers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.core.AppContextProvider;
import eu.trentorise.game.core.GameContext;
import eu.trentorise.game.core.TaskSchedule;
import eu.trentorise.game.services.GameService;
import eu.trentorise.game.task.ClassificationTask;

@Component
public class GameManager implements GameService {

	private static Map<String, String> repo;

	@Autowired
	TaskManager taskManager;

	@Autowired
	AppContextProvider provider;

	@PostConstruct
	@SuppressWarnings("unused")
	private void initRepo() {
		repo = new HashMap<String, String>();
		repo.put("save_itinerary", "game1");
		repo.put("classification", "game1");
		repo.put("action3", "game1");
		repo.put("action4", "game1");
		repo.put("action5", "game1");
		repo.put("action7", "game2");
		// repo.put("action1", "game3");
	}

	public String getGameIdByAction(String actionId) {
		return repo.get(actionId);
	}

	public void startupTasks(String gameId) {
		TaskSchedule schedule = new TaskSchedule();
		schedule.setCronExpression("0 * * * * *");
		taskManager.createTask(new ClassificationTask(schedule, 3,
				"green leaves", "final classification"), (GameContext) provider
				.getApplicationContext().getBean("gameCtx", gameId));

	}
}
