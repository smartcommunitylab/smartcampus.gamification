package eu.trentorise.game.challenges.api;

import eu.trentorise.game.challenges.exception.UndefinedChallengeException;
import eu.trentorise.game.challenges.model.Challenge;
import eu.trentorise.game.challenges.model.ChallengeType;

/**
 * A challenge factory create factory for given {@link ChallengeType}
 */
public interface ChallengeFactoryInterface {

    public Challenge createChallenge(ChallengeType chType)
	    throws UndefinedChallengeException;

}