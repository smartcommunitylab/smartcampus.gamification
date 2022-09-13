package eu.trentorise.game.stats;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ChallengeFailedRecordAnalyzer extends BaseRecordAnalyzer {
    private static final Logger logger = Logger.getLogger(ChallengeFailedRecordAnalyzer.class);

    protected ChallengeFailedRecordAnalyzer(Record record) {
        super(record);
    }

    @Override
    public Map<String, String> extractData() {

        String eventType = specificFields.get(campi[0]);
        String name = specificFields.get(campi[1]);

        Map<String, String> data = new HashMap<>(commonFields);
        data.put("eventType", eventType);
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);
        data.put("name", name);
        return data;

    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type=", "name="};
        return campi;
    }

}
