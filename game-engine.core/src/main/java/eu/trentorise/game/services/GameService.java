package eu.trentorise.game.services;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import eu.trentorise.game.model.Game;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.Rule;

@Service
public interface GameService {

	public String getGameIdByAction(String actionId);

	public void startupTasks(String gameId);

	public Game saveGameDefinition(Game game);

	public Game loadGameDefinitionById(String gameId);

	public Game loadGameDefinitionByAction(String actionId);

	public boolean deleteGame(String gameId);

	public List<Game> loadAllGames();

	public List<Game> loadGames(boolean onlyActive);

	public String addRule(Rule rule);

	public Rule loadRule(String gameId, String url);

	public boolean deleteRule(String gameId, String url);

	public void addConceptInstance(String gameId, GameConcept gc);

	public Set<GameConcept> readConceptInstances(String gameId);
}
