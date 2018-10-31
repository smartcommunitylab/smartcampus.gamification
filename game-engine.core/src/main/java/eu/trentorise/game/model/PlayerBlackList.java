package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "playerBlackList")
public class PlayerBlackList {

	@Id
	private String id;

	private String playerId;
	private String gameId;

	private List<String> blockedPlayers = new ArrayList<String>();

	public PlayerBlackList() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public List<String> getBlockedPlayers() {
		return blockedPlayers;
	}

	public void setBlockedPlayers(List<String> blockedPlayers) {
		this.blockedPlayers = blockedPlayers;
	}

}
