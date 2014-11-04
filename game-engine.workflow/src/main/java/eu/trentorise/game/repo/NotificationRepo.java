package eu.trentorise.game.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepo extends
		CrudRepository<NotificationPersistence, String> {

}
