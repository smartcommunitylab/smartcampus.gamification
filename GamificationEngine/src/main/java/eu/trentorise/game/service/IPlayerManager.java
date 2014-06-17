package eu.trentorise.game.service;

import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.response.GameResponse;

/**
 *
 * @author Luca Piras
 */
public interface IPlayerManager {
    
    public GameResponse add(Player player);
}