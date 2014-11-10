package eu.trentorise.game.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.PlayerService;

@Component("simplePlayerManager")
public class PlayerManager implements PlayerService {

	private final Logger logger = LoggerFactory.getLogger(PlayerManager.class);

	private ObjectMapper mapper = new ObjectMapper();

	private Map<String, StatePersistence> data;

	@PostConstruct
	@SuppressWarnings("unused")
	private void loadData() {

		try {
			File store = new File("playerstorage");
			if (store.exists()) {
				data = mapper.readValue(new File("playerstorage"), Map.class);
			} else {
				throw new IOException("playerstorage not exists");
			}

		} catch (Exception e) {
			logger.error("Error loading playerstorage: " + e.getMessage());
			data = new HashMap<String, StatePersistence>();
		}
	}

	public PlayerState loadState(String userId, String gameId) {
		String key = userId + "-" + gameId;
		return data.get(key) != null ? convert(mapper.convertValue(
				data.get(key), StatePersistence.class)) : new PlayerState();
	}

	public boolean saveState(PlayerState state) {

		if (StringUtils.isBlank(state.getGameId())
				|| StringUtils.isBlank(state.getPlayerId())) {
			throw new IllegalArgumentException(
					"field gameId and playerId of PlayerState MUST be set");
		}

		String key = state.getPlayerId() + "-" + state.getGameId();
		data.put(key, convert(state));

		try {
			mapper.writeValue(new FileOutputStream("playerstorage"), data);
			return true;
		} catch (Exception e) {
			logger.error("Error persisting playerstorage {}: {}", e.getClass()
					.getName(), e.getMessage());
			return false;
		}
	}

	private StatePersistence convert(PlayerState ps) {
		StatePersistence sp = new StatePersistence();
		for (GameConcept gc : ps.getState()) {
			sp.getConcepts().add(
					new ConceptPersistence(mapper.convertValue(gc, Map.class),
							gc.getClass().getCanonicalName()));
		}

		return sp;
	}

	private PlayerState convert(StatePersistence sp) {
		PlayerState ps = new PlayerState();
		for (ConceptPersistence cp : sp.getConcepts()) {
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
		logger.warn("method not implemented");
		throw new UnsupportedOperationException("method not implemented");
	}

	public List<PlayerState> loadStates(String gameId) {
		logger.warn("method not implemented");
		throw new UnsupportedOperationException("method not implemented");
	}
}

class ConceptPersistence {

	private Map<String, Object> concept;
	private String type;

	public ConceptPersistence(Map<String, Object> concept, String type) {
		this.concept = concept;
		this.type = type;
	}

	public ConceptPersistence() {
	}

	public Map<String, Object> getConcept() {
		return concept;
	}

	public void setConcept(Map<String, Object> concept) {
		this.concept = concept;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}

class StatePersistence {
	private List<ConceptPersistence> concepts = new ArrayList<ConceptPersistence>();

	public List<ConceptPersistence> getConcepts() {
		return concepts;
	}

	public void setConcepts(List<ConceptPersistence> concepts) {
		this.concepts = concepts;
	}

}