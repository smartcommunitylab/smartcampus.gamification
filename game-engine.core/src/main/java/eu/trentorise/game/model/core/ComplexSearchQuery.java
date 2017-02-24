package eu.trentorise.game.model.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ComplexSearchQuery extends SearchQuery {
	private Map<String, List<QueryPart>> query;

	@JsonCreator
	public ComplexSearchQuery(
			@JsonProperty("query") Map<String, List<QueryPart>> query,
			@JsonProperty("projection") Projection projection,
			@JsonProperty("sortItems") List<SortItem> sortItems) {
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

		/*
		 * Jackson permits to have only one annotation JsonCreator per Class so
		 * this private Constructor is used by jackson deserializer
		 */
		@JsonCreator
		@SuppressWarnings("unused")
		private QueryPart(@JsonProperty("conceptName") String conceptName,
				@JsonProperty("periodName") String periodName,
				@JsonProperty("instanceDate") Date instanceDate,
				@JsonProperty("clause") String clause,
				@JsonProperty("field") String field) {
			this.conceptName = conceptName;
			this.periodName = periodName;
			this.instanceDate = instanceDate;
			this.clause = clause;
			this.field = field;
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
