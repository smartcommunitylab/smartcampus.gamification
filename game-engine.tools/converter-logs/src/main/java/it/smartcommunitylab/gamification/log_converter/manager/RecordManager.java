package it.smartcommunitylab.gamification.log_converter.manager;

import it.smartcommunitylab.gamification.log_converter.beans.Record;
import it.smartcommunitylab.gamification.log_converter.beans.RecordType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RecordManager {

	private final static Logger logger = Logger.getLogger(RecordManager.class);

	Map<String, List<String>> badgesDictionary = new HashMap<String, List<String>>();

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

	public String analizzaBadgeCollection(Record record) {
		String out = null;

		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());

		String[] campi = { "type=", "ruleName=", "name=", "badges=" };
		int[] indiciCampi = new int[campi.length];
		int[] indiciInformazioni = new int[campi.length];
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
		String badgeColl = info[3].substring(campi[3].length() + 1);// ,
		badgeColl = badgeColl.substring(badgeColl.indexOf("[") + 1,
				badgeColl.indexOf("]"));
		String[] vettore = badgeColl.split(",");

		String nome = info[2].substring(campi[2].length() + 1);
		nome = nome.substring(0, nome.indexOf("\""));
		if (badgesDictionary.get(nome) != null) {
			List<String> risposta = new ArrayList<String>(
					CollectionUtils.subtract(Arrays.asList(vettore),
							badgesDictionary.get(nome)));
			System.out.println("risp: " + risposta);

		} else {
			System.out.println("vuoto");
		}

		return out;
	}

	public String analizzaClassification(Record record) {

		String out = null;

		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());

		String[] campi = { "type=", "action=", "internalData=", "oldState=" };
		int[] indiciCampi = new int[campi.length];
		int[] indiciInformazioni = new int[campi.length];
		String[] info = new String[campi.length];

		// idicizzazione
		for (int i = 0; i < campi.length; i++) {
			if (splitDiverso.contains(campi[i])) {
				indiciCampi[i] = splitDiverso.indexOf(campi[i]);
				indiciInformazioni[i] = indiciCampi[i] + campi[i].length();

			}
		}

		// estrazione informazione dai campi attraverso indice
		for (int i = 0; i < campi.length; i++) {
			if (i < campi.length - 1) {
				info[i] = splitDiverso.substring(indiciCampi[i],
						indiciCampi[i + 1]);
			} else {
				info[i] = splitDiverso.substring(indiciCampi[i],
						splitDiverso.length() - 1);
			}
		}

		// creazione nuovi campi classifica
		String classificationPosition = "\""
				+ info[2].split(",")[1].substring(13) + "\"";
		String classificationName = info[2].split(",")[0].substring(
				campi[2].length() + 3 + 10, info[2].split(",")[0].length() - 2)
				+ "\"";

		// restituzione risultato
		out = splitXSpazi + "classificationPosition=" + classificationPosition
				+ " classificationName=" + classificationName;
		logger.info("il nuovo messaggio per Classification �: " + out);
		return out;
	}

	public String analizzaAction(Record record) {
		String out = null;

		badgesDictionary.clear();

		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());

		String[] campi = { "type=", "action=", "payload=", "oldState=" };
		int[] indiciCampi = new int[campi.length];
		int[] indiciInformazioni = new int[campi.length];
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
		String json = info[3].substring(campi[3].length() + 1);// ,
		JsonParser parser = new JsonParser();
		logger.info("inizio parsing oldState");
		JsonArray jsonArray = parser.parse(json).getAsJsonArray();

		for (JsonElement element : jsonArray) {
			JsonObject obj = element.getAsJsonObject();

			if (obj.get("badgeEarned") != null) {

				logger.debug("oggetto: " + obj.toString());
				String badgeCollectionName = obj.get("name").getAsString();
				// nome badge
				logger.debug("nomeBadge==" + badgeCollectionName);

				JsonArray badges = obj.get("badgeEarned").getAsJsonArray();

				logger.debug("badges: " + badges.toString());

				List<String> listaValori = new ArrayList<String>();
				for (JsonElement badgeElement : badges) {
					listaValori.add(badgeElement.getAsString());
				}

				logger.debug("badges array size: " + listaValori.size());
				badgesDictionary.put(badgeCollectionName, listaValori);
			}
		}
		logger.debug("dizionario valori badges: " + badgesDictionary);

		out = splitXSpazi + info[0] + info[1];
		out = out.substring(0, out.length() - 1);
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
