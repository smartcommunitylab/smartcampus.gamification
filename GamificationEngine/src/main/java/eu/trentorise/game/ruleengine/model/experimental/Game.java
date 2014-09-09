package eu.trentorise.game.ruleengine.model.experimental;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author Luca Piras
 */
public class Game {
    
    protected Integer id;
    protected Collection<Player> players;
    protected Collection<ExternalAction> externalActions;
    //Since rules are defined in Drools, we assume there is no 	//data model for rules
    //protected Collection<??> rules;
    protected Collection<Plugin> plugins;
    //Integer pluginId
    protected Map<Integer, PluginState> gameState;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Collection<Player> players) {
        this.players = players;
    }

    public Collection<ExternalAction> getExternalActions() {
        return externalActions;
    }

    public void setExternalActions(Collection<ExternalAction> externalActions) {
        this.externalActions = externalActions;
    }

    public Collection<Plugin> getPlugins() {
        return plugins;
    }

    public void setPlugins(Collection<Plugin> plugins) {
        this.plugins = plugins;
    }

    public Map<Integer, PluginState> getGameState() {
        return gameState;
    }

    public void setGameState(Map<Integer, PluginState> gameState) {
        this.gameState = gameState;
    }

    @Override
    public String toString() {
        return "Game{" + "id=" + id + ", players=" + players + ", externalActions=" + externalActions + ", plugins=" + plugins + ", gameState=" + gameState + '}';
    }
}