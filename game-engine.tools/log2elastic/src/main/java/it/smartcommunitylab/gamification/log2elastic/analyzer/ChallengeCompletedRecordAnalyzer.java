package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class ChallengeCompletedRecordAnalyzer extends BaseRecordAnalyzer {
	private static final Logger logger = Logger.getLogger(ChallengeCompletedRecordAnalyzer.class);

	public ChallengeCompletedRecordAnalyzer(Record record) {
		super(record);
	}

	@Override
	public Map<String, String> extractData() {
		String eventType = specificFields.get(campi[0]);
		String name = specificFields.get(campi[1]);
		// fix name field
		name = name.replaceFirst("\"", "").replace("\" completed", "");

		Map<String, String> data = new HashMap<>(commonFields);
		data.put("eventType", eventType);
		data.put("name", name);

		return data;
	}

	@Override
	public String[] getNomiCampi() {
		String[] campi = { "type=", "name=" };
		return campi;
	}

}
