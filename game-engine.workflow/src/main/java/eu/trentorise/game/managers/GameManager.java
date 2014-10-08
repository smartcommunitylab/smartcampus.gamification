package eu.trentorise.game.managers;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import eu.trentorise.game.services.GameService;

@Component
public class GameManager implements GameService {

	private static Map<String, String> repo;

	@PostConstruct
	@SuppressWarnings("unused")
	private void initRepo() {
		repo = new HashMap<String, String>();
		repo.put("action1", "game1");
		repo.put("action2", "game1");
		repo.put("action3", "game1");
		repo.put("action4", "game1");
		repo.put("action5", "game1");
		repo.put("action7", "game2");
		// repo.put("action1", "game3");
	}

	public String getGameIdByAction(String actionId) {
		return repo.get(actionId);
	}

}
