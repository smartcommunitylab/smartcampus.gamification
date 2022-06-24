package it.smartcomunitylab.gamification.log2timescaledb.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2timescaledb.Record;

public class SurveyCompletedAnalyzer extends BaseRecordAnalyzer {

    private static final Logger logger = Logger.getLogger(SurveyCompletedAnalyzer.class);

    public SurveyCompletedAnalyzer(Record record) {
        super(record);
    }

    @Override
    public Map<String, String> extractData() {
        String eventType = specificFields.get(campi[0]);
        eventType = eventType.substring(0, eventType.indexOf(" "));
        logger.debug("eventType: " + eventType);

        Map<String, String> data = new HashMap<>(commonFields);
        data.put("eventType", eventType);
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);

        return data;
    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type="};
        return campi;
    }

}
