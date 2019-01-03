/**
 * Copyright 2018-2019 SmartCommunity Lab(FBK-ICT).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package it.smartcommunitylab.model.ext;

import java.util.HashMap;
import java.util.Map;

import it.smartcommunitylab.model.Notification;

public class MessageNotification extends Notification {

	private String key;
	private Map<String, Object> data = new HashMap<String, Object>();

	public MessageNotification() {
		super();
	}

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
