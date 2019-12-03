package it.smartcommunitylab.gamification.log2elastic.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.Record;

public class LevelGainedRecordAnalyzer extends BaseRecordAnalyzer {

    private static final Logger logger = Logger.getLogger(UserCreationRecordAnalyzer.class);

    protected LevelGainedRecordAnalyzer(Record record) {
        super(record);
    }

    @Override
    public Map<String, String> extractData() {
        String eventType = specificFields.get(campi[0]);
        logger.debug("eventType: " + eventType);

        Map<String, String> data = new HashMap<>(commonFields);
        data.put("eventType", eventType);
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);
        data.put("levelName", specificFields.get(campi[1]));
        data.put("levelType", specificFields.get(campi[2]));
        return data;
    }
    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type=", "levelName=", "levelType="};
        return campi;
    }

}
