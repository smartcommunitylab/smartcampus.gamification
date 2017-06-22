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

	String popolaJson() {
		return "{\"name\":\"LOOOL\"}";
	}

	public static void main(String[] args) throws IOException {
		PostClass esempioPost = new PostClass();
		String json = esempioPost.popolaJson();
		String response = esempioPost.post(
				"http://localhost:9200/libreria/brano", json);
		System.out.println("json : " + json + "\n" + "response: " + response);
	}
}
