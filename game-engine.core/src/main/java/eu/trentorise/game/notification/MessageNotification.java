package eu.trentorise.game.notification;

import java.util.HashMap;
import java.util.Map;

import eu.trentorise.game.model.core.Notification;

public class MessageNotification extends Notification {

	private String key;
	private Map<String, Object> data = new HashMap<String, Object>();

	public MessageNotification(String gameId, String playerId, String messageKey) {
		super(gameId, playerId);
		key = messageKey;
	}

	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, key=%s, data=%s]",
				getGameId(), getPlayerId(), key, data);
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void addData(String key, Object value) {
		data.put(key, value);
	}

	public String getKey() {
		return key;
	}

}
