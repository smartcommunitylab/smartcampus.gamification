package eu.trentorise.game.repo;

import java.util.Date;
import java.util.List;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge;

public interface ExtendedGroupChallengeRepo {

    List<GroupChallenge> playerGroupChallenges(String gameId, String playerId);

    List<GroupChallenge> playerGroupChallenges(String gameId, String playerId,
            ChallengeState challengeState);

    List<GroupChallenge> activeGroupChallenges(String gameId, String playerId, Date atDate);

    void deletePlayerGroupChallenges(String gameId, String playerId,
            ChallengeState challengeState);

    List<GroupChallenge> proposerInvitations(String gameId, String playerId);

    List<GroupChallenge> guestInvitations(String gameId, String playerId);

    GroupChallenge deleteProposedChallengeByGuest(String gameId, String playerId,
            String instanceName);

    GroupChallenge deleteProposedChallengeByProposer(String gameId, String playerId,
            String instanceName);
}
