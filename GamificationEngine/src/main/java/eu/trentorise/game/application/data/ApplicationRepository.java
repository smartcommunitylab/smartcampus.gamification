package eu.trentorise.game.application.data;

import eu.trentorise.game.action.model.Application;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Luca Piras
 */
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
}