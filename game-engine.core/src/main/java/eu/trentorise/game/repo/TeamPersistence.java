package eu.trentorise.game.repo;

import eu.trentorise.game.model.Team;

public class TeamPersistence extends StatePersistence {

	public TeamPersistence(Team t) {
		super(t);

		// set specific team data
		metadata.put("name", t.getName());
		metadata.put("members", t.getMembers());
	}
}
