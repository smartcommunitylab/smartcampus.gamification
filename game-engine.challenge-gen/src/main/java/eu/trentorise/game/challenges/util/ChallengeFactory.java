package eu.trentorise.game.challenges.util;

import java.util.HashMap;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

import eu.trentorise.game.challenges.BadgeCollectionCompletionChallenge;
import eu.trentorise.game.challenges.PercentMobilityChallenge;
import eu.trentorise.game.challenges.RecommendationChallenge;
import eu.trentorise.game.challenges.TripNumberChallenge;
import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;

public class ChallengeFactory {

	public Challenge createChallenge(ChallengeType chType) throws UndefinedChallengeException {
		switch (chType) {
			case PERCENT:
				return new PercentMobilityChallenge();	
			case TRIPNUMBER:
				return new TripNumberChallenge();	
			case BADGECOLLECTION:
				return new BadgeCollectionCompletionChallenge();
			case RECOMMENDATION:
				return new RecommendationChallenge();
			default: 
				throw new UndefinedChallengeException("Unknown challenge type!" + chType.toString()); 
			
		}
	}
	
}