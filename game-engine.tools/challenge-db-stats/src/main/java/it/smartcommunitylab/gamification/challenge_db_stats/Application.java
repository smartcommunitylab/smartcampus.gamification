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
	private final static String DB_NAME = "gamification1506";
	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) {
		String logfolderPath = args[0];
		logger.info("cartella dei file di log: " + logfolderPath);
		logger.info(String.format("host: %s port: %s db-name: %s", DB_HOST, DB_PORT, DB_NAME));
		creaLoggerChallenge(logfolderPath);
	}

	@SuppressWarnings("unchecked")
	public static void creaLoggerChallenge(String logfolderPath) {
		MongoClient mongoClient = new MongoClient(DB_HOST, DB_PORT);
		MongoDatabase db = mongoClient.getDatabase(DB_NAME);
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

		MongoCollection<Document> playerStates = db.getCollection("playerState");
		FindIterable<Document> playerState = playerStates.find(and(eq("gameId", "57ac710fd4c6ac7872b0e7a1")));
		String data = null;
		int totalChallengeCompleted = 0;
		int totalChallengeAssigned = 0;
		int totalRigheLog = 0;

		for (Document player : playerState) {
			int contChallengeCompleted = 0;
			int contSfidaAssegnata = 0;
			int righeDiLog = 0;

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
								+ start + " " + start + " " + "type=ChallengeAssigned name=\"" + challengeName + "\" "
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
									+ "type=ChallengeCompleted name=\"" + challengeName + "\" completed";
							logger.debug("sfida completata: " + sfidaCompletata);

							if (scritturaSuLog((Long) dateCompleted, sfidaCompletata, logfolderPath)) {
								logger.info("sfida completata scritta su log con successo su data "
										+ Instant.ofEpochMilli((Long) dateCompleted).atZone(ZoneId.systemDefault())
												.toLocalDate().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
								righeDiLog++;
							}

						} else {
							logger.debug("sfida non completata: " + challengeName);
						}
					}

					logger.info(String.format(
							"playerId: %s, Challenge assegnate: %s, Challenge completate: %s, righe aggiunte al log:%s ",
							playerId, contSfidaAssegnata, contChallengeCompleted, righeDiLog));
				} else {
					logger.debug("nessuna challenge per giocatore " + playerId);
				}
			} else {
				logger.debug(String.format("lo stato del giocatore %s e' vuoto", playerId));
			}

			totalChallengeAssigned += contSfidaAssegnata;
			totalChallengeCompleted += contChallengeCompleted;
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

	private static boolean scritturaSuLog(long dateTimestamp, String out, String logfolderPath) {
		String logFilename = getLogFileName(dateTimestamp);
		logger.debug("search for logFileName: " + logFilename);
		File logFile = new File(logfolderPath, logFilename);
		if (logFile.exists()) {
			FileWriter fw = null;
			BufferedWriter bw = null;
			PrintWriter pw = null;
			try {
				fw = new FileWriter(logFile, true);
				bw = new BufferedWriter(fw);
				pw = new PrintWriter(bw);
				pw.write(out + "\n");
				pw.flush();
				return true;
			} catch (IOException e) {
				logger.warn(String.format("Fallita scrittura su file %s: %s", logFile.getName(), e.getMessage()));
				return false;
			} finally {
				try {
					if (pw != null) {
						pw.close();
					}
					if (bw != null) {
						bw.close();
					}
					if (fw != null) {
						fw.close();
					}
				} catch (IOException e) {
					logger.error("Eccezione nella chiusura degli stream di scrittura");
				}

			}
		} else {
			logger.info(String.format("logFile %s non esiste", logFile.getName()));
			return false;
		}
	}
}
