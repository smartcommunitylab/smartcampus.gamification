package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class PointConceptRecordAnalyzer extends BaseRecordAnalyzer {

	private static final Logger logger = Logger.getLogger(PointConceptRecordAnalyzer.class);

	public PointConceptRecordAnalyzer(Record record) {
		super(record);
	}

	@Override
	public Map<String, String> extractData() {
		String name = specificFields.get(campi[2]);
		String ruleName = specificFields.get(campi[1]);
		logger.debug("ruleName: " + ruleName);
		logger.debug("name: " + name);
		String score = specificFields.get(campi[4]);
		logger.debug("score: " + score);
		String deltaScore = specificFields.get(campi[3]);
		logger.debug("deltascore: " + deltaScore);

		String eventType = specificFields.get(campi[0]);

		String executionId = commonFields.get("executionId");
		logger.debug("executionId:" + executionId);
		String executionTime = commonFields.get("executionTime");
		logger.debug("executionTime:" + executionTime);
		String gameId = commonFields.get("gameId");
		logger.debug("gameId:" + gameId);
		String logLevel = commonFields.get("logLevel");
		logger.debug("logLevel:" + logLevel);
		String playerId = commonFields.get("playerId");
		logger.debug("playerId:" + playerId);
		String timestamp = commonFields.get("timestamp");
		logger.debug("timestamp:" + timestamp);

		Map<String, String> data = new HashMap<>();
		data.put("logLevel", logLevel);
		data.put("executionId", executionId);
		data.put("executionTime", executionTime);
		data.put("timestamp", timestamp);
		data.put("eventType", eventType);
		data.put("gameId", gameId);
		data.put("playerId", playerId);
		data.put("ruleName", ruleName);
		data.put("score", score);
		data.put("deltaScore", deltaScore);
		data.put("name", name);

		return data;

	}

	@Override
	public String[] getNomiCampi() {
		String[] campi = { "type=", "ruleName=", "name=", "deltaScore=", "score=" };
		return campi;
	}

}
