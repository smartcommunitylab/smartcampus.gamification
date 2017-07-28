package eu.trentorise.game.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;
import eu.trentorise.game.task.Classification;

public class StatsLogger {

	private static final String LOGGER_NAME = "stats";

	private static final Logger statsLogger = LoggerFactory.getLogger(LOGGER_NAME);

	private static final Logger internalLogger = LoggerFactory.getLogger(StatsLogger.class);

	private static ObjectMapper mapper = new ObjectMapper();

	public static void logRule(String gameId, String playerId, String executionId, long executionMoment, long timestamp,
			String ruleName, PointConcept concept) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, executionMoment, timestamp);
		msg += " " + String.format("type=%s ruleName=\"%s\" name=\"%s\" score=%s", PointConcept.class.getSimpleName(),
				ruleName, concept.getName(), concept.getScore());
		statsLogger.info(msg);
	}

	public static void logRule(String gameId, String playerId, String executionId, long executionMoment,
			String ruleName, PointConcept concept) {
		logRule(gameId, playerId, executionId, executionMoment, System.currentTimeMillis(), ruleName, concept);
	}

	public static void logRulePointConceptDelta(String gameId, String playerId, String executionId,
			long executionMoment, String ruleName, PointConcept concept, double deltaScore) {
		logRulePointConceptDelta(gameId, playerId, executionId, executionMoment, System.currentTimeMillis(), ruleName,
				concept, deltaScore);
	}

	public static void logRuleBadgeCollectionConceptDelta(String gameId, String playerId, String executionId,
			long executionMoment, String ruleName, String conceptName, String badge) {
		logRuleBadgeCollectionConceptDelta(gameId, playerId, executionId, executionMoment, System.currentTimeMillis(),
				ruleName, conceptName, badge);
	}

	public static void logRule(String gameId, String playerId, String executionId, long executionMoment, long timestamp,
			String ruleName, BadgeCollectionConcept concept) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, executionMoment, timestamp);
		msg += " " + String.format("type=%s ruleName=\"%s\" name=\"%s\" badges=\"%s\"",
				BadgeCollectionConcept.class.getSimpleName(), ruleName, concept.getName(), concept.getBadgeEarned());
		statsLogger.info(msg);
	}

	public static void logRule(String gameId, String playerId, String executionId, long executionMoment,
			String ruleName, BadgeCollectionConcept concept) {
		logRule(gameId, playerId, executionId, executionMoment, System.currentTimeMillis(), ruleName, concept);
	}

	private static void logRulePointConceptDelta(String gameId, String playerId, String executionId,
			long executionMoment, long timestamp, String ruleName, PointConcept concept, double deltaScore) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, executionMoment, timestamp);

		msg += " " + String.format("type=%s ruleName=\"%s\" name=\"%s\" deltaScore=%s score=%s",
				PointConcept.class.getSimpleName(), ruleName, concept.getName(), deltaScore, concept.getScore());

		statsLogger.info(msg);
	}

	private static void logRuleBadgeCollectionConceptDelta(String gameId, String playerId, String executionId,
			long executionMoment, long timestamp, String ruleName, String conceptName, String badge) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, executionMoment, timestamp);

		msg += " " + String.format("type=%s ruleName=\"%s\" name=\"%s\" new_badge=\"%s\"",
				BadgeCollectionConcept.class.getSimpleName(), ruleName, conceptName, badge);
		statsLogger.info(msg);
	}

	public static void logAction(String gameId, String playerId, String executionId, long executionMoment,
			String action, Map<String, Object> inputData, List<Object> factObjects, PlayerState state) {
		logAction(gameId, playerId, executionId, executionMoment, System.currentTimeMillis(), action, inputData,
				factObjects, state);
	}

	public static void logAction(String gameId, String playerId, String executionId, long executionMoment,
			long timestamp, String action, Map<String, Object> inputData, List<Object> factObjects, PlayerState state) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, executionMoment, timestamp);

		msg += " " + String.format("type=%s actionName=\"%s\"", "Action", action);
		statsLogger.info(msg);
	}

	public static void logClassification(String gameId, String playerId, String executionId, long executionMoment,
			Map<String, Object> inputData, List<Object> factObjects) {
		logClassification(gameId, playerId, executionId, executionMoment, System.currentTimeMillis(), inputData,
				factObjects);
	}

	public static void logClassification(String gameId, String playerId, String executionId, long executionMoment,
			long timestamp, Map<String, Object> inputData, List<Object> factObjects) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, executionMoment, timestamp);

		if (CollectionUtils.isNotEmpty(factObjects)) {
			for (Object factObj : factObjects) {
				if (factObj instanceof Classification) {
					Classification obj = (Classification) factObj;
					msg += " " + String.format("type=%s classificationName=\"%s\" classificationPosition=%s",
							"Classification", obj.getName(), obj.getPosition());
					statsLogger.info(msg);
				}
			}
		} else {
			LogHub.warn(gameId, internalLogger,
					"Classification event with Classification payload null or absent in game {} for player {}", gameId,
					playerId);
		}

	}

	public static void logUserCreation(String gameId, String playerId, String executionId, long timestamp) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, timestamp, timestamp);
		msg += " " + String.format("type=%s creation", "UserCreation");
		statsLogger.info(msg);
	}

	public static void logChallengeAssignment(String gameId, String playerId, String executionId, long timestamp,
			String challengeName, Date start, Date end) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, timestamp, timestamp);
		msg += " " + String.format("type=%s name=\"%s\" startDate=%s endDate=%s", "ChallengeAssigned", challengeName,
				start.getTime(), end.getTime());
		statsLogger.info(msg);
	}

	public static void logChallengeComplete(String gameId, String playerId, String executionId, long executionTime,
			long timestamp, String challengeName) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, timestamp, timestamp);
		msg += " " + String.format("type=%s name=\"%s\" completed", "ChallengeCompleted", challengeName);
		statsLogger.info(msg);
	}

	public static void logEndGameAction(String gameId, String playerId, String executionId, long executionTime,
			long timestamp) {
		String msg = commonFieldsOutput(gameId, playerId, executionId, timestamp, timestamp);
		msg += " " + String.format("type=%s", "EndGameAction");
		statsLogger.info(msg);
	}

	private static String commonFieldsOutput(String gameId, String playerId, String executionId, long executionMoment,
			long timestamp) {
		return String.format("\"%s\" \"%s\" %s %s %s", gameId, playerId, executionId, executionMoment, timestamp);
	}
}
