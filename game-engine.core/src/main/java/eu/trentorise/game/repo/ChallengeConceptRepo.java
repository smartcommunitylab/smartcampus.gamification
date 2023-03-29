package eu.trentorise.game.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeConceptRepo extends CrudRepository<ChallengeConceptPersistence, String> {

	public List<ChallengeConceptPersistence> findByGameIdAndPlayerId(String gameId, String playerId);
	public ChallengeConceptPersistence findByGameIdAndPlayerIdAndName(String gameId, String playerId, String name);
	
	@Query(value = "{'gameId': ?0, 'concept.end': { $gte: ?1}, 'concept.state': ?2}", fields = "{'playerId': 1}")                 
	public List<ChallengeConceptPersistence> getProposedChallengePlayerIds(String gameId, Date from, String state); 

}
