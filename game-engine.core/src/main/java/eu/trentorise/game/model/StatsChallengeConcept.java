package eu.trentorise.game.model;

import eu.trentorise.game.core.StatsLogger;

public class StatsChallengeConcept extends ChallengeConcept {
	private String gameId;
	private String playerId;
	private String executionId;

	public StatsChallengeConcept(String gameId, String playerId, String executionId) {
		this.gameId = gameId;
		this.playerId = playerId;
		this.executionId = executionId;
	}

	@Override
	public boolean completed() {
		boolean isCompleted = super.completed();
		if (isCompleted) {
			StatsLogger.logChallengeComplete(gameId, playerId, executionId, getDateCompleted().getTime(),
					System.currentTimeMillis(), getName());
		}
		return isCompleted;

	}
}
