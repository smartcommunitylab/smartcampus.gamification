package it.smartcommunitylab.gamification.challenge_db_stats;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.regex.Pattern;

import org.junit.Assert;
import org.junit.Test;

public class ChallengeTest {


    @Test(expected = IllegalArgumentException.class)
    public void createChallengeWithNullGameId() {
        Challenge challenge = new Challenge(null, "player", "name", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createChallengeWithEmptyGameId() {
        Challenge challenge = new Challenge("    ", "player", "name", null, null, null);
    }


    @Test(expected = IllegalArgumentException.class)
    public void createChallengeWithNullPlayerId() {
        Challenge challenge = new Challenge(null, null, "name", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createChallengeWithEmptyPlayerId() {
        Challenge challenge = new Challenge(null, "   ", "name", null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createChallengeWithNullName() {
        Challenge challenge = new Challenge(null, "player", null, null, null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createChallengeWithEmptyName() {
        Challenge challenge = new Challenge(null, "player", "        ", null, null, null);
    }

    @Test
    public void isACompletedChallenge() {
        Challenge challenge = new Challenge("game", "player", "name", null, null, new Date());
        Assert.assertTrue(challenge.isCompleted());
    }

    @Test
    public void notCompletedChallenge() {
        Challenge challenge = new Challenge("game", "player", "name", null, null, null);
        Assert.assertFalse(challenge.isCompleted());
    }

    @Test
    public void correctAssignedLogFormat() {
        final String player = "player";
        final String challengeName = "name";
        final String game = "game";
        Date start =
                Date.from(LocalDateTime.of(2017, 11, 4, 0, 0).toInstant(ZoneOffset.of("+01:00")));
        Date end =
                Date.from(LocalDateTime.of(2017, 11, 6, 0, 0).toInstant(ZoneOffset.of("+01:00")));

        Challenge challenge = new Challenge(game, player, challengeName, start, end, null);
        String regExpPattern = String.format(
                "INFO - \"%s\" \"%s\" %s %s %s type=ChallengeAssigned name=\"%s\" startDate=%s endDate=%s",
                game, player, ".*", start.getTime(), start.getTime(), challengeName,
                start.getTime(), end.getTime());
        Assert.assertTrue(
                Pattern.compile(regExpPattern).matcher(challenge.toAssignedLogFormat()).matches());
    }

    public void correctCompletedLogFormat() {
        final String player = "player";
        final String challengeName = "name";
        final String game = "game";
        Date start =
                Date.from(LocalDateTime.of(2017, 11, 4, 0, 0).toInstant(ZoneOffset.of("+01:00")));
        Date end =
                Date.from(LocalDateTime.of(2017, 11, 6, 0, 0).toInstant(ZoneOffset.of("+01:00")));

        Challenge challenge = new Challenge(game, player, challengeName, start, end, null);
        String regExpPattern = String.format(
                "INFO - \"%s\" \"%s\" %s %s %s type=ChallengeCompleted name=\"%s\" completed", game,
                player, ".*", start.getTime(), start.getTime(), challengeName);
        Assert.assertTrue(
                Pattern.compile(regExpPattern).matcher(challenge.toCompletedLogFormat()).matches());
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidCompletedLogFormat() {
        final String player = "player";
        final String challengeName = "name";
        final String game = "game";
        Date start =
                Date.from(LocalDateTime.of(2017, 11, 4, 0, 0).toInstant(ZoneOffset.of("+01:00")));
        Date end =
                Date.from(LocalDateTime.of(2017, 11, 6, 0, 0).toInstant(ZoneOffset.of("+01:00")));

        Challenge challenge = new Challenge(game, player, challengeName, start, end, null);

        challenge.toCompletedLogFormat();
    }


}
