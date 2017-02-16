package eu.trentorise.game.model.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class ComplexSearchQuery extends SearchQuery {
	private Map<String, List<QueryPart>> query;

	public ComplexSearchQuery(List<SortItem> sortItems, Projection projection,
			Map<String, List<QueryPart>> query) {
		super(sortItems, projection);
		this.query = query;
	}

	public Map<String, List<QueryPart>> getQuery() {
		return query;
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

}
