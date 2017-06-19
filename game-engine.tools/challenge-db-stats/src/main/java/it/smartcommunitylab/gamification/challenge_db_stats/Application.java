package it.smartcommunitylab.gamification.challenge_db_stats;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Application {
	private final static String DB_HOST = "localhost";
	private final static int DB_PORT = 27017;
	private final static String DB_NAME = "gamification1506";
	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) {
		creaLoggerChallenge();
	}

	public static void creaLoggerChallenge() {
		MongoClient mongoClient = new MongoClient(DB_HOST, DB_PORT);
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		for (String collection : db.listCollectionNames()) {
			logger.info(collection);
		}
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

		MongoCollection<Document> playerStates = db
				.getCollection("playerState");
		FindIterable<Document> playerState = playerStates.find(and(eq("gameId",
				"57ac710fd4c6ac7872b0e7a1")));
		String data = null;
		for (Document document : playerState) {
			int contChallengeCompleted = 0;
			int contSfidaAssegnata = 0;
			int righeDiLog = 0;
			int contChallengeMiss = 0;
			Document player = document;
			Object gameId = player.get("gameId");
			Object playerId = player.get("playerId");
			@SuppressWarnings("unchecked")
			Map<String, Object> campi = player.get("concepts", Map.class);
			if (campi != null) {
				logger.debug(campi.get("ChallengeConcept"));
				@SuppressWarnings("unchecked")
				Map<String, Object> challengeConcept = (Map<String, Object>) campi
						.get("ChallengeConcept");
				if (challengeConcept != null) {
					logger.debug("si challengeConcept");
					Set<String> chivi = challengeConcept.keySet();
					logger.debug("chivi: " + chivi);
					for (String k : chivi) {
						Map<String, Object> sfide = (Map<String, Object>) challengeConcept;
						@SuppressWarnings("unchecked")
						Map<String, Object> obj = (Map<String, Object>) ((Document) sfide
								.get(k)).get("obj");
						contSfidaAssegnata++;
						logger.debug(obj);
						String random = UUID.randomUUID().toString();
						Object dateCompleted = obj.get("dateCompleted");
						Object start = obj.get("start");
						Object end = obj.get("end");
						// Object completed = obj.get("completed");

						data = dataFormat.format(start);
						logger.debug("data della giocata=" + data);
						if (trovaData(data, "C:\\Users\\sco\\Desktop\\test")) {
							logger.debug("FUNZIONA");
						}

						String sfidaAssegnata = "INFO - " + "\"" + gameId
								+ "\" \"" + playerId + "\" " + random + " "
								+ start + " " + start + " "
								+ " type=ChallengeAssigned name=\"" + k + "\" "
								+ "startDate=" + start + " endDate=" + end;
						logger.info("assegnata: " + sfidaAssegnata);

						if (dateCompleted != null) {
							contChallengeCompleted++;

							String out = "INFO - " + "\"" + gameId + "\" \""
									+ playerId + "\" " + random + " "
									+ dateCompleted + " " + dateCompleted + " "
									+ " type=ChallengeComplete name=\"" + k
									+ "\" ";
							logger.info("completata: " + out);
							righeDiLog++;
						} else {
							logger.debug("non si è completata la sfiga: " + k);
							logger.debug("sfida non completata");
							contChallengeMiss++;
						}
					}
				} else {
					logger.debug("no challengeConcept");
				}
			} else {
				logger.debug("no fields");
			}
			logger.info("playerId: " + playerId + " "
					+ " - challenge completate: " + contChallengeCompleted
					+ " - challenge mancate: " + contChallengeMiss
					+ " sul totale di: " + contSfidaAssegnata + " assegnate"
					+ " - righe di log: " + righeDiLog);
		}
	}

	public static Boolean trovaData(String data, String logfolderPath) {

		Boolean ok = false;
		File folder = new File(logfolderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				logger.warn("E' presente una directory - name: "
						+ listOfFiles[i].getName());
			} else {
				String nome = listOfFiles[i].getName();
				logger.debug(nome);
				logger.debug("la data è=" + data);
				logger.debug(nome.contains(data));

				if (nome.contains(data)) {
					logger.info("TROVATO! - il file è: " + nome + " - data: "
							+ data);
					ok = true;
				}
			}
		}
		return ok;
	}
}
