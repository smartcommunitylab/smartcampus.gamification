package eu.trentorise.game.core;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.hamcrest.Matchers;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Test;

public class StatsLoggerTest {

    @Test
    public void assign_challenge_with_start_date_null() {
        Date endDate = new LocalDateTime(2017, 10, 22, 0, 0).toDate();
        long timestamp = 10l;
        String message = StatsLogger.logChallengeAssignment("DOMAIN", "GAME", "PLAYER", "EXEC_ID",
                timestamp, "CHALLENGE_NAME", null, endDate);
        String expectedMessage =
                "\"DOMAIN\" \"GAME\" \"PLAYER\" EXEC_ID 10 10 type=ChallengeAssigned name=\"CHALLENGE_NAME\" startDate=null endDate=1508623200000";
        Assert.assertThat(message, Matchers.is(expectedMessage));
    }

    @Test
    public void assign_challenge_with_end_date_null() {
        Date startDate = new LocalDateTime(2017, 10, 22, 0, 0).toDate();
        long timestamp = 10l;
        String message = StatsLogger.logChallengeAssignment("DOMAIN", "GAME", "PLAYER", "EXEC_ID",
                timestamp, "CHALLENGE_NAME", startDate, null);
        String expectedMessage =
                "\"DOMAIN\" \"GAME\" \"PLAYER\" EXEC_ID 10 10 type=ChallengeAssigned name=\"CHALLENGE_NAME\" startDate=1508623200000 endDate=null";
        Assert.assertThat(message, Matchers.is(expectedMessage));
    }

    @Test
    public void assign_challenge_always_valid() {
        String message = StatsLogger.logChallengeAssignment("DOMAIN", "GAME", "PLAYER", "EXEC_ID",
                10l, "CHALLENGE_NAME", null, null);
        String expectedMessage =
                "\"DOMAIN\" \"GAME\" \"PLAYER\" EXEC_ID 10 10 type=ChallengeAssigned name=\"CHALLENGE_NAME\" startDate=null endDate=null";
        Assert.assertThat(message, Matchers.is(expectedMessage));
    }

    @Test
    public void assign_challenge_valid_in_a_range() {
        Date start = Date.from(
                LocalDate.parse("2017-10-12").atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(
                LocalDate.parse("2017-10-13").atStartOfDay(ZoneId.systemDefault()).toInstant());
        String message = StatsLogger.logChallengeAssignment("DOMAIN", "GAME", "PLAYER", "EXEC_ID",
                10l, "CHALLENGE_NAME", start, end);
        String expectedMessage =
                "\"DOMAIN\" \"GAME\" \"PLAYER\" EXEC_ID 10 10 type=ChallengeAssigned name=\"CHALLENGE_NAME\" startDate=1507759200000 endDate=1507845600000";
        Assert.assertThat(message, Matchers.is(expectedMessage));
    }

    @Test
    public void challenge_failure_event() {
        String message = StatsLogger.logChallengeFailed("", "GAME", "513", "EXEC_ID", 1000, 1200,
                "challenge_name");
        assertThat(message, is(
                "\"GAME\" \"513\" EXEC_ID 1000 1200 type=ChallengeFailed name=\"challenge_name\" failed"));
    }
}
