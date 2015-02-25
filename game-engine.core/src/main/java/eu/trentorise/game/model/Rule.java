package eu.trentorise.game.model;

public abstract class Rule {

	private String gameId;

	public Rule(String gameId) {
		this.gameId = gameId;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
