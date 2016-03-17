package eu.trentorise.game.repo;

import eu.trentorise.game.model.TeamState;

public class TeamPersistence extends StatePersistence {

	public TeamPersistence(String gameId, String playerId) {
		super(gameId, playerId);
	}

	public TeamPersistence(TeamState t) {
		super(t);

		// set specific team data
		metadata.put(TeamState.NAME_METADATA, t.getName());
		metadata.put(TeamState.MEMBERS_METADATA, t.getMembers());
	}
}
