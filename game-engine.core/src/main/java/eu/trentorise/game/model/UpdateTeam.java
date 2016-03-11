package eu.trentorise.game.model;

public class UpdateTeam {
	private String updateTag;

	public UpdateTeam() {
	}

	public UpdateTeam(String updateTag) {
		this.updateTag = updateTag;
	}

	public String getUpdateTag() {
		return updateTag;
	}

	public void setUpdateTag(String updateTag) {
		this.updateTag = updateTag;
	}

}
