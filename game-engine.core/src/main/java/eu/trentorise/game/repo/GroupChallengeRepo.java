package eu.trentorise.game.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge;

@Repository
public interface GroupChallengeRepo
        extends MongoRepository<GroupChallenge, String>, ExtendedGroupChallengeRepo {

    List<GroupChallenge> findByGameIdAndStateAndEndBefore(String gameId, ChallengeState state,
            Date end);
}
