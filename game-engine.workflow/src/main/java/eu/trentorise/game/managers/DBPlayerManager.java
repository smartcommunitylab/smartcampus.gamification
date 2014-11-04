package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.repo.PlayerRepo;
import eu.trentorise.game.repo.StatePersistence;
import eu.trentorise.game.services.PlayerService;

@Component("dbPlayerManager")
public class DBPlayerManager implements PlayerService {

	private final Logger logger = LoggerFactory
			.getLogger(DBPlayerManager.class);

	@Autowired
	private PlayerRepo repo;

	public PlayerState loadState(String userId, String gameId) {
		eu.trentorise.game.repo.StatePersistence state = repo
				.findByGameIdAndPlayerId(gameId, userId);

		return state == null ? new PlayerState(userId, gameId) : state
				.toPlayerState();
	}

	public boolean saveState(String userId, String gameId, PlayerState state) {
		eu.trentorise.game.repo.StatePersistence persistedState = repo
				.findByGameIdAndPlayerId(gameId, userId);
		StatePersistence toSave = new StatePersistence(state);
		if (persistedState == null) {
			persistedState = toSave;
		} else {
			persistedState.setConcepts(toSave.getConcepts());
		}

		repo.save(persistedState);
		return true;
	}

	public List<String> readPlayers(String gameId) {
		List<StatePersistence> states = repo.findByGameId(gameId);
		List<String> result = new ArrayList<String>();
		for (StatePersistence state : states) {
			result.add(state.getPlayerId());
		}

		return result;
	}

	public List<PlayerState> loadStates(String gameId) {
		List<StatePersistence> states = repo.findByGameId(gameId);
		List<PlayerState> result = new ArrayList<PlayerState>();
		for (StatePersistence state : states) {
			result.add(state.toPlayerState());
		}

		return result;
	}
}
