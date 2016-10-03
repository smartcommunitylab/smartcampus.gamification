package eu.trentorise.game.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;

public class ChallengeModel {

	@Id
	private String id;

	private String name;
	private Set<String> variables = new HashSet<>();

	private String gameId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<String> getVariables() {
		return variables;
	}

	public void setVariables(Set<String> variables) {
		this.variables = variables;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGameId() {
		return gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

}
