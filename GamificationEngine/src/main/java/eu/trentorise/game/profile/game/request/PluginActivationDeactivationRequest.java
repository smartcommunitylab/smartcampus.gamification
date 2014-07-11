package eu.trentorise.game.profile.game.request;

/**
 *
 * @author Luca Piras
 */
public class PluginActivationDeactivationRequest {
    
    protected Integer gameProfileId;
    protected Integer gamificationPluginId;
    protected boolean active;

    public Integer getGameProfileId() {
        return gameProfileId;
    }

    public void setGameProfileId(Integer gameProfileId) {
        this.gameProfileId = gameProfileId;
    }

    public Integer getGamificationPluginId() {
        return gamificationPluginId;
    }

    public void setGamificationPluginId(Integer gamificationPluginId) {
        this.gamificationPluginId = gamificationPluginId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    @Override
    public String toString() {
        return "PluginActivationDeactivationCO{" + "gameProfileId=" + gameProfileId + ", gamificationPluginId=" + gamificationPluginId + ", active=" + active + '}';
    }
}