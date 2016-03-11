package eu.trentorise.game.model;

public class UpdateTeam {
	private String propagationAction;

	public UpdateTeam() {
	}

	public UpdateTeam(String propagationAction) {
		this.propagationAction = propagationAction;
	}

	public String getPropagationAction() {
		return propagationAction;
	}

	public void setPropagationAction(String propagationAction) {
		this.propagationAction = propagationAction;
	}

}
