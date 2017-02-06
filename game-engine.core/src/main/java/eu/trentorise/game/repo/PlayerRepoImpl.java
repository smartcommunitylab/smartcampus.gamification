package eu.trentorise.game.repo;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class PlayerRepoImpl implements ExtendPlayerRepo {

	@Autowired
	private MongoTemplate mongo;

	@Override
	public List<StatePersistence> search(List<String> projectionIncludeFields,
			List<String> projectionExcludeFields) {
		projectionIncludeFields = ListUtils
				.emptyIfNull(projectionIncludeFields);
		projectionExcludeFields = ListUtils
				.emptyIfNull(projectionExcludeFields);
		Query query = new Query();
		for (String projField : projectionIncludeFields) {
			query.fields().include(projField);
		}
		for (String projField : projectionExcludeFields) {
			query.fields().exclude(projField);
		}

		return mongo.find(query, StatePersistence.class);
	}
}
