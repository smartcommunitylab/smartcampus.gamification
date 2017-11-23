package eu.trentorise.game.core;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.junit.Test;

public class StatsLoggerTest {


    @Test
    public void assign_challenge_with_start_date_null() {
        StatsLogger.logChallengeAssignment("GAME", "PLAYER", "EXEC_ID", System.currentTimeMillis(),
                "CHALLENGE_NAME", null, new Date());
    }

    @Test
    public void assign_challenge_with_end_date_null() {
        StatsLogger.logChallengeAssignment("GAME", "PLAYER", "EXEC_ID", System.currentTimeMillis(),
                "CHALLENGE_NAME", new Date(), null);
    }

    @Test
    public void assign_challenge_always_valid() {
        StatsLogger.logChallengeAssignment("GAME", "PLAYER", "EXEC_ID", System.currentTimeMillis(),
                "CHALLENGE_NAME", null, null);
    }

    @Test
    public void assign_challenge_valid_in_a_range() {
        Date start = Date.from(
                LocalDate.now().minusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date
                .from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        StatsLogger.logChallengeAssignment("GAME", "PLAYER", "EXEC_ID", System.currentTimeMillis(),
                "CHALLENGE_NAME", start, end);
    }
}
