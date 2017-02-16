package eu.trentorise.game.model.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class SearchCriteria {
	private Map<String, Object> rawQueryObj;
	private String rawQuery;
	private Map<String, List<QueryPart>> query;
	private List<SortItem> sortItems;
	private Projection projection;

	public SearchCriteria(Map<String, Object> rawQuery,
			Map<String, List<QueryPart>> query, List<SortItem> sortItems,
			Projection projection) {
		this.rawQueryObj = rawQuery;
		this.query = query;
		this.sortItems = sortItems;
		this.projection = projection;
	}

	public SearchCriteria(String rawQuery, Map<String, List<QueryPart>> query,
			List<SortItem> sortItems, Projection projection) {
		this.rawQuery = rawQuery;
		this.query = query;
		this.sortItems = sortItems;
		this.projection = projection;
	}

	public SearchCriteria() {
	}

	public List<SortItem> getSortItems() {
		return sortItems;
	}

	public Projection getProjection() {
		return projection;
	}

	public enum SearchElement {
		POINT_CONCEPT("pointConcept"), PERIODIC_POINT_CONCEPT(
				"periodicPointConcept"), BADGE_COLLECTION_CONCEPT(
				"badgeCollectionConcept"), CHALLENGE_CONCEPT("challengeConcept"), CUSTOM_DATA(
				"customData");

		private String displayName;

		private SearchElement(String name) {
			displayName = name;
		}

		public String getDisplayName() {
			return displayName;
		}

		@Override
		public String toString() {
			return displayName;
		}

	}

	public static class QueryPart {
		private String conceptName;
		private String periodName;
		private Date instanceDate;
		private String clause;
		private String field;

		public QueryPart(String conceptName, String periodName,
				Date instanceDate, String clause) {
			this.conceptName = conceptName;
			this.periodName = periodName;
			this.instanceDate = instanceDate;
			this.clause = clause;
		}

		public QueryPart(String conceptName, String clause) {
			this.conceptName = conceptName;
			this.clause = clause;
		}

		public QueryPart(String conceptName, String field, String clause) {
			this.conceptName = conceptName;
			this.clause = clause;
			this.field = field;
		}

		public String getConceptName() {
			return conceptName;
		}

		public String getClause() {
			return clause;
		}

		public String getPeriodName() {
			return periodName;
		}

		public Date getInstanceDate() {
			return instanceDate;
		}

		public String getField() {
			return field;
		}
	}

	public static class Projection {
		private List<String> include;
		private List<String> exclude;

		public Projection(List<String> include, List<String> exclude) {
			this.include = include;
			this.exclude = exclude;
		}

		public List<String> getIncludeFields() {
			return include;
		}

		public List<String> getExcludeFields() {
			return exclude;
		}
	}

	public Map<String, List<QueryPart>> getQuery() {
		return query;
	}

	public String getRawQuery() {
		return rawQuery;
	}

	public Map<String, Object> getRawQueryObj() {
		return rawQueryObj;
	}

}
