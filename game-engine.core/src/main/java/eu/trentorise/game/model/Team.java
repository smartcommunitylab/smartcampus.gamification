package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;

import eu.trentorise.game.repo.StatePersistence;

public class Team extends PlayerState {

	public static final String NAME_METADATA = "team-name";

	public static final String MEMBERS_METADATA = "team-members";

	public Team(String gameId, String playerId) {
		super(gameId, playerId);
	}

	public Team(StatePersistence state) {
		super(state);
		if (state != null) {
			name = (String) state.getMetadata().get(NAME_METADATA);
			members = (List<String>) state.getMetadata().get(MEMBERS_METADATA);
		}
	}

	private String name;

	private List<String> members = new ArrayList<>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getMembers() {
		return members;
	}

	public void setMembers(List<String> members) {
		this.members = members;
	}

}
