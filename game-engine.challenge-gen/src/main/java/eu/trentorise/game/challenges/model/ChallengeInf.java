package eu.trentorise.game.challenges.model;

import java.util.Collection;

public interface ChallengeInf {

	public Challenge makeChallenge(ChallengeType ct);
	public Collection<Challenge> makeChallenges();
	public Collection<Challenge> assignToUser();
	
}
