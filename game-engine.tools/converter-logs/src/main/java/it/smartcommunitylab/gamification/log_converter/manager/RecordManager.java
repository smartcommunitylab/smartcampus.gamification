package it.smartcommunitylab.gamification.log_converter.manager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import it.smartcommunitylab.gamification.log_converter.beans.Record;
import it.smartcommunitylab.gamification.log_converter.beans.RecordType;

public class RecordManager {
	private final static Logger logger = Logger.getLogger(RecordManager.class);
	Map<String, List<String>> badgesDictionary = new HashMap<String, List<String>>();
	Map<String, Double> scoresDictionary = new HashMap<String, Double>();

	public RecordManager() {
	}

	public Record analizza(String record) {
		Record result = null;
		if (record != null) {
			result = new Record();
			result.setContent(record);
			int indiceDelCampo = record.indexOf("type=");
			if (indiceDelCampo > 0) {
				result.setIndexType(indiceDelCampo);
				indiceDelCampo = indiceDelCampo + 5;
				String type = record.substring(indiceDelCampo, record.indexOf(" ", indiceDelCampo));
				result.setType(valueOf(type));
			} else {
				logger.warn("il record non contiene type=");
			}
		}
		return result;
	}

	public String trasformaFormatoPointConcept(Record record) {
		String out = "";
		String campiComuni = record.getContent().substring(0, record.getIndexType());
		String campiSpecifici = record.getContent().substring(record.getIndexType());
		String[] chiaviCampiSpecifici = { "type=", "ruleName=", "name=", "score=" };
		Map<String, String> informazioni = estraiInformazioni(campiSpecifici, chiaviCampiSpecifici);
		Double score = Double.valueOf(informazioni.get(chiaviCampiSpecifici[3]));
		String name = informazioni.get(chiaviCampiSpecifici[2]);
		Double delta = score - scoresDictionary.get(name);
		out = campiComuni + String.format("type=%s ruleName=\"%s\" name=\"%s\" deltaScore=%s score=%s",
				informazioni.get(chiaviCampiSpecifici[0]), informazioni.get(chiaviCampiSpecifici[1]),
				informazioni.get(chiaviCampiSpecifici[2]), delta, score);
		return out;
	}

	public String trasformaFormatoBadgeCollection(Record record) {
		String out = null;
		String campiComuni = record.getContent().substring(0, record.getIndexType());
		String campiSpecifici = record.getContent().substring(record.getIndexType());
		String[] chiaviCampiSpecifici = { "type=", "ruleName=", "name=", "badges=" };
		Map<String, String> informazioni = estraiInformazioni(campiSpecifici, chiaviCampiSpecifici);
		List<String> newBadges = trovaBadgeIniziali(informazioni.get(chiaviCampiSpecifici[2]),
				informazioni.get(chiaviCampiSpecifici[3]));
		out = campiComuni + String.format("type=%s ruleName=\"%s\" name=\"%s\" new_badge=\"%s\"",
				informazioni.get(chiaviCampiSpecifici[0]), informazioni.get(chiaviCampiSpecifici[1]),
				informazioni.get(chiaviCampiSpecifici[2]), newBadges.get(0));
		return out;
	}

	private Map<String, String> estraiInformazioni(String valoriSpecifici, String[] nomiCampi) {
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
				infos.put(nomiCampi[i],
						sanitizeString(valoriSpecifici.substring(indiceInizioValore, indiciCampi[i + 1])));
			} catch (ArrayIndexOutOfBoundsException e) {
				infos.put(nomiCampi[i], sanitizeString(valoriSpecifici.substring(indiceInizioValore)));
			}
		}

		return infos;
	}

	private String sanitizeString(String content) {
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

	private List<String> trovaBadgeIniziali(String badgeCollectionName, String actualBadges) {
		if (actualBadges.startsWith("[") && actualBadges.endsWith("]")) {
			actualBadges = actualBadges.replaceAll("\\[", "").replaceAll("\\]", "");
		}
		String[] vettore = actualBadges.split(",");
		List<String> newBadges = new ArrayList<>();
		for (String b : vettore) {
			newBadges.add(b.trim());
		}
		if (badgesDictionary.get(badgeCollectionName) != null) {
			logger.debug("valore per badgeCollection: " + badgeCollectionName + " - " + newBadges);
			logger.debug("Valore oldState: " + badgesDictionary.get(badgeCollectionName));
			newBadges = new ArrayList<String>(
					CollectionUtils.subtract(newBadges, badgesDictionary.get(badgeCollectionName)));
			logger.debug("nuovo badge: " + newBadges);
		} else {
			logger.debug("nessun valore per badgeCollection: " + badgeCollectionName);
		}
		return newBadges;
	}

	public String trasformaFormatoClassification(Record record) {
		String out = null;
		String campiComuni = record.getContent().substring(0, record.getIndexType());
		String campiSpecifici = record.getContent().substring(record.getIndexType());
		String[] chiaviCampiAction = { "type=", "action=", "internalData=", "oldState=" };
		Map<String, String> informazioni = estraiInformazioni(campiSpecifici, chiaviCampiAction);
		badgesDictionary = creaDizionarioBadges(informazioni.get(chiaviCampiAction[3]));
		scoresDictionary = creaDizionarioScore(informazioni.get(chiaviCampiAction[3]));
		if (logger.isDebugEnabled()) {
			logger.debug("oldState classification: " + informazioni.get(chiaviCampiAction[3]));
		}
		String internalDataContent = informazioni.get(chiaviCampiAction[2]);
		String classificationPosition = extractValue(internalDataContent, "position");
		String classificationName = sanitizeString(extractValue(internalDataContent, "name"));
		out = campiComuni + String.format("type=%s classificationName=\"%s\" classificationPosition=%s",
				informazioni.get(chiaviCampiAction[0]), classificationName, classificationPosition);

		logger.debug("il nuovo messaggio per Classification �: " + out);
		return out;
	}

	public String trasformaFormatoAction(Record record) {
		String out = null;
		String campiComuni = record.getContent().substring(0, record.getIndexType());
		String campiSpecifici = record.getContent().substring(record.getIndexType());
		String[] chiaviCampiAction = { "type=", "action=", "payload=", "oldState=" };
		Map<String, String> informazioni = estraiInformazioni(campiSpecifici, chiaviCampiAction);
		badgesDictionary = creaDizionarioBadges(informazioni.get(chiaviCampiAction[3]));
		scoresDictionary = creaDizionarioScore(informazioni.get(chiaviCampiAction[3]));
		logger.debug("dizionario score: " + scoresDictionary);
		out = campiComuni + String.format("type=%s actionName=\"%s\"", informazioni.get(chiaviCampiAction[0]),
				informazioni.get(chiaviCampiAction[1]));
		logger.debug("il nuovo messaggio per action e': " + out);
		return out;
	}

	private String extractValue(String jsonRepresentationContent, String fieldName) {
		JsonParser parser = new JsonParser();
		jsonRepresentationContent = puliziaJson(jsonRepresentationContent);
		JsonElement jsonElement = parser.parse(jsonRepresentationContent);
		return extractValue(jsonElement, fieldName);
	}

	private String extractValue(JsonElement element, String fieldName) {
		if (element.isJsonArray()) {
			for (JsonElement arrayElement : element.getAsJsonArray()) {
				String value = extractValue(arrayElement, fieldName);
				if (value != null) {
					return value;
				}
			}
		} else if (element.isJsonObject()) {
			JsonElement fieldValue = element.getAsJsonObject().get(fieldName);
			return fieldValue == null ? null : fieldValue.toString();
		} else {
			return null;
		}
		return null;
	}

	private Map<String, Double> creaDizionarioScore(String oldStateContent) {
		logger.debug("inizializzo dizionario per scores");
		Map<String, Double> dizionario = new HashMap<>();
		String json = puliziaJson(oldStateContent);
		JsonParser parser = new JsonParser();
		logger.debug("inizio parsing oldState");
		JsonArray jsonArray = parser.parse(json).getAsJsonArray();
		for (JsonElement element : jsonArray) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.get("score") != null) {
				String scoreName = obj.get("name").getAsString();
				logger.debug("nomeScore==" + scoreName);
				Double value = obj.get("score").getAsDouble();
				logger.debug("value: " + value);
				dizionario.put(scoreName, value);
			}
		}
		logger.debug("dizionario valori badges: " + badgesDictionary);
		return dizionario;
	}

	private Map<String, List<String>> creaDizionarioBadges(String oldStateContent) {
		logger.debug("inizializzo dizionario per badges");
		Map<String, List<String>> dizionario = new HashMap<>();
		String json = puliziaJson(oldStateContent);
		JsonParser parser = new JsonParser();
		logger.debug("inizio parsing oldState");
		JsonArray jsonArray = parser.parse(json).getAsJsonArray();
		for (JsonElement element : jsonArray) {
			JsonObject obj = element.getAsJsonObject();
			if (obj.get("badgeEarned") != null) {
				logger.debug("oggetto: " + obj.toString());
				String badgeCollectionName = obj.get("name").getAsString();
				logger.debug("nomeBadge==" + badgeCollectionName);
				JsonArray badges = obj.get("badgeEarned").getAsJsonArray();
				logger.debug("badges: " + badges.toString());
				List<String> listaValori = new ArrayList<String>();
				for (JsonElement badgeElement : badges) {
					listaValori.add(badgeElement.getAsString());
				}
				logger.debug("badges array size: " + listaValori.size());
				dizionario.put(badgeCollectionName, listaValori);
			}
		}
		logger.debug("dizionario valori badges: " + badgesDictionary);
		return dizionario;
	}

	private String puliziaJson(String content) {
		return StringEscapeUtils.unescapeJava(content);
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