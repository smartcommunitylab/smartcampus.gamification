package eu.trentorise.game.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import eu.trentorise.game.model.ChallengeConcept.ChallengeState;
import eu.trentorise.game.model.GroupChallenge;

@Repository
public interface GroupChallengeRepo
        extends MongoRepository<GroupChallenge, String>, ExtendedGroupChallengeRepo {

    List<GroupChallenge> findByGameIdAndStateAndEndBeforeAndChallengeModel(String gameId,
            ChallengeState state, Date end, String type);
    
    @Query(value = "{'gameId': ?0, 'end': { $gte: ?1}, 'state': ?2}", fields = "{'attendees': 1}")                 
	public List<GroupChallenge> getProposedChallengePlayerIds(String gameId, Date from, String state); 

}
