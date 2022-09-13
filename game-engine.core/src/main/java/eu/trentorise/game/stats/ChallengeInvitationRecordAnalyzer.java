package eu.trentorise.game.stats;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class ChallengeInvitationRecordAnalyzer extends BaseRecordAnalyzer {
    private static final Logger logger = Logger.getLogger(ChallengeInvitationRecordAnalyzer.class);

    protected ChallengeInvitationRecordAnalyzer(Record record) {
        super(record);
    }

    @Override
    public Map<String, String> extractData() {

        String eventType = specificFields.get(campi[0]);
        String guest = specificFields.get(campi[1]);
        String name = specificFields.get(campi[2]);
        String challengeType = specificFields.get(campi[3]);

        Map<String, String> data = new HashMap<>(commonFields);
        data.put("eventType", eventType);
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);
        data.put("guest", guest);
        data.put("name", name);
        data.put("challengeType", challengeType);
        return data;

    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type=", "guest=", "name=", "challengeType="};
        return campi;
    }

}
