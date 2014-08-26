package eu.trentorise.game.profile.game.data;

import eu.trentorise.game.profile.game.model.Game;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Luca Piras
 */
public interface GameRepository extends CrudRepository<Game, Integer> {
}