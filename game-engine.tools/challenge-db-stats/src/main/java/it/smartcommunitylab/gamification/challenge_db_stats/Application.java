package it.smartcommunitylab.gamification.challenge_db_stats;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.util.Map;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Application {

	private final static String DB_HOST = "localhost";
	private final static int DB_PORT = 27017;
	private final static String DB_NAME = "gamification0906";

	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) {
		MongoClient mongoClient = new MongoClient(DB_HOST, DB_PORT);
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		for (String collection : db.listCollectionNames()) {
			logger.info(collection);
		}

		MongoCollection<Document> playerStates = db.getCollection("playerState");

		Document playerState = playerStates.find(and(eq("gameId", "57ac710fd4c6ac7872b0e7a1"), eq("playerId", "24153")))
				.first();

		Map<String, Object> fields = playerState.get("concepts", Map.class);
		System.out.println(fields.get("ChallengeConcept"));

	}

}
