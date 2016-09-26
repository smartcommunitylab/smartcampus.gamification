package eu.trentorise.game.repo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.game.model.core.PlayerMoveLog;

@Repository
public interface PlayerMoveLogRepo extends
		MongoRepository<PlayerMoveLog, String> {

}
