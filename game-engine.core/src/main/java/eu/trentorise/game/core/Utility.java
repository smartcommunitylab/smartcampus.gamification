package eu.trentorise.game.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.trentorise.game.managers.DroolsEngine;

public class Utility {
	private String gameId;

	private Logger logger = LoggerFactory.getLogger(DroolsEngine.class);

	public Utility(String gameId) {
		this.gameId = gameId;
	}

	public void log(String msg) {
		LogHub.info(gameId, logger, msg);
	}
}
