package eu.trentorise.game.bean;

import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import eu.trentorise.game.model.GameConcept;

@JsonInclude(Include.NON_NULL)
public class PlayerStateDTO {
	private String playerId;
	private String gameId;

	private Map<String, Set<GameConcept>> state;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public Map<String, Set<GameConcept>> getState() {
		return state;
	}

	public void setState(Map<String, Set<GameConcept>> state) {
		this.state = state;
	}

}
