package eu.trentorise.game.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.services.PlayerService;

@Component
public class PlayerManager implements PlayerService {

	private final Logger logger = LoggerFactory.getLogger(PlayerManager.class);

	private ObjectMapper mapper = new ObjectMapper();

	private Map<String, PlayerState> data;

	@PostConstruct
	@SuppressWarnings("unused")
	private void loadData() {

		try {
			File store = new File("playerstorage");
			if (store.exists()) {
				data = mapper.readValue(new File("playerstorage"), Map.class);
				logger.info("" + data.size());
			} else {
				throw new IOException("playerstorage not exists");
			}

		} catch (Exception e) {
			logger.error("Error loading playerstorage: " + e.getMessage());
			data = new HashMap<String, PlayerState>();
		}
	}

	public PlayerState loadState(String userId, String gameId) {
		String key = userId + "-" + gameId;
		return data.get(key) != null ? mapper.convertValue(data.get(key),
				PlayerState.class) : new PlayerState();
	}

	public boolean saveState(String userId, String gameId, PlayerState state) {
		String key = userId + "-" + gameId;
		data.put(key, state);

		try {
			mapper.writeValue(new FileOutputStream("playerstorage"), data);
			return true;
		} catch (Exception e) {
			logger.error("Error persisting playerstorage {}: {}", e.getClass()
					.getName(), e.getMessage());
			return false;
		}
	}

	class StatePersistence {
		private String userId;
		private String gameId;

		private PlayerState state;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public String getGameId() {
			return gameId;
		}

		public void setGameId(String gameId) {
			this.gameId = gameId;
		}

		public PlayerState getState() {
			return state;
		}

		public void setState(PlayerState state) {
			this.state = state;
		}

	}
}
