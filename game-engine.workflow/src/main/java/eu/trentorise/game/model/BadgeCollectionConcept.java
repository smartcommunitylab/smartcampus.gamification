package eu.trentorise.game.model;

import java.util.ArrayList;
import java.util.List;

public class BadgeCollectionConcept extends GameConcept {

	private List<String> badgeEarned;

	public BadgeCollectionConcept() {
		badgeEarned = new ArrayList<String>();
	}

	public List<String> getBadgeEarned() {
		return badgeEarned;
	}

	public void setBadgeEarned(List<String> badgeEarned) {
		this.badgeEarned = badgeEarned;
	}
}
