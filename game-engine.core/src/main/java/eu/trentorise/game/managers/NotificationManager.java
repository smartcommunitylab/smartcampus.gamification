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

package eu.trentorise.game.managers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import eu.trentorise.game.model.core.Notification;
import eu.trentorise.game.repo.NotificationPersistence;
import eu.trentorise.game.repo.NotificationRepo;

@Component
public class NotificationManager {

	@Autowired
	NotificationRepo repo;

	public void notificate(Notification n) {
		repo.save(new NotificationPersistence(n));
	}

	public List<Notification> readNotifications(String gameId) {
		List<NotificationPersistence> nots = repo.findByGameId(gameId);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, Pageable pageable) {
		List<NotificationPersistence> nots = repo.findByGameId(gameId, pageable);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, long fromTs, long toTs) {
		List<NotificationPersistence> nots = null;
		if (fromTs != -1 && toTs != -1) {
			nots = repo.findByGameIdAndTimestampBetween(gameId, fromTs, toTs);
		} else if (fromTs != -1) {
			nots = repo.findByGameIdAndTimestampGreaterThan(gameId, fromTs);
		} else {
			nots = repo.findByGameIdAndTimestampLessThan(gameId, fromTs);
		}
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, long fromTs, long toTs, Pageable pageable) {
		List<NotificationPersistence> nots = null;
		if (fromTs != -1 && toTs != -1) {
			nots = repo.findByGameIdAndTimestampBetween(gameId, fromTs, toTs, pageable);
		} else if (fromTs != -1) {
			nots = repo.findByGameIdAndTimestampGreaterThan(gameId, fromTs, pageable);
		} else {
			nots = repo.findByGameIdAndTimestampLessThan(gameId, fromTs, pageable);
		}
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId) {
		List<NotificationPersistence> nots = repo.findByGameIdAndPlayerId(gameId, playerId);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId, Pageable pageable) {
		List<NotificationPersistence> nots = repo.findByGameIdAndPlayerId(gameId, playerId, pageable);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId, long fromTs, long toTs) {
		List<NotificationPersistence> nots = null;
		if (fromTs != -1 && toTs != -1) {
			nots = repo.findByGameIdAndPlayerIdAndTimestampBetween(gameId, playerId, fromTs, toTs);
		} else if (fromTs != -1) {
			nots = repo.findByGameIdAndPlayerIdAndTimestampGreaterThan(gameId, playerId, fromTs);
		} else {
			nots = repo.findByGameIdAndPlayerIdAndTimestampLessThan(gameId, playerId, fromTs);
		}
		return convert(nots);

	}

	public List<Notification> readNotifications(String gameId, String playerId, long fromTs, long toTs,
			Pageable pageable) {
		List<NotificationPersistence> nots = null;
		if (fromTs != -1 && toTs != -1) {
			nots = repo.findByGameIdAndPlayerIdAndTimestampBetween(gameId, playerId, fromTs, toTs, pageable);
		} else if (fromTs != -1) {
			nots = repo.findByGameIdAndPlayerIdAndTimestampGreaterThan(gameId, playerId, fromTs, pageable);
		} else {
			nots = repo.findByGameIdAndPlayerIdAndTimestampLessThan(gameId, playerId, fromTs, pageable);
		}
		return convert(nots);
	}

	private List<Notification> convert(List<NotificationPersistence> notifications) {
		List<Notification> result = new ArrayList<Notification>();
		for (NotificationPersistence not : notifications) {
			Notification n = not.toNotification();
			if (n != null) {
				result.add(n);
			}
		}
		return result;

	}
}
