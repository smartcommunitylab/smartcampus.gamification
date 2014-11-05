package eu.trentorise.game.notification;

import eu.trentorise.game.model.Notification;

public class BadgeNotification extends Notification {

	public BadgeNotification() {
		super();
	}

	public BadgeNotification(String gameId, String playerId, String badge) {
		super(gameId, playerId);
		this.badge = badge;
	}

	private String badge;

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

}
