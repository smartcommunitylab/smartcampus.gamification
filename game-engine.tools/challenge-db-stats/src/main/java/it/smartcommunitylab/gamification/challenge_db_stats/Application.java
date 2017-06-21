package it.smartcommunitylab.gamification.challenge_db_stats;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
	private final static String DB_NAME = "gamification0906";
	private static final Logger logger = Logger.getLogger(Application.class);
	private static String filename;

	public static void main(String[] args) throws IOException {
		String logfolderPath = args[0];
		logger.debug("cartella dei file di log: " + logfolderPath);
		logger.info(String.format("host: %s port: %s db-name: %s", DB_HOST, DB_PORT, DB_NAME));
		creaLoggerChallenge(logfolderPath);
	}

	@SuppressWarnings("unchecked")
	public static void creaLoggerChallenge(String logfolderPath) throws IOException {
		MongoClient mongoClient = new MongoClient(DB_HOST, DB_PORT);
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

		MongoCollection<Document> playerStates = db.getCollection("playerState");
		FindIterable<Document> playerState = playerStates.find(and(eq("gameId", "57ac710fd4c6ac7872b0e7a1")));
		String data = null;
		int totalChallengeCompleted = 0;
		int totalChallengeAssigned = 0;
		int totalRigheLog = 0;
		int totalChallengeMiss = 0;

		for (Document player : playerState) {
			int contChallengeCompleted = 0;
			int contSfidaAssegnata = 0;
			int righeDiLog = 0;
			int contChallengeMiss = 0;

			Object gameId = player.get("gameId");
			Object playerId = player.get("playerId");
			Map<String, Object> campi = player.get("concepts", Map.class);

			if (campi != null) {
				Map<String, Object> challengeConcepts = (Map<String, Object>) campi.get("ChallengeConcept");

				if (challengeConcepts != null && !challengeConcepts.isEmpty()) {
					logger.debug(String.format("Trovata challenge per giocatore %s", playerId));
					Set<String> challenges = challengeConcepts.keySet();
					logger.debug("challenges per il giocatore: " + challenges);

					for (String challengeName : challenges) {
						Map<String, Object> sfide = (Map<String, Object>) challengeConcepts;
						Map<String, Object> obj = (Map<String, Object>) ((Document) sfide.get(challengeName))
								.get("obj");
						contSfidaAssegnata++;
						logger.debug("campi della challenge: " + obj);
						String random = UUID.randomUUID().toString();
						Object dateCompleted = obj.get("dateCompleted");
						Object start = obj.get("start");
						Object end = obj.get("end");

						data = dataFormat.format(start);
						logger.debug(String.format("data di inizio della challenge %s", data));

						String sfidaAssegnata = "INFO - " + "\"" + gameId + "\" \"" + playerId + "\" " + random + " "
								+ start + " " + start + " " + " type=ChallengeAssigned name=\"" + challengeName + "\" "
								+ "startDate=" + start + " endDate=" + end;
						logger.debug("sfida assegnata: " + sfidaAssegnata);
						if (scritturaSuLog((Long) start, sfidaAssegnata, logfolderPath)) {
							logger.info("sfida assignata scritta su log con successo su data "
									+ Instant.ofEpochMilli((Long) start).atZone(ZoneId.systemDefault()).toLocalDate()
											.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
							righeDiLog++;
						}

						if (dateCompleted != null) {
							contChallengeCompleted++;

							String sfidaCompletata = "INFO - " + "\"" + gameId + "\" \"" + playerId + "\" " + random
									+ " " + dateCompleted + " " + dateCompleted + " "
									+ " type=ChallengeComplete name=\"" + challengeName + "\" ";
							logger.debug("sfida completata: " + sfidaCompletata);

							if (scritturaSuLog((Long) dateCompleted, sfidaCompletata, logfolderPath)) {
								logger.info("sfida completata scritta su log con successo su data "
										+ Instant.ofEpochMilli((Long) dateCompleted).atZone(ZoneId.systemDefault())
												.toLocalDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
								righeDiLog++;
							}

						} else {
							logger.debug("sfida non completata: " + challengeName);
							contChallengeMiss++;
						}
					}
					logger.info("playerId: " + playerId + " " + " - challenge completate: " + contChallengeCompleted
							+ " - challenge mancate: " + contChallengeMiss + " sul totale di: " + contSfidaAssegnata
							+ " assegnate" + " - righe di log: " + righeDiLog);
				} else {
					logger.debug("nessuna challenge per giocatore " + playerId);
				}
			} else {
				logger.debug(String.format("lo stato del giocatore %s e' vuoto", playerId));
			}

			totalChallengeAssigned += contSfidaAssegnata;
			totalChallengeCompleted += contChallengeCompleted;
			totalChallengeMiss += contChallengeMiss;
			totalRigheLog += righeDiLog;
		}
		logger.info(String.format("Challenge assegnate: %s, Challenge completate: %s, righe aggiunte al log: %s",
				totalChallengeAssigned, totalChallengeCompleted, totalRigheLog));
		mongoClient.close();
	}

	private static String getLogFileName(long dateTimestamp) {
		LocalDate date = Instant.ofEpochMilli(dateTimestamp).atZone(ZoneId.systemDefault()).toLocalDate();
		if (date.isEqual(LocalDate.now())) {
			return "NEW-gamification.stats.log";
		} else {
			return String.format("NEW-gamification.stats.log.%s",
					date.format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
		}
	}

	private static boolean scritturaSuLog(long dateTimestamp, String out, String logfolderPath) throws IOException {
		String logFilename = getLogFileName(dateTimestamp);
		logger.debug("search for logFileName: " + logFilename);
		File logFile = new File(logfolderPath, logFilename);
		if (logFile.exists()) {
			FileWriter fw = new FileWriter(logFile, true);
			BufferedWriter bw = new BufferedWriter(fw);
			PrintWriter pw = new PrintWriter(bw);
			pw.write(out + "\n");
			pw.flush();
			pw.close();
			bw.close();
			fw.close();
			return true;
		} else {
			logger.info(String.format("logFile %s not exist", logFile.getAbsolutePath()));
			return false;
		}
	}

	// public static Boolean trovaData(String data, String logfolderPath) {
	//
	// Boolean ok = false;
	// File folder = new File(logfolderPath);
	// File[] listOfFiles = folder.listFiles();
	// for (int i = 0; i < listOfFiles.length; i++) {
	// if (listOfFiles[i].isDirectory()) {
	// logger.warn("E' presente una directory - name: " +
	// listOfFiles[i].getName());
	// } else {
	// String nome = listOfFiles[i].getName();
	// logger.debug(nome);
	// logger.debug("la data �=" + data);
	// logger.debug(nome.contains(data));
	//
	// if (nome.contains(data) && nome.contains("NEW")) {
	// logger.info("TROVATO! - il file �: " + nome + " - data: " + data);
	// filename = nome;
	// ok = true;
	// }
	// }
	// }
	// return ok;
	// }
}
