package it.smartcommunitylab.gamification.user_creation_logs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;

public class Application {
	private static final Logger logger = Logger.getLogger(Application.class);
	private static String GAME_ID = "57ac710fd4c6ac7872b0e7a1";

	public static void main(String[] args) throws IOException {
		logger.info("start applicazione");
		String logfolderPath = args[0];
		String registrationUsersFile = args[1];
		logger.info("folder log path: " + logfolderPath);
		logger.info("file registrazione utenti: " + registrationUsersFile);
		elaboraRegistrazioni(logfolderPath, registrationUsersFile);

	}

	public static void elaboraRegistrazioni(String logFolderPath, String registrationUsersFile) {
		FileReader fr = null;
		BufferedReader br = null;
		File f;
		String[] valori = null;
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		String data = null;
		int totalRegistration = 0;
		int totalRecordInserted = 0;
		try {
			f = new File(registrationUsersFile);
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			String inputLine;
			try {
				while ((inputLine = br.readLine()) != null) {
					if (!inputLine.contains("socialId,personalData.timestamp")) {
						logger.debug("inputLine: " + inputLine);
						totalRegistration++;
						valori = inputLine.split(",");
						String recordLog = generaRecordLog(valori);
						long timestampRegistrazione = Long.valueOf(valori[1]);
						logger.debug(generaRecordLog(valori));
						Date date = new Date(Long.valueOf(valori[1]));
						data = dataFormat.format(date);

						logger.debug("data: " + data);
						if (scritturaSuLog(timestampRegistrazione, recordLog, logFolderPath)) {
							logger.info("scrittura registrazione su log con successo " + data);
							totalRecordInserted++;
						}
					}
				}
			} catch (IOException e) {
				logger.error(e);
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		}
		logger.info(String.format("Registrazioni totali: %s, Record inseriti in log: %s", totalRegistration,
				totalRecordInserted));
	}

	public static String generaRecordLog(String[] data) {

		String out = null;

		String playerId = data[0].trim();
		String dataregistrazione = data[1].trim();

		out = "INFO - " + "\"" + GAME_ID + "\"" + " " + playerId + " " + UUID.randomUUID().toString() + " "
				+ dataregistrazione + " " + dataregistrazione + " type=UserCreation";
		logger.debug("out :" + out);
		return out;
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
