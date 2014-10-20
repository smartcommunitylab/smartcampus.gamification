package eu.trentorise.game.managers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.GameConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.services.PlayerService;

@Component
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

	public boolean saveState(String userId, String gameId, PlayerState state) {
		String key = userId + "-" + gameId;
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
			if (gc instanceof PointConcept) {
				sp.getPoints().add((PointConcept) gc);
			}

			if (gc instanceof BadgeCollectionConcept) {
				sp.getBadges().add((BadgeCollectionConcept) gc);
			}
		}

		return sp;
	}

	private PlayerState convert(StatePersistence sp) {
		PlayerState ps = new PlayerState();
		ps.getState().addAll(sp.getPoints());
		ps.getState().addAll(sp.getBadges());

		return ps;
	}

}

class StatePersistence {
	private Set<PointConcept> points = new HashSet<PointConcept>();
	private Set<BadgeCollectionConcept> badges = new HashSet<BadgeCollectionConcept>();

	public StatePersistence() {
		points = new HashSet<PointConcept>();
		badges = new HashSet<BadgeCollectionConcept>();
	}

	public Set<PointConcept> getPoints() {
		return points;
	}

	public void setPoints(Set<PointConcept> points) {
		this.points = points;
	}

	public Set<BadgeCollectionConcept> getBadges() {
		return badges;
	}

	public void setBadges(Set<BadgeCollectionConcept> badges) {
		this.badges = badges;
	}

}