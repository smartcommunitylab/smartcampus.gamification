package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeConceptRepo extends CrudRepository<ChallengeConceptPersistence, String> {

	public List<ChallengeConceptPersistence> findByGameIdAndPlayerId(String gameId, String playerId);
	public ChallengeConceptPersistence findByGameIdAndPlayerIdAndName(String gameId, String playerId, String name);

}
