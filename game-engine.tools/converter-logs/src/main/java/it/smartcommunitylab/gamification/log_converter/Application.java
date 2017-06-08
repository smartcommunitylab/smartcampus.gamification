package it.smartcommunitylab.gamification.log_converter;

import java.io.IOException;

import org.apache.log4j.Logger;

public class Application {
	public static final String LOG_FOLDER_PATH = "C:\\Users\\sco\\Desktop\\test\\";
	public static String ext = "";
	private static final Logger logger = Logger.getLogger(Application.class);

	public static void main(String[] args) throws IOException {
		logger.info("start applicazione");
		AnalizzatoreLog analizzatoreLog = new AnalizzatoreLog();
		analizzatoreLog.elabora(LOG_FOLDER_PATH);
	}
}
