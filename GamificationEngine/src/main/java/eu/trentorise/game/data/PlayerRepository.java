package eu.trentorise.game.data;

import eu.trentorise.game.model.player.Player;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Luca Piras
 */
public interface PlayerRepository extends CrudRepository<Player, String> {

    List<Player> findByPoints(Integer points);
}