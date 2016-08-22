package eu.trentorise.smartcampus.gamification_web.models.status;

import java.util.List;

public class ServerChallengeConcept {

	private String name;
	private List<ServerChallengesData> challenges;

	public String getName() {
		return name;
	}

	public List<ServerChallengesData> getChallenges() {
		return challenges;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChallenges(List<ServerChallengesData> challenges) {
		this.challenges = challenges;
	}

	public ServerChallengeConcept() {
		super();
	}

	public ServerChallengeConcept(String name, List<ServerChallengesData> challenges) {
		super();
		this.name = name;
		this.challenges = challenges;
	}

}
