package eu.trentorise.game.plugin.response;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import eu.trentorise.game.plugin.model.GameCustomizedPlugin;
import java.util.Collection;

/**
 *
 * @author Luca Piras
 */
//Necessary for the getCustomizedPluginListService Test
@JsonIgnoreProperties(ignoreUnknown = true)
public class GameCustomizedPluginCollectionResponse {
    
    protected Collection<GameCustomizedPlugin> gameCustomizedPlugins;

    public Collection<GameCustomizedPlugin> getGameCustomizedPlugins() {
        return gameCustomizedPlugins;
    }

    public void setGameCustomizedPlugins(Collection<GameCustomizedPlugin> gameCustomizedPlugins) {
        this.gameCustomizedPlugins = gameCustomizedPlugins;
    }

    @Override
    public String toString() {
        return "GameCustomizedPluginCollectionResponse{" + "gameCustomizedPlugins=" + gameCustomizedPlugins + '}';
    }
}