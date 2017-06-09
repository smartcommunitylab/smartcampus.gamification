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

	public String analizzaAction(Record record) {
		String out = null;

		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());

		// System.out.println(splitXSpazi);

		// System.out.println(splitDiverso);

		String[] campi = { "type=", "action=", "payload=", "oldState=" };
		int[] indiciCampi = new int[4];
		int[] indiciInformazioni = new int[4];
		String[] info = new String[4];

		for (int i = 0; i < campi.length; i++) {
			if (splitDiverso.contains(campi[i])) {
				indiciCampi[i] = splitDiverso.indexOf(campi[i]);
				indiciInformazioni[i] = indiciCampi[i] + campi[i].length();

				// System.out.println("campo: " + indiciCampi[i]);
				// System.out.println("informazione: " + indiciInformazioni[i]);
			}
		}
		for (int i = 0; i < campi.length; i++) {
			if (i < campi.length - 1) {
				info[i] = splitDiverso.substring(indiciCampi[i],
						indiciCampi[i + 1]);
				// System.out.println("indice +1;  " + indiciCampi[i + 1]);
			} else {
				info[i] = splitDiverso.substring(indiciCampi[i],
						splitDiverso.length() - 1);
			}
			// System.out.println("numero " + i + " stampa: " + info[i]);

		}
		out = splitXSpazi + info[0] + info[1];
		logger.info("il nuovo messaggio per action �: " + out);
		return out;
	}

	/*
	 * public static void test() throws IOException { Record r = new Record();
	 * 
	 * // FileWriter fw = null; FileReader fr = null; BufferedReader br = null;
	 * Boolean sovrascrivo = false; File f; f = new
	 * File(Application.LOG_FOLDER_PATH + "lol.txt"); fr = new FileReader(f); br
	 * = new BufferedReader(fr);
	 * 
	 * String inputLine; String recordTrasformato = null;
	 * 
	 * String out = null; if ((inputLine = br.readLine()) != null) { out =
	 * analizzaAction(r);
	 * 
	 * // } System.out.println(out); }
	 */

	private RecordType valueOf(String type) {
		switch (type) {
		case "Action":
			return RecordType.ACTION;
		case "PointConcept":
			return RecordType.RULE_POINTCONCEPT;
		default:
			throw new IllegalArgumentException(type + " non � supportato");
		}
	}
}
