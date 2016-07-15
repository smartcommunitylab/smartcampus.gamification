package eu.trentorise.smartcampus.gamification_web.models.status;

import java.util.List;

public class BadgeCollectionConcept {

	private String name;
	private List<BadgeConcept> badgeEarned;

	public String getName() {
		return name;
	}

	public List<BadgeConcept> getBadgeEarned() {
		return badgeEarned;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setBadgeEarned(List<BadgeConcept> badgeEarned) {
		this.badgeEarned = badgeEarned;
	}
	
	public BadgeCollectionConcept() {
		super();
	}

	public BadgeCollectionConcept(String name, List<BadgeConcept> badgeEarned) {
		super();
		this.name = name;
		this.badgeEarned = badgeEarned;
	}

}
