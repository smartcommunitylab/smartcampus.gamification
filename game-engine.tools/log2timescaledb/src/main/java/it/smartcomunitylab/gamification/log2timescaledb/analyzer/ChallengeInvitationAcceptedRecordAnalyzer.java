package it.smartcomunitylab.gamification.log2timescaledb.analyzer;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2timescaledb.Record;

public class ChallengeInvitationAcceptedRecordAnalyzer extends BaseRecordAnalyzer {
    private static final Logger logger = Logger.getLogger(ChallengeInvitationAcceptedRecordAnalyzer.class);

    protected ChallengeInvitationAcceptedRecordAnalyzer(Record record) {
        super(record);
    }

    @Override
    public Map<String, String> extractData() {

        String eventType = specificFields.get(campi[0]);
        String name = specificFields.get(campi[1]);
        String challengeType = specificFields.get(campi[2]);

        Map<String, String> data = new HashMap<>(commonFields);
        data.put("eventType", eventType);
        // TODO improvement: eventType has to be moved to commonFields and type field set by
        // metaInfo
        data.put("type", eventType);
        data.put("name", name);
        data.put("challengeType", challengeType);
        return data;

    }

    @Override
    public String[] getNomiCampi() {
        String[] campi = {"type=", "name=", "challengeType="};
        return campi;
    }

}
