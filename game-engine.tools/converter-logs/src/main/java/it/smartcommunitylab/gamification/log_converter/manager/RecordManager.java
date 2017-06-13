package it.smartcommunitylab.gamification.log_converter.manager;

import it.smartcommunitylab.gamification.log_converter.beans.Record;
import it.smartcommunitylab.gamification.log_converter.beans.RecordType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class RecordManager {

	// metodo per gson
	public String parse(String jsonLine) {
		JsonElement jelement = new JsonParser().parse(jsonLine);
		System.out.println("PARSE");
		// errore.../eccezione
		JsonObject jobject = jelement.getAsJsonObject();
		jobject = jobject.getAsJsonObject("data");
		JsonArray jarray = jobject.getAsJsonArray("translations");
		jobject = jarray.get(0).getAsJsonObject();
		String result = jobject.get("name").toString();
		System.out.println(result);
		return result;
	}

	private final static Logger logger = Logger.getLogger(RecordManager.class);

	Map<String, List<String>> dictionary = new HashMap<String, List<String>>();
	Gson gson = new GsonBuilder().create();

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

	@SuppressWarnings("null")
	public String analizzaAction(Record record) {
		String out = null;

		dictionary.clear();

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
		// /////////////////////////
		// MEMORIZZAZIONE VARIABILI
		// pulizia json
		// System.out.println("indice campi 3 : " + indiciCampi[3]);
		String json = info[3].substring(campi[3].length() + 1);// ,
		String tmpjson = "";
		// per rimuovere i backslash dal json
		for (char c : json.toCharArray()) {
			if (c != '\\') {
				tmpjson += c;
			}
		}
		json = tmpjson;
		// System.out.println("json passato: " + json);

		//
		JSONParser parser = new JSONParser();
		try {
			System.out.println("TRY");
			// non esegue la parse correttamente
			Object obj = parser.parse(json);
			// System.out.println("stato oggetto: " + obj);
			JSONArray array = (JSONArray) obj;

			for (int i = 0; i < array.size(); i++) {
				if (array.get(i).toString().contains("badgeEarned")) {
					System.out.println("oggetto: " + array.get(i));
					String splitArrayNome = array.get(i).toString().split(",")[0];
					// System.out.println("splitArraynome: " + splitArrayNome);
					// nome badge
					String nome = splitArrayNome.substring(splitArrayNome
							.indexOf("name") + 6);
					System.out.println("nomeBadge==" + nome);

					String splitArrayValore = array
							.get(i)
							.toString()
							.substring(
									array.get(i).toString()
											.indexOf("badgeEarned"),
									array.get(i).toString().indexOf("}"));
					System.out.println("splitArrayvalore: " + splitArrayValore);
					// nome badge
					String valore = splitArrayValore.substring(splitArrayValore
							.indexOf("badgeEarned") + 13);
					System.out.println("badgeEarned==" + valore);

					List<String> listaValori = new ArrayList<String>();

					int lang = valore.split(",").length;
					for (int j = 0; j < lang; j++) {

						if (lang == 1 && valore != null) {
							System.out.println(valore.substring(
									valore.indexOf("[") + 1,
									valore.indexOf("]")));
							listaValori.add(valore.substring(
									valore.indexOf("[") + 1,
									valore.indexOf("]")));
						} else {
							String nuoviValori = valore.substring(
									valore.indexOf("[") + 1,
									valore.indexOf("]"));
							if (nuoviValori != "" && nuoviValori != null
									&& nuoviValori.contains(",")) {
								System.out.println("nuovi val " + nuoviValori);

								String[] vettore = nuoviValori.split(",");

								vettore[j] = vettore[j].substring(vettore[j]
										.indexOf("\"") + 1);
								vettore[j] = vettore[j].substring(0,
										vettore[j].indexOf("\""));

								System.out.println("STAMPO: " + vettore[j]);
								// .substring(
								// 0,
								// vettore[j]
								// .indexOf("\"")));
								listaValori.add(vettore[j]);
								System.out.println("LISTT: "
										+ listaValori.get(0));
							}
						}
					}
					System.out.println("GET: " + listaValori.get(0));
					dictionary.put(nome, listaValori);
				}
			}
			System.out.println("STAMPO DIZIONARIO");
			System.out.println(dictionary);
			/*
			 * System.out.println(array.get(0));
			 * 
			 * JSONObject obj2 = (JSONObject) array.get(1);
			 * System.out.println("obj2: " + obj2);
			 * System.out.println("Field \"1\"");
			 * System.out.println(obj2.get("1"));
			 */
		} catch (ParseException pe) {
			System.out.println("position: " + pe.getPosition());
			System.out.println(pe);
		}

		JSONObject g = new JSONObject(dictionary);
		// System.out.println("g = " + g);

		/*
		 * try { // JSONObject jsonObject = new JSONObject(); JsonObject
		 * jsonObject = new JsonParser().parse(json) .getAsJsonObject();
		 * System.out.println(jsonObject); // String name = (String)
		 * jsonObject.get("name"); // JSONArray badgesList = (JSONArray)
		 * jsonObject.get("badgeEarned"); // System.out.println("Game: " +
		 * name); System.out.println("\nBadges:"); //
		 * 
		 * @SuppressWarnings("unchecked") // Iterator<String> iterator =
		 * badgesList.iterator(); // while (iterator.hasNext()) { //
		 * System.out.println(iterator.next()); // }
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		//

		// System.out.println("nuovo json: " + parse(test));// ,
		// "public transport aficionado"));
		// System.out.println("GSON : " + gson);

		// dictionary.put("key", "value");
		// String value = dictionary.get("key");
		// //////////////////////////////////////////////
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
