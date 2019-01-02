package it.smartcommunitylab.model.ext;

import it.smartcommunitylab.model.Notification;

public class LevelGainedNotification extends Notification {
    private String levelName;
    private String levelType;
    private int levelIndex;


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


    public int getLevelIndex() {
        return levelIndex;
    }


    public void setLevelIndex(int levelIndex) {
        this.levelIndex = levelIndex;
    }

}
