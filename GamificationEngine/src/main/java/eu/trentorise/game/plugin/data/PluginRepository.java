package eu.trentorise.game.plugin.data;

import eu.trentorise.game.plugin.model.Plugin;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Luca Piras
 */
public interface PluginRepository extends CrudRepository<Plugin, Integer> {
}