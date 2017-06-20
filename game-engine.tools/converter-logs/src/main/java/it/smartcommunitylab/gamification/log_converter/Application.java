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
		analizzatoreLog.analizzaCartella(logfolderPath);

	}
}
