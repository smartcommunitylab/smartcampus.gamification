package eu.trentorise.game.model;


public class UpdateTeam {
	private String playerId;
	private InputData inputData;

	public UpdateTeam() {
	}

	public UpdateTeam(String playerId, InputData inputData) {
		this.playerId = playerId;
		this.inputData = inputData;
	}

	public String getPlayerId() {
		return playerId;
	}

	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}

	public InputData getInputData() {
		return inputData;
	}

	public void setInputData(InputData inputData) {
		this.inputData = inputData;
	}

}
