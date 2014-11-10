package eu.trentorise.smartcampus.gamification_web.models;

public class Notification {
	
	private String gameId = "";
	private String playerId = "";
	private long timestamp = 0L;
	private String badge = "";
	
	public Notification(){
		super();
	};
	
	public Notification(String gameId, String playerId, long timestamp, String badge) {
		super();
		this.gameId = gameId;
		this.playerId = playerId;
		this.timestamp = timestamp;
		this.badge = badge;
	}

	public String getGameId() {
		return gameId;
	}

	public String getPlayerId() {
		return playerId;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getBadge() {
		return badge;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	@Override
	public String toString() {
		return "Notification [gameId=" + gameId + ", playerId=" + playerId
				+ ", timestamp=" + timestamp + ", badge=" + badge + "]";
	}
	

}
