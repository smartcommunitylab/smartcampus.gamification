package it.smartcommunitylab.gamification.log_converter;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Application {
	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		logger.info("start applicazione");
		String logfolderPath = args[0];
		logger.info("folder log path: " + logfolderPath);
		AnalizzatoreLog analizzatoreLog = new AnalizzatoreLog();
		boolean sovrascriviLogElaborati;
		try {
			sovrascriviLogElaborati = Boolean.valueOf(args[1]);
			logger.info("sovrascrivi log elaborati: " + sovrascriviLogElaborati);
		} catch (Exception e) {
			sovrascriviLogElaborati = false;
			logger.info("valore di default per sovrascrivi log elaborati: " + sovrascriviLogElaborati);
		}
		analizzatoreLog.analizzaCartella(logfolderPath, sovrascriviLogElaborati);

	}
}
