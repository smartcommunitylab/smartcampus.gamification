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
	public List<StatePersistence> search(List<String> projectionFields) {
		projectionFields = ListUtils.emptyIfNull(projectionFields);
		Query query = new Query();
		if (!projectionFields.isEmpty()) {
			for (String projField : projectionFields) {
				query.fields().include(projField);
			}
		}

		return mongo.find(query, StatePersistence.class);
	}
}
