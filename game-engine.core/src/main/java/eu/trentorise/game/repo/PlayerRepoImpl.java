package eu.trentorise.game.repo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
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

import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.core.SearchCriteria;
import eu.trentorise.game.model.core.SearchCriteria.QueryPart;
import eu.trentorise.game.model.core.SearchCriteria.SearchElement;
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
				StringBuffer buffer = new StringBuffer();
				Map<String, List<QueryPart>> q = searchCriteria.getQuery();
				for (SearchElement element : SearchCriteria.SearchElement
						.values()) {
					List<QueryPart> queryParts = q
							.get(element.getDisplayName());
					if (queryParts != null) {
						buffer.append(queryPartToString(
								element.getDisplayName(), queryParts));
					}
				}
				if (buffer.length() > 0) {
					query = new BasicQuery(buffer.toString());
				} else {
					throw new IllegalArgumentException(
							"Query seems to be not valid: searchElements not valid");
				}
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

	private String queryPartToString(String concept, List<QueryPart> parts) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		if (parts != null) {
			for (int i = 0; i < parts.size(); i++) {
				QueryPart part = parts.get(i);
				buffer.append(fieldQueryString(concept, part));
				buffer.append(":").append(part.getClause().trim());
				if (i < parts.size() - 1) {
					buffer.append(",");
				}
			}
		}
		buffer.append("}");

		return buffer.toString();
	}

	private String fieldQueryString(String concept, QueryPart queryPart) {
		String queryString = null;
		String field = queryPart.getConceptName();
		if ("customData".equals(concept)) {
			queryString = String.format("\"customData.%s\"", field.trim());
		} else if ("pointConcept".equals(concept)) {
			queryString = String.format("\"concepts.%s.%s.obj.score\"",
					convertConceptToString(concept), field.trim());
		} else if ("badgeCollectionConcept".equals(concept)) {
			queryString = String.format("\"concepts.%s.%s.obj.badgeEarned\"",
					convertConceptToString(concept), field.trim());
		} else if ("periodicPointConcept".equals(concept)) {
			queryString = String.format(
					"\"concepts.%s.%s.obj.periods.%s.instances.%s.score\"",
					convertConceptToString(concept), field.trim(), queryPart
							.getPeriodName(),
					new DateTime(queryPart.getInstanceDate())
							.toString("YYYY-MM-dd'T'HH:mm:ss"));
		} else if ("challengeConcept".equals(concept)) {
			// if field is field or challenge-metadata
			if (isMetaField(queryPart.getField())) {
				queryString = "\"concepts.%s.%s.obj.%s\"";
			} else {
				queryString = "\"concepts.%s.%s.obj.fields.%s\"";
			}

			queryString = String.format(queryString,
					convertConceptToString(concept),
					queryPart.getConceptName(), queryPart.getField());
		}

		return queryString;
	}

	private boolean isMetaField(String field) {
		Field[] classVars = ChallengeConcept.class.getDeclaredFields();
		boolean isMetaField = false;
		for (Field classVar : classVars) {
			if (isMetaField = field.equals(classVar.getName())) {
				break;
			}
		}

		return isMetaField;
	}

	private String convertConceptToString(String concept) {
		String result = null;
		switch (concept) {
		case "pointConcept":
			result = "PointConcept";
			break;
		case "badgeCollectionConcept":
			result = "BadgeCollectionConcept";
			break;
		case "periodicPointConcept":
			result = "PointConcept";
			break;
		case "challengeConcept":
			result = "ChallengeConcept";
			break;
		case "customData":
			result = "customData";
			break;
		default:
			break;
		}

		return result;
	}

	private String parseToString(List<QueryPart> queryParts) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		if (queryParts != null) {
			for (QueryPart part : queryParts) {
				String[] fieldTokens = part.getConceptName().split(".");

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
