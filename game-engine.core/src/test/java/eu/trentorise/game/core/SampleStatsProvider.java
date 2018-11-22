package eu.trentorise.game.core;

import static eu.trentorise.game.core.StatsLogger.logAction;
import static eu.trentorise.game.core.StatsLogger.logBlacklist;
import static eu.trentorise.game.core.StatsLogger.logChallengeAccepted;
import static eu.trentorise.game.core.StatsLogger.logChallengeAssignment;
import static eu.trentorise.game.core.StatsLogger.logChallengeCompleted;
import static eu.trentorise.game.core.StatsLogger.logChallengeFailed;
import static eu.trentorise.game.core.StatsLogger.logChallengeInvitationAccepted;
import static eu.trentorise.game.core.StatsLogger.logChallengeInvitationRefused;
import static eu.trentorise.game.core.StatsLogger.logChallengeProposed;
import static eu.trentorise.game.core.StatsLogger.logChallengeRefused;
import static eu.trentorise.game.core.StatsLogger.logClassification;
import static eu.trentorise.game.core.StatsLogger.logEndGameAction;
import static eu.trentorise.game.core.StatsLogger.logInviteToChallenge;
import static eu.trentorise.game.core.StatsLogger.logLevelGained;
import static eu.trentorise.game.core.StatsLogger.logRuleBadgeCollectionConceptDelta;
import static eu.trentorise.game.core.StatsLogger.logRulePointConceptDelta;
import static eu.trentorise.game.core.StatsLogger.logUnblacklist;
import static eu.trentorise.game.core.StatsLogger.logUserCreation;

import java.util.Date;
import java.util.UUID;

import org.joda.time.DateTime;

import eu.trentorise.game.model.LevelInstance;
import eu.trentorise.game.model.PointConcept;

public class SampleStatsProvider {

    public static void main(String[] args) {
        final String domain = null;
        final String gameId = "GAME";
        long executionMoment = dateAsMillis("2018-11-22T11:00:22");
        final String actionUuid = uuid();
        logAction(domain, gameId, "101", actionUuid, executionMoment,
                executionMoment, "ACTION", null, null, null);
        logRulePointConceptDelta(domain, gameId, "101", actionUuid, executionMoment, "rules",
                new PointConcept("points"), 23D);
        logRuleBadgeCollectionConceptDelta(domain, gameId, "11", actionUuid,
                executionMoment, "rule badge", "my badges",
                "first medal");
        
        logBlacklist(domain, gameId, "101", uuid(), dateAsMillis("2018-11-22T23:10:22"), "445");
        logUnblacklist(domain, gameId, "101", uuid(), dateAsMillis("2018-11-22T23:50:12"), "445");
        long acceptedChallengeMillis = dateAsMillis("2018-11-22T08:50:12");
        logChallengeAccepted(domain, gameId, "111", uuid(), acceptedChallengeMillis,
                acceptedChallengeMillis, "challenge name");
        long assignmentChallengeMillis = dateAsMillis("2018-11-22T09:20:00");
        logChallengeAssignment(domain, gameId, "11", uuid(), assignmentChallengeMillis,
                "challenge assign", date("2018-11-22T00:00:00"), date("2018-11-23T00:00:00"));
        long completedChallengeMillis = dateAsMillis("2018-11-22T10:50:00");
        logChallengeCompleted(domain, gameId, "11", uuid(), completedChallengeMillis,
                completedChallengeMillis, "challenge name");
        
        long failedChallengeMillis = dateAsMillis("2018-11-22T10:50:00");
        logChallengeFailed(domain, gameId, "11", uuid(), failedChallengeMillis,
                failedChallengeMillis, "challenge name");

        long timestamp = dateAsMillis("2018-11-22T11:50:00");
        logChallengeInvitationAccepted(domain, gameId, "100", uuid(), timestamp,
                timestamp, "challenge name", "groupCompetitiveTime");

        logChallengeInvitationRefused(domain, gameId, "100", uuid(), timestamp,
                timestamp, "challenge name", "groupCompetitiveTime");

        logChallengeProposed(domain, gameId, "1111", uuid(), timestamp, timestamp,
                "challenge name");

        logChallengeRefused(domain, gameId, "1111", uuid(), timestamp, timestamp,
                "challenge name");

        logClassification(domain, gameId, "3333", uuid(), executionMoment, null, null);

        logEndGameAction(domain, gameId, "233", uuid(), timestamp,
                timestamp);
        logInviteToChallenge(domain, gameId, "23455", uuid(), timestamp, timestamp, "8",
                "instanceChallenge", "groupCompetitivePerformance");
        logLevelGained(domain, gameId, "2333",
                new LevelInstance("Green Warrior", "Adept"), uuid(), timestamp,
                timestamp);
        logUserCreation(domain, gameId, "222", uuid(), timestamp);
    }


    private static String uuid() {
        return UUID.randomUUID().toString();
    }

    private static long dateAsMillis(String datestring) {
        return DateTime.parse(datestring).getMillis();
    }

    private static Date date(String datestring) {
        return DateTime.parse(datestring).toDate();
    }

}
