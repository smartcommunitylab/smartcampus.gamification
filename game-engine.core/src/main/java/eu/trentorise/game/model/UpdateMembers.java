package eu.trentorise.game.model;

public class UpdateMembers {
	private String teamId;
	private InputData inputData;
	private String updateTag;

	public UpdateMembers() {
	}

	public UpdateMembers(String teamId, InputData inputData) {
		this.teamId = teamId;
		this.inputData = inputData;
	}

	public UpdateMembers(String teamId, InputData inputData, String updateTag) {
		this.teamId = teamId;
		this.inputData = inputData;
		this.updateTag = updateTag;
	}

	public String getTeamId() {
		return teamId;
	}

	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}

	public InputData getInputData() {
		return inputData;
	}

	public void setInputData(InputData inputData) {
		this.inputData = inputData;
	}

	public String getUpdateTag() {
		return updateTag;
	}

	public void setUpdateTag(String updateTag) {
		this.updateTag = updateTag;
	}

}
