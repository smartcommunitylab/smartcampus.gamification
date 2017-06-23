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
