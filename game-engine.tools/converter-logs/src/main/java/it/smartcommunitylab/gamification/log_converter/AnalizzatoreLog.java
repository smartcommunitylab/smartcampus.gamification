package it.smartcommunitylab.gamification.log_converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

import it.smartcommunitylab.gamification.log_converter.beans.Record;
import it.smartcommunitylab.gamification.log_converter.manager.RecordManager;

public class AnalizzatoreLog {

	private static final Logger logger = Logger.getLogger(AnalizzatoreLog.class);

	private static final String PREFIX_PROCESSED_FILE = "NEW-";

	private RecordManager recordManager = new RecordManager();

	public AnalizzatoreLog() {
	}

	// TODO se file esiste gi� non toccare o sovrascrivere
	public void analizzaCartella(String logfolderPath, boolean sovrascriviLogElaborati) throws IOException {
		logger.debug("inizio newData");
		File folder = new File(logfolderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				logger.warn("E' presente una directory - name: " + listOfFiles[i].getName());
			} else {
				if (!isLogFileProcessato(listOfFiles[i])) {
					elabora(listOfFiles[i], sovrascriviLogElaborati);
				}
			}
		}
	}

	private String creaNomeFileElaborato(File logFileDaElaborare) {
		return logFileDaElaborare.getParent() + "/" + PREFIX_PROCESSED_FILE + logFileDaElaborare.getName();
	}

	private boolean isLogFileProcessato(File logFile) {
		return logFile.getName().startsWith(PREFIX_PROCESSED_FILE);
	}

	public void elabora(File logFile, boolean sovrascriviLogElaborati) throws IOException {
		FileWriter fw = null;
		FileReader fr = null;
		BufferedReader br = null;
		try {
			String outputLogFile = creaNomeFileElaborato(logFile);
			if (!new File(outputLogFile).exists() || sovrascriviLogElaborati) {
				logger.info("elaboro file : " + logFile.getName());
				fr = new FileReader(logFile);
				br = new BufferedReader(fr);

				fw = new FileWriter(outputLogFile);
				// in base al type(action) che si vuole filtrare
				try {
					String inputLine;
					String recordTrasformato = null;
					while ((inputLine = br.readLine()) != null) {

						Record record = recordManager.analizza(inputLine);
						logger.debug("record type: " + record.getType());
						switch (record.getType()) {
						case ACTION:
							recordTrasformato = recordManager.analizzaAction(record);
							break;
						case CLASSIFICATION:
							recordTrasformato = recordManager.analizzaClassification(record);
							break;
						case RULE_BADGECOLLECTIONCONCEPT:
							recordTrasformato = recordManager.analizzaBadgeCollection(record);
							break;
						case RULE_POINTCONCEPT:
							recordTrasformato = recordManager.analizzaPointConcept(record);
							break;
						default:
							recordTrasformato = record.getContent();
							break;
						}
						logger.debug("scrivo la nuova riga");
						fw.write(recordTrasformato);
						fw.write("\n");
					}
					logger.info("fine elaborazione file : " + logFile.getName());
				} catch (FileNotFoundException e) {
					logger.error(e);
				}
			} else {
				logger.info(String.format("file %s non sovrascritto", outputLogFile));
			}
		} catch (FileNotFoundException e) {
			logger.error(e);
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (br != null)
					br.close();
			} catch (IOException ex) {
				logger.error(ex);
			}
		}

	}
}
