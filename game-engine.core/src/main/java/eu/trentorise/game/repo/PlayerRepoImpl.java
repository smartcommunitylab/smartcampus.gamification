package eu.trentorise.game.repo;

import java.util.List;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.UncategorizedMongoDbException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.util.JSONParseException;

import eu.trentorise.game.model.core.SearchCriteria;
import eu.trentorise.game.model.core.SearchCriteria.QueryPart;
import eu.trentorise.game.model.core.SortItem;

public class PlayerRepoImpl implements ExtendPlayerRepo {

	private static final Logger logger = LoggerFactory
			.getLogger(PlayerRepoImpl.class);
	@Autowired
	private MongoTemplate mongo;

	private ObjectMapper mapper = new ObjectMapper();

	@Override
	public Page<StatePersistence> search(SearchCriteria searchCriteria, Pageable pageable) {

		if (searchCriteria == null) {
			searchCriteria = new SearchCriteria();
		}

		Query query = null;
		List<StatePersistence> result = null;

		try {
			if (searchCriteria.getRawQueryObj() != null) {
				query = new BasicQuery(mapper.writeValueAsString(searchCriteria
						.getRawQueryObj()));
			} else if (StringUtils.isNotBlank(searchCriteria.getRawQuery())) {
				query = new BasicQuery(searchCriteria.getRawQuery());
			} else if (searchCriteria.getQuery() != null) {
				List<QueryPart> queryParts = ListUtils
						.emptyIfNull(searchCriteria.getQuery());
				query = new BasicQuery(parseToString(queryParts));
			} else {
				query = new Query();

			}

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

			query.with(pageable);

			result = mongo.find(query, StatePersistence.class);
		} catch (JSONParseException | UncategorizedMongoDbException
				| MongoException | JsonProcessingException e) {
			logger.error("Exception running mongo query in search", e);
			throw new IllegalArgumentException("Query seems to be not valid");
		}
		long totalSize = mongo.count(query, StatePersistence.class);
		return new PageImpl<>(result, pageable, totalSize);
	}

	private String parseToString(List<QueryPart> queryParts) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		if (queryParts != null) {
			for (QueryPart part : queryParts) {
				String[] fieldTokens = part.getField().split(".");

				// necessary because actual persistence in mongodb uses
				// GenericObjectPersistence
				if (fieldTokens.length >= 4) {
					fieldTokens = (String[]) ArrayUtils.add(fieldTokens, 3,
							"obj");
				}
				for (int i = 0; i < fieldTokens.length; i++) {
					buffer.append(fieldTokens[i]);
					if (i < fieldTokens.length - 1) {
						buffer.append(".");
					}
				}
			}
		}
		return null;
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
