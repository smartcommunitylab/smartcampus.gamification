package eu.trentorise.smartcampus.gamification_web.models.status;

public class PlayerData {

	private String playerId;
	private String gameId;
	private String nickName;
	
	public String getPlayerId() {
		return playerId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public PlayerData() {
		super();
	}

	public PlayerData(String playerId, String gameId, String nickName) {
		super();
		this.playerId = playerId;
		this.gameId = gameId;
		this.nickName = nickName;
	}
	
}
