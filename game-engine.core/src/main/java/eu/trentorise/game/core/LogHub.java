package eu.trentorise.game.core;

import org.slf4j.Logger;
import org.slf4j.helpers.MessageFormatter;

public class LogHub {

	public static void info(String gameId, Logger logger, String msg) {
		if (gameId != null) {
			logger.info("{} - {}", gameId, msg);
		} else {
			logger.info(msg);
		}
	}

	public static void error(String gameId, Logger logger, String msg) {
		if (gameId != null) {
			logger.error("{} - {}", gameId, msg);
		} else {
			logger.error(msg);
		}
	}

	public static void warn(String gameId, Logger logger, String msg) {
		if (gameId != null) {
			logger.warn("{} - {}", gameId, msg);
		} else {
			logger.warn(msg);
		}
	}

	public static void debug(String gameId, Logger logger, String msg) {
		if (gameId != null) {
			logger.debug("{} - {}", gameId, msg);
		} else {
			logger.debug(msg);
		}
	}

	public static void info(String gameId, Logger logger, String msg, Object... objs) {
		info(gameId, logger, MessageFormatter.arrayFormat(msg, objs).getMessage());
	}

	public static void error(String gameId, Logger logger, String msg, Object... objs) {
		error(gameId, logger, MessageFormatter.arrayFormat(msg, objs).getMessage());
	}

	public static void warn(String gameId, Logger logger, String msg, Object... objs) {
		warn(gameId, logger, MessageFormatter.arrayFormat(msg, objs).getMessage());
	}

	public static void debug(String gameId, Logger logger, String msg, Object... objs) {
		debug(gameId, logger, MessageFormatter.arrayFormat(msg, objs).getMessage());
	}

	public static void info(String gameId, Logger logger, String msg, Exception e) {
		if (gameId != null) {
			logger.info("{} - {}", gameId, msg, e);
		} else {
			logger.info(msg, e);
		}
	}

	public static void error(String gameId, Logger logger, String msg, Exception e) {
		if (gameId != null) {
			logger.error("{} - {}", gameId, msg, e);
		} else {
			logger.error(msg, e);
		}
	}

	public static void warn(String gameId, Logger logger, String msg, Exception e) {
		if (gameId != null) {
			logger.warn("{} - {}", gameId, msg, e);
		} else {
			logger.warn(msg, e);
		}
	}

	public static void debug(String gameId, Logger logger, String msg, Exception e) {
		if (gameId != null) {
			logger.debug("{} - {}", gameId, msg, e);
		} else {
			logger.debug(msg, e);
		}
	}
}
