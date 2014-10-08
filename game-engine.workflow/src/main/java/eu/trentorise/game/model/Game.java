package eu.trentorise.game.model;

import java.util.Set;

public class Game {
	private String id;
	private Set<String> actions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<String> getActions() {
		return actions;
	}

	public void setActions(Set<String> actions) {
		this.actions = actions;
	}

}
