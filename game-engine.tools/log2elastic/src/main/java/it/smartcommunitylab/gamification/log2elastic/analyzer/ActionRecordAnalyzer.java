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
		String actionName = specificFields.get(campi[1]);
		logger.debug("actionName:" + actionName);
		String eventType = specificFields.get(campi[0]);
		logger.debug("eventType:" + eventType);

		Map<String, String> data = new HashMap<>(commonFields);
		data.put("actionName", actionName);
		data.put("eventType", eventType);

		return data;
	}

	@Override
	public String[] getNomiCampi() {
		String[] campi = { "type=", "actionName=" };
		return campi;
	}

}
