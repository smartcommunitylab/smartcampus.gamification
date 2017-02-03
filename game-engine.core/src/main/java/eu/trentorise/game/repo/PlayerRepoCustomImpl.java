package eu.trentorise.game.repo;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

public class PlayerRepoCustomImpl implements PlayerRepoCustom {

	@Autowired
	private MongoTemplate mongo;

	@Override
	public void customMethod(List<String> projectionFields) {
		projectionFields = ListUtils.emptyIfNull(projectionFields);
		Query query = new Query();
		if (!projectionFields.isEmpty()) {
			for (String projField : projectionFields) {
				query.fields().include(projField);
			}
		}

		mongo.find(query, StatePersistence.class);
	}
}
