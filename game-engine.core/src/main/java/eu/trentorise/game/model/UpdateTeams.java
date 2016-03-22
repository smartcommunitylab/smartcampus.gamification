package eu.trentorise.game.model;

public class UpdateTeams {
	private String propagationAction;

	public UpdateTeams() {
	}

	public UpdateTeams(String propagationAction) {
		this.propagationAction = propagationAction;
	}

	public String getPropagationAction() {
		return propagationAction;
	}

	public void setPropagationAction(String propagationAction) {
		this.propagationAction = propagationAction;
	}

}
