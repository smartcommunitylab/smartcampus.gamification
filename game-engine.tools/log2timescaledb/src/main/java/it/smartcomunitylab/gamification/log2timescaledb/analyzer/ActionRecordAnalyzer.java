package it.smartcomunitylab.gamification.log2timescaledb.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2timescaledb.Record;

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
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);

        return data;
    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type=", "actionName="};
        return campi;
    }

}
