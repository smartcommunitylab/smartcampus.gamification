package eu.trentorise.game.model;

import java.util.HashSet;
import java.util.Set;

public class PlayerState {

	private String playerId;
	private String gameId;

	private Set<GameConcept> state;

	public PlayerState() {
		state = new HashSet<GameConcept>();
	}

	public Set<GameConcept> getState() {
		return state;
	}

	public void setState(Set<GameConcept> state) {
		this.state = state;
	}

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

}