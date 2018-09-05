package eu.trentorise.game.notification;

import eu.trentorise.game.model.core.Notification;

public class LevelGainedNotification extends Notification {
    private String levelName;
    private String levelType;


    @Override
    public String toString() {
        return String.format("[gameId=%s, playerId=%s, levelName=%s, levelType=%s]", getGameId(),
                getPlayerId(), getLevelName(), getLevelType());
    }


    public String getLevelName() {
        return levelName;
    }


    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }


    public String getLevelType() {
        return levelType;
    }


    public void setLevelType(String levelType) {
        this.levelType = levelType;
    }

}
