package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class ClassificationRecordAnalyzer extends BaseRecordAnalyzer {

	private static final Logger logger = Logger.getLogger(BadgeCollectionConceptRecordAnalyzer.class);

	public ClassificationRecordAnalyzer(Record record) {
		super(record);
	}

	@Override
	public Map<String, String> extractData() {

		String eventType = specificFields.get(campi[0]);
		String classificationName = specificFields.get(campi[1]);
		String classificationPosition = specificFields.get(campi[2]);

		Map<String, String> data = new HashMap<>(commonFields);
		data.put("eventType", eventType);
		data.put("classificationName", classificationName);
		data.put("classificationPosition", classificationPosition);

		return data;
	}

	@Override
	public String[] getNomiCampi() {
		String[] campi = { "type=", "classificationName=", "classificationPosition=" };
		return campi;
	}

}
