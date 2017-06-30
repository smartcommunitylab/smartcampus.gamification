package it.smartcommunitylab.gamification.log2elastic;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostClass {

	public static final MediaType JSON = MediaType
			.parse("application/json; charset=utf-8");

	OkHttpClient client = new OkHttpClient();

	String post(String url, String json) throws IOException {
		RequestBody body = RequestBody.create(JSON, json);
		Request request = new Request.Builder().url(url).post(body).build();
		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	String popolaJsonChallengeAssigned(String eventType, String name,
			String startDate, String endDate, String executionId,
			String executionTime, String gameId, String logLevel,
			String playerId, String timestamp) {

		return "{\"eventType\":" + "\"" + eventType + "\"," + "\"nome\":"
				+ "\"" + name + "\"," + "\"startDate\":" + startDate + ","
				+ "\"endDate\":" + endDate + "," + "\"executionId\":" + "\""
				+ executionId + "\"," + "\"executionTime\":" + executionTime
				+ "," + "\"gameId\":" + "\"" + gameId + "\"" + ","
				+ "\"logLevel\":" + "\"" + logLevel + "\"," + "\"playerId\":"
				+ "\"" + playerId + "\"," + "\"timestamp\":" + timestamp + "}";

	}

	String popolaJsonChallengeComplete(String eventType, String name,
			String executionId, String executionTime, String gameId,
			String logLevel, String playerId, String timestamp) {

		return "{\"eventType\":" + "\"" + eventType + "\"," + "\"nome\":"
				+ "\"" + name + "\"," + "\"executionId\":" + "\"" + executionId
				+ "\"," + "\"executionTime\":" + executionTime + ","
				+ "\"gameId\":" + "\"" + gameId + "\"" + "," + "\"logLevel\":"
				+ "\"" + logLevel + "\"," + "\"playerId\":" + "\"" + playerId
				+ "\"," + "\"timestamp\":" + timestamp + "}";

	}

	String popolaJsonUsercreation(String eventType, String executionId,
			String executionTime, String gameId, String logLevel,
			String playerId, String timestamp) {

		return "{\"eventType\":" + "\"" + eventType + "\","
				+ "\"executionId\":" + "\"" + executionId + "\","
				+ "\"executionTime\":" + executionTime + "," + "\"gameId\":"
				+ "\"" + gameId + "\"" + "," + "\"logLevel\":" + "\""
				+ logLevel + "\"," + "\"playerId\":" + "\"" + playerId + "\","
				+ "\"timestamp\":" + timestamp + "}";

	}

	String popolaJsonCollectionConcept(String ruleName, String name,
			String new_badge, String eventType, String executionId,
			String executionTime, String gameId, String logLevel,
			String playerId, String timestamp) {

		return "{\"ruleName\":" + ruleName + "," + "\"name\":" + name + ","
				+ "\"new_badge\":" + new_badge + "," + "\"eventType\":" + "\""
				+ eventType + "\"," + "\"executionId\":" + "\"" + executionId
				+ "\"," + "\"executionTime\":" + executionTime + ","
				+ "\"gameId\":" + "\"" + gameId + "\"" + "," + "\"logLevel\":"
				+ "\"" + logLevel + "\"," + "\"playerId\":" + "\"" + playerId
				+ "\"," + "\"timestamp\":" + timestamp + "}";

	}

	String popolaJsonClassification(String classificationName,
			String classificationPosition, String eventType,
			String executionId, String executionTime, String gameId,
			String logLevel, String playerId, String timestamp) {

		return "{\"classificationName\":" + classificationName + ","
				+ "\"classificationPosition\":" + classificationPosition + ","
				+ "\"eventType\":" + "\"" + eventType + "\","
				+ "\"executionId\":" + "\"" + executionId + "\","
				+ "\"executionTime\":" + executionTime + "," + "\"gameId\":"
				+ "\"" + gameId + "\"" + "," + "\"logLevel\":" + "\""
				+ logLevel + "\"," + "\"playerId\":" + "\"" + playerId + "\","
				+ "\"timestamp\":" + timestamp + "}";

	}

	String popolaJsonAction(String actionName, String eventType,
			String executionId, String executionTime, String gameId,
			String logLevel, String playerId, String timestamp) {
		// JsonParser parser = new JsonParser();
		// JsonObject o = parser.parse(
		/*
		 * "{\"actionName\": \" " + actionName + " \"," + "\"eventType\": \" " +
		 * eventType + " \", \"executionId\": \" " + executionId +
		 * "\",\"executionTime\": \" " + executionTime + "\"\"gameId\": \" " +
		 * gameId + "\",\"logLevel\": \" " + logLevel + "\",\"playerId\": \" " +
		 * playerId + "\",\"timestamp\": \" " + timestamp + "\"}")
		 * .getAsJsonObject();
		 */
		// System.out.println("json:" + o);

		return "{\"actionName\":" + actionName + "," + "\"eventType\":" + "\""
				+ eventType + "\"," + "\"executionId\":" + "\"" + executionId
				+ "\"," + "\"executionTime\":" + executionTime + ","
				+ "\"gameId\":" + "\"" + gameId + "\"" + "," + "\"logLevel\":"
				+ "\"" + logLevel + "\"," + "\"playerId\":" + "\"" + playerId
				+ "\"," + "\"timestamp\":" + timestamp + "}";

	}

	String popolaJsonPointCeption(String ruleName, String conceptName,
			String score, String deltaScore, String eventType,
			String executionId, String executionTime, String gameId,
			String logLevel, String playerId, String timestamp) {

		return "{\"ruleName\":" + ruleName + "," + "\"conceptName\":" + ""
				+ conceptName + "," + "\"score\":" + "\"" + score + "\","
				+ "\"deltaScore\":" + "\"" + deltaScore + "\","
				+ "\"eventType\":" + "\"" + eventType + "\","
				+ "\"executionId\":" + "\"" + executionId + "\","
				+ "\"executionTime\":" + executionTime + "," + "\"gameId\":"
				+ "\"" + gameId + "\"" + "," + "\"logLevel\":" + "\""
				+ logLevel + "\"," + "\"playerId\":" + "\"" + playerId + "\","
				+ "\"timestamp\":" + timestamp + "}";

	}

	public static void main(String[] args) throws IOException {
	}
}
