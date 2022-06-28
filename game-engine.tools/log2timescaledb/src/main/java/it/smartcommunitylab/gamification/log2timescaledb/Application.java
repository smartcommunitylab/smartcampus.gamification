package it.smartcommunitylab.gamification.log2timescaledb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;

import it.smartcomunitylab.gamification.log2timescaledb.analyzer.RecordAnalyzer;
import it.smartcomunitylab.gamification.log2timescaledb.analyzer.RecordAnalyzerFactory;

public class Application {
	private static final Logger logger = Logger.getLogger(Application.class);
	private static String CONN_URL = "";
	private static String FOLDER_INPUT = "";

	public static void main(String args[]) {
		FOLDER_INPUT = args[0];
		CONN_URL = args[1]; 
		logger.info(String.format("folder_input %s", FOLDER_INPUT));

		try (var conn = DriverManager.getConnection(CONN_URL)) {
			deleteSchema(conn);
			createSchema(conn);
			insertData(conn, FOLDER_INPUT);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		} catch (IOException ex) {
			// TODO Auto-generated catch block
			System.err.println(ex.getMessage());
		}
	}

	private static void deleteSchema(Connection conn) throws SQLException {
		try (var stmt = conn.createStatement()) {
			stmt.execute("DROP TABLE IF EXISTS eventLogs");
		}
	}

	private static void createSchema(final Connection conn) throws SQLException {

		// 1-- Do not forget to create timescaledb extension
		// CREATE EXTENSION timescaledb;

		// 2-- We start by creating a regular SQL table
		try (var stmt = conn.createStatement()) {
			stmt.execute(
					"CREATE TABLE IF NOT EXISTS eventLogs (gameid TEXT NOT NULL, playerid TEXT NOT NULL, executionid TEXT NOT NULL, executiontime TIMESTAMP, time TIMESTAMP NOT NULL, type TEXT, actionname TEXT, rulename TEXT, conceptname TEXT, deltascore DOUBLE PRECISION, score DOUBLE PRECISION)");
		}

		// 3-- Then we convert it into a hypertable that is partitioned by time
		try (var stmt = conn.createStatement()) {
			stmt.execute("SELECT create_hypertable('eventLogs', 'time', if_not_exists => TRUE)");
		}

	}

	private static void insertData(final Connection conn, String path) throws SQLException, IOException {

		for (File logFile : getStatsFile(FOLDER_INPUT)) {
			logger.info(logFile.getName());
			if (logFile.getName().indexOf("stats.log") == -1) 
				continue;
			if (!logFile.isDirectory()) {
				BufferedReader reader = new BufferedReader(new FileReader(logFile));
				logger.info(String.format("read logFile: %s", logFile.getName()));
				String row = null;
				int counter = 0;
				while ((row = reader.readLine()) != null) {
					Record record;
					try {
						record = analizza(row);
					} catch (IllegalArgumentException ile) {
						System.err.println(ile.getMessage());
						continue;
					}
					logger.debug("Record type: " + record.getType());
					RecordAnalyzer analyzer = RecordAnalyzerFactory.getAnalyzer(record);
					Map<String, String> recordFields = analyzer.extractData();
					if (recordFields.containsKey("gameId")) {
						counter++;
						String type = recordFields.containsKey("type") ? "'" + recordFields.get("type") + "'" : null;
						String actionName = recordFields.containsKey("actionName")
								? "'" + recordFields.get("actionName") + "'"
								: null;
						String ruleName = recordFields.containsKey("ruleName")
								? "'" + recordFields.get("ruleName") + "'"
								: null;
						String conceptName = recordFields.containsKey("conceptName")
								? "'" + recordFields.get("conceptName") + "'"
								: null;

						try (var stmt = conn.prepareStatement(
								"INSERT INTO eventLogs (gameid, playerid, executionid, executionTime, time, type, actionname, rulename, conceptname, deltascore, score) VALUES("
										+ "'" + recordFields.get("gameId") + "', " + "'" + recordFields.get("playerId")
										+ "', " + "'" + recordFields.get("executionId") + "', " + "to_timestamp("
										+ Long.valueOf(recordFields.get("executionTime")) / 1000 + "), "
										+ "to_timestamp(" + Long.valueOf(recordFields.get("timestamp")) / 1000 + "), "
										+ type + ", " + actionName + ", " + ruleName + ", " + conceptName + ", "
										+ recordFields.get("deltaScore") + ", " + recordFields.get("score") + ")")) {
							logger.info(counter + "-" + stmt);
							stmt.execute();
						}
					} else {
						logger.info("skip event for game not selected: " + recordFields.get("gameId"));
					}
				}
				logger.info("inserted " + counter + " entries");
				reader.close();
			}
		}
		logger.info("Done");
	}

	private static File[] getStatsFile(String folder) {
		File folderFile = new File(folder);
		if (folderFile.isDirectory()) {
			return folderFile.listFiles();
		}
		return new File[0];
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