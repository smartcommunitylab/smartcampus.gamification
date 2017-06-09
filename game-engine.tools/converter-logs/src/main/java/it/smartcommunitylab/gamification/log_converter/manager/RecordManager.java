package it.smartcommunitylab.gamification.log_converter.manager;

import it.smartcommunitylab.gamification.log_converter.beans.Record;
import it.smartcommunitylab.gamification.log_converter.beans.RecordType;

import org.apache.log4j.Logger;

public class RecordManager {

	private final static Logger logger = Logger.getLogger(RecordManager.class);

	public RecordManager() {
	}

	public Record analizza(String record) {
		Record result = null;
		if (record != null) {
			logger.debug("Analisi record: " + record);
			result = new Record();
			result.setContent(record);
			int indiceDelCampo = record.indexOf("type=");
			if (indiceDelCampo > 0) {
				result.setIndexType(indiceDelCampo);
				indiceDelCampo = indiceDelCampo + 5;
				String type = record.substring(indiceDelCampo,
						record.indexOf(" ", indiceDelCampo));
				logger.debug(String.format("Valore di type %s", type));
				result.setType(valueOf(type));
			} else {
				logger.warn("il record non contiene type=");
			}
		}

		return result;
	}

	public String analizzaClassification(Record record) {

		String out = null;

		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());

		String[] campi = { "type=", "action=", "internalData=", "oldState=" };
		int[] indiciCampi = new int[4];
		int[] indiciInformazioni = new int[4];
		String[] info = new String[4];

		for (int i = 0; i < campi.length; i++) {
			if (splitDiverso.contains(campi[i])) {
				indiciCampi[i] = splitDiverso.indexOf(campi[i]);
				indiciInformazioni[i] = indiciCampi[i] + campi[i].length();

			}
		}

		for (int i = 0; i < campi.length; i++) {
			if (i < campi.length - 1) {
				info[i] = splitDiverso.substring(indiciCampi[i],
						indiciCampi[i + 1]);
			} else {
				info[i] = splitDiverso.substring(indiciCampi[i],
						splitDiverso.length() - 1);
			}
		}

		// creazione nuovi campi

		String classificationPosition = "\""
				+ info[2].split(",")[1].substring(13) + "\"";
		String classificationName = info[2].split(",")[0].substring(
				campi[2].length() + 3 + 10, info[2].split(",")[0].length() - 2)
				+ "\"";

		out = splitXSpazi + "classificationPosition=" + classificationPosition
				+ " classificationName=" + classificationName;
		logger.info("il nuovo messaggio per Classification �: " + out);
		return out;
	}

	public String analizzaAction(Record record) {
		String out = null;

		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());

		String[] campi = { "type=", "action=", "payload=", "oldState=" };
		int[] indiciCampi = new int[4];
		int[] indiciInformazioni = new int[4];
		String[] info = new String[4];

		for (int i = 0; i < campi.length; i++) {
			if (splitDiverso.contains(campi[i])) {
				indiciCampi[i] = splitDiverso.indexOf(campi[i]);
				indiciInformazioni[i] = indiciCampi[i] + campi[i].length();

			}
		}
		for (int i = 0; i < campi.length; i++) {
			// coltrollo toglire ultimo spazio
			if (i < campi.length - 1) {
				info[i] = splitDiverso.substring(indiciCampi[i],
						indiciCampi[i + 1]);
			} else {
				info[i] = splitDiverso.substring(indiciCampi[i],
						splitDiverso.length() - 1);
			}

		}
		out = splitXSpazi + info[0] + info[1];
		logger.info("il nuovo messaggio per action �: " + out);
		return out;
	}

	private RecordType valueOf(String type) {
		switch (type) {
		case "Action":
			return RecordType.ACTION;
		case "PointConcept":
			return RecordType.RULE_POINTCONCEPT;
		case "BadgeCollectionConcept":
			return RecordType.RULE_BADGECOLLECTIONCONCEPT;
		case "Classification":
			return RecordType.CLASSIFICATION;
		default:
			throw new IllegalArgumentException(type + " non � supportato");
		}
	}
}
