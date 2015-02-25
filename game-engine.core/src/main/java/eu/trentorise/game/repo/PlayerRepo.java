package eu.trentorise.game.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepo extends CrudRepository<StatePersistence, String> {

	public List<StatePersistence> findByGameId(String id);

	public List<StatePersistence> findByPlayerId(String id);

	public StatePersistence findByGameIdAndPlayerId(String game, String player);
}
