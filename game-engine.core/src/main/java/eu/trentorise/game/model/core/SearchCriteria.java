package eu.trentorise.game.model.core;

import java.util.List;
import java.util.Map;

public class SearchCriteria {
	private Map<String, Object> rawQueryObj;
	private String rawQuery;
	private List<QueryPart> query;
	private List<SortItem> sortItems;
	private Projection projection;

	public SearchCriteria(Map<String, Object> rawQuery, List<QueryPart> query,
			List<SortItem> sortItems, Projection projection) {
		this.rawQueryObj = rawQuery;
		this.query = query;
		this.sortItems = sortItems;
		this.projection = projection;
	}

	public SearchCriteria(String rawQuery, List<QueryPart> query,
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

	public static class QueryPart {
		private String field;
		private String clause;

		public QueryPart(String field, String clause) {
			this.field = field;
			this.clause = clause;
		}

		public String getField() {
			return field;
		}

		public String getClause() {
			return clause;
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

	public List<QueryPart> getQuery() {
		return query;
	}

	public String getRawQuery() {
		return rawQuery;
	}

	public Map<String, Object> getRawQueryObj() {
		return rawQueryObj;
	}

}
