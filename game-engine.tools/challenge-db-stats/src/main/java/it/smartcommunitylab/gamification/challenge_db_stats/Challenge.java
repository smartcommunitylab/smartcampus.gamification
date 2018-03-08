package it.smartcommunitylab.gamification.challenge_db_stats;

import java.util.Date;
import java.util.UUID;

public class Challenge {
    private String gameId;
    private String playerId;
    private String challengeName;
    private Date start;
    private Date end;
    private Date dateCompleted;


    public Challenge(String gameId, String playerId, String challengeName, Date start, Date end,
            Date dateCompleted) {
        if (invalidString(gameId)) {
            throw new IllegalArgumentException("gameId cannot be blank");
        }

        if (invalidString(playerId)) {
            throw new IllegalArgumentException("playerId cannot be blank");
        }

        if (invalidString(challengeName)) {
            throw new IllegalArgumentException("challengeName cannot be blank");
        }
        this.gameId = gameId;
        this.playerId = playerId;
        this.challengeName = challengeName;
        this.start = start;
        this.end = end;
        this.dateCompleted = dateCompleted;
    }


    public String toAssignedLogFormat() {
        String executionId = generateUUID();
        return String.format(
                "INFO - \"%s\" \"%s\" %s %s %s type=ChallengeAssigned name=\"%s\" startDate=%s endDate=%s",
                gameId, playerId, executionId, start.getTime(), start.getTime(), challengeName,
                start.getTime(), end != null ? end.getTime() : null);
    }


    public String toCompletedLogFormat() {
        if (!isCompleted()) {
            throw new IllegalArgumentException("challenge is not completed");
        }
        String executionId = generateUUID();
        return String.format(
                "INFO - \"%s\" \"%s\" %s %s %s type=ChallengeCompleted name=\"%s\" completed",
                gameId, playerId, executionId, dateCompleted.getTime(), dateCompleted.getTime(),
                challengeName);
    }

    public boolean isCompleted() {
        return dateCompleted != null;
    }

    private static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    private static boolean invalidString(String toeval) {
        return toeval == null || toeval.trim().isEmpty();
    }


    public String getGameId() {
        return gameId;
    }


    public String getPlayerId() {
        return playerId;
    }


    public String getChallengeName() {
        return challengeName;
    }


    public Date getStart() {
        return start;
    }


    public Date getEnd() {
        return end;
    }


    public Date getDateCompleted() {
        return dateCompleted;
    }
}
