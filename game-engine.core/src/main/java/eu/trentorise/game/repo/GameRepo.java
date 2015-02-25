package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepo extends CrudRepository<GamePersistence, String> {

	public GamePersistence findByActions(String action);

	public List<GamePersistence> findByTerminated(boolean value);

}
