package it.smartcommunitylab.gamification.log2elastic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Application {

	public static void main(String[] args) throws IOException {

		// ----------
		FileReader fr = null;
		BufferedReader br = null;
		fr = new FileReader(args[0] + "\\"
				+ "NEW-gamification.stats.log.2016-10-29");// CAMBIO!!
		br = new BufferedReader(fr);

		String inputLine;
		String recordTrasformato = null;
		while ((inputLine = br.readLine()) != null) {
			Record record = analizza(inputLine);
			System.out.println("TYPE: " + record.getType());
			switch (record.getType()) {
			case ACTION:
				analizzaAction(record);
				break;
			case RULE_POINTCONCEPT:
				analizzaPointconcept(record);
				/*
				 * case CLASSIFICATION: recordTrasformato =
				 * recordManager.analizzaClassification(record); break; case
				 * RULE_BADGECOLLECTIONCONCEPT: recordTrasformato =
				 * recordManager.analizzaBadgeCollection(record); break; case
				 * RULE_POINTCONCEPT: recordTrasformato =
				 * recordManager.analizzaPointConcept(record); break;
				 */
			default:
				recordTrasformato = record.getContent();
				break;
			}
		}
	}

	public static Record analizza(String record) {
		Record result = null;
		if (record != null) {
			result = new Record();
			result.setContent(record);
			System.out.println("RECORD : " + record.toString());
			int indiceDelCampo = record.indexOf("type=");
			if (indiceDelCampo > 0) {
				result.setIndexType(indiceDelCampo);
				indiceDelCampo = indiceDelCampo + 5;
				String type = record.substring(indiceDelCampo,
						record.indexOf(" ", indiceDelCampo));
				result.setType(valueOf(type));
			} else {
				System.out.println("il record non contiene type=");
			}
		}
		return result;
	}

	public static void analizzaPointconcept(Record record) throws IOException {
		System.out.println("INIZIO AD ANALIZZARE POINTCONCEPT");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=", "ruleName=", "name=", "score=",
				"deltaScore=" };
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		System.out.println("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);

		postPointConcept(campi, info, infoSpazi);
		System.out.println("out: " + out);
	}

	public static void postPointConcept(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		System.out.println("POST");
		PostClass esempioPost = new PostClass();

		for (int i = 0; i < infoSpazi.length; i++) {
			System.out.println("infoSpazi indice " + i + " = " + infoSpazi[i]);
		}
		for (int i = 0; i < info.length; i++) {
			System.out.println("infoNormale indice " + i + " = " + info[i]);
		}

		String name = info[2].substring(campi[2].length());
		String ruleName = info[1].substring(campi[1].length(),
				info[1].length() - 1);
		System.out.println("ruleName: " + ruleName);
		System.out.println("name: " + name);
		String score = info[3].substring(campi[3].length(),
				info[3].length() - 1);
		System.out.println("score: " + score);
		String deltaScore = info[4].substring(campi[4].length());
		System.out.println("deltascore: " + deltaScore);

		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);

		String executionId = infoSpazi[4];
		System.out.println("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		System.out.println("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		System.out.println("gameId:" + gameId);
		String logLevel = "INFO -";
		System.out.println("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		System.out.println("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		System.out.println("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonPointCeption(ruleName, name, score,
				deltaScore, eventType, executionId, executionTime, gameId,
				logLevel, playerId, timestamp);
		String responsePost = esempioPost.post(
				"http://localhost:9200/gamefication1/PointConcept", json);
		System.out.println("json : " + json + "\n" + "response: "
				+ responsePost);

		System.out.println("GET");
		GetClass esempioGet = new GetClass();
		String responseGet = esempioGet
				.run("http://localhost:9200/gamefication1/_search?q=*");
		System.out.println("response: " + responseGet);
	}

	public static void analizzaAction(Record record) throws IOException {
		System.out.println("INIZIO AD ANALIZZARE ACTION");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=", "action=" };
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		System.out.println("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);

		postAction(campi, info, infoSpazi);
		System.out.println("out: " + out);
	}

	public static void postAction(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		System.out.println("POST");
		PostClass esempioPost = new PostClass();

		for (int i = 0; i < infoSpazi.length; i++) {
			System.out.println("info indice " + i + " = " + infoSpazi[i]);
		}

		String actionName = info[1].substring(campi[1].length());
		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);
		String executionId = infoSpazi[4];
		System.out.println("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		System.out.println("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		System.out.println("gameId:" + gameId);
		String logLevel = "INFO -";
		System.out.println("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		System.out.println("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		System.out.println("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonAction(actionName, eventType,
				executionId, executionTime, gameId, logLevel, playerId,
				timestamp);
		String responsePost = esempioPost.post(
				"http://localhost:9200/gamefication1/Action", json);
		System.out.println("json : " + json + "\n" + "response: "
				+ responsePost);

		System.out.println("GET");
		GetClass esempioGet = new GetClass();
		String responseGet = esempioGet
				.run("http://localhost:9200/gamefication1/_search?q=*");
		System.out.println("response: " + responseGet);
	}

	private static String[] estraiInformazioni(String splitDiverso,
			String[] campi) {
		int[] indiciCampi = new int[campi.length];
		int[] indiciInformazioni = new int[campi.length];
		String[] info = new String[campi.length];
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
						splitDiverso.length());
			}
		}
		return info;
	}

	private static RecordType valueOf(String type) {
		switch (type) {
		case "Action":
			return RecordType.ACTION;
		case "PointConcept":
			return RecordType.RULE_POINTCONCEPT;
		case "BadgeCollectionConcept":
			return RecordType.RULE_BADGECOLLECTIONCONCEPT;
		case "Classification":
			return RecordType.CLASSIFICATION;
		case "ChallengeComplete":
			return RecordType.CHALLENGECOMPLETE;
		case "UserCreation":
			return RecordType.USERCREATION;
		default:
			throw new IllegalArgumentException(type + " non � supportato");
		}
	}
	// private String puliziaJson(String[] campi, String[] info) {
	// return StringEscapeUtils.unescapeJava(info[3].substring(campi[3]
	// .length() + 1));
	// }

}
