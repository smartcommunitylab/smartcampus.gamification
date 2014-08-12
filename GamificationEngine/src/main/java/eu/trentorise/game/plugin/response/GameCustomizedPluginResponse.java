package eu.trentorise.game.plugin.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameCustomizedPluginResponse {
    
    protected GameCustomizedPlugin gameCustomizedPlugin;

    public GameCustomizedPlugin getGameCustomizedPlugin() {
        return gameCustomizedPlugin;
    }

    public void setGameCustomizedPlugin(GameCustomizedPlugin gameCustomizedPlugin) {
        this.gameCustomizedPlugin = gameCustomizedPlugin;
    }

    @Override
    public String toString() {
        return "GameCustomizedPluginResponse{" + "gameCustomizedPlugin=" + gameCustomizedPlugin + '}';
    }
}