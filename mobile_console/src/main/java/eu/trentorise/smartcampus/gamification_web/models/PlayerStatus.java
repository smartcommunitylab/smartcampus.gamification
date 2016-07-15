package eu.trentorise.smartcampus.gamification_web.models;

import java.util.List;

import eu.trentorise.smartcampus.gamification_web.models.status.BadgeCollectionConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.ChallengeConcept;
import eu.trentorise.smartcampus.gamification_web.models.status.PlayerData;
import eu.trentorise.smartcampus.gamification_web.models.status.PointConcept;

public class PlayerStatus {

	private PlayerData playerData;
	private List<PointConcept> pointConcept;
	private List<BadgeCollectionConcept> badgeCollectionConcept;
	private ChallengeConcept challengeConcept;
	
	public PlayerStatus() {
		super();
	}

	public List<BadgeCollectionConcept> getBadgeCollectionConcept() {
		return badgeCollectionConcept;
	}

	public void setBadgeCollectionConcept(List<BadgeCollectionConcept> badgeCollectionConcept) {
		this.badgeCollectionConcept = badgeCollectionConcept;
	}

	public PlayerData getPlayerData() {
		return playerData;
	}

	public ChallengeConcept getChallengeConcept() {
		return challengeConcept;
	}

	public void setPlayerData(PlayerData playerData) {
		this.playerData = playerData;
	}

	public void setChallengeConcept(ChallengeConcept challengeConcept) {
		this.challengeConcept = challengeConcept;
	}

	public List<PointConcept> getPointConcept() {
		return pointConcept;
	}

	public void setPointConcept(List<PointConcept> pointConcept) {
		this.pointConcept = pointConcept;
	}
	
}
