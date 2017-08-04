package it.smartcommunitylab.gamification.log2elastic;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ESHelper {

	public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

	private static final Logger logger = Logger.getLogger(ESHelper.class);
	static OkHttpClient client;
	static {
		client = new OkHttpClient();
	}

	private static final String ELASTIC_URL = "http://localhost:9200";
	private static final String INDEX_PREFIX = "gamification-stats-";

	private String pushToElastic(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		try (Response response = client.newCall(request).execute()) {
			if (!response.isSuccessful()) {
				logger.warn("Record not corrected pushed in elasticsearch: " + json);
			}
			String out = response.body().string();
			response.close();
			return out;
		}

	}

	private String pushToElastic(String url, Map<String, String> fields) throws IOException {
		String json = toJsonString(fields);
		return pushToElastic(url, json);
	}

	private String getIndexName(String gameId, long timestamp) {
		String indexSuffix = DateTimeFormatter.ofPattern("YYYY-MM-'w'w").withZone(ZoneOffset.systemDefault())
				.format(Instant.ofEpochMilli(timestamp));
		return INDEX_PREFIX + gameId + "-" + indexSuffix;

	}

	// String popolaJsonChallengeAssigned(String eventType, String name, String
	// startDate, String endDate,
	// String executionId, String executionTime, String gameId, String logLevel,
	// String playerId,
	// String timestamp) {
	//
	// return "{\"eventType\":" + "\"" + eventType + "\"," + "\"nome\":" + "\""
	// + name + "\"," + "\"startDate\":"
	// + startDate + "," + "\"endDate\":" + endDate + "," + "\"executionId\":" +
	// "\"" + executionId + "\","
	// + "\"executionTime\":" + executionTime + "," + "\"gameId\":" + "\"" +
	// gameId + "\"" + ","
	// + "\"logLevel\":" + "\"" + logLevel + "\"," + "\"playerId\":" + "\"" +
	// playerId + "\","
	// + "\"timestamp\":" + timestamp + "}";
	//
	// }

	// String popolaJsonChallengeComplete(String eventType, String name, String
	// executionId, String executionTime,
	// String gameId, String logLevel, String playerId, String timestamp) {
	//
	// return "{\"eventType\":" + "\"" + eventType + "\"," + "\"nome\":" + "\""
	// + name + "\"," + "\"executionId\":"
	// + "\"" + executionId + "\"," + "\"executionTime\":" + executionTime + ","
	// + "\"gameId\":" + "\""
	// + gameId + "\"" + "," + "\"logLevel\":" + "\"" + logLevel + "\"," +
	// "\"playerId\":" + "\"" + playerId
	// + "\"," + "\"timestamp\":" + timestamp + "}";
	//
	// }

	// String popolaJsonUsercreation(String eventType, String executionId,
	// String executionTime, String gameId,
	// String logLevel, String playerId, String timestamp) {
	//
	// return "{\"eventType\":" + "\"" + eventType + "\"," + "\"executionId\":"
	// + "\"" + executionId + "\","
	// + "\"executionTime\":" + executionTime + "," + "\"gameId\":" + "\"" +
	// gameId + "\"" + ","
	// + "\"logLevel\":" + "\"" + logLevel + "\"," + "\"playerId\":" + "\"" +
	// playerId + "\","
	// + "\"timestamp\":" + timestamp + "}";
	//
	// }

	// String popolaJsonCollectionConcept(String ruleName, String name, String
	// new_badge, String eventType,
	// String executionId, String executionTime, String gameId, String logLevel,
	// String playerId,
	// String timestamp) {
	//
	// return "{\"ruleName\":" + ruleName + "," + "\"name\":" + name + "," +
	// "\"new_badge\":" + new_badge + ","
	// + "\"eventType\":" + "\"" + eventType + "\"," + "\"executionId\":" + "\""
	// + executionId + "\","
	// + "\"executionTime\":" + executionTime + "," + "\"gameId\":" + "\"" +
	// gameId + "\"" + ","
	// + "\"logLevel\":" + "\"" + logLevel + "\"," + "\"playerId\":" + "\"" +
	// playerId + "\","
	// + "\"timestamp\":" + timestamp + "}";
	//
	// }

	// String popolaJsonClassification(String classificationName, String
	// classificationPosition, String eventType,
	// String executionId, String executionTime, String gameId, String logLevel,
	// String playerId,
	// String timestamp) {
	//
	// return "{\"classificationName\":" + classificationName + "," +
	// "\"classificationPosition\":"
	// + classificationPosition + "," + "\"eventType\":" + "\"" + eventType +
	// "\"," + "\"executionId\":" + "\""
	// + executionId + "\"," + "\"executionTime\":" + executionTime + "," +
	// "\"gameId\":" + "\"" + gameId
	// + "\"" + "," + "\"logLevel\":" + "\"" + logLevel + "\"," +
	// "\"playerId\":" + "\"" + playerId + "\","
	// + "\"timestamp\":" + timestamp + "}";
	//
	// }

	// String popolaJsonAction(String actionName, String eventType, String
	// executionId, String executionTime,
	// String gameId, String logLevel, String playerId, String timestamp) {
	//
	// return "{\"actionName\":" + actionName + "," + "\"eventType\":" + "\"" +
	// eventType + "\"," + "\"executionId\":"
	// + "\"" + executionId + "\"," + "\"executionTime\":" + executionTime + ","
	// + "\"gameId\":" + "\""
	// + gameId + "\"" + "," + "\"logLevel\":" + "\"" + logLevel + "\"," +
	// "\"playerId\":" + "\"" + playerId
	// + "\"," + "\"timestamp\":" + timestamp + "}";
	//
	// }

	// String popolaJsonPointCeption(String ruleName, String conceptName, String
	// score, String deltaScore,
	// String eventType, String executionId, String executionTime, String
	// gameId, String logLevel, String playerId,
	// String timestamp) {
	//
	// return "{\"ruleName\":" + ruleName + "," + "\"conceptName\":" + "" +
	// conceptName + "," + "\"score\":" + "\""
	// + score + "\"," + "\"deltaScore\":" + "\"" + deltaScore + "\"," +
	// "\"eventType\":" + "\"" + eventType
	// + "\"," + "\"executionId\":" + "\"" + executionId + "\"," +
	// "\"executionTime\":" + executionTime + ","
	// + "\"gameId\":" + "\"" + gameId + "\"" + "," + "\"logLevel\":" + "\"" +
	// logLevel + "\","
	// + "\"playerId\":" + "\"" + playerId + "\"," + "\"timestamp\":" +
	// timestamp + "}";
	//
	// }

	public void saveRecord(RecordType recordType, String gameId, long creationTimestamp,
			Map<String, String> recordFields) throws IOException {
		pushToElastic(
				ELASTIC_URL + "/" + getIndexName(gameId, creationTimestamp) + "/" + recordType.getRepresentation(),
				recordFields);
	}

	private String toJsonString(Map<String, String> fields) {
		Gson gson = new Gson();
		String jsonContent = gson.toJson(fields);
		return jsonContent;
	}

}
