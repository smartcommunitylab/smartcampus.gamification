package eu.trentorise.game.repo;

import java.util.List;

import eu.trentorise.game.model.GroupChallenge;

public interface ExtendedGroupChallengeRepo {

    List<GroupChallenge> playerGroupChallenges(String gameId, String playerId);
}
