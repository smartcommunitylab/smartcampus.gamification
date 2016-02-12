/**
 *    Copyright 2015 Fondazione Bruno Kessler - Trento RISE
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package eu.trentorise.game.notification;

import eu.trentorise.game.model.core.Notification;

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

	@Override
	public String toString() {
		return String.format("[gameId=%s, playerId=%s, badge=%s]", getGameId(),
				getPlayerId(), badge);
	}

}
