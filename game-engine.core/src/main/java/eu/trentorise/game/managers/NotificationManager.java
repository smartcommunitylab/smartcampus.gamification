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
import eu.trentorise.game.repo.NotificationQuery;
import eu.trentorise.game.repo.NotificationRepo;

@Component
public class NotificationManager {

	@Autowired
	NotificationRepo repo;
	
	@Autowired
	private RabbitMQManager rabbitMQManager;

	public void notificate(Notification n) {
		NotificationPersistence np = new NotificationPersistence(n); 
		repo.save(np);
		
		rabbitMQManager.sendMessage(np);
	}

	public List<Notification> readNotifications(String gameId) {
		List<NotificationPersistence> nots = repo.findGameNotificationsByQuery(gameId, null, null);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, Pageable pageable) {
		List<NotificationPersistence> nots = repo.findGameNotificationsByQuery(gameId, null, pageable);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, long fromTs, long toTs) {
		return readNotifications(gameId, fromTs, toTs, null);
	}

	public List<Notification> readNotifications(String gameId, long fromTs, long toTs, Pageable pageable) {
		NotificationQuery query = new NotificationQuery();
		query.setFromTs(fromTs);
		query.setToTs(toTs);
		List<NotificationPersistence> nots = repo.findGameNotificationsByQuery(gameId, query, pageable);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId) {
		return readNotifications(gameId, playerId, null);
	}

	public List<Notification> readNotifications(String gameId, String playerId, Pageable pageable) {
		List<NotificationPersistence> nots = repo.findPlayerNotificationsByQuery(gameId, playerId, null, pageable);
		return convert(nots);
	}

	public List<Notification> readNotificationsWithIncludedTypes(String gameId, String playerId,
			List<String> includeNotificationTypes, Pageable pageable) {

		NotificationQuery query = new NotificationQuery();
		query.setIncludeTypes(includeNotificationTypes);
		List<NotificationPersistence> nots = repo.findPlayerNotificationsByQuery(gameId, playerId, query, pageable);
		return convert(nots);
	}

	public List<Notification> readNotificationsWithExcludedTypes(String gameId, String playerId,
			List<String> excludeNotificationTypes, Pageable pageable) {
		NotificationQuery query = new NotificationQuery();
		query.setExcludeTypes(excludeNotificationTypes);
		List<NotificationPersistence> nots = repo.findPlayerNotificationsByQuery(gameId, playerId, query, pageable);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId, long fromTs, long toTs) {
		return readNotifications(gameId, playerId, fromTs, toTs, null, null, null);

	}

	public List<Notification> readNotifications(String gameId, long fromTs, long toTs, List<String> includeTypes,
			List<String> excludeTypes, Pageable pageable) {

		NotificationQuery query = new NotificationQuery();
		query.setFromTs(fromTs);
		query.setToTs(toTs);
		query.setIncludeTypes(includeTypes);
		query.setExcludeTypes(excludeTypes);

		List<NotificationPersistence> nots = repo.findGameNotificationsByQuery(gameId, query, pageable);
		return convert(nots);
	}

	public List<Notification> readNotifications(String gameId, String playerId, long fromTs, long toTs,
			List<String> includeTypes, List<String> excludeTypes, Pageable pageable) {

		NotificationQuery query = new NotificationQuery();
		query.setFromTs(fromTs);
		query.setToTs(toTs);
		query.setIncludeTypes(includeTypes);
		query.setExcludeTypes(excludeTypes);

		List<NotificationPersistence> nots = repo.findPlayerNotificationsByQuery(gameId, playerId, query, pageable);
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
