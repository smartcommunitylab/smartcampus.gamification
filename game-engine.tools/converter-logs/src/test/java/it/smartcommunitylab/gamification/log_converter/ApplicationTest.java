package it.smartcommunitylab.gamification.log_converter;

import it.smartcommunitylab.gamification.log_converter.beans.Record;
import it.smartcommunitylab.gamification.log_converter.manager.RecordManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.junit.Test;

public class ApplicationTest {

	private final Logger logger = Logger.getLogger(RecordManager.class);

	@Test
	public void elaborazione() throws IOException {
	}

	@Test
	public void analizzaAction() throws IOException {

		// lettura
		FileReader fr = null;
		BufferedReader br = null;
		File f;
		String output = "";
		try {
			f = new File("C:\\Users\\sco\\Desktop\\test\\" + "lol.txt");
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
		// ----------------------------------------------
		System.out.println("start");
		// elaborazione
		Record record = new Record();
		record.setContent(output);

		String splitXSpazi = record.getContent().substring(0,
				record.getIndexType());
		String splitDiverso = record.getContent().substring(
				record.getIndexType());

		System.out.println(record.getIndexType());

		String uscita = splitXSpazi + splitDiverso;

		System.out.println("SS: " + splitXSpazi + "");

		System.out.println("SD: " + splitDiverso + "\n");

	}
}
