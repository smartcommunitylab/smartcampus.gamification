package eu.trentorise.smartcampus.gamification_web.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepositoryDao extends CrudRepository<Player, String>{
	
	/*public Player findByPid(String id);
	
	public Player findBySocialId(String id);

	@Query("{'nikName': ?0}")
	public Player findByNick(String nickname);
	
	@Query("{'nikName': { '$regex': ?0, $options:'i'}}")
	public Player findByNickIgnoreCase(String nickname);*/
	
	Iterable<Player> findAllByType(String type);
	
	public Player findByPidAndType(String id, String type);
	
	public Player findBySocialIdAndType(String id, String type);

	@Query("{'nikName': ?0}")
	public Player findByNickAndType(String nickname, String type);
	
	@Query("{'nikName': { '$regex': ?0, $options:'i'}}")
	public Player findByNickIgnoreCaseAndType(String nickname, String type);

}
