package eu.trentorise.game.stats;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class EndGameRecordAnalyzer extends BaseRecordAnalyzer {

    private static final Logger logger = Logger.getLogger(UserCreationRecordAnalyzer.class);

    protected EndGameRecordAnalyzer(Record record) {
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
        return data;
    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type="};
        return campi;
    }

}
