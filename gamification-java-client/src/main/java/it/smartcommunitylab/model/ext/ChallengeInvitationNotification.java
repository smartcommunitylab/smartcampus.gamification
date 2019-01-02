package it.smartcommunitylab.model.ext;

import it.smartcommunitylab.model.Notification;

public class ChallengeInvitationNotification extends Notification {
    private String proposerId;

    @Override
    public String toString() {
        return String.format("[gameId=%s, playerId=%s, proposerId=%s]", getGameId(), getPlayerId(),
                proposerId);
    }

    public String getProposerId() {
        return proposerId;
    }

    public void setProposerId(String proposerId) {
        this.proposerId = proposerId;
    }

}
