package eu.trentorise.game.challenges;

import java.util.HashMap;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;

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