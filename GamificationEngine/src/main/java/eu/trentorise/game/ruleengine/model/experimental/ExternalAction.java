package eu.trentorise.game.ruleengine.model.experimental;

import java.util.Map;

/**
 *
 * @author Luca Piras
 */
public class ExternalAction {
    
    protected String name;
    protected Player player;
    protected Map<String, Object> params;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return "ExternalAction{" + "name=" + name + ", player=" + player + ", params=" + params + '}';
    }
}