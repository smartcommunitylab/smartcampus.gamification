package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class ChallengeAssignedRecordAnalyzer extends BaseRecordAnalyzer {
	private static final Logger logger = Logger.getLogger(ChallengeAssignedRecordAnalyzer.class);

	protected ChallengeAssignedRecordAnalyzer(Record record) {
		super(record);
	}

	@Override
	public Map<String, String> extractData() {

		String eventType = specificFields.get(campi[0]);
		String name = specificFields.get(campi[1]);
		String startDate = specificFields.get(campi[2]);
		String endDate = specificFields.get(campi[3]);

		Map<String, String> data = new HashMap<>(commonFields);
		data.put("eventType", eventType);
		data.put("name", name);
		data.put("startDate", startDate);
		data.put("endDate", endDate);

		return data;

	}

	@Override
	public String[] getNomiCampi() {
		String[] campi = { "type=", "name=", "startDate=", "endDate=" };
		return campi;
	}

}
