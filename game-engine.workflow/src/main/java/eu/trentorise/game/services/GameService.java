package eu.trentorise.game.services;

import org.springframework.stereotype.Service;

import eu.trentorise.game.model.Game;

@Service
public interface GameService {

	public String getGameIdByAction(String actionId);

	public void startupTasks(String gameId);

	public void saveGameDefinition(Game game);

	public Game loadGameDefinitionById(String gameId);
}
