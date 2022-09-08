package eu.trentorise.game.model;

public class ChallengeReportJSONStub {
	private String challengeType;
	private ChallengeConcept singleChallenge;
	private GroupChallenge groupChallenge;

	public String getChallengeType() {
		return challengeType;
	}

	public void setChallengeType(String challengeType) {
		this.challengeType = challengeType;
	}

	public ChallengeConcept getSingleChallenge() {
		return singleChallenge;
	}

	public void setSingleChallenge(ChallengeConcept singleChallenge) {
		this.singleChallenge = singleChallenge;
	}

	public GroupChallenge getGroupChallenge() {
		return groupChallenge;
	}

	public void setGroupChallenge(GroupChallenge groupChallenge) {
		this.groupChallenge = groupChallenge;
	}

}
