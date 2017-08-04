package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class ActionRecordAnalyzer extends BaseRecordAnalyzer {

	protected ActionRecordAnalyzer(Record record) {
		super(record);
	}

	private static final Logger logger = Logger.getLogger(ActionRecordAnalyzer.class);

	@Override
	public Map<String, String> extractData() {
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
		String actionName = specificFields.get(campi[1]);
		logger.debug("actionName:" + actionName);
		String eventType = specificFields.get(campi[0]);
		logger.debug("eventType:" + eventType);

		Map<String, String> data = new HashMap<>();
		data.put("actionName", actionName);
		data.put("eventType", eventType);
		data.put("executionId", executionId);
		data.put("executionTime", executionTime);
		data.put("logLevel", logLevel);
		data.put("playerId", playerId);
		data.put("timestamp", timestamp);
		data.put("gameId", gameId);

		return data;
	}

	@Override
	public String[] getNomiCampi() {
		String[] campi = { "type=", "actionName=" };
		return campi;
	}

}
