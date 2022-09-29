package eu.trentorise.game.stats;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class PostgresAppender extends WriterAppender {

	private static final Logger logger = Logger.getLogger(PostgresAppender.class);

	private String url;

	private static Connection conn;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Override
	public void append(LoggingEvent event) {
		try {
			createConnection();
			String row = getRow(event);
			insert(row);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			try {
				logToExceptionErrorFile(getRow(event));
			} catch (IOException ee) {
				logger.error(e.getMessage());
			}
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					/* Ignored */}
			}
		}
	}

	private void logToExceptionErrorFile(String row) throws IOException {
		FileWriter writer = new FileWriter(System.getProperty("logFolder") + System.getProperty("file.separator")
				+ "gamification.postgres.exception.stats.log", true);
		BufferedWriter buffer = new BufferedWriter(writer);
		PrintWriter printer = new PrintWriter(buffer, true);
		printer.print(row);
		printer.close();
		buffer.close();
		writer.close();
	}

	private void insert(String row) throws SQLException {
		Record record;
		record = analizza(row);
		logger.debug("Record type: " + record.getType());
		RecordAnalyzer analyzer = RecordAnalyzerFactory.getAnalyzer(record);
		Map<String, String> recordFields = analyzer.extractData();
		if (recordFields.containsKey("gameId")) {
			String type = recordFields.containsKey("type") ? "'" + recordFields.get("type") + "'" : null;
			String actionName = recordFields.containsKey("actionName") ? "'" + recordFields.get("actionName") + "'"
					: null;
			String ruleName = recordFields.containsKey("ruleName") ? "'" + recordFields.get("ruleName") + "'" : null;
			String conceptName = recordFields.containsKey("conceptName") ? "'" + recordFields.get("conceptName") + "'"
					: null;

			try (var stmt = conn.prepareStatement(
					"INSERT INTO eventLogs (gameid, playerid, executionid, executiontime, time, type, actionname, rulename, conceptname, deltascore, score) VALUES("
							+ "'" + recordFields.get("gameId") + "', " + "'" + recordFields.get("playerId") + "', "
							+ "'" + recordFields.get("executionId") + "', " + "to_timestamp("
							+ Long.valueOf(recordFields.get("executionTime")) / 1000 + "), " + "to_timestamp("
							+ Long.valueOf(recordFields.get("timestamp")) / 1000 + "), " + type + ", " + actionName
							+ ", " + ruleName + ", " + conceptName + ", " + recordFields.get("deltaScore") + ", "
							+ recordFields.get("score") + ")")) {
				logger.info("INSERT -" + stmt);
				stmt.execute();
			}
		} else {
			logger.info("skip event for game not selected: " + recordFields.get("gameId"));
		}

	}

	public Record analizza(String record) {
		Record result = null;
		if (record != null) {
			result = new Record(record);
			logger.debug("RECORD : " + record.toString());
		}
		return result;
	}

	public synchronized void createConnection() throws SQLException {
		if (conn == null || conn.isClosed()) {
			url = System.getenv("POSTGRES_URL");
			conn = DriverManager.getConnection(url);
		}
	}

	private String getRow(LoggingEvent event) {
		return super.layout.format(event);
	}

}
