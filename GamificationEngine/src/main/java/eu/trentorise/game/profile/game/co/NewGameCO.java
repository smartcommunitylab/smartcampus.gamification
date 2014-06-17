package eu.trentorise.game.profile.game.co;

/**
 *
 * @author Luca Piras
 */
public class NewGameCO {
    
    protected String gameName;

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public String toString() {
        return "NewGameCO{" + "gameName=" + gameName + '}';
    }
}