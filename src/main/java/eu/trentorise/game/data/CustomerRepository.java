package eu.trentorise.game.data;

import eu.trentorise.game.model.player.Customer;
import java.util.List;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Luca Piras
 */
//TODO: convert for Player
public interface CustomerRepository extends CrudRepository<Customer, Long> {

    List<Customer> findByLastName(String lastName);
}