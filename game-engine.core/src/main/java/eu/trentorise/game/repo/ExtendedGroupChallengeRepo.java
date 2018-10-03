package eu.trentorise.game.repo;

import java.util.List;

import eu.trentorise.game.model.GroupChallenge;

public interface ExtendedGroupChallengeRepo {

    public List<GroupChallenge> completedPerformanceGroupChallenges(String gameId);
}
