package it.smartcommunitylab.model.ext;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.smartcommunitylab.model.Inventory;
import it.smartcommunitylab.model.PlayerStateDTO;

public class PlayerControllerUtils {

    ObjectMapper mapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

	public PlayerStateDTO convertPlayerState(Map ps) {
		PlayerStateDTO res = null;
		if (ps != null) {
			res = new PlayerStateDTO();
			res.setGameId(ps.get("gameId").toString());
			res.setPlayerId(ps.get("playerId").toString());
			if (ps.get("customData") != null) {
				res.setCustomData(mapper.convertValue(ps.get("customData"), CustomData.class));
			}
			if (ps.get("levels") != null) {
				res.setLevels(mapper.convertValue(ps.get("levels"), new TypeReference<List<PlayerLevel>>() {
				}));
			}
			if (ps.get("inventory") != null) {
				res.setInventory(mapper.convertValue(ps.get("inventory"), Inventory.class));
			}
			// FIXME state is never null in PlayerState by design
			if (ps.get("state") != null) {
				res.setState(new HashMap<String, Set<GameConcept>>());
				Map<String, Object> concepts = (Map<String, Object>) ps.get("state");
				for (String key : concepts.keySet()) {
					if (key.equalsIgnoreCase("BadgeCollectionConcept")) {
						res.getState().put(key, mapper.convertValue(concepts.get(key),
								new TypeReference<HashSet<BadgeCollectionConcept>>() {
								}));
					} else if (key.equalsIgnoreCase("BadgeConcept")) {
						res.getState().put(key,
								mapper.convertValue(concepts.get(key), new TypeReference<HashSet<BadgeConcept>>() {
								}));
					} else if (key.equalsIgnoreCase("ChallengeConcept")) {
						res.getState().put(key,
								mapper.convertValue(concepts.get(key), new TypeReference<HashSet<ChallengeConcept>>() {
								}));
					} else if (key.equalsIgnoreCase("PointConcept")) {
						res.getState().put(key,
								mapper.convertValue(concepts.get(key), new TypeReference<HashSet<PointConcept>>() {
								}));
					}
				}
			}
		}

		return res;

	}

}
