package eu.trentorise.game.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class PlayerState {
	@JsonDeserialize(as = HashSet.class, contentAs = PointConcept.class)
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