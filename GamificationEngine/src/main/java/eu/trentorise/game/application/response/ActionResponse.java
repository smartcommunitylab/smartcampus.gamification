package eu.trentorise.game.application.response;

import eu.trentorise.game.application.model.Action;
import eu.trentorise.game.response.GameResponse;
import java.util.List;

/**
 *
 * @author Luca Piras
 */
public class ActionResponse extends GameResponse {
    
    protected List<Action> actions;

    public List<Action> getActions() {
        return actions;
    }

    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        return "ActionResponse{" + "actions=" + actions + '}';
    }
}