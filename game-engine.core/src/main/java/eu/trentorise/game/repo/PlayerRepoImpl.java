package eu.trentorise.game.repo;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import eu.trentorise.game.model.core.SearchCriteria;
import eu.trentorise.game.model.core.SortItem;

public class PlayerRepoImpl implements ExtendPlayerRepo {

	@Autowired
	private MongoTemplate mongo;

	@Override
	public Page<StatePersistence> search(SearchCriteria searchCriteria,
			Pageable pageable) {

		Query query = new Query();
		if (searchCriteria != null) {
			if (searchCriteria.getProjection() != null) {
				List<String> projectionIncludeFields = ListUtils
						.emptyIfNull(searchCriteria.getProjection()
								.getIncludeFields());
				List<String> projectionExcludeFields = ListUtils
						.emptyIfNull(searchCriteria.getProjection()
								.getExcludeFields());

				for (String projField : projectionIncludeFields) {
					query.fields().include(projField);
				}
				for (String projField : projectionExcludeFields) {
					query.fields().exclude(projField);
				}

			}
			if (searchCriteria.getSortItems() != null) {
				query.with(new Sort(createDataOders(searchCriteria
						.getSortItems())));
			}
		}
		query.with(pageable);
		List<StatePersistence> result = mongo.find(query,
				StatePersistence.class);
		long totalSize = mongo.count(query, StatePersistence.class);
		return new PageImpl<>(result, pageable, totalSize);
	}

	private Order[] createDataOders(List<SortItem> sortItems) {
		Order[] orders = new Order[sortItems.size()];
		int index = 0;
		for (SortItem sortItem : sortItems) {
			orders[index++] = new Order(Direction.fromString(sortItem
					.getDirection().name()), sortItem.getField());
		}
		return orders;
	}
}
