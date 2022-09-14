package eu.trentorise.game.stats;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.google.common.base.Preconditions;

public abstract class BaseRecordAnalyzer implements RecordAnalyzer {

    private static final Logger logger = Logger.getLogger(BaseRecordAnalyzer.class);

    protected Map<String, String> commonFields;
    protected Map<String, String> specificFields;
    protected String[] campi;
    protected Record record;

    protected BaseRecordAnalyzer(Record record) {
        Preconditions.checkNotNull(record, "Record cannot be null");
        String valoriComuni = record.getContent().substring(0, record.getIndexType()).trim();
        String valoriSpecifici = record.getContent().substring(record.getIndexType()).trim();
        commonFields = estraiCampiComuni(valoriComuni);
        commonFields = addMetaInformations(commonFields);
        specificFields = estraiCampiSpecifici(valoriSpecifici);
        campi = getNomiCampi();
        this.record = record;

    }

    private Map<String, String> addMetaInformations(Map<String, String> commonFields) {
        if (commonFields != null) {
            commonFields.put("type-input", "log2timescaledb");
            commonFields.put("model-version", "2.0");
        }

        return commonFields;

    }

    protected String sanitizeString(String content) {
        String sanitize = null;
        if (content != null) {
            content = content.trim();
            Matcher regExpMatcher = Pattern.compile("^\"(.*)\"$").matcher(content);
            if (regExpMatcher.find()) {
                sanitize = content.substring(1, content.length() - 1);
            } else {
                sanitize = content;
            }
        }

        return sanitize;
    }

    protected Map<String, String> estraiCampiComuni(String valoriComuni) {
        Pattern campiComuniPattern = Pattern.compile("(\\w+) - \"(.*)\" \"(.*)\" (.*) (.*) (.*)");
        Matcher matcher = campiComuniPattern.matcher(valoriComuni);
        Map<String, String> datiComuni = new HashMap<>();
        if (matcher.matches()) {
            datiComuni.put("logLevel", matcher.group(1));
            datiComuni.put("gameId", matcher.group(2));
            datiComuni.put("playerId", matcher.group(3));
            datiComuni.put("executionId", matcher.group(4));
            datiComuni.put("executionTime", matcher.group(5));
            datiComuni.put("timestamp", matcher.group(6));
            logger.debug(String.format(
                    "dati comuni: logLevel:%s gameId:%s, playerId:%s executionId:%s executionTime:%s timestamp:%s",
                    datiComuni.get("logLevel"), datiComuni.get("gameId"),
                    datiComuni.get("executionId"), datiComuni.get("playerId"),
                    datiComuni.get("executionTime"), datiComuni.get("timestamp")));
        } else {
            throw new IllegalArgumentException("the record doesn't respect common fields pattern");
        }

        return datiComuni;

    }

    protected Map<String, String> estraiCampiSpecifici(String valoriSpecifici) {
        String[] nomiCampi = getNomiCampi();
        int[] indiciCampi = new int[nomiCampi.length];

        Map<String, String> infos = new HashMap<>();
        int cursore = -1;
        for (String nomeCampo : nomiCampi) {
            int indiceCampo = valoriSpecifici.indexOf(nomeCampo);
            if (indiceCampo > -1) {
                indiciCampi[++cursore] = indiceCampo;
            }
        }

        for (int i = 0; i < nomiCampi.length; i++) {
            int indiceInizioValore = indiciCampi[i] + nomiCampi[i].length();
            try {
                infos.put(nomiCampi[i], sanitizeString(
                        valoriSpecifici.substring(indiceInizioValore, indiciCampi[i + 1])));
            } catch (ArrayIndexOutOfBoundsException e) {
                infos.put(nomiCampi[i],
                        sanitizeString(valoriSpecifici.substring(indiceInizioValore)));
            }
        }

        return infos;
    }

}
