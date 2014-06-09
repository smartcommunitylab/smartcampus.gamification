package eu.trentorise.game.service;

import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.response.SuccessResponse;

/**
 *
 * @author Luca Piras
 */
public interface IPlayerManager {
    
    public SuccessResponse add(Player player);
}