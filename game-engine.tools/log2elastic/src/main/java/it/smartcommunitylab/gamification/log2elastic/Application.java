package it.smartcommunitylab.gamification.log2elastic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Application {
	private static final String PREFIX_PROCESSED_FILE = "NEW-";

	static final String index = "gamefication_test";

	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		String logfolderPath = args[0];
		logger.debug("inizio newData");
		File folder = new File(logfolderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				logger.warn("E' presente una directory - name: "
						+ listOfFiles[i].getName());
			} else {
				if (isLogFileProcessato(listOfFiles[i])) {
					System.out.println("FILE: " + listOfFiles[i].getName());
					FileReader fr = null;
					BufferedReader br = null;
					fr = new FileReader(args[0] + "\\"
							+ listOfFiles[i].getName());
					System.out
							.println("file name: " + listOfFiles[i].getName());
					br = new BufferedReader(fr);

					String inputLine;
					String recordTrasformato = null;
					while ((inputLine = br.readLine()) != null) {
						Record record = analizza(inputLine);
						logger.debug("TYPE: " + record.getType());
						switch (record.getType()) {
						case ACTION:
							System.out.println("ACTION");

							analizzaAction(record);
							break;
						case RULE_POINTCONCEPT:
							System.out.println("RULE_POINTCONCEPT");

							analizzaPointconcept(record);
							break;
						case CLASSIFICATION:
							System.out.println("CLASSIFICATION");

							analizzaClassification(record);
							break;
						case RULE_BADGECOLLECTIONCONCEPT:
							System.out.println("RULE_BADGECOLLECTIONCONCEPT");

							analizzaBadgeCollectionConcept(record);
							break;
						case USERCREATION:
							System.out.println("USERCREATION");

							analizzaUserCreation(record);
							break;
						case CHALLENGECOMPLETE:
							System.out.println("CHALLENGECOMPLETE");

							analizzaChallengeComplete(record);
							break;
						case CHALLENGEASSIGNED:
							System.out.println("CHALLENGEASSIGNED");
							analizzaChallengeAssigned(record);
							break;
						default:
							System.out.println("non faccio nulla");

							recordTrasformato = record.getContent();
							break;
						}
					}
				}
				System.out.println("non entro!!");
			}
		}
	}

	private static boolean isLogFileProcessato(File logFile) {
		return logFile.getName().startsWith(PREFIX_PROCESSED_FILE);
	}

	public static Record analizza(String record) {
		Record result = null;
		if (record != null) {
			result = new Record();
			result.setContent(record);
			logger.debug("RECORD : " + record.toString());
			int indiceDelCampo = record.indexOf("type=");
			if (indiceDelCampo > 0) {
				result.setIndexType(indiceDelCampo);
				indiceDelCampo = indiceDelCampo + 5;
				logger.debug("indice del campo: " + indiceDelCampo);
				String type = null;
				if (record.indexOf(" ", indiceDelCampo) >= 0) {
					type = record.substring(indiceDelCampo,
							record.indexOf(" ", indiceDelCampo));
				} else {
					type = record.substring(indiceDelCampo, record.length());
				}
				logger.debug("type: " + type);
				result.setType(valueOf(type));
			} else {
				logger.debug("il record non contiene type=");
			}
		}
		return result;
	}

	public static void analizzaChallengeAssigned(Record record)
			throws IOException {
		logger.debug("INIZIO ChallengeAssigned");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=", "name=", "startDate=", "endDate=" };//
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		logger.debug("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);
		logger.debug("info: " + info[1]);
		postChallengeAssigned(campi, info, infoSpazi);
		logger.debug("out: " + out);
	}

	public static void postChallengeAssigned(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		logger.debug("POST");
		PostClass esempioPost = new PostClass();
		for (int i = 0; i < infoSpazi.length; i++) {
			logger.debug("info indice " + i + " = " + infoSpazi[i]);
		}
		for (int i = 0; i < info.length; i++) {
			logger.debug("info normale " + i + " = " + info[i]);
		}

		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);
		String name = info[1].substring(campi[1].length() + 1,
				info[1].length() - 2);
		name.replace("\"", "");
		String startDate = info[2].substring(campi[2].length(),
				info[2].length() - 1);
		String endDate = info[3].substring(campi[3].length(),
				info[3].length() - 1);
		String executionId = infoSpazi[4];
		logger.debug("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		logger.debug("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		logger.debug("gameId:" + gameId);
		String logLevel = "INFO -";
		logger.debug("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		logger.debug("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		logger.debug("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonChallengeAssigned(eventType, name,
				startDate, endDate, executionId, executionTime, gameId,
				logLevel, playerId, timestamp);
		String responsePost = esempioPost.post("http://localhost:9200/" + index
				+ "/ChallengeAssigned", json);
		logger.debug("json : " + json + "\n" + "response: " + responsePost);

	}

	public static void analizzaChallengeComplete(Record record)
			throws IOException {
		logger.debug("INIZIO ChallengeComplete");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=", "name=" };//
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		logger.debug("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);
		// logger.debug("info: " + info[1]);
		postChallengeComplete(campi, info, infoSpazi);
		logger.debug("out: " + out);
	}

	public static void postChallengeComplete(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		logger.debug("POST");
		PostClass esempioPost = new PostClass();
		for (int i = 0; i < infoSpazi.length; i++) {
			logger.debug("info indice " + i + " = " + infoSpazi[i]);
		}
		for (int i = 0; i < info.length; i++) {
			logger.debug("info normale " + i + " = " + info[i]);
		}

		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);
		String name = info[1].substring(campi[1].length() + 1,
				info[1].length() - 2);
		name.replace("\"", "");
		logger.debug("name GUARDA!: " + name);

		String executionId = infoSpazi[4];
		logger.debug("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		logger.debug("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		logger.debug("gameId:" + gameId);
		String logLevel = "INFO -";
		logger.debug("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		logger.debug("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		logger.debug("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonChallengeComplete(eventType, name,
				executionId, executionTime, gameId, logLevel, playerId,
				timestamp);
		String responsePost = esempioPost.post("http://localhost:9200/" + index
				+ "/ChallengeComplete", json);
		logger.debug("json : " + json + "\n" + "response: " + responsePost);

	}

	public static void analizzaUserCreation(Record record) throws IOException {
		logger.debug("INIZIO AD ANALIZZARE USER");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=" };//
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		logger.debug("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);

		postUserCreation(campi, info, infoSpazi);//
		logger.debug("out: " + out);
	}

	public static void postUserCreation(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		logger.debug("POST");
		PostClass esempioPost = new PostClass();

		for (int i = 0; i < infoSpazi.length; i++) {
			logger.debug("info indice " + i + " = " + infoSpazi[i]);
		}
		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);
		logger.debug("eventType: " + eventType);

		String executionId = infoSpazi[4];
		logger.debug("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		logger.debug("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		logger.debug("gameId:" + gameId);
		String logLevel = "INFO -";
		logger.debug("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		logger.debug("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		logger.debug("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonUsercreation(eventType,
				executionId, executionTime, gameId, logLevel, playerId,
				timestamp);
		String responsePost = esempioPost.post("http://localhost:9200/" + index
				+ "/UserCreation", json);
		logger.debug("json : " + json + "\n" + "response: " + responsePost);

	}

	public static void getMethod() throws IOException {
		logger.debug("GET");
		GetClass esempioGet = new GetClass();
		String responseGet = esempioGet.run("http://localhost:9200/" + index
				+ "/_search?q=*");
		logger.debug("response: " + responseGet);
	}

	public static void analizzaBadgeCollectionConcept(Record record)
			throws IOException {
		logger.debug("INIZIO AD ANALIZZARE BADGE COLLECTION");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=", "ruleName=", "name=", "new_badge=" };
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		logger.debug("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);

		postBadgeCollectionConcept(campi, info, infoSpazi);
		logger.debug("out: " + out);
	}

	public static void postBadgeCollectionConcept(String[] campi,
			String[] info, String[] infoSpazi) throws IOException {
		logger.debug("POST");
		PostClass esempioPost = new PostClass();

		for (int i = 0; i < infoSpazi.length; i++) {
			logger.debug("info indice " + i + " = " + infoSpazi[i]);
		}
		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);
		String ruleName = info[1].substring(campi[1].length(),
				info[1].length() - 1);
		String name = info[2].substring(campi[2].length());
		String new_badge = info[3].substring(campi[3].length());

		String executionId = infoSpazi[4];
		logger.debug("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		logger.debug("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		logger.debug("gameId:" + gameId);
		String logLevel = "INFO -";
		logger.debug("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		logger.debug("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		logger.debug("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonCollectionConcept(ruleName, name,
				new_badge, eventType, executionId, executionTime, gameId,
				logLevel, playerId, timestamp);
		String responsePost = esempioPost.post("http://localhost:9200/" + index
				+ "/BadgeCollectionConcept", json);
		logger.debug("json : " + json + "\n" + "response: " + responsePost);

	}

	public static void analizzaClassification(Record record) throws IOException {
		logger.debug("INIZIO AD ANALIZZARE Classification");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=", "classificationName=",
				"classificationPosition=" };
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		logger.debug("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);

		postClassification(campi, info, infoSpazi);
		logger.debug("out: " + out);
	}

	public static void postClassification(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		logger.debug("POST");
		PostClass esempioPost = new PostClass();

		for (int i = 0; i < infoSpazi.length; i++) {
			logger.debug("info indice " + i + " = " + infoSpazi[i]);
		}
		for (int i = 0; i < info.length; i++) {
			logger.debug("info NORMALE " + i + " = " + info[i]);
		}
		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);
		String classificationName = info[1].substring(campi[1].length(),
				info[1].length() - 1);
		String classificationPosition = info[2].substring(campi[2].length());
		String executionId = infoSpazi[4];
		logger.debug("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		logger.debug("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		logger.debug("gameId:" + gameId);
		String logLevel = "INFO -";
		logger.debug("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		logger.debug("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		logger.debug("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonClassification(
				classificationPosition, classificationName, eventType,
				executionId, executionTime, gameId, logLevel, playerId,
				timestamp);
		String responsePost = esempioPost.post("http://localhost:9200/" + index
				+ "/Classification", json);
		logger.debug("json : " + json + "\n" + "response: " + responsePost);

	}

	public static void analizzaPointconcept(Record record) throws IOException {
		logger.debug("INIZIO AD ANALIZZARE POINTCONCEPT");
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
		logger.debug("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);

		postPointConcept(campi, info, infoSpazi);
		logger.debug("out: " + out);
	}

	public static void postPointConcept(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		logger.debug("POST");
		PostClass esempioPost = new PostClass();

		for (int i = 0; i < infoSpazi.length; i++) {
			logger.debug("infoSpazi indice " + i + " = " + infoSpazi[i]);
		}
		for (int i = 0; i < info.length; i++) {
			logger.debug("infoNormale indice " + i + " = " + info[i]);
		}

		String name = info[2].substring(campi[2].length());
		String ruleName = info[1].substring(campi[1].length(),
				info[1].length() - 1);
		logger.debug("ruleName: " + ruleName);
		logger.debug("name: " + name);
		String score = info[3].substring(campi[3].length(),
				info[3].length() - 1);
		logger.debug("score: " + score);
		String deltaScore = info[4].substring(campi[4].length());
		logger.debug("deltascore: " + deltaScore);

		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);

		String executionId = infoSpazi[4];
		logger.debug("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		logger.debug("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		logger.debug("gameId:" + gameId);
		String logLevel = "INFO -";
		logger.debug("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		logger.debug("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		logger.debug("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonPointCeption(ruleName, name, score,
				deltaScore, eventType, executionId, executionTime, gameId,
				logLevel, playerId, timestamp);
		String responsePost = esempioPost.post("http://localhost:9200/" + index
				+ "/PointConcept", json);
		logger.debug("json : " + json + "\n" + "response: " + responsePost);

	}

	public static void analizzaAction(Record record) throws IOException {
		logger.debug("INIZIO AD ANALIZZARE ACTION");
		String out = null;
		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());
		splitXSpazi = splitXSpazi.replaceAll("\"", "");
		String[] campi = { "type=", "action=" };
		String[] info = estraiInformazioni(splitDiverso, campi);
		String[] infoSpazi = splitXSpazi.split(" ");
		logger.debug("info spazi: " + infoSpazi[0] + infoSpazi[1]
				+ infoSpazi[2] + " " + infoSpazi[3] + " " + infoSpazi[4]);

		postAction(campi, info, infoSpazi);
		logger.debug("out: " + out);
	}

	public static void postAction(String[] campi, String[] info,
			String[] infoSpazi) throws IOException {
		logger.debug("POST");
		PostClass esempioPost = new PostClass();

		for (int i = 0; i < infoSpazi.length; i++) {
			logger.debug("info indice " + i + " = " + infoSpazi[i]);
		}

		String actionName = info[1].substring(campi[1].length());
		String eventType = info[0].substring(campi[0].length(),
				info[0].length() - 1);
		String executionId = infoSpazi[4];
		logger.debug("executionId:" + executionId);
		String executionTime = infoSpazi[5];
		logger.debug("executionTime:" + executionTime);
		String gameId = infoSpazi[2];
		logger.debug("gameId:" + gameId);
		String logLevel = "INFO -";
		logger.debug("logLevel:" + logLevel);
		String playerId = infoSpazi[3];
		logger.debug("playerId:" + playerId);
		String timestamp = infoSpazi[6];
		logger.debug("timestamp:" + timestamp);

		String json = esempioPost.popolaJsonAction(actionName, eventType,
				executionId, executionTime, gameId, logLevel, playerId,
				timestamp);
		String responsePost = esempioPost.post("http://localhost:9200/" + index
				+ "/Action", json);
		logger.debug("json : " + json + "\n" + "response: " + responsePost);

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
			logger.debug("indice campii: " + indiciCampi[i]);
		}
		logger.debug("splitdiv: " + splitDiverso);

		for (int i = 0; i < campi.length; i++) {
			// coltrollo toglire ultimo spazio

			if (i < campi.length - 1) {
				info[i] = splitDiverso.substring(indiciCampi[i],
						indiciCampi[i + 1]);

			} else {
				info[i] = splitDiverso.substring(indiciCampi[i],
						splitDiverso.length());
			}
			logger.debug("COSA ESCE: " + info[i]);
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
		case "ChallengeAssigned":
			return RecordType.CHALLENGEASSIGNED;
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
