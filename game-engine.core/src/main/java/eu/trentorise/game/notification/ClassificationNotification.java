package eu.trentorise.game.notification;

import eu.trentorise.game.model.core.Notification;

public class ClassificationNotification extends Notification {
	private String classificationName;
	private int classificationPosition;

	public String getClassificationName() {
		return classificationName;
	}

	public void setClassificationName(String classificationName) {
		this.classificationName = classificationName;
	}

	public int getClassificationPosition() {
		return classificationPosition;
	}

	public void setClassificationPosition(int classificationPosition) {
		this.classificationPosition = classificationPosition;
	}

	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, classificationName=%s classificationPosition=%s]", getGameId(),
				getPlayerId(), classificationName, classificationPosition);
	}

}
