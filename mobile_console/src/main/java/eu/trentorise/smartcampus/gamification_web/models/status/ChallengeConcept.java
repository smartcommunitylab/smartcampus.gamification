package eu.trentorise.smartcampus.gamification_web.models.status;

import java.util.List;

public class ChallengeConcept {

	private List<ChallengesData> activeChallengeData;
	private List<ChallengesData> oldChallengeData;
	
	public ChallengeConcept() {
		super();
	}

	public List<ChallengesData> getActiveChallengeData() {
		return activeChallengeData;
	}

	public List<ChallengesData> getOldChallengeData() {
		return oldChallengeData;
	}

	public void setActiveChallengeData(List<ChallengesData> activeChallengeData) {
		this.activeChallengeData = activeChallengeData;
	}

	public void setOldChallengeData(List<ChallengesData> oldChallengeData) {
		this.oldChallengeData = oldChallengeData;
	}

}
