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

		Map<String, String> data = new HashMap<>(commonFields);
		data.put("eventType", eventType);
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
