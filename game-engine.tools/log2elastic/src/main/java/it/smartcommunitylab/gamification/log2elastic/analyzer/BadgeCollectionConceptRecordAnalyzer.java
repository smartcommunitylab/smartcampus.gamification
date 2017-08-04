package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class BadgeCollectionConceptRecordAnalyzer extends BaseRecordAnalyzer {

	private static final Logger logger = Logger.getLogger(BadgeCollectionConceptRecordAnalyzer.class);

	protected BadgeCollectionConceptRecordAnalyzer(Record record) {
		super(record);
	}

	@Override
	public Map<String, String> extractData() {

		String eventType = specificFields.get(campi[0]);
		String ruleName = specificFields.get(campi[1]);
		String name = specificFields.get(campi[2]);
		String newBadge = specificFields.get(campi[3]);

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
		data.put("new_badge", newBadge);
		data.put("name", name);

		return data;
	}

	@Override
	public String[] getNomiCampi() {
		String[] campi = { "type=", "ruleName=", "name=", "new_badge=" };
		return campi;
	}

}
