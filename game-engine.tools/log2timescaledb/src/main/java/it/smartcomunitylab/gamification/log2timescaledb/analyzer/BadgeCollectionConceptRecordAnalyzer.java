package it.smartcomunitylab.gamification.log2timescaledb.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2timescaledb.Record;

public class BadgeCollectionConceptRecordAnalyzer extends BaseRecordAnalyzer {

    private static final Logger logger =
            Logger.getLogger(BadgeCollectionConceptRecordAnalyzer.class);

    protected BadgeCollectionConceptRecordAnalyzer(Record record) {
        super(record);
    }

    @Override
    public Map<String, String> extractData() {

        String eventType = specificFields.get(campi[0]);
        String ruleName = specificFields.get(campi[1]);
        String name = specificFields.get(campi[2]);
        String newBadge = specificFields.get(campi[3]);

        Map<String, String> data = new HashMap<>(commonFields);
        data.put("eventType", eventType);
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);
        data.put("ruleName", ruleName);
        data.put("new_badge", newBadge);
        data.put("name", name);

        return data;
    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type=", "ruleName=", "name=", "new_badge="};
        return campi;
    }

}
