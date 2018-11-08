package eu.trentorise.game.model;

public class SystemPlayerState {

	private String playerId;
	private boolean available;

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public SystemPlayerState() {
	}

	public SystemPlayerState(String playerId, boolean available) {
		this.playerId = playerId;
		this.available = available;
	}

}
