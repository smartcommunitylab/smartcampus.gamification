package it.smartcommunitylab.gamification.log2elastic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.analyzer.RecordAnalyzer;
import it.smartcommunitylab.gamification.log2elastic.analyzer.RecordAnalyzerFactory;

public class Application {
	private static final String PREFIX_PROCESSED_FILE = "NEW-";

	// private static final String INDEX_NAME = "new-stats-model";
	// private static final String ELASTIC_URL = "http://localhost:9200";

	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		String logfolderPath = args[0];
		logger.debug("stats processiong logs");
		File folder = new File(logfolderPath);
		File[] listOfFiles = folder.listFiles();
		ESHelper esHelper = new ESHelper();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].isDirectory()) {
				if (isLogFileProcessato(listOfFiles[i])) {
					logger.info("processing FILE: " + listOfFiles[i].getName());
					FileReader fr = null;
					BufferedReader br = null;
					fr = new FileReader(args[0] + "/" + listOfFiles[i].getName());
					br = new BufferedReader(fr);

					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						Record record = analizza(inputLine);
						logger.debug("Record type: " + record.getType());
						RecordAnalyzer analyzer = RecordAnalyzerFactory.getAnalyzer(record);
						Map<String, String> recordFields = analyzer.extractData();
						esHelper.saveRecord(record.getType(), recordFields.get("gameId"),
								Long.valueOf(recordFields.get("executionTime")), recordFields);
					}

					br.close();
				}
			}
		}

		logger.info("application end");
	}

	private static boolean isLogFileProcessato(File logFile) {
		return logFile.getName().startsWith(PREFIX_PROCESSED_FILE);
	}

	public static Record analizza(String record) {
		Record result = null;
		if (record != null) {
			result = new Record(record);
			logger.debug("RECORD : " + record.toString());
		}
		return result;
	}

	// public static void analizzaChallengeAssigned(Record record) throws
	// IOException {
	// logger.debug("INIZIO ChallengeAssigned");
	// String out = null;
	// String splitXSpazi = record.getContent().substring(0,
	// record.getIndexType());
	// String splitDiverso =
	// record.getContent().substring(record.getIndexType());
	// splitXSpazi = splitXSpazi.replaceAll("\"", "");
	// String[] campi = { "type=", "name=", "startDate=", "endDate=" };//
	// String[] info = estraiInformazioni(splitDiverso, campi);
	// String[] infoSpazi = splitXSpazi.split(" ");
	// logger.debug(
	// "info spazi: " + infoSpazi[0] + infoSpazi[1] + infoSpazi[2] + " " +
	// infoSpazi[3] + " " + infoSpazi[4]);
	// logger.debug("info: " + info[1]);
	// postChallengeAssigned(campi, info, infoSpazi);
	// logger.debug("out: " + out);
	// }

	// public static void postChallengeAssigned(String[] campi, String[] info,
	// String[] infoSpazi) throws IOException {
	// logger.debug("POST");
	// ESHelper esempioPost = new ESHelper();
	// for (int i = 0; i < infoSpazi.length; i++) {
	// logger.debug("info indice " + i + " = " + infoSpazi[i]);
	// }
	// for (int i = 0; i < info.length; i++) {
	// logger.debug("info normale " + i + " = " + info[i]);
	// }
	//
	// String eventType = info[0].substring(campi[0].length(), info[0].length()
	// - 1);
	// String name = info[1].substring(campi[1].length() + 1, info[1].length() -
	// 2);
	// name.replace("\"", "");
	// String startDate = info[2].substring(campi[2].length(), info[2].length()
	// - 1);
	// String endDate = info[3].substring(campi[3].length(), info[3].length() -
	// 1);
	// String executionId = infoSpazi[4];
	// logger.debug("executionId:" + executionId);
	// String executionTime = infoSpazi[5];
	// logger.debug("executionTime:" + executionTime);
	// String gameId = infoSpazi[2];
	// logger.debug("gameId:" + gameId);
	// String logLevel = "INFO -";
	// logger.debug("logLevel:" + logLevel);
	// String playerId = infoSpazi[3];
	// logger.debug("playerId:" + playerId);
	// String timestamp = infoSpazi[6];
	// logger.debug("timestamp:" + timestamp);
	//
	// String json = esempioPost.popolaJsonChallengeAssigned(eventType, name,
	// startDate, endDate, executionId,
	// executionTime, gameId, logLevel, playerId, timestamp);
	// String responsePost = esempioPost.pushToElastic(ELASTIC_URL + "/" +
	// INDEX_NAME + "/ChallengeAssigned", json);
	// logger.debug("json : " + json + "\n" + "response: " + responsePost);
	//
	// }

	// public static void analizzaChallengeComplete(Record record) throws
	// IOException {
	// logger.debug("INIZIO ChallengeComplete");
	// String out = null;
	// String splitXSpazi = record.getContent().substring(0,
	// record.getIndexType());
	// String splitDiverso =
	// record.getContent().substring(record.getIndexType());
	// splitXSpazi = splitXSpazi.replaceAll("\"", "");
	// String[] campi = { "type=", "name=" };//
	// String[] info = estraiInformazioni(splitDiverso, campi);
	// String[] infoSpazi = splitXSpazi.split(" ");
	// logger.debug(
	// "info spazi: " + infoSpazi[0] + infoSpazi[1] + infoSpazi[2] + " " +
	// infoSpazi[3] + " " + infoSpazi[4]);
	// // logger.debug("info: " + info[1]);
	// postChallengeComplete(campi, info, infoSpazi);
	// logger.debug("out: " + out);
	// }

	// public static void postChallengeComplete(String[] campi, String[] info,
	// String[] infoSpazi) throws IOException {
	// logger.debug("POST");
	// ESHelper esempioPost = new ESHelper();
	// for (int i = 0; i < infoSpazi.length; i++) {
	// logger.debug("info indice " + i + " = " + infoSpazi[i]);
	// }
	// for (int i = 0; i < info.length; i++) {
	// logger.debug("info normale " + i + " = " + info[i]);
	// }
	//
	// String eventType = info[0].substring(campi[0].length(), info[0].length()
	// - 1);
	// String name = info[1].substring(campi[1].length() + 1, info[1].length() -
	// 2);
	// name.replace("\"", "");
	// logger.debug("name GUARDA!: " + name);
	//
	// String executionId = infoSpazi[4];
	// logger.debug("executionId:" + executionId);
	// String executionTime = infoSpazi[5];
	// logger.debug("executionTime:" + executionTime);
	// String gameId = infoSpazi[2];
	// logger.debug("gameId:" + gameId);
	// String logLevel = "INFO -";
	// logger.debug("logLevel:" + logLevel);
	// String playerId = infoSpazi[3];
	// logger.debug("playerId:" + playerId);
	// String timestamp = infoSpazi[6];
	// logger.debug("timestamp:" + timestamp);
	//
	// String json = esempioPost.popolaJsonChallengeComplete(eventType, name,
	// executionId, executionTime, gameId,
	// logLevel, playerId, timestamp);
	// String responsePost = esempioPost.pushToElastic(ELASTIC_URL + "/" +
	// INDEX_NAME + "/ChallengeCompleted", json);
	// logger.debug("json : " + json + "\n" + "response: " + responsePost);
	//
	// }

	// public static void analizzaUserCreation(Record record) throws IOException
	// {
	// logger.debug("INIZIO AD ANALIZZARE USER");
	// String out = null;
	// String splitXSpazi = record.getContent().substring(0,
	// record.getIndexType());
	// String splitDiverso =
	// record.getContent().substring(record.getIndexType());
	// splitXSpazi = splitXSpazi.replaceAll("\"", "");
	// String[] campi = { "type=" };//
	// String[] info = estraiInformazioni(splitDiverso, campi);
	// String[] infoSpazi = splitXSpazi.split(" ");
	// logger.debug(
	// "info spazi: " + infoSpazi[0] + infoSpazi[1] + infoSpazi[2] + " " +
	// infoSpazi[3] + " " + infoSpazi[4]);
	//
	// postUserCreation(campi, info, infoSpazi);//
	// logger.debug("out: " + out);
	// }

	// public static void postUserCreation(String[] campi, String[] info,
	// String[] infoSpazi) throws IOException {
	// logger.debug("POST");
	// ESHelper esempioPost = new ESHelper();
	//
	// for (int i = 0; i < infoSpazi.length; i++) {
	// logger.debug("info indice " + i + " = " + infoSpazi[i]);
	// }
	// String eventType = info[0].substring(campi[0].length(), info[0].length()
	// - 1);
	// logger.debug("eventType: " + eventType);
	//
	// String executionId = infoSpazi[4];
	// logger.debug("executionId:" + executionId);
	// String executionTime = infoSpazi[5];
	// logger.debug("executionTime:" + executionTime);
	// String gameId = infoSpazi[2];
	// logger.debug("gameId:" + gameId);
	// String logLevel = "INFO -";
	// logger.debug("logLevel:" + logLevel);
	// String playerId = infoSpazi[3];
	// logger.debug("playerId:" + playerId);
	// String timestamp = infoSpazi[6];
	// logger.debug("timestamp:" + timestamp);
	//
	// String json = esempioPost.popolaJsonUsercreation(eventType, executionId,
	// executionTime, gameId, logLevel,
	// playerId, timestamp);
	// String responsePost = esempioPost.pushToElastic(ELASTIC_URL + "/" +
	// INDEX_NAME + "/UserCreation", json);
	// logger.debug("json : " + json + "\n" + "response: " + responsePost);
	//
	// }

	// public static void analizzaBadgeCollectionConcept(Record record) throws
	// IOException {
	// logger.debug("INIZIO AD ANALIZZARE BADGE COLLECTION");
	// String out = null;
	// String splitXSpazi = record.getContent().substring(0,
	// record.getIndexType());
	// String splitDiverso =
	// record.getContent().substring(record.getIndexType());
	// splitXSpazi = splitXSpazi.replaceAll("\"", "");
	// String[] campi = { "type=", "ruleName=", "name=", "new_badge=" };
	// String[] info = estraiInformazioni(splitDiverso, campi);
	// String[] infoSpazi = splitXSpazi.split(" ");
	// logger.debug(
	// "info spazi: " + infoSpazi[0] + infoSpazi[1] + infoSpazi[2] + " " +
	// infoSpazi[3] + " " + infoSpazi[4]);
	//
	// postBadgeCollectionConcept(campi, info, infoSpazi);
	// logger.debug("out: " + out);
	// }

	// public static void postBadgeCollectionConcept(String[] campi, String[]
	// info, String[] infoSpazi)
	// throws IOException {
	// logger.debug("POST");
	// ESHelper esempioPost = new ESHelper();
	//
	// for (int i = 0; i < infoSpazi.length; i++) {
	// logger.debug("info indice " + i + " = " + infoSpazi[i]);
	// }
	// String eventType = info[0].substring(campi[0].length(), info[0].length()
	// - 1);
	// String ruleName = info[1].substring(campi[1].length(), info[1].length() -
	// 1);
	// String name = info[2].substring(campi[2].length());
	// String new_badge = info[3].substring(campi[3].length());
	//
	// String executionId = infoSpazi[4];
	// logger.debug("executionId:" + executionId);
	// String executionTime = infoSpazi[5];
	// logger.debug("executionTime:" + executionTime);
	// String gameId = infoSpazi[2];
	// logger.debug("gameId:" + gameId);
	// String logLevel = "INFO -";
	// logger.debug("logLevel:" + logLevel);
	// String playerId = infoSpazi[3];
	// logger.debug("playerId:" + playerId);
	// String timestamp = infoSpazi[6];
	// logger.debug("timestamp:" + timestamp);
	//
	// String json = esempioPost.popolaJsonCollectionConcept(ruleName, name,
	// new_badge, eventType, executionId,
	// executionTime, gameId, logLevel, playerId, timestamp);
	// String responsePost = esempioPost.pushToElastic(ELASTIC_URL + "/" +
	// INDEX_NAME + "/BadgeCollectionConcept",
	// json);
	// logger.debug("json : " + json + "\n" + "response: " + responsePost);
	//
	// }

	// public static void analizzaClassification(Record record) throws
	// IOException {
	// logger.debug("INIZIO AD ANALIZZARE Classification");
	// String out = null;
	// String splitXSpazi = record.getContent().substring(0,
	// record.getIndexType());
	// String splitDiverso =
	// record.getContent().substring(record.getIndexType());
	// splitXSpazi = splitXSpazi.replaceAll("\"", "");
	// String[] campi = { "type=", "classificationName=",
	// "classificationPosition=" };
	// String[] info = estraiInformazioni(splitDiverso, campi);
	// String[] infoSpazi = splitXSpazi.split(" ");
	// logger.debug(
	// "info spazi: " + infoSpazi[0] + infoSpazi[1] + infoSpazi[2] + " " +
	// infoSpazi[3] + " " + infoSpazi[4]);
	//
	// postClassification(campi, info, infoSpazi);
	// logger.debug("out: " + out);
	// }

	// public static void postClassification(String[] campi, String[] info,
	// String[] infoSpazi) throws IOException {
	// logger.debug("POST");
	// ESHelper esempioPost = new ESHelper();
	//
	// for (int i = 0; i < infoSpazi.length; i++) {
	// logger.debug("info indice " + i + " = " + infoSpazi[i]);
	// }
	// for (int i = 0; i < info.length; i++) {
	// logger.debug("info NORMALE " + i + " = " + info[i]);
	// }
	// String eventType = info[0].substring(campi[0].length(), info[0].length()
	// - 1);
	// String classificationName = info[1].substring(campi[1].length(),
	// info[1].length() - 1);
	// String classificationPosition = info[2].substring(campi[2].length());
	// String executionId = infoSpazi[4];
	// logger.debug("executionId:" + executionId);
	// String executionTime = infoSpazi[5];
	// logger.debug("executionTime:" + executionTime);
	// String gameId = infoSpazi[2];
	// logger.debug("gameId:" + gameId);
	// String logLevel = "INFO -";
	// logger.debug("logLevel:" + logLevel);
	// String playerId = infoSpazi[3];
	// logger.debug("playerId:" + playerId);
	// String timestamp = infoSpazi[6];
	// logger.debug("timestamp:" + timestamp);
	//
	// String json =
	// esempioPost.popolaJsonClassification(classificationPosition,
	// classificationName, eventType,
	// executionId, executionTime, gameId, logLevel, playerId, timestamp);
	// String responsePost = esempioPost.pushToElastic(ELASTIC_URL + "/" +
	// INDEX_NAME + "/Classification", json);
	// logger.debug("json : " + json + "\n" + "response: " + responsePost);
	//
	// }

	// public static void analizzaPointconcept(Record record) throws IOException
	// {
	// logger.debug("INIZIO AD ANALIZZARE POINTCONCEPT");
	// String out = null;
	// String splitXSpazi = record.getContent().substring(0,
	// record.getIndexType());
	// String splitDiverso =
	// record.getContent().substring(record.getIndexType());
	// splitXSpazi = splitXSpazi.replaceAll("\"", "");
	// String[] campi = { "type=", "ruleName=", "name=", "score=", "deltaScore="
	// };
	// String[] info = estraiInformazioni(splitDiverso, campi);
	// String[] infoSpazi = splitXSpazi.split(" ");
	// logger.debug(
	// "info spazi: " + infoSpazi[0] + infoSpazi[1] + infoSpazi[2] + " " +
	// infoSpazi[3] + " " + infoSpazi[4]);
	//
	// postPointConcept(campi, info, infoSpazi);
	// logger.debug("out: " + out);
	// }

	// public static void postPointConcept(String[] campi, String[] info,
	// String[] infoSpazi) throws IOException {
	// logger.debug("POST");
	// ESHelper esempioPost = new ESHelper();
	//
	// for (int i = 0; i < infoSpazi.length; i++) {
	// logger.debug("infoSpazi indice " + i + " = " + infoSpazi[i]);
	// }
	// for (int i = 0; i < info.length; i++) {
	// logger.debug("infoNormale indice " + i + " = " + info[i]);
	// }
	//
	// String name = info[2].substring(campi[2].length());
	// String ruleName = info[1].substring(campi[1].length(), info[1].length() -
	// 1);
	// logger.debug("ruleName: " + ruleName);
	// logger.debug("name: " + name);
	// String score = info[3].substring(campi[3].length(), info[3].length() -
	// 1);
	// logger.debug("score: " + score);
	// String deltaScore = info[4].substring(campi[4].length());
	// logger.debug("deltascore: " + deltaScore);
	//
	// String eventType = info[0].substring(campi[0].length(), info[0].length()
	// - 1);
	//
	// String executionId = infoSpazi[4];
	// logger.debug("executionId:" + executionId);
	// String executionTime = infoSpazi[5];
	// logger.debug("executionTime:" + executionTime);
	// String gameId = infoSpazi[2];
	// logger.debug("gameId:" + gameId);
	// String logLevel = "INFO -";
	// logger.debug("logLevel:" + logLevel);
	// String playerId = infoSpazi[3];
	// logger.debug("playerId:" + playerId);
	// String timestamp = infoSpazi[6];
	// logger.debug("timestamp:" + timestamp);
	//
	// String json = esempioPost.popolaJsonPointCeption(ruleName, name, score,
	// deltaScore, eventType, executionId,
	// executionTime, gameId, logLevel, playerId, timestamp);
	// String responsePost = esempioPost.pushToElastic(ELASTIC_URL + "/" +
	// INDEX_NAME + "/PointConcept", json);
	// logger.debug("json : " + json + "\n" + "response: " + responsePost);
	//
	// }

	// public static void analizzaAction(Record record) throws IOException {
	// logger.debug("INIZIO AD ANALIZZARE ACTION");
	// String out = null;
	// String splitXSpazi = record.getContent().substring(0,
	// record.getIndexType());
	// String splitDiverso =
	// record.getContent().substring(record.getIndexType());
	// splitXSpazi = splitXSpazi.replaceAll("\"", "");
	// String[] campi = { "type=", "action=" };
	// String[] info = estraiInformazioni(splitDiverso, campi);
	// String[] infoSpazi = splitXSpazi.split(" ");
	// logger.debug(
	// "info spazi: " + infoSpazi[0] + infoSpazi[1] + infoSpazi[2] + " " +
	// infoSpazi[3] + " " + infoSpazi[4]);
	//
	// postAction(campi, info, infoSpazi);
	// logger.debug("out: " + out);
	// }

	// public static void postAction(String[] campi, String[] info, String[]
	// infoSpazi) throws IOException {
	// logger.debug("POST");
	// ESHelper esempioPost = new ESHelper();
	//
	// if (logger.isDebugEnabled()) {
	// for (int i = 0; i < infoSpazi.length; i++) {
	// logger.debug("info indice " + i + " = " + infoSpazi[i]);
	// }
	// }
	//
	// String actionName = info[1].substring(campi[1].length());
	// String eventType = info[0].substring(campi[0].length(), info[0].length()
	// - 1);
	// String executionId = infoSpazi[4];
	// logger.debug("executionId:" + executionId);
	// String executionTime = infoSpazi[5];
	// logger.debug("executionTime:" + executionTime);
	// String gameId = infoSpazi[2];
	// logger.debug("gameId:" + gameId);
	// String logLevel = "INFO -";
	// logger.debug("logLevel:" + logLevel);
	// String playerId = infoSpazi[3];
	// logger.debug("playerId:" + playerId);
	// String timestamp = infoSpazi[6];
	// logger.debug("timestamp:" + timestamp);
	//
	// String json = esempioPost.popolaJsonAction(actionName, eventType,
	// executionId, executionTime, gameId, logLevel,
	// playerId, timestamp);
	// String responsePost = esempioPost.pushToElastic(ELASTIC_URL + "/" +
	// INDEX_NAME + "/Action", json);
	// logger.debug("json : " + json + "\n" + "response: " + responsePost);
	//
	// }

	// private static String[] estraiInformazioni(String splitDiverso, String[]
	// campi) {
	// int[] indiciCampi = new int[campi.length];
	// int[] indiciInformazioni = new int[campi.length];
	// String[] info = new String[campi.length];
	// for (int i = 0; i < campi.length; i++) {
	// if (splitDiverso.contains(campi[i])) {
	// indiciCampi[i] = splitDiverso.indexOf(campi[i]);
	// indiciInformazioni[i] = indiciCampi[i] + campi[i].length();
	// }
	// }
	// for (int i = 0; i < campi.length; i++) {
	// logger.debug("indice campii: " + indiciCampi[i]);
	// }
	// logger.debug("splitdiv: " + splitDiverso);
	//
	// for (int i = 0; i < campi.length; i++) {
	// // coltrollo toglire ultimo spazio
	//
	// if (i < campi.length - 1) {
	// info[i] = splitDiverso.substring(indiciCampi[i], indiciCampi[i + 1]);
	//
	// } else {
	// info[i] = splitDiverso.substring(indiciCampi[i], splitDiverso.length());
	// }
	// logger.debug("COSA ESCE: " + info[i]);
	// }
	// return info;
	// }

}
