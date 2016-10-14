package eu.trentorise.game.core;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import eu.trentorise.game.model.BadgeCollectionConcept;
import eu.trentorise.game.model.PlayerState;
import eu.trentorise.game.model.PointConcept;

public class StatsLogger {

	private static final String LOGGER_NAME = "stats";

	private static final Logger logger = LoggerFactory.getLogger(LOGGER_NAME);

	private static ObjectMapper mapper = new ObjectMapper();

	public static void logRule(String gameId, String playerId,
			String executionId, long executionMoment, long timestamp,
			String ruleName, PointConcept concept) {
		String msg = commonFieldsOutput(gameId, playerId, executionId,
				executionMoment, timestamp);
		msg += " "
				+ String.format("type=%s ruleName=\"%s\" name=\"%s\" score=%s",
						PointConcept.class.getSimpleName(), ruleName,
						concept.getName(), concept.getScore());
		logger.info(msg);
	}

	public static void logRule(String gameId, String playerId,
			String executionId, long executionMoment, String ruleName,
			PointConcept concept) {
		logRule(gameId, playerId, executionId, executionMoment,
				System.currentTimeMillis(), ruleName, concept);
	}

	public static void logRule(String gameId, String playerId,
			String executionId, long executionMoment, long timestamp,
			String ruleName, BadgeCollectionConcept concept) {
		String msg = commonFieldsOutput(gameId, playerId, executionId,
				executionMoment, timestamp);
		msg += " "
				+ String.format(
						"type=%s ruleName=\"%s\" name=\"%s\" badges=\"%s\"",
						BadgeCollectionConcept.class.getSimpleName(), ruleName,
						concept.getName(), concept.getBadgeEarned());
		logger.info(msg);
	}

	public static void logRule(String gameId, String playerId,
			String executionId, long executionMoment, String ruleName,
			BadgeCollectionConcept concept) {
		logRule(gameId, playerId, executionId, executionMoment,
				System.currentTimeMillis(), ruleName, concept);
	}

	public static void logAction(String gameId, String playerId,
			String executionId, long executionMoment, String action,
			Map<String, Object> inputData, List<Object> factObjects,
			PlayerState state) {
		logAction(gameId, playerId, executionId, executionMoment,
				System.currentTimeMillis(), action, inputData, factObjects,
				state);
	}

	public static void logAction(String gameId, String playerId,
			String executionId, long executionMoment, long timestamp,
			String action, Map<String, Object> inputData,
			List<Object> factObjects, PlayerState state) {
		String msg = commonFieldsOutput(gameId, playerId, executionId,
				executionMoment, timestamp);

		String stateString = null;

		try {
			stateString = mapper.writeValueAsString(state.getState());
			stateString = StringEscapeUtils.escapeJava(stateString);
		} catch (JsonProcessingException e) {
			logger.error("Exception serializing state to JSON");
		}

		if (!"scogei_classification".equals(action)) {
			String inputDataString = null;
			try {
				inputDataString = mapper.writeValueAsString(inputData);
				inputDataString = StringEscapeUtils.escapeJava(inputDataString);
			} catch (JsonProcessingException e) {
				logger.error("Exception serializing inputData to JSON");
			}

			msg += " "
					+ String.format(
							"type=%s action=\"%s\" payload=\"%s\" oldState=\"%s\"",
							"Action", action, inputDataString, stateString);
		} else {
			String internalDataString = null;
			try {
				internalDataString = mapper.writeValueAsString(factObjects);
				internalDataString = StringEscapeUtils
						.escapeJava(internalDataString);
			} catch (JsonProcessingException e) {
				logger.error("Exception serializing factObjects to JSON");
			}

			msg += " "
					+ String.format(
							"type=%s action=\"%s\" internalData=\"%s\" oldState=\"%s\"",
							"Classification", action, internalDataString,
							stateString);
		}
		logger.info(msg);
	}

	private static String commonFieldsOutput(String gameId, String playerId,
			String executionId, long executionMoment, long timestamp) {
		return String.format("\"%s\" \"%s\" %s %s %s", gameId, playerId,
				executionId, executionMoment, timestamp);
	}
}
