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
	public void analizzaCartella(String logfolderPath) throws IOException {
		logger.debug("inizio newData");
		File folder = new File(logfolderPath);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isDirectory()) {
				logger.warn("E' presente una directory - name: " + listOfFiles[i].getName());
			} else {
				String nome = listOfFiles[i].getName();
				if (!nome.startsWith(PREFIX_PROCESSED_FILE)) {
					logger.info("processo file : " + nome);
					elabora(logfolderPath, nome);
				}
			}
		}
	}

	public void elabora(String logFolderPath, String nome) throws IOException {
		FileWriter fw = null;
		FileReader fr = null;
		BufferedReader br = null;
		File f;
		try {
			f = new File(logFolderPath + nome);
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			if (!f.getName().contains("NEW")) {// agg sovrascrivo
				fw = new FileWriter(logFolderPath + PREFIX_PROCESSED_FILE + nome, false);
			} else {
				// to do
				fw = new FileWriter(logFolderPath + nome);
			}
			// in base al type(action) che si vuole filtrare
			try {
				String inputLine;
				String recordTrasformato = null;
				while ((inputLine = br.readLine()) != null) {

					Record record = recordManager.analizza(inputLine);
					logger.info("record type: " + record.getType());
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
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null)
					fw.close();
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
}
