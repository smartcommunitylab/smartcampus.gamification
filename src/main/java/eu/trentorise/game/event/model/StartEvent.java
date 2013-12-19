package eu.trentorise.game.event.model;

import eu.trentorise.game.model.player.Player;
import eu.trentorise.game.rule.model.Action;

/**
 *
 * @author Luca Piras
 */
public class StartEvent implements IStartEvent {
    
    protected Player player;
    
    protected Action action;

    
    @Override
    public Player getPlayer() {
        return player;
    }
    
    @Override
    public void setPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public void setAction(Action action) {
        //TODO: improve it with a list of Action, in the sense that a Player
        //can do a list of Action as StartEvent
        this.action = action;
    }

    
    @Override
    public String toString() {
        return "StartEvent{" + "player=" + player + ", action=" + action + '}';
    }
}