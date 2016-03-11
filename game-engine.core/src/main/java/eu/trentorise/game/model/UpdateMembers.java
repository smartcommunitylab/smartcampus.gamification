package eu.trentorise.game.model;

public class UpdateMembers {
	private String propagationAction;

	public UpdateMembers() {
	}

	public UpdateMembers(String propagationAction) {
		this.propagationAction = propagationAction;
	}

	public String getPropagationAction() {
		return propagationAction;
	}

	public void setPropagationAction(String propagationAction) {
		this.propagationAction = propagationAction;
	}

}
