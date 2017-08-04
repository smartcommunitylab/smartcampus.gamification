package it.smartcommunitylab.gamification.log2elastic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log2elastic.analyzer.RecordAnalyzer;
import it.smartcommunitylab.gamification.log2elastic.analyzer.RecordAnalyzerFactory;

public class Application {
	private static final String PREFIX_PROCESSED_FILE = "NEW-";

	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		String logfolderPath = args[0];
		logger.debug("stats processiong logs");
		File folder = new File(logfolderPath);
		File[] listOfFiles = folder.listFiles();
		ESHelper esHelper = new ESHelper();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (!listOfFiles[i].isDirectory()) {
				if (isLogFileProcessato(listOfFiles[i])) {
					logger.info("processing FILE: " + listOfFiles[i].getName());
					FileReader fr = null;
					BufferedReader br = null;
					fr = new FileReader(args[0] + "/" + listOfFiles[i].getName());
					br = new BufferedReader(fr);

					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						Record record = analizza(inputLine);
						logger.debug("Record type: " + record.getType());
						RecordAnalyzer analyzer = RecordAnalyzerFactory.getAnalyzer(record);
						Map<String, String> recordFields = analyzer.extractData();
						esHelper.saveRecord(record.getType(), recordFields.get("gameId"),
								Long.valueOf(recordFields.get("executionTime")), recordFields);
					}

					br.close();
				}
			}
		}

		logger.info("application end");
	}

	private static boolean isLogFileProcessato(File logFile) {
		return logFile.getName().startsWith(PREFIX_PROCESSED_FILE);
	}

	public static Record analizza(String record) {
		Record result = null;
		if (record != null) {
			result = new Record(record);
			logger.debug("RECORD : " + record.toString());
		}
		return result;
	}

}
