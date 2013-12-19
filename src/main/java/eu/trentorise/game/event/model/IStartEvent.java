package eu.trentorise.game.event.model;

import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.rule.model.Action;

/**
 *
 * @author Luca Piras
 */
public interface IStartEvent {
    
    public Player getPlayer();
    public void setPlayer(Player player);

    public Action getAction();
    public void setAction(Action action);
}