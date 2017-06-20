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
import java.util.Date;
import java.util.UUID;

import org.apache.log4j.Logger;

public class Application {
	private static final Logger logger = Logger.getLogger(Application.class);
	static String GameId = "57ac710fd4c6ac7872b0e7a1";
	private static String filename;

	public static void main(String[] args) throws IOException {
		logger.info("start applicazione");
		String logfolderPath = args[0];
		logger.info("folder log path: " + logfolderPath);
		estraiRighe(logfolderPath, "registrations.csv");

	}

	public static String[] estraiRighe(String logFolderPath, String nome) {
		FileWriter fw = null;
		FileReader fr = null;
		BufferedReader br = null;
		File f;
		String[] valori = null;
		String out = null;
		SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
		String data = null;
		try {
			f = new File(logFolderPath + nome);
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			String inputLine;
			String riga;
			try {
				while ((inputLine = br.readLine()) != null) {
					if (!inputLine.contains("socialId,personalData.timestamp")) {
						System.out.println("inputLine: " + inputLine);
						riga = inputLine;
						valori = riga.split(",");
						logger.debug(valori[0]);
						logger.info(creaStriga(valori));
						out = creaStriga(valori);
						Date date = new Date(Long.valueOf(valori[1]));
						data = dataFormat.format(date);
						System.out.println("valori=" + valori[1] + ".");

						// data = dataFormat.format(dataDaConvertire);
						System.out.println("data: " + data);
						// data = dataFormat.format(valori[1].toDate())
						if (trovaData(data, logFolderPath)) {
							scrittura(out, logFolderPath);
						}
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {

		}
		return valori;
	}

	public static String creaStriga(String[] vettore) {

		String out = null;

		String playerId = vettore[0];
		String dataregistrazione = vettore[1];

		out = "INFO - " + "\"" + GameId + "\"" + " " + playerId + " "
				+ UUID.randomUUID().toString() + " " + dataregistrazione + " "
				+ dataregistrazione + " type=UserCreation";
		logger.debug("out :" + out);
		return out;
	}

	public static void scrittura(String out, String logfolderPath)
			throws IOException {
		logger.debug("FILENAME: " + filename);
		FileWriter fw = new FileWriter(logfolderPath + filename, true);
		BufferedWriter bw = new BufferedWriter(fw);
		PrintWriter pw = new PrintWriter(bw);
		pw.write(out + "\n");
		logger.debug("COSA DOVREI SCRIVERE: " + "\n" + out);
		pw.flush();
		pw.close();
		bw.close();
		fw.close();
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

				if (nome.contains(data) && nome.contains("NEW")) {
					logger.info("TROVATO! - il file è: " + nome + " - data: "
							+ data);
					filename = nome;
					ok = true;
				}
			}
		}
		return ok;
	}
}
