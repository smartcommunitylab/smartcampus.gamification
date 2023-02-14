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

	public void log(Object msg) {
		LogHub.info(gameId, logger, String.valueOf(msg));
	}

	public Double getDouble(Object o) {
		return new Double(o.toString());
	}

	public Integer getInteger(Object o) {
		return new Integer(o.toString());
	}
}
