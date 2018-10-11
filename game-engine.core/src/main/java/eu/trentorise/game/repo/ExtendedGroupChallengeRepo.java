package eu.trentorise.game.repo;

import java.util.List;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge;

public interface ExtendedGroupChallengeRepo {

    List<GroupChallenge> playerGroupChallenges(String gameId, String playerId);

    List<GroupChallenge> playerGroupChallenges(String gameId, String playerId,
            ChallengeState challengeState);

    void deletePlayerGroupChallenges(String gameId, String playerId,
            ChallengeState challengeState);
}
