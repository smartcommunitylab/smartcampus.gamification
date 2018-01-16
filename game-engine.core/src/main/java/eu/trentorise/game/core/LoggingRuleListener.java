package eu.trentorise.game.core;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.LogManager;
import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.perf4j.StopWatch;
import org.perf4j.log4j.Log4JStopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.CustomData;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.notification.BadgeNotification;

public class LoggingRuleListener implements RuleRuntimeEventListener {

    private Logger logger = LoggerFactory.getLogger(LoggingRuleListener.class);

    private String domain;
    private String gameId;
    private String playerId;
    private String executionId;
    private long executionMoment;
    private PlayerState playerState;

    public LoggingRuleListener(String domain, String gameId, String playerId,
            PlayerState playerState, String executionId, long executionMoment) {
        this.domain = domain;
        this.gameId = gameId;
        this.playerId = playerId;
        this.executionId = executionId;
        this.executionMoment = executionMoment;
        this.playerState = playerState;
    }

    public LoggingRuleListener() {}

    @Override
    public void objectDeleted(ObjectDeletedEvent deleteEvent) {}

    @Override
    public void objectInserted(ObjectInsertedEvent insertEvent) {
        Object workingObj = insertEvent.getObject();

        if (workingObj instanceof PointConcept) {
            PointConcept pc = (PointConcept) workingObj;

            LogHub.info(gameId, logger,
                    "rule \'{}\' created PointConcept \'{}\' with score {}"
                            + (StringUtils.isBlank(playerId) ? "" : " of player {}"),
                    insertEvent.getRule() != null ? insertEvent.getRule().getName() : "-",
                    pc.getName(), pc.getScore(), playerId);
        }

        if (workingObj instanceof BadgeCollectionConcept) {
            BadgeCollectionConcept bcc = (BadgeCollectionConcept) workingObj;
            LogHub.info(gameId, logger,
                    "rule \'{}\' created BadgeCollectionConcept \'{}\' with badges {}"
                            + (StringUtils.isBlank(playerId) ? "" : " of player {}"),
                    insertEvent.getRule() != null ? insertEvent.getRule().getName() : "-",
                    bcc.getName(), bcc.getBadgeEarned(), playerId);
        }

        if (workingObj instanceof BadgeNotification) {
            BadgeNotification bn = (BadgeNotification) workingObj;
            LogHub.info(gameId, logger,
                    "rule \'{}\' created BadgeNotification for badge \'{}\'"
                            + (StringUtils.isBlank(playerId) ? "" : " of player {}"),
                    insertEvent.getRule() != null ? insertEvent.getRule().getName() : "-",
                    bn.getBadge(), playerId);
        }

        if (workingObj instanceof CustomData) {
            LogHub.info(gameId, logger,
                    "rule \'{}\' added CustomData"
                            + (StringUtils.isBlank(playerId) ? "" : " of player {}"),
                    insertEvent.getRule() != null ? insertEvent.getRule().getName() : "-",
                    playerId);
        }
    }

    @Override
    public void objectUpdated(ObjectUpdatedEvent updateEvent) {
        StopWatch stopWatch =
                LogManager.getLogger(StopWatch.DEFAULT_LOGGER_NAME).getAppender("perf-file") != null
                        ? new Log4JStopWatch() : null;
        if (stopWatch != null) {
            stopWatch.start("update rule listener");
        }

        Object workingObj = updateEvent.getObject();

        if (workingObj instanceof PointConcept) {
            PointConcept pc = (PointConcept) workingObj;
            double deltaScore =
                    PlayerStateUtils.getDeltaScore(playerState, pc.getName(), pc.getScore());
            LogHub.info(gameId, logger,
                    "rule \'{}\' updated PointConcept \'{}\' of {} (total: {})"
                            + (StringUtils.isBlank(playerId) ? "" : " of player {}"),
                    updateEvent.getRule() != null ? updateEvent.getRule().getName() : "-",
                    pc.getName(), deltaScore, pc.getScore(), playerId);

            StatsLogger.logRulePointConceptDelta(domain, gameId, playerId, executionId,
                    executionMoment, updateEvent.getRule().getName(), pc, deltaScore);
            PlayerStateUtils.incrementPointConcept(playerState, pc.getName(), deltaScore);

        }

        if (workingObj instanceof BadgeCollectionConcept) {
            BadgeCollectionConcept bcc = (BadgeCollectionConcept) workingObj;
            List<String> deltaBadges = PlayerStateUtils.getDeltaBadges(playerState, bcc.getName(),
                    bcc.getBadgeEarned());
            if (!deltaBadges.isEmpty()) {
                LogHub.info(gameId, logger,
                        "rule \'{}\' updated BadgeCollectionConcept \'{}\' with \'{}\'"
                                + (StringUtils.isBlank(playerId) ? "" : " of player {}"),
                        updateEvent.getRule() != null ? updateEvent.getRule().getName() : "-",
                        bcc.getName(), deltaBadges.get(0), playerId);
                StatsLogger.logRuleBadgeCollectionConceptDelta(domain, gameId, playerId,
                        executionId, executionMoment, updateEvent.getRule().getName(),
                        bcc.getName(), deltaBadges.get(0));
                PlayerStateUtils.incrementBadgeCollectionConcept(playerState, bcc.getName(),
                        deltaBadges.get(0));
            }
        }

        if (workingObj instanceof CustomData) {
            LogHub.info(gameId, logger,
                    "rule \'{}\' updated CustomData"
                            + (StringUtils.isBlank(playerId) ? "" : " of player {}"),
                    updateEvent.getRule() != null ? updateEvent.getRule().getName() : "-",
                    playerId);
        }

        if (stopWatch != null) {
            stopWatch.stop("update rule listener", String.format(
                    "update facts in rule %s of game %s", updateEvent.getRule().getName(), gameId));
        }
    }
}
