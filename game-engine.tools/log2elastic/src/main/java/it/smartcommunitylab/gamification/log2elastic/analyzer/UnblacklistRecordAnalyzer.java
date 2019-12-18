package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class UnblacklistRecordAnalyzer extends BaseRecordAnalyzer {

    protected UnblacklistRecordAnalyzer(Record record) {
        super(record);
    }

    private static final Logger logger = Logger.getLogger(UnblacklistRecordAnalyzer.class);

    @Override
    public Map<String, String> extractData() {
        String blockedPlayer = specificFields.get(campi[1]);
        logger.debug("blockedPlayer:" + blockedPlayer);
        String eventType = specificFields.get(campi[0]);
        logger.debug("eventType:" + eventType);

        Map<String, String> data = new HashMap<>(commonFields);
        data.put("blockedPlayer", blockedPlayer);
        data.put("eventType", eventType);
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);

        return data;
    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type=", "blockedPlayer="};
        return campi;
    }

}
