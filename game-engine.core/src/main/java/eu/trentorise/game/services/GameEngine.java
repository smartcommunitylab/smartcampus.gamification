package eu.trentorise.game.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import eu.trentorise.game.model.PlayerState;

@Service
public interface GameEngine {

	public PlayerState execute(String gameId, PlayerState state,
			String action, Map<String, Object> data);
}
