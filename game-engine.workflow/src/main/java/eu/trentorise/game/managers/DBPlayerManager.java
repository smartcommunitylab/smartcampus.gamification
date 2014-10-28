package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.GameConcept;
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

	private ObjectMapper mapper = new ObjectMapper();;

	public PlayerState loadState(String userId, String gameId) {
		eu.trentorise.game.repo.StatePersistence state = repo
				.findByGameIdAndPlayerId(gameId, userId);

		return state == null ? new PlayerState() : convert(state);
	}

	public boolean saveState(String userId, String gameId, PlayerState state) {
		eu.trentorise.game.repo.StatePersistence persistedState = repo
				.findByGameIdAndPlayerId(gameId, userId);
		StatePersistence toSave = convert(userId, gameId, state);
		if (persistedState == null) {
			persistedState = toSave;
		} else {
			persistedState.setConcepts(toSave.getConcepts());
		}

		repo.save(persistedState);
		return true;
	}

	private eu.trentorise.game.repo.StatePersistence convert(String playerId,
			String gameId, PlayerState ps) {
		eu.trentorise.game.repo.StatePersistence sp = new eu.trentorise.game.repo.StatePersistence();
		sp.setGameId(gameId);
		sp.setPlayerId(playerId);
		for (GameConcept gc : ps.getState()) {
			sp.getConcepts().add(
					new eu.trentorise.game.repo.ConceptPersistence(mapper
							.convertValue(gc, Map.class), gc.getClass()
							.getCanonicalName()));
		}

		return sp;
	}

	private PlayerState convert(eu.trentorise.game.repo.StatePersistence sp) {
		PlayerState ps = new PlayerState();
		for (eu.trentorise.game.repo.ConceptPersistence cp : sp.getConcepts()) {
			GameConcept gc;
			try {
				gc = mapper.convertValue(cp.getConcept(),
						(Class<? extends GameConcept>) Thread.currentThread()
								.getContextClassLoader()
								.loadClass(cp.getType()));
				ps.getState().add(gc);
			} catch (Exception e) {
				logger.error("Problem to load class {}", cp.getType());
			}
		}
		return ps;
	}

	public List<String> readPlayers(String gameId) {
		List<StatePersistence> states = repo.findByGameId(gameId);
		List<String> result = new ArrayList<String>();
		for (StatePersistence state : states) {
			result.add(state.getPlayerId());
		}

		return result;
	}
}
