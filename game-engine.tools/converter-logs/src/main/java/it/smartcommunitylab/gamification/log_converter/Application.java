package it.smartcommunitylab.gamification.log_converter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class Application {

	public static String path = "C:\\Users\\sco\\Desktop\\test\\";
	public static String ext = "";

	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		logger.info("start applicazione");
		NewData();
	}

	static public void NewData() throws IOException {
		File folder = new File(path);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			String nome = listOfFiles[i].getName();
			logger.info(nome);
			// nome = nome.replaceFirst("[.][^.]+$", "");
			Tutto(nome);
		}
	}

	static public void scrittura(String nome) {
		try {
			String input = elaborazione(nome);
			FileWriter fw = new FileWriter(path + nome + "-NEW");
			fw.write(input);
			fw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static public String elaborazione(String nome) throws IOException {

		// dichiarazioni
		String output = "";

		String input = lettura(nome);
		// FileWriter f = new FileWriter("nuovo");
		// String CosaDeveUscire = lettura("exit");
		// System.out.println(CosaDeveUscire);

		// elaborazione dati
		String[] elementi = input.split(" ");
		// System.out.println("split: " + elementi[8]);
		logger.debug("split: " + elementi[8]);
		for (int i = 0; i < elementi.length; i++) {
			if (!(elementi[i].contains("payload") || elementi[i]
					.contains("oldState"))) {
				output += elementi[i] + " ";
			}
		}
		// toglie l'ultimo spazio
		output = output.substring(0, output.length() - 1);

		// controllo
		// System.out.println(output.length());
		// System.out.println(CosaDeveUscire.length());
		// System.out.println(output.equals(CosaDeveUscire));
		return output;
	}

	static public String lettura(String nome) throws IOException {
		FileReader fr = null;
		BufferedReader br = null;
		File f;
		String output = "";
		try {
			f = new File(path + nome);
			// System.out.println(f.canRead());
			fr = new FileReader(f);
			br = new BufferedReader(fr);

			String str;
			if (f.exists()) {
				while ((str = br.readLine()) != null) {
					output += str;
					// System.out.println(str);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		// System.out.println("risuttato " + output);
		return output;
	}

	static public void Tutto(String nome) throws IOException {
		FileWriter fw = null;
		FileReader fr = null;
		BufferedReader br = null;
		File f;
		try {
			f = new File(path + nome);
			// System.out.println(f.canRead());
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			fw = new FileWriter(path + nome + "-NEW");

			try {
				String str;
				if (f.exists()) {
					while ((str = br.readLine()) != null) {
						String[] elementi = str.split(" ");
						try {
							for (int i = 0; i < elementi.length; i++) {
								if (!(elementi[i].contains("payload") || elementi[i]
										.contains("oldState"))) {
									fw.write(elementi[i] + " ");
								}
							}
							fw.write("\n");
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
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
