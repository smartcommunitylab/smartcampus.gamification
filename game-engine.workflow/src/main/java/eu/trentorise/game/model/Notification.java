package eu.trentorise.game.model;


public abstract class Notification {

	private String gameId;
	private String playerId;
	private long timestamp;

	public Notification(String gameId, String playerId) {
		this.gameId = gameId;
		this.playerId = playerId;
		timestamp = System.currentTimeMillis();
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

}
