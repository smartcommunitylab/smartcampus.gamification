package eu.trentorise.game.notification;

import eu.trentorise.game.model.core.Notification;

public class ChallengeInvitationAcceptedNotification extends Notification {
    private String challengeName;
    private String guestId;

    @Override
    public String toString() {
        return String.format("[gameId=%s, playerId=%s, guestId=%s, challengeName=%s]", getGameId(),
                getPlayerId(), guestId, challengeName);
    }

    public String getChallengeName() {
        return challengeName;
    }

    public void setChallengeName(String challengeName) {
        this.challengeName = challengeName;
    }

    public String getGuestId() {
        return guestId;
    }

    public void setGuestId(String guestId) {
        this.guestId = guestId;
    }

}
