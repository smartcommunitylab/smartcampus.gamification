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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Application {
    private static final Logger logger = Logger.getLogger(Application.class);

    /**
     * 
     * @param args: a0 -> path of log folder, a1 -> gameId, a2 -> mongoHost, a3 -> mongoPort, a4 ->
     *        dbName, a5 -> search for assigned challenges (true by default) , a6 -> search for
     *        completed challenges (true by default)
     * 
     */
    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext(args);
        logger.info("cartella dei file di log: " + context.getLogFolderPath());
        logger.info(String.format("host: %s port: %s db-name: %s", context.getMongoHost(),
                context.getMongoPort(), context.getDbName()));
        creaLoggerChallenge(context);
    }

    @SuppressWarnings("unchecked")
    public static void creaLoggerChallenge(ApplicationContext context) {
        MongoClient mongoClient = new MongoClient(context.getMongoHost(), context.getMongoPort());
        MongoDatabase db = mongoClient.getDatabase(context.getDbName());
        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");

        MongoCollection<Document> playerStates = db.getCollection("playerState");
        FindIterable<Document> playerState =
                playerStates.find(and(eq("gameId", context.getGameId())));
        String data = null;
        int totalChallengeCompleted = 0;
        int totalChallengeAssigned = 0;
        int totalRigheLog = 0;

        for (Document player : playerState) {
            int contChallengeCompleted = 0;
            int contSfidaAssegnata = 0;
            int righeDiLog = 0;

            // Object gameId = player.get("gameId");
            Object playerId = player.get("playerId");
            Map<String, Object> campi = player.get("concepts", Map.class);

            if (campi != null) {
                Map<String, Object> challengeConcepts =
                        (Map<String, Object>) campi.get("ChallengeConcept");

                if (challengeConcepts != null && !challengeConcepts.isEmpty()) {
                    logger.debug(String.format("Trovata challenge per giocatore %s", playerId));
                    Set<String> challenges = challengeConcepts.keySet();
                    logger.debug("challenges per il giocatore: " + challenges);

                    for (String challengeName : challenges) {
                        Map<String, Object> obj =
                                (Map<String, Object>) ((Document) challengeConcepts
                                        .get(challengeName)).get("obj");
                        contSfidaAssegnata++;
                        logger.debug("campi della challenge: " + obj);
                        Map<String, Object> challengeFields = new HashMap<>();
                        challengeFields.put("dateCompleted", obj.get("dateCompleted"));
                        challengeFields.put("start", obj.get("start"));
                        challengeConcepts.put("end", obj.get("end"));


                        Challenge challenge = instanceOfChallenge(context.getGameId(),
                                (String) playerId, challengeFields);
                        data = dataFormat.format(challenge.getStart());
                        logger.debug(String.format("data di inizio della challenge %s", data));

                        String sfidaAssegnata = challenge.toAssignedLogFormat();
                        logger.debug("sfida assegnata: " + sfidaAssegnata);
                        if (scritturaSuLog(challenge.getStart().getTime(), sfidaAssegnata,
                                context.getLogFolderPath())) {
                            logger.info("sfida assignata scritta su log con successo su data "
                                    + Instant.ofEpochMilli(challenge.getStart().getTime())
                                            .atZone(ZoneId.systemDefault()).toLocalDate()
                                            .format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
                            righeDiLog++;
                        }

                        if (challenge.isCompleted()) {
                            contChallengeCompleted++;

                            String sfidaCompletata = challenge.toCompletedLogFormat();
                            logger.debug("sfida completata: " + sfidaCompletata);

                            if (scritturaSuLog(challenge.getDateCompleted().getTime(),
                                    sfidaCompletata, context.getLogFolderPath())) {
                                logger.info("sfida completata scritta su log con successo su data "
                                        + Instant
                                                .ofEpochMilli(
                                                        challenge.getDateCompleted().getTime())
                                                .atZone(ZoneId.systemDefault()).toLocalDate()
                                                .format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
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
        logger.info(String.format(
                "Challenge assegnate: %s, Challenge completate: %s, righe aggiunte al log: %s",
                totalChallengeAssigned, totalChallengeCompleted, totalRigheLog));
        mongoClient.close();
    }

    private static Challenge instanceOfChallenge(String gameId, String playerId,
            Map<String, Object> fields) {
        return new Challenge(gameId, playerId, (String) fields.get("challengeName"),
                (Date) fields.get("start"), (Date) fields.get("end"),
                (Date) fields.get("dateCompleted"));
    }

    private static String getLogFileName(long dateTimestamp) {
        LocalDate date =
                Instant.ofEpochMilli(dateTimestamp).atZone(ZoneId.systemDefault()).toLocalDate();
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
                logger.warn(String.format("Fallita scrittura su file %s: %s", logFile.getName(),
                        e.getMessage()));
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

    private static class ApplicationContext {
        private String logFolderPath;
        private String gameId;
        private String mongoHost = "127.0.0.1";
        private int mongoPort = 27017;
        private String dbName;
        private boolean convertAssignedChallenges = true;
        private boolean convertCompletedChallenges = true;

        public ApplicationContext(String[] args) {
            logFolderPath = args[0];
            gameId = args[1];
            mongoHost = args[2];
            mongoPort = Integer.valueOf(args[3]);
            dbName = args[4];
            if (args.length > 5) {
                convertAssignedChallenges = Boolean.valueOf(args[5]);
                convertCompletedChallenges = Boolean.valueOf(args[6]);
            }

        }

        public String getLogFolderPath() {
            return logFolderPath;
        }

        public String getGameId() {
            return gameId;
        }

        public String getMongoHost() {
            return mongoHost;
        }

        public int getMongoPort() {
            return mongoPort;
        }

        public String getDbName() {
            return dbName;
        }

        public boolean isConvertAssignedChallenges() {
            return convertAssignedChallenges;
        }

        public boolean isConvertCompletedChallenges() {
            return convertCompletedChallenges;
        }

    }
}
