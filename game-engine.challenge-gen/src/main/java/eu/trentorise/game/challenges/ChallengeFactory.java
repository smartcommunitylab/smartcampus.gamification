package eu.trentorise.game.challenges;

import eu.trentorise.game.challenges.api.ChallengeFactoryInterface;
import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;

public class ChallengeFactory implements ChallengeFactoryInterface {

    /* (non-Javadoc)
     * @see eu.trentorise.game.challenges.util.ChallengeFactoryInterface#createChallenge(eu.trentorise.game.challenges.model.ChallengeType)
     */
    public Challenge createChallenge(ChallengeType chType)
	    throws UndefinedChallengeException {
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
	    throw new UndefinedChallengeException("Unknown challenge type!"
		    + chType.toString());

	}
    }

}