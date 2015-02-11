package eu.trentorise.game.services;

import java.util.List;

import org.springframework.stereotype.Service;

import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.Rule;

@Service
public interface GameService {

	public String getGameIdByAction(String actionId);

	public void startupTasks(String gameId);

	public void saveGameDefinition(Game game);

	public Game loadGameDefinitionById(String gameId);

	public List<Game> loadGames();

	public void addRule(Rule rule);

	public Rule loadRule(String gameId, String url);
}
