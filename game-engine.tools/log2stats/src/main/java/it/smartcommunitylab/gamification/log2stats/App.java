package it.smartcommunitylab.gamification.log2stats;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.log4j.Logger;

public class App {

    private static final Logger logger = Logger.getLogger(App.class);

    private static final boolean OVERWRITE_STATS = true;
    private static final String FOLDER_INPUT = "";
    private static final String FOLDER_OUTPUT = "";

    public static void main(String[] args) throws IOException {
        logger.info(String.format("overwrite_stats %s", OVERWRITE_STATS));
        logger.info(String.format("folder_input %s", FOLDER_INPUT));
        logger.info(String.format("folder_output %s", FOLDER_OUTPUT));

        File outputFolder = new File(FOLDER_OUTPUT);
        if (!outputFolder.exists()) {
            logger.info(String.format("output folder not exist"));
            if (outputFolder.mkdirs()) {
                logger.info(
                        String.format("created output folder %s", outputFolder.getAbsolutePath()));
            }
        }
        Analyzer analyzer = new Analyzer();

        for (File logFile : getStatsFile(FOLDER_INPUT)) {
            if (!logFile.isDirectory()) {
                BufferedReader reader = new BufferedReader(new FileReader(logFile));
                logger.info(String.format("read logFile: %s", logFile.getName()));
                String statsFileName = statsFileName(logFile.getName());
                BufferedWriter writer = new BufferedWriter(
                        new FileWriter(new File(FOLDER_OUTPUT, statsFileName), !OVERWRITE_STATS));
                logger.info(String.format("write stasFile: %s", logFile.getName()));
                String row = null;
                while ((row = reader.readLine()) != null) {
                    String result = analyzer.analyze(row);
                    if (result == null) {
                        logger.error(String.format("row issue: %s", row));
                    }
                    if (result != null && !result.isEmpty()) {
                        writer.write(result + "\n");
                    }
                }
                reader.close();
                writer.close();
            }
        }
        logger.info("Done");
    }

    private static String statsFileName(String logFileName) {
        if (logFileName != null) {
            return logFileName.replace(".all.", ".stats.");
        } else {
            return null;
        }
    }

    private static File[] getStatsFile(String folder) {
        File folderFile = new File(folder);
        if (folderFile.isDirectory()) {
            return folderFile.listFiles();
        }
        return new File[0];
    }

}
