package eu.trentorise.game.repo;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.trentorise.game.notification.BadgeNotification;

public class NotificationRepoImpl implements ExtendedNotificationRepo {

	private static final String NOTIFICATION_PACKAGE = BadgeNotification.class.getPackage().getName();

	@Autowired
	private MongoTemplate mongo;

	@Override
	public List<NotificationPersistence> findGameNotificationsByQuery(String gameId,
			NotificationQuery notificationQuery, Pageable pageable) {

		Query query = new Query(createQueryStructure(gameId, notificationQuery));
		if (pageable != null) {
			query.with(pageable);
		}

		return mongo.find(query, NotificationPersistence.class);
	}

	@Override
	public List<NotificationPersistence> findPlayerNotificationsByQuery(String gameId, String playerId,
			NotificationQuery notificationQuery, Pageable pageable) {
		Criteria criteria = createQueryStructure(gameId, notificationQuery);
		criteria.and("obj.playerId").is(playerId);
		Query query = new Query(criteria);
		if (pageable != null) {
			query.with(pageable);
		}

		return mongo.find(query, NotificationPersistence.class);
	}

	private Criteria createQueryStructure(String gameId, NotificationQuery notificationQuery) {
		Criteria criteria = Criteria.where("obj.gameId").is(gameId);
		if (notificationQuery != null) {
			if (notificationQuery.getFromTs() > -1 && notificationQuery.getToTs() > -1) {
				criteria.and("obj.timestamp").gte(notificationQuery.getFromTs()).lt(notificationQuery.getToTs());
			} else if (notificationQuery.getToTs() > -1) {
				criteria.and("obj.timestamp").lt(notificationQuery.getToTs());
			} else if (notificationQuery.getFromTs() > -1) {
				criteria.and("obj.timestamp").gte(notificationQuery.getFromTs());
			}

			if (!CollectionUtils.isEmpty(notificationQuery.getIncludeTypes())) {
				criteria.and("type").in(processNotificationTypes(notificationQuery.getIncludeTypes()));
			} else if (!CollectionUtils.isEmpty(notificationQuery.getExcludeTypes())) {
				criteria.and("type").nin(processNotificationTypes(notificationQuery.getExcludeTypes()));
			}
		}

		return criteria;
	}

	private List<String> processNotificationTypes(List<String> types) {
		if (types != null) {
			return types.stream().filter(t -> t.indexOf(".") == -1).map(t -> NOTIFICATION_PACKAGE + "." + t)
					.collect(Collectors.toList());
		}
		return types;
	}

}
