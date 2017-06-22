package it.smartcommunitylab.gamification.log2elastic;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GetClass {
	OkHttpClient client = new OkHttpClient();

	String run(String url) throws IOException {
		Request request = new Request.Builder().url(url).build();

		try (Response response = client.newCall(request).execute()) {
			return response.body().string();
		}
	}

	public static void main(String[] args) throws IOException {
		GetClass example = new GetClass();
		String response = example
				.run("http://localhost:9200/libreria/_search?q=*");
		System.out.println("response: " + response);
	}
}