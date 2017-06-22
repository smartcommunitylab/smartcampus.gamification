package it.smartcommunitylab.gamification.log2elastic;

import java.io.IOException;

public class Application {

	public static void main(String[] args) throws IOException {
		// post
		System.out.println("POST");
		PostClass esempioPost = new PostClass();
		String json = esempioPost.popolaJson();
		String responsePost = esempioPost.post(
				"http://localhost:9200/libreria/brano", json);
		System.out.println("json : " + json + "\n" + "response: "
				+ responsePost);
		// get
		System.out.println("GET");
		GetClass eempioGet = new GetClass();
		String responseGet = eempioGet
				.run("http://localhost:9200/libreria/_search?q=*");
		System.out.println("response: " + responseGet);
	}
}
