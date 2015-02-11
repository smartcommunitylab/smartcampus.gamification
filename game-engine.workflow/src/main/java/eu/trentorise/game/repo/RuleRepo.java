package eu.trentorise.game.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import eu.trentorise.game.model.DBRule;

@Repository
public interface RuleRepo extends CrudRepository<DBRule, String> {

}
