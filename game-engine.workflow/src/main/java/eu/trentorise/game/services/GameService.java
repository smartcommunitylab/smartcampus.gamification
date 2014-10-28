package eu.trentorise.game.services;

import org.springframework.stereotype.Service;

@Service
public interface GameService {

	public String getGameIdByAction(String actionId);

	public void startupTasks(String gameId);
}
