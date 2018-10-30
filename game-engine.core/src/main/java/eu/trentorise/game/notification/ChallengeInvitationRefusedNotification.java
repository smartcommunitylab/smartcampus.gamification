package eu.trentorise.game.notification;

import eu.trentorise.game.model.core.Notification;

public class ChallengeInvitationRefusedNotification extends Notification {
    private String challengeName;

    @Override
    public String toString() {
        return String.format("[gameId=%s, playerId=%s, challengeName=%s]", getGameId(),
                getPlayerId(), challengeName);
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

}
