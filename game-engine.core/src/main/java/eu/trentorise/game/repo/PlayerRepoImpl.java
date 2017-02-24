package eu.trentorise.game.repo;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.ListUtils;
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
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.util.JSONParseException;

import eu.trentorise.game.model.ChallengeConcept;
import eu.trentorise.game.model.core.ComplexSearchQuery;
import eu.trentorise.game.model.core.ComplexSearchQuery.SearchElement;
import eu.trentorise.game.model.core.RawSearchQuery;
import eu.trentorise.game.model.core.SearchQuery.SortItem;
import eu.trentorise.game.model.core.StringSearchQuery;

public class PlayerRepoImpl implements ExtendPlayerRepo {

	private static final Logger logger = LoggerFactory
			.getLogger(PlayerRepoImpl.class);
	@Autowired
	private MongoTemplate mongo;

	private ObjectMapper mapper = new ObjectMapper();

	private String addProjection(eu.trentorise.game.model.core.SearchQuery.Projection proj) {
		String result = null;
		if (proj != null) {
			List<String> projectionIncludeFields = ListUtils.emptyIfNull(proj
					.getIncludeFields());
			List<String> projectionExcludeFields = ListUtils.emptyIfNull(proj
					.getExcludeFields());
			StringBuffer buffer = new StringBuffer();
			buffer.append("{");

			for (String includeField : projectionIncludeFields) {
				if (buffer.length() > 1) {
					buffer.append(",");
				}
				buffer.append(String.format("\"%s\":1", includeField));
				// append type field when necessary to permit correct creation
				// of
				// instance of class concept
				String typeField = appendTypeField(includeField);
				if (typeField != null) {
					buffer.append(String.format(",\"%s\":1", typeField));
				}

			}

			for (String excludedField : projectionExcludeFields) {
				if (buffer.length() > 1) {
					buffer.append(",");
				}
				buffer.append(String.format("\"%s\":0", excludedField));
			}
			buffer.append("}");
			result = buffer.toString();
		}

		return result;
	}

	private String appendTypeField(String field) {
		if (field != null && field.contains(".obj.")) {
			return field.replaceFirst(".obj.*", ".type");
		}

		return null;

	}

	// private BasicQuery addProjection(BasicQuery query,
	// eu.trentorise.game.model.core.SearchQuery.Projection proj) {
	// if (proj != null) {
	// List<String> projectionIncludeFields = ListUtils.emptyIfNull(proj
	// .getIncludeFields());
	// List<String> projectionExcludeFields = ListUtils.emptyIfNull(proj
	// .getExcludeFields());
	//
	// for (String projField : projectionIncludeFields) {
	// query.fields().include(projField);
	// }
	// for (String projField : projectionExcludeFields) {
	// query.fields().exclude(projField);
	// }
	//
	// }
	// return query;
	// }

	/*
	 * TO REMOVE
	 */
	// private Query addProjection(Query query, Projection proj) {
	// if (proj != null) {
	// List<String> projectionIncludeFields = ListUtils.emptyIfNull(proj
	// .getIncludeFields());
	// List<String> projectionExcludeFields = ListUtils.emptyIfNull(proj
	// .getExcludeFields());
	//
	// for (String projField : projectionIncludeFields) {
	// query.fields().include(projField);
	// }
	// for (String projField : projectionExcludeFields) {
	// query.fields().exclude(projField);
	// }
	//
	// }
	// return query;
	// }

	// private Query addSort(Query query, List<SortItemOld> sortItems) {
	// if (sortItems != null) {
	// query.with(new Sort(createDataOders(sortItems)));
	// }
	//
	// return query;
	// }

	private Query addSort(Query query, List<SortItem> sortItems) {
		if (sortItems != null) {
			query.with(new Sort(createDataOders(sortItems)));
		}

		return query;
	}

	/*
	 * TO REMOVE
	 */
	// private String queryPartToStringSearch(String concept, List<QueryPart>
	// parts) {
	// StringBuffer buffer = new StringBuffer();
	// buffer.append("{");
	// if (parts != null) {
	// for (int i = 0; i < parts.size(); i++) {
	// QueryPart part = parts.get(i);
	// buffer.append(fieldQueryString(concept, part));
	// buffer.append(":").append(part.getClause().trim());
	// if (i < parts.size() - 1) {
	// buffer.append(",");
	// }
	// }
	// }
	// buffer.append("}");
	//
	// return buffer.toString();
	// }

	private String queryPartToString(String concept, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryPart> parts) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("{");
		if (parts != null) {
			for (int i = 0; i < parts.size(); i++) {
				eu.trentorise.game.model.core.ComplexSearchQuery.QueryPart part = parts
						.get(i);
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

	/*
	 * TO REMOVE
	 */
	// private String fieldQueryString(String concept, QueryPart queryPart) {
	// String queryString = null;
	// String field = queryPart.getConceptName();
	// if ("customData".equals(concept)) {
	// queryString = String.format("\"customData.%s\"", field.trim());
	// } else if ("pointConcept".equals(concept)) {
	// queryString = String.format("\"concepts.%s.%s.obj.score\"",
	// convertConceptToString(concept), field.trim());
	// } else if ("badgeCollectionConcept".equals(concept)) {
	// queryString = String.format("\"concepts.%s.%s.obj.badgeEarned\"",
	// convertConceptToString(concept), field.trim());
	// } else if ("periodicPointConcept".equals(concept)) {
	// queryString = String.format(
	// "\"concepts.%s.%s.obj.periods.%s.instances.%s.score\"",
	// convertConceptToString(concept), field.trim(), queryPart
	// .getPeriodName(),
	// new DateTime(queryPart.getInstanceDate())
	// .toString("YYYY-MM-dd'T'HH:mm:ss"));
	// } else if ("challengeConcept".equals(concept)) {
	// // if field is field or challenge-metadata
	// if (isMetaField(queryPart.getField())) {
	// queryString = "\"concepts.%s.%s.obj.%s\"";
	// } else {
	// queryString = "\"concepts.%s.%s.obj.fields.%s\"";
	// }
	//
	// queryString = String.format(queryString,
	// convertConceptToString(concept),
	// queryPart.getConceptName(), queryPart.getField());
	// }
	//
	// return queryString;
	// }

	private String fieldQueryString(String concept, eu.trentorise.game.model.core.ComplexSearchQuery.QueryPart queryPart) {
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

	// private Order[] createDataOders(List<SortItemOld> sortItems) {
	// Order[] orders = new Order[sortItems.size()];
	// int index = 0;
	// for (SortItemOld sortItem : sortItems) {
	// orders[index++] = new Order(Direction.fromString(sortItem
	// .getDirection().name()), sortItem.getField());
	// }
	// return orders;
	// }

	private Order[] createDataOders(List<SortItem> sortItems) {
		Order[] orders = new Order[sortItems.size()];
		int index = 0;
		for (SortItem sortItem : sortItems) {
			orders[index++] = new Order(Direction.fromString(sortItem
					.getDirection().name()), sortItem.getField());
		}
		return orders;
	}

	@Override
	public Page<StatePersistence> search(String gameId, RawSearchQuery query, Pageable pageable) {
		List<StatePersistence> result = null;
		BasicQuery q = null;
		String queryString = "{}";
		String projection = null;
		try {
			if (query != null) {
				if (query.getQuery() != null) {
					queryString = mapper.writeValueAsString(query.getQuery());
				}
				projection = addProjection(query.getProjection());
			}
			q = new BasicQuery(queryString, projection);
			q = (BasicQuery) addSort(q, query != null ? query.getSortItems()
					: null);
			q.with(pageable);

			if (gameId != null) {
				q.addCriteria(Criteria.where("gameId").is(gameId));
			}

			result = mongo.find(q, StatePersistence.class);

		} catch (JSONParseException | UncategorizedMongoDbException
				| MongoException | JsonProcessingException e) {
			exceptionHandler(e);
		}

		long totalSize = mongo.count(q, StatePersistence.class);
		return new PageImpl<>(result, pageable, totalSize);
	}

	@Override
	public Page<StatePersistence> search(String gameId, ComplexSearchQuery query, Pageable pageable) {
		List<StatePersistence> result = null;
		BasicQuery q = null;
		String queryString = "{}";
		String projection = null;
		StringBuffer buffer = new StringBuffer();
		if (query != null) {
			Map<String, List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryPart>> parts = query
					.getQuery();
			if (parts != null) {
				for (SearchElement element : ComplexSearchQuery.SearchElement
						.values()) {
					List<eu.trentorise.game.model.core.ComplexSearchQuery.QueryPart> queryParts = parts
							.get(element.getDisplayName());
					if (queryParts != null) {
						buffer.append(queryPartToString(
								element.getDisplayName(), queryParts));
					}
				}
				if (buffer.length() > 0) {
					queryString = buffer.toString();
				} else {
					throw new IllegalArgumentException(
							"Query seems to be not valid: searchElements not valid");
				}

			}

			projection = addProjection(query.getProjection());
		}
		q = new BasicQuery(queryString, projection);
		q = (BasicQuery) addSort(q, query != null ? query.getSortItems() : null);
		q.with(pageable);
		if (gameId != null) {
			q.addCriteria(Criteria.where("gameId").is(gameId));
		}
		try {
			result = mongo.find(q, StatePersistence.class);
		} catch (JSONParseException | UncategorizedMongoDbException
				| MongoException e) {
			exceptionHandler(e);
		}
		long totalSize = mongo.count(q, StatePersistence.class);
		return new PageImpl<>(result, pageable, totalSize);
	}

	private void exceptionHandler(Exception e) {
		logger.error("Exception running mongo query in search", e);
		throw new IllegalArgumentException("Query seems to be not valid");
	}

	@Override
	public Page<StatePersistence> search(String gameId, StringSearchQuery query, Pageable pageable) {
		List<StatePersistence> result = null;
		String queryString = "{}";
		if (query != null && query.getQuery() != null) {
			queryString = query.getQuery();
		}

		BasicQuery q = new BasicQuery(queryString);
		q.with(pageable);
		if (gameId != null) {
			q.addCriteria(Criteria.where("gameId").is(gameId));
		}
		try {
			result = mongo.find(q, StatePersistence.class);
		} catch (JSONParseException | UncategorizedMongoDbException
				| MongoException e) {
			exceptionHandler(e);
		}
		long totalSize = mongo.count(q, StatePersistence.class);
		return new PageImpl<>(result, pageable, totalSize);

	}

}
