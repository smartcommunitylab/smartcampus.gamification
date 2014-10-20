package eu.trentorise.game.model;

import java.util.HashSet;
import java.util.Set;

public class PlayerState {

	private Set<GameConcept> state;

	public PlayerState() {
		state = new HashSet<GameConcept>();
	}

	public Set<GameConcept> getState() {
		return state;
	}

	public void setState(Set<GameConcept> state) {
		this.state = state;
	}

}