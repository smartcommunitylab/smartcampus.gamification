package eu.trentorise.game.repo;

import java.util.Set;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.game.model.ChallengeModel;

@Repository
public interface ChallengeModelRepo extends
		MongoRepository<ChallengeModel, String> {

	public Set<ChallengeModel> findByGameId(String gameId);

	public String deleteByGameIdAndId(String gameId, String modelId);

	public ChallengeModel findByGameIdAndId(String gameId, String modelId);

	public ChallengeModel findByGameIdAndName(String gameId, String modelName);

}
